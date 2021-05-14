package com.jiyehoo.informationentry.view;

public interface ISetView {
    /**
     * toast
     */
    void showToast(String msg);

    /**
     * 发现新版本的 dialog
     */
    void showUpdateDialog(String title, String msg);

    /**
     * 开启指纹开关
     */
    void setSbFinger(boolean isChecked);

}
