package com.jiyehoo.informationentry.util;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用来存储 homeId
 */
public enum HomeModel {
    INSTANCE;

    public static final String CURRENT_HOME_ID = "homeId";

    public final void setHomeId(Context context, long homeId) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putLong(CURRENT_HOME_ID, homeId);
        editor.apply();
    }

    public static long getHomeId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        return sp.getLong(CURRENT_HOME_ID, 0);
    }

    /**
     * check if current home set
     */
//    public final boolean checkHomeId(Context context) {
//        return getCurrentHome(context) != 0L;
//    }

    public final void clearHomeId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(CURRENT_HOME_ID);
        editor.apply();
    }
}

