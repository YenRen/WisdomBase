package com.hnxx.wisdombase.model.bean;


/**
 * Created by zhoujun on 2019/6/12.
 * Describe:
 */
public class LoginReqBean {

    private String userName; //用户名/手机号
    private String userCode; //密码/验证码
    private int loginType;   //登录方式  0为密码登录 1为验证码登录
    public static final int TYPE_PWD = 0;
    public static final int TYPE_VERCODE = 1;

    public LoginReqBean(String userName, String pwd,int type) {
        this.userName = userName;
        this.userCode = pwd;
        this.loginType = type;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }
}
