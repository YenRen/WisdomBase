
package com.hnxx.wisdombase.ui.widget.swipeback;

import android.os.Bundle;
import android.view.View;
import com.hnxx.wisdombase.ui.base.V3BaseActivity;

public class SwipeBackActivity extends V3BaseActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null) {
            return mHelper.findViewById(id);
        }
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

	/**
	 * 这里写方法名
	 * <p>
	 * Description: 这里用一句话描述这个方法的作用
	 * <p>
	 * @date 2015-5-29
	 */
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 这里写方法名
	 * <p>
	 * Description: 这里用一句话描述这个方法的作用
	 * <p>
	 * @date 2015-5-29
	 */
	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 这里写方法名
	 * <p>
	 * Description: 这里用一句话描述这个方法的作用
	 * <p>
	 * @date 2015-5-29
	 */
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		
	}

}
