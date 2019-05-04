package com.example.mylittlestartup.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylittlestartup.R;


public class ShopElementsAdapter extends RecyclerView.Adapter<ShopElementsViewHolder> {
    private int numElements;
    private int[] logos;
    private String[] descriptions;
    private int[] prices;

    ShopElementsAdapter(int numElements, int[] logos, String[] descriptions, int[] prices) {
        this.numElements = numElements;
        this.logos = logos;
        this.descriptions = descriptions;
        this.prices = prices;
    }

    @NonNull
    @Override
    public ShopElementsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.shop_element, viewGroup, false);

        return new ShopElementsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopElementsViewHolder shopElementsViewHolder, int i) {
        shopElementsViewHolder.bind(logos[i], descriptions[i], prices[i]);
    }

    @Override
    public int getItemCount() {
        return numElements;
    }
}
