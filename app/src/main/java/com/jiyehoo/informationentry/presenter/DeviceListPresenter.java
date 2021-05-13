package com.jiyehoo.informationentry.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
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
import com.jiyehoo.informationentry.util.LoadingDialogUtil;
import com.jiyehoo.informationentry.util.TyDeviceActiveBusiness;
import com.jiyehoo.informationentry.view.IDeviceListView;
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

import pub.devrel.easypermissions.EasyPermissions;

public class DeviceListPresenter {

    private static final int REQUEST_CODE = 0;
    private static final int ACTIVITY_RESULT = 1;

    private final String TAG = "###DeviceListPresenter";
    private final Context context;
    private final IDeviceListView view;
    private final IDeviceListModel model;
//    private final LoadingDialogUtil loadingDialogUtil;
    // 权限
    String PERMISSION_STORAGE_MSG = "请授予权限，否则影响部分使用功能";
    int PERMISSION_STORAGE_CODE = 10001;
    String[] PERMS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};


    public DeviceListPresenter(IDeviceListView view) {
        context = (Context) view;
        this.view = view;
        model = new DeviceListModel();
//        loadingDialogUtil = new LoadingDialogUtil(context);
    }

    /**
     * 获取设备列表
     */
    public void getDeviceList() {
//        loadingDialogUtil.showLoading(true);


        view.showSwipeRefresh(true);
        model.clear();
        long homeId = HomeModel.getHomeId(context);
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                // 消除加载框
//                loadingDialogUtil.showLoading(false);
                view.showSwipeRefresh(false);

                if (bean.getDeviceList() != null && bean.getDeviceList().size() > 0) {
                    view.showNoDeviceTip(false);
                    // 存List
                    model.setDeviceList(bean.getDeviceList());
                    model.getDeviceList().forEach(deviceBean ->
                            Log.d(TAG, "设备名:" + deviceBean.getName() + " devId:" + deviceBean.getDevId()));

                    // 显示 rv
//                    view.showRv(model.getDeviceList());
                    adapterSetListener(model.getDeviceList());
                    view.showSwipeRefresh(false);
                } else {
                    Log.d(TAG, "设备列表为空");
                    // 需要考虑删除设备之后会调用到这里，所以需要将 rv 清空
                    view.rvRemoveAll();
                    view.showNoDeviceTip(true);
                    view.showSwipeRefresh(false);

                }

            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Log.d(TAG, "设备列表获取失败");
                view.showToast("获取设备列表失败");
                view.showSwipeRefresh(false);
            }
        });
    }

    /**
     * 配置 rv 的 adapter，这里实现了点击和长按
     */
    private void adapterSetListener(List<DeviceBean> deviceBeanList) {
        DeviceListAdapter adapter = new DeviceListAdapter(deviceBeanList);
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
                view.showToast("移除成功");
                // 刷新，重新拉 deviceList
                getDeviceList();
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

                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("配网失败")
                                .setMessage(msg)
                                .setPositiveButton("确定", null)
                                .show();
                        alertDialog.show();
                    }

                    @Override
                    public void onSuccess(BusinessResponse businessResponse, DeviceBean deviceBean, String msg) {
                        if (deviceBean != null) {
                            Log.d(TAG, "配网成功:" + deviceBean.getName());
                            // todo 配网成功下发 经纬度

                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000); // 休眠 1 秒
                                    String lonStr = String.valueOf(HomeModel.getLon(context));
                                    String latStr = String.valueOf(HomeModel.getLat(context));
                                    Log.d(TAG, "SP获取GPS：" + lonStr + "," + latStr);
                                    sendDps(deviceBean.getDevId(), lonStr, latStr);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();



//                            view.showToast("添加设备成功");
                            // 刷新，重新拉 DeviceList，更新 rv
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

                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("扫码失败")
                                .setMessage(errorMessage)
                                .setPositiveButton("确定", null)
                                .show();
                        alertDialog.show();
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

        if (EasyPermissions.hasPermissions(context, PERMS)) {
            // 已经申请过权限，做想做的事
            Log.d(TAG, "拍照和储存权限拥有,开始扫码");
            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE ).create();
            ScanUtil.startScan((Activity) context, ACTIVITY_RESULT, options);
        } else {
            // 没有申请过权限，现在去申请
            /*
             @param host Context对象
             @param rationale  权限弹窗上的提示语。
             @param requestCode 请求权限的唯一标识码
             @param perms 一系列权限
             */
            Log.d(TAG, "没有权限，开始申请");
            EasyPermissions.requestPermissions((Activity) context, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMS);
        }
    }

    /**
     * 发送 DPS
     */
    private void sendDps(String devId, String id, String ctl) {
        ITuyaDevice device = TuyaHomeSdk.newDeviceInstance(devId);
        Log.d(TAG, "开始发送经纬度DPS,devIdL" + devId);
        HashMap<String, String> map = new HashMap<>();
        map.put(id, ctl);
        String dps = JSONObject.toJSONString(map);
        Log.d(TAG, "发送的DPS:" + dps);
        device.publishDps(dps, new IResultCallback() {
            @Override
            public void onError(String code, String msg) {
                Log.d(TAG, "发送DPS失败:" + msg);
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "发送DPS成功");
            }
        });
    }


//    public void startQR() {
//        Log.d(TAG, "开始权限检测");
//        List<String> permissionList = new ArrayList<>();
//
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.CAMERA);
//        }
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//
//        // 如果列表为空，就是全部权限都获取了，不用再次获取了。不为空就去申请权限
//        if (!permissionList.isEmpty()) {
//            String[] permissionArray = new String[permissionList.size()];
//            permissionList.toArray(permissionArray);
//            ActivityCompat.requestPermissions((Activity) context, permissionArray, REQUEST_CODE);
//        } else {
//            // 有权限，扫码
//            Log.d(TAG, "有权限,开始扫码");
//            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE ).create();
//            ScanUtil.startScan((Activity) context, ACTIVITY_RESULT, options);
//        }
//    }





}
