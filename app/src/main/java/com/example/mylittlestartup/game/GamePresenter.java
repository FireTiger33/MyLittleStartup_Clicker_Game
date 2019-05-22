package com.example.mylittlestartup.game;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.R;
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

    private List<Integer> itemsIds;
    private List<CountDownTimer> workTimers;  // TODO manager
    private List<Upgrade> upgradeItems;

    private MediaPlayer gamePlayer;

    GamePresenter(GameContract.View view) {
        mView = view;
        mRepository = new GameRepositoryImpl(view.getAppContext());
        mPlayerRepository = ClickerApplication.from(view.getAppContext()).getPlayerRepository();

        workTimers = new ArrayList<>();
        itemsIds = new ArrayList<>();
        if (isMusicSoundState()) {
            musicOn();
        }
    }

    private boolean isMusicSoundState() {
        PlayerRepository playerRepository = ClickerApplication.from(mView.getAppContext()).getPlayerRepository();
        return playerRepository.isMusicSoundState();
    }

    private void musicOn() {
        gamePlayer = MediaPlayer.create(mView.getViewContext(), R.raw.game_sound);
        gamePlayer.setLooping(true);
    }

    private void startWorkers(List<Upgrade> upgrades) {
        upgradeItems = upgrades;
        for (Upgrade item: upgradeItems) {
            addWorkTimer(item);
            itemsIds.add(item.getId());
        }
        startWorkTimers();
    }

    private void startWorkTimers() {
        for (CountDownTimer timer: workTimers) {
            timer.start();
        }
    }

    private void isDoneWork(Upgrade item) {
        addMoney(item.getValue()*item.getCount());
        int itemId = item.getId();
        for (int i = 0; i < itemsIds.size(); i++) {
            if (itemsIds.get(i) == itemId) {
                workTimers.get(i).start();
            }
        }
    }

    private void addWorkTimer(final Upgrade upgradeItem) {
        // TODO replace 1000 with an minInterval
        workTimers.add(new CountDownTimer(upgradeItem.getInterval(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) { }
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
        if (gamePlayer != null) {
            gamePlayer.start();
        }
        mView.showWorkers();
    }

    @Override
    public void onGamePause() {
        if (gamePlayer != null) {
            gamePlayer.pause();
        }
        Log.d(tag, "onGamePause");
        for (CountDownTimer timer: workTimers) {
            timer.cancel();
        }
        workTimers.clear();
        itemsIds.clear();
        saveMoney();
    }

    @Override
    public void onTouchLocationTouched(float x, float y, int action) {

    }

    @Override
    public void onTouchLocationActionUP() {
        addMoney(1000);
    }

    @Override
    public void onShopButtonClicked() {
        mView.showShopScreen();

    }

    private void addMoney(int delta) {
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
        mView.showAddedMoney(x, y, mPlayerRepository.getK());
        addMoney(mPlayerRepository.getK());
    }

    @Override
    public void onSpecClickAreaClicked(float x, float y) {
        mView.showAddedMoney(x, y, mPlayerRepository.getKSpec());
        addMoney(mPlayerRepository.getKSpec());
    }

    @Override
    public void onBugIsAlive() {
        addMoney(-mPlayerRepository.getK()*10);
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
    public void onWorkerPushed(Upgrade upgrade) {
        Log.d(tag, "onWorkerPushed: " + "value = " + upgrade.getValue());
        addMoney(upgrade.getValue());
    }

    @Override
    public void onUpgradeWorker(final Upgrade upgrade) {
        mRepository.buyWorkerUpgrade(upgrade, new GameContract.Repository.WorkerUpgradeCallback() {
            @Override
            public void onSuccess(Upgrade upgradedWorker) {
                mView.showUpgradeWorker(upgradedWorker);  // difference of array and database indexing
                getMoney();
                mPlayerRepository.setK(mPlayerRepository.getK() + 1);
                mPlayerRepository.setKSpec(100  + (mPlayerRepository.getK()-1) * 150);
                final Toast toast = Toast.makeText(mView.getViewContext(), "LVL: " + upgradedWorker.getCount(), Toast.LENGTH_SHORT);
                new CountDownTimer(400, 100) {
                    public void onTick(long millisUntilFinished) {
                        toast.show();
                    } public void onFinish() {
                        toast.cancel();
                    }
                }.start();
            }

            @Override
            public void onError() {
                final Toast toast = Toast.makeText(mView.getViewContext(), "Недостаточно средств.\n Стоимость: "
                        + upgrade.getPrice(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void onFollowingObjDisappeared(int score) {
        addMoney(score*10);
    }
}
