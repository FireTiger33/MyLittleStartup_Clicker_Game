package com.example.mylittlestartup.game;

public interface GameContract {

    interface View {
        void showShopScreen();
        // TODO add methods for added game entities
    }

    interface Presenter {
        void onShopButtonClicked();
        void checkMoneyVal();
    }
}
