package com.jiyehoo.informationentry.model;

import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.Collection;
import java.util.Map;

public class CtrlModel implements ICtrlModel {

    private Map<String, SchemaBean> map;
    private DeviceBean deviceBean;
    private Collection<SchemaBean> beanCollection;
    private ITuyaDevice device;

    public CtrlModel() {
        // todo 设置初始化，暂时没想好要初始化啥
    }

    public Map<String, SchemaBean> getMap() {
        return map;
    }

    public void setMap(Map<String, SchemaBean> map) {
        this.map = map;
    }

    public Collection<SchemaBean> getBeanCollection() {
        return beanCollection;
    }

    public void setBeanCollection(Collection<SchemaBean> beanCollection) {
        this.beanCollection = beanCollection;
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public ITuyaDevice getDevice() {
        return device;
    }

    public void setDevice(ITuyaDevice device) {
        this.device = device;
    }

    public void clear() {

    }
}
