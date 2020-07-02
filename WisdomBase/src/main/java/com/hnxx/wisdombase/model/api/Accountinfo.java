package com.hnxx.wisdombase.model.api;

/**
 * @author zhoujun
 * @date 2020/1/8 9:21
 * 用户的所有信息  包括注册返回的所有信息，以及 个人的一些偏好设置
 */
public class Accountinfo {

    String phone;
    String userName;
    String sex;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
