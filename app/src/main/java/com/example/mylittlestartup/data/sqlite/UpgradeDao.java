package com.example.mylittlestartup.data.sqlite;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UpgradeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Upgrade... upgrades);

    @Query("update Upgrade set mCount = mCount + 1 where mId = :id")
    void increaseCounter(int id);

    @Query("update Upgrade set mCount = mCount + 1," +
            "mValue = mValue * 2 + 100, " +
            "mInterval = mInterval + 5000, " +
            "mPrice = (mPrice * 3)," +
            "mPicID = :picId where mId = :workerId")
    void upgradeWorker(int workerId, int picId);

    @Query("update Upgrade set mCount = 0," +
            "mValue = 0, " +
            "mInterval = 5000, " +
            "mPrice = 2000," +
            "mPicID = :picId where mId = :workerId")
    void layOffWorker(int workerId, int picId);

    @Query("select * from Upgrade order by mId")
    List<Upgrade> all();

    @Query("select * from Upgrade where mName != 'worker' order by mId")  // TODO
    List<Upgrade> allUpgrades();

    @Query("select * from Upgrade where mId = :id")
    List<Upgrade> worker(int id);

    @Query("select * from Upgrade where mName == 'worker' order by mId")
    List<Upgrade> allWorkers();
}
