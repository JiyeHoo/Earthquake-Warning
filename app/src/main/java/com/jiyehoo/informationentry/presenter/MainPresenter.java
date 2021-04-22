package com.jiyehoo.informationentry.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.model.IMainModel;
import com.jiyehoo.informationentry.model.MainModel;
import com.jiyehoo.informationentry.util.HttpUtil;
import com.jiyehoo.informationentry.view.IMainView;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.bean.WeatherBean;
import com.tuya.smart.home.sdk.callback.IIGetHomeWetherSketchCallBack;
import com.tuya.smart.home.sdk.callback.ITuyaGetHomeListCallback;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainPresenter {
    private final String TAG = "###MainPresenter";

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
                        getWeatherInfo();
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
     * 获取天气
     */
    public void getWeatherInfo() {
        long homeId = HomeModel.getHomeId(mContext);
        if (TextUtils.isEmpty(String.valueOf(homeId)) || homeId == -1) {
            return;
        }
        TuyaHomeSdk.newHomeInstance(homeId).getHomeWeatherSketch(120.075652,30.306265,
                new IIGetHomeWetherSketchCallBack() {
            @Override
            public void onSuccess(WeatherBean result) {
                Log.d(TAG, "获取天气成功:" + result.getCondition() + ",Url:" + result.getInIconUrl());
                view.showWeatherIcon(result.getInIconUrl());
            }
            @Override
            public void onFailure(String errorCode, String errorMsg) {
                Log.d(TAG, "获取天气失败:" + errorMsg);
            }
        });
    }

}
