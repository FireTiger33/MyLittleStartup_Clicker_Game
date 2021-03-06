package com.example.mylittlestartup.data.executors;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    private final ScheduledExecutorService mScheduledExecutorService;

    private static AppExecutors sAppExecutors = new AppExecutors();

    public static AppExecutors getInstance() {
        return sAppExecutors;
    }

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread, ScheduledExecutorService scheduledExecutorService) {
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
        mScheduledExecutorService = scheduledExecutorService;
    }

    private AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor(), Executors.newScheduledThreadPool(1));
    }

    public Executor diskIO() {
        return mDiskIO;
    }

    public Executor networkIO() {
        return mNetworkIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    public ScheduledExecutorService scheduled(){
        return mScheduledExecutorService;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
