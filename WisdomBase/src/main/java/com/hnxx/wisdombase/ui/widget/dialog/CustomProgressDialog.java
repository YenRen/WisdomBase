/**
 * 显示进度窗
 * CustomProgressDialog pDialog = CustomProgressDialog.createDialog(getActivity());
 * pDialog.setMessage("正在加载下一章节，文字比较长的时候是怎么一个效果呢");
 * pDialog.show();
 * <p>
 * 取消进度窗
 * pDialog.dismiss();
 */
package com.hnxx.wisdombase.ui.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hnxx.wisdombase.R;
import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.ui.base.BaseDialog;
import com.hnxx.wisdombase.ui.utils.NavigationBarUtil;

/**
 * 自定义进度窗体
 *
 * @ClassName:CustomProgressDialog
 * @Description: 自定义进度窗体
 * @author: maj
 * @date: 2015年5月20日
 */
public class CustomProgressDialog extends BaseDialog {

    public CustomProgressDialog(Context context) {
        this(context, R.style.customprogressdialog);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(false);
        View contentView = getContentView(context);
        if (contentView != null) {
            this.setContentView(getContentView(context));
            this.getWindow().getAttributes().gravity = Gravity.CENTER;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View getContentView(Context context) {
        LayoutInflater inflate = (LayoutInflater)
                WisdomApplication.Instance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mContentView = inflate.inflate(R.layout.customprogressdialog, null);
        return mContentView;
    }

    public void setMessage(String strMessage) {
        TextView tvMsg = null;
        tvMsg = CustomProgressDialog.this.findViewById(R.id.customprogressdialog_tv_msg);

        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }

    @Override
    public void show() {
        Window window = this.getWindow();
        if (window == null) {
            super.show();
            return;
        }
        NavigationBarUtil.focusNotAle(window);
        super.show();
        NavigationBarUtil.clearFocusNotAle(window);
    }
}
