package com.hnxx.wisdombase.ui.widget.viewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.hnxx.wisdombase.ui.interfacor.BannerViewMotionEventInterface;


/**
 * 自定义VierPager，支持指定Banner滑动区域
 * Created by zhoujun on 2019/12/31.
 */
public class ZViewPager extends ViewPager {

    private BannerViewMotionEventInterface mBannerInterface;

    public ZViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isInBannerView(ev)) {
            return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public boolean isInBannerView(MotionEvent event) {
        if (mBannerInterface == null) {
            return false;
        } else {
            return mBannerInterface.isInBannerView(event);
        }
    }

    public BannerViewMotionEventInterface getBannerInterface() {
        return mBannerInterface;
    }

    public void setBannerInterface(BannerViewMotionEventInterface bannerInterface) {
        this.mBannerInterface = bannerInterface;
    }

    private boolean mIsDisallowIntercept = false;

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        // keep the info about if the innerViews do
        // requestDisallowInterceptTouchEvent
        mIsDisallowIntercept = disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // the incorrect array size will only happen in the multi-touch
        // scenario.
        if (ev.getPointerCount() > 1 && mIsDisallowIntercept) {
            requestDisallowInterceptTouchEvent(false);
            boolean handled = super.dispatchTouchEvent(ev);
            requestDisallowInterceptTouchEvent(true);
            return handled;
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }
}
