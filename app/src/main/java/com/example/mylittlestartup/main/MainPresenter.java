package com.example.mylittlestartup.main;


import android.media.MediaPlayer;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.BaseCallback;


public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private MainContract.Repository mRepository;

    private MediaPlayer player;

    MainPresenter(MainContract.View view, MainContract.Repository repository) {
        this.mView = view;
        this.mRepository = repository;
        player = MediaPlayer.create(view.getViewContext(), R.raw.main_sound_128kbit);
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

    @Override
    public void onViewShowed() {
        player.start();
    }

    @Override
    public void onViewClosed() {
        player.pause();
    }

}
