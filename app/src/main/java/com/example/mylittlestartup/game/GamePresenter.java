package com.example.mylittlestartup.game;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.achievements.AchievementsManager;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.GameRepositoryImpl;
import com.example.mylittlestartup.data.PlayerRepository;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.shop.ShopContract;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GamePresenter implements GameContract.Presenter {
    private String tag = GamePresenter.class.getName();

    private GameContract.View mView;
    private GameRepositoryImpl mRepository;
    private PlayerRepository mPlayerRepository;

    private List<Integer> workerItemsIds;
    private int maxEmploeesLVL = 0;
    private List<CountDownTimer> workTimers;  // TODO manager
    private boolean workTimersIsPaused = false;
    private List<Upgrade> upgradeItems;


    GamePresenter(GameContract.View view) {
        mView = view;
        mRepository = new GameRepositoryImpl(view.getAppContext());
        mPlayerRepository = ClickerApplication.from(view.getAppContext()).getPlayerRepository();

        workTimers = new ArrayList<>();
        workerItemsIds = new ArrayList<>();
    }

    private void startWorkers(List<Upgrade> upgrades) {
        upgradeItems = upgrades;
        for (Upgrade item : upgradeItems) {
            addWorkTimer(item);
            workerItemsIds.add(item.getId());
        }
        startWorkTimers();
    }

    private void startWorkTimers() {
        for (CountDownTimer timer : workTimers) {
            timer.start();
        }
    }

    private void isDoneWork(Upgrade item) {
        addMoney(item.getValue() * item.getCount());
        int itemId = item.getId();
        for (int i = 0; i < workerItemsIds.size(); i++) {
            if (workerItemsIds.get(i) == itemId) {
                workTimers.get(i).start();
            }
        }
    }

    private void addWorkTimer(final Upgrade upgradeItem) {
        // TODO replace 1000 with an minInterval
        workTimers.add(new CountDownTimer(upgradeItem.getInterval(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                isDoneWork(upgradeItem);
            }
        });
    }

    private void getMoney() {
        mRepository.getScore(new GameContract.Repository.ScoreCallback() {
            @Override
            public void onSuccess(final int score) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mView.setMoney(score);
                    }
                });
            }

            @Override
            public void onError() {
                Log.wtf(TAG, "onError: callback in getScore");
            }
        });
    }

    private void saveMoney() {
        mRepository.saveScore(new GameContract.Repository.ScoreCallback() {
            @Override
            public void onSuccess(int score) {
                Log.d(TAG, "onSuccess: successfully saved money");
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError: failure on saveScore");
            }
        });
    }

    private void gameFetchUpgrades() {
        mRepository.fetchUpgrades(new ShopContract.Repository.FetchCallback() {
            @Override
            public void onSuccess(List<Upgrade> upgrades) {
                startWorkers(upgrades);
            }

            @Override
            public void onError() {
                Log.wtf(TAG, "Failed to get data on upgrades");
            }
        });
    }

    @Override
    public void onGameStart() {
        Log.d(tag, "onGameStart");
        getMoney();
        gameFetchUpgrades();
        mView.showWorkers();
    }

    @Override
    public void onGamePause() {
        Log.d(tag, "onGamePause");
        for (CountDownTimer timer : workTimers) {
            timer.cancel();
        }
        workTimers.clear();
        workerItemsIds.clear();
        saveMoney();
    }

    @Override
    public void onTouchLocationTouched(float x, float y, int action) {  // TODO

    }

    @Override
    public void onTouchLocationActionUP() {
        addMoney(1000);
    } // TODO remove

    @Override
    public void onShopButtonClicked() {
        mView.showShopScreen();
    }

    private void addMoney(int delta) {
        if (delta > 0) {
            AchievementsManager.getInstance().IncProgress("money", delta, mView.getViewContext());
        }
        mRepository.incrementScore(delta, new BaseCallback() {
            @Override
            public void onSuccess() {
                getMoney();
            }

            @Override
            public void onError() {
                Log.wtf(TAG, "onError: callback in incrementScore");
            }
        });
    }

    @Override
    public void onCommonClickLocationClicked(float x, float y) {
        if (workTimersIsPaused) {
            workTimersIsPaused = false;
            startWorkTimers();
        }
        mView.showAddedMoney(x, y, mPlayerRepository.getK());
        addMoney(mPlayerRepository.getK());
        AchievementsManager.getInstance().IncProgress("click", 1, mView.getViewContext());
    }

    @Override
    public void onCommonClickLocationClickPaused() {
        for (CountDownTimer timer: workTimers) {
            timer.cancel();
        }
        workTimersIsPaused = true;
    }

    @Override
    public void onSpecClickAreaClicked(float x, float y) {
        mView.showAddedMoney(x, y, mPlayerRepository.getKSpec());
        addMoney(mPlayerRepository.getKSpec());
        AchievementsManager.getInstance().IncProgress("click", 1, mView.getViewContext());
    }

    @Override
    public void onBugIsAlive() {
        addMoney(-(int) Math.pow(10, maxEmploeesLVL + 1));
        mView.showMoneyPulseAnim();
    }

    @Override
    public void fetchWorkers() {
        Log.d(tag, "fetchWorkers");
        mRepository.fetchWorkers(new ShopContract.Repository.FetchCallback() {
            @Override
            public void onSuccess(List<Upgrade> upgrades) {
                Log.d(tag, "fetchedWorkersSuccess: " + upgrades.size());
                mView.createWorkers(upgrades);
            }

            @Override
            public void onError() {
                // todo show some errors?
            }
        });
    }

    @Override
    public void fetchSpeeders(final GameContract.Repository.IntCallback callback) {
        Log.d(tag, "fetchSpeeders");
        mRepository.fetchSpeeders(new ShopContract.Repository.FetchCallback() {
            @Override
            public void onSuccess(List<Upgrade> upgrades) {
                Log.d(tag, "fetchedSpeedersSuccess: " + upgrades.size());
                int totalSpeed = 0;
                for (Upgrade upgrade : upgrades) {
                    totalSpeed += upgrade.getCount() * upgrade.getValue();
                }
                callback.onSuccess(totalSpeed);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }


    @Override
    public void onWorkerPushed(Upgrade upgrade) {
        Log.d(tag, "onWorkerPushed: " + "value = " + upgrade.getValue());
        mView.showMoneyPulseAnim();
        addMoney(upgrade.getValue());
    }

    @Override
    public void onUpgradeWorker(final Upgrade upgrade) {
        mRepository.buyWorkerUpgrade(upgrade, new GameContract.Repository.WorkerUpgradeCallback() {
            @Override
            public void onSuccess(Upgrade upgradedWorker) {
                if (maxEmploeesLVL < upgradedWorker.getCount()) {
                    maxEmploeesLVL = upgradedWorker.getCount();
                }
                mView.showUpgradeWorker(upgradedWorker);  // difference of array and database indexing
                getMoney();
                mPlayerRepository.setK(mPlayerRepository.getK() + 1);
                mPlayerRepository.setKSpec(100 + (mPlayerRepository.getK() - 1) * 150);
                final Toast toast = Toast.makeText(mView.getViewContext(), "LVL: " + upgradedWorker.getCount(), Toast.LENGTH_SHORT);
                new CountDownTimer(400, 100) {
                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    }

                    public void onFinish() {
                        toast.cancel();
                    }
                }.start();
            }

            @Override
            public void onError() {
                final Toast toast = Toast.makeText(mView.getViewContext(),
                        "Недостаточно средств.\nСтоимость: " + upgrade.getPrice(),
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onLayOffWorker(final Upgrade upgrade) {
        mRepository.layOffWorker(upgrade, new GameContract.Repository.WorkerUpgradeCallback() {
            @Override
            public void onSuccess(Upgrade upgradedWorker) {
                mView.showUpgradeWorker(upgradedWorker);  // difference of array and database indexing
                if (upgrade.getCount() == maxEmploeesLVL) {
                    mRepository.getMaxWorkerLVL(new GameContract.Repository.IntCallback() {
                        @Override
                        public void onSuccess(int val) {
                            maxEmploeesLVL = val;
                            Log.d(tag, "now MAX Worker LVL = " + maxEmploeesLVL);
                        }

                        @Override
                        public void onError() {
                        }
                    });
                }
                Log.d(tag, "Layoff worker lvl " + upgrade.getCount());
                mPlayerRepository.setK(mPlayerRepository.getK() - upgrade.getCount());
                mPlayerRepository.setKSpec(100 + (mPlayerRepository.getK() - 1) * 150);
            }

            @Override
            public void onError() {
                final Toast toast = Toast.makeText(mView.getViewContext(),
                        "Что-то пошло не так",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onShowWorkerUpgradeDialog() {
        mView.pauseGameObjects();
    }

    @Override
    public void onCloseWorkerUpgradeDialog() {
        mView.resumeGameObjects();
    }

    @Override
    public void checkEnoughMoney(final int price, final BaseCallback callback) {
        mRepository.getScore(new GameContract.Repository.ScoreCallback() {
            @Override
            public void onSuccess(final int score) {
                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (score - price > 0) {
                            callback.onSuccess();
                        } else {
                            callback.onError();
                        }
                    }
                });
            }

            @Override
            public void onError() {
                Log.wtf(TAG, "onError: callback in getScore");
                callback.onError();
            }
        });
    }

    @Override
    public void registerPlayerBug(final BaseCallback callback) {

    }

    @Override
    public void onFollowingObjDisappeared(int score) {
        addMoney(score * 10);
    }
}
