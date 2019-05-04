package com.example.mylittlestartup.main;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;
import com.example.mylittlestartup.achievements.AchievementsView;
import com.example.mylittlestartup.authorization.AuthContract;
import com.example.mylittlestartup.authorization.AuthView;
import com.example.mylittlestartup.game.GameView;
import com.example.mylittlestartup.settings.SettingsView;
import com.example.mylittlestartup.shop.ShopView;

public class MainActivity extends AppCompatActivity implements Router {
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager.beginTransaction()
                .replace(R.id.main_activity, new MainFragment())
                .commit();
    }


    @Override
    public void openSignUpScreen() {
        Bundle args = new Bundle();
        args.putString(AuthContract.authMethodKey, AuthContract.authMethodSignUp);

        AuthView fragment = new AuthView();
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, fragment)
                .commit();
    }

    @Override
    public void openLoginScreen() {
        Bundle args = new Bundle();
        args.putString(AuthContract.authMethodKey, AuthContract.authMethodLogin);

        AuthView fragment = new AuthView();
        fragment.setArguments(args);

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, fragment)
                .commit();
    }

    @Override
    public void openSettingsScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new SettingsView())
                .commit();
    }

    @Override
    public void openAchievementsScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new AchievementsView())
                .commit();
    }

    @Override
    public void openGameScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new GameView())
                .commit();
    }

    @Override
    public void openShopScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new ShopView())
                .commit();
    }
}
