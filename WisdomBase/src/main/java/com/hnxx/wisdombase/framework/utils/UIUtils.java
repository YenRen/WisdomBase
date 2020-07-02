package com.hnxx.wisdombase.framework.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.hnxx.wisdombase.config.application.BaseApp;
import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.ui.widget.CustomToast;

/**
 * Created by zhoujun on 2018/11/27.
 * Describe:UI方面常用的方法工具类
 */
public class UIUtils {
    private UIUtils() {
    }

    /**
     * 直接获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApp.getApplication();
    }

    /**
     * 获取前台显示的Activity
     *
     * @return
     */
    public static Activity getForegroundActivity() {
        return WisdomApplication.Instance().getZlActivityLifecycleCallbacks().getForegroundActivity();
    }


    /*---------  Handler和主线程相关----------*/
    public static Thread getMainThread() {
        return BaseApp.getMainThread();
    }

    public static long getMainThreadId() {
        return (long) BaseApp.getMainThreadId();
    }

    public static Handler getHandler() {
        return BaseApp.getMainThreadHandler();
    }

    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getHandler().postDelayed(runnable, delayMillis);
    }

    public static boolean post(Runnable runnable) {
        return getHandler().post(runnable);
    }

    public static void removeCallbacks(Runnable runnable) {
        getHandler().removeCallbacks(runnable);
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    public static boolean isRunInMainThread() {
        return (long) Process.myTid() == getMainThreadId();
    }
    /*---------  Handler和主线程相关   end----------*/


    /*---------  资源相关----------*/
    public static View inflate(int resId) {
        Activity activity = getForegroundActivity();
        return activity != null ? inflate(activity, resId) : inflate(getContext(), resId);
    }

    public static View inflate(Context context, int resId) {
        return LayoutInflater.from(context).inflate(resId, null);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static int getIdentifier(String name, String defType) {
        return getResources().getIdentifier(name, defType, getContext().getPackageName());
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static int getDimens(int resId) {
        return getResources().getDimensionPixelSize(resId);
    }

    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static ColorStateList getColorStateList(int resId) {
        return getResources().getColorStateList(resId);
    }
    /*---------  资源相关   end----------*/


    /*---------  意图相关----------*/
    public static void startActivity(Intent intent) {
        Activity activity = getForegroundActivity();
        if (activity != null) {
            activity.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }
    }

    public static Intent newIntent(Class<? extends Activity> clz) {
        return new Intent(getContext(), clz);
    }
    /*---------  意图相关  end----------*/


    /*---------  系统吐司相关----------*/
    public static void toast(int resId) {
        toast(getString(resId));
    }

    public static void toast(final CharSequence str) {
        if (isRunInMainThread()) {
            showToast(str);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
        }
    }

    private static void showToast(CharSequence str) {
        //只有App处于前台才吐司
        if (WisdomApplication.Instance().getZlActivityLifecycleCallbacks().isApplicationInForeground()&& !TextUtils.isEmpty(str)) {
            CustomToast.showToast(getContext(), str, Toast.LENGTH_SHORT);
        }
    }
    /*---------  系统吐司相关  end----------*/





    /*---------  分辨率相关----------*/

    public static int dip2px(float dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5F);
    }

    public static int px2dip(float px) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5F);
    }
    /*---------  分辨率相关  end----------*/
}
