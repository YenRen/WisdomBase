package com.hnxx.wisdombase.ui.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hnxx.wisdombase.framework.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;

public class BaseCommonWebActivity extends TitlebarActivity{

    private static final String TAG = "BaseCommonWebActivity";
    protected Context mContext;

    @Override
    protected void initActivityContent() {
        mContext = this;
        EventBus.getDefault().register(this);
//        setActivityContent(R.layout.base_native_webview_activity);
        initData();
    }

    public void initData() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm.isActive()) {
//            if (myNativeWebView.getWindowToken() != null) {
//                imm.hideSoftInputFromWindow(myNativeWebView.getWindowToken(), 0);
//            }
//        }
    }

    private void loadUrl() {
//        if (m_strUrl != null) {
//            myNativeWebView.loadUrl(m_strUrl);
//        }
    }
    private boolean isOnBack;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (!TextUtils.isEmpty(nowUrl) && !nowUrl.contains("actapril/index.action")&&myNativeWebView.canGoBack()) {
//                myNativeWebView.goBack();
//                isOnBack = true;
//            } else {
//                finish();
//            }
//        }
        return false;
    }

    private void back() {
        finish();
    }

    @Override
    protected void findViewById() {
        layoutStyle = 1;
        super.findViewById();
//        View view = LayoutInflater.from(this).inflate(R.layout.layout_nativewebview, null);
    }

    @Override
    public void onBackClick(View view) {
        this.defaultFinish();
    }

    @Override
    protected void setListener() {

    }



//    public void showLoadError(String failingUrl) {
//        no_net.setVisibility(View.VISIBLE);
//        myNativeWebView.setVisibility(View.GONE);
//        swipeRefreshView.stopRefresh();
//    }
//
//    @Override
//    public void onPageFinished(WebView view, String url) {
//        swipeRefreshView.stopRefresh();
//        myNativeWebView.setWebViewLoadFinished(null);
//    }
//
//    @Override
//    public void progressChanged(int newProgress) {
//        if (newProgress >= 90 && !isFails) {
//            no_net.setVisibility(View.GONE);
//            myNativeWebView.setVisibility(View.VISIBLE);
//            isFails = false;
//        }
//
//    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.d(TAG, "onNewIntent");
        super.onNewIntent(intent);
    }


    @Override
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        swipeRefreshView.startRefresh();
    }


//    private String convertViewToBitmap(View mWebView) {
//        mWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        mWebView.layout(0, 0, mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
//        mWebView.setDrawingCacheEnabled(true);
//        mWebView.buildDrawingCache();
//        Bitmap longImage = Bitmap.createBitmap(mWebView.getMeasuredWidth(),
//                mWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        // 画布的宽高和 WebView 的网页保持一致
//        Canvas canvas = new Canvas(longImage);
//        Paint paint = new Paint();
//        canvas.drawBitmap(longImage, 0, mWebView.getMeasuredHeight(), paint);
//        mWebView.draw(canvas);
//
//        String path = WoPath.instance().picCachePath;
//        String fileName = "share_423.jpg";
//        String mShareImgPath = path + fileName;
//        SavaImage(longImage, path, "share_423");
//        mWebView.setDrawingCacheEnabled(false);
//        return mShareImgPath;
//    }

    public void SavaImage(Bitmap bitmap, String path, String name) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + name + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
