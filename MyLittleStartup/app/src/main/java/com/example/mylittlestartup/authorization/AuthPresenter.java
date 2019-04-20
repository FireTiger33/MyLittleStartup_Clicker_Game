package com.example.mylittlestartup.authorization;


public class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View view;

    public AuthPresenter(AuthContract.View view) {
        this.view = view;
    }

    @Override
    public void onAuthButtonClicked(String login, String pass) {
        // TODO work with database and executors
    }
}
