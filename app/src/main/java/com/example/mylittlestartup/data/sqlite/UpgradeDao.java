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

    @Query("update Upgrade set mCount = mCount + 1," +
            "mValue = mValue * 2 + 100, " +
            "mInterval = mInterval + 10, " +
            "mPrice = (mPrice * 5)," +
            "mPicID = :picId where mId = :workerId")
    void upgradeWorker(int workerId, int picId);

    @Query("select * from Upgrade order by mId")
    public List<Upgrade> all();

    @Query("select * from Upgrade where mName != 'worker' order by mId")  // TODO
    public List<Upgrade> allUpgrades();

    @Query("select * from Upgrade where mId = :id")
    public List<Upgrade> worker(int id);

    @Query("select * from Upgrade where mName == 'worker' order by mId")
    public List<Upgrade> allWorkers();
}
