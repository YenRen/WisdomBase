package com.hnxx.wisdombase.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by zhoujun on 2018/6/4.
 * Describe:Dialog基类
 */
public class BaseDialog extends Dialog {
    protected Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        //关闭Dialog则继续使用挂靠Activity的菜单信息
        super.onStop();
    }

    protected Activity getAttachActivity() {
        Context context = getContext();
        if (context instanceof ContextThemeWrapper) {
            context = ((ContextThemeWrapper) context).getBaseContext();
        }
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }


    @Override
    public void show() {
        //Activity关闭了，就不要show了
        if (getAttachActivity() == null || getAttachActivity().isFinishing()) {
            return;
        }
        super.show();
    }

    @Override
    public void dismiss() {
        //Activity关闭了，就不要dismiss了
        if (getAttachActivity() == null || getAttachActivity().isFinishing()) {
            return;
        }
        super.dismiss();
    }
}
