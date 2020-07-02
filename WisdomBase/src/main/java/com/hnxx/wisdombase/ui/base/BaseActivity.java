/**
 * @author zhoujun
 * @date Nov 4
 */
package com.hnxx.wisdombase.ui.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.framework.interfaces.LifeDisposable;
import com.hnxx.wisdombase.listener.ThrottleGestureListener;
import com.hnxx.wisdombase.ui.setting.SettingsManager;
import com.hnxx.wisdombase.ui.utils.ScreenUtil;
import com.hnxx.wisdombase.ui.widget.CustomToast;
import com.hnxx.wisdombase.ui.widget.dialog.CustomProgressDialog;
import com.hnxx.wisdombase.ui.widget.swipeback.SwipeBackLayout;

import java.util.List;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 通用Activity基类
 *
 * @author zhoujun
 */
public abstract class BaseActivity extends AppCompatActivity
        implements XxWisdomActivityHelper.EnableSwipeback, LifeDisposable {
    protected Context mCtx;
    protected WisdomApplication mApp;
    private BaseActivityHelper mHelper;
    /**
     * 状态栏背景
     */
    private int mStatusBarBackgroundResource = R.color.t_titlebar_bg;
    /**
     * 单Fragment布局ID
     */
    protected int SINGLE_FRAGMENT_LAYOUT_ID = R.layout.single_fragment_layout;
    protected CustomProgressDialog mCustomProgressDialog;
    public ImmersionBar immersionBar;
    /**
     * 防止响应重复点击事件，默认不开启
     */
    private ThrottleGestureListener mGestureListener;
    private GestureDetector mGestureDetector;
    /**
     * 默认开启防抖动，阅读器关闭
     */
    public boolean throttleFirst = true;

    /**
     * rx被通知者结合，挂载生命周期上，避免内存泄漏
     */
    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCtx = this;
        mApp = (WisdomApplication) this.getApplicationContext();
        mApp.addActivity(this);
        mGestureListener = new ThrottleGestureListener();
        mGestureDetector = new GestureDetector(mCtx, mGestureListener);
        initWindow();

        if (mHelper == null && this.canSwipeback()) {
            try {
                if (null != SettingsManager.activity_helper_option.getValue()) {
                    mHelper = (BaseActivityHelper) Class.forName(
                            SettingsManager.activity_helper_option.getValue())
                            .newInstance();
                    mHelper.bindActivity(this);
                }
            } catch (Exception e) {
                Log.e("zhoujun", "onCreate: =====");
            }
        }

        if (mHelper != null) {
            mHelper.onCreate(savedInstanceState);
        }
        super.onCreate(savedInstanceState);

        if (getLayoutId() == 0) {
            setContentView(SINGLE_FRAGMENT_LAYOUT_ID);
        } else {
            setContentView(getLayoutId());
        }
        findViewById();
        bindButterKnife();
        setListener();
        init(savedInstanceState);
        initTranslucentStatusBar(getToolBarId());
    }

    protected void bindButterKnife(){
        ButterKnife.bind(this);
    }

    /**
     * 添加Rx Disposable
     *
     * @param disposable
     */
    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }


    /**
     * 设置toolbar的id 用于沉浸式状态栏
     *
     * @return
     */
    protected int getToolBarId() {
        return 0;
    }

    protected void setActivityContent(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.activity_root_view, fragment);
        ft.commitAllowingStateLoss();
    }

    /**
     * 页面为Fragment时设置页面背景
     */
    public void setFragmentActivityContentBgColor(int resId){
        LinearLayout llContent = findViewById(R.id.activity_root_view);
        if (llContent != null){
            llContent.setBackgroundResource(resId);
        }
    }


    /**
     * 修改状态栏颜色
     *
     * @param colorId
     */
    public void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && colorId != 0) {
            immersionBar = ImmersionBar.with(this);
            immersionBar.statusBarColor(colorId);
            immersionBar.statusBarDarkFont(true, 0.1f);
            immersionBar.keyboardEnable(true);
            immersionBar.init();
        }
    }

    /**
     * 修改状态栏颜色
     *
     * @param colorId
     * @param isDarkTheme 状态字体是否暗色
     */
    public void setStatusBarColorWithFitsSystemWindows(int colorId, boolean isDarkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            content.setFitsSystemWindows(true);
            immersionBar = ImmersionBar.with(this);
            immersionBar.statusBarColor(colorId);
            immersionBar.statusBarDarkFont(isDarkTheme, 0.1f);
            immersionBar.keyboardEnable(true);
            immersionBar.init();
        }
    }

    /**
     * 修改状态栏颜色
     *
     * @param isDarkTheme 是否暗色
     */
    public void setStatusBarTextColor(boolean isDarkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            immersionBar = ImmersionBar.with(this);
            //状态栏字体白色
            immersionBar.statusBarDarkFont(isDarkTheme, 0.1f);
            immersionBar.init();
        }
    }

    /**
     * 修改状态栏颜色
     *
     * @param colorId
     */
    public void setStatusBarColorWithViewId(int viewId, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && colorId != 0) {
            immersionBar = ImmersionBar.with(this);
            immersionBar.statusBarView(viewId);
            immersionBar.statusBarColor(colorId);
            immersionBar.statusBarDarkFont(true, 0.1f);
            immersionBar.init();
        }
    }

    /**
     * 沉浸式标题栏
     *
     * @param toolBarId
     */
    protected void initTranslucentStatusBar(int toolBarId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && toolBarId != 0) {
            immersionBar = ImmersionBar.with(this);
            immersionBar.titleBar(toolBarId);
            immersionBar.statusBarDarkFont(true, 0.1f);
            //immersionBar.statusBarColor(R.color.white);
            immersionBar.keyboardEnable(true);  //解决软键盘与底部输入框冲突问题
            immersionBar.init();
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //解决api21以上，23以下的ui问题
            View content = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            content.setFitsSystemWindows(true);
            immersionBar = ImmersionBar.with(this);
            immersionBar.statusBarColor(R.color.black);
            immersionBar.keyboardEnable(true);
            immersionBar.init();
        }
    }

    /**
     * 设置全屏
     */
    protected void setFullScreen() {
        //设置全屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            immersionBar = ImmersionBar.with(this).reset();

            if (ScreenUtil.isAllScreenDevice()) {
                //为什么这里只隐藏状态栏，因为8.0全面屏不提高targetsdkversion隐藏不掉导航栏
                immersionBar.hideBar(BarHide.FLAG_HIDE_STATUS_BAR);
            } else {
                immersionBar.hideBar(BarHide.FLAG_HIDE_BAR);
            }
            immersionBar.init();
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //设置全屏的flag
        }
    }

    protected void setTranslucentStatusBar(boolean isShow) {
        if (isShow) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    protected void setTranslucentNavBar(boolean isShow) {
        if (isShow) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 设置背景
     *
     * @param resId
     */
    protected void setBackgroundResource(int resId) {
        View v = this.findViewById(R.id.activity_root_view);
        if (v != null) {
            v.setBackgroundResource(resId);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        mApp.removeActivity(this);
        super.onDestroy();
        if (mHelper != null) {
            mHelper.onDestroy();
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }


    /**
     * 初始化窗口属性
     */
    protected void initWindow() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 获取页面的布局资源id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 通过ID绑定控件
     */
    protected void findViewById(){
    }

    /**
     * 设置控件的监听事件
     */
    protected abstract void setListener();

    /**
     * 界面数据初始化
     */
    protected abstract void init(Bundle savedInstanceState);


    /**
     * 默认退出
     */
    public void defaultFinish() {
        super.finish();

        if (mHelper != null) {
            mHelper.finish();
        }
    }

    public BaseActivityHelper getActivityHelper() {
        return mHelper;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mHelper != null) {
            mHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean onBackClick() {
        if (mHelper != null) {
            boolean handle = mHelper.onBackClick();
            if (handle) {
                return true;
            }
        }
        this.defaultFinish();
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (throttleFirst && null != mGestureListener && null != mGestureDetector) {
            if (mGestureDetector.onTouchEvent(ev)) {
                return true;
            } else {
                return super.dispatchTouchEvent(ev);

            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyboard(this);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity != null){
            View view = activity.getCurrentFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (view != null && inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void showSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager manager = ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (null != manager && null != view) {
            manager.showSoftInput(view, 0);
        }
    }

    public boolean isSoftShowing(Activity activity) {
        //获取当屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom + getSoftButtonsBarHeight(activity);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mHelper != null) {
            boolean handle = mHelper.onKeyDown(keyCode, event);
            if (handle) {
                return true;
            }
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (onBackClick()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHelper != null) {
            mHelper.onPause();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (mHelper != null) {
            mHelper.onStop();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mHelper != null) {
            mHelper.onPostCreate(savedInstanceState);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mHelper != null) {
            mHelper.onStart();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (mHelper != null) {
            mHelper.onRestart();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mHelper != null) {
            mHelper.onResume();
        }
    }

    /**
     * 顶部状态栏是否透明
     *
     * @return
     */
    protected boolean isTranslucentStatusBar() {
        return true;
    }

    /**
     * 底部导航栏（虚拟按键栏）是否透明
     *
     * @return
     */
    protected boolean isTranslucentNavBar() {
        return false;
    }

    /**
     * 是否在顶部增加状态栏高度的标题色
     *
     * @return
     */
    protected boolean isAddStatusBarPadding() {
        return true;
    }

    /**
     * 设置状态栏背景(建议在init阶段调用)
     *
     * @param mStatusBarBackgroundResource 资源ID(R.color.xx or R.drawable.xx)
     */
    public void setStatusBarBackgroundResource(int mStatusBarBackgroundResource) {
        this.mStatusBarBackgroundResource = mStatusBarBackgroundResource;
    }

    @Override
    public int getSwipebackFlag() {
        return SwipeBackLayout.EDGE_BOTTOM;
//        return SwipeBackLayout.EDGE_LEFT;
    }

    protected void startActivity(Class<?> clz) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        startActivity(intent);
    }


    protected void showProgressDialog(final String msg) {
        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CustomProgressDialog(this);
        }
        if (!mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.setMessage(msg);
            mCustomProgressDialog.show();
        }
    }

    protected void showProgressDialog(final String msg, final String cancelHint) {
        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CustomProgressDialog(this);
        }
        if (!mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.setMessage(msg);
            mCustomProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    CustomToast.showToast(mCtx, cancelHint, Toast.LENGTH_SHORT);
                }
            });
            mCustomProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if (mCustomProgressDialog != null && mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.dismiss();
        }
    }

    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程,查看该应用是否在运行
        if (appProcessInfos == null || appProcessInfos.isEmpty()) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    return true;
                }
            }
        }
        return false;
    }


}
