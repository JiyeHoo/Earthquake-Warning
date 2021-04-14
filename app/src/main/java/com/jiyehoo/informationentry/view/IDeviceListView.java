package com.jiyehoo.informationentry.view;

import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public interface IDeviceListView {
    void showToast(String msg);
    void showRv(List<DeviceBean> deviceBeanList);
}
