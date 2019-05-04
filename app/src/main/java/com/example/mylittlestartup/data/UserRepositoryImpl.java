package com.example.mylittlestartup.data;

import com.example.mylittlestartup.data.api.SessionApi;
import com.example.mylittlestartup.data.api.UserApi;
import com.example.mylittlestartup.data.executors.AppExecutors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class UserRepositoryImpl implements UserRepository {
    private UserApi mUserApi;
    private SessionApi mSessionApi;

    public UserRepositoryImpl(UserApi userApi, SessionApi sessionApi) {
        mUserApi = userApi;
        mSessionApi = sessionApi;
    }

    @Override
    public void authorize(String login, String pass, final BaseCallback authorizeCallback) {
        mSessionApi.login(new UserApi.User(login, pass)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, final Response<Void> response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            authorizeCallback.onSuccess();
                        } else {
                            authorizeCallback.onError();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        authorizeCallback.onError();
                    }
                });
            }
        });
    }

    @Override
    public void register(String login, String pass, final BaseCallback authorizeCallback) {

        mUserApi.register(new UserApi.User(login, pass)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, final Response<Void> response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            authorizeCallback.onSuccess();
                        } else {
                            authorizeCallback.onError();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        authorizeCallback.onError();
                    }
                });
            }
        });

    }

    @Override
    public void checkAuthorize(final BaseCallback authorizeCallback) {
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
