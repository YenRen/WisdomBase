/**
 * <p>
 * Copyright: Copyright (c) 2012
 * Company: ZTE
 * Description: 字符串工具类实现文件
 * </p>
 *
 * @Title StringUtil.java
 * @Package com.unicom.zworeader.framework.util
 * @version 1.0
 * @author jamesqiao10065075
 * @date 2012-02-08
 * 阅读客户端Android通用功能包
 */

/** 阅读客户端Android通用功能包  */
package com.hnxx.wisdombase.framework.utils;


import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类 *
 *
 * @ClassName: StringUtil
 * @Description: 汇集字符串处理相关的一些功能函数。
 * @author: jamesqiao10065075
 * @date: 2012-2-8
 *
 */
public final class StringUtil {

    /** 日志标签 */
    public static final String LOG_TAG = "StringUtil";
    private static final String[] units = {"", "十", "百", "千", "万", "十", "百",
            "千", "亿"};
    private static final String[] nums = {"一", "二", "三", "四", "五", "六", "七",
            "八", "九", "十"};

    /********************************** 删除空格类型 **********************************/
    /** 删除全部空格 */
    public static final int TYPE_REMOVE_SPACE_ALL = 0;
    /** 删除左边空格 */
    public static final int TYPE_REMOVE_SPACE_LEFT = 1;
    /** 删除右边空格 */
    public static final int TYPE_REMOVE_SPACE_RIGHT = 2;
    /** 删除两端空格 */
    public static final int TYPE_REMOVE_SPACE_BOTH = 3;
    /** ASCII空格 - 0x20 */
    public static final char SPACE_ASC = ' ';
    /** UTF-8空格 - E38080 */
    public static final char SPACE_UTF8 = '　';

    /** 中文正则表达式 */
    private static Pattern g_chinesePattern = Pattern.compile("[\u4e00-\u9fa5]");

    /** 英文正则表达式 */
    private static Pattern g_englishPattern = Pattern.compile("[a-zA-z]");

    /** 章节标题正则表达式 */
    private static Pattern g_titlePattern;

    /**
     * 不使用默认构造函数
     */
    private StringUtil() {

    }

    /**
     * 判断某个字符串是否已某个子字符串开头，当strTarget或strPrefix为null或空字符串的时候，返回false，其他根据实际情况返回。
     *
     * @date 2012-4-16
     * @param strTarget
     *            待比较的目标字符串
     * @param strPrefix
     *            待搜索的前缀字符串
     * @return
     *         字符串是否已某个子字符串开头，当strTarget或strPrefix为null或空字符串的时候，返回false，其他根据实际情况返回
     *         。
     */
    public static boolean startsWith(String strTarget, String strPrefix) {
        if (isEmptyString(strTarget) || isEmptyString(strPrefix)) {
            return false;
        } else {
            return strTarget.startsWith(strPrefix);
        }
    }

    /**
     * 判空的依据是null或者空字符串
     *
     * @date 2012-2-8
     * @param strTarget
     *            待判断的目标字符串
     * @return 如果是null或者是空字符串，则返回true，否则，返回false。
     */
    public static boolean isEmptyString(String strTarget) {
        return TextUtils.isEmpty(strTarget);
    }

    /**
     *
     * 获取非空字符串，如果不为空，直接返回；为空则用""代替。
     *
     * @author jamesqiao10065075
     * @date 2012-3-23
     * @param strTarget
     *            目标字符串
     * @return 如果目标字符串为null，则返回""；否则返回目标字符串。
     */
    public static String getStringNotNull(String strTarget) {
        if (null != strTarget) {
            return strTarget;
        } else {
            return "";
        }
    }

    /**
     *
     * 判断两个字符串是否相同，依据是否都为null或者内容是否相同。
     *
     * @date 2012-6-7
     * @author jamesqiao10065075
     * @param strSource
     *            字符串1
     * @param strTarget
     *            字符串2
     * @return 如果都为null或者内容相同，则返回true；否则返回false。
     */
    public static boolean isSameString(String strSource, String strTarget) {
        // 都不为null
        if (null != strSource && null != strTarget) {
            return strSource.equals(strTarget);
        }
        // 两个都为null
        else // 有一个为null
        {
            return null == strSource && null == strTarget;
        }
    }

