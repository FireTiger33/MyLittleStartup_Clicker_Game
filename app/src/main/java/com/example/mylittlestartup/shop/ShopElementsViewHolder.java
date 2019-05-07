package com.example.mylittlestartup.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.sqlite.Upgrade;

class ShopElementsViewHolder extends RecyclerView.ViewHolder {
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

    void bind(Upgrade upgrade) {
        mUpgrade = upgrade;

        mLogo.setImageResource(upgrade.getPicID());
        mDescriptionView.setText(upgrade.getDescription());
        mPriceView.setText(String.valueOf(upgrade.getPrice()));
        mGoodsQuantity.setText(String.valueOf(upgrade.getCount()));
    }
}
