package com.example.mylittlestartup;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;


import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.sqlite.DBRepository;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.data.sqlite.UpgradeDao;

import java.util.ArrayList;
import java.util.List;

public class ClickerApplication extends Application {
    private ApiRepository mApiRepository;
    private DBRepository mDbRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepository = new ApiRepository(getApplicationContext());
        mDbRepository = Room.databaseBuilder(this, DBRepository.class, "main_db")
                .allowMainThreadQueries() // TODO() don't forget to remove this line in release! Only for debug purpose!
                .fallbackToDestructiveMigration()
                .build();

        addBasicUpgrades();
    }

    public void addBasicUpgrades() {
        UpgradeDao dao = mDbRepository.getUpgradeDao();
        List<Upgrade> upgrades = dao.all();
        if (upgrades.isEmpty()) {
            Upgrade[] basicUpgrades = {
                    new Upgrade(1, 100, "hello", "world", 0, 1000, R.drawable.ic_launcher_background),
                    new Upgrade(2, 1000, "manager", "", 0, 10000, R.drawable.ic_launcher_foreground)
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

    public static ClickerApplication from(Context context) {
        return (ClickerApplication) context.getApplicationContext();
    }
}
