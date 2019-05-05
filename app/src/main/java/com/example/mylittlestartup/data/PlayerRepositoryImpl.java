package com.example.mylittlestartup.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.game.GameContract;

import static android.content.Context.MODE_PRIVATE;

public class PlayerRepositoryImpl implements PlayerRepository, GameContract.Repository {
    private SharedPreferences mPreferences;

    private static final String KEY_PREF = "PLAYER_PREF";
    private static final String KEY_PREF_SCORE = "PLAYER_SCORE";
    private static final String KEY_PREF_LOGGED_IN = "PLAYER_LOGGED_IN";

    private int mRuntimeScore;
    private boolean mIsLoggedIn;

    private synchronized int getRuntimeScore() {
        return mRuntimeScore;
    }

    private synchronized void setRuntimeScore(int runtimeScore) {
        mRuntimeScore = runtimeScore;
    }

    private synchronized boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    private synchronized void setLoggedIn(boolean loggedIn) {
        mIsLoggedIn = loggedIn;
    }


    public PlayerRepositoryImpl(Context context) {
        mPreferences = context.getSharedPreferences(KEY_PREF, MODE_PRIVATE);
        setRuntimeScore(mPreferences.getInt(KEY_PREF_SCORE, 0));
        setLoggedIn(mPreferences.getBoolean(KEY_PREF_LOGGED_IN, false));
    }

    @Override
    public void setLoggedIn(final BaseCallback callback) {
        setLoggedIn(true);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences
                        .edit()
                        .putBoolean(KEY_PREF_LOGGED_IN, isLoggedIn())
                        .apply();

                // target thread is on callback provider responsibility
                callback.onSuccess();
            }
        });
    }

    @Override
    public void setNotLoggedIn(final BaseCallback callback) {
        setLoggedIn(false);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences
                        .edit()
                        .putBoolean(KEY_PREF_LOGGED_IN, isLoggedIn())
                        .apply();


                callback.onSuccess();
            }
        });
    }

    @Override
    public void setScore(int score, final BaseCallback callback) {
        setRuntimeScore(score);
        callback.onSuccess();
    }

    @Override
    public void incrementScore(int delta, BaseCallback callback) {
        setRuntimeScore(getRuntimeScore() + delta);
        callback.onSuccess();
    }

    @Override
    public void getScore(ScoreCallback callback) {
        if (getRuntimeScore() != 0) {
            callback.onSuccess(getRuntimeScore());
        } else {
            callback.onError();
        }
    }

    @Override
    public void saveScore(final BaseCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences
                        .edit()
                        .putInt(KEY_PREF_SCORE, getRuntimeScore())
                        .apply();

                callback.onSuccess();
            }
        });
    }

    @Override
    public void isLoggedIn(BaseCallback callback) {
        if (isLoggedIn()) {
            callback.onSuccess();
        } else {
            callback.onError();
        }
    }
}
