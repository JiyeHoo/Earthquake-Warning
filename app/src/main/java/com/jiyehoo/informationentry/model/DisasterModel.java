package com.jiyehoo.informationentry.model;

import com.qweather.sdk.bean.WarningBean;

import java.util.List;

/**
 * @author JiyeHoo
 * @description: 灾害数据 model
 * @date :2021/5/17 上午12:50
 */
public class DisasterModel implements IDisasterModel{
    private List<WarningBean.WarningBeanBase> baseList;

    public DisasterModel() {}

    public List<WarningBean.WarningBeanBase> getBaseList() {
        return baseList;
    }

    public void setBaseList(List<WarningBean.WarningBeanBase> baseList) {
        this.baseList = baseList;
    }
}