    /**
     *
     * 获取拆分后指定位置的单个字符串，根据分隔符拆分字符串，并返回对应位置的字符串
     *
     * @author jamesqiao10065075
     * @date 2012-3-23
     * @param strSrc
     *            用分隔符分割的原始字符串
     * @param strSeperator
     *            分隔符
     * @param iPos
     *            指定位置（基于0）
     * @return 对应位置的字符串，如果入参非法，则返回null;如果分隔符之间无内容，返回空串。
     */
    public static String getSplitedString(String strSrc, String strSeperator,
                                          int iPos) {
        LogUtil.d(LOG_TAG, "strSrc=" + strSrc + ",strSeperator=" + strSeperator
                + ",iPos=" + iPos);

        if (null == strSrc) {
            LogUtil.w(LOG_TAG, "strSrc is null!");
            return null;
        } else if ("".equals(strSrc) || isEmptyString(strSeperator)) {
            LogUtil.w(LOG_TAG, "strSrc is empty or strSeperator is empty!");
            return strSrc;
        }

        String[] arStrings = splitString(strSrc, strSeperator);

        LogUtil.d(LOG_TAG, "length=" + arStrings.length);
        if (iPos >= 0 && iPos < arStrings.length) {
            return getStringNotNull(arStrings[iPos]);
        } else {
            LogUtil.w(LOG_TAG, "Pos out of range[0," + arStrings.length + "]");
            return null;
        }
    }

    /**
     *
     * 分隔字符串，使用指定分隔符分隔字符串，如果两个分隔符之间没有字符，则返回空串
     *
     * @author jamesqiao10065075
     * @date 2012-3-23
     * @param strTarget
     *            源字符串
     * @param strSeperator
     *            分隔符
     * @return 分隔后的字符串数组
     */
    public static String[] splitString(String strTarget, String strSeperator) {
        List<String> listRets = new ArrayList<String>();
        if (isEmptyString(strTarget) || isEmptyString(strSeperator)) {
            return listRets.toArray(new String[1]);
        }

        // 下一个分隔符位置
        int iLastPos = 0;
        int iPos = strTarget.indexOf(strSeperator, iLastPos);
        if (iPos >= 0) {
            while (iPos >= 0) {
                if (iPos > iLastPos) {
                    listRets.add(strTarget.substring(iLastPos, iPos));
                } else
                // 两个分隔符紧邻
                {
                    listRets.add("");
                }

                iLastPos = iPos + 1;

                iPos = strTarget.indexOf(strSeperator, iLastPos);
            }

            // 最后一段
            if (iLastPos <= (strTarget.length() - 1)) {
                listRets.add(strTarget.substring(iLastPos));
            }
            // 最后一个
            else {
                listRets.add("");
            }
        } else
        // 整体作为一个单元项
        {
            listRets.add(strTarget);
        }

        return listRets.toArray(new String[1]);
    }

    /**
     *
     * 去除字符串中的两端的空格(包括ASC空格和UTF8空格)
     *
     * @author jamesqiao10065075
     * @date 2012-3-27
     * @param strSource
     *            来源字符串
     * @return 去除空格后的字符串
     */
    public static String removeSpace(String strSource) {
        return removeSpace(strSource, TYPE_REMOVE_SPACE_BOTH);
    }

