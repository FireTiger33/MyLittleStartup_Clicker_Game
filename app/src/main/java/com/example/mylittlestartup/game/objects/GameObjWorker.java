package com.example.mylittlestartup.game.objects;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

public class GameObjWorker extends Upgrade {
    private final String tag = GameObjWorker.class.getName();

    private ImageButton pushButton;
    private Upgrade mUpgrade;
    private ImageButton mLogo;
    private View mItemView;
    private CountDownTimer workTimer;

    public GameObjWorker(View itemView, Upgrade upgrade, final GameContract.Presenter presenter) {
        mItemView = itemView;
        mUpgrade = upgrade;
        Log.d(tag, "Create worker");
        mLogo = itemView.findViewById(R.id.worker_preview);
        pushButton = itemView.findViewById(R.id.push_button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushButton.setVisibility(View.INVISIBLE);
                Log.d(tag, "pushButtonClicked");
                presenter.onWorkerPushed(getUpgrade());
                show();
            }
        });
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "onClickItemView");
                presenter.onUpgradeWorker(getUpgrade());
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
    private Upgrade getUpgrade() {
        return mUpgrade;
    }
    public int getWorkerId() {
        return mUpgrade.getId();
    }

}
