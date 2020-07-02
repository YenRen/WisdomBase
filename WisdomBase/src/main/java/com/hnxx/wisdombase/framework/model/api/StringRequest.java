package com.hnxx.wisdombase.framework.model.api;

import com.hnxx.wisdombase.config.application.AppConfig;
import com.hnxx.wisdombase.framework.retrofit.ApiManager;
import com.hnxx.wisdombase.framework.retrofit.constants.RetrofitFactoryType;
import com.hnxx.wisdombase.framework.retrofit.entity.Result;

import io.reactivex.Observable;

/**
 * @author zhoujun
 * @date 2020/1/13 14:30
 */
public class StringRequest  extends BaseApiReq<StringApi>{
    String requsetUrl;

    @Override
    protected StringApi createApi() {
//        return  ApiManager.getInstance().createApi(StringApi.class,requsetUrl,null);
        return ApiManager.getInstance().createApi(StringApi.class, null, null, true);

    }


    public Observable<Result<String>> stringRequest(String url){
        return getApi().StringRequest(url);
    }
}
