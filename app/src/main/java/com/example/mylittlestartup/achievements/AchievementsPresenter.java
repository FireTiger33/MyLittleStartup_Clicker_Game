package com.example.mylittlestartup.achievements;

import com.example.mylittlestartup.data.AchievementsRepositoryImpl;

import com.example.mylittlestartup.data.sqlite.Achievement;


import java.util.List;

public class AchievementsPresenter implements AchievementsContract.Presenter {
    private AchievementsContract.View mView;
    private AchievementsContract.Repository mRepository;

    public AchievementsPresenter(AchievementsContract.View view) {
        mView = view;
        mRepository = new AchievementsRepositoryImpl(view.getAppContext());
    }

    @Override
    public void fetchAchievements() {
        mRepository.getAchievements(new AchievementsContract.Repository.AchievementCallback() {
            @Override
            public void onSuccess(List<Achievement> achievements) {
                mView.showAchievements(achievements);
            }

            @Override
            public void onError() {
            }
        });
    }
}
