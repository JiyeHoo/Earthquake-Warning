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
     * 指纹开关
     */
    void setSbFinger(boolean isChecked);

    /**
     * 指纹开关(立即，不触发回调)
     */
    void setSbFingerImmediately(boolean isChecked);

    /**
     * 结束 Activity
     */
    void finishSetActivity();

}
