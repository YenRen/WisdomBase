package com.hnxx.wisdombase.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnxx.wisdombase.R;


/**
 * Created by YER on 2019/9/23
 */

public class LoadingWaitingDialog extends Dialog {


    private String text;
    private AnimationDrawable animationDrawable;

    public LoadingWaitingDialog(Context context) {
        super(context, R.style.confirm_dialog);
    }

    public LoadingWaitingDialog(Context context, String text) {
        super(context, R.style.confirm_dialog);
        this.text = text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_load_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        //设置点击返回键不消失
        setCancelable(false);
        if (text != null) {
            TextView textView = findViewById(R.id.loading_text);
            textView.setText(text);
        } else {
            findViewById(R.id.loading_text).setVisibility(View.GONE);
        }
        ImageView progressImageView = findViewById(R.id.loading_imv);
        animationDrawable = (AnimationDrawable) getContext().getResources().getDrawable(R.drawable.waiting_loading);
        progressImageView.setImageDrawable(animationDrawable);
    }

    public void setText(String text) {
        this.text=text;
        TextView textView = findViewById(R.id.loading_text);
        if (textView != null) {
            textView.setText(text);
        }
    }

    /**
     * 开始帧动画
     */
    @Override
    protected void onStart() {
        animationDrawable.start();
        super.onStart();
    }

    /**
     * 停止帧动画
     */
    @Override
    protected void onStop() {
        animationDrawable.stop();
        super.onStop();
    }




}
