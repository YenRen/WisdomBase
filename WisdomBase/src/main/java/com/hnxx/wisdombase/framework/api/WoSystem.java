package com.hnxx.wisdombase.framework.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.StatFs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class WoSystem {

	private static WoSystem myWoSystem;

	/** 单例 */
	public static WoSystem instance() {
		if (null == myWoSystem) {
			myWoSystem = new WoSystem();
		}
		return myWoSystem;
	}

	public static final int SUCCESS = 1;
	public static final int FAILD = 0;
	// 是否root
	public static boolean ISROOT = false;

	// 手机屏幕宽度
	public static int SCREEN_WIDTH = 480;
	// 手机屏幕高度
	public static int SCREEN_HEIGHT = 800;
	// 广告图片高度..
	public static int SCREEN_BANNER_HEIGHT = 100;
	// 图标宽
	public static int SCREEN_ICON_WIDTH = 115;
	// 图标宽_滑动
	public static int SCREEN_ICON_WIDTH_SLIP = 80;
	// 图标高_滑动
	public static int SCREEN_ICON_HEIGHT_SLIP = 100;
	// 滑动_最大值
	public static int SCREEN_MAXVALUE = 10000;
	// 图标高_滑动_
	public static int SCREEN_HEIGHT_SLIP = 50;
	// 图标高
	public static int SCREEN_ICON_HEIGHT = 160;
	// 图标宽
	public static int SCREEN_ICON_WIDTH_CAT = 100;
	// 图标高
	public static int SCREEN_ICON_HEIGHT_CAT = 100;
	// 图标宽
	public static int SCREEN_ICON_WIDTH_SORT = 70;
	// 图标高
	public static int SCREEN_ICON_HEIGHT_SORT = 70;

	public static int SDCAED_SIZE_MIN_VAL = 10; // sd卡小于此值就不能下载
	// 电池电量百分比
	public static int BATTERY_PERCENT = 50;
	// 屏幕密度
	public static float DENSITY = 1.0f;
	
	//屏幕秘密啊Dpi
	public static int DENSITYDPI=120;

	// 判断SD卡剩余容量大小
	public static boolean isAvaiableSpace(int sizeMb) {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			// File file = new File(sdcard);
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);
			// long availableSpare = (long)
			// (statFs.getBlockSize()*((long)statFs.getAvailableBlocks()-4))/(1024*1024);//以比特计算
			// 换算成MB
            return sizeMb <= availableSpare;
		}
		return false;
	}

	// 读取Assets目录下文件内容
	public static String readAssetsFileString(Context context, String fielName) {
		String str = null;
		try {
			InputStream is = context.getAssets().open(fielName);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			str = new String(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return str;
	}

	
	/**
	 * 将Assets目录文件拷贝到其他位置
	 * @param context
	 * @param assetsSrc assets目录下相对路径
	 * @param des 拷贝到的路径
	 * @return
	 */
	public static boolean copyAssetsToFilesystem(Context context, String assetsSrc, String des) {
		InputStream istream = null;
		OutputStream ostream = null;
		try{
			AssetManager am = context.getAssets();
			istream = am.open(assetsSrc);
			ostream = new FileOutputStream(des);
			byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = istream.read(buffer))>0){
	    		ostream.write(buffer, 0, length);
	    	}
	    	istream.close();
	    	ostream.close();
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				if(istream!=null) {
                    istream.close();
                }
				if(ostream!=null) {
                    ostream.close();
                }
			}
			catch(Exception ee){
				ee.printStackTrace();
			}
			return false;
		}
		return true;
	}
	

}
