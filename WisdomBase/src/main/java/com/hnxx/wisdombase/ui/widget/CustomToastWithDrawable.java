/**
 * 版权所有：中兴通讯股份有限公司
 * 文件名称：CustomToast.java
 * 文件作者：Xiezhuoxun
 * 开发时间：2013-4-15
 * <p>
 * 修改者：maj
 * 修改时间：2015-05-19
 * 修改内容：显示内容改为自定义视图
 */
package com.hnxx.wisdombase.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.hnxx.wisdombase.framework.utils.UIUtils;


/**
 * @Author YER
 * 自定义带图片的Toast
 */
public class CustomToastWithDrawable {

    /** The m toast. */
    private static Toast mToast;
    private static Object mObj = new Object();
    private static LayoutInflater mInflate;

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    private static void showToast(final Context context, final CharSequence text, final int duration,
                                  final int gravity, final Drawable drawable, final int drawableGravity) {
        if (context == null || TextUtils.isEmpty(text)) {
            return;
        }
        UIUtils.runInMainThread(new Runnable() {

            @Override
            public void run() {
                synchronized (mObj) {
                    if (mToast != null) {
                        final View view = mToast.getView();
                        TextView tvText = view
                                .findViewById(R.id.customtoast_tv_text);
                        tvText.setText(TextUtils.isEmpty(text) ? "" : text);
                        setTextDrawble(context, tvText, drawable, drawableGravity);
                        mToast.setDuration(duration);
                        int yOffSet = 0;
                        if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
                            yOffSet = 200;
                        }
                        mToast.setGravity(gravity, 0, yOffSet);
                    } else {
                        Context pContext = context;
                        if (context instanceof Activity) {
                            pContext = WisdomApplication.Instance()
                                    .getApplicationContext();
                        }
                        mToast = Toast.makeText(pContext, TextUtils.isEmpty(text) ? "" : text, duration);

                        int yOffSet = 0;
                        if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP) {
                            yOffSet = 200;
                        }
                        mToast.setGravity(gravity, 0, yOffSet);
                        View contentView = getContentView(context, TextUtils.isEmpty(text) ? "" : text);
                        TextView tvText = contentView.findViewById(R.id.customtoast_tv_text);
                        setTextDrawble(pContext, tvText, drawable, drawableGravity);
                        mToast.setView(contentView);
                    }
                    try {
                        //show()里面报NullPointerException，这个地方try一下
                        //Attempt to invoke interface method void android.app.INotificationManager.enqueueToast on a null object reference
                        mToast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 设置文字图片
     *
     * @param context
     * @param tvText
     * @param drawable
     * @param drawableGravity
     */
    private static void setTextDrawble(Context context, TextView tvText, Drawable drawable, int drawableGravity) {
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            int padding15 = PhoneInfoTools.dip2px(context, 15);
            int padding20 = PhoneInfoTools.dip2px(context, 20);
            int padding40 = PhoneInfoTools.dip2px(context, 40);
            tvText.setCompoundDrawablePadding(padding15);
            tvText.setPadding(padding40, padding20, padding40, padding20);
            if (drawableGravity == Gravity.TOP) {
                tvText.setCompoundDrawables(null, drawable, null, null);
            } else if (drawableGravity == Gravity.LEFT || drawableGravity == Gravity.START) {
                tvText.setCompoundDrawables(drawable, null, null, null);
            } else if (drawableGravity == Gravity.RIGHT || drawableGravity == Gravity.END) {
                tvText.setCompoundDrawables(null, null, drawable, null);
            } else if (drawableGravity == Gravity.BOTTOM) {
                tvText.setCompoundDrawables(null, null, null, drawable);
            }
        }
    }

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    public static void showToast(Context context, CharSequence text, int duration, Drawable drawable) {
        showToast(context, text, duration, Gravity.CENTER, drawable, Gravity.TOP);
    }

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    public static void showToast(Context context, CharSequence text, int duration, int drawableResId) {
        if (context != null && drawableResId != 0) {
            Drawable drawable = context.getResources().getDrawable(drawableResId);
            showToast(context, text, duration, Gravity.CENTER, drawable, Gravity.TOP);
        }
    }

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    public static void showToast(Context context, CharSequence text, int duration, Drawable drawable, int drawableGravity) {
        showToast(context, text, duration, Gravity.CENTER, drawable, drawableGravity);
    }

    /**
     * 显示一个Toast，如果由于不可控因素短时间多次调用了该方法，只会显示最后一次的消息内容
     *
     * @param context  the m context
     * @param text     the text
     * @param duration the duration
     * @version
     */
    public static void showToast(Context context, CharSequence text, int duration, int drawableResId, int drawableGravity) {
        if (context != null && drawableResId != 0) {
            Drawable drawable = context.getResources().getDrawable(drawableResId);
            showToast(context, text, duration, Gravity.CENTER, drawable, drawableGravity);
        }
    }


    /**
     *
     * Description: 获取自定义toast视图
     * <p>
     *
     * @date 2015年5月19日
     * @author maj
     * @param context
     * @param text
     * @return
     */
    private static View getContentView(Context context, CharSequence text) {
        if(mInflate == null) {
            mInflate = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        View contentView = mInflate.inflate(R.layout.customtoast_with_drawable, null);
        TextView tvText = contentView
                .findViewById(R.id.customtoast_tv_text);
        tvText.setText(TextUtils.isEmpty(text) ? "" : text);

        return contentView;
    }
    /**
     * 为什么有这个方法，坑爹的华为高版本在连续toast会出问题，所以我们在第二次toast时重置
     */
    public static void cancelToast(){
        if(mToast!=null){
            mToast.cancel();
            mToast=null;
        }
    }
}
