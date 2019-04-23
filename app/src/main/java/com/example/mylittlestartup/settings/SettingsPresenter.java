package com.example.mylittlestartup.settings;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View view;

    public SettingsPresenter(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void onSoundsSettingButtonClicked() {
        // TODO
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
