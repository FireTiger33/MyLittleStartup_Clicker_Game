package com.example.mylittlestartup.authorization;

public class AuthPresenter implements AuthContract.Presenter {

    private AuthContract.View mView;
    private AuthContract.Repository mRepository;

    public AuthPresenter(AuthContract.View view, AuthContract.Repository repository) {
        this.mView = view;
        this.mRepository = repository;
    }

    @Override
    public void onAuthButtonClicked(String login, String pass) {
        mView.showProgress();

        if (mView.getAuthMethod().equals(AuthContract.authMethodLogin)) {
            mRepository.authUser(login, pass, new AuthContract.Repository.ValidationCallback() {
                @Override
                public void onSuccess() {
                    mView.hideProgress();
                    mView.showMainScreen();
                }

                @Override
                public void onError() {
                    mView.hideProgress();
                    mView.showAuthorisationError();
                }

                @Override
                public void onNotFound() {
                    mView.showSignUpScreen();
                }
            });
        } else if (mView.getAuthMethod().equals(AuthContract.authMethodSignUp)) {
            mRepository.registerNewUser(login, pass, new AuthContract.Repository.ValidationCallback() {
                @Override
                public void onSuccess() {
                    mView.hideProgress();
                    mView.showLoginScreen();
                }

                @Override
                public void onError() {
                    mView.hideProgress();
                    mView.showAuthorisationError();
                }

                @Override
                public void onNotFound() {

                }
            });
        }
    }
}
