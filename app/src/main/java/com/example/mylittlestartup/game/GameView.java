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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mylittlestartup.R;
import com.example.mylittlestartup.Router;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.objects.GameClickableObj;
import com.example.mylittlestartup.game.objects.GameObjWorker;
import com.example.mylittlestartup.game.objects.RunningGameClickableObj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
//import com.example.mylittlestartup.game.objects.GameFollowingObj;


public class GameView extends Fragment implements GameContract.View {
    String tag = GameView.class.getName();
    final private Random random = new Random();
    private View mView;
    private GamePresenter presenter;
    private Button shopButton;
    final private int[] workerViewIds = {
            R.id.worker_1,
            R.id.worker_2,
            R.id.worker_3,
            R.id.worker_4,
            R.id.worker_5
    };
    private List<GameObjWorker> workers;
    private FrameLayout touchLocation;  // container for Objects
    private TextView moneyValView;
    private RelativeLayout workersContainer;
    private View workerUpgradeDialogView;
    private ValueAnimator moneyViewAnimator;
    private WebView monitorView;
    private CountDownTimer timerWorkUpgrades;

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
        workers = new ArrayList<>();
        timerWorkUpgrades = new CountDownTimer(1000, 200) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                presenter.onCommonClickLocationClickPaused();
            }
        }.start();
    }

    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(tag, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_game, container, false);
        presenter.fetchWorkers();

        shopButton = mView.findViewById(R.id.button_shop);
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onShopButtonClicked();
            }
        });

        moneyValView = mView.findViewById(R.id.money_val);
        moneyValView.setText("0");

        workersContainer = mView.findViewById(R.id.workers_container);
        workerUpgradeDialogView = inflater.inflate(R.layout.window_worker_upgrade_dialog, null);

//        workersContainer.addView(inflater.inflate(R.layout.game_element_worker, null, false));

        touchLocation = mView.findViewById(R.id.touch_location);

        monitorView = mView.findViewById(R.id.monitor);
        monitorView.loadUrl("file:///android_asset/monitor_view.html");
        setMonitorWebViewSettings();
        monitorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timerWorkUpgrades.start();
                    presenter.onCommonClickLocationClicked(event.getX(), event.getY());
                }

                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });

        return mView;
    }

    private void setMonitorWebViewSettings() {
        monitorView.getSettings().setJavaScriptEnabled(true);
        monitorView.getSettings().setAllowFileAccess(true);
        monitorView.getSettings().setAllowContentAccess(true);
        monitorView.getSettings().setAllowFileAccessFromFileURLs(true);
        monitorView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        monitorView.getSettings().setUseWideViewPort(true);
        monitorView.setVerticalScrollBarEnabled(false);
        monitorView.setHorizontalScrollBarEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(tag, "onViewCreated");
        presenter.onGameStart();
        createIssueButton();
        createBugButton();
        /*presenter.registerPlayerBug(new BaseCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });*/
    }

    @Override
    public void showMoneyPulseAnim() {
        final int defTextSize = (int)(moneyValView.getTextSize()/3);
        moneyViewAnimator = ValueAnimator.ofInt(defTextSize, defTextSize+10).setDuration(150L);
        moneyViewAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moneyValView.setTextSize((int)animation.getAnimatedValue());
            }
        });
        moneyViewAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moneyValView.setTextSize(30);
            }
        });
        moneyViewAnimator.start();
    }

    @Override
    public void createWorkers(List<Upgrade> upgrades) {
        Log.d(tag, "createWorkers: " + upgrades.size());
        int i = 0;
        int[] endAnimCoord = new int[2];
        moneyValView.getLocationInWindow(endAnimCoord);
        endAnimCoord[0] += moneyValView.getWidth()/3;

        for (Upgrade worker: upgrades) {
            Log.d(tag, "Create worker");
            this.workers.add(new GameObjWorker(workersContainer.findViewById(workerViewIds[i++]),
                    workerUpgradeDialogView, endAnimCoord, worker, presenter));
        }
    }

    @Override
    public void showWorkers() {
        for (GameObjWorker worker : workers) {
            worker.show();
        }
    }

    @Override
    public void showUpgradeWorker(Upgrade upgradedWorker) {
        for (GameObjWorker worker: workers) {
            if (worker.getWorkerId() == upgradedWorker.getId()) {
                worker.onUpgraded(upgradedWorker);
            }
        }
    }

    @Override
    public void pauseGameObjects() {
        runningGameClickableObj.pause();
        gameClickableObj.pause();
    }

    @Override
    public void resumeGameObjects() {
        runningGameClickableObj.resume();
        gameClickableObj.resume();
    }

    private void createIssueButton() {
        gameClickableObj = new GameClickableObj(touchLocation, presenter,
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
        runningGameClickableObj = new RunningGameClickableObj(touchLocation, presenter,
                mView.findViewById(R.id.bug), 30, 5, 10, colorSet);
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
        int k_x = random.nextBoolean()? 1: -1;
        int k_y = random.nextBoolean()? 1: -1;
        view.setX(x+k_x*70);
        view.setY(y+k_y*50);
        view.setText("$ " + val);
        touchLocation.addView(view);
//        moneyValView.setTextSize(30);

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
        Log.d(tag, "onPause");
        presenter.onGamePause();
        pauseGameObjects();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(tag, "onResume");
        super.onResume();
        resumeGameObjects();
        presenter.onGameStart();
    }

    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy");
        presenter.onGamePause();
        runningGameClickableObj.destroy();
        gameClickableObj.destroy();
        super.onDestroy();
    }
}
