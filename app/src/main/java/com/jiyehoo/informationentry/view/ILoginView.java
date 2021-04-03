package com.jiyehoo.informationentry.view;

public interface ILoginView {
    // 显示Toast
    void showToast(String msg);
    // 加载
    void showLoading();
    void disShowLoading();
    // 按钮禁用
    void ableBtn();
    void disableBtn();

    void gotoMainActivity();
    void gotoSignInActivity();

    String getUserName();
    String getPwd();
}
