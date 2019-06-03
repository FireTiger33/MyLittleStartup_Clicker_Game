package com.example.mylittlestartup.game;

import android.content.Context;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.shop.ShopContract;

import java.util.List;


public interface GameContract {

    interface View {
        void showShopScreen();
        Context getAppContext();
        Context getViewContext();
        void setMoney(int delta);
        void showAddedMoney(float x, float y, int val);
        void showMoneyPulseAnim();
        void createWorkers(List<Upgrade> upgrades);
        void showWorkers();
        void showUpgradeWorker(Upgrade upgradedWorker);
        void pauseGameObjects();
        void resumeGameObjects();

        // TODO add methods for added game entities
    }

    interface Presenter {
        void onShopButtonClicked();
        void onGameStart();
        void onGamePause();
        void onTouchLocationTouched(float x, float y, int action);
        void onTouchLocationActionUP();
        void onCommonClickLocationClicked(float x, float y);
        void onCommonClickLocationClickPaused();
        void onSpecClickAreaClicked(float x, float y);
        void onBugIsAlive();
        void fetchWorkers();
        void fetchSpeeders(Repository.IntCallback callback);
        void onWorkerPushed(Upgrade upgrade);
        void onUpgradeWorker(Upgrade upgrade);
        void onLayOffWorker(Upgrade upgrade);
        void onShowWorkerUpgradeDialog();
        void onCloseWorkerUpgradeDialog();
        void checkEnoughMoney(final int price, final BaseCallback callback);
        void registerPlayerBug(final BaseCallback callback);

        void onFollowingObjDisappeared(int score);
    }

    interface Repository {

        interface ScoreCallback {
            void onSuccess(int score);

            void onError();
        }
        interface WorkerUpgradeCallback {
            void onSuccess(Upgrade upgradedWorker);
            void onError();
        }

        interface IntCallback {
            void onSuccess(int lvl);
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

        void fetchWorkers(final ShopContract.Repository.FetchCallback callback);
        void fetchSpeeders(final ShopContract.Repository.FetchCallback callback);
        void buyWorkerUpgrade(final Upgrade worker, final WorkerUpgradeCallback callback);
        void layOffWorker(final Upgrade worker, final WorkerUpgradeCallback callback);
        void getMaxWorkerLVL(final IntCallback callback);
    }
}