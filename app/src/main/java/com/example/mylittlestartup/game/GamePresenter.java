package com.example.mylittlestartup.game;

import android.os.Looper;
import android.util.Log;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.executors.AppExecutors;

import static android.content.ContentValues.TAG;

public class GamePresenter implements GameContract.Presenter {
    private GameContract.View mView;
    private GameContract.Repository mRepository;

    public GamePresenter(GameContract.View view, GameContract.Repository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void onShopButtonClicked() {
        mView.showShopScreen();
    }

    @Override
    public void checkMoneyVal() {
        // TODO sync with server and DB and edit
    }

    @Override
    public void addMoney(int delta) {
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
    public void saveMoney() {
        mRepository.saveScore(new BaseCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: successfully saved money");
            }

            @Override
            public void onError() {
                Log.e(TAG, "onError: failure on saveScore");
            }
        });
    }

    @Override
    public void onKeyboardClick() {
        // todo - when k will be stored in storage
    }

    @Override
    public void getMoney() {
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
}
