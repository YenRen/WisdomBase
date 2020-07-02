package com.hnxx.wisdombase.model.bean;

import com.hnxx.wisdombase.model.api.Accountinfo;

/**
 * @author zhoujun
 * @date 2019/12/30 16:59
 */
public class LoginMessage {
    private String token;
    private String logintime;
    private Accountinfo accountinfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public Accountinfo getAccountinfo() {
        return accountinfo;
    }

    public void setAccountinfo(Accountinfo accountinfo) {
        this.accountinfo = accountinfo;
    }
}
