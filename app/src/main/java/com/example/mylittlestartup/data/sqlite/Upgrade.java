package com.example.mylittlestartup.data.sqlite;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;


/**
 * в магазине:
 * - цена товара, она должна меняться в зависимости от уже купленных
 * - собстно количество
 * - описание того, что даёт
 * - сколько даёт
 * - за какой интервал
 * - id картинки
 */

@Entity
public class Upgrade {

    @PrimaryKey(autoGenerate = true)
    private int mId;

    private int mPrice;
    private String mName;
    private String mDescription;
    private int mCount;
    private int mInterval;
    private int mValue;
    private int mPicID;

    @Ignore
    public Upgrade(int price, String name, String description, int count, int interval, int value, int picID) {
        mPrice = price;
        mName = name;
        mDescription = description;
        mCount = count;
        mInterval = interval;
        mPicID = picID;
        mValue = value;
    }

    public Upgrade() {}

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
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

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getInterval() {
        return mInterval;
    }

    public void setInterval(int interval) {
        mInterval = interval;
    }

    public int getPicID() {
        return mPicID;
    }

    public void setPicID(int picID) {
        mPicID = picID;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        mValue = value;
    }
}
