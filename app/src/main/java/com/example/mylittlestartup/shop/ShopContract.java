package com.example.mylittlestartup.shop;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.util.List;

public interface ShopContract {
    interface View {
        void showUpgradeList(List<Upgrade> upgrades);

        void incrementUpgradeCounter(int upgradeID);
    }


    interface Presenter {
        void onBuyUpgrade(int upgradeID);
        void fetchUpgrades();
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

        void buyUpgrade(int upgradeID, BaseCallback callback);
    }
}