    /**
     *
     * 去除字符串中的空格(包括ASC空格和UTF8空格)，
     * 支持删除全部空格、只删除左边空格、只删除右边空格，删除两端空格
     *
     * @author jamesqiao10065075
     * @date 2012-3-27
     * @param strSource
     *            来源字符串
     * @param iRemoveType
     *            TYPE_REMOVE_SPACE_ALL等于全部，TYPE_REMOVE_SPACE_LEFT等于左边，
     *            TYPE_REMOVE_SPACE_RIGHT等于右边，TYPE_REMOVE_SPACE_BOTH等于两端
     * @return 去除空格后的字符串
     */
    public static String removeSpace(String strSource, int iRemoveType) {
        if (isEmptyString(strSource)) {
            LogUtil.w(LOG_TAG, "strSource is empty!");
            return "";
        }

        String strReturn = strSource;

        switch (iRemoveType) {
            case TYPE_REMOVE_SPACE_ALL: // 去除全部空格
            {
                strReturn = strReturn.replace(String.valueOf(SPACE_ASC), "");
                strReturn = strReturn.replace(String.valueOf(SPACE_UTF8), "");
                break;
            }
            case TYPE_REMOVE_SPACE_LEFT: // 去除左边空格
            case TYPE_REMOVE_SPACE_RIGHT: // 去除右边空格
            case TYPE_REMOVE_SPACE_BOTH: // 去除两端空格
            {
                // 去除左边或者两端空格
                if (TYPE_REMOVE_SPACE_BOTH == iRemoveType
                        || TYPE_REMOVE_SPACE_LEFT == iRemoveType) {
                    int i = 0;
                    int n = strReturn.length();
                    for (i = 0; i < n; i++) {
                        if (strReturn.charAt(i) != SPACE_ASC &&
                                strReturn.charAt(i) != SPACE_UTF8) {
                            break;
                        }
                    }

                    if (i < n) {
                        strReturn = strReturn.substring(i);
                    } else {
                        strReturn = "";
                    }
                }
                // 去除右边或者两端空格
                if (TYPE_REMOVE_SPACE_BOTH == iRemoveType
                        || TYPE_REMOVE_SPACE_RIGHT == iRemoveType) {
                    int i = 0;
                    int n = strReturn.length();
                    for (i = n - 1; i >= 0; i--) {
                        if (strReturn.charAt(i) != SPACE_ASC &&
                                strReturn.charAt(i) != SPACE_UTF8) {
                            break;
                        }
                    }

                    if (i >= 0) {
                        strReturn = strReturn.substring(0, i + 1);
                    } else {
                        strReturn = "";
                    }
                }
                break;
            }
            default: //
            {
                break;
            }
        }

        return strReturn;
    }

    /**
     *
     * 整型值转换为字节
     *
     * @date 2013-1-14
     * @author jamesqiao10065075
     * @param iValue
     *            整型值
     * @return
     */
    public static byte[] toLH(int iValue) {
        byte[] byData = new byte[4];

        byData[0] = (byte) (iValue & 0xff);
        byData[1] = (byte) (iValue >> 8 & 0xff);
        byData[2] = (byte) (iValue >> 16 & 0xff);
        byData[3] = (byte) (iValue >> 24 & 0xff);

        return byData;
    }

    /**
     *
     * 字节转换为字符串形式
     *
     * @date 2013-1-14
     * @author jamesqiao10065075
     * @param byData
     * @return
     */
    public static String bytesToString(byte[] byData) {
        if (null == byData) {
            LogUtil.w(LOG_TAG, "byData is null!");
            return null;
        }
        StringBuffer sbResult = new StringBuffer();
        int iLength = byData.length;
        for (int i = 0; i < iLength; i++) {
            sbResult.append((char) (byData[i] & 0xff));
        }

        return sbResult.toString();
    }

    /**
     * 去除字符串里括号之间的内容
     *
     * @date 2012-11-6
     * @author zte
     * @param strTarget
     *            原始字符串
     * @return 去除以后的字符串
     */
    public static String trimContentBetweenBrackets(String strTarget) {
        if (StringUtil.isEmptyString(strTarget)) {
            LogUtil.w(LOG_TAG, "strTarget is empty!");
            return "";
        }

        return strTarget.replaceAll("\\(.*\\)", "");
    }

