/**
 * Copyright (c) hb-cloud 2015
 *
 * @author Sunky
 * @date 2015年11月19日
 */
package com.hnxx.wisdombase.db.sp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.hnxx.wisdombase.config.application.WisdomApplication;

import java.io.Serializable;

/**
 * SharedPreferences基类
 * @author zhoujun
 *
 */
public abstract class BaseSp implements Serializable {
    private final static String SP_NAME_PREFIX = "UnicomWisdom_";

    private Context mCtx;
    protected SharedPreferences mSp;

    protected SharedPreferences.Editor mEditor;

    /**
     * 设置自定义SP名称，请保证全局唯一，自定义的SP名称将自动加上前缀组成真实的SP名称
     * @return
     */
    protected abstract String getCustomSpName();


    public BaseSp() {
        mCtx = WisdomApplication.Instance().getContext();
        mSp = mCtx.getSharedPreferences(getSpName(), Activity.MODE_PRIVATE);
        mEditor = mSp.edit();
    }

    public void setContext(Context ctx) {
        this.mCtx = ctx;
    }

    public Context getContext() {
        return mCtx;
    }

    /**
     * 获取真实Sp名称(SP_NAME_PREFIX + getCustomSpName())
     * @return
     */
    public final String getSpName() {
        return SP_NAME_PREFIX + getCustomSpName();
    }

    /**
     * 清空配置
     */
    public void clearAll() {
        mEditor.clear();
        mEditor.commit();
    }

    /**
     * 清空指定key的缓存
     * @param key
     */
    public void remove(String key) {
        mEditor.remove(key);
        mEditor.commit();
    }

    public void put(String key, String value) {
        mEditor.putString(key, value);
        mEditor.commit();
    }

    public void put(String key, int value) {
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    public void put(String key, boolean value) {
        mEditor.putBoolean(key, value);
        mEditor.commit();
    }

    public void put(String key, float value) {
        mEditor.putFloat(key, value);
        mEditor.commit();
    }

    public void put(String key, long value) {
        mEditor.putLong(key, value);
        mEditor.commit();
    }

    public String getString(String key) {
        return mSp.getString(key, "");
    }

    public String getString(String key, String defValue) {
        return mSp.getString(key, defValue);
    }

    public int getInt(String key) {
        return mSp.getInt(key, 0);
    }

    public int getInt(String key, int defValue) {
        return mSp.getInt(key, defValue);
    }

    /**
     * 获取Boolean类型配置
     * @param key
     * @return 如果不存在则返回false
     */
    public boolean getBoolean(String key) {
        return mSp.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSp.getBoolean(key, defValue);
    }

    public float getFloat(String key) {
        return mSp.getFloat(key, 0);
    }

    public float getFloat(String key, float defValue) {
        return mSp.getFloat(key, defValue);
    }

    public long getLong(String key) {
        return mSp.getLong(key, 0);
    }

    public long getLong(String key, long defValue) {
        return mSp.getLong(key, defValue);
    }

    public SharedPreferences getSp() {
        return mSp;
    }
}
