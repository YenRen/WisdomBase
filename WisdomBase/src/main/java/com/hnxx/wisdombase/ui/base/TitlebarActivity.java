/**
 *
 * @author zhoujun
 * @date Nov 6
 */
package com.hnxx.wisdombase.ui.base;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;
import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.config.application.IntentKey;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.hnxx.wisdombase.ui.utils.KeyboardHelper;
import com.hnxx.wisdombase.ui.widget.swiperfresh.SwipeRefreshView;

/**
 * @author zhoujun
 */
public abstract class TitlebarActivity extends BaseActivity{

	protected LayoutInflater mInflater;

	protected TextView title_tv;
	public RelativeLayout llytTitlebarContainer;
	private LinearLayout common_title_bar_rightmenu_ll;
	private ImageView btnBack;
	private LinearLayout.LayoutParams ivParams;
	protected TextView video_tv_all;
	protected FrameLayout activity_content;
	protected SwipeRefreshView swipeRefreshView;
	/** 0activity普通布局  1 webview */
	protected int layoutStyle = 0;
	protected String title;
	public boolean hiddenTitleBarFlag;
	protected AppBarLayout llTitle;
	private Toolbar toolbar;
    public CoordinatorLayout baseLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		hiddenTitleBarFlag = this.getIntent().getBooleanExtra("hiddenTitleBar", false);
		mInflater = this.getLayoutInflater();
        Bundle bundle = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		title = this.getIntent().getStringExtra(IntentKey.INTENT_KEY_TITLE);
		if (!TextUtils.isEmpty(title)) {
			setTitleBarText(title);
		}
	}

	abstract protected void initActivityContent();

	@Override
	public boolean canSwipeback() {
		//默认有返回按钮就能左滑后退
//		return btnBack.getVisibility()==View.VISIBLE;
		return true;
	}

	@Override
	final protected int getLayoutId() {
		return R.layout.activity_base;
	}

	public void setActivityContent(int layoutId) {
		View view = mInflater.inflate(layoutId, null);
		setActivityContent(view);
	}

	public void setActivityContent(View view) {
		if (null != activity_content) {
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT);
			activity_content.addView(view, params);
		}
	}

	@Override
	public void setActivityContent(Fragment fragment) {
		FragmentTransaction transaction = this.getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.activity_base_content, fragment);
		transaction.commitAllowingStateLoss();
	}

	public void setTitleBarText(String text) {
		if (null != title_tv) {
			title_tv.setText(text);
		}
	}

	public TextView getVideoTvAllTextView(int visibile){
		video_tv_all.setVisibility(visibile);
		return video_tv_all;
	}

	public void hiddenTitleBar(boolean flag) {
		if(flag) {
			llytTitlebarContainer.setVisibility(View.GONE);
			//vDivider.setVisibility(View.GONE);
			swipeRefreshView.setDividerHide(View.GONE);
			llTitle.setVisibility(View.GONE);
		} else {
			llytTitlebarContainer.setVisibility(View.VISIBLE);
			//vDivider.setVisibility(View.GONE);
			swipeRefreshView.setDividerHide(View.GONE);
			llTitle.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected int getToolBarId() {
		if (hiddenTitleBarFlag){
			return 0;
		}
		return R.id.toolbar;
	}

	@Override
	protected void findViewById() {
		swipeRefreshView = this.findViewById(R.id.swipeRefreshView);

		llTitle = this.findViewById(R.id.appbar);
		toolbar = this.findViewById(R.id.toolbar);
		if(layoutStyle==0){
			View v = LayoutInflater.from(this).inflate(R.layout.layout_frameview,null);
			activity_content = v.findViewById(R.id.activity_base_content);
			swipeRefreshView.addBodyLayout(v);
		}
        baseLayout = findViewById(R.id.baseLayout);
		video_tv_all = this.findViewById(R.id.video_tv_all);
		title_tv = this.findViewById(R.id.title_tv);
		llytTitlebarContainer = this.findViewById(R.id.commontitlebar_llyt_titlebar);
		//vDivider = this.findViewById(R.id.commontitlebar_v_divider);
		common_title_bar_rightmenu_ll = this.findViewById(R.id.common_title_bar_rightmenu_ll);
		setSupportActionBar(toolbar);
        btnBack = this.findViewById(R.id.back_iv);
		initActivityContent();
		hiddenTitleBar(hiddenTitleBarFlag);

		if (!hiddenTitleBarFlag) {
			CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) llTitle.getLayoutParams();
			int height = PhoneInfoTools.dip2px(this, 48) + 1;
			boolean addSystemBarHeight = true;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && addSystemBarHeight) {
				height += PhoneInfoTools.getStatusBarHeightByRes(this);
			}
			params.height = height;
			llTitle.setLayoutParams(params);
		}
	}

	/** 设置返回按钮是否显示 */
	protected void setBackBottonVisiable(boolean isVisiable) {
	    if (isVisiable) {
            btnBack.setVisibility(View.VISIBLE);
        } else {
	        btnBack.setVisibility(View.GONE);
        }

    }

	protected TextView addRightMenu(String text) {
		TextView view = (TextView) mInflater.inflate(R.layout.widget_title_bar_rightmenu_tv, null);
		view.setText(text);
		common_title_bar_rightmenu_ll.addView(view);
		return view;
	}

	protected TextView addRightMenu(String text,int drawableResId) {
		TextView view = (TextView) mInflater.inflate(R.layout.widget_title_bar_rightmenu_tv2, null);
		view.setText(text);

		Drawable leftDrawable = getResources().getDrawable(drawableResId);
		leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
		view.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getResources().getDisplayMetrics()));
		view.setCompoundDrawables(leftDrawable, null, null, null);
		common_title_bar_rightmenu_ll.addView(view);
		return view;
	}

	protected ImageView addRightMenu(int drawableResId) {
		if (null == ivParams) {
			int size = getResources()
					.getDimensionPixelSize(R.dimen.dimen_24dp);
			ivParams = new LinearLayout.LayoutParams(size, size);
			ivParams.setMargins(0, 0, getResources()
					.getDimensionPixelSize(R.dimen.dimen_13dp), 0);
		}
		ImageView view = (ImageView) mInflater.inflate(R.layout.widget_title_bar_rightmenu_iv, null);
		view.setBackgroundResource(drawableResId);
		common_title_bar_rightmenu_ll.addView(view, ivParams);
		return view;
	}

	/**
	 * 设置imageView src（意见反馈）
	 * @param drawableResId
	 * @return
	 */
	protected ImageView addRightMenuSrc(int drawableResId) {
		if (null == ivParams) {
			ivParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
			ivParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.dimen_18dp), 0);
}
	ImageView view = (ImageView) mInflater.inflate(R.layout.widget_title_bar_rightmenu_iv, null);
		view.setImageResource(drawableResId);
				common_title_bar_rightmenu_ll.addView(view, ivParams);
				return view;
				}

	protected RelativeLayout addRightMenuRl(int drawableResId) {
		if (null == ivParams) {
			int size = getResources()
					.getDimensionPixelSize(R.dimen.custom_width);
			int size2 = getResources()
					.getDimensionPixelSize(R.dimen.custom_height);
			ivParams = new LinearLayout.LayoutParams(size, size2);
			ivParams.setMargins(0, 0, getResources()
					.getDimensionPixelSize(R.dimen.dimen_13dp), 0);
		}
		RelativeLayout view = (RelativeLayout) mInflater.inflate(R.layout.widget_title_bar_rightmenu_rl, null);
		ImageView image = view.findViewById(R.id.image);
		image.setBackgroundResource(drawableResId);
		common_title_bar_rightmenu_ll.addView(view, ivParams);
		return view;
	}

	public void onBackClick(View view) {
		//按后退同时关闭输入法
		KeyboardHelper.hideSoftInput(this);
		this.defaultFinish();
	}

	/**
	 * 开始加载数据
	 *
	 * @param isShowContent 是否显示主体内容
	 */
	protected void onDataloadStart(boolean isShowContent) {
//		progressbar_ll.setVisibility(View.GONE);
		swipeRefreshView.startRefresh();
		swipeRefreshView.setOnRefreshEndListener(new SwipeRefreshView.OnRefreshEndListener() {
			@Override
			public void onRefreshEnd() {
				activity_content.setVisibility(View.VISIBLE);
			}
		});
		if (isShowContent) {
			activity_content.setVisibility(View.VISIBLE);
		} else {
			activity_content.setVisibility(View.GONE);
		}
	}

	/**
	 * 加载数据完成
	 */
	protected void onDataloadFinished() {
//		progressbar_ll.setVisibility(View.GONE);
		swipeRefreshView.stopRefresh();
	}

	protected boolean isLoadingData() {
//		return View.VISIBLE == progressbar_ll.getVisibility();
		return false;
	}

	public void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.slide_left_out);
	}


	public View getViewOnTitleBarRight() {
		View view = null;
		if (common_title_bar_rightmenu_ll != null) {
			int childCount = common_title_bar_rightmenu_ll.getChildCount();
			if (childCount > 0) {
				view = common_title_bar_rightmenu_ll.getChildAt(childCount - 1);
			}
		}

		return view;
	}

	public AppBarLayout getLlTitle() {
		return llTitle;
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}


	CollapsingToolbarLayout collapsingToolbarLayout;
	public void setBackground(int res){
		collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
		swipeRefreshView.setBgColor(Color.parseColor("#00000000"));
		llTitle.setBackgroundColor(Color.parseColor("#00000000"));
		collapsingToolbarLayout.setBackgroundColor(Color.parseColor("#00000000"));
		toolbar.setBackgroundColor(Color.parseColor("#00000000"));
		llytTitlebarContainer.setBackgroundColor(Color.parseColor("#00000000"));
		baseLayout.setBackground(getResources().getDrawable(res));
	}
}
