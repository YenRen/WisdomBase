/**
 * <p>
 * Copyright: Copyright (c) 2013
 * Company: ZTE
 * Description: 所有Activity的基类
 * </p>
 * @Title BaseActivity.java
 * @Package com.unicom.zworeader.ui.activity
 * @version 1.0
 * @author XieZhuoxun
 * @date 2013年8月13日 下午5:31:16
 */
package com.hnxx.wisdombase.ui.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.hnxx.wisdombase.ui.widget.CustomToast;

/**
 * BaseActivity类
 * 
 * @ClassName:BaseActivity
 * @Description: 所有Activity的基类
 * @author: zhoujun
 * @date: 2013年8月13日 下午5:31:16
 * 
 */
@Deprecated
public abstract class V3BaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		findViewById();
		setListener();
		init();
	}

	/**
	 * <p>
	 * Description: 通过ID绑定控件
	 * <p>
	 * 
	 * @date 2013年8月14日 上午8:57:58
	 * @author XieZhuoxun
	 */
	protected abstract void findViewById();

	/**
	 * <p>
	 * Description: 设置控件的监听事件
	 * <p>
	 * 
	 * @date 2013年8月14日 上午8:58:46
	 * @author XieZhuoxun
	 */
	protected abstract void setListener();

	/**
	 * <p>
	 * Description: 界面数据初始化
	 * <p>
	 * 
	 * @date 2013年8月14日 上午8:59:13
	 * @author XieZhuoxun
	 */
	protected abstract void init();

	/**
	 * <p>
	 * Description: 短暂显示Toast提示(来自res)
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:14:17
	 * @author XieZhuoxun
	 * @param resId
	 */
	public void showShortToast(int resId) {
		Toast toast = Toast
				.makeText(this, getString(resId), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * <p>
	 * Description: 短暂显示Toast提示(来自String)
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:14:31
	 * @author XieZhuoxun
	 * @param text
	 */
	public void showShortToast(String text) {
		CustomToast.showToast(this, text, Toast.LENGTH_SHORT);
	}

	/**
	 * <p>
	 * Description: 长时间显示Toast提示(来自res)
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:14:42
	 * @author XieZhuoxun
	 * @param resId
	 */
	public void showLongToast(int resId) {
		CustomToast.showToast(this, getString(resId), Toast.LENGTH_LONG);
	}

	/**
	 * <p>
	 * Description: 长时间显示Toast提示(来自String)
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:14:55
	 * @author XieZhuoxun
	 * @param text
	 */
	public void showLongToast(String text) {
		CustomToast.showToast(this, text, Toast.LENGTH_LONG);
	}

	/**
	 * <p>
	 * Description: 通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:15:23
	 * @author XieZhuoxun
	 * @param cls
	 */
	public void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/**
	 * <p>
	 * Description: 通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:15:23
	 * @author XieZhuoxun
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
	 * @author XieZhuoxun
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
		// overridePendingTransition(R.anim.push_left_in, R.anim.slide_left_out);
	}

	/**
	 * <p>
	 * Description: 含有Bundle通过Class跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:15:35
	 * @author XieZhuoxun
	 * @param cls
	 * @param bundle
	 */
	public void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.slide_left_out);
	}

	/**
	 * <p>
	 * Description: 通过Action跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:15:50
	 * @author XieZhuoxun
	 * @param action
	 */
	public void startActivity(String action) {
		startActivity(action, null);
	}

	/**
	 * <p>
	 * Description: 含有Bundle通过Action跳转界面
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:16:01
	 * @author XieZhuoxun
	 * @param action
	 * @param bundle
	 */
	public void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
		// overridePendingTransition(R.anim.push_left_in, R.anim.slide_left_out);
	}

	/**
	 * <p>
	 * Description: 含有标题和内容的对话框
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:16:22
	 * @author XieZhuoxun
	 * @param title
	 * @param message
	 * @return
	 */
	public AlertDialog showAlertDialog(String title, String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).show();
		return alertDialog;
	}

	/**
	 * <p>
	 * Description: 含有标题、内容、两个按钮的对话框
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:16:34
	 * @author XieZhuoxun
	 * @param title
	 * @param message
	 * @param positiveText
	 * @param onPositiveClickListener
	 * @param negativeText
	 * @param onNegativeClickListener
	 * @return
	 */
	public AlertDialog showAlertDialog(String title, String message,
                                       String positiveText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       String negativeText,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/**
	 * <p>
	 * Description: 含有标题、内容、图标、两个按钮的对话框
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:16:49
	 * @author XieZhuoxun
	 * @param title
	 * @param message
	 * @param icon
	 * @param positiveText
	 * @param onPositiveClickListener
	 * @param negativeText
	 * @param onNegativeClickListener
	 * @return
	 */
	public AlertDialog showAlertDialog(String title, String message, int icon,
                                       String positiveText,
                                       DialogInterface.OnClickListener onPositiveClickListener,
                                       String negativeText,
                                       DialogInterface.OnClickListener onNegativeClickListener) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle(title)
				.setMessage(message).setIcon(icon)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
		return alertDialog;
	}

	/**
	 * <p>
	 * Description: 带有右进右出动画的退出
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:17:04
	 */
	@Override
    public void finish() {
		super.finish();
		// overridePendingTransition(R.anim.push_right_in,
		// R.anim.push_right_out);
	}

	/**
	 * <p>
	 * Description: 默认退出
	 * <p>
	 * 
	 * @date 2013年8月14日 上午9:17:15
	 * @author XieZhuoxun
	 */
	public void defaultFinish() {
		super.finish();
	}

	/**
	 *
	 * isLogin
	 * <p>
	 * Description: 是否登录
	 * <p>
	 *
	 * @date 2013-9-23
	 * @author lily
	 * @return
	 */
//	@Override
//    public boolean isLogin() {
//        return AccountUtil.isLogined();
//
//	}

}
