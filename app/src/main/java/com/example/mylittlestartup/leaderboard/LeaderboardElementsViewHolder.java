package com.example.mylittlestartup.leaderboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.api.UserApi;

class LeaderboardElementsViewHolder extends RecyclerView.ViewHolder {
    private TextView mPlaceNumber;
    private TextView mUsername;
    private TextView mScore;

    LeaderboardElementsViewHolder(@NonNull View itemView) {
        super(itemView);

        mPlaceNumber = itemView.findViewById(R.id.leaderboard_place);
        mUsername = itemView.findViewById(R.id.leaderboard_username);
        mScore = itemView.findViewById(R.id.leaderboard_score);
    }

    void bind(UserApi.UserPlain leaderboardUser, int i) {
        mPlaceNumber.setText(String.valueOf(i + 1));
        mUsername.setText(leaderboardUser.getUsername());
        mScore.setText(String.valueOf(leaderboardUser.getScore()));
    }
}
