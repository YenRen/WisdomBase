package com.hnxx.wisdombase.ui.base;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.hnxx.wisdombase.listener.AppBarStateChangeListener;
import com.hnxx.wisdombase.ui.widget.sliding.slidingtab.SlidingTabLayout;

/**
 * @author: zhoujun
 * @date: 2018/1/2 at 16:26
 */
public abstract class BaseMainFragment extends BaseFragment {

    private CoordinatorLayout rootLayout;
    private AppBarLayout appBarLayout;
    protected Toolbar toolbar;
    private SlidingTabLayout mTabLayout;
    private FrameLayout flContent;
    private View ivSearch;
    private ImageView ivSetting;
    private View contentView;
    private TextView tvTitle;
    private LinearLayout llSlidingTab;
    private ImageView ivTabMore;
    private ImageView ivTitle;
    private String title;
    private View titleLine;
    private ImageView ivTabShadow;
    private LinearLayout vNoNet;
    private OnClickListener onFreshClickListener;

    /**
     * 内容布局id
     *
     * @return
     */
    protected abstract int getContentLayoutId();

    protected abstract void findViewByIdMain();

    protected abstract void setListenerMain();

    @Override
    protected int getLayoutId() {
        return R.layout.base_main_fragment;
    }

    protected void setAppBarScrollFlags(int flags){
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(flags);
        toolbar.setLayoutParams(params);
    }

    @Override
    protected void findViewById() {
        rootLayout = super.findViewById(R.id.root_layout);
        appBarLayout = super.findViewById(R.id.appbar);
        toolbar = super.findViewById(R.id.toolbar);
        mTabLayout = super.findViewById(R.id.tf_tab_layout);
        flContent = super.findViewById(R.id.fl_content);
        llSlidingTab = super.findViewById(R.id.ll_sliding_tab);
        ivTabShadow = super.findViewById(R.id.iv_tab_shadow);
        tvTitle = super.findViewById(R.id.home_title_bar_title_tv);
        ivTitle = super.findViewById(R.id.home_title_bar_title_iv);
        ivSearch = super.findViewById(R.id.home_title_bar_search_iv);
        ivSetting = super.findViewById(R.id.home_title_bar_setting_iv);
        ivTabMore = super.findViewById(R.id.iv_tab_more);
        titleLine = super.findViewById(R.id.title_line);
        vNoNet = super.findViewById(R.id.no_net);

        showTitle();
    }


    @Override
    protected void setListener() {
        if (onClickListener != null) {
            ivSearch.setOnClickListener(onClickListener);
            ivSetting.setOnClickListener(onClickListener);
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                LogUtil.d("wayne", "STATE--" + state.name());
                //展开状态
                if (state == State.EXPANDED) {
                    titleLine.setVisibility(View.INVISIBLE);
                } else {
                    titleLine.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void setOnTitleViewClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void setTabIndex(int index, String title) {
        this.curIndex = index;
        this.title = title;
    }

    @Override
    protected void init() {
        if (getContentLayoutId() != 0) {
            contentView = View.inflate(getActivity(), getContentLayoutId(), null);
            flContent.addView(contentView);
        }
        setTabLayoutVisible(false);
        setTabIvMoreVisible(false);
        findViewByIdMain();
        setListenerMain();
    }

    protected void showTitle() {
        getTvTitle().setText(title);
    }

    @Override
    protected <V extends View> V findViewById(int resId) {
        if (null != contentView) {
            return (V) contentView.findViewById(resId);
        } else {
            return null;
        }
    }

    protected void setTabLayoutVisible(boolean isVisible) {
        llSlidingTab.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    protected void setTabIvMoreVisible(boolean isVisible) {
        ivTabMore.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        ivTabShadow.setVisibility(isVisible ? View.VISIBLE : View.GONE);

        //显示前5个自定义的频道（屏幕宽度像素640以下显示4个）
        int widthPx = PhoneInfoTools.getWidth(getActivity());
        int width = PhoneInfoTools.px2dip(getActivity(), widthPx);
        if (isVisible) {
            width -= 50;
        }
        mTabLayout.setmTabValidWidth(width);
    }

    protected void showNoNetView(boolean isShow) {
        if(isAdded()) {
            llSlidingTab.setVisibility(isShow ? View.GONE : View.VISIBLE);
            if (isShow) {
                vNoNet.setVisibility(View.VISIBLE);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) vNoNet.getLayoutParams();
                params.setMargins(0, (int) getResources().getDimension(R.dimen.common_tab_height2), 0, 0);
                vNoNet.setGravity(Gravity.CENTER);
            } else {
                vNoNet.setVisibility(View.GONE);
            }
        }
    }

    protected void setOnFreshClickListener(OnClickListener listener) {
        this.onFreshClickListener = listener;
    }

    public CoordinatorLayout getRootLayout() {
        return rootLayout;
    }

    public AppBarLayout getAppBarLayout() {
        return appBarLayout;
    }

    public SlidingTabLayout getmTabLayout() {
        return mTabLayout;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public View getIvSearch() {
        return ivSearch;
    }

    public ImageView getIvSetting() {
        return ivSetting;
    }

    public ImageView getIvTabMore() {
        return ivTabMore;
    }

    public ImageView getIvTitle() {
        return ivTitle;
    }

    public View getTitleLine() {
        return titleLine;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
