package com.example.mylittlestartup.achievements;

import android.content.Context;
import android.support.v4.util.Pair;

import com.example.mylittlestartup.data.sqlite.Achievement;

import java.util.List;
import java.util.Map;

public interface AchievementsContract {
    interface View {
        void showAchievements(List<Achievement> achievements);
        Context getAppContext();
    }

    interface Presenter {
        void fetchAchievements();
    }

    interface Repository{
        interface AchievementCallback {
            void onSuccess(List<Achievement> achievementList);

            void onError();
        }
        interface AchievementProcCallback {
            void onSuccess(Map<String, AchievementsManager.AchievementProc> achievementList);

            void onError();
        }
        interface AchievementTypeCallback {
            void onSuccess(Pair<String, AchievementsManager.AchievementProc> achievement);

            void onEmpty();
        }
        interface RefreshCallback {
            void onRefresh(String type);
        }

        void getAchievements(AchievementCallback callback);
        void getAchievementsProc(AchievementProcCallback callback);
        void getTypeAchievement(final String type, AchievementTypeCallback callback);

        void updateProgress(final Map<String, AchievementsManager.AchievementProc> achievementsProc,
                            final RefreshCallback callback);

        void achieveType(String type, int progress, final RefreshCallback callback);
    }
}
