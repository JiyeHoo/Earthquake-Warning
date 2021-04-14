package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.util.Log;

import com.jiyehoo.informationentry.model.DeviceListModel;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.model.IDeviceListModel;
import com.jiyehoo.informationentry.view.IDeviceListView;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

public class DeviceListPresenter {

    private final String TAG = "DeviceListPresenter";
    private Context context;
    private IDeviceListView view;
    private IDeviceListModel model;

    public DeviceListPresenter(IDeviceListView view) {
        context = (Context) view;
        this.view = view;
        model = new DeviceListModel();
    }

    /**
     * 获取设备列表
     */
    public void getDeviceList() {
        long homeId = HomeModel.getHomeId(context);
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                if (bean.getDeviceList() != null && bean.getDeviceList().size() > 0) {
                    // 存List
                    model.setDeviceList(bean.getDeviceList());
                    model.getDeviceList().forEach(deviceBean ->
                            Log.d(TAG, "设备名:" + deviceBean.getName() + " devId:" + deviceBean.getDevId()));

                    // 显示 rv
                    view.showRv(model.getDeviceList());
                } else {
                    Log.d(TAG, "设备列表为空");
                }

            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Log.d(TAG, "设备列表获取失败");
                view.showToast("获取设备列表失败");
            }
        });
    }

    private void showList() {

    }



}
