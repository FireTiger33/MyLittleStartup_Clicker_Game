package com.example.mylittlestartup.leaderboard;

import android.util.Log;

import com.example.mylittlestartup.data.LeaderboardRepositoryImpl;
import com.example.mylittlestartup.data.api.UserApi;

import java.util.List;

class LeaderboardPresenter implements LeaderboardContract.Presenter {
    private LeaderboardContract.View mView;
    private LeaderboardContract.Repository mRepository;

    public LeaderboardPresenter(LeaderboardContract.View view) {
        mView = view;
        mRepository = new LeaderboardRepositoryImpl(view.getAppContext());
    }

    @Override
    public void fetchLeaderboard() {
        mRepository.fetchLeaderboard(new LeaderboardContract.Repository.LeaderboardCallback() {
            @Override
            public void onSuccess(List<UserApi.UserPlain> leaderboardUsers) {
                mView.showLeaderboardList(leaderboardUsers);
            }

            @Override
            public void onError() {
                mView.showConnectionError();
            }
        });
    }
}
