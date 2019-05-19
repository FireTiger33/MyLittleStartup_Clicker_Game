package com.example.mylittlestartup.game.objects;

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

    public GameObjWorker(View itemView, Upgrade upgrade, final GameContract.Presenter presenter) {
        mItemView = itemView;
        mUpgrade = upgrade;
        mLogo = itemView.findViewById(R.id.worker_preview);
        pushButton = itemView.findViewById(R.id.push_button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onWorkerPushed(mUpgrade);
            }
        });
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "onClickItemView");
                presenter.onUpgradeWorker(mUpgrade);
            }
        });
        onWorkerUpgraded(mUpgrade);
    }

    public void onWorkerUpgraded(Upgrade upgrade) {  // TODO
        mUpgrade = upgrade;
        Log.d(tag, "Worker LVL: " + mUpgrade.getCount());
        mLogo.setImageResource(upgrade.getPicID());
    }

    public int getWorkerId() {
        return mUpgrade.getId();
    }

}
