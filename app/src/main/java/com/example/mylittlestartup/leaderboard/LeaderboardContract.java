package com.example.mylittlestartup.leaderboard;

import android.content.Context;

import com.example.mylittlestartup.data.api.UserApi;

import java.util.List;

public interface LeaderboardContract {
    interface View {
        void showLeaderboardList(List<UserApi.UserPlain> leaderboardUsers);
        void showConnectionError();
        Context getAppContext();
    }


    interface Presenter {
        void fetchLeaderboard();
    }

    interface Repository {
        interface LeaderboardCallback {
            void onSuccess(List<UserApi.UserPlain> data);

            void onError();
        }

        /**
         * Get all users in score order
         *
         *
         * @param callback
         */
        void fetchLeaderboard(LeaderboardCallback callback);
    }
}
