package com.hnxx.wisdombase.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hnxx.wisdombase.R;

/***
 * 通用按钮控件
 * @author zhoujun
 * @date 2019年8月21日17:06:38
 */
public class CustomButtonView extends LinearLayout {

    /***
     * 默认效果,点击时变换背景
     */
    public static final int STYLE_NORMAL = 0;
    /****
     * 保持按下效果
     */
    public static final int STYLE_SELECTED = 1;
    /***
     * 保持置灰效果
     */
    public static final int STYLE_ENABLED = 2;

    private int backgroundResourceId = R.drawable.selector_new_button_bg;
    private int textcolorResourceId = R.color.selector_new_button_color;
    private float textsize = 16;
    private String text = "";
    private int style = STYLE_NORMAL;
    private TextView textView;

    public CustomButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomButtonView);
            backgroundResourceId = a.getResourceId(R.styleable.CustomButtonView_mBackground, R.drawable.selector_new_button_bg);
            textcolorResourceId = a.getResourceId(R.styleable.CustomButtonView_mTextColor, R.color.selector_new_button_color);
            text = a.getString(R.styleable.CustomButtonView_mText);
            textsize = a.getDimension(R.styleable.CustomButtonView_mTextSize, 16);
            style = a.getInt(R.styleable.CustomButtonView_mStyle, 0);
        }
        setBackgroundResource(backgroundResourceId);
        setGravity(Gravity.CENTER);
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        addView(textView, new LayoutParams(LayoutParams.WRAP_CONTENT, (int) context.getResources().getDimension(R.dimen.dimen_45dp)));
        textView.setTextColor(getResources().getColorStateList(textcolorResourceId));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textsize);
        textView.setText(text);
        //样式
        styleChange(style);
    }

    /***
     * 设置按钮样式
     * @param style STYLE_NORMAL 默认 ，STYLE_SELECTED按下，STYLE_ENABLED置灰
     */
    public void setStyle(int style) {
        styleChange(style);
    }

    /***
     * 改变样式
     * @param style
     */
    private void styleChange(int style) {
        switch (style) {
            case STYLE_NORMAL:
                setSelected(false);
                setEnabled(true);
                textView.setSelected(false);
                textView.setEnabled(true);
                break;
            case STYLE_SELECTED:
                setSelected(true);
                setEnabled(true);
                textView.setSelected(true);
                textView.setEnabled(true);
                break;
            case STYLE_ENABLED:
                setSelected(false);
                setEnabled(false);
                textView.setSelected(false);
                textView.setEnabled(false);
                break;
        }
    }

    public void setText(String text) {
        if (textView != null) {
            textView.setText(text);
        }
    }
}
