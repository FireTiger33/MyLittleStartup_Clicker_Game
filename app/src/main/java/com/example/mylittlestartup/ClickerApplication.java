package com.example.mylittlestartup;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;


import com.example.mylittlestartup.data.api.ApiRepository;
import com.example.mylittlestartup.data.sqlite.DBRepository;

public class ClickerApplication extends Application {
    private ApiRepository mApiRepository;
    private DBRepository mDbRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mApiRepository = new ApiRepository();
        mDbRepository = Room.databaseBuilder(this, DBRepository.class, "main_db")
                .allowMainThreadQueries() // TODO() don't forget to remove this line in release! Only for debug purpose!
                .fallbackToDestructiveMigration()
                .build();
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
