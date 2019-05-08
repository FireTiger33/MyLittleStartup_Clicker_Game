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
        void showLeaderboardScreen();
    }

    interface Presenter {
        void onStartGameButtonClicked();
        void onLoginButtonClicked();
        void onSignUpButtonClicked();
        void onSettingsButtonClicked();
        void onAchievementsButtonClicked();
        void onLeaderboardButtonClicked();
        void checkIsLoggedIn();
    }

    interface Repository {
        void wasAuthorized(final BaseCallback callback);
    }

}
