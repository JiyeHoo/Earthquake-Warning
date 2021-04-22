package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.jiyehoo.informationentry.model.CtrlModel;
import com.jiyehoo.informationentry.model.DpBooleanItem;
import com.jiyehoo.informationentry.model.DpEnumItem;
import com.jiyehoo.informationentry.model.DpValueItem;
import com.jiyehoo.informationentry.model.ICtrlModel;
import com.jiyehoo.informationentry.view.ICtrlView;
import com.tuya.smart.android.device.bean.BoolSchemaBean;
import com.tuya.smart.android.device.bean.EnumSchemaBean;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.android.device.bean.ValueSchemaBean;
import com.tuya.smart.android.device.enums.DataTypeEnum;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IDevListener;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.Collection;
import java.util.Map;

public class CtrlPresenter {

    private final String TAG = "###CtrlPresenter";
    private final Context context;
    private final ICtrlView view;
    private final ICtrlModel model;
    private final String devId;

    public CtrlPresenter(ICtrlView view, String devId) {
        context = (Context) view;
        this.view = view;
        model = new CtrlModel();
        this.devId = devId;
    }

    /**
     * 获取数据存入 model
     */
    public void getSchemaMap() {
        if (devId == null) {
            view.showToast("devId 为空");
            return;
        }
        model.clear();
        ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(devId);
        DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        // Map<DP编号， DP内容>
        Map<String, SchemaBean> map = TuyaHomeSdk.getDataInstance().getSchema(devId);
        if (null == map) {
            Log.d(TAG, "map 为空");
            return;
        }
        Collection<SchemaBean> beanCollection = map.values();

        model.setDevice(mDevice);
        model.setDeviceBean(deviceBean);
        model.setMap(map);
        model.setBeanCollection(beanCollection);

        registerListener();
    }

    /**
     * 设备状态监听
     */
    public void registerListener() {

        if (model.getDevice() != null) {
            Log.d(TAG, "设置设备监听");
            model.getDevice().registerDevListener(new IDevListener() {
                @Override
                public void onDpUpdate(String devId, String dpStr) {
                    // dp 更新，dpStr 为 json
                    Log.d(TAG, "监听：dp 更新：" + dpStr);
                }

                @Override
                public void onRemoved(String devId) {
                    // 设备移除
                    Log.d(TAG, "监听：设备移除:" + devId);
                }

                @Override
                public void onStatusChanged(String devId, boolean online) {
                    // 设备在线状态改变
                    Log.d(TAG, "监听：设备在线状态改变");
                }

                @Override
                public void onNetworkStatusChanged(String devId, boolean status) {
                    // 网络状态改变，网络是否可用
                    Log.d(TAG, "监听：设备网络：" + status);
                }

                @Override
                public void onDevInfoUpdate(String devId) {
                    // 设备信息更新
                    Log.d(TAG, "监听：设备信息改变");
                }
            });
        } else {
            Log.d(TAG, "注册监听，设备 null");
        }
    }

    /**
     * 取消监听
     */
    public void unregisterListener() {
        if (model.getDevice() != null) {
            model.getDevice().unRegisterDevListener();
        } else {
            Log.d(TAG, "取消监听，设备 null");
        }
    }

    /**
     * 显示数据
     */
    public void showDp() {
        if (devId == null) {
            return;
        }

        for (SchemaBean bean : model.getBeanCollection()) {
            // todo 显示每一个 dp 的视图
            Log.d(TAG, bean.getName());

            Object value = model.getDeviceBean().getDps().get(bean.getId());
            if (bean.type.equals(DataTypeEnum.OBJ.getType())) {
                switch (bean.getSchemaType()) {
                    // todo 判断 dp 类型，显示不同 view
                    // boolean
                    case BoolSchemaBean.type:
                        DpBooleanItem booleanItem = new DpBooleanItem(context, bean, value, model.getDevice());
                        view.addView(booleanItem);
                        break;
                    // value
                    case ValueSchemaBean.type:
                        DpValueItem valueItem = new DpValueItem(context, bean, value, model.getDevice());
                        view.addView(valueItem);
                        break;
                    // enum
                    case EnumSchemaBean.type:
                        DpEnumItem enumItem = new DpEnumItem(context, bean, value, model.getDevice());
                        view.addView(enumItem);
                        break;
                    default:
                        break;

                }
            }
        }

    }

    /**
     * 恢复出厂
     */
    public void resetFactory() {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("恢复出厂")
                .setMessage("是否清除设备所有数据，同时移除设备！")
                .setPositiveButton("确定", (dialog, which) -> {
                    // 恢复出厂
                    view.showSwipeRefresh(true);
                    model.getDevice().removeDevice(new IResultCallback() {
                        @Override
                        public void onError(String errorCode, String errorMsg) {
                            Log.d(TAG, "恢复出厂失败");
                            view.showToast("恢复出厂失败");
                            view.showSwipeRefresh(false);
                        }

                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "恢复出厂成功");
                            view.showToast("恢复出厂成功");
                            view.showSwipeRefresh(false);
                            view.finishActivity();
                        }
                    });
                })
                .setNegativeButton("取消", null)
                .show();
        alertDialog.show();
    }

    /**
     * 销毁 activity 时候销毁 device 实例
     */
    public void destroyDevice() {
        model.getDevice().onDestroy();
    }

}
