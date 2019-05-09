package com.example.mylittlestartup.game;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

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
    private String mTAG = GamePresenter.class.getName();

    private GameContract.View mView;
    private GameRepositoryImpl mRepository;

    private List<Integer> itemsIds;
    private List<CountDownTimer> workTimers;
    private List<Upgrade> upgradeItems;

    private MediaPlayer gamePlayer;

    private int k;  // clickMultiplier
    private int spec_k;


    GamePresenter(GameContract.View view) {
        mView = view;
        mRepository = new GameRepositoryImpl(view.getAppContext());
        k = 1;  // TODO achievement increasing multiplier
        spec_k = 100;
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
        Log.d(mTAG, "onGameStart");
        getMoney();
        gameFetchUpgrades();
        if (gamePlayer != null) {
            gamePlayer.start();
        }
    }

    @Override
    public void onGamePause() {
        if (gamePlayer != null) {
            gamePlayer.pause();
        }
        Log.d(mTAG, "onGamePause");
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
    public void onCommonClickLocationClicked() {
        addMoney(k);
    }

    @Override
    public void onSpecClickAreaClicked() {
        addMoney(spec_k);
    }
}
