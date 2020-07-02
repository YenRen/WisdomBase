package com.hnxx.wisdombase.framework.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import com.hnxx.wisdombase.config.application.AppConfig;
import com.hnxx.wisdombase.config.application.WisdomApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取手机相关参数信息
 *
 * @author zhoujun
 */
public class PhoneInfoTools {
    private static final String TAG = "PhoneInfoTools";

    private static final String STR_OS_NAME = "Android";
    private static final int INT_IMEI_DEFAULT_LEN = 15;
    public static final String STR_IMEI_DEFAULT_VALUE = "000000000000000";
    public static final String STR_IMSI_DEFAULT_VALUE = "000000000000000";
    public static final String STR_ANDROID_ID_DEFAULT_VALUE = "000000000000000";
    public static final String STR_MAC_ADDR_DEFAULT_VALUE = "000000000000";

    /**
     * 版本号
     */
    private static int mVersionCode = 0;

    /**
     * 统计版本名
     */
    private static String mStatVersionName = "";

    /**
     * 版本名
     */
    private static String mVersionName = "";

    /**
     * 客户端ID(客户端编号+版本名)
     */
    private static String mClientAllId = "";

    /**
     * 灰度版本号
     */
    private static String mBetaVersion = "";

    /**
     * 渠道号
     */
    private static String mChannelId = "";

    /**
     * 手机号码(本地取号)
     */
    private static String mPhoneNum = "";

    /**
     * IMSI
     */
    private static String IMSI = "";

    /**
     * IMEI
     */
    private static String IMEI = "";

    public static void setIP(String IP) {
        PhoneInfoTools.IP = IP;
    }

    /**
     * IP
     */
    private static String IP = "";

    /**
     * Android ID
     */
    private static String mAndroidId = "";


