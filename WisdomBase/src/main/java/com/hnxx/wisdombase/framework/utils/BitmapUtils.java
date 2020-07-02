package com.hnxx.wisdombase.framework.utils;

import android.graphics.Bitmap;

import com.hnxx.wisdombase.ui.utils.MyImageUtil;
import com.hnxx.wisdombase.ui.utils.MyImageUtil.IImageLoadListener2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zhoujun
 * @date 2020/1/9 11:11
 */
public class BitmapUtils {
    // 头像下载到本地
    public static void saveImageToSdk(String imageUrl,String desPath,int width,int height) {
        MyImageUtil.getBitmap((IImageLoadListener2) bitmap ->
                saveBitmap(bitmap,desPath),
                imageUrl,
                width,
                height,
                new MyImageUtil.MyImageConfig());
    }

    private static void saveBitmap(Bitmap bm, String desPath) {
        File file = null;

        try {
            file = new File(desPath);
            if (file != null && file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
