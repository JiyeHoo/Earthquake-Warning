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
import com.jiyehoo.informationentry.util.MyLog;
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
    // ??????
    String PERMISSION_STORAGE_MSG = "????????????????????????????????????????????????";
    int PERMISSION_STORAGE_CODE = 10001;
    String[] PERMS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};


    public DeviceListPresenter(IDeviceListView view) {
        context = (Context) view;
        this.view = view;
        model = new DeviceListModel();
//        loadingDialogUtil = new LoadingDialogUtil(context);
    }

    /**
     * ??????????????????
     */
    public void getDeviceList() {
//        loadingDialogUtil.showLoading(true);


        view.showSwipeRefresh(true);
        model.clear();
        long homeId = HomeModel.getHomeId(context);
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                // ???????????????
//                loadingDialogUtil.showLoading(false);
                view.showSwipeRefresh(false);

                if (bean.getDeviceList() != null && bean.getDeviceList().size() > 0) {
                    view.showNoDeviceTip(false);
                    // ???List
                    model.setDeviceList(bean.getDeviceList());
                    model.getDeviceList().forEach(deviceBean ->
                            MyLog.d(TAG, "?????????:" + deviceBean.getName() + " devId:" + deviceBean.getDevId()));

                    // ?????? rv
//                    view.showRv(model.getDeviceList());
                    adapterSetListener(model.getDeviceList());
                } else {
                    MyLog.d(TAG, "??????????????????");
                    // ?????????????????????????????????????????????????????????????????? rv ??????
                    view.rvRemoveAll();
                    view.showNoDeviceTip(true);
                }
                view.showSwipeRefresh(false);

            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                MyLog.d(TAG, "????????????????????????");
                view.showToast("????????????????????????");
                view.showSwipeRefresh(false);
            }
        });
    }

    /**
     * ?????? rv ??? adapter?????????????????????????????????
     */
    private void adapterSetListener(List<DeviceBean> deviceBeanList) {
        DeviceListAdapter adapter = new DeviceListAdapter(deviceBeanList);
        adapter.setOnDeviceItemClickListener(new OnDeviceItemClickListener() {
            @Override
            public void onItemClick(DeviceBean deviceBean) {
                MyLog.d(TAG, "??????");
                // ??????????????????????????? devId ???????????? device ??????????????????????????????
                // ????????? ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(deviceBean.getDevId());
                Intent intent = new Intent(context, DeviceCtrlActivity.class);
                intent.putExtra("devId", deviceBean.getDevId());
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(DeviceBean deviceBean) {
                MyLog.d(TAG, "??????");
                // ??????????????? dialog
                view.showDialog(deviceBean.getName(), "???????????????????????????????????????????????????", deviceBean.getDevId());
            }
        });
        // ????????????????????? adapter ?????? rv
        view.showRv(adapter);
    }

    /**
     * ????????????
     */
    public void removeDevice(String devId) {
        ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(devId);
        mDevice.removeDevice(new IResultCallback() {
            @Override
            public void onError(String code, String msg) {
                MyLog.d(TAG, "????????????:" + msg);
            }

            @Override
            public void onSuccess() {
                MyLog.d(TAG, "????????????");
                view.showToast("????????????");
                // ?????????????????? deviceList
                getDeviceList();
            }
        });
    }

    /**
     * NB-iot
     * ??????
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
                        MyLog.d(TAG, "????????????");

                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("????????????")
                                .setMessage(msg)
                                .setPositiveButton("??????", null)
                                .show();
                        alertDialog.show();
                    }

                    @Override
                    public void onSuccess(BusinessResponse businessResponse, DeviceBean deviceBean, String msg) {
                        if (deviceBean != null) {
                            MyLog.d(TAG, "????????????:" + deviceBean.getName());
                            // todo ?????????????????? ?????????

                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000); // ?????? 1 ???
                                    String lonStr = String.valueOf(HomeModel.getLon(context));
                                    String latStr = String.valueOf(HomeModel.getLat(context));
                                    MyLog.d(TAG, "SP??????GPS???" + lonStr + "," + latStr);
                                    sendDps(deviceBean.getDevId(), lonStr, latStr);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();



//                            view.showToast("??????????????????");
                            // ?????????????????? DeviceList????????? rv
                            getDeviceList();
//                            adapter.notifyDataSetChanged();
                        } else {
                            MyLog.d(TAG, "deviceBean is null");
                        }
                    }
                }
        );

    }

    /**
     * NB-Iot
     * ???????????????????????????????????????
     */
    public void qrGetUuid(String url) {

        HashMap<String, Object> postData = new HashMap<>();
        postData.put("code", url);
        TuyaHomeSdk.getRequestInstance().requestWithApiNameWithoutSession(
                "tuya.m.qrcode.parse", "4.0", postData, String.class, new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String info) {
                        MyLog.d(TAG, "?????? NBInfo ??????:" + info);
                        // ???????????? json????????????????????? id ????????????
                        NBInfoBean nbInfoBean = new Gson().fromJson(info, NBInfoBean.class);
                        String nbId = nbInfoBean.getActionData().getId();
                        MyLog.d(TAG, "NB_ID:" + nbId);
                        activatorNB(nbId);
                    }

                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        MyLog.d(TAG, "?????? NB_info ??????:" + errorMessage);

                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setTitle("????????????")
                                .setMessage(errorMessage)
                                .setPositiveButton("??????", null)
                                .show();
                        alertDialog.show();
                    }
                }
        );
    }

    /**
     * ??????
     * ??????????????????
     */
    public void startQR() {
        MyLog.d(TAG, "??????????????????");

        if (EasyPermissions.hasPermissions(context, PERMS)) {
            // ???????????????????????????????????????
            MyLog.d(TAG, "???????????????????????????,????????????");
            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE ).create();
            ScanUtil.startScan((Activity) context, ACTIVITY_RESULT, options);
        } else {
            // ???????????????????????????????????????
            /*
             @param host Context??????
             @param rationale  ??????????????????????????????
             @param requestCode ??????????????????????????????
             @param perms ???????????????
             */
            MyLog.d(TAG, "???????????????????????????");
            EasyPermissions.requestPermissions((Activity) context, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMS);
        }
    }

    /**
     * ?????? DPS
     */
    private void sendDps(String devId, String id, String ctl) {
        ITuyaDevice device = TuyaHomeSdk.newDeviceInstance(devId);
        MyLog.d(TAG, "?????????????????????DPS,devIdL" + devId);
        HashMap<String, String> map = new HashMap<>();
        map.put(id, ctl);
        String dps = JSONObject.toJSONString(map);
        MyLog.d(TAG, "?????????DPS:" + dps);
        device.publishDps(dps, new IResultCallback() {
            @Override
            public void onError(String code, String msg) {
                MyLog.d(TAG, "??????DPS??????:" + msg);
            }

            @Override
            public void onSuccess() {
                MyLog.d(TAG, "??????DPS??????");
            }
        });
    }


//    public void startQR() {
//        MyLog.d(TAG, "??????????????????");
//        List<String> permissionList = new ArrayList<>();
//
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.CAMERA);
//        }
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//        }
//
//        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
//        if (!permissionList.isEmpty()) {
//            String[] permissionArray = new String[permissionList.size()];
//            permissionList.toArray(permissionArray);
//            ActivityCompat.requestPermissions((Activity) context, permissionArray, REQUEST_CODE);
//        } else {
//            // ??????????????????
//            MyLog.d(TAG, "?????????,????????????");
//            HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE ).create();
//            ScanUtil.startScan((Activity) context, ACTIVITY_RESULT, options);
//        }
//    }





}
