/**
 *
 * @author zhoujun
 * @date Nov 4
 */
package com.hnxx.wisdombase.ui.base;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.framework.interfaces.LifeDisposable;
import com.hnxx.wisdombase.ui.widget.CustomToast;
import com.hnxx.wisdombase.ui.widget.dialog.CustomProgressDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author zhoujun
 */
public abstract class BaseFragment extends Fragment implements LifeDisposable {

    protected View mRootView;
    protected WisdomApplication mApplication;
    protected Context mCtx;
    protected LayoutInflater mInflater;
    protected CustomProgressDialog mCustomProgressDialog;
    protected Unbinder mUnbinder;

    /**弹窗适配页面需要使用到，故修改为static*/
    public int curIndex;
    protected String title;
    protected OnClickListener onClickListener;

    /**
     * rx被通知者结合，挂载生命周期上，避免内存泄漏
     */
    private CompositeDisposable mCompositeDisposable;

    /**
     * 添加Rx Disposable
     *
     * @param disposable
     */
    @Override
    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mApplication = WisdomApplication.Instance();
        if(container == null)
        {
            mCtx = this.getActivity();
        }else
        {
            mCtx = container.getContext();
        }

        mInflater = inflater;
        //viewpager中fragment复用可能出现recyclerView对象不同问题
        if(null == mRootView){
            mRootView = inflater.inflate(getLayoutId(), null);
            mUnbinder = ButterKnife.bind(this, mRootView);
            findViewById();
            setListener();
            init();
        }

        return mRootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (this.mUnbinder != null) {
            this.mUnbinder.unbind();
            this.mUnbinder = null;
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    /**
     * 获取页面的布局资源id
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected void findViewById(){};

    protected <V extends View> V findViewById(int resId) {
        if (null != mRootView) {
            return (V) mRootView.findViewById(resId);
        } else {
            return null;
        }
    }

    protected abstract void setListener();

    protected abstract void init();

    protected void showProgressDialog(final String msg) {
        if(mCustomProgressDialog == null) {
            mCustomProgressDialog = new CustomProgressDialog(getActivity());
        }
        mCustomProgressDialog.setMessage(msg);
        mCustomProgressDialog.show();
    }

    protected void showProgressDialog(final String msg, final String cancelHint) {
        if (mCustomProgressDialog == null) {
            mCustomProgressDialog = new CustomProgressDialog(getActivity());
        }
        if (!mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.setMessage(msg);
            mCustomProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    CustomToast.showToast(mCtx,cancelHint, Toast.LENGTH_SHORT);
                }
            });
            mCustomProgressDialog.show();
        }
    }

    protected void dismissProgressDialog() {
        if(mCustomProgressDialog != null && mCustomProgressDialog.isShowing()) {
            mCustomProgressDialog.dismiss();
        }
    }

    public void setTabIndex(int index, String title) {
        this.curIndex = index;
        this.title = title;
    }

    public void setOnTitleViewClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
