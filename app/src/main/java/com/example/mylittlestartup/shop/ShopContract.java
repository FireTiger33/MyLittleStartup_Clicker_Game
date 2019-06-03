package com.example.mylittlestartup.shop;

import android.content.Context;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

import java.util.List;

public interface ShopContract {
    interface View {
        void showUpgradeList();
        Context getAppContext();
        Context getViewContext();
        void showMoney(int money);

        void incrementUpgradeCounter(int upgradeID);
    }


    interface Presenter {
        void onBuyUpgrade(Upgrade upgrade);
        void getMoney();
//        void fetchUpgrades();
        void onViewClosed();
        void checkEnoughMoney(final int price, final BaseCallback callback);
        ShopElementsAdapter getShopElementsAdapter();
    }

    interface Repository {
        interface FetchCallback {
            void onSuccess(List<Upgrade> upgrades);
            void onError();
        }

        /**
         * Get all upgrades
         *
         * Every Upgrade contains counter which shows
         * how much has already been acquired and
         *
         * @param callback
         */
        void fetchUpgrades(FetchCallback callback);

        void fetchSpeeders(FetchCallback callback);

        void buyUpgrade(Upgrade upgrade, BaseCallback callback);
        void getScore(final GameContract.Repository.ScoreCallback scoreCallback);
    }
}
