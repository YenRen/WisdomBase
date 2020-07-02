package com.hnxx.wisdombase.framework.retrofit.callback;

import com.hnxx.wisdombase.framework.retrofit.entity.Result;

/**
 * @author zhoujun
 * @date 2020/1/8 15:06
 */
public abstract class WisModelCallback<T> extends WisCallback<Result<T>>{

    @Override
    protected void onSuccessData(Result<T> tResult) {
        onSuccessModel(tResult.getData());
    }

    public abstract void onSuccessModel(T t);
}
