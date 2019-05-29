package com.example.mylittlestartup.data;

import com.example.mylittlestartup.achievements.AchievementsContract;
import com.example.mylittlestartup.achievements.AchievementsManager;

import java.util.Map;

public interface AchievementsRepository extends AchievementsContract.Repository {
    void getAchievements(final AchievementCallback callback);
    void getAchievementsProc(AchievementProcCallback callback);
    void getTypeAchievement(final String type, AchievementTypeCallback callback);

    void updateProgress(final Map<String, AchievementsManager.AchievementProc> achievementsProc,
                        final RefreshCallback callback);

    void achieveType(String type, int progress, final RefreshCallback callback);
}
