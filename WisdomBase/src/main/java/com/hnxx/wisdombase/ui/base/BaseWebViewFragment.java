package com.hnxx.wisdombase.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.ui.interfacor.BannerViewMotionEventInterface;
import com.hnxx.wisdombase.ui.interfacor.WebViewLoadFinished;
import com.hnxx.wisdombase.ui.utils.DateUtil;
import com.hnxx.wisdombase.ui.widget.swiperfresh.SwipeRefreshView;

import org.greenrobot.eventbus.EventBus;

/**
 * WebView基础框架(自动适应Base和Main工程)
 *
 * @author zhoujun
 */
public class BaseWebViewFragment extends BaseFragment
        implements WebViewLoadFinished{

	private static final String TAG = "BaseWebViewFragment";

	protected BaseNativeWebView myNativeWebView;

	public SwipeRefreshView swipeRefreshView;

//	public View progressbar;
	protected String requestUrl = "";

	private View no_net;

	protected TextView wifi_check_settings;

	private BannerViewMotionEventInterface bannerViewMotionEventInterface;

	public long outTime = DateUtil.ONE_HOUR * 2;


	/** 是否开启懒加载 */
	private boolean isEnableLazyLoad = false;

	/** 是否开启WebView缓存 */
	private boolean isEnableCache = false;

	private boolean enableScroll = true; // 默认webview可以滑动

	private boolean reloadWeb;

	private boolean isEnablePullFresh;

	/** 页面加载状态值 */
	private enum LoadState {
		/** 未初始化 */
		LOAD_ST_INIT, /** 加载中 */
		LOAD_ST_ING, /** 加载成功 */
		LOAD_ST_OK, /** 加载失败 */
		LOAD_ST_FAIL
	}

	/** 页面加载状态 */
	private LoadState mLoadState = LoadState.LOAD_ST_INIT;

	private boolean RedirectUrl = false;
	private String mPageName = "";

	@Override
	protected int getLayoutId() {
				return R.layout.no_pull_base_native_webview;
	}

	@Override
	protected void findViewById() {
		swipeRefreshView = findViewById(R.id.swipeRefreshWebView);
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.layout_basemynativewebview,null);
		myNativeWebView = v.findViewById(R.id.my_nativewebview);
		swipeRefreshView.addBodyLayout(v);

		myNativeWebView.setVerticalScrollBarEnabled(false);  //取消Vertical ScrollBar显示
		myNativeWebView.setHorizontalScrollBarEnabled(false);// 取消Horizontal ScrollBar显示
		wifi_check_settings = findViewById(R.id.wifi_check_settings);
	}

	@Override
	protected void setListener() {

		myNativeWebView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {
				return true;
			}
		});
	}

	@SuppressLint("JavascriptInterface")
    @Override
	protected void init() {

	}

	public BaseWebViewFragment() {}

	/**
	 * Webview构建(默认不启用缓存、禁止下拉刷新)
	 * @param url
	 *            需加载的网址
	 */
	public BaseWebViewFragment(String url) {
		setUrl(url);
		setEnableCache(false);
	}

	/**
	 * Webview构建
	 *
	 * @param url
	 *            需加载的网址
	 * @param isEnableCache
	 *            是否启用页面缓存
	 * @param isEnablePullFresh
	 *            是否启用下拉刷新
	 */
	public BaseWebViewFragment(String url, boolean isEnableCache,
                               boolean isEnablePullFresh) {
		setUrl(url);
		setEnableCache(isEnableCache);
	}
	

	public String getUrl() {
		return requestUrl;
	}
	
	public void setUrl(String url) {
//		if (url.contains("http"))
		requestUrl = url;
	}


	@Override
	public void onPageFinished(WebView view, String url) {
		loadUrlOK();
	}
	
	public BaseNativeWebView getMyNativeWebView() {
		return myNativeWebView;
	}
	
	public void setNativeWebView(BaseNativeWebView myNativeWebView) {
		this.myNativeWebView = myNativeWebView;
	}
	
	public boolean isEnableCache() {
		return isEnableCache;
	}
	
	public void setEnableCache(boolean isEnableCache) {
		this.isEnableCache = isEnableCache;
	}

	
	/**
	 * 页面加载成功
	 */
	private void loadUrlOK() {
		mLoadState = LoadState.LOAD_ST_OK;
		swipeRefreshView.stopRefresh();
		myNativeWebView.setVisibility(View.VISIBLE);

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}


	boolean hasBanner;
	public boolean isHasBanner() {
		return hasBanner;
	}
	public void setHasBanner(boolean hasBanner) {
		this.hasBanner = hasBanner;
	}

}
