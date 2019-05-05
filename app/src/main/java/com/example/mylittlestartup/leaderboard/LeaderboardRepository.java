package com.example.mylittlestartup.leaderboard;

import com.example.mylittlestartup.data.api.UserApi;

import java.util.List;

public interface LeaderboardRepository {
    interface LeaderboardCallback {
        void onSuccess(List<UserApi.User> data);

        void onError();
    }

    void getLeaderboard(LeaderboardCallback callback);
}
