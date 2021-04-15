package com.jiyehoo.informationentry.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.jiyehoo.informationentry.activity.DeviceCtrlActivity;
import com.jiyehoo.informationentry.adapter.DeviceListAdapter;
import com.jiyehoo.informationentry.adapter.OnDeviceItemClickListener;
import com.jiyehoo.informationentry.bean.NBInfoBean;
import com.jiyehoo.informationentry.model.DeviceListModel;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.model.IDeviceListModel;
import com.jiyehoo.informationentry.util.TyDeviceActiveBusiness;
import com.jiyehoo.informationentry.view.IDeviceListView;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.network.Business;
import com.tuya.smart.android.network.http.BusinessResponse;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeviceListPresenter {

    private static final int REQUEST_CODE = 0;
    private static final int ACTIVITY_RESULT = 1;

    private final String TAG = "###DeviceListPresenter";
    private final Context context;
    private final IDeviceListView view;
    private final IDeviceListModel model;
    private DeviceListAdapter adapter;

    public DeviceListPresenter(IDeviceListView view) {
        context = (Context) view;
        this.view = view;
        model = new DeviceListModel();
    }

    /**
     * 获取设备列表
     */
    public void getDeviceList() {
        model.clear();
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
//                    view.showRv(model.getDeviceList());
                    adapterSetListener(model.getDeviceList());

                } else {
                    Log.d(TAG, "设备列表为空");
                    // todo 清空
                    view.rvRemoveAll();
                }

            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Log.d(TAG, "设备列表获取失败");
                view.showToast("获取设备列表失败");
            }
        });
    }

    /**
     * 配置 rv 的 adapter，这里实现了点击和长按
     */
    private void adapterSetListener(List<DeviceBean> deviceBeanList) {
        adapter = new DeviceListAdapter(deviceBeanList);
        adapter.setOnDeviceItemClickListener(new OnDeviceItemClickListener() {
            @Override
            public void onItemClick(DeviceBean deviceBean) {
                Log.d(TAG, "点击");
                // 跳转到设备控制，将 devId 传入获取 device 实例，用于控制设备：
                // 用法： ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(deviceBean.getDevId());
                Intent intent = new Intent(context, DeviceCtrlActivity.class);
                intent.putExtra("devId", deviceBean.getDevId());
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(DeviceBean deviceBean) {
                Log.d(TAG, "长按");
                // 删除设备的 dialog
                view.showDialog(deviceBean.getName(), "是否移除设备？移除之后需要重新配网", deviceBean.getDevId());
            }
        });
        // 将这个设置好的 adapter 传给 rv
        view.showRv(adapter);
    }

    /**
     * 删除设备
     */
    public void removeDevice(String devId) {
        ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(devId);
        mDevice.removeDevice(new IResultCallback() {
            @Override
            public void onError(String code, String msg) {
                Log.d(TAG, "移除失败:" + msg);
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "移除成功");
                // todo 刷新列表
                // list remove
                getDeviceList();
//                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * NB-iot
     * 配网
     */
    private void activatorNB(String id) {
        long homeId = HomeModel.getHomeId(context);

        TyDeviceActiveBusiness mBusiness = new TyDeviceActiveBusiness();
        mBusiness.bindNbDevice(id,
                homeId,
                "+08:00",
                new Business.ResultListener<DeviceBean>() {
                    @Override
                    public void onFailure(BusinessResponse businessResponse, DeviceBean deviceBean, String msg) {
                        Log.d(TAG, "配网失败");
                        view.showToast("配网失败:" + msg);
                    }

                    @Override
                    public void onSuccess(BusinessResponse businessResponse, DeviceBean deviceBean, String msg) {
                        if (deviceBean != null) {
                            Log.d(TAG, "配网成功:" + deviceBean.getName());
                            // todo 刷新 rv
                            getDeviceList();
//                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "deviceBean is null");
                        }
                    }
                }
        );

    }

    /**
     * NB-Iot
     * 根据扫码得到的信息开始配网
     */
    public void qrGetUuid(String url) {

        HashMap<String, Object> postData = new HashMap<>();
        postData.put("code", url);
        TuyaHomeSdk.getRequestInstance().requestWithApiNameWithoutSession(
                "tuya.m.qrcode.parse", "4.0", postData, String.class, new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String info) {
                        Log.d(TAG, "获取 NBInfo 成功:" + info);
                        // 返回的是 json，需要获取其中 id 用于配网
                        NBInfoBean nbInfoBean = new Gson().fromJson(info, NBInfoBean.class);
                        String nbId = nbInfoBean.getActionData().getId();
                        Log.d(TAG, "NB_ID:" + nbId);
                        activatorNB(nbId);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        Log.d(TAG, "获取 NB_info 失败:" + errorMessage);
                        view.showToast("获取 NB_info 失败:" + errorMessage);
                    }
                }
        );
    }

    /**
     * 权限
     * 摄像头、储存
     */
    public void startQR() {
        Log.d(TAG, "开始权限检测");
        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
        if (!permissionList.isEmpty()) {
            String[] permissionArray = new String[permissionList.size()];
            permissionList.toArray(permissionArray);
            ActivityCompat.requestPermissions((Activity) context, permissionArray, REQUEST_CODE);
        } else {
            // 有权限，扫码
            Log.d(TAG, "有权限,开始扫码");
            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE ).create();
            ScanUtil.startScan((Activity) context, ACTIVITY_RESULT, options);
        }
    }



}
