package com.example.mylittlestartup.achievements;

import android.content.Context;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.Toast;

import com.example.mylittlestartup.data.AchievementsRepository;

import java.util.Map;

public class AchievementsManager {
    private AchievementsRepository mRepository;
    private Map<String, AchievementProc> mAchievementsProc; // <type, achievement>

    private AchievementsManager() {
        if (mAchievementsProc == null && mRepository != null) {
            initProcData();
        }
    }

    private static class AchivementsManagerHolder {
        private static AchievementsManager mInstance = new AchievementsManager();
    }

    public static AchievementsManager getInstance() {
        return AchivementsManagerHolder.mInstance;
    }

    public void initAM(AchievementsRepository repository) {
        setRepository(repository);
        initProcData();
    }

    public void setRepository(AchievementsRepository repository) {
        mRepository = repository;
    }

    public void initProcData() {
        if (mRepository == null) return;

        mRepository.getAchievementsProc(
                new AchievementsContract.Repository.AchievementProcCallback() {
            @Override
            public void onSuccess(Map<String, AchievementProc> achievementList) {
                mAchievementsProc = achievementList;
            }

            @Override
            public void onError() {

            }
        });
    }

    static public class AchievementProc {
        public int goal;
        public int progress;
        public String title;
        public boolean isUpdated;

        public AchievementProc(int goal, int progress, String title) {
            this.goal = goal;
            this.progress = progress;
            this.title = title;
            this.isUpdated = false;
        }
    }

    public void UpdateProgressInDB() {
        if (mRepository == null) return;

        mRepository.updateProgress(mAchievementsProc,
                new AchievementsContract.Repository.RefreshCallback() {
            @Override
            public void onRefresh(final String type) {
                mRepository.getTypeAchievement(type,
                        new AchievementsContract.Repository.AchievementTypeCallback() {
                    @Override
                    public void onSuccess(Pair<String, AchievementProc> achievement) {
                        assert achievement.first != null;
                        assert achievement.second != null;
                        AchievementProc achievementProc = mAchievementsProc.get(type);
                        if (achievementProc == null) return;;
                        achievementProc.goal = achievement.second.goal;
                        achievementProc.title = achievement.second.title;
                        if (achievementProc.progress > achievement.second.progress) {
                            achievementProc.isUpdated = true;
                        }
                    }

                    @Override
                    public void onEmpty() {
                        mAchievementsProc.remove(type);
                    }
                });
            }
        });

    }

    public void IncProgress(String type, int progress, Context context) {
         AchievementProc achievementProc = mAchievementsProc.get(type);
         if (achievementProc == null) {
             return;
         }
        achievementProc.progress += progress;
        achievementProc.isUpdated = true;
        Log.d("ACHIEVEMENTS", "Inc type["+ type + "]: " + String.valueOf(achievementProc.progress) + "/" + String.valueOf(achievementProc.goal));
        if (achievementProc.progress >= achievementProc.goal) {
            mRepository.achieveType(type, achievementProc.progress,
                    new AchievementsContract.Repository.RefreshCallback() {
                        @Override
                        public void onRefresh(final String type) {
                            mRepository.getTypeAchievement(type,
                                    new AchievementsContract.Repository.AchievementTypeCallback() {
                                        @Override
                                        public void onSuccess(Pair<String, AchievementProc> achievement) {
                                            assert achievement.first != null;
                                            assert achievement.second != null;
                                            AchievementProc achievementProc = mAchievementsProc.get(type);
                                            if (achievementProc == null) return;;
                                            achievementProc.goal = achievement.second.goal;
                                            achievementProc.title = achievement.second.title;
                                            if (achievementProc.progress > achievement.second.progress) {
                                                achievementProc.isUpdated = true;
                                            }
                                        }

                                        @Override
                                        public void onEmpty() {
                                            mAchievementsProc.remove(type);
                                        }
                                    });
                        }
                    });

            final Toast toast = Toast.makeText(context, "Achieved: "
                    + achievementProc.title, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
