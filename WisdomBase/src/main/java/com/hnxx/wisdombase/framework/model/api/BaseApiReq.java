package com.hnxx.wisdombase.framework.model.api;


import com.hnxx.wisdombase.framework.utils.GsonUtils;

import okhttp3.RequestBody;

/**
 * Created by zhoujun on 2019/12/18.
 * Describe:
 */
public abstract class BaseApiReq<T> {
    private T api;

    public T getApi() {
        if (api == null) {
            api = createApi();
        }
        return api;
    }

    protected abstract T createApi();

    /**
     * 默认方式创建RequestBody
     *
     * @param obj 直接以json方式传递
     * @return
     */
    public RequestBody buildRequestBody(Object obj) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), GsonUtils.toJson(obj));
        return body;
    }
}
