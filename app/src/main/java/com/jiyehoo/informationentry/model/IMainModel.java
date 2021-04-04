package com.jiyehoo.informationentry.model;

public interface IMainModel {
    void setOneTextString(String s);
    String getOneTextString();

    void getUserInfo();
    String getUserInfoEmail();
    String getUserInfoHeadPicUrl();
    String getUserInfoName();
    String getUserInfoPhone();
}
