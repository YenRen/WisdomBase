package com.hnxx.wisdombase.ui.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 一秒(单位：毫秒)
     */
    public static final long ONE_SECOND = 1000;

    /**
     * 一分钟(单位：毫秒)
     */
    public static final long ONE_MINUTE = 60000;

    /**
     * 一小时(单位：毫秒)
     */
    public static final long ONE_HOUR = 3600000;

    /**
     * 一天(单位：毫秒)
     */
    public static final long ONE_DAY = 86400000;

    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;
    public static final String SDF_YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static Date string2Date(SimpleDateFormat sdf, String stringDate) {
        Date sendTime = null;
        try {
            sendTime = sdf.parse(stringDate);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        return sendTime;
    }

    /**
     * 获取时间的年月日
     *
     * @param stringDate
     * @return
     */
    public static Date String2DateyyyyMMddHHmmss(String stringDate) {
        Date re = null;
        try {
            re = string2Date(new SimpleDateFormat("yyyyMMddHHmmss"), stringDate);
        }catch (Exception e){
            return null;
        }
        return re;
    }

    public static Date String2Date4OnLineRead(String stringDate) {
        Date re = null;

        re = string2Date(new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss"),
                stringDate);
        return re;
    }

    public static Date String2Date(String stringDate) {
        Date re = null;

        re = string2Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                stringDate);
        return re;
    }

    public static Date String2DateyyyyMMdd(String stringDate) {
        Date re = null;

        re = string2Date(new SimpleDateFormat("yyyyMMdd"), stringDate);
        return re;
    }

    public static Date String2Dateyyyy_MM_dd(String stringDate) {
        Date re = null;

        re = string2Date(new SimpleDateFormat("yyyy-MM-dd"), stringDate);
        return re;
    }

    /**
     * 将时间转换为yyyy.MM.dd格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringdDot(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy.MM.dd").format(dateTime);
        }
        return re;
    }
    /**
     * 获取时间的年月日
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringYY_MM_DD(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
        }
        return re;
    }
    /**
     * 获取时间的年月日
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringYY_MM_DDChiness(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy年MM月dd日").format(dateTime);
        }
        return re;
    }

    public static String date2String(Date date, String simpleDateFormat) {
        String result = "";
        if (date != null) {
            result = new SimpleDateFormat(simpleDateFormat).format(date);
        }

        return result;
    }


    /**
     * 获取时间的年月日
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringYYMMDD(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyyMMdd").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为yyyyMMddHHmmss格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringyyyyMMddHHmmss(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyyMMddHHmmss").format(dateTime);
        }
        return re;
    }

    /**
     * ddHHmmssSSS
     *
     * @param dateTime
     * @return
     */
    public static String date2StringddHHmmssSSS(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("ddHHmmssSSS").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为ddHHmmss格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringddHHmmss(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("ddHHmmss").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为yyyyMMddHHmmss格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringyyyyMM(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyyMM").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为MM月dd日格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringMMdd(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("MM月dd日").format(dateTime);
        }
        return re;
    }


    /** 将yyyyMMddHHmmss格式的字符串转为yyyy-MM-dd HH:mm:ss
     *
     * @param stringDate
     * @return */
    public static Date Date2StringyyyyMMddHHmmss(String stringDate)
    {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);

        return dateTime;
    }

    /**
     * 将yyyyMMddHHmmss格式的字符串转为yyyy-MM-dd HH:mm:ss
     *
     * @param stringDate
     * @return
     */
    public static String Date2StringyyyyMMddHHmmss2Normal(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        re = Date2String(dateTime);
        return re;
    }

    /**
     * 将yyyyMMddHHmmss格式的字符串转为yyyy-MM-dd HH:mm:ss
     *
     * @param stringDate
     * @return
     */
    public static String Date2StringyyyyMMddHHmmss2hhmm(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        re = Date2String4OnLineReadMMDDHHMM(dateTime);

        return re;
    }


    public static String Date2StringyyyyMMddHHmmss2MMddHHmm(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);


        re = Date2String4OnLineReadMMDDHHMM(dateTime);
        return re;
    }

    public static String Date2StringyyMMddHHmm2Normal(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmm");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        re = new SimpleDateFormat("yy-MM-dd HH:mm").format(dateTime);
        return re;
    }

    public static String Date2StringyyyyMMddHHmm2Normal(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        re = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(dateTime);
        return re;
    }

    public static String Date2StringyyyyMMddHHmm2Chinese(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        re = new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(dateTime);
        return re;
    }

    public static String Date2StringyyyyMM2Chinese(String stringDate) {
        if(TextUtils.isEmpty(stringDate)){
            return "";
        }
        String re = null;

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            Date dateTime = string2Date(simpleDateFormat, stringDate);
            re = new SimpleDateFormat("yyyy年MM月dd日").format(dateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    public static String Date2String2ymd(String stringDate) {
        if(stringDate.length()<8) {
            return stringDate;
        }
        if(stringDate.length()>8){
            stringDate = stringDate.substring(0,8);
        }
        stringDate = stringDate.substring(0,8);

        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMdd");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        if(null != dateTime){
            re = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
        }
        if(TextUtils.isEmpty(re)){
            re = stringDate;
        }
        return re;
    }

    public static String Date2StringyyyyMMdd2Normal(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        if(null != dateTime){
            re = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
        }
        if(TextUtils.isEmpty(re)){
            re = stringDate;
        }
        return re;
    }

    public static String Date2StringyyyyMMdd(String stringDate) {
        String re = null;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyyMMdd");

        Date dateTime = string2Date(simpleDateFormat, stringDate);
        if(null != dateTime){
            re = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
        }
        if(TextUtils.isEmpty(re)){
            re = stringDate;
        }
        return re;
    }

    public static String Date2StringYMD(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy-MM-dd").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2String(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2String2h(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy-MM-dd HH").format(dateTime);
        }
        return re;
    }

    /**
     * 将时间转换为格式的字符串
     *
     * @param dateTime
     * @return
     */
    public static String Date2Stringhhmm(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
        }
        return re;
    }

    public static String Date2String4OnLineRead(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime);
        }
        return re;
    }


    public static String Date2String4OnLineReadMMDDHHMM(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("MM-dd HH:mm").format(dateTime);
        }
        return re;
    }

    /**
     * 获取时间的小时和分
     *
     * @param dateTime
     * @return
     */

    public static String Date2StringHHMM(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("HH:mm").format(dateTime);
        }

        return re;
    }

    /**
     * 获取时间的小时和分
     *
     * @param dateTime
     * @return
     */
    public static String Date2StringyyMMddHHmmss(Date dateTime) {
        String re = null;
        if (dateTime != null) {
            re = new SimpleDateFormat("yyMMddHHmmss").format(dateTime);
        }

        return re;
    }

    public static boolean isToday(Date time) {
        boolean re = false;
        String toDayString = Date2StringYY_MM_DD(new Date());
        String timeString = Date2StringYY_MM_DD(time);
        if (toDayString.equals(timeString)) {
            re = true;
        }

        return re;
    }

    /**
     * 计算从现在到指定日期的天数
     */
    public static long afterTodayDays(Date futureTime) {
        Date now = new Date();
        long lFuture = futureTime.getTime();
        long lNow = now.getTime();

        if (lFuture < lNow) {
            return 0;
        }

        long diffDays = (lFuture - lNow) / (ONE_DAY);

        return diffDays;
    }

    /*
     * 计算两个时间相差的天数
     * @param time1 时间1
     * @param time2 时间2
     * @return 两个时间相差的天数
     */
    public static double dayAmount(long time1, long time2) {
        long timeAmount = Math.abs(time1 - time2);
        double dayAmount = timeAmount / ONE_DAY;

        return dayAmount;
    }

    /**
     * 计算两个时间相差的小时数
     *
     * @param time1
     * @param time2
     * @return 两个时间相差的小时数
     */
    public static double hourAmount(long time1, long time2) {
        long timeAmount = Math.abs(time1 - time2);
        double hourAmount = timeAmount / ONE_HOUR;

        return hourAmount;
    }

    /**
     * 计划两个时间差的秒数
     * @param time1
     * @param time2
     * @return
     */
    public static long secondAmount(long time1, long time2) {
        long timeAmount = Math.abs(time1 - time2);

        return timeAmount / 1000;
    }

    /**
     * 返回距指定时间友好的逝去时间，
     *
     * @param lastTime 上次时间
     * @return 例如"1分钟前", "1小时前"等
     */
    public static String getFriendlyPassTime(long lastTime) {
        StringBuilder sb = new StringBuilder();

        long nowTime = System.currentTimeMillis();
        long intervalTime = nowTime - lastTime;
        long intervalDay = intervalTime / (ONE_DAY);
        long intervalhour = (intervalTime % (ONE_DAY)) / (ONE_HOUR);
        long intervalminute = ((intervalTime % (ONE_DAY)) % (ONE_HOUR))
                / (ONE_MINUTE);

        if (intervalDay > 0) {
            sb.append(intervalDay);
            sb.append("天");
        }

        if (intervalhour > 0) {
            sb.append(intervalhour);
            sb.append("小时");
        }

        if (intervalminute > 0) {
            sb.append(intervalminute);
            sb.append("分钟");
        }

        if (sb.length() == 0) {
            sb.append("1分钟前");
        } else {
            sb.append("前");
        }

        return sb.toString();
    }

    public static long getCurrentTimeAsLong() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * 保留11位
     *
     * @return
     */
    public static long getCurrentTimeAsLong2() {
        String time1 = String.valueOf(System.currentTimeMillis());
        if (time1.length() > 11) {
            time1 = time1.substring(0, 11);
        }

        return Long.parseLong(time1);
    }

    /**
     * 将给定的日期long型转成一个只保留前11位的long型
     *
     * @return
     */
    public static long getTimeAsLong2(long timeMillis) {
        String time1 = String.valueOf(timeMillis);
        if (time1.length() > 11) {
            time1 = time1.substring(0, 11);
        }

        return Long.parseLong(time1);
    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return 如果true表示在同一周
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isSameWeek(String date1, String date2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(date1);
            d2 = format.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(d1);
        cal2.setTime(d2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        // subYear==0,说明是同一年
        if (subYear == 0) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR);
        }
        // 例子:cal1是"2005-1-1"，cal2是"2004-12-25"
        // java对"2004-12-25"处理成第52周
        // "2004-12-26"它处理成了第1周，和"2005-1-1"相同了
        // 大家可以查一下自己的日历
        // 处理的比较好
        // 说明:java的一月用"0"标识，那么12月用"11"
        else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR);
        }
        // 例子:cal1是"2004-12-31"，cal2是"2005-1-1"
        else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11) {
            return cal1.get(Calendar.WEEK_OF_YEAR) == cal2
                    .get(Calendar.WEEK_OF_YEAR);

        }
        return false;
    }

    public static String fromTodaySimple(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long one_hour = ONE_HOUR / 1000;
        long one_mimute = ONE_MINUTE / 1000;
        long one_day = ONE_DAY / 1000;
        long time = date.getTime() / 1000;
        long now = System.currentTimeMillis() / 1000;
        long ago = now - time;
        if (ago <= one_hour) {
            if(ago / one_mimute<=0){
                return "刚刚";
            }
            return ago / one_mimute + "分钟前";
        }
        else if (ago <= one_day) {
            return ago / one_hour + "小时前";
        }
        else if (ago <= one_day * 2) {
            return "昨天";
        }
        else if (ago <= one_day * 3) {
            return "前天";
        }
        else if (ago <= ONE_MONTH) {
            long day = ago / one_day;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / one_day;
            return month + "个月前";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
            // so month+1
            return year + "年前";
        }
    }

    public static String fromToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long one_hour = ONE_HOUR / 1000;
        long one_mimute = ONE_MINUTE / 1000;
        long one_day = ONE_DAY / 1000;
        long time = date.getTime() / 1000;
        long now = System.currentTimeMillis() / 1000;
        long ago = now - time;
        if (ago <= one_hour) {
            long tMinAgo = ago / one_mimute;
            if (tMinAgo == 0){
                return "刚刚";
            }
            return tMinAgo + "分钟前";
        }
        else if (ago <= one_day) {
            return ago / one_hour + "小时" + (ago % one_hour / one_mimute)
                    + "分钟前";
        }
        else if (ago <= one_day * 2) {
            return "昨天";
        }
        else if (ago <= one_day * 3) {
            return "前天";
        }
        else if (ago <= ONE_MONTH) {
            long day = ago / one_day;
            return day + "天前";
        } else if (ago <= ONE_YEAR) {
            long month = ago / ONE_MONTH;
            long day = ago % ONE_MONTH / one_day;
            return month + "个月前";
        } else {
            long year = ago / ONE_YEAR;
            int month = calendar.get(Calendar.MONTH) + 1;// JANUARY which is 0
            // so month+1
            return year + "年前";
        }
    }

    /**
     * 根据日期，判断是一天内、一周内、一月内、还是一月以前
     *
     * @param timeMilis
     * @return
     */
    public static String getDateSortTag(long timeMilis) {
        double dayAmount = DateUtil.dayAmount(timeMilis,
                System.currentTimeMillis());
        if (dayAmount > 30) {
            return "一月以前";
        } else if (dayAmount > 7) {
            return "一月内";
        } else if (dayAmount > 1) {
            return "一周内";
        } else {
            return "一天内";
        }
    }

    /**
     * @param timeMillis 毫秒数
     * @return 转换为mm:ss的字符串格式
     */
    public static String formatTimeMMSS(long timeMillis) {
        String result = "";
        String minuteString = "";
        String secondString = "";
        if (timeMillis < 1000) {
            return result;
        } else {
            long secondTotal = timeMillis / 1000; // 得到总共秒数
            int minutes = (int) (secondTotal / 60); // 得到分钟数
            int second = (int) (secondTotal % 60); // 得到秒数
            if (minutes < 10) {
                minuteString = "0" + minutes;
            } else {
                minuteString = "" + minutes;
            }

            if (second < 10) {
                secondString = "0" + second;
            } else {
                secondString = "" + second;
            }
            result = minuteString + ":" + secondString;
        }

        return result;
    }

    // long转换为格式化String类型
    // currentTime要转换的long类型的时间
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
    public static String longToDate(long time, String formatType) {
        Date dateOld = new Date(time); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = date2String(dateOld, formatType); // 把date类型的时间转换为string

        return sDateTime;
    }


    public static Date getNowDate() {
        return Calendar.getInstance().getTime();
    }

    /**
     * 比较日期，如果date1大于date2返回1，如果date1小于date2返回-1，相等返回0
     * @param date1 日期格式：yyyyMMdd
     * @param date2 日期格式：yyyyMMdd
     * @return
     */
    public static int compareDate(String date1, String date2) {
        try {
            Date dt1 = String2DateyyyyMMdd(date1);
            Date dt2 = String2DateyyyyMMdd(date2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getContinuedTime(long createTime){
        String result = "";
        if(TextUtils.isEmpty(String.valueOf(createTime))){
            return result;
        }
        long nowTime = getNowDate().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(createTime > nowTime){
            result = "录制于："+format.format(new Date(createTime));
            return result;
        }

        Date createDate = new Date(createTime);
        result = fromToday(createDate);
        return result;
    }

    public static String afterToday(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = format.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            long one_day = ONE_DAY / 1000;
            long time = date.getTime() / 1000;
            long now = System.currentTimeMillis() / 1000;
            long ago = time-now;

            if(ago > 0){
                if (ago >= one_day && ago <= one_day*8) {
                    return ago/one_day+"天后到期";
                }
                else if (ago <= one_day) {
                    return "今天到期";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 毫秒值转分钟
     *
     * @param millis
     * @return
     */
    public static int timeMillis2Minute(int millis){
        return millis/1000/60;
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * 是否为当天有效时间
     * @param date
     * @return
     */
    public static boolean isValidToday(Date date){
        boolean result = false;
        Calendar nowCal = Calendar.getInstance();
        nowCal.setTime(new Date());
        Calendar comCal = Calendar.getInstance();
        comCal.setTime(date);
//        Log.d("newReadTimeStat","isValidToday == "+nowCal.getTimeInMillis()+" , "+comCal.getTimeInMillis());
        if(isToday(date) && comCal.before(nowCal)){
            result = true;
        }
        return result;
    }

    /**
     * ms转分钟
     * @param ms
     * @return
     */
    public static int msParseMunite(long ms){
        if(ms != 0){
            return (int) (ms/1000/60);
        }
        return (int) ms;
    }

    /**
     * s转分钟
     * @param ms
     * @return
     */
    public static int sParseMunite(long ms){
        if(ms != 0){
            return (int) (ms/60);
        }
        return (int) ms;
    }

    /**
     * 秒转毫秒
     * @param s
     * @return
     */
    public static long sParseMs(long s){
        if(s != 0){
            return s*1000;
        }
        return s;
    }

    /**
     * 获取原始时间戳叠加
     * @param origStamp
     * @param readTime
     * @return
     */
//    public static String obtainOrigStamp(String origStamp,long readTime){
//        if(TextUtils.isEmpty(origStamp)){
//            origStamp = resetStamp();
//        }
//        String stamp = AESUtil.decrypt(origStamp, AESCryptor.addtimeEncryptCode);
//        if(!TextUtils.isEmpty(stamp)){
//            String[] splite = stamp.split("&&");
//            if(null != splite && splite.length == 2){
//                long overlay = Long.valueOf(splite[1])+readTime;
//                stamp = splite[0] + "&&" + overlay;
//            }
//        }
//        return AESUtil.encryptAES(stamp, AESCryptor.addtimeEncryptCode);
//    }

    /**
     * 初始化时间戳
     * @return
     */
//    public static String resetStamp(){
//        String stamp = DateUtil.getCurrentTimeAsLong()+"&&"+"0";
//        return AESUtil.encryptAES(stamp, AESCryptor.addtimeEncryptCode);
//    }

    /**
     * 生成特定时间戳
     * @return
     */
//    public static String createSpeStamp(long readTime){
//        String stamp = DateUtil.getCurrentTimeAsLong()+"&&"+ readTime;
//        return AESUtil.encryptAES(stamp, AESCryptor.addtimeEncryptCode);
//    }

    /**
     * 从时间戳中获取阅读时长
     * @param stampEncry：加密时间戳
     * @return
     */
//    public static long getReadTimeFormStamp(String stampEncry){
//        long readTime = 0;
//        if(TextUtils.isEmpty(stampEncry)){
//            stampEncry = resetStamp();
//        }
//        String stamp = AESUtil.decrypt(stampEncry, AESCryptor.addtimeEncryptCode);
//        if(!TextUtils.isEmpty(stamp)){
//            String[] splite = stamp.split("&&");
//            if(null != splite && splite.length == 2){
//                readTime = Long.valueOf(splite[1]);
//            }
//        }
//        return readTime;
//    }

    /**
     * 从时间戳中获取时间戳
     * @param stampEncry：加密时间戳
     * @return
     */
//    public static long getPrefixFormStamp(String stampEncry){
//        long prefix = 0;
//        if(TextUtils.isEmpty(stampEncry)){
//            stampEncry = resetStamp();
//        }
//        String stamp = AESUtil.decrypt(stampEncry, AESCryptor.addtimeEncryptCode);
//        if(!TextUtils.isEmpty(stamp)){
//            String[] splite = stamp.split("&&");
//            if(null != splite && splite.length == 2){
//                prefix = Long.valueOf(splite[0]);
//            }
//        }
//        return prefix;
//    }

    /**
     * 毫秒值转分钟
     * @param millisecond
     * @return
     */
    public static long millisecond2Minute(long millisecond){
        return millisecond/1000/60;
    }
}
