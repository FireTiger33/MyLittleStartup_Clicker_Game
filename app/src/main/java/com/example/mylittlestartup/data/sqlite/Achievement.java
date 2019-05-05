package com.example.mylittlestartup.data.sqlite;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Achievement {

    @PrimaryKey
    private int mId;
    private String mName;
    private String mDescription;
    private int mImage;

    public Achievement(String name, String description, int image) {
        mName = name;
        mDescription = description;
        mImage = image;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int image) {
        mImage = image;
    }
}
