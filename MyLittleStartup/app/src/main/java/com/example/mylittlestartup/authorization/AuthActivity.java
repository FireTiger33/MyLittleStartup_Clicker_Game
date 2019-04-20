package com.example.mylittlestartup.authorization;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.main.MainActivity;

public class AuthActivity extends Activity implements AuthContract.View {
    public static final String authMethodKey = "AUTH_METHOD";
    public static final String authMethodLogin = "LOGIN";
    public static final String authMethodSignUp = "SIGN_UP";

    private TextView titleText;
    private EditText loginField;
    private EditText passField;
    private Button authButton;
    private ProgressBar progressBar;

    private AuthContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        presenter = new AuthPresenter(this);

        progressBar = findViewById(R.id.progress);
        titleText = findViewById(R.id.text_authorization_title);
        loginField = findViewById(R.id.login);
        passField = findViewById(R.id.pass);
        authButton = findViewById(R.id.button_authorization);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAuthButtonClicked(
                        loginField.getText().toString(),
                        passField.getText().toString()
                );
            }
        });

        Intent intent = getIntent();
        String authMethod = intent.getStringExtra(authMethodKey);
        if (authMethod.equals(authMethodLogin)) {
            this.showLoginScreen();
        } else if (authMethod.equals(authMethodSignUp)) {
            this.showSignUpScreen();
        }

    }

    @Override
    public void showLoginScreen() {
        // TODO edit titleText
        authButton.setText(R.string.login);
    }

    @Override
    public void showSignUpScreen() {
        // TODO edit titleText
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
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        /*Instrumentation inst = new Instrumentation();
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);*/
    }
}
