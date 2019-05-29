package com.example.mylittlestartup.data.sqlite;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AchievementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Achievement... achievements);

    @Query("select * from Achievement order by mType, mGoal asc")
    List<Achievement> all();

    @Query("select * from Achievement where mIsDone != 1 order by mType, mGoal asc")
    List<Achievement> fetchNotDone();

    @Query("select * from Achievement where mType = :type and mIsDone != 1 order by mType, mGoal asc")
    List<Achievement> fetchTypeNotDone(String type);

    @Query("update Achievement set mProgress = :progress where mType = :type")
    void updateProgress(int progress, String type);

    @Query("update Achievement set mProgress = :progress, mIsDone = 1 where mType = :type and :progress >= mGoal and mIsDone != 1")
    void achieveType(int progress, String type);
}
