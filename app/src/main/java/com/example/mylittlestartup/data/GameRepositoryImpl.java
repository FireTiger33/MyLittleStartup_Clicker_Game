package com.example.mylittlestartup.data;

import android.content.Context;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.achievements.AchievementsContract;
import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.api.GameApi;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.data.sqlite.Achievement;
import com.example.mylittlestartup.data.sqlite.AchievementDao;
import com.example.mylittlestartup.data.sqlite.DBRepository;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.data.sqlite.UpgradeDao;
import com.example.mylittlestartup.game.GameContract;
import com.example.mylittlestartup.shop.ShopContract;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepositoryImpl implements GameContract.Repository, ShopContract.Repository, AchievementsContract.Repository {
    private GameApi mGameApi;
    private UpgradeDao mUpgradeDao;
    private AchievementDao mAchievementDao;
    private PlayerRepository mPlayerRepository;

    public GameRepositoryImpl(Context context) {
        mGameApi = ApiRepository.from(context).getGameApi();
        mUpgradeDao = DBRepository.from(context).getUpgradeDao();
        mAchievementDao = DBRepository.from(context).geAchievementDao();
        mPlayerRepository = ClickerApplication.from(context).getPlayerRepository();
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

    /*@Override
    public void postScore(final int score, final BaseCallback callback) {
        GameApi.ScorePlain scorePlain = new GameApi.ScorePlain();
        scorePlain.score = score;

        mGameApi.sync(scorePlain).enqueue(new Callback<GameApi.ScorePlain>() {
            @Override
            public void onResponse(Call<GameApi.ScorePlain> call, Response<GameApi.ScorePlain> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GameApi.ScorePlain scorePlain1 = response.body();

                    // todo validate?

                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<GameApi.ScorePlain> call, Throwable t) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                });
            }
        });
    }*/

    @Override
    public void setScore(int score, BaseCallback callback) {
        mPlayerRepository.setScore(score, callback);
    }

    @Override
    public void incrementScore(int delta, BaseCallback callback) {
        mPlayerRepository.incrementScore(delta, callback);
    }

    @Override
    public void getScore(ScoreCallback callback) {
        mPlayerRepository.getScore(callback);
    }

    @Override
    public void saveScore(BaseCallback callback) {
        mPlayerRepository.saveScore(callback);
    }

    @Override
    public void fetchUpgrades(final FetchCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Upgrade> all = mUpgradeDao.all();

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
    public void buyUpgrade(final int upgradeID, final BaseCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mUpgradeDao.increaseCounter(upgradeID);
                // todo player repository - count

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess();
                    }
                });

                // todo onError if not enough money
            }
        });

    }
}
