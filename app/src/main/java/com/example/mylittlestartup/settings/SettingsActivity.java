package com.example.mylittlestartup.settings;

import android.app.Activity;
import android.os.Bundle;

import com.example.mylittlestartup.R;

public class SettingsActivity extends Activity implements SettingsContract.View {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // TODO
    }

    @Override
    public void showAppInformation() {
        // TODO
    }

    @Override
    public void showContactsInformation() {
        // TODO
    }

}
