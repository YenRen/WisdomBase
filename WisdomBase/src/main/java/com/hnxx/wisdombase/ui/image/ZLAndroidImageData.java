
package com.hnxx.wisdombase.ui.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.ui.utils.MyImageUtil.ScaleAndClipType;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class ZLAndroidImageData implements ZLImageData {
	public final static int MEMORY_OPTIMIZE_HEIGHT_LIMIT = 300;//内存优化的图片高度上限
	public final static int MEMORY_OPTIMIZE_WIDTH_LIMIT = 480;//内存优化的图片宽度上限
	private byte[] myArray;
	private Bitmap myBitmap;
	private int myRealWidth;
	private int myRealHeight;
	private int myLastRequestedWidth;
	private int myLastRequestedHeight;
	private ScaleAndClipType myLastRequestType;
	
	public ZLAndroidImageData(byte[] array) {
		myArray = array;
	}
	
	public ZLAndroidImageData(String srcPath){
		
		FileInputStream in = null;
		ByteArrayOutputStream bytestream = null;
		try {
			in =  new FileInputStream(srcPath);
			bytestream = new ByteArrayOutputStream();  
			int ch;  
			while ((ch = in.read()) != -1) {  
			bytestream.write(ch);  
			}
            byte[] imgdata = bytestream.toByteArray();
			
			myArray = imgdata;
			
			
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}finally{
			try {
				bytestream.close();
				in.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			}
		
		}
		
	}

	public synchronized int getWidth()
    {
	    decodeBitmapSize();
        return myRealWidth;
    }
	
	public synchronized int getHeight()
	{
	    decodeBitmapSize();
	    return myRealHeight;
	}
	
	public void decodeBitmapSize()
	{
        if (null == myArray)
        {
            return;
        }
        try
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();

            if (myRealWidth <= 0)
            {
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(myArray, 0, myArray.length, options);
                myRealWidth = options.outWidth;
                myRealHeight = options.outHeight;
            }
        }
        catch (OutOfMemoryError e)
        {
            System.gc();
            System.gc();
            LogUtil.e("memory", "overflow");
        }
        catch (Exception e)
        {
            // TODO: handle exception+
            System.gc();
            System.gc();
            LogUtil.e("ZLAndroidImageData", "" + e);
        }
	}
	
	public synchronized Bitmap getBitmap(int desireWidth, int desireHeight, ScaleAndClipType type) {
		if (null == myArray) {
			return null;
		}
		if (((desireWidth != myLastRequestedWidth) || (desireHeight != myLastRequestedHeight))
				|| type != myLastRequestType
				|| null == myBitmap) {
			if (myBitmap != null) {
		/**
				 * 一张图片只要当彻底不用时才需要释放掉，当activity跳转或者展示dialog，父activity并没有finish时，
				 * 此时不能将bitmap recyle 此处暂没有执行的机会，因为 myBitmap是放到缓存中，一个图片就一个bitmap对象，
				 * 通过view的setLayoutParams进行像素的转换
				 */
				myBitmap.recycle();
				myBitmap = null;
			}
			try {
				final BitmapFactory.Options options = new BitmapFactory.Options();
				
				if (myRealWidth <= 0) {
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeByteArray(myArray, 0, myArray.length, options);
					myRealWidth = options.outWidth;
					myRealHeight = options.outHeight;
				}
				
				options.inJustDecodeBounds = false;
				int inSampleSizeHeight = 1;
				int inSampleSizeWidth = 1;
				//如果要求的图片大小小于200(魔法数字，改成常量定义MEMORY_OPTIMIZE_HEIGHT_LIMIT),则按照实际要求的值进行处理
				int tmpHeight = MEMORY_OPTIMIZE_HEIGHT_LIMIT;
				int tmpWidth = MEMORY_OPTIMIZE_WIDTH_LIMIT;
				if (desireHeight > 0 && desireHeight < tmpHeight) {
					tmpHeight = desireHeight;
				}
				if (desireWidth > 0 && desireWidth < tmpWidth) {
					tmpWidth = desireWidth;
				}
				inSampleSizeHeight = (int) (options.outHeight / (float) tmpHeight);
				if (inSampleSizeHeight < 1) {
					inSampleSizeHeight = 1;
				}
				inSampleSizeWidth = (int) (options.outWidth / (float) tmpWidth);
				if (inSampleSizeWidth < 1 || type == ScaleAndClipType.Scale_Default) {
					inSampleSizeWidth = 1;
				}
				
				options.inSampleSize =  inSampleSizeHeight > inSampleSizeWidth ? inSampleSizeHeight : inSampleSizeWidth;
//				options.inSampleSize = computeSampleSize(options,-1,600*800);
				options.inJustDecodeBounds = false;
				//xiqiubo add begin 20130401 for bitmap 优化将默认32位图设置为16位图
				options.inInputShareable = true;
				options.inPurgeable = true;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				//xiqiubo add end 20130401 for bitmap 优化将默认32位图设置为16位图
				Bitmap bmp = BitmapFactory.decodeByteArray(myArray, 0, myArray.length, options);
				
				int width = desireWidth, height = desireHeight;
				if (type == ScaleAndClipType.Scale_Default) {
					myBitmap = bmp;
				} else if (type == ScaleAndClipType.Scale_Fixed_X) {
					//宽度确定，高度按照宽度缩放比缩放
					height = desireWidth * myRealHeight / myRealWidth;
					myBitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
				} else if (type == ScaleAndClipType.Scale_Fixed_Y) {
					//高度确定，宽度按照宽度缩放比缩放
					width = desireHeight * myRealWidth / myRealHeight;
					myBitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
				} else if (type == ScaleAndClipType.Scale_Fixed_XY) {
					//直接按照指定的宽和高缩放
					myBitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
				} else if (type == ScaleAndClipType.Scale_Clip_XY) {
					//缩放并裁剪到指定宽度和高度。比如将100*200的图片显示成60*80，则先将图片缩放到60*120，再将高120上下各裁剪20个像素变成80
					//两侧用乘法避免除法导致浮点数比较
					if (desireHeight * myRealWidth > desireWidth * myRealHeight) {
						width = desireHeight * myRealWidth / myRealHeight;
						Bitmap tmpBmp = Bitmap.createScaledBitmap(bmp, width, height,
							true);
						int adjustWidth = (width - desireWidth) / 2;
						myBitmap = Bitmap.createBitmap(tmpBmp, adjustWidth, 0, desireWidth,
							desireHeight);
					} else if (desireHeight * myRealWidth < desireWidth * myRealHeight) {
						height = desireWidth * myRealHeight / myRealWidth;
						Bitmap tmpBmp = Bitmap.createScaledBitmap(bmp, width, height,
							true);
						int adjustHeight = (height - desireHeight) / 2;
						myBitmap = Bitmap.createBitmap(tmpBmp, 0, adjustHeight, desireWidth,
							desireHeight);
					} else {
						myBitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
					}
				} else if(type == ScaleAndClipType.Scale_Clip_Y){
					if (desireHeight * myRealWidth > desireWidth * myRealHeight) {
						width = desireHeight * myRealWidth / myRealHeight;
						Bitmap tmpBmp = Bitmap.createScaledBitmap(bmp, width, height,
							true);
						myBitmap = Bitmap.createBitmap(tmpBmp, 0, 0, desireWidth,
							desireHeight);
					} else if (desireHeight * myRealWidth < desireWidth * myRealHeight) {
						height = desireWidth * myRealHeight / myRealWidth;
						Bitmap tmpBmp = Bitmap.createScaledBitmap(bmp, width, height,
							true);
						myBitmap = Bitmap.createBitmap(tmpBmp, 0, 0, desireWidth,
							desireHeight);
					} else {
						myBitmap = Bitmap.createScaledBitmap(bmp, width, height, true);
					}
				}
				
				if (myBitmap != null) {
					myLastRequestedWidth = desireWidth;
					myLastRequestedHeight = desireHeight;
					myLastRequestType = type;
				}
            }
            catch (OutOfMemoryError e)
            {
                System.gc();
                System.gc();
                LogUtil.e("memory", "overflow");
            }
            catch (Exception e)
            {
                // TODO: handle exception+
                System.gc();
                System.gc();
                LogUtil.e("ZLAndroidImageData", "" + e);
                LogUtil.e("ffff", "width" + desireHeight  + " " + desireWidth);
            }
		}
		return myBitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
	public Bitmap getMyBitmap() {
		return myBitmap;
	}

	public void setMyBitmap(Bitmap myBitmap) {
		if(null != this.myBitmap) {
			this.myBitmap.recycle();
			this.myBitmap = null;
		}
		this.myBitmap = myBitmap;
	}



	public byte[] getMyArray() {
		return myArray;
	}



	public void setMyArray(byte[] myArray) {
		this.myArray = myArray;
	}
}
