package com.example.mylittlestartup.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mylittlestartup.ClickerApplication;
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
    private SessionApi mSessionApi;
    private PlayerRepository mPlayerRepository;

    public UserRepositoryImpl(@NonNull Context context) {
        mUserApi = ApiRepository.from(context).getUserApi();
        mSessionApi = ApiRepository.from(context).getSessionApi();
        mPlayerRepository = ClickerApplication.from(context).getPlayerRepository();
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
        mPlayerRepository.isLoggedIn(new BaseCallback() {
            @Override
            public void onSuccess() {
                authorizeCallback.onSuccess();
            }

            @Override
            public void onError() {
                mSessionApi.checkLogin().enqueue(new Callback<SessionApi.SessionResponse>() {
                    @Override
                    public void onResponse(Call<SessionApi.SessionResponse> call, final Response<SessionApi.SessionResponse> response) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (response.code() == 200) {
                                    mPlayerRepository.setLoggedIn(new BaseCallback() {
                                        @Override
                                        public void onSuccess() {
                                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    authorizeCallback.onSuccess();
                                                }
                                            });
                                        }

                                        @Override
                                        public void onError() {
                                            // impossible
                                        }
                                    });
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
        });


    }
}
