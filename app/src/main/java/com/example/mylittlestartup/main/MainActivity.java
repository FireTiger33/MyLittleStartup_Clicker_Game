package com.example.mylittlestartup.main;

import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mylittlestartup.AppActions;
import com.example.mylittlestartup.ClickerApplication;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;
import com.example.mylittlestartup.achievements.AchievementsView;
import com.example.mylittlestartup.authorization.AuthContract;
import com.example.mylittlestartup.authorization.AuthView;
import com.example.mylittlestartup.data.PlayerRepository;
import com.example.mylittlestartup.game.GameView;
import com.example.mylittlestartup.leaderboard.LeaderboardView;
import com.example.mylittlestartup.settings.SettingsView;
import com.example.mylittlestartup.shop.ShopView;

public class MainActivity extends AppCompatActivity implements Router, AppActions {
    String tag = MainActivity.class.getName();

    private MediaPlayer musicPlayer;
    private boolean isGameScreenStarted = false;

    private PlayerRepository playerRepository;

    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerRepository = ClickerApplication.from(getApplicationContext()).getPlayerRepository();

        fragmentManager.beginTransaction()
                .replace(R.id.main_activity, new MainFragment())
                .commit();

        musicOnMainSound();
    }

    private void clearMusicPlayer() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = null;
        }
    }

    private void musicOnMainSound() {
        if (playerRepository.isMusicSoundState()) {
            Log.d(tag, "musicOnMainSound: soundOn");
            clearMusicPlayer();
            musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.main_sound_128kbit);
            musicPlayer.start();
        } else {
            Log.d(tag, "musicOnMainSound: soundOff");
        }
    }

    private void musicOnGameSound() {
        if (playerRepository.isMusicSoundState()) {
            Log.d(tag, "musicOnGameSound: soundOn");
            clearMusicPlayer();
            musicPlayer = MediaPlayer.create(getApplicationContext(), R.raw.game_sound);
            musicPlayer.start();
        } else {
            Log.d(tag, "musicOnMainSound: soundOff");
        }
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
    public void openLeaderboardScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new LeaderboardView())
                .commit();
    }

    @Override
    public void openGameScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new GameView())
                .commit();
        musicOnGameSound();
        isGameScreenStarted = true;
    }

    @Override
    public void openShopScreen() {
        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.main_activity, new ShopView())
                .commit();
    }

    @Override
    public void openMainScreen() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
        musicOnMainSound();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicPlayer != null) {
            musicPlayer.start();
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 1 && isGameScreenStarted) {
            openMainScreen();
            isGameScreenStarted = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
        super.onPause();
    }

    @Override
    public void musicSoundOff() {
        Log.d(tag, "Music Off");
        playerRepository.setMusicSoundStateOff();
        clearMusicPlayer();
    }

    @Override
    public void musicSoundOn() {
        Log.d(tag, "Music On");
        playerRepository.setMusicSoundStateOn();
    }
}
