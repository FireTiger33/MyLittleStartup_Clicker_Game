package com.example.mylittlestartup.data.api;

import android.content.Context;

import com.example.mylittlestartup.ClickerApplication;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiRepository {
    private final UserApi mUserApi;
    private final SessionApi mSessionApi;
    private final GameApi mGameApi;

    private final OkHttpClient mOkHttpClient;

    public ApiRepository(Context context) {
        CookieJar cookieJar = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .cookieJar(cookieJar)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        // todo https
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(new HttpUrl.Builder().scheme("http")
                        .host("185.5.248.128").port(3000)
                        .build())
                .client(mOkHttpClient)
                .build();

        mUserApi = retrofit.create(UserApi.class);
        mSessionApi = retrofit.create(SessionApi.class);
        mGameApi = retrofit.create(GameApi.class);
    }

    public static ApiRepository from(Context context) {
        return ClickerApplication.from(context).getApis();
    }

    public UserApi getUserApi() {
        return mUserApi;
    }

    public SessionApi getSessionApi() {
        return mSessionApi;
    }

    public GameApi getGameApi() {
        return mGameApi;
    }
}

