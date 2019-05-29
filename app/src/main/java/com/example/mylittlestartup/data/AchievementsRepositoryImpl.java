package com.example.mylittlestartup.data;

import android.content.Context;
import android.support.v4.util.Pair;

import com.example.mylittlestartup.achievements.AchievementsContract;
import com.example.mylittlestartup.achievements.AchievementsManager;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.data.sqlite.Achievement;
import com.example.mylittlestartup.data.sqlite.AchievementDao;
import com.example.mylittlestartup.data.sqlite.DBRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AchievementsRepositoryImpl implements AchievementsRepository, AchievementsContract.Repository {
    private AchievementDao mAchievementDao;

    public AchievementsRepositoryImpl(Context context) {
        mAchievementDao = DBRepository.from(context).geAchievementDao();
    }

    @Override
    public void getAchievements(final AchievementCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Achievement> all = mAchievementDao.all();

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(all);
                    }
                });
            }
        });
    }

    @Override
    public void getAchievementsProc(final AchievementProcCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Achievement> all = mAchievementDao.fetchNotDone();

                Map<String, AchievementsManager.AchievementProc> procData = new HashMap<>();
                for (Achievement achievement: all) {
                    if (procData.containsKey(achievement.getType())) {
                        AchievementsManager.AchievementProc procType =
                                Objects.requireNonNull(procData.get(achievement.getType()));
                        if (procType.goal > achievement.getGoal()) {
                            procType.goal = achievement.getGoal();
                            procType.title = achievement.getTitle();
                        }
                    } else {
                        procData.put(achievement.getType(),
                                new AchievementsManager.AchievementProc(
                                        achievement.getGoal(),
                                        achievement.getProgress(),
                                        achievement.getTitle()
                                )
                        );
                    }
                }

                callback.onSuccess(procData);
            }
        });
    }

    @Override
    public void getTypeAchievement(final String type, final AchievementTypeCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Achievement> achievements = mAchievementDao.fetchTypeNotDone(type);

                if (achievements.size() == 0) {
                    callback.onEmpty();
                    return;
                }
                AchievementsManager.AchievementProc achievementBody = null;
                for (Achievement achievement: achievements) {
                    if (achievementBody == null) {
                        achievementBody = new AchievementsManager.AchievementProc(
                                achievement.getGoal(), achievement.getProgress(),
                                achievement.getTitle());
                        continue;
                    }

                    if (achievement.getGoal() < achievementBody.goal) {
                        achievementBody.goal = achievement.getGoal();
                        achievementBody.title = achievement.getTitle();
                    }
                }

                callback.onSuccess(new Pair<>(achievements.get(0).getType(), achievementBody));
            }
        });
    }

    @Override
    public void updateProgress(final Map<String, AchievementsManager.AchievementProc>
                                           achievementsProc,
                               final RefreshCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<String, AchievementsManager.AchievementProc> item:
                        achievementsProc.entrySet()) {
                    AchievementsManager.AchievementProc achievement = item.getValue();
                    if (achievement.isUpdated) {
                        mAchievementDao.updateProgress(achievement.progress, item.getKey());
                    }
                    if (achievement.progress >= achievement.goal) {
                        achieveType(item.getKey(), achievement.progress, callback);
                    }
                }
            }
        });
    }

    @Override
    public void achieveType(final String type, final int progress, final RefreshCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mAchievementDao.achieveType(progress, type);
                callback.onRefresh(type);
            }
        });
    }
}
