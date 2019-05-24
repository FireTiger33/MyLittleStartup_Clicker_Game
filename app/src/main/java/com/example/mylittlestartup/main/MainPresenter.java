package com.example.mylittlestartup.main;

import android.util.Log;

import com.example.mylittlestartup.data.BaseCallback;


public class MainPresenter implements MainContract.Presenter {
    private final String tag = MainPresenter.class.getName();

    private MainContract.View mView;
    private MainContract.Repository mRepository;


    MainPresenter(MainContract.View view, MainContract.Repository repository) {
        this.mView = view;
        this.mRepository = repository;
    }


    @Override
    public void onStartGameButtonClicked() {
        mView.showScreenGame();
    }

    @Override
    public void onLoginButtonClicked() {
        mView.showLoginScreen();
    }

    @Override
    public void onSignUpButtonClicked() {
        mView.showSignUpScreen();
    }

    @Override
    public void onSettingsButtonClicked() {
        mView.showSettingsScreen();
    }

    @Override
    public void onAchievementsButtonClicked() {
        mView.showAchievementsScreen();
    }

    @Override
    public void onLeaderboardButtonClicked() {
        mView.showLeaderboardScreen();
    }

    @Override
    public void checkIsLoggedIn() {
        mRepository.wasAuthorized(new BaseCallback() {
            @Override
            public void onSuccess() {
                mView.hideAuthorizationButtons();
            }

            @Override
            public void onError() {
                // todo idk maybe highlight singup/login?
                String hello  = "hello";
            }
        });
    }
}
