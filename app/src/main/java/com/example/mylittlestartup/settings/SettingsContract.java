package com.example.mylittlestartup.settings;

import android.content.Context;
import android.widget.ToggleButton;

public interface SettingsContract {

    interface View {
        void showAppInformation();
        void showContactsInformation();
        void setMusicSoundButtonStateOn();
        void musicSoundOff();
        void musicSoundOn();
        Context getAppContext();
    }

    interface Presenter {
        void onSoundsSettingButtonClicked(ToggleButton button);  // possible to implement differently
        void checkMusicSoundState();
        void onLogoutButtonClicked();
        void onFeedbackButtonClicked();
        void onAboutAppButtonClicked();
    }

    // TODO logout in repository

}
