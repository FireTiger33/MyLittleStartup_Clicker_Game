package com.example.mylittlestartup.shop;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.GameRepositoryImpl;
import com.example.mylittlestartup.data.PlayerRepository;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

import java.util.List;

class ShopPresenter implements ShopContract.Presenter {
    private MediaPlayer player;
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
                player = MediaPlayer.create(mView.getViewContext(), R.raw.on_buy_upgrade_sound);
                player.start();
            }

            @Override
            public void onError() {
                Toast toast = Toast.makeText(mView.getViewContext(), "Недостаточно средств", Toast.LENGTH_SHORT);
                toast.show();
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

    @Override
    public void onViewClosed() {
        // player.stop();
    }
}
