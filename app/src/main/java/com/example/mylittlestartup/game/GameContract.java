package com.example.mylittlestartup.game;

import android.content.Context;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.shop.ShopContract;

import java.util.List;


public interface GameContract {

    interface View {
        /**
         * Opens shop screen using router
         */
        void showShopScreen();
        Context getAppContext();
        Context getViewContext();

        /**
         * Update the amount of money displayed
         * @param money new money value
         */
        void setMoney(int money);

        /**
         * Displays the running money animation at the touch point.
         *
         * @param x touch point abscissa
         * @param y touch point ordinate
         * @param val added money value
         */
        void showAddedMoney(float x, float y, int val);

        /**
         * Displays a pulsating animation money.
         * Animation duration: 150ms.
         * Using value animation.
         */
        void showMoneyPulseAnim();

        /**
         * Create GameObjWorkers from list workers
         * @param upgrades workers from DB
         */
        void createWorkers(List<Upgrade> upgrades);

        /**
         * Show workers on display
         */
        void showWorkers();

        /**
         * Display changes after worker improvement
         * @param upgradedWorker worker whose parameters have been changed
         */
        void showUpgradeWorker(Upgrade upgradedWorker);

        /**
         * Calls the onPause method on game objects that appear(bug and issues).
         */
        void pauseGameObjects();

        /**
         * Calls the onResume method on game objects that appear(bug and issues).
         */
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
         * @param callback callback actions
         */
        void setScore(int score, BaseCallback callback);

        /**
         * Add given delta to score in runtime storage
         *
         * @param delta Given delta
         * @param callback callback actions
         */
        void incrementScore(int delta, BaseCallback callback);

        /**
         * Get score from runtime storage
         *
         * @param callback callback actions
         */
        void getScore(ScoreCallback callback);

        /**
         * Save score in persistent storage and in server
         *
         * @param callback callback actions
         */
        void saveScore(ScoreCallback callback);

        void fetchWorkers(final ShopContract.Repository.FetchCallback callback);
        void fetchSpeeders(final ShopContract.Repository.FetchCallback callback);
        void buyWorkerUpgrade(final Upgrade worker, final WorkerUpgradeCallback callback);
        void layOffWorker(final Upgrade worker, final WorkerUpgradeCallback callback);
        void getMaxWorkerLVL(final IntCallback callback);
    }
}