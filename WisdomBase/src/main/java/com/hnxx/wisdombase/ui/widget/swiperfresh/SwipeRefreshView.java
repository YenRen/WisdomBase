package com.hnxx.wisdombase.ui.widget.swiperfresh;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hnxx.wisdombase.R;

/**
 *  注意这个布局 SwipeRefreshView 只能动态加载里面的子View 才会起作用 在xml里添加子View是不起作用滴
 *  如果想在xml中直接添加子View 即可使用原生控件
 *  加载动画
 *  @author zhoujun
 *  @date 2019年12月15日11:14:16
 */

public class SwipeRefreshView extends FrameLayout{

    private MySwipeRefreshLayout swipeRefreshLayout;
    private OnRefreshEndListener onRefreshEndListener;
    private OnPullRefreshingListener onPullRefreshingListener;
    private View divider;
    private boolean isNeedDivider = false;
    private FrameLayout bodyLayout;
    public static final int REFRESH_STATE_LOAD = 0;
    public static final int REFRESH_STATE_PULL = 1;
    private int refreshState;
    private View sMainView;

    public SwipeRefreshView(Context context) {
        super(context);
    }

    public SwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(!isInEditMode()) {
            View view = LayoutInflater.from(context).inflate(R.layout.swipe_loading_view, null);
            addView(view);
            swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
            divider = findViewById(R.id.divider);
            bodyLayout = findViewById(R.id.content_layout);
            sMainView = findViewById(R.id.swp_main_view);
            //默认不能下拉
            swipeRefreshLayout.setEnabled(false);
            swipeRefreshLayout.setColorSchemeResources(R.color.color_pink);
            swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
            swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                refreshState = REFRESH_STATE_PULL;
                    if(onPullRefreshingListener!=null){
                        onPullRefreshingListener.onPullRefreshing();
                    }
            });
        }
    }

    public void addBodyLayout(View view){
        bodyLayout.removeAllViews();
        bodyLayout.addView(view);
    }

    /***
     * 停止刷新
     */
    public void stopRefresh(){
        if(refreshState == REFRESH_STATE_PULL){//下拉
        }else {
            if(onRefreshEndListener!=null){
                onRefreshEndListener.onRefreshEnd();
            }
        }
        swipeRefreshLayout.postDelayed(() ->
                swipeRefreshLayout.setRefreshing(false),500);
    }


    /***
     * 开始刷新并加载动画
     */
    public void startRefresh(){
        refreshState = REFRESH_STATE_LOAD;
            //自动加载
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
    }

    public boolean isNeedDivider() {
        return isNeedDivider;
    }

    public void setNeedDivider(boolean needDivider) {
        isNeedDivider = needDivider;
    }

    /***
     * 设置是否需要下拉刷新
     */
    public void setNeedPullRefresh(boolean flag){
        swipeRefreshLayout.setEnabled(flag);
    }

    /***
     * 解决滑动冲突
     */
    public void setChildView(View view){
        swipeRefreshLayout.setScrollUpChild(view);
    }

    public interface OnRefreshEndListener{
        void onRefreshEnd();
    }

    public interface OnPullRefreshingListener{
        void onPullRefreshing();
    }

    public void setOnRefreshEndListener(OnRefreshEndListener onRefreshEndListener) {
        this.onRefreshEndListener = onRefreshEndListener;
    }

    public void setDividerHide(int visibility){
        divider.setVisibility(visibility);
    }

    public void setOnPullRefreshingListener(OnPullRefreshingListener onPullRefreshingListener) {
        this.onPullRefreshingListener = onPullRefreshingListener;
    }

    /**
     * 获取是否在刷新中
     * @return
     */
    public boolean isRefreshing(){
        if (swipeRefreshLayout != null) {
            return swipeRefreshLayout.isRefreshing();
        }
        return false;
    }

    public void setBgColor(int color){
        sMainView.setBackgroundColor(color);
    }
}
