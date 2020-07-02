package com.hnxx.wisdombase.framework.api;

/**
 * Created by zhoujun on 2019/4/15.
 */
public interface NetPlatCorrespond {
    // 远程服务器网络 (0.生产环境 1.外网测试环境 2.内网测试环境)
    int NET_ENV_RELESE =  0;
    int NET_ENV_OUT = 1;
    int NET_ENV_INNER = 2;
}
