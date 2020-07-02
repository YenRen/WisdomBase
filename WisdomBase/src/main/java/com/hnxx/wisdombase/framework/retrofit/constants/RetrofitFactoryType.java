package com.hnxx.wisdombase.framework.retrofit.constants;

import com.hnxx.wisdombase.framework.retrofit.factroy.DefaultRetrofitFactory;
import com.hnxx.wisdombase.framework.retrofit.factroy.IRetrofitFactory;

/**
 * @author zhoujun
 * @date 2019/12/19
 * Describe:这里可登记着retrof上课it的多个策略
 */
public enum RetrofitFactoryType {
    /**
     * 默认的retorfit配置
     */
    def(DefaultRetrofitFactory.class),
//    video(VideoRetrofitFactory.class)
    ;

    Class<? extends IRetrofitFactory> factoryClz;

    public Class<? extends IRetrofitFactory> getFactoryClz() {
        return factoryClz;
    }

    /**
     * 记录每个枚举对应哪个策略，避免混乱
     *
     * @param factoryClz
     */
    RetrofitFactoryType(Class<? extends IRetrofitFactory> factoryClz) {
        this.factoryClz = factoryClz;
    }
}
