package com.example.mylittlestartup.authorization;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;
import com.example.mylittlestartup.data.UserRepositoryImpl;
import com.example.mylittlestartup.main.MainActivity;

import java.util.Objects;

public class AuthView extends Fragment implements AuthContract.View {
    private String authMethod;

    private TextView titleText;
    private EditText loginField;
    private Drawable defLoginFieldBackground;
    private EditText passField;
    private Button authButton;
    private ProgressBar progressBar;

    private AuthContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new AuthPresenter(this, new UserRepositoryImpl(getContext().getApplicationContext()));

        Bundle args = this.getArguments();
        assert args != null;

        authMethod = args.getString(AuthContract.authMethodKey);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth, container,false);

        progressBar = view.findViewById(R.id.progress);
        titleText = view.findViewById(R.id.text_authorization_title);
        loginField = view.findViewById(R.id.login);
        defLoginFieldBackground = loginField.getBackground();
        loginField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onFieldsClicked();
            }
        });
        passField = view.findViewById(R.id.pass);
        passField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                onFieldsClicked();
            }
        });

        authButton = view.findViewById(R.id.button_authorization);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAuthButtonClicked(
                        loginField.getText().toString(),
                        passField.getText().toString()
                );
            }
        });

        if (authMethod.equals(AuthContract.authMethodLogin)) {
            this.showLoginScreen();
        } else if (authMethod.equals(AuthContract.authMethodSignUp)) {
            this.showSignUpScreen();
        }

        return view;
    }

    private void onFieldsClicked() {
        if (loginField.getBackground() != defLoginFieldBackground) {
            loginField.setBackground(defLoginFieldBackground);
            passField.setBackground(defLoginFieldBackground);
        }
    }

    @Override
    public void showLoginScreen() {
        // TODO edit titleText
        authMethod = AuthContract.authMethodLogin;
        authButton.setText(R.string.login);
    }

    @Override
    public void showSignUpScreen() {
        // TODO edit titleText
        authMethod = AuthContract.authMethodSignUp;
        authButton.setText(R.string.signup);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showAuthorisationError() {
        loginField.setBackground(new ColorDrawable(Color.RED));
        passField.setBackground(new ColorDrawable(Color.RED));
    }

    @Override
    public void showMainScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openMainScreen();
        } else {
            Log.e(getTag(), "This activity is not a Router");
        }
    }

    @Override
    public String getAuthMethod() {
        return authMethod;
    }
}
