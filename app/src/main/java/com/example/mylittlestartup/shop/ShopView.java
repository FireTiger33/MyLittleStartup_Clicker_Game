package com.example.mylittlestartup.shop;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mylittlestartup.R;

public class ShopView extends Fragment {
    private TextView moneyValView;

    private RecyclerView shopView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter shopElementsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int[] logos = {
                R.drawable.shop_coffe,
                R.drawable.shop_man
        };
        String[] descriptions = {
                "10$ / 2s",
                "100$ / 5s"
        };
        int[] prices  = {  // TODO price change depending on the quantity that is taken from DB
                1000,
                10000
        };

        shopElementsAdapter = new ShopElementsAdapter(2, logos, descriptions, prices);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_container, container, false);

        moneyValView = view.findViewById(R.id.money_val);
        moneyValView.setText(String.valueOf(100000));

        layoutManager = new LinearLayoutManager(this.getContext());

        shopView = view.findViewById(R.id.shop_view);
        shopView.setAdapter(shopElementsAdapter);
        shopView.setLayoutManager(layoutManager);

        return view;
    }
}
