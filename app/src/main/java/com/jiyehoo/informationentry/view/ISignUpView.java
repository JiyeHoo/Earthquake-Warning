package com.jiyehoo.informationentry.view;

/**
 * 注册界面操作
 */
public interface ISignUpView {
    // 获取 ET 的数据
    String getEmail();
    String getPhone();
    String getPwd();
    String getCode();

    void showJumpBean();
    void showToast(String msg);

    void gotoLoginActivity();
}
