package com.example.mylittlestartup.settings;

import android.util.Log;
import android.widget.ToggleButton;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.data.PlayerRepository;

public class SettingsPresenter implements SettingsContract.Presenter {
    String tag = SettingsPresenter.class.getName();

    private SettingsContract.View view;

    public SettingsPresenter(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void onSoundsSettingButtonClicked(ToggleButton button) {
        if (button.isChecked()) {
            view.musicSoundOn();
        } else {
            view.musicSoundOff();
        }
    }

    @Override
    public void checkMusicSoundState() {
        PlayerRepository playerRepository = ClickerApplication.from(view.getAppContext()).getPlayerRepository();
        if (playerRepository.isMusicSoundState()) {
            view.setMusicSoundButtonStateOn();
        }
    }

    @Override
    public void onLogoutButtonClicked() {
        // TODO
    }

    @Override
    public void onFeedbackButtonClicked() {
        // TODO
    }

    @Override
    public void onAboutAppButtonClicked() {
        // TODO
    }
}
