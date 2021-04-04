package com.jiyehoo.informationentry.view;

public interface IResetPwdView {
    String getEmail();
    String getPwd();
    String getCode();

    void showToast(String msg);
}
