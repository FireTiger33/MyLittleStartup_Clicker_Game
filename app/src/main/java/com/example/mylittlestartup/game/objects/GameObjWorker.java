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
    private GameContract.Presenter mPresenter;

    public GameObjWorker(View itemView, Upgrade upgrade, final GameContract.Presenter presenter) {
        mPresenter = presenter;
        mItemView = itemView;
        mUpgrade = upgrade;
        mLogo = itemView.findViewById(R.id.worker_preview);
        pushButton = itemView.findViewById(R.id.push_button);

        onUpgraded(mUpgrade);
    }

    public void onUpgraded(Upgrade upgrade) {  // TODO
        mUpgrade = upgrade;
        Log.d(tag, "onUpgraded");
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushButton.setVisibility(View.INVISIBLE);
                mPresenter.onWorkerPushed(mUpgrade);
                show();
            }
        });
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "onClickItemView");
                mPresenter.onUpgradeWorker(mUpgrade);
            }
        });
        workTimer = new CountDownTimer(mUpgrade.getInterval(), mUpgrade.getInterval()) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(tag, "worker: " + mUpgrade.getId() + "pushUntilFinished = " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                pushButton.setVisibility(View.VISIBLE);
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

}
