package com.hnxx.wisdombase.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.config.application.IntentKey;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.hnxx.wisdombase.ui.utils.KeyboardHelper;
import com.hnxx.wisdombase.ui.widget.swiperfresh.SwipeRefreshView;

public abstract class TitleWithHeadBgActivity extends BaseActivity {

    protected LayoutInflater mInflater;
    public boolean hiddenTitleBarFlag;
    private String title;
    RelativeLayout titleView;
    public SwipeRefreshView swipeRefreshView;
    FrameLayout mContentView;
    protected View ivBack;
    private TextView mTvTitle;
    private View maskView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hiddenTitleBarFlag = this.getIntent().getBooleanExtra("hiddenTitleBar", false);
        title = this.getIntent().getStringExtra(IntentKey.INTENT_KEY_TITLE);
        mInflater = this.getLayoutInflater();
        super.onCreate(savedInstanceState);
        setStatusBarColor(R.color.transparent_00);
    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_title_with_head_bg;
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void findViewById() {
        titleView = findViewById(R.id.titleview);
        ivBack = findViewById(R.id.imv_back);
        mTvTitle = findViewById(R.id.tv_title);
        mTvTitle.setText(title);
        swipeRefreshView = findViewById(R.id.swiperefreshview);
        View v = LayoutInflater.from(this).inflate(R.layout.title_with_headbg_content,null);
        maskView = v.findViewById(R.id.transition_view);
        swipeRefreshView.addBodyLayout(v);
        mContentView = v.findViewById(R.id.activity_base_content);
        hiddenTitleBar(hiddenTitleBarFlag);
        initActivityContent();
        if (!hiddenTitleBarFlag) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleView.getLayoutParams();
            int height = PhoneInfoTools.dip2px(this, 48) + 1;
            boolean addSystemBarHeight = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && addSystemBarHeight) {
                height += PhoneInfoTools.getStatusBarHeightByRes(this);
            }
            params.height = height;
            titleView.setLayoutParams(params);
        }
    }

    public void hiddenTitleBar(boolean flag) {
        if(flag) {
            swipeRefreshView.setDividerHide(View.GONE);
            titleView.setVisibility(View.GONE);
        } else {
            swipeRefreshView.setDividerHide(View.GONE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    abstract protected void initActivityContent();

    @Override
    public void setActivityContent(Fragment fragment) {
        FragmentTransaction transaction = this.getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.activity_base_content, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void setActivityContent(int layoutId) {
        View view = mInflater.inflate(layoutId, null);
        setActivityContent(view);
    }

    public void setActivityContent(View view) {
        if (null != mContentView) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            mContentView.addView(view, params);
        }
    }

    @Override
    public boolean canSwipeback() {
        return true;
    }

    public void onBackClick(View view) {
        //按后退同时关闭输入法
        KeyboardHelper.hideSoftInput(this);
        this.defaultFinish();
    }

    protected void setTitleContent(String title){
        mTvTitle.setText(title);
    }

    protected void setTitleViewBackgroud(int drawable){
        titleView.setBackground(getResources().getDrawable(drawable));
    }

    protected void setTranslationViewGone(){
        maskView.setVisibility(View.GONE);
    }
}
