package com.hnxx.wisdombase.framework.retrofit.callback;



import com.hnxx.wisdombase.framework.retrofit.exception.ApiException;
import com.hnxx.wisdombase.framework.retrofit.exception.ReqErr;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhoujun on 2019/12/17.
 * Describe:请求回调处理
 */
public abstract class Callback<T> {

    private boolean isLoading;


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public final void onComplete() {
        onEnd();
    }

    public final void onSubscribe(Disposable d) {

    }

    /**
     * 错误处理
     *
     * @param e
     */
    public final void onError(Throwable e) {
        e.printStackTrace();
        onEnd();
//        onFail(e);
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            //没有捕捉的错误
            onError(new ApiException(e, ReqErr.UNKNOWN));
        }
    }

    /**
     * 错误回调
     */
    protected abstract void onError(ApiException ex);

    public abstract void onNext(T t);


    /**
     * 无论成功失败，都会执行的回调
     */
    protected void onEnd() {

    }

}
