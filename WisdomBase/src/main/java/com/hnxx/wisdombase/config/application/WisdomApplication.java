package com.hnxx.wisdombase.config.application;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhoujun
 * @date 2019/12/12 11:15
 */
public class WisdomApplication extends BaseApp{
    private static final String TAG = "XxWiApplication";
    private ZlActivityLifecycleCallbacks mZlActivityLifecycleCallbacks;
    private long startTime;
    private volatile static WisdomApplication wisdomApplication;
    public static WisdomApplication Instance() {
        if (wisdomApplication == null) {
            synchronized (WisdomApplication.class) {
                if (wisdomApplication == null) {
                    wisdomApplication = new WisdomApplication();
                }
            }
        }
        return wisdomApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        wisdomApplication = this;
        startTime = System.currentTimeMillis();
        String packageName = getPackageName();
        String processName = PhoneInfoTools.getProcessName(android.os.Process.myPid());


        if (packageName.equals(processName)
                || (packageName + ":message").equals(processName)) {
//            ThreadManager.getLongPool().execute(() -> {
//                //加密数据库尽早初始化
//                DBHelper.getInstance().getReadableDatabase();
//            });
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .methodOffset(7)
                    .tag("WisdomXx")
                    .build();

            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
                @Override
                public boolean isLoggable(int priority, String tag) {
                    return AppConfig.ISSHOWLOG;
                }
            });
            printDuring("SQLiteDatabase、WisdomConfiguration");
        }

        if (packageName.equals(processName)) {
            mZlActivityLifecycleCallbacks = new ZlActivityLifecycleCallbacks();
            registerActivityLifecycleCallbacks(mZlActivityLifecycleCallbacks);

            ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                    .setDownsampleEnabled(true)
                    .setBitmapsConfig(Bitmap.Config.RGB_565)
                    .build();
            Fresco.initialize(getApplicationContext(), config);
        }

    }

    public ZlActivityLifecycleCallbacks getZlActivityLifecycleCallbacks() {
        return mZlActivityLifecycleCallbacks;
    }

    /**
     * 启动项耗时
     *
     * @param name
     */
    private void printDuring(String name) {
        long endTime = System.currentTimeMillis();
        LogUtil.d(TAG, "启动" + name + "during: " + (endTime - startTime));
        startTime = endTime;
    }

    // The activity is destroyed.
    private List<Activity> activityList = new LinkedList<Activity>();

    // 添加Activity 到list中
    public void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    // we must remove the activity when it destroy
    public boolean removeActivity(Activity activity) {
        return activityList.remove(activity);
    }

    public List<Activity> getActivitys() {
        return (new LinkedList<Activity>(activityList));
    }

    public Activity getTopActivity() {
        if (null != activityList && activityList.size() > 0) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }

    public Context getContext() {
        return this.getApplicationContext();
    }


    private List<Service> serviceList = new LinkedList<>();
    // 添加Service 到list中
    public void addService(Service service) {
        serviceList.add(service);
    }

    // 退出时调用此方法stop所有的Service
    public void clearAllService() {
        for (Service service : serviceList) {
            service.stopSelf();
        }
    }
    /**
     * 退出进程 code: 0 为正常退出 ； 1 异常退出
     *
     * @param code c
     */
    public void exitApp(int code) {
        clearAllService();
        List<Activity> activities = getActivitys();
        for (Activity activity : activities) {
            activity.finish();
        }
        System.exit(code);
    }

}
