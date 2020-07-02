package com.hnxx.wisdombase.framework.retrofit.entity;

/**
 * Created by zhoujun on 2019/6/5.
 * 处理不规则返回
 */
public class StringResult extends Result<String> {
    public String key;
    public String key_type;
    public long expires_in;
}
