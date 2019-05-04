package com.example.mylittlestartup.game;

import android.os.Bundle;
import android.os.CountDownTimer;
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
    private CountDownTimer workTime;

    private Button shopButton;
    private ImageButton clickLocation;
    private Button touchLocation;
    private TextView moneyValView;
    private int money;
    private int k;  // coefficient up money on click

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new GamePresenter(this);

        workTime = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) { }
            @Override
            public void onFinish() { isDoneWork(); }
        };
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        money = 0;  // TODO DB
        k = 1;  // TODO DB

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
            public void onClick(View v) {  // TODO move to presenter
                money += k;
                moneyValView.setText(String.valueOf(money));
            }
        });

        workTime.start();

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

    private void isDoneWork() {
        money += 10;
        moneyValView.setText(String.valueOf(money));
        workTime.start();
    }
}
