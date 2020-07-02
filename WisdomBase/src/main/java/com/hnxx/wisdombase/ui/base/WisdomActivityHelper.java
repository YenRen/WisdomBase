package com.hnxx.wisdombase.ui.base;

import android.os.Bundle;
import android.view.View;

import com.hnxx.wisdombase.ui.widget.swipeback.SwipeBackActivityBase;
import com.hnxx.wisdombase.ui.widget.swipeback.SwipeBackActivityHelper;
import com.hnxx.wisdombase.ui.widget.swipeback.SwipeBackLayout;
import com.hnxx.wisdombase.ui.widget.swipeback.Utils;

/**
 * Created by zhoujun on 15/4/14.
 */
public class WisdomActivityHelper extends BaseActivityHelper
        implements SwipeBackActivityBase {

    public boolean canSwipeback = false;

    private SwipeBackActivityHelper mHelper;
    private SwipeBackLayout mSwipeBackLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getActivity()) {
            canSwipeback = getActivity().canSwipeback();
        }

        if (canSwipeback) {
            mHelper = new SwipeBackActivityHelper(getActivity());
            mHelper.onActivityCreate();
            mSwipeBackLayout = getSwipeBackLayout();
            mSwipeBackLayout.setEdgeTrackingEnabled(getActivity().getSwipebackFlag());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (canSwipeback) {
            setSwipebackEdgeMode();
        }

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (canSwipeback) {
            mHelper.onPostCreate();
        }
    }

    @Override
    public View findViewById(int id) {
        if (canSwipeback) {
            View v = super.findViewById(id);
            if (v == null && mHelper != null) {
                return mHelper.findViewById(id);
            }
            return v;
        }

        return null;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        if (getSwipeBackLayout() != null) {
            getSwipeBackLayout().setEnableGesture(enable);
        }
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(getActivity());
        getSwipeBackLayout().scrollToFinishActivity();
    }

    public void setSwipebackEdgeMode() {
        if (canSwipeback) {
            int mode = getActivity().getSwipebackFlag();//SettingsManager
            // .swipeback_mode_option.getValue();

            switch (mode) {
                case SwipeBackLayout.EDGE_LEFT:
                    mSwipeBackLayout
                            .setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
                    break;
                case SwipeBackLayout.EDGE_RIGHT:
                    mSwipeBackLayout
                            .setEdgeTrackingEnabled(SwipeBackLayout.EDGE_RIGHT);
                    break;
                case SwipeBackLayout.EDGE_BOTTOM:
                    mSwipeBackLayout.setEdgeTrackingEnabled(
                            SwipeBackLayout.EDGE_BOTTOM);
                    break;
                case SwipeBackLayout.EDGE_ALL:
                    mSwipeBackLayout
                            .setEdgeTrackingEnabled(SwipeBackLayout.EDGE_ALL);
                    break;
                default:
            }
        }
    }

    public interface EnableSwipeback {

        /**
         * 是否支持滑动后退
         *
         * @return
         */
        boolean canSwipeback();

        /**
         * 滑动后退的flag(如左滑，右滑
         *
         * @return
         */
        int getSwipebackFlag();
    }

}
