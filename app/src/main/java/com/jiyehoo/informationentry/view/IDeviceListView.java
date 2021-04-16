package com.jiyehoo.informationentry.view;

import com.jiyehoo.informationentry.adapter.DeviceListAdapter;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public interface IDeviceListView {
    void showToast(String msg);
    void showRv(DeviceListAdapter adapter);
    void showDialog(String title, String msg, String devId);
    void rvRemoveAll();
    void showNoDeviceTip(boolean haveShow);
}
