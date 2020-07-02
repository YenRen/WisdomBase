package com.hnxx.wisdombase.framework.retrofit;


import io.reactivex.disposables.Disposable;

/**
 * @author zhoujun
 * @date 2019/12/19
 * Describe:
 */
public interface LifeDisposable {
    void addDisposable(Disposable disposable);
}

