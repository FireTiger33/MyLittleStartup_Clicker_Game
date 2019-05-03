package com.example.mylittlestartup.main;


public class MainPresenter implements MainContract.Presenter{

    private MainContract.View view;

    MainPresenter(MainContract.View view) {
        this.view = view;
        // TODO check logged, if true call hideAuthorizationButtons
    }

    @Override
    public void onStartGameButtonClicked() {
        view.showScreenGame();
    }

    @Override
    public void onLoginButtonClicked() {
        view.showLoginScreen();
    }

    @Override
    public void onSignUpButtonClicked() {
        view.showSignUpScreen();
    }

    @Override
    public void onSettingsButtonClicked() {
        view.showSettingsScreen();
    }

    @Override
    public void onAchievementsButtonClicked() {
        view.showAchievementsScreen();
    }

}
