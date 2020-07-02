package com.hnxx.wisdombase.config.application;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;

/**
 * Created by zhoujun on 2019/12/17.
 * Describe:Application基类，不包含任何业务相关代码
 */
public class BaseApp extends Application {
    private static BaseApp mContext;
    private static Handler mMainThreadHandler;
    private static Looper mMainThreadLooper;
    private static Thread mMainThread;
    private static int mMainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mMainThreadHandler = new Handler();
        mMainThreadLooper = this.getMainLooper();
        mMainThread = Thread.currentThread();
        mMainThreadId = Process.myTid();
    }

    public static BaseApp getApplication() {
        return mContext;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }
}

