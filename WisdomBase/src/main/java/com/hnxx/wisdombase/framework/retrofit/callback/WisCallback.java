package com.hnxx.wisdombase.framework.retrofit.callback;

import com.google.gson.reflect.TypeToken;
import com.hnxx.wisdombase.framework.retrofit.constants.Constants;
import com.hnxx.wisdombase.framework.retrofit.constants.ResultCode;
import com.hnxx.wisdombase.framework.retrofit.entity.Result;
import com.hnxx.wisdombase.framework.retrofit.exception.ApiException;
import com.hnxx.wisdombase.framework.retrofit.exception.ReqErr;
import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.framework.utils.UIUtils;

/**
 * @author zhoujun
 * @date 2019/12/19
 */
public abstract class WisCallback<T extends Result> extends Callback<T> {

    private TypeToken<T> typeToken;

    public WisCallback() {
    }

    @Override
    public final void onNext(T result) {
        onSuccessData(result);//
    }

    protected abstract void onSuccessData(T t);

    @Override
    protected final void onError(ApiException exception) {
        forceDo(exception);
        onFail(exception);
    }

    /**
     * 针对错误进行一些强制性需要处理的逻辑。
     *
     * @param exception
     */
    private void forceDo(ApiException exception) {
        Throwable t = exception.getCause() == null ? exception : exception.getCause();

        LogUtil.w(Constants.TAG, LogUtil.getStackTrace(exception));
        //针对所有code进行模板处理，同时也支持重写
        switch (exception.getCode()) {
            case ResultCode.CODE_TOKEN_FAIL://token失效
                break;
            default:
                break;
        }

    }

    /**
     * 交由用户实现，目前简单toast
     *
     * @param exception
     */
    protected void onFail(ApiException exception) {
        //针对所有code进行模板处理，同时也支持重写
        switch (exception.getCode()) {
            case ResultCode.CODE_FAIL://失败
                onFailData(exception);
                break;
            case ResultCode.CODE_TOKEN_FAIL://token失效
                onTokenFail(exception);
                break;
//            case ResultCode.CODE_REORDER_FAIL://该用户已订购该产品（内容或者套餐）
//                onReOrderFail(exception);
//                break;
            case ReqErr.HTTP_ERROR:
                onNetError(exception);
                break;
            case ReqErr.NETWORK_ERROR:
                onNetError(exception);
                break;
            case ReqErr.PARSE_ERROR:
                UIUtils.toast(exception.getDisplayMessage());
                break;
            default:
                UIUtils.toast(exception.getDisplayMessage());
                break;
        }
    }

    protected void onNetError(ApiException exception) {
        UIUtils.toast(exception.getDisplayMessage());
    }

    /* ------------------------- CODE处理区 --------------------------------*/


    protected void onTokenFail(ApiException ex) {
        UIUtils.toast(ex.getDisplayMessage());
    }

    protected void onFailData(ApiException ex) {
        UIUtils.toast(ex.getDisplayMessage());
    }

    /* ------------------------- CODE处理区 end --------------------------------*/
}
