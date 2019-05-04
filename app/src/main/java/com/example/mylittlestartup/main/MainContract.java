package com.example.mylittlestartup.main;

import com.example.mylittlestartup.data.BaseCallback;

public interface MainContract {

    interface View {
        void hideAuthorizationButtons();
        void showScreenGame();
        void showLoginScreen();
        void showSignUpScreen();
        void showSettingsScreen();
        void showAchievementsScreen();
    }

    interface Presenter {
        void onStartGameButtonClicked();
        void onLoginButtonClicked();
        void onSignUpButtonClicked();
        void onSettingsButtonClicked();
        void onAchievementsButtonClicked();
    }

    interface Repository {
        void wasAuthorized(final BaseCallback callback);
    }

}
