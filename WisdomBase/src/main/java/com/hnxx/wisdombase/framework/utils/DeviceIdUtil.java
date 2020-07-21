package com.hnxx.wisdombase.framework.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

import com.hnxx.wisdombase.framework.storage.WisdomPath;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;

/**
 * 获取设备唯一识别码工具
 *
 * @author: zhoujun
 * @date: 2019-10-28 at 19:47
 */
public class DeviceIdUtil {

    /**
     * IMEI
     */
    private static String IMEI = "";
    /**
     * Aandroid Q无法获取imei，使用移动安全联盟获取的唯一识别码（不一定能获取到）
     */
    private static String UDID = "";
    /**
     * WOID APP自己生成的唯一id（这个用户删除wobooks文件夹就会重置）
     */
    private static String WOID = "";

    public static String fileImei = "deviceidimei.txt";
    private static String fileUdid = "deviceidudid.txt";
    private static String fileWoid = "deviceidwoid.txt";
    public static final String UN_KNOWN = "UN_KNOWN";

    /**
     * 获得设备imei
     *
     * @return
     */
    public static String getIMEI(Context context) {
        if (!TextUtils.isEmpty(IMEI)) {
            return IMEI;
        }
        String fromJson = "";
        if (checkPermission(context, PERMISSIONS_STORAGE[0])) {
//            fromJson = readFromFileTxt(fileImei);



        } else {
            return "";
        }
        LogUtil.e("wayne", "getIMEI-fileImei:" + fromJson);
        if (!TextUtils.isEmpty(fromJson)) {
            IMEI = fromJson;
        } else {
            //如果读取文件没有获取到内容，置为unKnown，避免重复读文件
            IMEI = UN_KNOWN;
        }
        return IMEI;
    }

    /**
     * 固定前缀WO + 安卓特定前缀A + 品牌机型预留00000 + 24位随机数字及大写字母组合
     *
     * @param length
     * @return
     */
    private static String getNumWords(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (random.nextInt(2) % 2 == 0) {
                //产生大写字母
                val.append((char) (65 + random.nextInt(26)));
            } else {
                // 产生数字
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        LogUtil.e("wayne", val.toString());
        return val.toString();
    }

    /**
     * 读写权限
     */
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 保存内容到TXT文件中
     *
     * @param content
     * @return
     */
    public static boolean writeToFileTxt(Context context,String fileName, String content) {
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter;
        createDirectory(WisdomPath.instance(context.getApplicationContext()).deviceIds);
        File file = new File(WisdomPath.instance(context.getApplicationContext()).deviceIds, fileName);
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 读取文件内容
     *
     * @return
     */
    private static String readFromFileTxt(String filePath,String fileName) {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath, fileName);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 创建文件夹
     *
     * @param fileDirectory
     */
    public static void createDirectory(String fileDirectory) {
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
