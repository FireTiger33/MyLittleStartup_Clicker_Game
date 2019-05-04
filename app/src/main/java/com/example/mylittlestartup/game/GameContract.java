package com.example.mylittlestartup.game;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Achievement;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.util.List;

public interface GameContract {

    interface View {
        void showShopScreen();
        // TODO add methods for added game entities
    }

    interface Presenter {
        void onShopButtonClicked();
        void checkMoneyVal();
    }

    interface Repository {
        interface AchievementCallback {
            void onSuccess(List<Achievement> achievementList);

            void onError();
        }

        interface UpgradeCallback {
            void onSuccess(List<Upgrade> upgradeList);

            void onError();
        }

        void getAchievements(AchievementCallback callback);

        void postScore(int score, BaseCallback callback);

        void getUpgrades(UpgradeCallback callback);

        void buyUpgrade(int upgradeID, BaseCallback callback);
    }
}
