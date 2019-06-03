package com.example.mylittlestartup.game.objects;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.example.mylittlestartup.R;
import com.example.mylittlestartup.data.BaseCallback;
import com.example.mylittlestartup.data.sqlite.Upgrade;
import com.example.mylittlestartup.game.GameContract;

import java.io.IOException;
import java.io.InputStream;


public class GameObjWorker extends Upgrade {
    private final String tag = GameObjWorker.class.getName();

    private final ImageButton pushButton;
    private boolean readyToPush = false;
    private Upgrade mUpgrade;
    private final GameContract.Presenter mPresenter;
    private ImageButton mLogo;
    private final View mItemView;
    private CountDownTimer workTimer;
    private int mSpeed = 1;
    private final int[] pushAnimateEndCoordinate;

    private AlertDialog.Builder upgradeDialog;
    private View mUpgradeDialogView;
    private TextView nextLVLTextView;
    private TextView nextValueTextView;
    private TextView nextIntervalTextView;
    private TextView priceView;
    private boolean needRecreateUpgradeDialog = true;


    public GameObjWorker(View itemView, View upgradeDialogView, int[] endCoordinateForPushAnimation, Upgrade upgrade, final GameContract.Presenter presenter) {
        Log.d(tag, "Create worker");
        mPresenter = presenter;
        mItemView = itemView;
        mUpgradeDialogView = upgradeDialogView;
        mUpgrade = upgrade;
        pushAnimateEndCoordinate = endCoordinateForPushAnimation;
        mLogo = itemView.findViewById(R.id.worker_preview);
        pushButton = itemView.findViewById(R.id.push_button);
        pushButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "pushButtonClicked");
                if (readyToPush) {
                    readyToPush = false;
                    showPushAnimation();
                    workTimer.start();
                }
                //show();

            }
        });
        mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(tag, "onClickItemView");
                if (readyToPush) {
                    readyToPush = false;
                    showPushAnimation();
                    workTimer.start();
                } else {
                    showUpgradeDialog();
                }
            }
        });

        mPresenter.fetchSpeeders(new GameContract.Repository.IntCallback() {
            @Override
            public void onSuccess(int lvl) {
                Log.d(tag, "newSpeed = " + lvl);
                if (lvl != 0) {
                    mSpeed = lvl;
                    onUpgraded(mUpgrade);
                }
            }

            @Override
            public void onError() { }
        });

        onUpgraded(mUpgrade);
    }

    private void recreateDialogViews() {
        if (nextLVLTextView == null) {
            nextLVLTextView = mUpgradeDialogView.findViewById(R.id.next_lvl);
            nextValueTextView = mUpgradeDialogView.findViewById(R.id.next_value);
            nextIntervalTextView = mUpgradeDialogView.findViewById(R.id.next_interval);
            priceView = mUpgradeDialogView.findViewById(R.id.price_worker);
        }
        nextLVLTextView.setText(new StringBuilder("lvl: " + mUpgrade.getCount() + " -> " + (mUpgrade.getCount() + 1)));
        nextValueTextView.setText(new StringBuilder("value: " + mUpgrade.getValue() + " -> " + (mUpgrade.getValue() * 2 + 100)));
        nextIntervalTextView.setText(new StringBuilder("pushInterval: " + mUpgrade.getInterval()/1000 + "s -> " + (mUpgrade.getInterval()/1000 + 5 + "s")));
        priceView.setText(new StringBuilder(mUpgrade.getPrice() + " $"));
        mPresenter.checkEnoughMoney(mUpgrade.getPrice(), new BaseCallback() {
            @Override
            public void onSuccess() {
                priceView.setTextColor(Color.GREEN);
            }

            @Override
            public void onError() {
                priceView.setTextColor(Color.RED);
            }
        });
    }

    private void createUpgradeDialog() {
        upgradeDialog = new AlertDialog.Builder(mItemView.getContext());
        if (mUpgrade.getCount() == 0) {
            upgradeDialog.setPositiveButton(R.string.hire, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(tag, "clickUpgradeWorker");
                    mPresenter.onUpgradeWorker(mUpgrade);
                }
            });
        } else {
            upgradeDialog.setPositiveButton(R.string.promote, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(tag, "clickUpgradeWorker");
                    mPresenter.onUpgradeWorker(mUpgrade);
                }
            });
            upgradeDialog.setNeutralButton(R.string.layoff, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(tag, "clickLayOffWorker");
                    mPresenter.onLayOffWorker(mUpgrade);
                }
            });
        }
        upgradeDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        upgradeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Log.d(tag, "upgradeDialogDismiss");
                /*if (mUpgradeDialogView.getParent() != null) {
                    ((ViewGroup) mUpgradeDialogView.getParent()).removeView(mUpgradeDialogView);
                }*/
                mPresenter.onCloseWorkerUpgradeDialog();
            }
        });
        needRecreateUpgradeDialog = false;
    }

    private void showUpgradeDialog() {
        Log.d(tag, "createUpgradeDialog");
        if (needRecreateUpgradeDialog) {
            Log.d(tag, "createDialog == null");
            createUpgradeDialog();
        }
        recreateDialogViews();
        if (mUpgradeDialogView.getParent() != null) {
            ((ViewGroup) mUpgradeDialogView.getParent()).removeView(mUpgradeDialogView);
        }
        upgradeDialog.setView(mUpgradeDialogView);
        upgradeDialog.create();
        upgradeDialog.show();
        mPresenter.onShowWorkerUpgradeDialog();
    }

    public void onUpgraded(Upgrade upgrade) {
        mUpgrade = upgrade;
        Log.d(tag, "onUpgraded");
        if (mUpgrade.getCount() == 1) {
            needRecreateUpgradeDialog = true;
        }
        workTimer = new CountDownTimer(mUpgrade.getInterval() / mSpeed, mUpgrade.getInterval()) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(tag, "worker: " + mUpgrade.getId() + "pushUntilFinished = " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                if (mUpgrade.getCount() > 0) {
                    readyToPush = true;
                    pushButton.setVisibility(View.VISIBLE);
                }
            }
        };
        show();
    }

    private void setLogo() {
        try {
            Log.d(tag, "setLogo");
            AssetManager assetManager = mItemView.getContext().getAssets();
            Log.d(tag, "setLogo: getAssets");
            InputStream inputStream = assetManager.open(/*"img/programmer_lvl1.svg"*/mUpgrade.getPicPath());
            Log.d(tag, "setLogo: openFile");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d(tag, "setLogo: decodedStream");
            if (bitmap == null) {
                Log.d(tag, "setLogo: bitmapDecodeInputStream == null");
                inputStream = assetManager.open(/*"img/programmer_lvl1.svg"*/mUpgrade.getPicPath());SVG svg = null;
                try {
                    svg = SVG.getFromInputStream(inputStream);
                    Log.d(tag, "setLogo: SVG decoded");
                } catch (SVGParseException e) {
                    e.printStackTrace();
                }
                if (svg != null) {
                    Picture picture = svg.renderToPicture();
                    PictureDrawable pictureDrawable = new PictureDrawable(picture);

                    mLogo.setImageDrawable(pictureDrawable);
                } else {
                    Log.d(tag, "setLogo: SVG == null");
                }
            } else {
                mLogo.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            Log.d(tag, "setLogo: impossible");
            e.printStackTrace();
        }
    }

    public void show() {
        Log.d(tag, "show: " + mUpgrade.getCount());
        setLogo();
        workTimer.start();
    }

    public int getWorkerId() {
        return mUpgrade.getId();
    }

    private void showPushAnimation() {

        int[] viewEndPos = new int[2];
        pushButton.getLocationOnScreen(viewEndPos);
        viewEndPos[0] -= pushAnimateEndCoordinate[0];
        viewEndPos[1] -= pushAnimateEndCoordinate[1];
        TranslateAnimation pushAnimation = new TranslateAnimation(0, -viewEndPos[0], 0, -viewEndPos[1]);
        pushAnimation.setDuration(1500);
        pushAnimation.setInterpolator(new AccelerateInterpolator());
        pushAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pushButton.setVisibility(View.INVISIBLE);
                mPresenter.onWorkerPushed(mUpgrade);
            }

        });
        pushButton.startAnimation(pushAnimation);
    }

}
