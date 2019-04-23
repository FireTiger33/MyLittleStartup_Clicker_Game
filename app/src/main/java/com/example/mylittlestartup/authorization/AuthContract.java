package com.example.mylittlestartup.authorization;

public interface AuthContract {

    interface View {
        void showLoginScreen();
        void showSignUpScreen();

        void showProgress();
        void hideProgress();

        void showMainScreen();
    }

    interface Presenter {
        void onAuthButtonClicked(String login, String pass);
    }

    interface Repository {
        void registerNewUser(String login, String pass, ValidationCallback callback);
        void authUser(String login, String pass, ValidationCallback callback);

        interface ValidationCallback {
            void onSuccess();
            void onError();
            void onNotFound();
        }
    }

}
