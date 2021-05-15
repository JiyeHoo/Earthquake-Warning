package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/15 下午6:52
 */
public enum LoginSpModel {
    INSTANCE;

    public static final String IS_REMEMBER = "isRemember";
    public static final String USER = "user";
    public static final String PWD = "pwd";

    /**
     * 清空记住的信息
     */
    public final void clearInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(USER);
        editor.remove(PWD);
        editor.apply();
    }

    /**
     * 是否记住密码
     */
    public final void setIsRemember(Context context, boolean isRemember) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(IS_REMEMBER, isRemember);
        editor.apply();
    }

    public final boolean getIsRemember(Context context) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        return sp.getBoolean(IS_REMEMBER, false);
    }

    /**
     * 获取记住的账号
     */
    public final void setUser(Context context, String User) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(USER, User);
        editor.apply();
    }

    public final String getUser(Context context) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        return sp.getString(USER, "");
    }

    /**
     * 获取记住的密码
     */
    public final void setPwd(Context context, String User) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PWD, User);
        editor.apply();
    }

    public final String getPwd(Context context) {
        SharedPreferences sp = context.getSharedPreferences("LoginSpModel", Context.MODE_PRIVATE);
        return sp.getString(PWD, "");
    }
}
