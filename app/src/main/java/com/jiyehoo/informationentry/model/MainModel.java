package com.jiyehoo.informationentry.model;

import android.text.TextUtils;
import android.util.Log;

import com.jiyehoo.informationentry.bean.MainBean;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class MainModel implements IMainModel{
    private final String TAG = "MainModel";

    private MainBean bean;

    public MainModel() {
        bean = new MainBean();
    }


    @Override
    public void setOneTextString(String s) {
        if (!TextUtils.isEmpty(s) && s.length() > 0) {
            bean.setOneTextString(s);
        } else {
            bean.setOneTextString(" ");
        }
    }

    @Override
    public String getOneTextString() {
        return stringNotNull(bean.getOneTextString());
    }



    @Override
    public void getUserInfo() {
        User user = TuyaHomeSdk.getUserInstance().getUser();
        bean.setUser(user);
    }

    @Override
    public String getUserInfoEmail() {
        return stringNotNull(bean.getUser().getEmail());
    }

    @Override
    public String getUserInfoHeadPicUrl() {
        Log.d(TAG, "headPic:" + bean.getUser().getHeadPic());
        if (!TextUtils.isEmpty(bean.getUser().getHeadPic()) && bean.getUser().getHeadPic().length() > 0) {
            return bean.getUser().getPhoneCode();
        } else {
            return "http://tc.jiyehoo.com:81/images/2020/03/09/ea0daee84c7ce5d31af971b79a8707ca.jpg";
        }
    }

    @Override
    public String getUserInfoName() {
        return stringNotNull(bean.getUser().getNickName());
    }

    @Override
    public String getUserInfoPhone() {
        return stringNotNull(bean.getUser().getPhoneCode());
    }

    private String stringNotNull(String s) {
        if (!TextUtils.isEmpty(s) && s.length() > 0) {
            return s;
        } else {
            return "default";
        }
    }
}
