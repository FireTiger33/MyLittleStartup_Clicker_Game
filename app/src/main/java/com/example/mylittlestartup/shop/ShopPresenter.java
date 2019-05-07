package com.example.mylittlestartup.shop;

import android.util.Log;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.GameRepositoryImpl;
import com.example.mylittlestartup.data.PlayerRepository;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

import java.util.List;

class ShopPresenter implements ShopContract.Presenter {
    private ShopContract.View mView;
    private ShopContract.Repository mRepository;
    private PlayerRepository mPlayerRepository;

    public ShopPresenter(ShopContract.View view) {
        mView = view;
        mRepository = new GameRepositoryImpl(view.getAppContext());
        mPlayerRepository = ClickerApplication.from(view.getAppContext()).getPlayerRepository();
    }

    @Override
    public void onBuyUpgrade(final Upgrade upgrade) {
        mRepository.buyUpgrade(upgrade, new BaseCallback() {
            @Override
            public void onSuccess() {
                mView.incrementUpgradeCounter(upgrade.getId() - 1);  // difference of array and database indexing
                getMoney();
            }

            @Override
            public void onError() {
                // todo show some errors?
            }
        });
    }

    @Override
    public void getMoney() {  // TODO get from DB
        mPlayerRepository.getScore(new GameContract.Repository.ScoreCallback() {
            @Override
            public void onSuccess(int score) {
                mView.showMoney(score);
            }

            @Override
            public void onError() {
                Log.e("ShopPresenter", "onError: getMoney getScore callback");
            }
        });
    }

    @Override
    public void fetchUpgrades() {
        mRepository.fetchUpgrades(new ShopContract.Repository.FetchCallback() {
            @Override
            public void onSuccess(List<Upgrade> upgrades) {
                mView.showUpgradeList(upgrades);
            }

            @Override
            public void onError() {
                // todo show some errors?
            }
        });
    }
}
