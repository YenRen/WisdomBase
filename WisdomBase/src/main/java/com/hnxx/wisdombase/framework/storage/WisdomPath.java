package com.hnxx.wisdombase.framework.storage;

import android.content.Context;
import android.os.Environment;

import com.hnxx.wisdombase.config.application.AppConfig;
import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.framework.model.StorageBean;
import com.hnxx.wisdombase.framework.utils.StorageUtils;

import java.util.ArrayList;

/**
 * @author zhoujun
 * @date 2019/12/12 14:52
 */
public class WisdomPath {

    /** 设备唯一码目录 */
    public String deviceIds;

    /** 是否存在SD卡 */
    public final boolean isExistSdCard;

    /** 是否存在外置SD卡 */
    public boolean isExistExtSDCard = false;

    /** 系统Root区私有目录 */
    private final String systemRootPath;

    /** 扩展分区目录--可能是内置存储路径，也可能为外置存储路径 */
    public final String extPath;

    /** APP文件根目录 */
    public final String appRootPath;

    /** 缓存目录 */
    private final String cachePath;

    /**外置SD卡存储路径*/
    public final String extSDCardStoragePath;

    /**内置临时存储路径（SD卡创建目录失败时启用）*/
    public final String internalTempStoragePath;

    /** 异常日志目录 */
    public String exceptionLogPath;

    /** 图片缓存 */
    public final String picCachePath;
    /** webview缓存 */
    public final String webviewCachePath;

    private static volatile WisdomPath mInstance;

    public static WisdomPath instance(Context context) {
        if (mInstance == null) {
            synchronized (WisdomPath.class) {
                if (mInstance == null) {
                    mInstance = new WisdomPath(context);
                }
            }
        }
        return mInstance;
    }

    public WisdomPath(Context context){
        // 实例化时一次性全部初始化，提高二次访问性能
        Context ctx = context;

        isExistSdCard = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);

        systemRootPath = ctx.getFilesDir().getParentFile().getPath() + "/";
        String mainPath;
        if (isExistSdCard) {
            extPath = Environment.getExternalStorageDirectory().getPath() + "/";
            mainPath = extPath;
        } else {
            extPath = "";
            mainPath = systemRootPath;
        }
        internalTempStoragePath = mainPath + "Android/data/" + ctx.getPackageName() + "/";
        extSDCardStoragePath = getExtSDCardStoragePath(ctx);
        mainPath = mainPath + "XiangWisdom/";
        appRootPath = mainPath /*+ WoConfiguration.VERSION_TAG + "/"*/;
        cachePath = appRootPath + "Cache/";
        picCachePath = cachePath + "Pic/";
        webviewCachePath = cachePath + "Webview/";
        if (AppConfig.networkEnv == 0) {
            exceptionLogPath = systemRootPath + "ExceptionLog/";
        } else {
            exceptionLogPath = cachePath + "ExceptionLog/";
        }
    }

    private String getExtSDCardStoragePath(Context ctx){
        String extSDCardStoragePathTemp = "";
        ArrayList<StorageBean> storageList = StorageUtils.getStorageData(ctx);
        if(null != storageList){
            boolean hasExtSDCard = false;
            int iSize = storageList.size();
            for(int i = 0; i < iSize; i++){
                StorageBean storageBean = storageList.get(i);
                String path = storageBean.getPath();
                if(!path.contains("usb")){
                    final boolean mounted = "mounted".equalsIgnoreCase(storageBean.getMounted());
                    if(storageBean.getRemovable() && mounted){
                        hasExtSDCard = true;
                        extSDCardStoragePathTemp = path + "/Android/data/" + ctx.getPackageName() + "/";
                        break;
                    }
                }
            }
            isExistExtSDCard = hasExtSDCard;
        }
        return extSDCardStoragePathTemp;
    }
}
