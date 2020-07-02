package com.hnxx.wisdombase.ui.base;

import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.appbar.AppBarLayout;
import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.hnxx.wisdombase.listener.AppBarStateChangeListener;
import com.hnxx.wisdombase.ui.interfacor.BannerViewMotionEventInterface;
import com.hnxx.wisdombase.ui.widget.sliding.slidingtab.SlidingTabLayout;
import com.hnxx.wisdombase.ui.widget.viewpager.PageSelectedListener;
import com.hnxx.wisdombase.ui.widget.viewpager.ZViewPager;

import java.util.ArrayList;

/**
 * @author: zhoujun
 * @date: 2018/1/9 at 19:43
 */

public abstract class NestedTabFragment extends BaseFragment implements View.OnClickListener, BannerViewMotionEventInterface {

    private CoordinatorLayout rootLayout;
    private AppBarLayout appBarLayout;
    protected Toolbar toolbar;
    private RelativeLayout mTitleBgView;
    protected SlidingTabLayout mTabLayout;
    private ZViewPager zViewPager;
    private View titleLine;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivSearch;
    private LinearLayout llRightMenu;
    private TextView tvVideoAll;
    private FrameLayout tabLeftContainer;
    private FrameLayout tabRightContainer;
    private ImageView mDividerView;
    private View mLineBlewTitle;

    private LinearLayout.LayoutParams ivParams;
    private MyPagerAdapter mTabAdapter;
    private PageSelectedListener pageSelectedListener;

    /**
     * 设置此参数可以改变Tab位置
     */
    private int currTabIndex;
    private boolean hasProceesTabWidth;
    protected FrameLayout bottomLayout;
    private OnRightRedDotClick onRightRedDotClick;
    /**
     * 填充布局在tablayout左边或者右边
     */
    public enum TabMoreGravity {
        LEFT,
        RIGHT
    }

