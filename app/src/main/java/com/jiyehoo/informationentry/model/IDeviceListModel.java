package com.jiyehoo.informationentry.model;

import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public interface IDeviceListModel {
    void setDeviceList(List<DeviceBean> deviceBeanList);
    List<DeviceBean> getDeviceList();
}
