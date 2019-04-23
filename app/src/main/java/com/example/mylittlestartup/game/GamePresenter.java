package com.example.mylittlestartup.game;

public class GamePresenter implements GameContract.Presenter {
    private GameContract.View view;

    public GamePresenter(GameContract.View view) {
        this.view = view;
    }

    @Override
    public void onShopButtonClicked() {
        view.showShopScreen();
    }

    @Override
    public void checkMoneyVal() {
        // TODO sync with server and DB and edit
    }
}
