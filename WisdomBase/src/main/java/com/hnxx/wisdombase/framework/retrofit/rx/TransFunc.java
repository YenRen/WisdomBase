package com.hnxx.wisdombase.framework.retrofit.rx;


import com.hnxx.wisdombase.framework.retrofit.entity.Result;
import com.hnxx.wisdombase.framework.utils.GsonUtils;
import com.hnxx.wisdombase.framework.utils.ReflectionUtils;

import io.reactivex.functions.Function;

/**
 * Created by zhoujun on 2019/1/29.
 * Describe:这里进行解析message至data
 */
public class TransFunc<T> implements Function<T, T> {
    @Override
    public T apply(T t) {
        if (!(t instanceof Result)) {
            return t;
        }
        Result result = (Result) t;
        Object message = result.getData();
        if (message == null) {
            return t;
        }
        Object o = GsonUtils.fromJson(message.toString(), ReflectionUtils.mygetSuperClassGenricType(t.getClass(), 0));
        try {
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return t;
    }
}
