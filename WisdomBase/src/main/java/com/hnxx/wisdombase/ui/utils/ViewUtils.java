/**
 * 版权所有：中兴通讯股份有限公司
 * 文件名称：ViewUtils.java
 * 文件作者：xiezhuoxun
 * 开发时间：2013-3-21
 */
package com.hnxx.wisdombase.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hnxx.wisdombase.framework.utils.LogUtil;

import java.util.concurrent.atomic.AtomicInteger;


public class ViewUtils {
    private static long lastClickTime;

    /**
     * 防止按钮连续点击 的方法
     * 很多点击事件连续点击时都会发生异常，用这个类可以控件按钮不可连续点击
     * 这里设置按钮在连续一秒之内不可点击两次
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            LogUtil.d("ViewUtils", "点击时间间隔小于1s,点击失效！");
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick(int second) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < second * 1000) {
            LogUtil.d("ViewUtils", "点击时间间隔小于" + second + "s,点击失效！");
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 毫秒数
     * @param millisSecond
     * @return
     */
    public static boolean isFastDoubleClickMs(long millisSecond) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < millisSecond) {
            LogUtil.d("ViewUtils", "点击时间间隔小于" + millisSecond + "Ms,点击失效！");
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        setListViewHeightBasedOnChildren(listView, false);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView,
                                                        boolean fastCompute) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        int iCount = listAdapter.getCount();
        if (!fastCompute) {
            // 遍历计算
            for (int i = 0; i < iCount; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height =
                    totalHeight + (listView.getDividerHeight() * (iCount - 1)) +
                            listView.getPaddingBottom() +
                            listView.getPaddingTop();
            listView.setLayoutParams(params);
        } else {
            // 快速计算高度，只有当type相同时，listitem高度一致时才能这样计算
            SparseArray<TypeData> typeGroupMap = new SparseArray<TypeData>();
            for (int i = 0; i < iCount; i++) {
                int type = listAdapter.getItemViewType(i);
                TypeData data = typeGroupMap.get(type);
                if (null == data) {
                    data = new TypeData(type);
                    View listItem = listAdapter.getView(i, null, listView);
                    listItem.measure(0, 0);
                    data.height = listItem.getMeasuredHeight();
                    data.count = 1;
                    typeGroupMap.put(type, data);
                } else {
                    ++data.count;
                }
            }
            int listHeight = 0;
            int count = typeGroupMap.size();
            for (int index = 0; index < count; ++index) {
                TypeData d = typeGroupMap.valueAt(index);
                listHeight += d.height * d.count;
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height =
                    listHeight + (listView.getDividerHeight() * (iCount - 1)) +
                            listView.getPaddingBottom() +
                            listView.getPaddingTop();
            listView.setLayoutParams(params);
        }
    }


    public static void setListViewHeightByFirstItem(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int iCount = listAdapter.getCount();
        // 遍历计算
        View listItem = listAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        int itemHeigth = listItem.getMeasuredHeight();
        int totalHeigth = itemHeigth * iCount;
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeigth + (listView.getDividerHeight() * (iCount - 1)) + listView.getPaddingBottom() + listView.getPaddingTop();
        listView.setLayoutParams(params);

    }


    static class TypeData {
        final int type;
        int count;
        int height;

        TypeData(int type) {
            this.type = type;
        }
    }

    public static int getListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return -1;
        }
        int totalHeight = 0;
        int iCount = listAdapter.getCount();
        for (int i = 0; i < iCount; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem == null) {
                continue;
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        return totalHeight + (listView.getDividerHeight() * (iCount - 1));
    }

    /**
     * parserImageWidthByUrl
     * <p/>
     * Description: 根据url获取图片的宽度
     * <p/>
     *
     * @param url eg:http://10.118.15.31/cnt/image/1008/11008635/cover_176.jpg
     * @return 176
     * @author zhoujun
     */
    public static int parserImageWidthByUrl(final String url) {
        int underLineIndex = url.lastIndexOf('_');
        int lastPointIndex = url.lastIndexOf('.');
        int width = 0;
        if (underLineIndex < lastPointIndex) {
            try {
                String imageWidth =
                        url.substring(underLineIndex + 1, lastPointIndex);
                width = Integer.parseInt(imageWidth);
            } catch (IndexOutOfBoundsException e) {
                width = 0;
            } catch (NumberFormatException e) {
                width = 0;
            }
        }
        return width;
    }

    public static int getX(View view) {
        int tranX = view.getLeft();
        for (ViewParent parent = view.getParent(); parent instanceof ViewGroup;
             parent = parent.getParent()) {
            tranX += ((View) parent).getLeft();
        }
        return tranX;
    }

    public static int getY(View view) {
        int tranY = view.getTop();
        for (ViewParent parent = view.getParent(); parent instanceof ViewGroup;
             parent = parent.getParent()) {
            tranY += ((View) parent).getTop();
        }
        return tranY;
    }

    public static Point getCenter(View view) {
        int topLeftX = getX(view);
        int topLeftY = getY(view);
        int width = view.getWidth();
        int height = view.getHeight();
        return new Point(topLeftX + width / 2, topLeftY + height / 2);
    }

    /**
     * 判断点击的坐标是否在页面之外。
     * <p>
     * 使用场景比如：自定义的Activity,设置为Dialog样式，需要在点击页面之外的地方关闭页面。（在api11前无内置方法）。
     *
     * @param activty 页面
     * @param event   MotionEvent
     * @return
     */
    public static boolean isOutOfBounds(Activity activty, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(activty)
                .getScaledWindowTouchSlop();
        final View decorView = activty.getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    /**
     * view 淡入淡出
     *
     * @param visible
     * @param view
     */
    public static void animateVisibleAction(boolean visible, final View view) {
        view.clearAnimation();
        if (visible) {
            view.setVisibility(View.VISIBLE);
            view.animate().alpha(1f).setDuration(800).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //当invisible动画运行时，出现图标不显示情况
                    if (view.getVisibility() != View.VISIBLE) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.animate().alpha(0f).setDuration(800).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
    }

    /**
     * 判断触点是否落在该View上
     */
    public static boolean isTouchInView(MotionEvent ev, View v) {
        int[] vLoc = new int[2];
        v.getLocationOnScreen(vLoc);
        float motionX = ev.getRawX();
        float motionY = ev.getRawY();
        return motionX >= vLoc[0] && motionX <= (vLoc[0] + v.getWidth())
                && motionY >= vLoc[1] && motionY <= (vLoc[1] + v.getHeight());
    }

    //当 View 有一点点不可见时立即返回false!
    public static boolean isVisibleLocal(View target) {
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        return rect.top == 0;
    }

    private static AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * 获取一个未被使用的id,用于View.setId();
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return View.generateViewId();
        }
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) {
                newValue = 1; // Roll over to 1, not 0.
            }
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    /**
     * 把自身从父View中移除
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * 适配RecyclerView+NestedScrollView的滑动卡顿
     *
     * @param recyclerView
     * @Author zhoujun
     */
    public static void nestedRecyclerView(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            throw new RuntimeException("请先设置LayoutManager");
        }
        layoutManager.setAutoMeasureEnabled(true);
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
        }
        recyclerView.setFocusableInTouchMode(false);//设置不需要焦点
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);//为了解决滑动不流畅的问题
    }
}
