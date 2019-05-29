package com.example.mylittlestartup.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.util.List;


public class ShopElementsAdapter extends RecyclerView.Adapter<ShopBaseElementsViewHolder> {
    private List<Upgrade> mUpgrades;
    private ShopContract.Presenter mPresenter;

    ShopElementsAdapter(List<Upgrade> upgrades, ShopContract.Presenter presenter) {
        mUpgrades = upgrades;
        mPresenter = presenter;
    }

    @NonNull
    @Override
    public ShopBaseElementsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = null;
        if (mUpgrades.get(i).getName().equals("upgrade")) {
            view = inflater.inflate(R.layout.shop_element_upgrade, viewGroup, false);
            return new ShopUpgradeElementsViewHolder(view, mPresenter);
        } else if (mUpgrades.get(i).getName().equals("speeder")) {
            view = inflater.inflate(R.layout.shop_element_speeder, viewGroup, false);
            return new ShopSpeederElementsViewHolder(view, mPresenter);
        }

        return new ShopBaseElementsViewHolder(view, mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull final ShopBaseElementsViewHolder shopBaseElementsViewHolder, int i) {
        final Upgrade upgrade = mUpgrades.get(i);
        mPresenter.checkEnoughMoney(mUpgrades.get(i).getPrice(), new BaseCallback() {
            @Override
            public void onSuccess() {
                shopBaseElementsViewHolder.bind(upgrade, true);
            }

            @Override
            public void onError() {
                shopBaseElementsViewHolder.bind(upgrade, false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUpgrades.size();
    }

    void incrementUpgradeCounter(int upgradeID) {
        for (int i = 0; i < mUpgrades.size(); i++) {
            Upgrade upgrade = mUpgrades.get(i);

            if (upgrade.getId() == upgradeID+1) {
                upgrade.setCount(upgrade.getCount() + 1);
                super.notifyItemChanged(i);
                break;
            }
        }
    }
}