    /**
     *
     * 字符串中关键字匹配后，使用特殊颜色高亮显示，若有多个关键字对应多种颜色高亮，则
     * 将结果SpannableString再次作为参数传入函数中,此时strSrc将不起作用.
     *
     * @author juncc
     * @date 2013-1-15
     * @param strSrc
     *            源字符串
     * @param strPattern
     *            关键字字符串,允许使用正则表达式
     * @param iColor
     *            匹配后,关键字所显示的颜色,可以使用Color类中的颜色常量
     * @param dstSpannable
     *            包含匹配规则的SpannableString实例
     * @return 若strSrc 与 dstSpannable均为空，则返回null，否则，返回包含特定规则的SpannableString实例
     */
    public static SpannableString getKeyWordSpannableString(String strSrc,
                                                            String strPattern, int iColor, SpannableString dstSpannable) {
        // 若s为空，使用strSrc创建一个新的SpannableString
        if (dstSpannable == null) {
            if (isEmptyString(strSrc)) {
                return null;
            }

            dstSpannable = new SpannableString(strSrc);
        }

        // 若关键字为空，则返回s
        if (isEmptyString(strPattern)) {
            return dstSpannable;
        }

        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(dstSpannable);

        // 使所有关键字高亮显示
        while (matcher.find()) {
            int iS = matcher.start();
            int iE = matcher.end();

            ForegroundColorSpan span = new ForegroundColorSpan(iColor);

            dstSpannable
                    .setSpan(span, iS, iE, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return dstSpannable;
    }

    /**
     * 为文本增加颜色
     * @param str 原始文本
     * @return
     */
    public static SpannableString getColorSpanString(String str, int iColor) {
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(iColor);
        spanString.setSpan(span, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }


    /**
     * 为中间某段文本增加颜色
     * @param preStr 前置文本(原色)，可为空
     * @param colorStr 高亮文本(使用iColor颜色)
     * @param nextStr 后置文本(原色)，可为空
     * @param iColor 特定颜色
     * @return
     */
    public static SpannableString getColorSpanString(String preStr, String colorStr,
                                                     String nextStr, int iColor) {
        SpannableString spanString = new SpannableString(preStr + colorStr + nextStr);
        ForegroundColorSpan span = new ForegroundColorSpan(iColor);
        int preStrLen = 0;
        if (!TextUtils.isEmpty(preStr)) {
            preStrLen = preStr.length();
        }
        spanString.setSpan(span, preStrLen, preStrLen + colorStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }


    /**
     * 为文本增加点击事件
     * @param str 原始文本
     * @return
     */
    public static SpannableString getClickSpanString(String str) {
        SpannableString spanString = new SpannableString(str);
        ClickableSpan span = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                // TODO Auto-generated method stub

            }
        };

        spanString.setSpan(span, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }


    /**
     * 为文本增加删除线
     * @param str 原始文本
     * @return
     */
    public static SpannableString getStrikeSpanString(String str) {
        SpannableString spanString = new SpannableString(str);
        StrikethroughSpan span = new StrikethroughSpan();
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }


    /**
     * 为文本增加下划线
     * @param str 原始文本
     * @return
     */
    public static SpannableString getUnderlineStr(String str) {
        SpannableString spanString = new SpannableString(str);
        UnderlineSpan span = new UnderlineSpan();
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }


    /**
     * 将文本变成粗体
     * @param str 原始文本
     * @return
     */
    public static SpannableString getBoldStr(String str) {
        SpannableString spanString = new SpannableString(str);
        StyleSpan span = new StyleSpan(Typeface.BOLD);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }

    /**
     * 将文本变成斜体
     * @param str 原始文本
     * @return
     */
    public static SpannableString getItalicStr(String str) {
        SpannableString spanString = new SpannableString(str);
        StyleSpan span = new StyleSpan(Typeface.ITALIC);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }


    /**
     * 为文本增加超链接
     * @param str 原始文本
     * @return
     */
    public static SpannableString getUrlStr(String str) {
        SpannableString spanString = new SpannableString(str);
        URLSpan span = new URLSpan(str);
        spanString.setSpan(span, 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanString;
    }

    /**
     * 将小写的数字转化成 大写的数字 isSearchNum 为 true 时，10-->一十 ,11--> 一十一 isSearchNum 为
     * false 时，10 -->十 ，11-->十一
     *
     * @param mathNum
     * @param isSearchNum
     * @return
     */
    public static String mathNum2ChineseNum(int mathNum, boolean isSearchNum) {
        String result = "";
        if (mathNum < 0) {
            result = "负";
            mathNum = Math.abs(mathNum);
        }
        String t = String.valueOf(mathNum);
        for (int i = t.length() - 1; i >= 0; i--) {
            int r = (int) (mathNum / Math.pow(10, i));
            if (r % 10 != 0) {
                String s = String.valueOf(r);
                String l = s.substring(s.length() - 1);
                result += nums[Integer.parseInt(l) - 1];
                result += (units[i]);
            } else {
                if (!result.endsWith("零")) {
                    result += "零";
                }
            }
        }

        if (result.endsWith("零") && result.length() > 1) // 如果最后一位为0，去掉转化后的最后一位零
        {
            result = result.substring(0, result.length() - 1);
        }

        if (mathNum > 9 && mathNum < 20 && !isSearchNum) // 10-19的数字，转化的时候去掉最前面的一，如10对应十，18对应十八
        {
            result = result.substring(1);
        }
        return result;
    }

    public static int dataFromString(String value) {
        int resule = 0;
        if (TextUtils.isEmpty(value)) {
            return resule;
        }
        if (null != value) {
            resule = Integer.valueOf(value);
        }
        return resule;
    }

    /**
     * 判断指定字符是否为英文字母
     * @param c
     * @return
     */
    public static boolean isEnglishLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }


    /**
     * 非法字符串校验
     * @param normalString
     * @return
     */
    static Pattern patternValidString = Pattern
            .compile("^[a-zA-Z0-9_\\*\\s*\\u4e00-\\u9fa5]*");
    public static boolean isValidString(String normalString) {
        if (TextUtils.isEmpty(normalString)) {
            return false;
        }

        Matcher matchernick = patternValidString.matcher(normalString);
        return matchernick.matches();
    }


    /**
     * 邮箱格式校验
     * @param email
     * @return
     */
    static Pattern patternEmail = Pattern
            .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
    public static boolean isValidMail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        Matcher matchernick = patternEmail.matcher(email);
        return matchernick.matches();
    }


    /**
     * 将json数组转化成字符串，以逗号隔开
     * @param jsonArray
     * @return
     */
    public static String jsonArrayToStr(List<String> jsonArray) {
        if (jsonArray == null || jsonArray.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        //Gson gson=new Gson();


        //Type type = new TypeToken<ArrayList<String>>(){}.getType();
        //ArrayList<String> list = gson.fromJson(jsonArray, type);

        for (int i = 0; i < jsonArray.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(jsonArray.get(i));
        }

        return sb.toString();
    }


    /**
     * 将以逗号隔开的字符串转化成json数组
     * @param str
     * @return
     */

    public static List<String> strToJsonArray(String str) {
        ArrayList<String> list = new ArrayList<String>();
        if (TextUtils.isEmpty(str)) {
            return list;
        }

        String[] array = str.split(",");
        for (String tmp : array) {
            list.add(tmp);
        }

        return list;
        //String jsonArray = gson.toJson(list);
        //return jsonArray;
    }


    /**
     * 将字符串转化成int，如果空则返回0
     * @param str 待转化的字符串
     * @return
     */
    public static int toInt(String str) {
        int re = 0;
        if (str != null) {
            str = str.trim();
        }
        try {
            if (TextUtils.isEmpty(str) || "null".equals(str) || "空".equals(str)) {
                return re;
            } else {
                return Integer.valueOf(str);
            }
        } catch (Exception e) {
            return re;
        }
    }

    /**
     * 判断字符串是否为0，空或null亦为true
     * @param value
     * @return
     */
    public static boolean isZeroValue(String value) {
        if (TextUtils.isEmpty(value)) {
            return true;
        } else {
            return "null".equals(value) || "0".equals(value);
        }
    }

    // 3.随机获取一个6位数的整数，要求数字不重复，不含0
    public static int getRandom6() {
        int returnNum = 0;
        //自定义有序数
        int[] ra = new int[9];
        for (int i = 0; i < 9; i++) {
            ra[i] = i + 1;
        }
        //无序排列并取值
        for (int i = 0; i < 6; i++) {
            returnNum *= 10;
            Random rd = new Random();
            int temp1 = rd.nextInt(9 - i);
            int temp2 = ra[8 - i];//保存相对末尾的数据
            ra[8 - i] = ra[temp1];//交换
            ra[temp1] = temp2;
            returnNum += ra[8 - i];//取值
        }
        return returnNum;
    }

    /**
     * 手机号中间的四位用*号填充
     * @param phone
     * @return
     */
    public static String getPhoneNumberAnony(String phone) {
        String result = phone;
        if (PhoneInfoTools.isMobilePhone(phone)) {
            result = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        }

        return result;
    }

    public static String IDCardValidate(String IDStr) throws ParseException {
        String errorInfo = "";// 记录错误信息
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            errorInfo = "身份证号码长度应该为15位或18位。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 数字 除最后以外都为数字 ================
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            errorInfo = "身份证15位号码都应为数字; 18位号码除最后一位外，都应为数字。";
            return errorInfo;
        }
        // =======================(end)========================

        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            errorInfo = "身份证地区编码错误。";
            return errorInfo;
        }

        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
            errorInfo = "身份证生日无效。";
            return errorInfo;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                || (gc.getTime().getTime() - s.parse(
                strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
            errorInfo = "身份证生日不在有效范围。";
            return errorInfo;
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            errorInfo = "身份证月份无效";
            return errorInfo;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            errorInfo = "身份证日期无效";
            return errorInfo;
        }
        // =====================(end)=====================

        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                errorInfo = "身份证无效，不是合法的身份证号码";
                return errorInfo;
            }
        } else {
            return "";
        }
        // =====================(end)=====================
        return "";
    }

