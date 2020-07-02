package com.hnxx.wisdombase.ui.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hnxx.wisdombase.config.application.WisdomApplication;
import com.hnxx.wisdombase.framework.utils.LogUtil;
import com.hnxx.wisdombase.ui.widget.CustomToast;

/**
 * @author zhoujun 应用于TextView比较常用的处理,而提供的帮助类
 */
public class TextViewUtils {
    private TextViewUtils() {
    }

    /**
     * 为TextView创建点击监听器,用于伸展和收缩文本
     *
     * @param lines
     * @param tvs
     */
    public static void expendTextView(final int lines, TextView... tvs) {
        for (final TextView tv : tvs) {
            tv.setOnClickListener(new OnClickListener() {
                boolean flag = false;

                @Override
                public void onClick(View v) {
                    if (flag) {
                        flag = true;
                        tv.setEllipsize(null);
                        tv.setSingleLine(false);
                    } else {
                        flag = false;
                        tv.setEllipsize(TextUtils.TruncateAt.END);
                        tv.setLines(lines);
                    }
                }
            });
        }
    }

    /**
     * 单独设置EditText控件中hint文本的字体大小，可能与EditText文字大小不同
     *
     * @param editText 输入控件
     * @param hintText hint的文本内容
     * @param textSize textSize hint的文本的文字大小（以dp为单位设置即可）
     */
    public static void setHintTextSize(EditText editText, String hintText, int textSize) {
        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString(hintText);
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // 设置hint
        editText.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }
    // -------------------------------------------

    /**
     * 检测是否为空
     *
     * @param emptyOpean
     * @param tvs
     * @return
     */
    public static boolean checkEmpty(Context context, EmptyOpean emptyOpean,
                                     TextView... tvs) {
        for (int i = 0; i < tvs.length; i++) {
            if (TextUtils.isEmpty(fromTv(tvs[i]))) {
                CustomToast.showToast(context, emptyOpean.emtpyAction(i), Toast.LENGTH_SHORT);
                return true;
            }
        }
        return false;
    }


    /**
     * 检测是否为空
     *
     * @param tv
     * @return
     */
    public static boolean isEmpty(TextView tv) {
        String s = fromTv(tv);
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 检测控件内容符合字符串吗
     *
     * @param tv
     * @return
     */
    public static boolean equalsString(TextView tv, String str) {
        if (str == null) {
            return false;
        }
        return str.equals(fromTv(tv));
    }

    /**
     * 检测是否为空
     *
     * @return
     */
    public static boolean isEmpty(TextView... tvs) {
        for (TextView tv : tvs) {
            if (isEmpty(tv)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测是否为空,如果为空toast hint
     *
     * @return
     */
    public static boolean isEmptyAndHint(TextView... tvs) {
        for (TextView tv : tvs) {
            if (isEmpty(tv)) {
                CustomToast.showToast(WisdomApplication.Instance().getContext(), tv.getHint().toString(), Toast.LENGTH_SHORT);
                return true;
            }
        }
        return false;
    }

    public interface EmptyOpean {
        String emtpyAction(int position);
    }

    // -------------------------------------------

    /**
     * 从TextView上获取文本
     *
     * @param tv
     * @return
     */
    public static String fromTv(TextView tv) {
        String str = tv.getText().toString();
        if (tv.getInputType() == InputType.TYPE_CLASS_NUMBER) {
            return str.trim();
        }
        return str;
    }

    /**
     * 判断两个TextView的文本是否一致
     *
     * @param tv1
     * @param tv2
     * @return
     */
    public static boolean equalsText(TextView tv1, TextView tv2) {
        return fromTv(tv1).equals(fromTv(tv2));
    }

    /**
     * 自动弹出软盘,针对Dialog的EditText即使获取焦点,依旧不自动弹出软盘的解决方案
     *
     * @param dialog
     */
    public static void autoPopSoftForDialog(TextView tv, Dialog dialog) {
        tv.requestFocus();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 根据宽度，调整自身字体大小，以一行显示完全
     *
     * @param tv
     * @param textViewWidth textView宽度
     */
    public static void autoTextSize(TextView tv, int textViewWidth) {
        textViewWidth = textViewWidth - tv.getPaddingLeft() - tv.getPaddingRight();
        String s = fromTv(tv);
        float textWidth = tv.getPaint().measureText(s);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (textViewWidth / textWidth) * tv.getTextSize());
        while (true) {
            //计算所有文本占有的屏幕宽度(pix)
            textWidth = tv.getPaint().measureText(s);
            //如果所有文本的宽度超过TextView自身限定的宽度，那么就尝试迭代的减小字体的textSize，直到不超过TextView的宽度为止。
            if (textWidth > textViewWidth) {
                int textSize = (int) tv.getTextSize();
                textSize = textSize - 2;
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            } else {
                break;
            }
        }
        LogUtil.d("自适应字体的最终大小：" + tv.getTextSize() + " pix");
    }


}
