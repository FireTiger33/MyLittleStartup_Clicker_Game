package com.example.mylittlestartup.shop;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.io.IOException;
import java.io.InputStream;

class ShopUpgradeElementsViewHolder extends ShopBaseElementsViewHolder {
    private final String tag = ShopUpgradeElementsViewHolder.class.getName();

    private ImageView mLogo;
    private TextView mDescriptionView;
    private TextView mPriceView;
    private TextView mGoodsQuantity;

    private Upgrade mUpgrade;

    ShopUpgradeElementsViewHolder(@NonNull final View itemView, final ShopContract.Presenter presenter) {
        super(itemView, presenter);

        mLogo = itemView.findViewById(R.id.logo);
        mDescriptionView = itemView.findViewById(R.id.description);
        mPriceView = itemView.findViewById(R.id.price);
        mGoodsQuantity = itemView.findViewById(R.id.goods_quantity);
        Button mBuyButton = itemView.findViewById(R.id.buy_button);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.button_click_anim);
                v.startAnimation(animation);
                presenter.onBuyUpgrade(mUpgrade);
            }
        });
    }

    void bind(Upgrade upgrade, boolean enoughMoney) {
        super.bind(upgrade, enoughMoney);
        mDescriptionView.setText(upgrade.getDescription());
        mPriceView.setText(new StringBuilder("$ " + upgrade.getPrice()));
        mGoodsQuantity.setText(new StringBuilder("x " + upgrade.getCount()));
    }
}
