package com.example.mylittlestartup;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;


import com.example.mylittlestartup.achievements.AchievementsManager;
import com.example.mylittlestartup.data.AchievementsRepository;
import com.example.mylittlestartup.data.AchievementsRepositoryImpl;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.PlayerRepository;
import com.example.mylittlestartup.data.PlayerRepositoryImpl;
import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.data.sqlite.Achievement;
import com.example.mylittlestartup.data.sqlite.AchievementDao;
import com.example.mylittlestartup.data.sqlite.DBRepository;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.data.sqlite.UpgradeDao;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClickerApplication extends Application {
    private ApiRepository mApiRepository;
    private DBRepository mDbRepository;
    private PlayerRepository mPlayerRepository;
    private AchievementsRepository mAchievementsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepository = new ApiRepository(getApplicationContext());
        mDbRepository = Room.databaseBuilder(this, DBRepository.class, "main_db")
                .fallbackToDestructiveMigration()
                .build();
        mPlayerRepository = new PlayerRepositoryImpl(this);
        mAchievementsRepository = new AchievementsRepositoryImpl(this);

        AchievementsManager.getInstance().initAM(mAchievementsRepository);

        // TODO: Only for debug!
        /*mPlayerRepository.setNotLoggedIn(new BaseCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });*/

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                addBasicUpgradesAndAchievements();
            }
        });
        AppExecutors.getInstance().scheduled().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Log.d("ACHIEVEMENTS", "Update achievements");
                AchievementsManager.getInstance().UpdateProgressInDB();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void addBasicUpgradesAndAchievements() {
        UpgradeDao upgradeDao = mDbRepository.getUpgradeDao();

        // TODO: Only for debug!
        //mDbRepository.clearAllTables();

        List<Upgrade> upgrades = upgradeDao.all();
        if (upgrades.isEmpty()) {
            Upgrade[] basicUpgrades = {
                    new Upgrade(1000, "Кофемашина", "10$ / 2s", 0, 2000, 10, "img/shop_coffee.svg"),
                    new Upgrade(10000, "Менеждер", "100$ / 5s", 0, 5000, 100, "img/shop_man.jpg"),
                    new Upgrade(1000, "Кофемашина", "20$ / 3s", 0, 3000, 20, "img/shop_coffee.svg"),
                    new Upgrade(10000, "Менеждер", "200$ / 8s", 0, 8000, 200, "img/shop_man.jpg"),
                    new Upgrade(1000, "Кофемашина", "30$ / 4s", 0, 4000, 30, "img/shop_coffee.svg"),
                    new Upgrade(10000, "Менеждер", "300$ / 12s", 0, 12000, 300, "img/shop_man.jpg"),
                    new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                    new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                    new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                    new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                    new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png")
            };

            upgradeDao.insert(basicUpgrades);
        }

        AchievementDao achievementDao = mDbRepository.geAchievementDao();

        List<Achievement> achievements = achievementDao.all();
        if (achievements.isEmpty()) {
            Achievement[] basicAchievements = {
                    new Achievement("click", "Continue, please", "10 clicks", 10, "clicks", ""),
                    new Achievement("click", "Not enough", "100 clicks", 100, "clicks", ""),
                    new Achievement("click", "Oh, my", "1000 clicks", 1000, "clicks", ""),
                    new Achievement("click", "Touch me", "10000 clicks", 10000, "clicks", ""),
                    new Achievement("click", "Is the screen broken?", "100000 clicks", 100000, "clicks", ""),

                    new Achievement("sound", "Stop The Music", "From LENNE & THE LEE KINGS with Love", 1, "Off music", ""),
                    new Achievement("sound", "DJ", "Scraaaaatch", 2, "Off/On music double", ""),

                    new Achievement("money", "First money", "Thank you, mom", 1000, "$", ""),
                    new Achievement("money", "Enough", "Lets buy macbook and go away", 5000, "$", ""),
                    new Achievement("money", "It's money", "10000 $", 10000, "$", ""),
                    new Achievement("money", "Good job", "Good boy", 20000, "$", ""),
                    new Achievement("money", "Success", "Please, pay the workers", 100000, "$", ""),
                    new Achievement("money", "Unicorn", "Congratulations! GAME OVER", 1000000000, "$", ""),
            };

            achievementDao.insert(basicAchievements);
            AchievementsManager.getInstance().initProcData();
        }
    }

    public ApiRepository getApis() {
        return mApiRepository;
    }

    public DBRepository getDb() {
        return mDbRepository;
    }

    public PlayerRepository getPlayerRepository() {
        return mPlayerRepository;
    }

    public static ClickerApplication from(Context context) {
        return (ClickerApplication) context.getApplicationContext();
    }
}
