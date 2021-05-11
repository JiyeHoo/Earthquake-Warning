package com.jiyehoo.informationentry.util;

import android.icu.text.SimpleDateFormat;

import java.util.Locale;

public class TimeUtil {
    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(long stamp) {
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(stamp * 1000);
    }

    /**
     * 将时间转换为时间戳
     */
    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return String.valueOf(sdf.parse(date).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
}
