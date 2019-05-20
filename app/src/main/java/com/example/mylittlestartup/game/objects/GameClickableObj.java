package com.example.mylittlestartup.game.objects;

import android.animation.ValueAnimator;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.mylittlestartup.game.GamePresenter;

import java.util.Random;

public class GameClickableObj extends BaseGameObj implements Runnable{
    private final String tag = GameClickableObj.class.getName();

    private CountDownTimer timer;

    public GameClickableObj(FrameLayout container, final GamePresenter presenter, View view, int maxIntervalSec) {
        super(container, presenter, view, maxIntervalSec);
        mView.setVisibility(View.INVISIBLE);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                mPresenter.onSpecClickAreaClicked(mView.getX(), mView.getY());
                run();
            }
        });
    }

    @Override
    public void run() {
        int interval = random.nextInt(maxInterval);

        timer = new CountDownTimer(interval, interval) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                changeStartLocation();
                showAnimAppearance();
            }
        };
        timer.start();
    }

    private void showAnimAppearance() {
        mView.setAlpha(0);
        mView.setVisibility(View.VISIBLE);
        ValueAnimator viewAnimator = ValueAnimator.ofFloat(0, 1).setDuration(1000);
        viewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mView.setAlpha((float)animation.getAnimatedValue());
            }
        });
        viewAnimator.start();
    }

    @Override
    public void destroy() {
        stop();
        super.destroy();
        timer = null;
    }

    public void pause() {
        timer.cancel();
    }

    public void resume() {
        timer.cancel();
        timer.start();
    }

    private void stop() {
        mView.setVisibility(View.INVISIBLE);
        timer.cancel();
    }
}
