package com.jiyehoo.informationentry.model;

import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.Collection;
import java.util.Map;

public interface ICtrlModel {
    Map<String, SchemaBean> getMap();
    void setMap(Map<String, SchemaBean> map);

    Collection<SchemaBean> getBeanCollection();
    void setBeanCollection(Collection<SchemaBean> beanCollection);

    DeviceBean getDeviceBean();
    void setDeviceBean(DeviceBean deviceBean);

    ITuyaDevice getDevice();
    void setDevice(ITuyaDevice device);

    void clear();

}
