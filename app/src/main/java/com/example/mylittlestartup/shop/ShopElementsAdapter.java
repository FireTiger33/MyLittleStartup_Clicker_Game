package com.example.mylittlestartup.shop;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class ShopElementsAdapter extends RecyclerView.Adapter<ShopBaseElementsViewHolder> {
    private final String tag = ShopElementsAdapter.class.getName();

    private final String NAME_UPGRADE = "upgrade";
    private final String NAME_SPEEDER = "speeder";
    private final short TYPE_UPGRADE = 0;
    private final short TYPE_SPEEDER = 1;

    private List<Upgrade> mUpgrades;
    private ShopContract.Presenter mPresenter;

    ShopElementsAdapter(ShopContract.Presenter presenter) {
        mUpgrades = new ArrayList<>();
        mPresenter = presenter;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUpgrades.get(position).getName().equals(NAME_UPGRADE)) return TYPE_UPGRADE;
        return TYPE_SPEEDER;
    }

    @NonNull
    @Override
    public ShopBaseElementsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(tag, "onCreateViewHolder: viewType = " + viewType);
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = null;
        if (viewType == TYPE_UPGRADE) {
            view = inflater.inflate(R.layout.shop_element_upgrade, viewGroup, false);
            return new ShopUpgradeElementsViewHolder(view, mPresenter);
        } else if (viewType == TYPE_SPEEDER) {
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
        Log.d(tag, "getItemCount: " + mUpgrades.size());
        return mUpgrades.size();
    }

    void addElementsToShop(List<Upgrade> upgrades) {
        Log.d(tag, "addElementsToShop: " + upgrades.size());
        int oldCount = mUpgrades.size();
        mUpgrades.addAll(upgrades);
        notifyItemRangeChanged(oldCount, mUpgrades.size());
    }

    void incrementUpgradeCounter(int upgradeID) {
        for (int i = 0; i < mUpgrades.size(); i++) {
            Upgrade upgrade = mUpgrades.get(i);

            if (upgrade.getId() == upgradeID+1) {
                upgrade.setCount(upgrade.getCount() + 1);
                notifyItemRangeChanged(0, mUpgrades.size());
                break;
            }
        }
    }
}
