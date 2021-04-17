package com.jiyehoo.informationentry.model;

import com.jiyehoo.informationentry.view.ICtrlView;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.Collection;
import java.util.Map;

public class CtrlModel implements ICtrlModel {

    private Map<String, SchemaBean> map;
    private DeviceBean deviceBean;
    private Collection<SchemaBean> beanCollection;

    public CtrlModel() {
        // todo 设置初始化
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
}
