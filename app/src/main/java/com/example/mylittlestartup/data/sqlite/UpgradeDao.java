package com.example.mylittlestartup.data.sqlite;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UpgradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Upgrade... upgrades);

    @Query("update Upgrade set mCount = mCount + 1 where mId = :id")
    public void increaseCounter(int id);

    @Query("select * from Upgrade order by mId")
    public List<Upgrade> all();
}
