package com.hnxx.wisdombase.ui.image;

import com.hnxx.wisdombase.framework.utils.LogUtil;

public final class ZLAndroidImageManager extends ZLImageManager
{

    private static ZLAndroidImageManager instance;

    public static ZLAndroidImageManager getInstance()
    {
        if (null == instance)
        {
            instance = new ZLAndroidImageManager();
        }
        return instance;
    }

    @Override
    public ZLAndroidImageData getImageData(ZLImage image)
    {
        try
        {
            if (image instanceof ZLSingleImage)
            {
                ZLSingleImage singleImage = (ZLSingleImage) image;
                if ("image/palm".equals(singleImage.mimeType()))
                {
                    return null;
                }
                byte[] array = singleImage.byteData();
                if (array == null)
                {
                    return null;
                }
                return new ZLAndroidImageData(array);
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            LogUtil.w("ZLAndroidImageData", e + "");
            return null;
        }
    }
}
