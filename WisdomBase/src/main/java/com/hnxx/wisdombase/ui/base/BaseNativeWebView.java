/**
 *
 */
package com.hnxx.wisdombase.ui.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.hnxx.wisdombase.framework.storage.WisdomPath;
import com.hnxx.wisdombase.ui.utils.DateUtil;
import com.hnxx.wisdombase.ui.widget.CustomToast;

/**
 * 自定义WebView组件
 *
 * @author zhoujun
 */
public class BaseNativeWebView extends WebView {
    private static final String TAG = "BaseWebView";
//    private WebViewLoadFinished webViewLoadFinished;
//    private WebProgressChanged webProgressChanged;
    protected Activity activity;
    protected Context context;
    private boolean blockLoadingNetworkImage;

    /**
     * 是否启用本地缓存机制
     */
    private boolean enableCache = false;

    /**
     * 本地缓存过期时间
     */
    private long expireTime = 24 * 3600 * 1000;

    public BaseNativeWebView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public BaseNativeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }


    public void init() {
        //clearCache(true);
        final WebSettings mWebSettings = this.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSavePassword(false);
        mWebSettings.setDefaultTextEncodingName("UTF-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(true);
        }
        // 是否使用WebView自带的cache
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSettings.setAppCacheMaxSize(4 * 1024 * 1024);
        mWebSettings.setAppCachePath(WisdomPath.instance(getContext().getApplicationContext()).webviewCachePath + "innerCache");
        mWebSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        // you xian ji
        mWebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 添加输入框获取焦点代码
        this.setFocusable(true);
        this.requestFocusFromTouch();

        mWebSettings.setDatabaseEnabled(false);
        mWebSettings.setDomStorageEnabled(false);
        String databasePath = getContext().getDir("databases",
                Context.MODE_PRIVATE).getPath();
        setVerticalScrollBarEnabled(false);  //取消Vertical ScrollBar显示
        setHorizontalScrollBarEnabled(false);// 取消Horizontal ScrollBar显示
        // mWebSettings.setDatabasePath(cachePath);
        // String path1 = mWebSettings.getDatabasePath();
        // 设置在页面装载完成之后再去加载图片
        //mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setBlockNetworkImage(true);
        blockLoadingNetworkImage = true;
        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onExceededDatabaseQuota(String url,
                                                String databaseIdentifier, long currentQuota,
                                                long estimatedSize, long totalUsedQuota,
                                                QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(estimatedSize * 2);
            }
            // 扩充缓存的容量
            @Override
            public void onReachedMaxAppCacheSize(long spaceNeeded,
                                                 long totalUsedQuota, QuotaUpdater quotaUpdater) {
                quotaUpdater.updateQuota(spaceNeeded * 2);
            }
            /*
             * @Override public boolean onJsAlert(WebView view, String url,
             * String message, JsResult result) {
             * Toast toast = Toast.makeText(context, message,
             * Toast.LENGTH_SHORT); toast.show(); return true; }
             */

            @Override
            public void onProgressChanged(WebView view, int progress) {
                if (progress >= 99) {
                    if (blockLoadingNetworkImage) {
                        mWebSettings.setBlockNetworkImage(false);
                        blockLoadingNetworkImage = false;
                    }
                }
            }

        });

        mWebSettings.setBuiltInZoomControls(false);
        setWebViewClient(new MyWebViewClient());
        initJsImagView();
    }

    @SuppressLint("JavascriptInterface")
    public void initJsImagView() {
        addJavascriptInterface(/*new JavascriptInterface(getContext())*/this, "imagelistner");
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，
        // 函数的功能是在图片点击的时候调用本地java接口并传递url过去
        this.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    @JavascriptInterface
    public void openImage(String img) {
        CustomToast.showToast(context,img, Toast.LENGTH_SHORT);
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();
            if(loadFinishLisenter!=null) {
                loadFinishLisenter.webViewLoadFinished();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private OnWebViewLoadFinishLisenter loadFinishLisenter;

    public interface OnWebViewLoadFinishLisenter{
        void webViewLoadFinished();
    }

    public void setOnWebViewLoadFinishLisenter(OnWebViewLoadFinishLisenter loadFinishLisenter){
        this.loadFinishLisenter = loadFinishLisenter;
    }


}
