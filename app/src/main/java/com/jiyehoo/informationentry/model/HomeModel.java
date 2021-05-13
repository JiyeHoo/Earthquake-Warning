package com.jiyehoo.informationentry.model;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author JiyeHoo
 * @description: home 的储存：homeId、经纬度
 */
public enum HomeModel {
    INSTANCE;

    public static final String CURRENT_HOME_ID = "homeId";
    public static final String GPS_LON = "lon";
    public static final String GPS_LAT = "lat";

    public final void setHomeId(Context context, long homeId) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putLong(CURRENT_HOME_ID, homeId);
        editor.apply();
    }

    public static long getHomeId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        return sp.getLong(CURRENT_HOME_ID, -1);
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

    public final void setLon(Context context, double lon) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String lonStr = String.valueOf(lon);
        editor.putString(GPS_LON, lonStr);
        editor.apply();
    }

    public static double getLon(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        double  lon = Double.parseDouble(sp.getString(GPS_LON, "0"));
        return lon;
    }

    public final void setLat(Context context, double lat) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String latStr = String.valueOf(lat);
        editor.putString(GPS_LAT, latStr);
        editor.apply();
    }

    public static double getLat(Context context) {
        SharedPreferences sp = context.getSharedPreferences("HomeModel", Context.MODE_PRIVATE);
        double lat = Double.parseDouble(sp.getString(GPS_LAT, "0"));
        return lat;
    }
}

