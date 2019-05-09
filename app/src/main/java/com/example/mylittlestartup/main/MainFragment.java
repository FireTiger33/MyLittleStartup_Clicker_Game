package com.example.mylittlestartup.main;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;
import com.example.mylittlestartup.data.UserRepositoryImpl;

import java.util.Objects;

public class MainFragment extends Fragment implements MainContract.View {
    private String logTag = MainActivity.class.getName();

    private MainContract.Presenter presenter;

    private Button loginButton;
    private Button signUpButton;
    private Button gameStartButton;
    private Button settingsButton;
    private ImageButton achievementsButton;
    private ImageButton leaderboardButton;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this, new UserRepositoryImpl(getContext().getApplicationContext()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.onViewShowed();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onViewClosed();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView nameGame = view.findViewById(R.id.game_name);
        Typeface customFont = Typeface.createFromAsset(
                Objects.requireNonNull(getActivity()).getAssets(), "fonts/Equestria.ttf"
        );
        nameGame.setTypeface(customFont);

        gameStartButton = view.findViewById(R.id.button_start_game);
        gameStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onStartGameButtonClicked();
            }
        });

        loginButton = view.findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLoginButtonClicked();
            }
        });

        signUpButton = view.findViewById(R.id.button_signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSignUpButtonClicked();
            }
        });

        settingsButton = view.findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSettingsButtonClicked();
            }
        });

        achievementsButton = view.findViewById(R.id.achievements_button);
        achievementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAchievementsButtonClicked();
            }
        });

        leaderboardButton = view.findViewById(R.id.leaderboard_button);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLeaderboardButtonClicked();
            }
        });

        presenter.checkIsLoggedIn();

        return view;
    }

    @Override
    public void hideAuthorizationButtons() {
        loginButton.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
    }

    @Override
    public void showScreenGame() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openGameScreen();
        } else {
            Log.e(logTag, "This activity is not a Router");
        }
    }

    @Override
    public void showLoginScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openLoginScreen();
        } else {
            Log.e(logTag, "This activity is not a Router");
        }
    }

    @Override
    public void showSignUpScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openSignUpScreen();
        } else {
            Log.e(logTag, "This activity is not a Router");
        }
    }

    @Override
    public void showSettingsScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openSettingsScreen();
        } else {
            Log.e(logTag, "This activity is not a Router");
        }
    }

    @Override
    public void showAchievementsScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openAchievementsScreen();
        } else {
            Log.e(logTag, "This activity is not a Router");
        }
    }

    @Override
    public void showLeaderboardScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openLeaderboardScreen();
        } else {
            Log.e(logTag, "This activity is not a Router");
        }
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }
}
