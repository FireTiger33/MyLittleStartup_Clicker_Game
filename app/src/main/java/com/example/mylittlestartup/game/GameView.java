package com.example.mylittlestartup.game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;
import com.example.mylittlestartup.game.objects.GameClickableObj;
import com.example.mylittlestartup.game.objects.RunningGameClickableObj;
//import com.example.mylittlestartup.game.objects.GameFollowingObj;


public class GameView extends Fragment implements GameContract.View {
    String tag = GameView.class.getName();

    private View mView;
    private GamePresenter presenter;
    private Button shopButton;
    private FrameLayout touchLocation;  // container for Objects
    private TextView moneyValView;
    private ValueAnimator moneyViewAnimator;
    private ViewFlipper monitorView;
    private final int[] monitorImagesId = {
            R.drawable.monitor_step_0,
            R.drawable.monitor_step_1,
            R.drawable.monitor_step_2
    };

    private GameClickableObj gameClickableObj;
    private RunningGameClickableObj runningGameClickableObj;
//    private GameFollowingObj followingObj;

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

        mView = inflater.inflate(R.layout.fragment_game, container, false);

        shopButton = mView.findViewById(R.id.button_shop);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShopButtonClicked();
            }
        });

        moneyValView = mView.findViewById(R.id.money_val);
        moneyValView.setText("0");

        touchLocation = mView.findViewById(R.id.touch_location);

        monitorView = mView.findViewById(R.id.monitor);
        monitorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    presenter.onCommonClickLocationClicked(event.getX(), event.getY());
                }

                return true;
            }
        });

        // Add images with code on monitor
        for (int i: monitorImagesId) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(i);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            monitorView.addView(imageView);
        }
        monitorView.startFlipping();

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createIssueButton();
        createBugButton();
    }

    @Override
    public void showMoneyPulsAnim() {
        final float defTextSize = moneyValView.getTextSize()/3;
        moneyViewAnimator = ValueAnimator.ofFloat(defTextSize, defTextSize+10).setDuration(150L);
        moneyViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moneyValView.setTextSize((float)animation.getAnimatedValue());
            }
        });
        moneyViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moneyValView.setTextSize(defTextSize);
            }
        });
        moneyViewAnimator.start();
    }

    private void createIssueButton() {
        gameClickableObj = new GameClickableObj(monitorView, presenter,
                mView.findViewById(R.id.issue), 60);
        gameClickableObj.run();
    }

    private void createBugButton() {
        int[] colorSet = {
                Color.argb(80, 0, 255, 0),
                Color.argb(80, 160, 214, 0),
                Color.argb(80, 215, 167, 0),
                Color.argb(80, 246, 109, 0),
                Color.argb(80, 255, 0, 0)
        };
        runningGameClickableObj = new RunningGameClickableObj(monitorView, presenter,
                mView.findViewById(R.id.bug), 60, 5, colorSet);
        runningGameClickableObj.run();
    }

    private TextView getRunningMoneyView() {
        TextView view = new TextView(touchLocation.getContext());
        view.setZ(10);
        view.setTextColor(Color.argb(255, 0, 130, 47));
        view.setTextSize(25);

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
        moneyValView.setText("$ " + money);
    }

    @Override
    public void showAddedMoney(float x, float y, int val) {
        final TextView view = getRunningMoneyView();
        view.setX(x-70);
        view.setY(y-50);
        view.setText("$ " + val);
        touchLocation.addView(view);

        CountDownTimer timer = new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                view.setX(view.getX() - 8);
                view.setY(view.getY() - 12);
                view.setAlpha(view.getAlpha() - 0.05f);
            }

            @Override
            public void onFinish() {
                view.setVisibility(View.INVISIBLE);
                touchLocation.removeView(view);
            }
        };
        timer.start();
    }

    @Override
    public void onPause() {
        presenter.onGamePause();
        runningGameClickableObj.destroy();
        gameClickableObj.destroy();
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
