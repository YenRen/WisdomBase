package com.hnxx.wisdombase.ui.image;

import android.content.Context;
import android.widget.ImageView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.youth.banner.loader.ImageLoader;

/**
 * Created by zhoujun on 2017/8/2.
 */

public class MyImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

    }

    @Override
    public ImageView createImageView(Context context) {
        SimpleDraweeView simpleDraweeView=new SimpleDraweeView(context);
        //设置圆角
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(PhoneInfoTools.dip2px(context,6));
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder.setPlaceholderImage(R.mipmap.welcome).build();
        simpleDraweeView.setHierarchy(hierarchy);
        simpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        simpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY);
        return simpleDraweeView;
    }
}
