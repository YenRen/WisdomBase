package com.hnxx.wisdombase.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

/**
 * 用户注册回调BaseActivity的生命周期及相关的方法，自行添加
 * <p/>
 * Created by wangdan on 15/4/14.
 */
public class BaseActivityHelper {

    private BaseActivity mActivity;

    protected void bindActivity(BaseActivity activity) {
        this.mActivity = activity;
    }

    protected BaseActivity getActivity() {
        return mActivity;
    }

    protected void onCreate(Bundle savedInstanceState) {

    }

    public void onPostCreate(Bundle savedInstanceState) {

    }

    public View findViewById(int id) {
        return mActivity.findViewById(id);
    }

    protected void onStart() {

    }

    protected void onRestart() {

    }

    protected void onResume() {

    }

    protected void onPause() {

    }

    protected void onStop() {

    }

    public void onDestroy() {

    }

    public void finish() {

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public boolean onBackClick() {
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

}
