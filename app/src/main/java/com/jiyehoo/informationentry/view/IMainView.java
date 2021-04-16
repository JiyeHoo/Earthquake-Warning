package com.jiyehoo.informationentry.view;

public interface IMainView {
    void showOneTextToTv(String text);
    void showToast(String msg);
    void showUserInfo(String nickname, String email, String headPicUrl);
    void showWeatherIcon(String url);

}
