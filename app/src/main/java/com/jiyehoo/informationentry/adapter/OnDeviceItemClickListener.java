package com.jiyehoo.informationentry.adapter;

import com.tuya.smart.sdk.bean.DeviceBean;

public interface OnDeviceItemClickListener {

    void onItemClick(DeviceBean deviceBean);

    void onItemLongClick(DeviceBean deviceBean);
}
