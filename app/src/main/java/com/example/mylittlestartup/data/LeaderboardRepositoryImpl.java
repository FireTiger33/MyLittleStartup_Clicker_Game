package com.example.mylittlestartup.data;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.api.UserApi;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.leaderboard.LeaderboardContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaderboardRepositoryImpl implements LeaderboardContract.Repository {
    private UserApi mUserApi;

    public LeaderboardRepositoryImpl(@NonNull Context context) {
        mUserApi = ApiRepository.from(context).getUserApi();
    }

    @Override
    public void fetchLeaderboard(final LeaderboardCallback callback) {
        mUserApi.getAll().enqueue(new Callback<List<UserApi.UserPlain>>() {
            @Override
            public void onResponse(Call<List<UserApi.UserPlain>> call, final Response<List<UserApi.UserPlain>> response) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (response.code() == 200) {
                            List<UserApi.UserPlain> responseUsers = response.body();
                            Collections.sort(responseUsers, new UserApi.SortByScore());
                            
                            callback.onSuccess(responseUsers);
                        } else {
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserApi.UserPlain>> call, Throwable t) {
                callback.onError();
            }
        });
    }
}
