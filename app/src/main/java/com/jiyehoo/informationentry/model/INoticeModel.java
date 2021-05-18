package com.jiyehoo.informationentry.model;

import com.tuya.smart.sdk.bean.message.MessageBean;

import java.util.List;

/**
 * @author JiyeHoo
 * @description: 通知 model 的接口
 * @date :2021/5/18 上午12:51
 */
public interface INoticeModel {
    void setNoticeList(List<MessageBean> beanList);
    List<MessageBean> getNoticeList();
}