    public static final String getOsVersion(boolean isUrl) {
        String osVersion = "";
        if (isUrl) {
            try {
                osVersion = URLEncoder.encode(STR_OS_NAME + Build.VERSION.RELEASE, "utf-8");
            } catch (UnsupportedEncodingException e) {
                osVersion = STR_OS_NAME;
            }
        } else {
            osVersion = STR_OS_NAME + Build.VERSION.RELEASE;
        }
        return osVersion;
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public static final int getVersionCode(Context context) {
        if (mVersionCode < 1) {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            try {
                PackageInfo info = manager.getPackageInfo(packageName, 0);
                mVersionCode = info.versionCode;
            } catch (NameNotFoundException e) {
                mVersionCode = 1;
            }
        }

        return mVersionCode;
    }

    /**
     * 获取统计版本名
     *
     * @param context
     * @return
     */
    public static final String getStatVersionName(Context context) {
        if (TextUtils.isEmpty(mStatVersionName)) {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            try {
                PackageInfo info = manager.getPackageInfo(packageName, 0);
                mStatVersionName = info.versionName + "."
                        + AppConfig.STR_SUB_VERSION_CODE;
            } catch (NameNotFoundException e) {
                mStatVersionName = "1.0.0.0101";
            }
        }
        return mStatVersionName;
    }

    /**
     * 获取版本名
     *
     * @param context
     * @return
     */
    public static final String getVersionName(Context context) {
        if (TextUtils.isEmpty(mVersionName)) {
            PackageManager manager = context.getPackageManager();
            String packageName = context.getPackageName();
            try {
                PackageInfo info = manager.getPackageInfo(packageName, 0);
                mVersionName = info.versionName;
            } catch (NameNotFoundException e) {
                mVersionName = "1.0.0";
            }
        }
        return mVersionName;
    }

    /**
     * 客户端ID+版本名称
     *
     * @param context
     * @return
     */
    public static final String getClientAllId(Context context) {
        if (TextUtils.isEmpty(mClientAllId)) {
            String versionName = getStatVersionName(context);
            mClientAllId = "00000010000000000001" + versionName;
        }

        return mClientAllId;
    }

    /**
     * 获取灰度版本号
     *
     * @param context
     * @return 实际的或者为“”
     */
    public static final String getBateversion(Context context) {
        if (TextUtils.isEmpty(mBetaVersion)) {
            String betaversion = getChannelMetaData(context, "betaversion");
            if (TextUtils.isEmpty(betaversion)) {
                mBetaVersion = "";
            } else {
                mBetaVersion = betaversion;
            }
        }

        return mBetaVersion;
    }

    /**
     * 美团打包获取渠道号
     *
     * @param context
     * @return
     */
//    public static final String getChannelByWalle(Context context) {
//        String channel = WalleChannelReader.getChannel(context);
//        if (TextUtils.isEmpty(channel)) {
//            channel = STR_CHANNEL_ID_BY_WALLE;
//        }
//        Log.d("Channel", "baidu Channel = " + channel);
//        return channel;
//    }

    /**
     * 获取客户端渠道号，首先从sp里取，若sp未保存，则再从manifest中读取
     *
     * @param context
     * @return
     */
//    public static final String getChannelMetadataValue(Context context) {
//        if (TextUtils.isEmpty(mChannelId)) {
//            try {
//                SharedPreferences sp = context.getApplicationContext().getSharedPreferences("ZWelcomeActivity",
//                        Context.MODE_PRIVATE);
//                String code = sp.getString("ChannelCode", "");
//                if (TextUtils.isEmpty(code)) {
//                    LauncherSp launcherSp = new LauncherSp();
//                    code = launcherSp.getChannelCode();
//                    if (TextUtils.isEmpty(code)) {
////                        code = getChannelMetaData(context, "myMsg");
//                        code = WalleChannelReader.getChannel(context);
//                    }
//                }
//                if (TextUtils.isEmpty(code)) {
//                    mChannelId = STR_CHANNEL_ID_BY_WALLE;
//                } else {
//                    mChannelId = code;
//                }
//            } catch (Exception e) {
//                mChannelId = STR_CHANNEL_ID_BY_WALLE;
//            }
//        }
//        Log.d("Channel", "Channel == " + mChannelId);
//        return mChannelId;
//    }



    /**
     * 获取渠道信息
     * 不要使用该方法获取myMsg，使用#getChannelMetadataValue
     *
     * @param context
     * @param key
     * @return
     */
    public static String getChannelMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 获取手机号(本地取号)
     *
     * @param context
     * @return 能获取到则返回11位手机号，否则返回""
     */
    public static final String getPhoneNum(Context context) {
        try {
            if (TextUtils.isEmpty(mPhoneNum)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String tel = tm.getLine1Number();
                if (!TextUtils.isEmpty(tel)) {
                    tel = tel.trim();
                    if (tel.length() > 11) {
                        tel = tel.substring(tel.length() - 11);
                    }
                    if (isMobilePhone(tel)) {
                        mPhoneNum = tel;
                    } else {
                        mPhoneNum = "";
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return mPhoneNum;
    }


    /**
     * 获取IMSI
     *
     * @param context
     * @return 获取不到返回默认值
     */
    public static final String getIMSI(Context context) {
        return getIMSI(context, false);
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @param
     * @return 获取不到返回默认值
     */
    public static final String getIMSI(Context context, boolean refresh) {
        try {
            if (refresh || TextUtils.isEmpty(IMSI)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String imsi = tm.getSubscriberId();
                if (TextUtils.isEmpty(imsi)) {
                    IMSI = STR_IMSI_DEFAULT_VALUE;
                } else {
                    IMSI = imsi;
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return IMSI;
    }

    /**
     * 通过网络获取IMSI
     *
     * @return
     */
    public static final String getIMSIByNet() {
        try {
            TelephonyManager telMag = (TelephonyManager) UIUtils.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (telMag == null) {
                return STR_IMSI_DEFAULT_VALUE;
            }
            String simOperator = telMag.getSimOperator();
            if (TextUtils.isEmpty(simOperator)) {
                return STR_IMSI_DEFAULT_VALUE;
            }
            return simOperator;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return STR_IMSI_DEFAULT_VALUE;
    }

    /**
     * 获取IMEI
     *
     * @param context
     * @return 获取不到返回默认值
     */
    public static final String getIMEI(Context context) {
        try {
            if (TextUtils.isEmpty(IMEI)) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                String imei = tm.getDeviceId();
                // 应服务端要求，IMEI要保证长度为15位
                if (!TextUtils.isEmpty(imei)) {
                    imei = imei.trim() + STR_IMEI_DEFAULT_VALUE;
                    imei = imei.substring(0, INT_IMEI_DEFAULT_LEN);
                    DeviceIdUtil.writeToFileTxt(DeviceIdUtil.fileImei, imei);
                }

                if (TextUtils.isEmpty(imei)) {
                    IMEI = STR_IMEI_DEFAULT_VALUE;
                } else {
                    IMEI = imei;
                }
            }
        } catch (SecurityException e) {
            //AndroidQ将彻底禁止第三方应用获取设备的imei序列号,但是移动安全联盟说部分AndroidQ过渡期仍可获取IMEI，因此获取本地存储还是放到这里来吧
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
                //AndroidQ获取之前文件夹保存的imei
                String imei = DeviceIdUtil.getIMEI(context);
                if (TextUtils.equals(DeviceIdUtil.UN_KNOWN, imei)){
                    IMEI = STR_IMEI_DEFAULT_VALUE;
                } else {
                    IMEI = imei;
                }
            }
            e.printStackTrace();
        }
        //Aandroid Q无法获取imei，使用移动安全联盟获取的唯一识别码（不一定能获取到）,统一使用主平台获取方式
        return IMEI;
    }

    public static String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    public static String getIP(Context ctx) {
        if (!TextUtils.isEmpty(IP)) {
            return IP;
        }
        StringBuilder IPStringBuilder = new StringBuilder();
        try {
            String name = ConnectNetName(ctx);
            if (TextUtils.equals(name, "wifi")) {
                // 获取wifi服务
                WifiManager wifiManager = (WifiManager) ctx
                        .getSystemService(Context.WIFI_SERVICE);
                // 判断wifi是否开启
                if (wifiManager.isWifiEnabled()) {
                    //          wifiManager.setWifiEnabled(true);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    int ipAddress = wifiInfo.getIpAddress();
                    IP = intToIp(ipAddress);
                    return IP;
                }
            } else {
                Enumeration<NetworkInterface> networkInterfaceEnumeration =
                        NetworkInterface.getNetworkInterfaces();
                while (networkInterfaceEnumeration.hasMoreElements()) {
                    NetworkInterface networkInterface =
                            networkInterfaceEnumeration.nextElement();
                    Enumeration<InetAddress> inetAddressEnumeration =
                            networkInterface.getInetAddresses();
                    while (inetAddressEnumeration.hasMoreElements()) {
                        InetAddress inetAddress =
                                inetAddressEnumeration.nextElement();
                        if (!inetAddress.isLoopbackAddress() &&
                                !inetAddress.isLinkLocalAddress() &&
                                inetAddress.isSiteLocalAddress()) {
                            IPStringBuilder.append(inetAddress.getHostAddress());
                            IP = IPStringBuilder.toString();
                            return IP;
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            return "127.0.0.1";
        }

        IP = IPStringBuilder.toString();
        return IP;
    }

    /**
     * 网络连接是否为wifi
     *
     * @param _Context
     * @return
     */
    public static boolean isWifiConnection(Context _Context) {
        return "wifi".equals(ConnectNetName(_Context));
    }

    /**
     * 获取网络接入方式
     *
     * @param _Context
     * @return
     */
    public static final String ConnectNetName(Context _Context) {
        String connectnetname = "nonetwork";
        ConnectivityManager conMan = (ConnectivityManager) _Context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = conMan.getActiveNetworkInfo();
        // wifi
        if (null != info && info.getTypeName().toLowerCase().contains("wifi")) {
            return connectnetname = "wifi";
        }

        // 手机网络
        if (null != info && null != info.getExtraInfo()) {
            String defaultAPN = info.getExtraInfo().toLowerCase();
            if (defaultAPN.contains("cmwap")) {
                connectnetname = "cmwap";
            } else if (defaultAPN.contains("cmnet")) {
                connectnetname = "cmnet";
            } else if (defaultAPN.contains("3gwap")) {
                connectnetname = "3gwap";
            } else if (defaultAPN.contains("3gnet")) {
                connectnetname = "3gnet";
            } else if (defaultAPN.contains("uniwap")) {
                connectnetname = "uniwap";
            } else if (defaultAPN.contains("uninet")) {
                connectnetname = "uninet";
            } else if (defaultAPN.contains("ctwap")) {
                connectnetname = "ctwap";
            } else if (defaultAPN.contains("ctnet")) {
                connectnetname = "ctnet";
            } else {
                connectnetname = "unknown";
            }
        }

        return connectnetname;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = WisdomApplication.Instance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = WisdomApplication.Instance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    public static String getDisplayMetrics(Context cx) {
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            return getRealHeight(cx) + "_" + getWidth(cx);
        } else {
            return getHeight(cx) + "_" + getWidth(cx);
        }
    }

    @TargetApi(17)
    public static int getRealHeight(Context cx) {
        try {
            WindowManager wm = (WindowManager) cx.getSystemService(Context.WINDOW_SERVICE);
            if (null != wm) {
                // 获取屏幕高和宽(包含虚拟按键，即手机屏幕真实的高和宽)
                Display display = wm.getDefaultDisplay();
                Point outSize = new Point();
                display.getRealSize(outSize);
                return outSize.y;
            } else {
                return getHeight(cx);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getHeight(cx);
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param cx
     * @return
     */
    public static final int getWidth(Context cx) {
        // DisplayMetrics dm = new DisplayMetrics();
        DisplayMetrics dm = WisdomApplication.Instance().getResources().getDisplayMetrics();

        int screenWidth = dm.widthPixels;

        return screenWidth;

    }

    public static final int getwidthdip(Context cx) {

        int pxwidth = getWidth(cx);
        int dipwidth = px2dip(cx, pxwidth);
        return dipwidth;
    }

    /**
     * 获取屏幕高度
     * 获取屏幕高和宽(不包含虚拟按键)
     *
     * @param cx
     * @return
     */
    public static int getHeight(Context cx) {
        // 获取屏幕高和宽(不包含虚拟按键)
        DisplayMetrics dm = WisdomApplication.Instance().getResources().getDisplayMetrics();

        int screenHeight = dm.heightPixels;

        return screenHeight;

    }

    /**
     * 获取系统状态栏高度
     *
     * @param cx
     * @return
     */
    public static int getStatusBasrHeight(Activity cx) {
        if (cx == null) {
            return 0;
        }
        Rect frame = new Rect();
        cx.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        return frame.top;
    }

    public static int getStatusBarHeightByRes(Context cx) {
        int result = 0;
        int resourceId = cx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = cx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取ua
     *
     * @param cx
     * @return
     */
    public static final String getUA(Context cx) {
        String userAgent = Build.MODEL;
        return userAgent.trim();
    }

    /**
     * 中国联通的2G业务WAP浏览器中使用的APN为“UNIWAP”， 3G业务WAP浏览器使用的APN为“3GWAP”；
     * 中国联通的2G上公网使用的APN为“UNINET”， 3G业务上网卡及上公网使用的APN为“3GNET“。
     * 中国移动上内网的APN为“CMWAP“， 上网卡及上公网使用的APN为“CMNET“
     *
     * @param context
     * @return
     */
    public static String getAPN(Context context) {
        String apn = "";
        // 通过context得到ConnectivityManager连接管理
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 通过ConnectivityManager得到NetworkInfo网络信息
        NetworkInfo info = manager.getActiveNetworkInfo();
        // 获取NetworkInfo中的apn信息
        if (info != null) {
            apn = info.getExtraInfo();
            if (apn == null) {

                // apn = UIResource.MOBILE_NONETWORK;
                apn = "手机未取到网络信息";
            }
        } else {
            // apn = UIResource.MOBILE_NONETWORK;
            apn = "手机未取到网络信息";
        }
        return apn;
    }

    public static final String getSdkVersion() {

        return android.os.Build.VERSION.RELEASE;
    }

    public static final String getAndroidSdkVersion() {

        int i = (android.os.Build.VERSION.RELEASE).indexOf(".");

        return "Android_" + (android.os.Build.VERSION.RELEASE).substring(0, i + 2);
    }

    /**
     * 是否存在sd卡
     *
     * @return
     */
    public static final boolean isExistSdCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

    }

    /**
     * 获取系统当前时间
     *
     * @return
     */
    public static String getSystemTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return year + "." + month + "." + day + "." + hour + "." + minute;
    }

    /**
     * 格式化时间
     *
     * @param format 格式化显示样式
     * @param date   时间
     * @return
     */
    public static String format(String format, Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);

    }

    /**
     * 格式化时间HH:MM:SS
     *
     * @param _ms 时间 ms
     * @return
     */
    public static String ms2HMS(int _ms) {
        String HMStime;
        _ms /= 1000;
        int hour = _ms / 3600;
        int mint = (_ms % 3600) / 60;
        int sed = _ms % 60;
        String hourStr = String.valueOf(hour);
        if (hour < 10) {
            hourStr = "0" + hourStr;
        }
        String mintStr = String.valueOf(mint);
        if (mint < 10) {
            mintStr = "0" + mintStr;
        }
        String sedStr = String.valueOf(sed);
        if (sed < 10) {
            sedStr = "0" + sedStr;
        }
        HMStime = hourStr + ":" + mintStr + ":" + sedStr;
        return HMStime;
    }


    /**
     * 列表载入时动画的公用controller
     *
     * @return
     */
    public static LayoutAnimationController getListAnimationController() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
    }


    /**
     * 检查网络状态
     */
    public static final boolean checkNetworkInfo(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // mobile 3G Data Network
        State mobile = null;
        if (null != conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)) {
            mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        }

        // wifi
        State wifi = null;
        if (null != conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)) {
            wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        }

        // 如果3G网络和wifi网络都未连接，且不是处于正在连接状态 则进入Network Setting界面 由用户配置网络连接
        if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
            return true;
        } else if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取加密的手机号码
     *
     * @param number
     */
    public static String getEncrypPhoneNumber(String number) {
        if (number.length() == 11) {
            StringBuffer sb = new StringBuffer();
            sb.append(number, 0, 3);
            sb.append("XXXX");
            sb.append(number, 7, number.length());
            return sb.toString();
        } else {
            return "0";
        }
    }


    /**
     * 返回系统AndroidID
     *
     * @param context
     * @return
     */
    public static final String getAndroidID(Context context) {
        if (TextUtils.isEmpty(mAndroidId)) {
            String androidID = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            if (TextUtils.isEmpty(androidID)) {
                mAndroidId = STR_ANDROID_ID_DEFAULT_VALUE;
            } else {
                mAndroidId = androidID;
            }
        }

        return mAndroidId;
    }

    /**
     * 返回WifiMac(注意：当Wifi未曾启动前，取不到Mac)
     */
    public static final String getWifiMac(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress() == null ? STR_MAC_ADDR_DEFAULT_VALUE : info.getMacAddress();
    }

    public static void setmChannelId(String mChannelId) {
        PhoneInfoTools.mChannelId = mChannelId;
    }

    /**
     * 判断是否为疑似手机号   此判断是不全面的**
     *
     * @param phoneNumber
     * @return
     */
    static Pattern pattern1 = Pattern.compile("^1[0-9]{10}");

    public static boolean isMobilePhone(String phoneNumber) {
        boolean isMobile = false;
        if (TextUtils.isEmpty(phoneNumber)) {
            return isMobile;
        }
        if (phoneNumber.startsWith("10")
                || phoneNumber.startsWith("11")
                || phoneNumber.startsWith("12")) {
            return isMobile;
        }
        Matcher matcher1 = pattern1.matcher(phoneNumber);
        if (matcher1.matches()) {
            isMobile = true; // 手机号码格式不正确
        }

        return isMobile;
    }

    /**
     * 隐藏手机号码中间四位
     *
     * @param phoneNum
     * @return
     */
    public static String phoneNumerParserFourStar(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum) && isMobilePhone(phoneNum)) {
            String phoneNumber = phoneNum.substring(0, 3) + "****" + phoneNum.substring(7);
            return phoneNumber;
        }
        return phoneNum;
    }

    /**
     * 获取随机的UUID，去除破折号(-)
     */
    public static String getUuidWithoutDash() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    //显示虚拟键盘
    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        String mode = android.os.Build.MODEL;
        if (TextUtils.isEmpty(mode) || "unknow".equalsIgnoreCase(mode)) {
            mode = Build.HARDWARE;
        }

        return mode;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        String brand = android.os.Build.BRAND;
        if (TextUtils.isEmpty(brand) || "unknow".equalsIgnoreCase(brand)) {
            brand = android.os.Build.MANUFACTURER;
        }

        return brand;
    }


    // 获得可用的内存
    public static long getMemoryUnUsed(Context mContext) {
        long MEM_UNUSED;
        // 得到ActivityManager
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        // 创建ActivityManager.MemoryInfo对象
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);

        // 取得剩余的内存空间，返回的内存大小单位是MB
        MEM_UNUSED = mi.availMem;

        MEM_UNUSED = MEM_UNUSED / 1024 / 1024;

        return MEM_UNUSED;
    }

    // 获得总内存
    public static long getmem_TOLAL() {
        long mTotal;
        // /proc/meminfo读出的内核信息进行解释
        String path = "/proc/meminfo";
        String content = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(path), 8);
            String line;
            if ((line = br.readLine()) != null) {
                content = line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // beginIndex
        int begin = content.indexOf(':');
        // endIndex
        int end = content.indexOf('k');
        // 截取字符串信息

        content = content.substring(begin + 1, end).trim();
        mTotal = Integer.parseInt(content);
        return mTotal;
    }

    /**
     * 跳转到权限设置界面，
     *
     * @param activity
     */
    public static void go2PermissionSetting(Context activity) {
        if (null == activity) {
            return;
        }
        PermissionPageUtils permissionPageUtils = new PermissionPageUtils(activity);
        permissionPageUtils.jumpPermissionPage();
    }

    /***
     * 是否连接了蓝牙
     */
    public static boolean getCurrentBluetoothDevice() {
        boolean isConn = false;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Class<BluetoothAdapter> bluetoothAdapterClass = BluetoothAdapter.class;//得到BluetoothAdapter的Class对象
        try {//得到连接状态的方法
            Method method = bluetoothAdapterClass.getDeclaredMethod("getConnectionState", (Class[]) null);
            //打开权限
            method.setAccessible(true);
            int state = (int) method.invoke(adapter, (Object[]) null);

            if (state == BluetoothAdapter.STATE_CONNECTED) {
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                for (BluetoothDevice device : devices) {
                    Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                    method.setAccessible(true);
                    boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                    if (isConnected) {
                        isConn = true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isConn;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 设备是否root
     * @return
     */
    public static boolean isDeviceRooted() {
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath))
            return true;
        if (new File(xBinPath).exists() && isExecutable(xBinPath))
            return true;
        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();
            Log.i(TAG, str);
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return false;
    }


    /**
     * 无运营商
     */
    public static final int OPERATORS_UNKNOWN = 0;
    /**
     * 运营商：联通
     */
    public static final int OPERATORS_UNICOM = 1;
    /**
     * 运营商：移动
     */
    public static final int OPERATORS_MOBILE = 2;
    /**
     * 运营商：电信
     */
    public static final int OPERATORS_TELECOM = 3;

    /**
     * 获取运营商
     *
     * @return
     */
    public static int getOperators() {
        String imsi;
        //官方文档只支持5.1及其之后的系统提供双卡API。对于之前的系统版本，目前的市场占比已经很少了。采用的策略是放弃不支持的机器，因为这边对精度要求非常高，可以判断不出来运营商，但不能判断出错。
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            imsi = PhoneInfoTools.getIMSIByNet();
        } else {
            imsi = PhoneInfoTools.getIMSI(UIUtils.getContext(), true);
        }
        // 移动设备网络代码（英语：Mobile Network Code，MNC）是与移动设备国家代码（Mobile Country Code，MCC）（也称为“MCC /
        // MNC”）相结合, 例如46000，前三位是MCC，后两位是MNC 获取手机服务商信息
        // IMSI号前面3位460是国家，紧接着后面2位00 运营商代码
        if (TextUtils.isEmpty(imsi)) {
            return OPERATORS_UNKNOWN;
        }
        if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            return OPERATORS_MOBILE;
            //46006是联通物联网的，不能登录
        } else if (imsi.startsWith("46001") || imsi.startsWith("46006") || imsi.startsWith("46009")) {
            return OPERATORS_UNICOM;
        } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
            return OPERATORS_TELECOM;
        }
        return OPERATORS_UNKNOWN;
    }

    /***
     * 获取当前的网络状态
     *
     * 没有网络0：WIFI网络1：3G网络2：2G网络3
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }

    /**
     * 判断一个Activity是否正在运行
     *
     * @param pkg  pkg为应用包名
     * @param cls  cls为类名eg
     * @param context
     * @return
     */
    public static boolean isClsRunning(Context context, String pkg, String cls) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        ActivityManager.RunningTaskInfo task = tasks.get(0);
        if (task != null) {
            return TextUtils.equals(task.topActivity.getPackageName(), pkg) &&
                    TextUtils.equals(task.topActivity.getClassName(), cls);
        }
        return false;
    }

}