    protected abstract boolean shouldHiddenTitleBar();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_nested_tab;
    }

    @Override
    protected void findViewById() {
        rootLayout = super.findViewById(R.id.root_layout);
        appBarLayout = super.findViewById(R.id.appbar);
        toolbar = super.findViewById(R.id.toolbar);
        mTitleBgView = super.findViewById(R.id.commontitlebar_llyt_titlebar);
        mTabLayout = super.findViewById(R.id.tf_tab_layout);
        zViewPager = super.findViewById(R.id.tf_view_pager);
        titleLine = super.findViewById(R.id.title_line);
        mLineBlewTitle = super.findViewById(R.id.title_blew_line);

        tabLeftContainer = super.findViewById(R.id.tab_left_container);
        tabRightContainer = super.findViewById(R.id.tab_right_container);
        mDividerView = super.findViewById(R.id.divider_line);
        tvTitle = this.findViewById(R.id.title_tv);
        ivBack = this.findViewById(R.id.back_iv);
        ivSearch = this.findViewById(R.id.iv_search);
        llRightMenu = this.findViewById(R.id.common_title_bar_rightmenu_ll);
        tvVideoAll = this.findViewById(R.id.video_tv_all);
        bottomLayout = this.findViewById(R.id.bottomLayout);
        ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootLayout.setLayoutParams(layoutParams);
        setToolbarScrollFlag();
    }

    @Override
    protected void setListener() {
        ivBack.setOnClickListener(this);
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

    /**
     * 设置页头滑动模式
     */
    protected void setToolbarScrollFlag(){
        if (toolbar != null){
            //默认scroll|enterAlways|snap
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        }
    }

    @Override
    protected void init() {
        rootLayout.setBackgroundColor(getResources().getColor(R.color.white));
        if (shouldHiddenTitleBar()) {
            appBarLayout.setVisibility(View.GONE);
        }

        mTabAdapter = new MyPagerAdapter(getChildFragmentManager());
        initTab();

        zViewPager.setAdapter(mTabAdapter);

        if (!hasProceesTabWidth) {
            mTabLayout.setmTabValidWidth(PhoneInfoTools.getwidthdip(getActivity()));
        }

        mTabLayout.setViewPager(zViewPager);


        initTabPosition();
        zViewPager.setBannerInterface(this);

        zViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //如果Activity支持滑动后退，子fragment滑动到第一个则开启后退
                adaptSwipe(position==0);
                if (pageSelectedListener != null) {
                    pageSelectedListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 设置是否开启滑动关闭
     * @param enableSwipe
     */
    private void adaptSwipe(boolean enableSwipe) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity && ((BaseActivity) activity).canSwipeback()) {
            BaseActivityHelper activityHelper = ((BaseActivity) activity).getActivityHelper();
            if (activityHelper instanceof WisdomActivityHelper) {
                ((WisdomActivityHelper) activityHelper).setSwipeBackEnable(enableSwipe);
            }
        }
    }

    /**
     * 在tablayout左边或者右边添加布局（此方法一定在addTab之后调用，需要根据tab个数计算宽度）
     *
     * @param view
     * @param gravity
     */
    protected void setTabContainerView(View view, TabMoreGravity gravity) {
        if (view != null && mTabAdapter != null) {
            hasProceesTabWidth = true;

            int sWidth = PhoneInfoTools.getWidth(getActivity());
            int count = mTabAdapter.getCount();
            if (count > 3) {
                //tab大余三个只占三个的宽度
                count = 5;
            } else {
                count += 2;
            }
            int tabWidth = sWidth / count;
            int tabHeight = PhoneInfoTools.dip2px(getActivity(), getResources().getDimension(R.dimen.common_tab_height));

            RelativeLayout.LayoutParams tabParams = (RelativeLayout.LayoutParams) mTabLayout.getLayoutParams();
            tabParams.width = sWidth - tabWidth * 2;
            tabParams.height = tabHeight;
            mTabLayout.setLayoutParams(tabParams);
            mTabLayout.setmTabValidWidth(PhoneInfoTools.px2dip(getActivity(), tabParams.width));

            LogUtil.d("wayne", "tabWidth:" + tabWidth + ", tabHeight:" + tabHeight);

            if (gravity == TabMoreGravity.LEFT) {
                RelativeLayout.LayoutParams layoutParamsL = (RelativeLayout.LayoutParams) tabLeftContainer.getLayoutParams();
                layoutParamsL.width = tabWidth;
                layoutParamsL.height = tabHeight;
                tabLeftContainer.setLayoutParams(layoutParamsL);
                tabLeftContainer.setVisibility(View.VISIBLE);

                tabLeftContainer.addView(view);
            } else {
                RelativeLayout.LayoutParams layoutParamsR = (RelativeLayout.LayoutParams) tabRightContainer.getLayoutParams();
                layoutParamsR.width = tabWidth;
                layoutParamsR.height = tabHeight;
                tabRightContainer.setLayoutParams(layoutParamsR);
                tabRightContainer.setVisibility(View.VISIBLE);

                tabRightContainer.addView(view);
            }
        }
    }

    /**
     * 在tablayout左边或者右边添加布局（此方法一定在addTab之后调用，需要根据tab个数计算宽度）
     *  tab平铺
     * @param view
     * @param margin
     */
    protected void setTabContainerView(View view,int margin) {
        if (view != null && mTabAdapter != null) {
            int sWidth = PhoneInfoTools.getWidth(getActivity());
            int count = mTabAdapter.getCount();
            int tabWidth = (sWidth - margin) / count;
            int tabHeight = PhoneInfoTools.dip2px(getActivity(), getResources().getDimension(R.dimen.common_tab_height));

            RelativeLayout.LayoutParams tabParams = (RelativeLayout.LayoutParams) mTabLayout.getLayoutParams();
            tabParams.width = tabWidth * count;
            tabParams.height = tabHeight;
            mTabLayout.setLayoutParams(tabParams);
            mTabLayout.setmTabValidWidth(PhoneInfoTools.px2dip(getActivity(), tabParams.width));

            tabParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tabRightContainer.setVisibility(View.VISIBLE);
            tabRightContainer.addView(view);
        }

    }

    /**
     * tabLayout左边布局是否可见
     *
     * @param visible
     */
    protected void setTabLeftContainerVisible(boolean visible) {
        tabLeftContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * tabLayout左边布局是否可见
     *
     * @param visible
     */
    protected void setTabRightContainerVisible(boolean visible) {
        tabRightContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取tabLayout左边布局
     *
     * @return
     */
    public View getTabLeftContainer() {
        View view = null;
        if (tabLeftContainer != null) {
            int childCount = tabLeftContainer.getChildCount();
            if (childCount > 0) {
                view = tabLeftContainer.getChildAt(childCount - 1);
            }
        }
        return view;
    }

    /**
     * 获取tabLayout右边布局
     *
     * @return
     */
    public View getTabRightContainer() {
        View view = null;
        if (tabLeftContainer != null) {
            int childCount = tabLeftContainer.getChildCount();
            if (childCount > 0) {
                view = tabLeftContainer.getChildAt(childCount - 1);
            }
        }
        return view;
    }

    /**
     * OnFragmentStartedListener用于fragment start回调之后外部获取控件不为空 可对view进行操作
     */
    private OnFragmentStartedListener onFragmentStartedListener;

    public interface OnFragmentStartedListener {
        void onFragmentStarted();
    }

    public void setOnFragmentStartedListener(OnFragmentStartedListener onFragmentStartedListener) {
        this.onFragmentStartedListener = onFragmentStartedListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (onFragmentStartedListener != null) {
            onFragmentStartedListener.onFragmentStarted();
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTitleBgColor(int color) {
        mTitleBgView.setBackgroundColor(color);
    }


    public void setTileLineVisible(){
        mLineBlewTitle.setVisibility(View.VISIBLE);
    }

    public void setTitle(int resId) {
        tvTitle.setText(getString(resId));
    }

    public ImageView getIvSearch() {
        return ivSearch;
    }

    public TextView getVideoTvAllTextView(int visibile) {
        tvVideoAll.setVisibility(visibile);
        return tvVideoAll;
    }

    public TextView addRightMenu(String text) {
        TextView view = (TextView) mInflater.inflate(R.layout.widget_title_bar_rightmenu_tv, null);
        view.setText(text);
        llRightMenu.addView(view);
        return view;
    }

    public ImageView addRightMenu(int drawableResId) {
        if (null == ivParams) {
            int size = getResources().getDimensionPixelSize(R.dimen.iconsize_titlebar);
            ivParams = new LinearLayout.LayoutParams(size, size);
            ivParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.common_padding_30px), 0);
        }
        ImageView view = (ImageView) mInflater
                .inflate(R.layout.widget_title_bar_rightmenu_iv, null);
        view.setBackgroundResource(drawableResId);
        llRightMenu.addView(view, ivParams);
        return view;
    }

    public View getViewOnTitleBarRight() {
        View view = null;
        if (llRightMenu != null) {
            int childCount = llRightMenu.getChildCount();
            if (childCount > 0) {
                view = llRightMenu.getChildAt(childCount - 1);
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back_iv) {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                if (activity instanceof BaseActivity) {
                    ((BaseActivity) activity).defaultFinish();
                } else {
                    activity.finish();
                }
            }
        } else if (view.getId() == R.id.layout_expire_remind) {
            if(onRightRedDotClick!=null){
                onRightRedDotClick.onRedDotClick();
            }
        }
    }

    /**
     * 设置页面选择发生变化时的监听
     *
     * @param pageSelectedListener
     */
    public void setPageSelectedListener(final PageSelectedListener pageSelectedListener) {
        this.pageSelectedListener = pageSelectedListener;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (listener != null && zViewPager != null) {
            zViewPager.setOnPageChangeListener(listener);
        }
    }

    /**
     * 增加Tab
     *
     * @param newTab  使用自定义Fragment作为Tab
     * @param tabName Tab名称
     */
    public void addTab(Fragment newTab, String tabName) {
        if (newTab == null || TextUtils.isEmpty(tabName)) {
            return;
        }

        mTabAdapter.addFragment(newTab, tabName);
    }

    /**
     * 增加Tab (默认不启用页面缓存，不启用下拉刷新, 不带Banner)
     *
     * @param url     需加载的网址
     * @param tabName Tab名称
     */
    public void addTab(String url, String tabName) {
        addTab(url, tabName, false, false);
    }

    /**
     * 增加Tab (默认不带Banner)
     *
     * @param url               需加载的网址
     * @param tabName           Tab名称
     * @param isEnableCache     是否启用页面缓存
     * @param isEnablePullFresh 是否启用下拉刷新
     */
    public void addTab(String url, String tabName, boolean isEnableCache,
                       boolean isEnablePullFresh) {
        addTab(url, tabName, isEnableCache, isEnablePullFresh, false);
    }

    /**
     * 增加Tab
     *
     * @param url               需加载的网址
     * @param tabName           Tab名称
     * @param isEnableCache     是否启用页面缓存
     * @param isEnablePullFresh 是否启用下拉刷新
     * @param hasBanner         是否存在Banner(仅允许在最上面固定位置)
     */
    public void addTab(String url, String tabName, boolean isEnableCache,
                       boolean isEnablePullFresh, boolean hasBanner) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(tabName)) {
            return;
        }

        BaseWebViewFragment fragment = new BaseWebViewFragment();
        fragment.setUrl(url);
        fragment.setEnableCache(isEnableCache);
//        fragment.setEnablePullFresh(isEnablePullFresh);
//
//        fragment.setHasBanner(hasBanner);
        addTab(fragment, tabName);
    }


    public void addTab(String url, String fromPage, String tabName, boolean isEnableCache,
                       boolean isEnablePullFresh, boolean hasBanner, String pageId, String actionId) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(tabName)) {
            return;
        }
        BaseWebViewFragment fragment = new BaseWebViewFragment();
        fragment.setUrl(url);
        fragment.setEnableCache(isEnableCache);
        addTab(fragment, tabName);
    }

    /**
     * 获取当前选中的Tab Index
     *
     * @return
     */
    public int getSelectedTabIndex() {
        return zViewPager.getCurrentItem();
    }

    /**
     * 初始化Tab，请使用addTab增加。
     */
    protected abstract void initTab();

    /**
     * 初始化Tab位置 通过currTabIndex设定
     */
    private void initTabPosition() {
        setTabPosition(currTabIndex);
    }

    /**
     * 改变Tab位置
     *
     * @param index 此参数可以改变Tab位置
     */
    public void setTabPosition(int index) {
        zViewPager.setCurrentItem(index);
    }


    /**
     * 设置Tab位置
     *
     * @param currTabIndex 此参数可以初始化Tab位置
     */
    public void setCurrTabIndex(int currTabIndex) {
        this.currTabIndex = currTabIndex;
    }

    @Override
    public boolean isInBannerView(MotionEvent e) {
        try {
            if (mTabAdapter == null || mTabAdapter.getCurrentFragment() == null){
                return false;
            }
            Fragment curFragment = mTabAdapter.getCurrentFragment();
            if (!(curFragment instanceof BaseWebViewFragment)) {
                return false;
            }

            BaseWebViewFragment webFragment = (BaseWebViewFragment) curFragment;
            if (!webFragment.isHasBanner()) {
                return false;
            }

            int titleHeight = PhoneInfoTools.dip2px(mCtx, 30 + 56);
            int screenWidth = PhoneInfoTools.getWidth(mCtx);
            int bannery = screenWidth * 18 / 64;

            float y = e.getY();

            if(webFragment.getMyNativeWebView() == null){
                return false;
            }

            float scrolly = webFragment.getMyNativeWebView().getScrollY();

            if (titleHeight + bannery - scrolly > y) {
                webFragment.getMyNativeWebView().setFocusable(true);
                webFragment.getMyNativeWebView().requestFocus();
                if (scrolly < 2) {
                    webFragment.getMyNativeWebView().scrollTo(0, 1);
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
    }

    public BaseWebViewFragment getFragmentForPage(int i) {
        if (mTabAdapter == null || i >= mTabAdapter.getCount()) {
            return null;
        } else {
            Fragment curFragment = mTabAdapter.getItem(i);
            if (!(curFragment instanceof BaseWebViewFragment)) {
                return null;
            }
            BaseWebViewFragment webFragment = (BaseWebViewFragment) curFragment;
            return webFragment;
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mFragments = new ArrayList<>();
        private ArrayList<String> mTitles = new ArrayList<>();

        private Fragment currentFragment = null;

        private Fragment getCurrentFragment() {
            return currentFragment;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            currentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        public void addFragment(Fragment newTab, String tabName) {
            mTitles.add(tabName);
            mFragments.add(newTab);
        }
    }

    public void setmDividerViewViSibility(boolean visiable){
        mDividerView.setVisibility(visiable?View.VISIBLE:View.GONE);
    }

    public interface OnRightRedDotClick{
        void onRedDotClick();
    }

    public void setOnRightRedDotClick(OnRightRedDotClick onRightRedDotClick) {
        this.onRightRedDotClick = onRightRedDotClick;
    }

    public Toolbar getViewAppBar (){
        return toolbar;
    }

}
