package com.hnxx.wisdombase.framework.retrofit.entity;

import com.hnxx.wisdombase.framework.retrofit.constants.ResultCode;

/**
 * @author zhoujun
 * @date 2019/12/19
 * Describe:
 */
public class Result<T> {
    private String code = ResultCode.CODE_SUCCESS;//天杀的，接口还出现不给innercode的情况，我怕还有不给code的情况，先设个默认成功的默认值
    private String innercode = ResultCode.CODE_SUCCESS;
    private T data;
    private String msg;
    private int status = -1;
    private String wrongmessage;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWrongmessage() {
        return wrongmessage;
    }

    public void setWrongmessage(String wrongmessage) {
        this.wrongmessage = wrongmessage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getInnercode() {
        return innercode;
    }

    public void setInnercode(String innercode) {
        this.innercode = innercode;
    }

//    public T getMessage() {
//        return data;
//    }
//
//    public void setMessage(T message) {
//        this.data = message;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
