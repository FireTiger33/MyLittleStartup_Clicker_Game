package com.example.mylittlestartup.shop;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.util.List;

class ShopPresenter implements ShopContract.Presenter {
    private ShopContract.View mView;
    private ShopContract.Repository mRepository;

    public ShopPresenter(ShopContract.View view, ShopContract.Repository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void onBuyUpgrade(final int upgradeID) {
        mRepository.buyUpgrade(upgradeID, new BaseCallback() {
            @Override
            public void onSuccess() {
                mView.incrementUpgradeCounter(upgradeID);
            }

            @Override
            public void onError() {
                // todo show some errors?
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
