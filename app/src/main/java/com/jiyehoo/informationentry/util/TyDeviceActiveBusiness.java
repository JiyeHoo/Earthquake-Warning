package com.jiyehoo.informationentry.util;


import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.base.ApiParams;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.network.Business;
import com.tuya.smart.android.network.http.BusinessResponse;
import com.tuya.smart.interior.config.bean.ActiveTokenBean;
import com.tuya.smart.interior.config.bean.ConfigDevResp;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class TyDeviceActiveBusiness extends Business {

    /**
     * NB-IOT 设备配网
     */
    public void bindNbDevice(String token, long homeId, String timeZone, ResultListener<DeviceBean> listener){
        ApiParams apiParams = new ApiParams("tuya.m.nb.device.user.bind", "1.0");
        apiParams.setSessionRequire(true);
        apiParams.putPostData("hid", token);
        apiParams.putPostData("timeZone", timeZone);
        apiParams.setGid(homeId);
        this.asyncRequest(apiParams, DeviceBean.class, listener);
    }

    /**
     * 红外等虚拟设备、gprs绑定
     */
    public void deviceBind(long homeId, String uuid, String devId, String timeZone, final ResultListener<CommonDeviceBean> listener) {
        ApiParams apiParams = new ApiParams("tuya.m.device.user.bind", "1.0");
        apiParams.setSessionRequire(true);
        apiParams.putPostData("token", uuid);
        apiParams.putPostData("devId", devId);
        apiParams.putPostData("timeZone", timeZone);
        apiParams.setGid(homeId);
        asyncRequest(apiParams, CommonDeviceBean.class, listener);
    }

    /**
     * 获取产品信息
     */
    public void getProductInfo(String productId, String uuid, String mac, ResultListener<ProductInfoBean> listener) {
        ApiParams apiParams = new ApiParams("tuya.m.product.info.get", "1.0");
        apiParams.setSessionRequire(true);
        if (!TextUtils.isEmpty(productId)) {
            apiParams.putPostData("productId", productId);
        }
        if (!TextUtils.isEmpty(mac)) {
            apiParams.putPostData("uuid", "");
            apiParams.putPostData("mac", mac);
        } else {
            apiParams.putPostData("uuid", uuid);
            apiParams.putPostData("mac", "");
        }
        this.asyncRequest(apiParams, ProductInfoBean.class, listener);
    }

    /**
     * 重置设备使其恢复到待配网状态,只发送不接受响应,云端有兜底方案,10分钟后主动恢复
     * @param tokens List<String>转换成Json
     * @param devIds List<String>转换成Json
     */
    public void resetDevice(List<String> tokens, List<String> devIds) {
        ApiParams apiParams = new ApiParams("tuya.m.device.reset", "2.0");
        apiParams.putPostData("tokens", JSONArray.toJSONString(tokens));
        apiParams.putPostData("devIds", JSONArray.toJSONString(devIds));
        L.d("ResetDevice", "token is: " + tokens.toString() + " devIds is: " + devIds.toString());
        this.asyncRequest(apiParams, new ResultListener<JSONObject>() {
            @Override
            public void onFailure(BusinessResponse businessResponse, JSONObject jsonObject, String s) {
                L.d("ResetDevice", "onFailure");
            }

            @Override
            public void onSuccess(BusinessResponse businessResponse, JSONObject jsonObject, String s) {
                L.d("ResetDevice", "onSuccess");
            }
        });
    }

    /**
     * 获取指定token激活的设备
     */
    public void queryDevicesByToken(String token, ResultListener<ConfigDevResp> listener) {
        ApiParams apiParams = new ApiParams("tuya.m.device.list.token", "3.0");
        apiParams.setSessionRequire(true);
        apiParams.putPostData("token", token);
        apiParams.setBizDM("device_config_add");
        this.asyncRequest(apiParams, ConfigDevResp.class, listener);
    }

    /**
     * 获取配网不绑定家庭的 token
     */
    public void createActiveTokenNoBindHome(ResultListener<ActiveTokenBean> listener) {
        ApiParams apiParams = new ApiParams("tuya.m.device.token.create", "2.0");
        apiParams.setSessionRequire(true);
        asyncRequest(apiParams, ActiveTokenBean.class, listener);
    }
}
