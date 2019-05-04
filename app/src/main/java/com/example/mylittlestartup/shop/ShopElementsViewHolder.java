package com.example.mylittlestartup.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylittlestartup.R;

class ShopElementsViewHolder extends RecyclerView.ViewHolder {
    private ImageView logo;
    private TextView descriptionView;
    private TextView priceView;

    ShopElementsViewHolder(@NonNull View itemView) {
        super(itemView);
        logo = itemView.findViewById(R.id.logo);
        descriptionView = itemView.findViewById(R.id.description);
        priceView = itemView.findViewById(R.id.price);
    }

    void bind(int resIdLogo, String description, int price) {
        logo.setImageResource(resIdLogo);
        descriptionView.setText(description);
        priceView.setText(String.valueOf(price));
    }
}
