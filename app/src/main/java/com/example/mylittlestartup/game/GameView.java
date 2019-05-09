package com.example.mylittlestartup.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;

import java.util.Random;


public class GameView extends Fragment implements GameContract.View {
    String tag = GameView.class.getName();

    private GamePresenter presenter;

    private Button shopButton;
    private ImageButton clickLocation;
    private ImageView touchLocation;  // TODO for buster in osu style
    private Button touchArea;
    private TextView moneyValView;

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

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter.onGameStart();

        View view = inflater.inflate(R.layout.fragment_game, container, false);

        shopButton = view.findViewById(R.id.button_shop);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShopButtonClicked();
            }
        });

        moneyValView = view.findViewById(R.id.money_val);
        moneyValView.setText("0");

        touchArea = view.findViewById(R.id.touch_area);
        touchArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onSpecClickAreaClicked();
                refreshTouchAreaPos();
            }
        });

        touchLocation = view.findViewById(R.id.touch_location);
        touchLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                presenter.onTouchLocationTouched(motionEvent.getX(),
                        motionEvent.getY(),
                        motionEvent.getAction());

                return true;
            }
        });

        clickLocation = view.findViewById(R.id.click_location);
        clickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { presenter.onCommonClickLocationClicked();
            }
        });


        return view;
    }

    private void refreshTouchAreaPos() {
        int numHorizontalStates = 13;
        int numVerticalStates = 6;
        int locationWidth = clickLocation.getWidth();
        int locationHeight = clickLocation.getHeight();
        Random random = new Random();
        int newPosHorizontal = locationWidth / numHorizontalStates
                * (random.nextInt(numHorizontalStates));
        int newPosVertical = locationHeight / numVerticalStates
                * (random.nextInt(numVerticalStates));

        touchArea.setX(newPosHorizontal);
        touchArea.setY(newPosVertical);
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
    public void onResume() {
        super.onResume();
        presenter.onGameStart();
    }

    @Override
    public void onDestroy() {
        presenter.onGamePause();
        super.onDestroy();
    }
}
