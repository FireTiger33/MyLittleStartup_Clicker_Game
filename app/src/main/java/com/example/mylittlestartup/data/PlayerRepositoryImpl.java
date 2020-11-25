package com.example.mylittlestartup.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;
import com.example.mylittlestartup.shop.ShopContract;

import static android.content.Context.MODE_PRIVATE;

public class PlayerRepositoryImpl implements PlayerRepository, GameContract.Repository {
    private SharedPreferences mPreferences;

    private static final String KEY_PREF = "PLAYER_PREF";
    private static final String KEY_PREF_SCORE = "PLAYER_SCORE";
    private static final String KEY_PREF_LOGGED_IN = "PLAYER_LOGGED_IN";
    private static final String KEY_PREF_ID = "PLAYER_ID";

    private static final String KEY_K = "PLAYER_K";
    private static final String KEY_K_SPEC = "PLAYER_K_SPEC";

    private static final String KEY_PREF_MUSIC_SOUND_STATE = "MUSIC_SOUND_STATE";

    private int mRuntimeScore;
    private int mUserID;
    private boolean mIsLoggedIn;
    private boolean mMusicSoundState;

    private int mK;
    private int mKSpec;

    public synchronized int getK() {
        return mK;
    }

    public synchronized void setK(int k) {
        mK = k;
    }

    public synchronized int getKSpec() {
        return mKSpec;
    }

    public synchronized void setKSpec(int KSpec) {
        mKSpec = KSpec;
    }

    public synchronized int getUserID() {
        return mUserID;
    }

    public synchronized void setUserID(int userID) {
        mUserID = userID;
    }

    private synchronized int getRuntimeScore() {
        return mRuntimeScore;
    }

    private synchronized void setRuntimeScore(int runtimeScore) {
        if (runtimeScore >= 0) {
            mRuntimeScore = runtimeScore;
        } else {
            mRuntimeScore = 0;
        }
    }

    private synchronized boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    private synchronized void setLoggedIn(boolean loggedIn) {
        mIsLoggedIn = loggedIn;
    }

    private synchronized void setMusicSoundState(boolean state) {
        mMusicSoundState = state;
    }


    public PlayerRepositoryImpl(Context context) {
        mPreferences = context.getSharedPreferences(KEY_PREF, MODE_PRIVATE);
        setRuntimeScore(mPreferences.getInt(KEY_PREF_SCORE, 0));
        setLoggedIn(mPreferences.getBoolean(KEY_PREF_LOGGED_IN, false));
        setMusicSoundState(mPreferences.getBoolean(KEY_PREF_MUSIC_SOUND_STATE, true));
        setK(mPreferences.getInt(KEY_K, 1));
        setKSpec(mPreferences.getInt(KEY_K_SPEC, 100));
    }

    @Override
    public void setMusicSoundStateOn() {
        setMusicSoundState(true);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences.edit()
                        .putBoolean(KEY_PREF_MUSIC_SOUND_STATE, mMusicSoundState)
                        .apply();
            }
        });
    }  // new

    @Override
    public void setMusicSoundStateOff() {
        setMusicSoundState(false);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences.edit()
                        .putBoolean(KEY_PREF_MUSIC_SOUND_STATE, mMusicSoundState)
                        .apply();
            }
        });
    }  // new

    @Override
    public boolean isMusicSoundState() {
        return mMusicSoundState;
    }  // new

    @Override
    public void setLoggedIn(int id, final BaseCallback callback) {
        setLoggedIn(true);
        setUserID(id);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences
                        .edit()
                        .putBoolean(KEY_PREF_LOGGED_IN, isLoggedIn())
                        .putInt(KEY_PREF_ID, getUserID())
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
        callback.onSuccess(getRuntimeScore());
    }

    @Override
    public void saveScore(final ScoreCallback callback) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mPreferences
                        .edit()
                        .putInt(KEY_PREF_SCORE, getRuntimeScore())
                        .putInt(KEY_K, getK())
                        .putInt(KEY_K_SPEC, getKSpec())
                        .apply();

                callback.onSuccess(getRuntimeScore());
            }
        });
    }

    @Override
    public void fetchWorkers(ShopContract.Repository.FetchCallback callback) {
        // TODO ???
    }

    @Override
    public void fetchSpeeders(ShopContract.Repository.FetchCallback callback) {
        // TODO ???
    }

    @Override
    public void buyWorkerUpgrade(Upgrade worker, WorkerUpgradeCallback callback) {
        // TODO ???
    }

    @Override
    public void layOffWorker(Upgrade worker, WorkerUpgradeCallback callback) {
        // TODO ???
    }

    @Override
    public void getMaxWorkerLVL(IntCallback callback) {
        // TODO ???
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
