package com.jiyehoo.informationentry.model;

import com.qweather.sdk.bean.WarningBean;

import java.util.List;

/**
 * @author JiyeHoo
 * @description: 预警数据 model 的接口
 * @date :2021/5/17 上午12:58
 */
public interface IDisasterModel {
    List<WarningBean.WarningBeanBase> getBaseList();
    void setBaseList(List<WarningBean.WarningBeanBase> baseList);
}
