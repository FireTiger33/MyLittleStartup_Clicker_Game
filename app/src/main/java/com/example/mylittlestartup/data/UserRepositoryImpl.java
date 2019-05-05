package com.example.mylittlestartup.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mylittlestartup.authorization.AuthContract;
import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.api.SessionApi;
import com.example.mylittlestartup.data.api.UserApi;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.main.MainContract;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepositoryImpl implements AuthContract.Repository, MainContract.Repository {
    private UserApi mUserApi;
    private Context mContext;
    private SessionApi mSessionApi;

    public UserRepositoryImpl(@NonNull Context context) {
        mContext = context;

        mUserApi = ApiRepository.from(mContext).getUserApi();
        mSessionApi = ApiRepository.from(mContext).getSessionApi();
    }

    @Override
    public void registerNewUser(String login, String pass, final ValidationCallback callback) {

        mUserApi.register(new UserApi.User(login, pass)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, final Response<Void> response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        });
    }

    @Override
    public void authUser(String login, String pass, final ValidationCallback callback) {
        mSessionApi.login(new UserApi.User(login, pass)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, final Response<Void> response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        });

    }

    @Override
    public void wasAuthorized(final BaseCallback authorizeCallback) {
        mSessionApi.checkLogin().enqueue(new Callback<SessionApi.SessionResponse>() {
            @Override
            public void onResponse(Call<SessionApi.SessionResponse> call, final Response<SessionApi.SessionResponse> response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            SessionApi.SessionResponse body = response.body();
                            // todo put to shared preference id
                            authorizeCallback.onSuccess();
                        } else {
                            authorizeCallback.onError();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<SessionApi.SessionResponse> call, Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        authorizeCallback.onError();
                    }
                });
            }
        });
    }
}
