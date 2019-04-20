package com.example.mylittlestartup.main;

public interface MainContract {

    interface View {
        void hideAuthorizationButtons();
        void showScreenGame();
        void showLoginScreen();
        void showSignUpScreen();
        void showSettingsScreen();
        void showAchievementsView();
    }

    interface Presenter {
        void onStartGameButtonClicked();
        void onLoginButtonClicked();
        void onSignUpButtonClicked();
        void onSettingsButtonClicked();
        void onAchievementsButtonClicked();
    }

    interface Repository {
        void wasAuthorized();
    }

}
