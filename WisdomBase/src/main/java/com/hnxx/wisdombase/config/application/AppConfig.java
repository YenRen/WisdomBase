package com.hnxx.wisdombase.config.application;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.hnxx.wisdombase.BuildConfig;
import com.hnxx.wisdombase.framework.api.NetPlatCorrespond;

/**
 * @author zhoujun
 * @date 2019/12/12 10:55
 */
public class AppConfig {
    // 远程服务器网络 (0.生产环境 1.外网测试环境 2.内网测试环境)
    public static int networkEnv = BuildConfig.API_ENV ?
            NetPlatCorrespond.NET_ENV_RELESE : NetPlatCorrespond.NET_ENV_INNER;

    // 现场版本是否开启调试日志
    public static boolean ISSHOWLOG = !BuildConfig.API_ENV;

    public final static String STR_SUB_VERSION_CODE = BuildConfig.BUILD_DATE;

    public static String WISDOM_XX_ADDRESS;
    public static String WISDOM_XX_ADDRESS_NEWS;

    static {
        if (0 == networkEnv) {
            WISDOM_XX_ADDRESS = "";
        } else {
            WISDOM_XX_ADDRESS = "http://192.168.0.115:8081/";
            WISDOM_XX_ADDRESS_NEWS = "http://192.168.0.177:8081/";
        }
    }


    /**
     * 判断手机是否有可用的网络连接.
     *
     * @param context
     * @return true, if is network available
     * @author XieZhuoxun
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            return false;
        } else {
            // 如果仅仅是用来判断网络连接，则可以使用 cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo item : info) {
                    if (item != null && item.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
