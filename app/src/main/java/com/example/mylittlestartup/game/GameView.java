package com.example.mylittlestartup.game;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;


public class GameView extends Fragment implements GameContract.View {
    private GamePresenter presenter;

    private Button shopButton;
    private ImageButton clickLocation;
    private Button touchLocation;  // TODO for busted in osu style
    private TextView moneyValView;
    private View view;
    private int k;  // coefficient up money on click

    @Nullable
    @Override
    public Context getAppContext() {
        return getContext().getApplicationContext();
    }

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new GamePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.onGameStart();

        view = inflater.inflate(R.layout.fragment_game, container, false);

        k = 100;  // TODO DB

        shopButton = view.findViewById(R.id.button_shop);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShopButtonClicked();
            }
        });

        moneyValView = view.findViewById(R.id.money_val);
        moneyValView.setText("0");

        clickLocation = view.findViewById(R.id.click_location);
        clickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addMoney(k);
            }
        });

        return view;
    }

    @Override
    public void showShopScreen() {
        Router router = (Router) getActivity();
        if (router != null) {
            router.openShopScreen();
        } else {
            Log.e(getTag(), "This activity is not a Router");
        }
    }


    @Override
    public void setMoney(int money) {
        moneyValView.setText(String.valueOf(money));
    }

    @Override
    public void onPause() {
        presenter.onGamePause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onGamePause();
        super.onDestroy();
    }
}
