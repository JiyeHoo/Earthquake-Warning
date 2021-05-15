package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * @author JiyeHoo
 * @description: 设置的储存
 * @date :2021/5/15 上午12:37
 */
public enum SetSpModel {
    INSTANCE;
    private final String TAG = "###SetSpModel";

    public static final String IS_FINGER_OPEN = "isFingerOpen";

    public final void setIsFingerOpen(Context context, boolean isOpen) {
        SharedPreferences sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(IS_FINGER_OPEN, isOpen);
        editor.apply();
    }

    public static boolean getIsFingerOpen(Context context) {
        SharedPreferences sp = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sp.getBoolean(IS_FINGER_OPEN, false);
    }


}
