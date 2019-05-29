package com.example.mylittlestartup.data;

import android.content.Context;
import android.util.Log;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.achievements.AchievementsContract;
import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.api.GameApi;
import com.example.mylittlestartup.data.api.UserApi;
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
    final private String tag = GameRepositoryImpl.class.getName();

    private GameApi mGameApi;
    private UserApi mUserApi;
    private UpgradeDao mUpgradeDao;
    private AchievementDao mAchievementDao;
    private PlayerRepository mPlayerRepository;

    private boolean noApiSyncYet = true; // scores

    private final String[] workerPicPaths = {
            "img/worker_avatar.png",
            "img/programmer_lvl1.svg",
            "img/programmer_lvl2.svg"
    };

    public GameRepositoryImpl(Context context) {
        mGameApi = ApiRepository.from(context).getGameApi();
        mUserApi = ApiRepository.from(context).getUserApi();
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
    public void getScore(final ScoreCallback callback) {
        mPlayerRepository.getScore(new ScoreCallback() {
            @Override
            public void onSuccess(int score) {
                if (score == 0 && noApiSyncYet) {
                    mUserApi.get(mPlayerRepository.getUserID()).enqueue(new Callback<UserApi.UserPlain>() {
                        @Override
                        public void onResponse(Call<UserApi.UserPlain> call, Response<UserApi.UserPlain> response) {
                            final UserApi.UserPlain user = response.body();

                            if (response.code() == 200 && user != null) { // if user is logged in
                                mPlayerRepository.setScore(user.score, new BaseCallback() {
                                    @Override
                                    public void onSuccess() {
                                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onSuccess(user.score);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError() {
                                        Log.wtf("GameRepositoryImpl", "getScore: ScoreCallback onSuccess: mUserApi.get: onResponse: setScore: onError");
                                    }
                                });
                            } else { // if no login
                                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onError();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<UserApi.UserPlain> call, Throwable t) {
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError();
                                }
                            });
                        }
                    });

                    noApiSyncYet = false;
                } else {
                    callback.onSuccess(score);
                }
            }

            @Override
            public void onError() {
                // never get here
                Log.wtf("GameRepositoryImpl", "getScore: ScoreCallback: onError");
            }
        });
    }

    @Override
    public void saveScore(final ScoreCallback callback) {
        mPlayerRepository.saveScore(new ScoreCallback() {
            @Override
            public void onSuccess(final int score) {
                mGameApi.sync(new GameApi.ScorePlain(score)).enqueue(new Callback<GameApi.ScorePlain>() {
                    @Override
                    public void onResponse(Call<GameApi.ScorePlain> call, Response<GameApi.ScorePlain> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onSuccess(score);
                                }
                            });
                        } else {
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError();
                                }
                            });
                            Log.e("GameRepositoryImpl", "run: saveScore: on api sync onResponse");
                        }
                    }

                    @Override
                    public void onFailure(Call<GameApi.ScorePlain> call, final Throwable t) {
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError();
                            }
                        });
                        Log.e("GameRepositoryImpl", "run: saveScore: on api sync onFailure", t);
                    }
                });
            }

            @Override
            public void onError() {
                Log.wtf("GameRepositoryImpl", "onError: mPlayerRepository saveScore callback");
            }
        });
    }

    @Override
    public void fetchWorkers(final FetchCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Upgrade> workers = mUpgradeDao.allWorkers();

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(workers);
                    }
                });
            }
        });
    }

    @Override
    public void fetchUpgrades(final FetchCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Upgrade> all = mUpgradeDao.allUpgrades();

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
    public void fetchSpeeders(final FetchCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<Upgrade> all = mUpgradeDao.allSpeeders();

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
    public void buyUpgrade(final Upgrade upgrade, final BaseCallback callback) {
        mPlayerRepository.getScore(new ScoreCallback() {
            @Override
            public void onSuccess(final int score) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (score >= upgrade.getPrice()) {
                            mUpgradeDao.increaseCounter(upgrade.getId());
                            mPlayerRepository.setScore(score - upgrade.getPrice(), new BaseCallback() {
                                @Override
                                public void onSuccess() {
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess();
                                        }
                                    });
                                }

                                @Override
                                public void onError() {
                                    Log.e(tag, "buyUpgrade: onError when setScore");
                                }
                            });
                        } else {
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError();
                                    Log.d(tag, "not enough money");
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onError() {
                Log.e("GameRepositoryImpl", "buyUpgrade: onError when getScore");
            }
        });
    }

    @Override
    public void buyWorkerUpgrade(final Upgrade worker, final WorkerUpgradeCallback callback) {
        mPlayerRepository.getScore(new ScoreCallback() {
            @Override
            public void onSuccess(final int score) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (score >= worker.getPrice()) {
                            int workerPrice = worker.getPrice();
                            int nextPicId = worker.getCount() >= workerPicPaths.length-1? workerPicPaths.length-1: worker.getCount()+1;

                            Upgrade upgrade = new Upgrade(
                                    worker.getPrice() * 3,
                                    worker.getName(),
                                    worker.getDescription(),
                                    worker.getCount() + 1,
                                    worker.getInterval() + 5000,
                                    worker.getValue() * 2 + 100,
                                    workerPicPaths[nextPicId]
                            );

                            upgrade.setId(worker.getId());
                            mUpgradeDao.upgradeWorker(upgrade);

                            final List<Upgrade> upgradedWorker = mUpgradeDao.worker(worker.getId());
                            Log.d(tag, "buyWorkerUpgrade: UpgradedWorkerLVL = " + upgradedWorker.get(0).getCount());
                            mPlayerRepository.setScore(score - workerPrice, new BaseCallback() {
                                @Override
                                public void onSuccess() {
                                    AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            callback.onSuccess(upgradedWorker.get(0));
                                        }
                                    });
                                }

                                @Override
                                public void onError() {
                                    Log.e(tag, "buyUpgrade: onError when setScore");
                                }
                            });
                        } else {
                            AppExecutors.getInstance().mainThread().execute(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onError();
                                    Log.d(tag, "not enough money");
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onError() {
                Log.e("GameRepositoryImpl", "buyUpgrade: onError when getScore");
            }
        });
        /*buyUpgrade(worker, new BaseCallback() {
            @Override
            public void onSuccess() {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mUpgradeDao.upgradeWorker(worker.getId(), workerPicIds[worker.getCount()]);
                    }
                });
                callback.onSuccess();
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });*/
    }

    @Override
    public void layOffWorker(final Upgrade worker, final WorkerUpgradeCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mUpgradeDao.layOffWorker(worker.getId(), workerPicPaths[0]);
                final List<Upgrade> upgradedWorker = mUpgradeDao.worker(worker.getId());
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(upgradedWorker.get(0));
                    }
                });
            }
        });
    }

    @Override
    public void getMaxWorkerLVL(final IntCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final int maxLVL = mUpgradeDao.getMaxWorkerLVL();
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(maxLVL);
                    }
                });
            }
        });
    }
}
