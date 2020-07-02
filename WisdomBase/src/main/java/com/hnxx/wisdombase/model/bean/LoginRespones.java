package com.hnxx.wisdombase.model.bean;

import com.hnxx.wisdombase.framework.retrofit.entity.Result;

/**
 * @author zhoujun
 * @date 2019/12/30 16:55
 */
public class LoginRespones extends Result<LoginMessage> {
    private String accessToken;
    private String refreshToken;
    private String phone;
    private String name;
    private String userid;
    private String sex;
    private String avatar;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
