package com.hnxx.wisdombase.ui.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.framework.api.WoSystem;
import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.ui.image.NetworkImage;
import com.hnxx.wisdombase.ui.image.NetworkImageHelp;
import com.hnxx.wisdombase.ui.image.ZLAndroidImageData;
import com.hnxx.wisdombase.ui.image.ZLAndroidImageManager;
import com.hnxx.wisdombase.ui.image.ZLImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 处理图片延时加载
 *
 * @author Administrator
 */
public class MyImageUtil {
    public enum ScaleAndClipType {
        Scale_Default,//默认值，和以前处理一样
        Scale_Fixed_Y,//按原尺寸比例缩放到指定高度
        Scale_Fixed_X,//按原尺寸比例缩放到指定宽度
        Scale_Fixed_XY,//缩放到指定宽度和高度
        Scale_Clip_XY,//缩放并裁剪到指定宽度和高度。比如将100*200的图片显示成60*80，则先将图片缩放到60*120，再将高120上下各裁剪20个像素变成80
        Scale_Clip_Y//缩放并裁剪到指定宽度和高度。比如将100*200的图片显示成60*80，则先将图片缩放到60*120，再将高从上裁剪40个像素变成80
    }

    /**
     * 图片工具配置类
     *
     * @ClassName:MyImageConfig
     * @Description: 因配置参数会越来越多，为了减少方法的参数个数，以后新增参数都加入到该配置类中
     * @author: zhoujun
     */
    public static class MyImageConfig {
        public int defaultResId;//默认图片资源id
        public String a;//对应的业务标识，比如所处的页面或控件，不建议使用
        public ScaleAndClipType type;//图片缩放、剪切类型

        /**
         * 图片附带文字TextView
         */
        public TextView tvew;

        /**
         * 图片附带文字
         */
        public String strTvew;

        public MyImageConfig() {
            defaultResId = -1;
            a = "";
            type = ScaleAndClipType.Scale_Default;
        }

        public MyImageConfig(int defaultResId, String a, ScaleAndClipType type) {
            this.defaultResId = defaultResId;
            this.a = a;
            this.type = type;
        }

        public MyImageConfig(int defaultResId, String a, TextView tvew, String strTvew) {
            this.defaultResId = defaultResId;
            this.a = a;
            type = ScaleAndClipType.Scale_Default;
            this.tvew = tvew;
            this.strTvew = strTvew;
        }
    }

//    public final static int FIXED_HEIGHT = -1;
//    
//    public final static int FIXED_WIDTH = -2;

    private static String TAG = "MyImageUtil";

    public static int imageNumber = 0;

    public static int iSecImgNo = 1;

    public static int iFirImgNo = 0;

    private static HashSet<String> myAwaitedCovers = new HashSet<String>();

    private static HashMap<String, NetworkImage> listNetworkImg = new HashMap<String, NetworkImage>();

//	private static HashMap<String, ZLAndroidImageData> ListImgCache = new HashMap<String, ZLAndroidImageData>();

    //	private static HashMap<String, SoftReference<ZLAndroidImageData>> ListImgCache = new HashMap<String, SoftReference<ZLAndroidImageData>>();
    private static HashMap<String, ZLAndroidImageData> ListImgCache = new HashMap<String, ZLAndroidImageData>();

    public static List<BaseAdapter> imageAdapters = new ArrayList<BaseAdapter>();

    public static int mGlobleCacheSize = 40;
    public static int mCacheSize = 25;

    public static final class Size {
        public final int Width;
        public final int Height;

