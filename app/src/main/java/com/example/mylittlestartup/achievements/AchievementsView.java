package com.example.mylittlestartup.achievements;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylittlestartup.R;

public class AchievementsView extends Fragment implements AchievementsContract.View {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.achievements_container, container, false);

        return view;
    }

    @Override
    public void showAchievementMoreInfo() {

    }
    // TODO AchievementsContract, AchievementsView methods
    // TODO other implementation details achievements
    // TODO use achievements_container, achievement_element, achievement_zoom
}
