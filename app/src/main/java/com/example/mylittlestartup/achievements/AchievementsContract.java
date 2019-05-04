package com.example.mylittlestartup.achievements;

public interface AchievementsContract {
    interface View {
        void showAchievementMoreInfo();
    }

    interface Presenter {
        void onAchievementClicked();
    }

    interface Repository{
        void checkAchievementRelevations();
    }
}
