package com.hnxx.wisdombase.framework.interfaces;


import io.reactivex.disposables.Disposable;

/**
 * Created by zhoujun on 2018/9/20.
 * Describe:
 */
public interface LifeDisposable {
    void addDisposable(Disposable disposable);
}

