package com.jiyehoo.informationentry.model;

import com.tuya.smart.home.sdk.api.IDevModel;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.List;

public class DeviceListModel implements IDeviceListModel {

    private List<DeviceBean> deviceBeanList;

    public DeviceListModel() {
        deviceBeanList = new ArrayList<>();
    }

    @Override
    public void setDeviceList(List<DeviceBean> deviceBeanList) {
        this.deviceBeanList = deviceBeanList;
    }

    @Override
    public List<DeviceBean> getDeviceList() {
        return deviceBeanList;
    }


}
