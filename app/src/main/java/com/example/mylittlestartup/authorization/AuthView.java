package com.example.mylittlestartup.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.UserRepositoryImpl;
import com.example.mylittlestartup.main.MainActivity;

import java.util.Objects;

public class AuthView extends Fragment implements AuthContract.View {
    private String authMethod;

    private TextView titleText;
    private EditText loginField;
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
        passField = view.findViewById(R.id.pass);

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
    public void showMainScreen() {
        Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(), MainActivity.class);
        startActivity(intent);
        /*Instrumentation inst = new Instrumentation();
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);*/
    }

    @Override
    public String getAuthMethod() {
        return authMethod;
    }
}
