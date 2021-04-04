package com.jiyehoo.informationentry.bean;

import com.tuya.smart.android.user.bean.User;

public class MainBean {
    private String oneTextString;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getOneTextString() {
        return oneTextString;
    }

    public void setOneTextString(String oneTextString) {
        this.oneTextString = oneTextString;
    }

    public MainBean() {
        oneTextString = null;
        user = new User();
    }
}
