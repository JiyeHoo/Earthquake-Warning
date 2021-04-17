package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.util.Log;

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
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.Collection;
import java.util.Map;

public class CtrlPresenter {

    private final String TAG = "CtrlPresenter";
    private Context context;
    private ICtrlView view;
    private ICtrlModel model;
    private String devId;

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
        DeviceBean deviceBean = TuyaHomeSdk.getDataInstance().getDeviceBean(devId);
        // Map<DP编号， DP内容>
        Map<String, SchemaBean> map = TuyaHomeSdk.getDataInstance().getSchema(devId);
        Collection<SchemaBean> beanCollection = map.values();


        model.setDeviceBean(deviceBean);
        model.setMap(map);
        model.setBeanCollection(beanCollection);
    }

    /**
     * 显示数据
     */
    public void showDp() {
        if (devId == null) {
            return;
        }

        ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance(devId);

        for (SchemaBean bean : model.getBeanCollection()) {
            // todo 显示每一个 dp 的视图
            Log.d(TAG, bean.getName());

            Object value = model.getDeviceBean().getDps().get(bean.getId());
            if (bean.type.equals(DataTypeEnum.OBJ.getType())) {
                switch (bean.getSchemaType()) {
                    // todo 判断 dp 类型，显示不同 view
                    // boolean
                    case BoolSchemaBean.type:
                        DpBooleanItem booleanItem = new DpBooleanItem(context, bean, value, mDevice);
                        view.addView(booleanItem);
                        break;
                    // value
                    case ValueSchemaBean.type:
                        DpValueItem valueItem = new DpValueItem(context, bean, value, mDevice);
                        view.addView(valueItem);
                        break;
                    // enum
                    case EnumSchemaBean.type:
                        DpEnumItem enumItem = new DpEnumItem(context, bean, value, mDevice);
                        view.addView(enumItem);
                        break;
                    default:
                        break;

                }
            }
        }
    }

}
