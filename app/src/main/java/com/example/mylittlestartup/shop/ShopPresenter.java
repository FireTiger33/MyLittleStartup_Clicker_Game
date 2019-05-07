package com.example.mylittlestartup.shop;

import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.GameRepositoryImpl;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.util.List;

class ShopPresenter implements ShopContract.Presenter {
    private ShopContract.View mView;
    private ShopContract.Repository mRepository;

    public ShopPresenter(ShopContract.View view) {
        mView = view;
        mRepository = new GameRepositoryImpl(view.getAppContext());
    }

    @Override
    public void onBuyUpgrade(final int upgradeID) {
        mRepository.buyUpgrade(upgradeID, new BaseCallback() {
            @Override
            public void onSuccess() {
                mView.incrementUpgradeCounter(upgradeID - 1);  // difference of array and database indexing
            }

            @Override
            public void onError() {
                // todo show some errors?
            }
        });
    }

    @Override
    public void getMoney() {  // TODO get from DB
        mView.showMoney(1000);
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
