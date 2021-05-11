package com.jiyehoo.informationentry.util;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    private static final String TAG = "###TimeUtil";
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

    public static String dateToStamp(String dateStr) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = simpleDateFormat.parse(dateStr);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 字符串转 date
     */
    public static Date strToDate(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (Exception e) {
            Log.d(TAG, "Str 转 date 失败:" + e);
        }
        return null;
    }
}
