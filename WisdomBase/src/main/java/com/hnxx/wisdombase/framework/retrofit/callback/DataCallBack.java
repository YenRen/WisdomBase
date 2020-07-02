package com.hnxx.wisdombase.framework.retrofit.callback;


import com.hnxx.wisdombase.framework.retrofit.entity.Result;

/**
 * Created by zhoujun on 2019/12/17.
 * Describe:只需要实体数据的CallBack
 */
public abstract class DataCallBack<T> extends WisCallback<Result<T>> {

    @Override
    protected final void onSuccessData(Result<T> tResult) {
        onSuccessData(tResult.getData());
    }

    protected abstract void onSuccessData(T message);

}
