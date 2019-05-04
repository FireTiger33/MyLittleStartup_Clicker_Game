package com.example.mylittlestartup.data.api;

import android.content.Context;

import com.example.mylittlestartup.ClickerApplication;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ApiRepository {
    private final UserApi mUserApi;
    private final SessionApi mSessionApi;

    private final OkHttpClient mOkHttpClient;

    public ApiRepository() {
        mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .build();

        // todo вытащить из shared pref куки и сунуть в клиент

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(new HttpUrl.Builder().scheme("http")
                        .host("185.5.248.128").port(3000)
                        .build())
                .client(mOkHttpClient)
                .build();

        mUserApi = retrofit.create(UserApi.class);
        mSessionApi = retrofit.create(SessionApi.class);
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
}

