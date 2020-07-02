package com.hnxx.wisdombase.db.sp;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.hnxx.wisdombase.framework.utils.GsonUtils;
import com.hnxx.wisdombase.model.bean.LoginRespones;

/**
 * @author zhoujun
 * @date 2020/1/8 8:49
 */
public class LoginSp extends BaseSp {

    public static final String LOGIN_RES = "loginRes";

    @Override
    protected String getCustomSpName() {
        return "LoginSp";
    }

    private static volatile LoginSp mLoginSp;

    public static LoginSp getInstance() {
        if (mLoginSp == null) {
            synchronized (LoginSp.class) {
                if (mLoginSp == null) {
                    mLoginSp = new LoginSp();
                }
            }
        }
        return mLoginSp;
    }

    public void saveLoginRes(LoginRespones loginRes) {
        //对LoginRes进行缓存
        try {
            if (loginRes == null) {
//                clearAll();
//                mEditor.remove(LOGIN_RES).apply();
            } else {
                String s = GsonUtils.toJson(loginRes);
                if (!TextUtils.isEmpty(s)) {
                    mEditor.putString(LOGIN_RES, s).apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public LoginRespones getLoginRes() {
        try {
            String loginRes = getString(LOGIN_RES, "");
            if (!TextUtils.isEmpty(loginRes)) {
                LoginRespones bean = GsonUtils.fromJson(loginRes, new TypeToken<LoginRespones>() {});
                return bean;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
