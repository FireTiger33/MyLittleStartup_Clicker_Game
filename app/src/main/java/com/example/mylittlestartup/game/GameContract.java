package com.example.mylittlestartup.game;

import android.content.Context;

import com.example.mylittlestartup.data.BaseCallback;


public interface GameContract {

    interface View {
        void showShopScreen();
        Context getAppContext();
        Context getViewContext();
        void setMoney(int delta);
        void showAddedMoney(float x, float y, int val);

        // TODO add methods for added game entities
    }

    interface Presenter {
        void onShopButtonClicked();
        void onGameStart();
        void onGamePause();
        void onTouchLocationTouched(float x, float y, int action);
        void onTouchLocationActionUP();
        void onCommonClickLocationClicked(float x, float y);
        void onSpecClickAreaClicked(float x, float y);
        void onBugIsAlive();

        void onFollowingObjDisappeared(int score);
    }

    interface Repository {

        interface ScoreCallback {
            void onSuccess(int score);

            void onError();
        }

        /**
         * Set given score to runtime storage
         *
         * @param score Current score
         * @param callback
         */
        void setScore(int score, BaseCallback callback);

        /**
         * Add given delta to score in runtime storage
         *
         * @param delta Given delta
         * @param callback
         */
        void incrementScore(int delta, BaseCallback callback);

        /**
         * Get score from runtime storage
         *
         * @param callback
         */
        void getScore(ScoreCallback callback);

        /**
         * Save score in persistent storage and in server
         *
         * @param callback
         */
        void saveScore(ScoreCallback callback);
    }
}