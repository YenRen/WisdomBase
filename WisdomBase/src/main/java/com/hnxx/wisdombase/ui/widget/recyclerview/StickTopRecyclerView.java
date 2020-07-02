package com.hnxx.wisdombase.ui.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.framework.utils.PhoneInfoTools;
import com.hnxx.wisdombase.ui.utils.ViewUtils;


/**
 * Created on 2019/12/26.
 *
 * @author zhoujun
 * @describe 一键置顶
 */

public class StickTopRecyclerView extends RelativeLayout{

    private Context mContext;
    private RecyclerView mRecyclerView;
    private ImageView mFloatAction;
    private int totalDy;

    public StickTopRecyclerView(Context context) {
        this(context,null);
    }

    public StickTopRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public StickTopRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.stick_top_layout,null);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mFloatAction = view.findViewById(R.id.floatAction);
        addView(view);
        setListener();
    }

    /**
     * 调整floataction 位置
     * @param dpBottomMargin
     * @param dpRightMargin
     */
    public void locateFloatAction(int dpBottomMargin,int dpRightMargin){
        LayoutParams params = (LayoutParams) mFloatAction.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.recyclerView);
        params.bottomMargin = PhoneInfoTools.dip2px(getContext(),dpBottomMargin);
        params.rightMargin = PhoneInfoTools.dip2px(getContext(),dpRightMargin);
        mFloatAction.setLayoutParams(params);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public ImageView getImageView() {
        return mFloatAction;
    }

    public void setImageView(ImageView floatAction) {
        this.mFloatAction = floatAction;
    }


    private void setListener(){
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE && Math.abs(totalDy)> PhoneInfoTools.getHeight(getContext())*1.5){
                    ViewUtils.animateVisibleAction(true,mFloatAction);
                }else{
                    ViewUtils.animateVisibleAction(false,mFloatAction);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!mRecyclerView.canScrollVertically(-1)){
                    ViewUtils.animateVisibleAction(false,mFloatAction);
                }
                totalDy -= dy;
            }
        });
        mFloatAction.setOnClickListener(v -> mRecyclerView.smoothScrollToPosition(0));
    }

}
