package com.hnxx.wisdombase.ui.utils;

import android.content.Context;

import com.hnxx.wisdombase.framework.storage.WisdomPath;
import com.hnxx.wisdombase.framework.utils.LogUtil;

import java.net.URI;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CacheUtil
{
    private static HashMap<String, String> mCacheMap = new HashMap<String, String>();
    private static int mCacheFileNameMaxLength = 40;

    public static String getCacheFileName(Context context,final String url, String mimeType)
    {
        return getCacheFileName(context,url, mimeType, "/-");
    }

    public static String getCacheFileName(Context context,final String url, String mimeType,
                                          String delimiters)
    {
        String cachefilename = mCacheMap.get(url);
        if (null != cachefilename)
        {
            return cachefilename;
        }
        URI uri;
        try
        {
            uri = new URI(url);
        }
        catch (java.net.URISyntaxException ex)
        {
            return null;
        }

        String host = uri.getHost();

        if (host == null)
        {
            return null;
        }

        cachefilename = WisdomPath.instance(context).picCachePath;
        String urlpath = host + uri.getPath();
        String filename = "";
        try
        {
            StringTokenizer st = new StringTokenizer(urlpath, "/");
            while (st.hasMoreTokens())
            {
                filename += Integer.toHexString(st.nextToken().hashCode());
            }
//            LogUtil.d("CacheUtil", "getCacheFileName......" + url + "\n" + filename);
        }
        catch (Exception e)
        {
            filename = Integer.toHexString(urlpath.hashCode());
            LogUtil.e("CacheUtil", "getCacheFileName error:" + e.getMessage());
        }
        
        //如果路径长度超过50
        String tmpname = "";
        if(filename.length() > mCacheFileNameMaxLength) {
            int blocklen = filename.length() / 5;
            for(int i = 0; i < 5; ++i) {
                String tmp = filename.substring(i * blocklen, (i + 1) * blocklen);
                if(4 == i) {
                    tmp = filename.substring(i * blocklen);
                }
                tmpname += Integer.toHexString(tmp.hashCode());
            }
//            LogUtil.d("CacheUtil", "length too long:" + tmpname);
            filename = tmpname;
        }
        
        cachefilename += filename + ".cache";
        mCacheMap.put(url, cachefilename);
        return cachefilename;
    }
}
