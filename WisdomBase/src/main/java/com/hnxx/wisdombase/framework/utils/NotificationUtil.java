package com.hnxx.wisdombase.framework.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

/**
 * 通知栏帮助类
 *
 * @author maj
 */
public class NotificationUtil {

    private static NotificationUtil mNotificationUtil;

    private NotificationUtil() {
    }

    public static NotificationUtil instance() {
        synchronized (NotificationUtil.class) {
            if (mNotificationUtil == null) {
                synchronized (NotificationUtil.class) {
                    mNotificationUtil = new NotificationUtil();
                }
            }
        }

        return mNotificationUtil;
    }

    @SuppressWarnings("deprecation")
    private Notification generateNotification(Context context, int icon,
                                              String tickerText, String title, String content, Intent intent,
                                              int requestCode, int flags, Boolean autoCancel, Boolean needSound) {
        PendingIntent pendIntent = PendingIntent.getActivity(context,
                requestCode, intent, flags);

        Notification notification = null;
        //7.0系统的大部分手机，没有NotificationCompat调用的setRemoteHistory方法，报错java.lang.NoSuchMethodError: No virtual method setRemoteInputHistory
        //目前通过这种方式解决

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context, "zworeader")
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icon)
                    .setContentIntent(pendIntent)
                    .setTicker(tickerText)
                    .setWhen(System.currentTimeMillis())
                    .build();
        }else {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(icon)
                    .setContentIntent(pendIntent)
                    .setTicker(tickerText)
                    .setWhen(System.currentTimeMillis())
                    .build();
        }

        if (autoCancel) {
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }
        if (needSound) {
            notification.defaults |= Notification.DEFAULT_SOUND;
        }
        return notification;
    }

    /**
     * 简单显示一个Notification
     *
     * @param context
     * @param notifyId     notification的ID，建议用时间戳，而不是一个随便写一个0，1之类的
     * @param iconId
     * @param tickerText   通知栏标题
     * @param contentTitle 通知栏展开标题
     * @param contentText  通知栏展开详细内容
     * @param autoCancel   点击了通知栏之后，是否让通知栏自动消失
     * @param needSound    在弹出通知栏时是否有声音提示
     */
    public void showNotification(Context context, int notifyId, int iconId,
                                 String tickerText, String contentTitle, String contentText,
                                 Boolean autoCancel, Boolean needSound) {
        NotificationManager mNotificationManager = getNotificationManager(context);

        // 定义通知栏展现的内容信息
        Intent notificationIntent = new Intent();
        Notification notification = generateNotification(context, iconId,
                tickerText, contentTitle, contentText, notificationIntent, 0,
                PendingIntent.FLAG_UPDATE_CURRENT, autoCancel, needSound);

        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(notifyId, notification);
    }

    /**
     * 显示一个Notification, 可以绑定intent
     *
     * @param context
     * @param notifyId     notification的ID，建议用时间戳：(int)System.currentTimeMillis();
     * @param tickerText   通知栏标题
     * @param contentTitle 通知栏展开标题
     * @param contentText  通知栏展开详细内容
     * @param intent       定义要跳转的Activity,及要传递的参数
     * @param autoCancel   点击了通知栏之后，是否让通知栏自动消失
     * @param needSound    在弹出通知栏时是否有声音提示
     */
    public void showNotification(Context context, int notifyId, int iconId,
                                 String tickerText, String contentTitle, String contentText,
                                 Intent intent, Boolean autoCancel, Boolean needSound) {
        NotificationManager mNotificationManager = getNotificationManager(context);

        // 定义通知栏展现的内容信息
        Notification notification = generateNotification(context, iconId,
                tickerText, contentTitle, contentText, intent, 0,
                PendingIntent.FLAG_UPDATE_CURRENT, autoCancel, needSound);

        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
        mNotificationManager.notify(notifyId, notification);
    }

    /**
     * 获取NotificationManager
     *
     * @param context
     * @return
     */
    public NotificationManager getNotificationManager(Context context) {

        NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("zworeader", "notification", NotificationManager.IMPORTANCE_LOW);
            mChannel.setSound(null,null);
            mChannel.enableVibration(false);
            mChannel.setVibrationPattern(new long[]{0});
            manager.createNotificationChannel(mChannel);
        }
        return manager;
    }

    /**
     * 显示一个自定义的通知栏
     *
     * @param context      安卓上下文
     * @param notifyId     notification的ID，建议用时间戳：(int)System.currentTimeMillis();
     * @param iconId       图标资源id
     * @param tickerText   通知栏标题
     * @param contentTitle 通知栏展开标题
     * @param contentText  通知栏展开详细内容
     * @param intent       定义要跳转的Activity,及要传递的参数
     * @param autoCancel   点击了通知栏之后，是否让通知栏自动消失
     * @param iRemoteViews 通知栏自定义的样式，及处理过程
     * @param needSound    在弹出通知栏时是否有声音提示
     */
    public void showCustomNotification(Context context, int notifyId,
                                       int iconId, String tickerText, String contentTitle,
                                       String contentText, Intent intent, Boolean autoCancel,
                                       IRemoteViews iRemoteViews, Boolean needSound) {
        NotificationManager manager = getNotificationManager(context);

        Notification notification = generateNotification(context, iconId,
                tickerText, contentTitle, contentText, intent, notifyId,
                PendingIntent.FLAG_UPDATE_CURRENT, autoCancel, needSound);

        // 通知自定义视图
        if (iRemoteViews != null) {
            iRemoteViews.initViews(notification);
        }

        // 发出通知
        manager.notify(notifyId, notification);
    }

    public interface IRemoteViews {

        RemoteViews initViews(Notification notification);
    }
}
