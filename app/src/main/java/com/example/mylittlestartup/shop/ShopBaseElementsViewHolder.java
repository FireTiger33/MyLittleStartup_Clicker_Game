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
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.io.IOException;
import java.io.InputStream;

class ShopBaseElementsViewHolder extends RecyclerView.ViewHolder {
    final private String tag = ShopBaseElementsViewHolder.class.getName();

    ImageView mLogo;
    TextView mDescriptionView;
    TextView mPriceView;
    View mBuyButton;

    Upgrade mUpgrade;

    ShopBaseElementsViewHolder(@NonNull final View itemView, final ShopContract.Presenter presenter) {
        super(itemView);

        mLogo = itemView.findViewById(R.id.logo);
        mDescriptionView = itemView.findViewById(R.id.description);
        mPriceView = itemView.findViewById(R.id.price);
        mBuyButton = itemView.findViewById(R.id.buy_button);

        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.button_click_anim);
                v.startAnimation(animation);
                presenter.onBuyUpgrade(mUpgrade);
            }
        });
    }

    void setLogo() {
        try {
            Log.d(tag, "setLogo");
            AssetManager assetManager = mPriceView.getContext().getAssets();
            Log.d(tag, "setLogo: getAssets");
            InputStream inputStream = assetManager.open(/*"img/programmer_lvl1.svg"*/mUpgrade.getPicPath());
            Log.d(tag, "setLogo: openFile");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d(tag, "setLogo: decodedStream");
            if (bitmap == null) {
                Log.d(tag, "setLogo: bitmapDecodeInputStream == null");
                inputStream = assetManager.open(/*"img/programmer_lvl1.svg"*/mUpgrade.getPicPath());
                SVG svg = null;
                try {
                    svg = SVG.getFromInputStream(inputStream);
                    Log.d(tag, "setLogo: SVG decoded");
                } catch (SVGParseException e) {
                    e.printStackTrace();
                }
                if (svg != null) {
                    Picture picture = svg.renderToPicture();
                    PictureDrawable pictureDrawable = new PictureDrawable(picture);

                    mLogo.setImageDrawable(pictureDrawable);
                } else {
                    Log.d(tag, "setLogo: SVG == null");
                }
            } else {
                mLogo.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            Log.d(tag, "setLogo: impossible");
            e.printStackTrace();
        }
    }

    void bind(Upgrade upgrade, boolean enoughMoney) {
        mUpgrade = upgrade;
        setLogo();
        if (enoughMoney) {
            mPriceView.setTextColor(Color.GREEN);
        } else {
            mPriceView.setTextColor(Color.RED);
        }
    }
}
