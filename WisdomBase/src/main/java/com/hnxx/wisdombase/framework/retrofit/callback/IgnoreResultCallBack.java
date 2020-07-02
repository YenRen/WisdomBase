package com.hnxx.wisdombase.framework.retrofit.callback;

/**
 *
 * @author zhoujun
 * @date 2019/12/19
 * Describe:不需要处理成功回调的CallBack
 */
public class IgnoreResultCallBack<T> extends DataCallBack<T> {
    @Override
    protected final void onSuccessData(T message) {

    }
}
