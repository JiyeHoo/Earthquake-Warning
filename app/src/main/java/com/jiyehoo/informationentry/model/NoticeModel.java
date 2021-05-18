package com.jiyehoo.informationentry.model;


import com.tuya.smart.sdk.bean.message.MessageBean;

import java.util.List;

/**
 * @author JiyeHoo
 * @description: 通知的 model
 * @date :2021/5/18 上午12:50
 */
public class NoticeModel implements INoticeModel {
    private List<MessageBean> beanList;

    public NoticeModel() {

    }


    @Override
    public void setNoticeList(List<MessageBean> beanList) {
        this.beanList = beanList;
    }

    @Override
    public List<MessageBean> getNoticeList() {
        return beanList;
    }
}
