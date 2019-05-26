package com.example.mylittlestartup.data.sqlite;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import com.example.mylittlestartup.ClickerApplication;

@Database(entities = {Achievement.class, Upgrade.class}, version = 4)
public abstract class DBRepository extends RoomDatabase {
    public abstract UpgradeDao getUpgradeDao();
    public abstract AchievementDao geAchievementDao();

    public static DBRepository from(Context context) {
        return ClickerApplication.from(context).getDb();
    }
}