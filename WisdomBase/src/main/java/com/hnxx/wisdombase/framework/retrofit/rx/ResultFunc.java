package com.hnxx.wisdombase.framework.retrofit.rx;

import com.hnxx.wisdombase.framework.retrofit.constants.ResultCode;
import com.hnxx.wisdombase.framework.retrofit.entity.Result;
import com.hnxx.wisdombase.framework.retrofit.exception.ServerException;

import io.reactivex.functions.Function;


/**
 * Created by zhoujun on 2019/1/28.
 * Describe:返回结果功能回调
 */
public class ResultFunc<T extends Result> implements Function<T, T> {
    @Override
    public T apply(T tResult) {
        if (!ResultCode.CODE_SUCCESS.equals(tResult.getCode())) {
            throw new ServerException(tResult.getCode(), tResult.getWrongmessage());
        }
        return tResult;
    }
}
