package com.example.mylittlestartup.data.sqlite;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Achievement {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    private String mType;
    private String mTitle;
    private String mDescription;
    private int mGoal;
    private int mProgress;
    private String mUnit;
    private boolean mIsDone;
    private String mImageTag;

    @Ignore
    public Achievement(String type, String title, String description, int goal, String unit, String imageTag) {
        this.mType = type;
        this.mTitle = title;
        this.mDescription = description;
        this.mGoal = goal;
        this.mProgress = 0;
        this.mUnit = unit;
        this.mIsDone = false;
        this.mImageTag = imageTag;
    }

    public Achievement() {
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getGoal() {
        return mGoal;
    }

    public void setGoal(int goal) {
        this.mGoal = goal;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public String getUnit() {
        return mUnit;
    }

    public void setUnit(String unit) {
        this.mUnit = unit;
    }

    public boolean isIsDone() {
        return mIsDone;
    }

    public void setIsDone(boolean isDone) {
        this.mIsDone = isDone;
    }

    public String getImageTag() {
        return mImageTag;
    }

    public void setImageTag(String image) {
        mImageTag = image;
    }
}
