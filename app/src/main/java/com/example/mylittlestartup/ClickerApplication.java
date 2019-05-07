package com.example.mylittlestartup;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;


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

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                addBasicUpgrades();
            }
        });
    }

    public void addBasicUpgrades() {
        UpgradeDao dao = mDbRepository.getUpgradeDao();
        List<Upgrade> upgrades = dao.all();
        if (upgrades.isEmpty()) {
            Upgrade[] basicUpgrades = {
                    new Upgrade(1000, "Кофемашина", "10$ / 2s", 0, 2000, R.drawable.shop_coffe),
                    new Upgrade(10000, "Менеждер", "100$ / 5s", 0, 5000, R.drawable.shop_man),
                    new Upgrade(1000, "Кофемашина", "10$ / 2s", 0, 2000, R.drawable.shop_coffe),
                    new Upgrade(10000, "Менеждер", "100$ / 5s", 0, 5000, R.drawable.shop_man),
                    new Upgrade(1000, "Кофемашина", "10$ / 2s", 0, 2000, R.drawable.shop_coffe),
                    new Upgrade(10000, "Менеждер", "100$ / 5s", 0, 5000, R.drawable.shop_man),
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
