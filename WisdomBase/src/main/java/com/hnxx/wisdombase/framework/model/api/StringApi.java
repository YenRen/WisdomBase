package com.hnxx.wisdombase.framework.model.api;

import com.hnxx.wisdombase.framework.retrofit.entity.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * @author zhoujun
 * @date 2020/1/13 14:31
 */
public interface  StringApi {

    @GET
    Observable<Result<String>> StringRequest(
            @Url String url);
}
