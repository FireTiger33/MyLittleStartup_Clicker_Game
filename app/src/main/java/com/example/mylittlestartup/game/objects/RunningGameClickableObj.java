package com.example.mylittlestartup.game.objects;

import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.util.Log;

import com.example.mylittlestartup.game.GamePresenter;

public class RunningGameClickableObj extends BaseGameObj implements Runnable {
    private final String tag = RunningGameClickableObj.class.getName();

    private CountDownTimer appearanceTimer;
    private ValueAnimator objAnimX, objAnimY;
    private int dirX, dirY;
    private int hp;
    private int maxHP;
    private float dx, dy;
    private int maxDxDy;
    private CountDownTimer runTimer;
    private boolean runTimerHasBeenPaused = false;
    private long runTimerUntilFinished;
    final private int[] foregroundColorSet;

    public RunningGameClickableObj(FrameLayout container, final GamePresenter presenter,
                                   View view, int maxIntervalSec, int clicksToDIe, int maxSpeed,
                                   final int[] foregroundColorSet) {
        super(container, presenter, view, maxIntervalSec);
        maxHP = clicksToDIe;
        maxDxDy = maxSpeed;
        this.foregroundColorSet = foregroundColorSet;
        mView.setVisibility(View.INVISIBLE);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                hp--;
                if (hp == 0) {
                    new CountDownTimer(1000, 1000 / maxHP) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            presenter.onSpecClickAreaClicked(mView.getX(), mView.getY());
                        }

                        @Override
                        public void onFinish() {

                        }
                    }.start();
                    stop();
                    run();
                } else {
                    mView.setForeground(new ColorDrawable(foregroundColorSet[hp - 1]));
                }
            }
        });
        dirX = 1;
        dirY = 1;
        changeDxDy();

        runTimer = createNewRunTimer(10000, 500);
        objAnimX = ValueAnimator.ofInt(0, 10);
        objAnimX.setRepeatCount(ValueAnimator.INFINITE);
        objAnimX.setDuration(100L);
        objAnimY = ValueAnimator.ofInt(0, 10);
        objAnimY.setRepeatCount(ValueAnimator.INFINITE);
        objAnimY.setDuration(30L);
        objAnimX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float dt;
                int dir = getDirX();
                float pos = mView.getX();
                dt = getDX();
                if (pos + dt * dir > maxX || pos + dt * dir < 0) {
                    dirX = -dirX;
                    dir = -dir;
                    changeDxDy();
                }
                mView.setX(pos + dt * dir);
            }
        });
        objAnimY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float dt;
                int dir = getDirY();
                float pos = mView.getY();
                dt = getDY();
                if (pos + dt * dir > maxY || pos + dt * dir < 0) {
                    dirY = -dirY;
                    dir = -dir;
                    changeDxDy();
                }
                mView.setY(pos + dt * dir);
            }
        });
    }

    @Override
    public void run() {
        hp = maxHP;
        appearanceTimer = new CountDownTimer(random.nextInt(maxInterval), 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished <= 1000) {
                    if (mContainer.getForeground() != null) {
                        mContainer.setForeground(null);
                    } else {
                        mContainer.setForeground(new ColorDrawable(foregroundColorSet[maxHP - 1]));
                    }
                }
            }

            @Override
            public void onFinish() {
                mContainer.setForeground(null);
                changeStartLocation();
                mView.setForeground(new ColorDrawable(foregroundColorSet[maxHP - 1]));
                mView.setVisibility(View.VISIBLE);
                runTimer.start();
                objAnimX.start();
                objAnimY.start();
            }
        };
        appearanceTimer.start();
    }

    @Override
    public void destroy() {
        stop();
        super.destroy();
        appearanceTimer = null;
        runTimer = null;
        objAnimY = null;
        objAnimX = null;
    }

    private void changeDxDy() {
        dx = random.nextInt(maxDxDy) + 5;
        dy = random.nextInt(maxDxDy) + 5;
    }

    private float getDX() {
        return dx;
    }

    private float getDY() {
        return dy;
    }

    private int getDirX() {
        return dirX;
    }

    private int getDirY() {
        return dirY;
    }

    private CountDownTimer createNewRunTimer(long duration, long downInterval) {
        return new CountDownTimer(duration, downInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                mPresenter.onBugIsAlive();
                runTimerUntilFinished = millisUntilFinished;
//                mView.setVisibility((mView.getVisibility() == View.VISIBLE? View.INVISIBLE:View.VISIBLE));
            }

            @Override
            public void onFinish() {
                for (int i = 0; i < 25 * hp; i++) {
                    mPresenter.onBugIsAlive();
                }
                stop();
                run();
            }
        };
    }

    public void pause() {
        Log.d(tag, "pause");
        runTimer.cancel();
        runTimerHasBeenPaused = true;
        objAnimX.pause();
        objAnimY.pause();
        appearanceTimer.cancel();
    }

    public void resume() {
        Log.d(tag, "resume");
        if (runTimerHasBeenPaused) {
            Log.d(tag, "runWillBePaused");
            runTimerHasBeenPaused = false;
            runTimer = createNewRunTimer(runTimerUntilFinished, 500);
            runTimer.start();
            objAnimX.resume();
            objAnimY.resume();
        }
//        appearanceTimer.cancel();
//        appearanceTimer.start();
    }

    private void stop() {
        mView.setVisibility(View.INVISIBLE);
        runTimer.cancel();
        appearanceTimer.cancel();
        objAnimX.cancel();
        objAnimY.cancel();
    }

}
