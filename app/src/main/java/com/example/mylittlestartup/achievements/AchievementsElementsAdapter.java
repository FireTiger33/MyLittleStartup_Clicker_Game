package com.example.mylittlestartup.achievements;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.api.UserApi;
import com.example.mylittlestartup.data.sqlite.Achievement;

import java.util.List;


public class AchievementsElementsAdapter extends RecyclerView.Adapter<AchievementsElementsViewHolder> {
    private List<Achievement> mAchievements;

    public AchievementsElementsAdapter(List<Achievement> achievements) {
        mAchievements = achievements;
    }

    @NonNull
    @Override
    public AchievementsElementsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.achievement_element, viewGroup, false);

        return new AchievementsElementsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsElementsViewHolder achievementsElementsViewHolder, int i) {
        achievementsElementsViewHolder.bind(mAchievements.get(i));
    }

    @Override
    public int getItemCount() {
        return mAchievements.size();
    }
}