    static Pattern patternNumeric = Pattern.compile("[0-9]*");
    private static boolean isNumeric(String str) {
        Matcher isNum = patternNumeric.matcher(str);
        return isNum.matches();
    }


    /**
     * 功能：设置地区编码
     *
     * @return Hashtable 对象
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 验证日期字符串是否是YYYY-MM-DD格式
     *
     * @param str
     * @return
     */
    public static boolean isDataFormat(String str) {
        boolean flag = false;
        //String regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
        String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
        Pattern pattern1 = Pattern.compile(regxStr);
        Matcher isNo = pattern1.matcher(str);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }

    /***
     * 毫秒转化
     */
    public static String formatTime(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;

        //若不足1天显示“X小时” 不足1小时显示“X分钟”
        if (day < 1) {
            if (hour < 1) {
                return minute + "分钟";
            } else {
                return hour + "小时";
            }
        } else {
            return day + "天";
        }
    }

    public static String formatTime2(long ms) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        //小时
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        //分钟
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        //秒
        String strSecond = second < 10 ? "0" + second : "" + second;

        return strHour + ":" + strMinute + ":" + strSecond;
    }

    /**
     * 获取url中对应参数值
     * @param url
     * @param name
     * @return
     */
    public static String getValueByName(String url, String name) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(name)) {
                result = str.replace(name + "=", "");
                break;
            }
        }
        return result;
    }

    /**
     * 修改url参数值
     * @param url
     * @param key
     * @param value
     * @return
     */
    public static String replace(String url, String key, String value) {
        if (!TextUtils.isEmpty(url) && !TextUtils.isEmpty(key)) {
            url = url.replaceAll("(" + key + "=[^&]*)", key + "=" + value);
        }
        return url;
    }

    /**
     * 获取拼接的cntName+"/"+chapterName;
     * @param cntName
     * @return
     */
    public static String getSuffCntName(String cntName) {
        String result = "";
        if (!TextUtils.isEmpty(cntName)) {
            int index = cntName.indexOf("/");
            if (-1 != index) {
                result = cntName.substring(0, index);
            } else {
                result = cntName;
            }
        }
        return result;
    }

    /**
     * 获取拼接的cntName+"/"+chapterName;
     * @param cntName
     * @return
     */
    public static String getSuffChapterName(String cntName) {
        String result = "";
        if (!TextUtils.isEmpty(cntName)) {
            int index = cntName.lastIndexOf("/");
            if (-1 != index) {
                result = cntName.substring(index + 1);
            } else {
                result = cntName;
            }
        }
        return result;
    }

    /**
     * 字符串中是否包含了汉字
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        boolean flag = false;
        Matcher m = g_chinesePattern.matcher(str);
        if (m.find()) {
            flag = true;
        }

        return flag;
    }

    /**
     * 字符串中是否包含了英文字母
     * @param str
     * @return
     */
    public static boolean isContainEnglish(String str) {
        boolean flag = false;
        Matcher m = g_englishPattern.matcher(str);
        if (m.find()) {
            flag = true;
        }

        return flag;
    }


    /**
     * 字符串中是否包含了章节标题头
     * @param str
     * @return
     */
    public static boolean isContainTitle(String str) {
        boolean flag = false;
        if (g_titlePattern == null) {
            g_titlePattern = Pattern.compile("(第([0-9零一二三四五六七八九十百千万]+)[章节回集卷篇幕段])|(.*\\" +
                    "([0-9零一二三四五六七八九十百千万]+\\))");
        }
        Matcher m = g_titlePattern.matcher(str);
        if (m.find()) {
            flag = true;
        }

        return flag;
    }

    /**
     * 获取本地电台attributes
     * @return
     */
    public static HashMap<String, String> getFmLocalMap() {
        HashMap<String, String> localMap = new HashMap<>();
        localMap.put("0", "");//全国
        localMap.put("11", "3");//北京
        localMap.put("31", "83");//上海
        localMap.put("13", "5");//天津
        localMap.put("83", "257");//重庆
        localMap.put("51", "217");//广东
        localMap.put("36", "99");//浙江
        localMap.put("34", "85");//江苏
        localMap.put("74", "202");//湖南
        localMap.put("81", "259");//四川
        localMap.put("19", "19");//山西
        localMap.put("76", "169");//河南
        localMap.put("71", "187");//湖北
        localMap.put("97", "69");//黑龙江
        localMap.put("91", "44");//辽宁
        localMap.put("18", "7");//河北
        localMap.put("17", "151");//山东
        localMap.put("30", "111");//安徽
        localMap.put("38", "129");//福建
        localMap.put("59", "239");//广西
        localMap.put("85", "281");//贵州
        localMap.put("86", "291");//云南
        localMap.put("75", "139");//江西
        localMap.put("90", "59");//吉林
        localMap.put("87", "327");//甘肃
        localMap.put("88", "351");//宁夏
        localMap.put("10", "31");//内蒙古
        localMap.put("50", "254");//海南
        localMap.put("79", "308");//西藏
        localMap.put("70", "342");//青海
        localMap.put("89", "357");//新疆
        localMap.put("84", "316");//陕西
        return localMap;
    }

    public static String getTimeString(String noSlipTime14) {
        String year = noSlipTime14.substring(0, 4);
        String month = noSlipTime14.substring(4, 6);
        String day = noSlipTime14.substring(6, 8);
        String min = noSlipTime14.substring(8, 10);
        String seconds = noSlipTime14.substring(10, 12);
        return year + "-" + delZero(month) + "-" + delZero(day) + "-" + min + "-" + seconds;
    }

    public static String delZero(String string) {
        if (string.startsWith("0")) {
            return string.substring(1);
        }
        return string;
    }

    public static String translateLable2Char(String content) {
        return content.replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("\'", "&apos;");
    }

    public static String translateChar2Lable(String content) {
        return content.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&apos;", "\'");
    }

    /**
     * 英文双引号改成中文双引号
     * @param content
     * @return
     */
    public static String processQuotationMarks(String content) {
        String regex = "\"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        String reCT = content;

        while (matcher.find()) {
            String itemMatch = "“" + matcher.group(1) + "”";
            reCT = reCT.replaceAll("\"" + matcher.group(1) + "\"", itemMatch);
        }

        return reCT;
    }

    /*【通用标准】内容的单位和动词标准 start*/

    /**
     * 字符串类型转int型
     * @param cntType
     * @return
     */
    private static int getIntegerValue(String cntType) {
        int value = 0;
        try {
            value = Integer.valueOf(cntType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  文字 长10 个字打点
     * @param str
     * @return
     */
    public static String getTenLenthStr(String str) {
        if (!StringUtil.isEmptyString(str)) {
            if (str.length() > 10) {
                str = str.substring(0, 10);
                str = str + "...";
            }
        }
        return str;
    }

    /**
     * 获取实际长度，中文计算为2，字母符号计算为1
     * @param value 字符串
     * @return
     */
    public static int getRealLength(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        int valueLength = 0;
        for (char c : value.toCharArray()) {
            valueLength += ((c >= 0x4E00 && c <= 0x9FA5) ? 2 : 1);
        }
        return valueLength;
    }

    /**
     * 获取
     * @param value 字符串
     * @param strLength 字符长度（在英文为1个长度，中文为2个长度，下的角标）
     *                    如 abc   字符长度为1，取1
     *                    如 啊波次 字符长度为1，取0
     *                    如 a啊b波 字符长度为1，取1
     *                    如 啊a波b 字符长度为1，取0
     * @return 角标
     */
    public static int getIndexByLength(String value, int strLength) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        int i = 0;
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        char[] chars = value.toCharArray();
        for (int valueLength = 0; i < chars.length && valueLength < strLength * 2; i++) {
            char c = chars[i];
            valueLength += ((c >= 0x4E00 && c <= 0x9FA5) ? 2 : 1);
        }
        return i;
    }

    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;// 有一个中文字符就返回
            }
        }
        return false;
    }

    public static boolean isTtsSplitChar(char a){
        return a == '。' || a == '，' ||  a == ';'||  a == '：'||  a == '？'||  a == '！'||  a == '!'||  a == '.'||  a == ','||  a == '?';
    }

    public static boolean isTtsCharTag(char c){
        return c == ',' || c == '，' || c == '。' || c == '？'
                || c == '?' || c == '!' || c == '！' || c == '—'
                || c == '.' || c == '-'|| c == '”'|| c == '"'
                || c == ':'|| c == '“';
    }


    public static String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto!important;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }
}
