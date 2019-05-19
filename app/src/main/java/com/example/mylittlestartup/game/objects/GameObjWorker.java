package com.example.mylittlestartup.game.objects;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

public class GameObjWorker extends Upgrade {

    private ImageButton pushButton;
    private Upgrade mUpgrade;
    private ImageView mLogo;

    public GameObjWorker(View itemView, final GameContract.Presenter presenter) {
        pushButton = itemView.findViewById(R.id.push_button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onWorkerPushed(mUpgrade);
            }
        });
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onUpgradeWorker(mUpgrade);
            }
        });
    }

    void bind(Upgrade upgrade) {
        mUpgrade = upgrade;
        mLogo.setImageResource(upgrade.getPicID());
    }

}
