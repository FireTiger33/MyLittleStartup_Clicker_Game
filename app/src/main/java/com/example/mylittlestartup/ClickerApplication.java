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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

    public  Upgrade[] concat(Upgrade[] A, Upgrade[] B) {
        int aLen = A.length;
        int bLen = B.length;

        @SuppressWarnings("unchecked")
        Upgrade[] C = (Upgrade[]) Array.newInstance(A.getClass().getComponentType(), aLen+bLen);
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);

        return C;
    }

    public void addBasicUpgrades() {
        UpgradeDao dao = mDbRepository.getUpgradeDao();
        boolean refreshDB = false;
        Upgrade[] basicUpgrades = {
                new Upgrade(1000, "upgrade", "5$ / 1s", 0, 1000, 5, "img/shop_coffee.svg"),
                new Upgrade(10000, "upgrade", "100$ / 5s", 0, 5000, 100, "img/shop_man.jpg"),
                new Upgrade(2000, "upgrade", "12$ / 2s", 0, 2000, 12, "img/shop_coffee.svg"),
                new Upgrade(20000, "upgrade", "220$ / 8s", 0, 8000, 220, "img/shop_man.jpg"),
                new Upgrade(4000, "upgrade", "20$ / 3s", 0, 3000, 20, "img/shop_coffee.svg"),
                new Upgrade(30000, "upgrade", "400$ / 12s", 0, 12000, 400, "img/shop_man.jpg"),
        };
        Upgrade[] basicSpeeders = {
                new Upgrade(20000, "speeder", "2", 0, 0, 2, "img/telegram.svg"),
                new Upgrade(50000, "speeder", "3", 0, 0, 3, "img/github.svg"),
        };
        Upgrade[] basicWorkers = {
                new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png"),
                new Upgrade(2000, "worker", "ничего", 0, 5000, 0, "img/worker_avatar.png")
        };

        // TODO: Only for debug!
        // mDbRepository.clearAllTables();

        Upgrade[] all = new Upgrade[0];

        List<Upgrade> upgrades = dao.allUpgrades();
        if (upgrades.isEmpty()) {
            refreshDB = true;
            all = concat(all, basicUpgrades);
        } else {
            all = concat(all, basicUpgrades);
//            all.addAll(upgrades);
        }

        List<Upgrade> speeders = dao.allSpeeders();
        if (speeders.isEmpty()) {
            refreshDB = true;
            all = concat(all, basicSpeeders);
        } else {
            all = concat(all, basicSpeeders);
//            all.addAll(speeders);
        }

        List<Upgrade> workers = dao.allWorkers();
        if (workers.isEmpty()) {
            refreshDB = true;
            all = concat(all, basicWorkers);
        } else {
            all = concat(all, basicWorkers);
//            all.addAll(workers);
        }

        if (refreshDB) {

            dao.insert(all);
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
