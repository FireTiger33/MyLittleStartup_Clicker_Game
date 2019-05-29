package com.example.mylittlestartup.achievements;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Achievement;

import java.util.List;
import java.util.Objects;

public class AchievementsView extends Fragment implements AchievementsContract.View {
    private RecyclerView achievementsView;
    private RecyclerView.LayoutManager layoutManager;
    private AchievementsElementsAdapter achievementsElementsAdapter;

    private AchievementsContract.Presenter mPresenter;

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new AchievementsPresenter(this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.achievements_container, container, false);

        layoutManager = new LinearLayoutManager(this.getContext());

        mPresenter.fetchAchievements();

        return mView;
    }

    @Override
    public void showAchievements(List<Achievement> achievements) {
        achievementsElementsAdapter = new AchievementsElementsAdapter(achievements);

        achievementsView = mView.findViewById(R.id.achievements_view);
        achievementsView.setAdapter(achievementsElementsAdapter);
        achievementsView.setLayoutManager(layoutManager);
    }

    @Override
    public Context getAppContext() {
        return Objects.requireNonNull(getContext()).getApplicationContext();
    }
}
