package com.jiyehoo.informationentry.presenter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.bean.NBInfoBean;
import com.jiyehoo.informationentry.model.IMainModel;
import com.jiyehoo.informationentry.model.MainModel;
import com.jiyehoo.informationentry.util.HomeModel;
import com.jiyehoo.informationentry.util.HttpUtil;
import com.jiyehoo.informationentry.util.TyDeviceActiveBusiness;
import com.jiyehoo.informationentry.view.IMainView;
import com.tuya.smart.android.base.ApiParams;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.network.Business;
import com.tuya.smart.android.network.http.BusinessResponse;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.builder.TuyaQRCodeActivatorBuilder;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainPresenter {
    private final String TAG = "MainPresenter";

    private final Context mContext;
    private final IMainView view;
    private final IMainModel model;

    public MainPresenter(IMainView view) {
        this.view = view;
        mContext = (Context) view;
        model = new MainModel();
    }

    /**
     * 获取第0个homeId，空则创建
     */
    public void setHomeId() {
        TuyaHomeSdk.getHomeManagerInstance().queryHomeList(new ITuyaGetHomeListCallback() {
            @Override
            public void onSuccess(List<HomeBean> homeBeanList) {
                if (!homeBeanList.isEmpty() && homeBeanList.size() >= 0) {
                    // homeList 不为空，则取第0个，写入 Sp
                    long homeId = homeBeanList.get(0).getHomeId();
                    HomeModel.INSTANCE.setHomeId(mContext, homeId);
                } else {
                    // 为空则创建一个 home
                    createHome();
                }
            }

            @Override
            public void onError(String errorCode, String error) {
                Log.d(TAG, "获取 homeList 失败");
                view.showToast("初始化失败");
            }
        });
    }

    /**
     * 创建 home
     */
    private void createHome() {
        TuyaHomeSdk.getHomeManagerInstance().createHome(
                "orgName",
                0,
                0,
                "",
                new ArrayList<>(),
                new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        // homeId 写入 sp
                        Log.d(TAG, "创建 home 成功:" + bean.getHomeId());
                        HomeModel.INSTANCE.setHomeId(mContext, bean.getHomeId());
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Log.d(TAG, "创建 home 失败");
                        view.showToast("创建默认组织失败");
                    }
                }
        );
    }

    public void updateNickName() {
        final View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_nikname, null);
        EditText editText = v.findViewById(R.id.et_nav_update_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("修改昵称")
                .setView(v)
                .setPositiveButton("确定", (dialog, which) -> {
                    String name = editText.getText().toString();
                    if (!TextUtils.isEmpty(name) && name.length() > 0) {
                        TuyaHomeSdk.getUserInstance().updateNickName(name, new IReNickNameCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "修改昵称成功");
                                view.showToast("修改成功");
                                setUserInfo();
                            }

                            @Override
                            public void onError(String code, String error) {
                                Log.d(TAG, "修改昵称失败");
                                view.showToast("修改失败：" + error);
                            }
                        });
                    } else {
                        view.showToast("昵称不得为空");
                    }
                })
                .show();
    }

    public void setOneText() {
        String url = "https://v1.hitokoto.cn/?c=i&encode=text&max_length=15&min_length=8";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "一言请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String s = Objects.requireNonNull(response.body()).string();
                model.setOneTextString(s);
                view.showOneTextToTv(model.getOneTextString());
            }
        });

    }

    public void setUserInfo() {
        model.getUserInfo();
        view.showUserInfo(model.getUserInfoName(), model.getUserInfoEmail(), model.getUserInfoHeadPicUrl());
    }

    /**
     * NB-iot
     * 将扫码的到的 String 通过接口，获取到 id
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
     * NB-iot
     * 将设备的 UUID 用于配网
     */
    private void activatorNB(String id) {
        long homeId = HomeModel.getHomeId(mContext);

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
                        } else {
                            Log.d(TAG, "deviceBean is null");
                        }
                    }
                }
        );

    }
}
