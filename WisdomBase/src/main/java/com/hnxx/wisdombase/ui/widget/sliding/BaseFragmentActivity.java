package com.hnxx.wisdombase.ui.widget.sliding;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.hnxx.wisdombase.config.application.WisdomApplication;

public class BaseFragmentActivity extends FragmentActivity{
	// Service instance for RPC

	public WisdomApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (WisdomApplication) this.getApplication();
//		mApplication.setCurrentActivity(this);
		mApplication.addActivity(this);
	}


	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mApplication.removeActivity(this);
	}

	@Override
    public void finish() {
		super.finish();
	}

	public void defaultFinish() {
		super.finish();
	}

	/**
	 * <p>
	 * Description: 通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午11:24:29
	 * @author zhoujun
	 * @param cls
	 */
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/**
	 * <p>
	 * Description: 含有Bundle通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午11:24:32
	 * @author zhoujun
	 * @param cls
	 * @param bundle
	 */
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		this.startActivity(intent);
		// mActivity.overridePendingTransition(R.anim.push_left_in,
		// R.anim.slide_left_out);
	}

	/**
	 * <p>
	 * Description: 通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:15:23
	 * @author zhoujun
	 * @param cls
	 */
	public void startActivityForResult(Class<?> cls, int requestCode) {
		startActivityForResult(cls, null, requestCode);
	}

	/**
	 * <p>
	 * Description: 含有Bundle通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:15:35
	 * @author zhoujun
	 * @param cls
	 * @param bundle
	 */
	public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivityForResult(intent, requestCode);
	}
}