        public Size(int w, int h) {
            Width = w;
            Height = h;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof Size)) {
                return false;
            }
            final Size s = (Size) other;
            return Width == s.Width && Height == s.Height;
        }
    }

    public static Size getImageSize(Context context,final String imgUrl) {
        NetworkImage img = listNetworkImg.get(imgUrl);
        if (null == img) {
            if (imgUrl != null && !"".equals(imgUrl)) {
                img = new NetworkImage(context,imgUrl, imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
                listNetworkImg.put(imgUrl, img);
            }
        }
        if (null != img) {
            if (img.isSynchronized()) {
                ZLAndroidImageData data = ListImgCache.get(img.getFileName(context));
                if (null == data) {
                    data = ZLAndroidImageManager.getInstance().getImageData(img);
                }
                if (null != data && data.getWidth() > 0 && data.getHeight() > 0) {
                    return new Size(data.getWidth(), data.getHeight());
                }
            }
        }
        return null;
    }

    public interface IImageLoadListener {
        void loadFinished(int status);
    }

    public static void setImage(Activity mActivity, IImageLoadListener l, String imgUrl, int width, int height, MyImageConfig config) {
        NetworkImage nwimage = listNetworkImg.get(imgUrl);
        if (nwimage == null) {
            if (imgUrl != null && !"".equals(imgUrl)) {
                nwimage = new NetworkImage(mActivity,imgUrl, imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
                listNetworkImg.put(imgUrl, nwimage);
            }
        }

        setAdImage(mActivity, l, null, nwimage, width, height, config, true);
    }

    /**
     * 图片设置需要获取到手机的分辨率，根据分辨率的大小，将图片设置为一个适合大高度和宽度
     *
     * @param mActivity
     * @param coverView
     * @param imgUrl
     * @param width
     * @param height
     */
    public static void setImage(Activity mActivity, ImageView coverView, String imgUrl, int width, int height) {
        setImage(mActivity, coverView, imgUrl, width, height, new MyImageConfig());
    }

    public static void setImage(Activity mActivity, ImageView coverView, String imgUrl, int width, int height, int defaultResId) {
        setImage(mActivity, coverView, imgUrl, width, height, new MyImageConfig(defaultResId, "", ScaleAndClipType.Scale_Default));
    }

    public static void setImage(Activity mActivity, ImageView coverView, String imgUrl, int width, int height, int defaultResId, ScaleAndClipType type) {
        setImage(mActivity, coverView, imgUrl, width, height, new MyImageConfig(defaultResId, "", type));
    }

    public static void setImage(Activity mActivity, ImageView coverView, String imgUrl, int width, int height, String a) {
        setImage(mActivity, coverView, imgUrl, width, height, new MyImageConfig(-1, a, ScaleAndClipType.Scale_Default));
    }

    public static void setImage(Activity mActivity, ImageView coverView, String imgUrl, int width, int height, MyImageConfig config) {
        NetworkImage nwimage = listNetworkImg.get(imgUrl);
        if (nwimage == null) {
            if (imgUrl != null && !"".equals(imgUrl)) {
                nwimage = new NetworkImage(mActivity,imgUrl, imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
                listNetworkImg.put(imgUrl, nwimage);
            }
        }

        setAdImage(mActivity, null, coverView, nwimage, width, height, config, true);
    }

    private static void setAdImage(final Activity mActivity, final IImageLoadListener l, final ImageView coverView, final NetworkImage image,
                                   final int width, final int height, final MyImageConfig config, boolean isFirstload) {

        Bitmap coverBitmap = null;
        ZLImage cover = image;
        if (cover == null) {
            // coverBitmap =
        }
        if (cover != null) {
            ZLAndroidImageData data = null;
            final ZLAndroidImageManager mgr = ZLAndroidImageManager.getInstance();
            if (cover instanceof NetworkImage) {
                final NetworkImage img = (NetworkImage) cover;
                if (img.isSynchronized()) {
//                  SoftReference<ZLAndroidImageData> soft = ListImgCache.get(img.getFileName());
//                  if(soft != null){
//                      data = soft.get();
//                  }
//                  if (null == data) {
//                      // data 将图片数据缓存到内存中
//                      data = mgr.getImageData(img);
//                      //ZLAndroidImageData为弱引用，会自动回收
//                      ListImgCache.put(img.getFileName(),new SoftReference<ZLAndroidImageData>(data));
//                  }
                    ZLAndroidImageData soft = ListImgCache.get(img.getFileName(mActivity) + config.a);
                    if (soft != null) {
                        data = soft;
                    }
                    if (null == data) {
                        // data 将图片数据缓存到内存中
                        data = mgr.getImageData(img);
                        // ZLAndroidImageData为弱引用，会自动回收
                        ListImgCache.put(img.getFileName(mActivity) + config.a, data);
                    }
                } else {
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setAdImage(mActivity, l, coverView, image, width, height, config, false);
                                }
                            });
                        }
                    };
                    final NetworkImageHelp networkView = NetworkImageHelp.Instance();
                    if (!networkView.isCoverLoading(img.Url)) {
                        networkView.performCoverSynchronization(mActivity,img, runnable);
                        myAwaitedCovers.add(img.Url);
                    } else if (!myAwaitedCovers.contains(img.Url)) {
                        networkView.addCoverSynchronizationRunnable(img.Url, runnable);
                        myAwaitedCovers.add(img.Url);
                    }
                }
            } else {
                data = mgr.getImageData(cover);
            }
            if (data != null) {
                coverBitmap = data.getBitmap(width, height, config.type);
                if (null == coverBitmap) {
                    //图片加载失败，重新下载加载
                    ListImgCache.remove(image.getFileName(mActivity) + config.a);
                    listNetworkImg.remove(image.Url);
                    image.clearCache(mActivity);
                    setAdImage(mActivity, l, coverView, image, width, height, config, isFirstload);
                }
            } else {
            }
        }
        if (null != coverBitmap) {
            if (null != coverView) {
                coverView.setImageDrawable(new BitmapDrawable(coverBitmap));
            } else if (null != l) {
                l.loadFinished(0);
            }

            if (null != config.tvew) {
                config.tvew.setVisibility(View.GONE);
            }
            if (!isFirstload) {
                for (int i = 0; i < imageAdapters.size(); ++i) {
                    if (null != imageAdapters.get(i)) {
                        imageAdapters.get(i).notifyDataSetChanged();
                    }
                }
            }
/*            switch (coverView.getId())
            {
                case R.id.ImageView01:
                case R.id.ImageView02:
                case R.id.ImageView03:
//                zsa.notifyDataChanged();
//                    ((ZLAndroidApplication) mActivity.getApplication()).getzBookSelfActivity().notifyDataChanged();
                    break;
                default:
                    break;
            }*/
//          coverView.setImageBitmap(null);
//          coverView.setImageBitmap(coverBitmap);
        } else {
//          coverView.setImageBitmap(BitmapFactory.decodeResource(
//                  mActivity.getResources(),
//                  R.drawable.fengmian));
            //默认图片资源id
            if (-1 != config.defaultResId) {
                if (null != coverView) {
                    coverView.setImageResource(config.defaultResId);
                }
                if (null != config.tvew) {
                    config.tvew.setVisibility(View.VISIBLE);
                    config.tvew.setText(config.strTvew);
                }
            } else {
                if (null != coverView) {
                    coverView.setImageResource(R.mipmap.welcome);
                }
            }
        }

    }



    public static NetworkImage getImage(Context context,String imgUrl) {
        NetworkImage nwimage = listNetworkImg.get(imgUrl);
        if (nwimage == null) {
            if (imgUrl != null && !"".equals(imgUrl)) {
                nwimage = new NetworkImage(context,imgUrl, imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
                listNetworkImg.put(imgUrl, nwimage);
            }
        }
        return nwimage;
    }

    public static Bitmap getBitmap(Context context,String imgUrl) {
        NetworkImage img = listNetworkImg.get(imgUrl);
        if (null == img) {
            if (imgUrl != null && !"".equals(imgUrl)) {
                img = new NetworkImage(context,imgUrl, imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
                listNetworkImg.put(imgUrl, img);
            }
        }
        if (null != img) {
            if (img.isSynchronized()) {
                ZLAndroidImageData data = ListImgCache.get(img.getFileName(context));
                if (null == data) {
                    data = ZLAndroidImageManager.getInstance().getImageData(img);
                }
                if (null != data && data.getWidth() > 0 && data.getHeight() > 0) {
                    return data.getMyBitmap();
                }
            }
        }
        return null;
    }

    public static Bitmap getBitmapByUri(ContentResolver cr, Uri url) {
        try {
            InputStream input = cr.openInputStream(url);

            final BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, options);
            int myRealWidth = options.outWidth;

            options.inJustDecodeBounds = false;
            int inSampleSizeWidth = 1;
            int maxWidth = WoSystem.SCREEN_WIDTH;
            inSampleSizeWidth = (int) (myRealWidth / (float) maxWidth);
            if (inSampleSizeWidth < 1) {
                inSampleSizeWidth = 1;
            }

            options.inSampleSize = inSampleSizeWidth;
            options.inJustDecodeBounds = false;
            options.inInputShareable = true;
            options.inPurgeable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            input = cr.openInputStream(url);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
            input.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public interface IImageLoadListener2 {
        void loadFinished(Bitmap bitmap);
    }

    public static Bitmap getBitmap(Context context,final IImageLoadListener2 l, final String imgUrl,
                                   final int width, final int height, final MyImageConfig config) {
        NetworkImage image = listNetworkImg.get(imgUrl);
        if (image == null) {
            if (imgUrl != null && !"".equals(imgUrl)) {
                image = new NetworkImage(context,imgUrl, imgUrl.substring(imgUrl.lastIndexOf(".") + 1));
                listNetworkImg.put(imgUrl, image);
            }
        }

        Bitmap coverBitmap = null;
        ZLImage cover = image;
        if (cover != null) {
            ZLAndroidImageData data = null;
            final ZLAndroidImageManager mgr = ZLAndroidImageManager.getInstance();
            if (cover instanceof NetworkImage) {
                final NetworkImage img = (NetworkImage) cover;
                if (img.isSynchronized()) {
                    LogUtil.d("ffff", "存在图片缓存" + width);
                    ZLAndroidImageData soft = ListImgCache.get(img.getFileName(context) + config.a);
                    if (soft != null) {
                        LogUtil.d("ffff", "缓存中取   大小：" + img.byteData().length + "  名称：" + img.getFileName(context) + config.a);
                        data = soft;
                    }
                    if (null == data) {
                        if (img != null && img.byteData() != null) {
                            LogUtil.d("ffff", "Byte数组中取   大小：" + img.byteData().length + img.byteData().length + "  名称：" + img.getFileName(context) + config.a);
                            // data 将图片数据缓存到内存中
                            data = mgr.getImageData(img);
                            // ZLAndroidImageData为弱引用，会自动回收
                            ListImgCache.put(img.getFileName(context) + config.a, data);
                        }
                    }
                } else {
                    final Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            getBitmap(context,l, imgUrl, width, height, config);
                        }
                    };
                    final NetworkImageHelp networkView = NetworkImageHelp.Instance();
                    if (!networkView.isCoverLoading(img.Url)) {
                        networkView.performCoverSynchronization(context,img, runnable);
                        myAwaitedCovers.add(img.Url);
                    } else if (!myAwaitedCovers.contains(img.Url)) {
                        networkView.addCoverSynchronizationRunnable(img.Url, runnable);
                        myAwaitedCovers.add(img.Url);
                    }
                }
            } else {
                data = mgr.getImageData(cover);
            }
            if (data != null) {
                coverBitmap = data.getBitmap(width, height, config.type);
                if (null == coverBitmap) {
                    LogUtil.i("ffff", "图片加载失败，重新下载加载" + width);
                    //图片加载失败，重新下载加载
                    ListImgCache.remove(image.getFileName(context) + config.a);
                    listNetworkImg.remove(image.Url);
                    image.clearCache(context);
                    getBitmap(context,l, imgUrl, width, height, config);
                }
            }
        }
        if (null != coverBitmap) {
            if (null != l) {
                l.loadFinished(coverBitmap);
            }

        }
        return coverBitmap;
    }


}
