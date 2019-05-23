package com.example.mylittlestartup.game.objects;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

public class GameObjWorker extends Upgrade {
    private final String tag = GameObjWorker.class.getName();

    private final ImageButton pushButton;
    private Upgrade mUpgrade;
    private final GameContract.Presenter mPresenter;
    private ImageButton mLogo;
    private final View mItemView;
    private CountDownTimer workTimer;
    private final int[] pushAnimateEndCoordinate;

    public GameObjWorker(View itemView, int[] endCoordinateForPushAnimation, Upgrade upgrade, final GameContract.Presenter presenter) {
        mPresenter = presenter;
        mItemView = itemView;
        mUpgrade = upgrade;
        pushAnimateEndCoordinate = endCoordinateForPushAnimation;
        Log.d(tag, "Create worker");
        mLogo = itemView.findViewById(R.id.worker_preview);
        pushButton = itemView.findViewById(R.id.push_button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "pushButtonClicked");
                onWorkerPushed();
                show();
            }
        });
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "onClickItemView");
                mPresenter.onUpgradeWorker(getUpgrade());
            }
        });

        onUpgraded(mUpgrade);
    }

    public void onUpgraded(Upgrade upgrade) {
        mUpgrade = upgrade;
        Log.d(tag, "onUpgraded");
        workTimer = new CountDownTimer(mUpgrade.getInterval(), mUpgrade.getInterval()) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(tag, "worker: " + mUpgrade.getId() + "pushUntilFinished = " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (mUpgrade.getCount() > 0) {
                    pushButton.setVisibility(View.VISIBLE);
                }
            }
        };
        show();
    }

    public void show() {
        Log.d(tag, "show: " + mUpgrade.getCount());
        mLogo.setImageResource(mUpgrade.getPicID());
        workTimer.start();
    }

    public int getWorkerId() {
        return mUpgrade.getId();
    }

    private void onWorkerPushed() {
        int[] viewEndPos = new int[2];
        pushButton.getLocationOnScreen(viewEndPos);
        viewEndPos[0] -= pushAnimateEndCoordinate[0];
        viewEndPos[1] -= pushAnimateEndCoordinate[1];
        TranslateAnimation pushAnimation = new TranslateAnimation(0, -viewEndPos[0], 0, -viewEndPos[1]);
        pushAnimation.setDuration(1500);
        pushAnimation.setInterpolator(new AccelerateInterpolator());
        pushAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationEnd(Animation animation) {
                pushButton.setVisibility(View.INVISIBLE);
                mPresenter.onWorkerPushed(mUpgrade);
            }

        });
        pushButton.startAnimation(pushAnimation);
    }

    private Upgrade getUpgrade() {
        return mUpgrade;
    }

}
