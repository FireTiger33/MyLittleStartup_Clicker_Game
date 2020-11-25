package com.example.mylittlestartup.game.objects;

import android.view.View;
import android.widget.FrameLayout;

import com.example.mylittlestartup.game.GamePresenter;

import java.util.Random;

class BaseGameObj {

    class Point {
        float x;
        float y;
        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    View mView;
    int maxX, maxY;
    private Point mCoordinate;
    FrameLayout mContainer;
    GamePresenter mPresenter;
    int maxInterval;
    Random random;

    BaseGameObj(FrameLayout container, final GamePresenter presenter, View view, int maxIntervalSec) {
        mContainer = container;
        mView = view;
        mPresenter = presenter;
        maxInterval = maxIntervalSec * 1000;
        random = new Random();
        mCoordinate = new Point(0/*random.nextInt(500)*/, 0/*random.nextInt(400)*/);
    }

    void changeStartLocation() {
        maxX = mContainer.getWidth() - mView.getWidth();
        maxY = mContainer.getHeight() - mView.getHeight();
        mCoordinate.x = random.nextFloat()*maxX;
        mCoordinate.y = random.nextFloat()*maxY;
        mView.setX(mCoordinate.x);
        mView.setY(mCoordinate.y);
    }

    void destroy() {
        mContainer = null;
        mView = null;
        mPresenter = null;
        random = null;
        mCoordinate = null;
    }
}
