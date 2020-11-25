package com.example.mylittlestartup.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;

import com.example.mylittlestartup.AppActions;
import com.example.mylittlestartup.R;

public class SettingsView extends Fragment implements SettingsContract.View {
    String tag = SettingsView.class.getName();

    SettingsContract.Presenter presenter;

    private ToggleButton soundButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new SettingsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        soundButton = view.findViewById(R.id.sound_button);
        soundButton.setBackgroundResource(R.drawable.speaker_off/*R.color.holo_red_dark*/);
        presenter.checkMusicSoundState();
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSoundsSettingButtonClicked(soundButton);
            }
        });
        
        return view;
    }

    @Override
    public void showAppInformation() {
        // TODO
    }

    @Override
    public void showContactsInformation() {
        // TODO
    }

    @Override
    public void setMusicSoundButtonStateOn() {
        soundButton.setChecked(true);
        soundButton.setBackgroundResource(R.drawable.speaker_on/*R.color.holo_green_dark*/);
    }

    @Override
    public void musicSoundOff() {
        soundButton.setBackgroundResource(R.drawable.speaker_off/*R.color.holo_red_dark*/);

        AppActions actions = (AppActions) getActivity();
        if (actions != null) {
            actions.musicSoundOff();
        } else {
            Log.d(tag, "This activity is not a AppActions");
        }
    }

    @Override
    public void musicSoundOn() {
        soundButton.setBackgroundResource(R.drawable.speaker_on/*R.color.holo_green_dark*/);

        AppActions actions = (AppActions) getActivity();
        if (actions != null) {
            actions.musicSoundOn();
        } else {
            Log.d(tag, "This activity is not a AppActions");
        }
    }

    @Override
    public Context getAppContext() {
        return getContext().getApplicationContext();
    }


}
