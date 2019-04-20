package com.example.mylittlestartup.settings;

public interface SettingsContract {

    interface View {
        void showAppInformation();
        void showContactsInformation();
    }

    interface Presenter {
        void onSoundsSettingButtonClicked();  // possible to implement differently
        void onLogoutButtonClicked();
        void onFeedbackButtonClicked();
        void onAboutAppButtonClicked();
    }

    // TODO logout in repository

}
