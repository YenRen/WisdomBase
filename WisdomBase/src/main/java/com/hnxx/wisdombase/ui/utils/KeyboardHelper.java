package com.hnxx.wisdombase.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.framework.utils.UIUtils;


/**
 * Created by YER on 2019/10/15.
 * Describe:
 */
public class KeyboardHelper {

    public KeyboardHelper(Activity context) {
        this.mContext = context;
        /**
         * 获取屏幕的高度,该方式的获取不包含虚拟键盘
         */
        screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        screenHeight6 = screenHeight / 6;
        rootView = mContext.getWindow().getDecorView();
    }

    /**
     * 隐藏软键盘
     * @param editText
     */
    public void hideKeyBoard(EditText editText){
        InputMethodManager imm=
                (InputMethodManager)
                        WisdomApplication.getApplication()
                                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    /**
     * 弹起软键盘
     * @param editText
     */
    public void openKeyBoard(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        UIUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm=
                        (InputMethodManager)
                                WisdomApplication.getApplication()
                                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,0);
                editText.setSelection(editText.getText().length());
            }
        },200);
   /*     Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            public void run() {

            }
        },200);*/
    }


    Activity mContext;
    /**
     * 虚拟键盘高度
     */
    int virtualKeyboardHeight;
    /**
     * 屏幕高度
     */
    int screenHeight;
    /**
     * 屏幕6分之一的高度，作用是防止获取到虚拟键盘的高度
     */
    int screenHeight6;
    View rootView;


    /**
     * @param listener
     */
    public void setOnKeyboardChangeListener(final KeyboardChangeListener listener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            //当键盘弹出隐藏的时候会 调用此方法。
            @Override
            public void onGlobalLayout() {
                /**
                 * 回调该方法时rootView还未绘制，需要设置绘制完成监听
                 */
                rootView.post(new Runnable() {
                    @Override
                    public void run() {
                        Rect rect = new Rect();
                        /**
                         * 获取屏幕底部坐标
                         */
                        rootView.getWindowVisibleDisplayFrame(rect);
                        /**
                         * 获取键盘的高度
                         */
                        int heightDifference = screenHeight - rect.bottom;
                        if (heightDifference < screenHeight6) {
                            virtualKeyboardHeight = heightDifference;
                            if (listener != null) {
                                listener.onKeyboardHide();
                            }
                        } else {
                            if (listener != null) {
                                listener.onKeyboardShow(heightDifference - virtualKeyboardHeight);
                            }
                        }
                    }
                });
            }
        });
    }
    /**
     * 软键盘状态切换监听
     */
    public interface KeyboardChangeListener {
        /**
         * 键盘弹出
         *
         * @param keyboardHight 键盘高度
         */
        void onKeyboardShow(int keyboardHight);
        /**
         * 键盘隐藏
         */
        void onKeyboardHide();
    }


    /**
     * 动态隐藏软键盘
     *
     * @param activity activity
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
