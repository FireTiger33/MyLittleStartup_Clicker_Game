package com.example.mylittlestartup;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;


import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.PlayerRepository;
import com.example.mylittlestartup.data.PlayerRepositoryImpl;
import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.executors.AppExecutors;
import com.example.mylittlestartup.data.sqlite.DBRepository;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.data.sqlite.UpgradeDao;

import java.util.List;

public class ClickerApplication extends Application {
    private ApiRepository mApiRepository;
    private DBRepository mDbRepository;
    private PlayerRepository mPlayerRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepository = new ApiRepository(getApplicationContext());
        mDbRepository = Room.databaseBuilder(this, DBRepository.class, "main_db")
                .fallbackToDestructiveMigration()
                .build();
        mPlayerRepository = new PlayerRepositoryImpl(this);

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
                addBasicUpgrades();
            }
        });
    }

    public void addBasicUpgrades() {
        UpgradeDao dao = mDbRepository.getUpgradeDao();

        // TODO: Only for debug!
        // mDbRepository.clearAllTables();

        List<Upgrade> upgrades = dao.all();
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

            dao.insert(basicUpgrades);
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
