package com.example.mylittlestartup.main;


import android.media.MediaPlayer;
import android.util.Log;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.PlayerRepository;


public class MainPresenter implements MainContract.Presenter {
    String tag = MainPresenter.class.getName();

    private MainContract.View mView;
    private MainContract.Repository mRepository;

    private MediaPlayer player;

    MainPresenter(MainContract.View view, MainContract.Repository repository) {
        this.mView = view;
        this.mRepository = repository;
    }


    private boolean isMusicSoundState() {
        PlayerRepository playerRepository = ClickerApplication.from(mView.getAppContext()).getPlayerRepository();
        return playerRepository.isMusicSoundState();
    }

    private void musicOn() {
        player = MediaPlayer.create(mView.getViewContext(), R.raw.main_sound_128kbit);
        player.start();
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
        Log.d(tag, "onViewShowed");
        if (player == null) {
            if (isMusicSoundState()) {
                musicOn();
            }
        } else {
            player.start();
        }
    }

    @Override
    public void onViewClosed() {
        if (player != null) {
            player.pause();
        }
    }

}
