package com.jiudi.shopping.util;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 作者：maqing on 2016/10/21 16:30
 * 邮箱：2856992713@qq.com
 */
public class DateTimeUtil {

    /**
     * 时间日期格式化到年月日时分秒.
     */
    public static final String FORMAT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间日期格式化到年月日
     */
    public static final String FORMAT_YMD = "yyyy-MM-dd";
    /**
     * 时间日期格式化到年月
     */
    public static final String FORMAT_YM = "yyyy-MM";
    /**
     * 时间日期格式化到年月日时分
     */
    public static final String FORMAT_YMD_HM = "yyyy-MM-dd HH:mm";

    /**
     * 时间日期格式化到月日
     */
    public static final String FORMAT_MD = "MM/dd";
    /**
     * 时分秒
     */
    public static final String FORMAT_HMS = "HH:mm:ss";
    /**
     * 时分
     */
    public static final String FORMAT_HM = "HH:mm";
    /**
     * 上午
     */
    public static final String AM = "AM";
    /**
     * 下午
     */
    public static final String PM = "PM";

    /**
     * 获取当前日期时间
     *
     * @param format 所需日期时间格式
     * @return TimeStr
     */
    public static String getCurrentDate(String format) {
        String curDateTime = null;
        try {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(format);
            Calendar c = new GregorianCalendar();
            curDateTime = mSimpleDateFormat.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curDateTime;
    }

    /**
     * 比较时间先后
     *
     * @param data1  data1
     * @param data2  data2
     * @param format format
     * @return 1:data1大于data2 -1:data1小于data2
     */
    public static int compareTime(String data1, String data2, String format) {
        DateFormat df = new SimpleDateFormat(format);
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = df.parse(data1);
            dt2 = df.parse(data2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1 != null && dt2 != null) {
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            }
        }
        return 0;
    }


    public static String timeFormat(long timeMillis, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, FORMAT_YMD);
    }

    public static String formatPhotoDate(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：xx天xx小时xx分xx秒
     */
    public static String[] getDistanceTime(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            day = diff / (24 * 60 * 60 * 1000);
            hour = (diff / (60 * 60 * 1000) - day * 24);
            min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            sec = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timeIntervals[] = new String[]{day + "", hour + "", min + "", sec + ""};
//        return day + "天" + hour + "小时" + min + "分" + sec + "秒";
        return timeIntervals;
    }

    /**
     * 两个时间相差距离秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00:00
     * @return String 返回值为：秒
     */
    public static long getDistanceTimeSeconds(String str1, String str2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date one;
        Date two;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            sec = diff / (1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sec;
    }
}

