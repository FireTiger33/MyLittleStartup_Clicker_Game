package com.example.mylittlestartup.achievements;

import com.example.mylittlestartup.data.sqlite.Achievement;

import java.util.List;

public interface AchievementsContract {
    interface View {
        void showAchievementMoreInfo();
    }

    interface Presenter {
        void onAchievementClicked();
    }

    interface Repository{
        interface AchievementCallback {
            void onSuccess(List<Achievement> achievementList);

            void onError();
        }

        void getAchievements(AchievementCallback callback);
    }
}
