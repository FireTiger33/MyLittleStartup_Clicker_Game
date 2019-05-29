package com.example.mylittlestartup.shop;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import com.example.mylittlestartup.data.sqlite.Upgrade;


class ShopSpeederElementsViewHolder extends ShopBaseElementsViewHolder {
    private final String tag = ShopUpgradeElementsViewHolder.class.getName();

    ShopSpeederElementsViewHolder(@NonNull final View itemView, final ShopContract.Presenter presenter) {
        super(itemView, presenter);

    }

    @Override
    void bind(Upgrade upgrade, boolean enoughMoney) {
        super.bind(upgrade, enoughMoney);
        Log.d(tag, "bind");
//        mDescriptionView.setText(upgrade.getDescription());
        mPriceView.setText(new StringBuilder("$ " + upgrade.getPrice()));
        if (mUpgrade.getCount() > 0) {
            mBuyButton = null;
            itemView.setForeground(new ColorDrawable(Color.argb(80, 128, 128, 128)));
        }
    }

}
