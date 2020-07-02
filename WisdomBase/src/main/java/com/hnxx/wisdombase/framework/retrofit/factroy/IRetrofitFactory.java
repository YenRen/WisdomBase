package com.hnxx.wisdombase.framework.retrofit.factroy;

import retrofit2.Retrofit;

/**
 * @author zhoujun
 * @date 2019/12/19
 * Describe:retrofit工厂
 */
public interface IRetrofitFactory {
    Retrofit createRetrofit(String url);
}
