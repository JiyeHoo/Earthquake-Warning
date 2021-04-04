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
    // 是否记住密码
//    boolean isRemember();
    // 存储账号密码
//    void rememberPwd();

    // 加载记住的账号密码
    void loadPwd(String userName, String pwd);

    // 获取checkBox 状态
    boolean getCheckBoxState();

    void gotoMainActivity();
    void gotoSignInActivity();

    String getUserName();
    String getPwd();
}
