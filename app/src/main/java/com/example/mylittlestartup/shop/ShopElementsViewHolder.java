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
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.io.IOException;
import java.io.InputStream;

class ShopElementsViewHolder extends RecyclerView.ViewHolder {
    private final String tag = ShopElementsViewHolder.class.getName();

    private ImageView mLogo;
    private TextView mDescriptionView;
    private TextView mPriceView;
    private TextView mGoodsQuantity;

    private Upgrade mUpgrade;

    ShopElementsViewHolder(@NonNull View itemView, final ShopContract.Presenter presenter) {
        super(itemView);

        mLogo = itemView.findViewById(R.id.logo);
        mDescriptionView = itemView.findViewById(R.id.description);
        mPriceView = itemView.findViewById(R.id.price);
        mGoodsQuantity = itemView.findViewById(R.id.goods_quantity);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBuyUpgrade(mUpgrade);
            }
        });
    }

    private void setLogo() {
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
        mDescriptionView.setText(upgrade.getDescription());
        mPriceView.setText(new StringBuilder("$ " + upgrade.getPrice()));
        mGoodsQuantity.setText(new StringBuilder("x " + upgrade.getCount()));
    }
}
