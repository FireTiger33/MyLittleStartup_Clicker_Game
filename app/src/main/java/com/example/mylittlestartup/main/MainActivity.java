package com.example.mylittlestartup.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.authorization.AuthActivity;
import com.example.mylittlestartup.game.GameActivity;
import com.example.mylittlestartup.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements MainContract.View{
    private String logTag = MainActivity.class.getName();

    private MainContract.Presenter presenter;
    private Button gameStartButton;
    private Button loginButton;
    private Button signUpButton;
    private Button settingsButton;
    private Button achievementsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this);

        gameStartButton = findViewById(R.id.button_start_game);
        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStartGameButtonClicked();
            }
        });

        loginButton = findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLoginButtonClicked();
            }
        });

        signUpButton = findViewById(R.id.button_signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSignUpButtonClicked();
            }
        });

        settingsButton = findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSettingsButtonClicked();
            }
        });

        achievementsButton = findViewById(R.id.achievements_button);
        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public void hideAuthorizationButtons() {
        loginButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
    }

    @Override
    public void showScreenGame() {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        intent.putExtra(AuthActivity.authMethodKey, AuthActivity.authMethodLogin);
        startActivity(intent);
    }

    @Override
    public void showSignUpScreen() {
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        intent.putExtra(AuthActivity.authMethodKey, AuthActivity.authMethodSignUp);
        startActivity(intent);
    }

    @Override
    public void showSettingsScreen() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showAchievementsView() {
        // TODO
    }
}
