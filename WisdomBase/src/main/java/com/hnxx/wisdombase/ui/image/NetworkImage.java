
package com.hnxx.wisdombase.ui.image;

import android.content.Context;

import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.ui.utils.CacheUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class NetworkImage extends ZLSingleImage {

	public static final String MIME_PNG = "image/png";
	public static final String MIME_JPEG = "image/jpeg";

	public final String Url;
	
	private boolean isCache;
	
	private volatile boolean mySynchronized;
	private static final int mCacheValidTime = 2592000;//30 * 24 * 60 * 60 缓存时间一个月，单位秒；为0时表示永久缓存;
	private Context context;
	// mimeType string MUST be interned
	public NetworkImage(Context context,String url, String mimeType) {
		super(mimeType);
		Url = url;
		this.context = context;
		checkFileIsExist(context);
		
	}
	public NetworkImage(String url, String mimeType,boolean isCache) {
		super(mimeType);
		
		this.isCache = isCache;
		Url = url;
		checkFileIsExist(context);
		
		
	}
    
	public void clearCache(Context context) {
        this.mySynchronized = false;
        final File imageFile = new File(getFileName(context));
        if (imageFile.exists()) {
            imageFile.delete();
        }
    }
    
	private static final String TOESCAPE = "<>:\"|?*\\";

	// mimeType string MUST be interned
	public  String makeImageFileName(Context context,String url, String mimeType) {
	    return CacheUtil.getCacheFileName(context,url, mimeType, "_");
	}

	public String getFileName(Context context) {
		return makeImageFileName(context,Url, mimeType());
	}
	
	public boolean isSynchronized() {
		return mySynchronized;
	}

	public void synchronize(Context context) {
		synchronizeInternal(context,false);
	}

	public void synchronizeFast(Context context) {
		synchronizeInternal(context,true);
	}

	private final void synchronizeInternal(Context context,boolean doFast) {
		if (mySynchronized) {
			return;
		}
		try {
			final String fileName = getFileName(context);
			if (fileName == null) {
				// TODO: error message ???
				return;
			}
			final int index = fileName.lastIndexOf(File.separator);
			
			//
			if (index != -1) {
				final String dir = fileName.substring(0, index);
				final File dirFile = new File(dir);
				
				
				if (!dirFile.exists() && !dirFile.mkdirs()) {
					return;
				}
				if (!dirFile.exists() || !dirFile.isDirectory()) {
					return;
				}
			}
			
			final File imageFile = new File(fileName);
			
			if (imageFile.exists()) {
				
				final long diff = (System.currentTimeMillis() - imageFile.lastModified()) / 1000;
				final long valid = mCacheValidTime;
				if (0 == valid || (diff >= 0 && diff <= valid)) {
					return;
				} else {
				    imageFile.delete();
				}
			}
			if (doFast) {
				return;
			}
			ZLNetworkManager.Instance().downloadToFile(Url, imageFile);
		} finally {
			mySynchronized = true;
		}
	}
	
	private final void checkFileIsExist(Context context){
		
		final String fileName = getFileName(context);
		if (fileName == null) {
			// TODO: error message ???
			return;
		}
		final int index = fileName.lastIndexOf(File.separator);
		
		//
		if (index != -1) {
			final String dir = fileName.substring(0, index);
			final File dirFile = new File(dir);
			
			//判断文件夹是否存在并创建
			if (!dirFile.exists() && !dirFile.mkdirs()) {
				return;
			}
			if (!dirFile.exists() || !dirFile.isDirectory()) {
				return;
			}
		}
		
		final File imageFile = new File(fileName);
		
		if (imageFile.exists()) {
			//判断缓存文件超过一个月则删除
		    final long diff = (System.currentTimeMillis() - imageFile.lastModified()) / 1000;
            final long valid = mCacheValidTime;
			if (0 == valid || (diff >= 0 && diff <= valid)) {
				
				mySynchronized = true;
				
			} else {
			    imageFile.delete();
			}
		}
		
	}
	
	

	@Override
	public byte [] byteData() {
		
		if (!mySynchronized) {
			return null;
		}
		final String fileName = getFileName(context);
		if (fileName == null) {
			return null;
		}
		final File imageFile = new File(fileName);
		if (!imageFile.exists()) {
			return null;
		}
		try {
			final byte[] data = new byte[(int)imageFile.length()];
			final FileInputStream stream = new FileInputStream(imageFile);
			stream.read(data);
			stream.close();
			return data;
		} catch (Exception e) {
		    LogUtil.e("NetworkImage", ""+e);
			return null;
		}
	}
	
	
	public  byte [] byteDataImg(){
		final String fileName = getFileName(context);
		if (fileName == null) {
			return null;
		}
		final File imageFile = new File(fileName);
		if (!imageFile.exists()) {
			return null;
		}
		try {
			final byte[] data = new byte[(int)imageFile.length()];
			final FileInputStream stream = new FileInputStream(imageFile);
			stream.read(data);
			stream.close();
			return data;
		} catch (IOException e) {
			return null;
		}
	}

}
