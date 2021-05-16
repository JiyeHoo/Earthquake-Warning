package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.biometrics.BiometricPrompt;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.model.SetSpModel;
import com.jiyehoo.informationentry.util.FingerUtil;
import com.jiyehoo.informationentry.util.HttpUtil;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.ISetView;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @author JiyeHoo
 * @description: 设置界面
 */
public class SetPresenter {
    private final String TAG = "###SetPresenter";

    private Context mContext;
    private ISetView view;
//    private ISetModel model;

    public SetPresenter(ISetView view) {
        this.view = view;
        mContext = (Context) view;
//        model = new SetModel();
    }

    /**
     * 初始化开关状态
     */
    public void initSwitchState() {
        // 指纹开关是否需要开启
        if (SetSpModel.getIsFingerOpen(mContext)) {
            view.setSbFingerImmediately(true);
        }
    }

    /**
     * 开启消息通知
     */
    public void openNotice() {
        // todo 开启消息通知
    }

    /**
     * 关闭消息通知
     */
    public void closeNotice() {
        // todo 关闭消息通知
    }

    /**
     * 开启云端同步
     */
    public void openCloud() {
        // todo 开启云端同步
    }

    /**
     * 关闭云端同步
     */
    public void closeCloud() {
        // todo 关闭云端同步
    }

    /**
     * 开启离线地图
     */
    public void openLocalMap() {
        // todo 开启离线地图
    }

    /**
     * 关闭离线地图
     */
    public void closeLocalMap() {
        // todo 关闭离线地图
    }

    /**
     * 开启清理缓存
     */
    public void openClear() {
        // todo 开启自动清理缓存
    }

    /**
     * 关闭清理缓存
     */
    public void closeClear() {
        // todo 关闭自动清理缓存
    }

    /**
     * 开启指纹
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void openFinger() {
        MyLog.d(TAG, "开启指纹");
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // 5次属于错误
                MyLog.d(TAG, "指纹错误 " + errString);
                view.setSbFinger(false);
                view.showToast("指纹开启失败，过段时间再试吧！");
                view.finishSetActivity();
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // 成功
                MyLog.d(TAG, "指纹通过 " + result.toString());
                view.showToast("设置成功");
                // todo 记录 sp
                MyLog.d(TAG, "记录指纹开启");
                SetSpModel.INSTANCE.setIsFingerOpen(mContext, true);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // 单次失败
                MyLog.d(TAG, "指纹失败");
            }
        };

        DialogInterface.OnClickListener cancelCallback = (dialog, which) -> {
            view.setSbFinger(false);
            MyLog.d(TAG, "取消了指纹认证");
            view.showToast("取消打开指纹保护");

        };


        FingerUtil fingerUtil = new FingerUtil(mContext, "开启指纹保护", "请验证使用者身份", callback, cancelCallback);
        fingerUtil.startFinger();
    }

    /**
     * 关闭指纹
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void closeFinger() {
        MyLog.d(TAG, "开始认证，企图关闭指纹");

        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // 5次属于错误
                MyLog.d(TAG, "指纹错误 " + errString);
                view.setSbFinger(true);
                view.showToast("指纹关闭失败，过段时间再试吧！");
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // 成功
                view.showToast("设置成功");
                // todo 记录 sp
                MyLog.d(TAG, "记录指纹关闭");

                SetSpModel.INSTANCE.setIsFingerOpen(mContext, false);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                // 单次失败
                MyLog.d(TAG, "指纹失败");
            }
        };

        DialogInterface.OnClickListener cancelCallback = (dialog, which) -> {
            view.setSbFinger(true);
            MyLog.d(TAG, "取消了指纹认证");
            view.showToast("取消关闭指纹保护");

        };
        FingerUtil fingerUtil = new FingerUtil(mContext, "关闭指纹保护", "请验证使用者身份", callback, cancelCallback);
        fingerUtil.startFinger();
    }

    /**
     * 退出
     */
    public void loginOut() {
        TuyaHomeSdk.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                MyLog.d(TAG, "退出成功");
                // 清除 sp 中的 homeId
                HomeModel.INSTANCE.clearHomeId(mContext);
                Intent intent = new Intent("com.jiyehoo.broadcastoffline.OFFLINE");
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                view.showToast("退出失败:" + errorMsg);
            }
        });
    }

    /**
     * 更新
     */
    public void updateVersion() {
        MyLog.d(TAG, "开始更新");
        HttpUtil.sendOkHttpRequest("http://bs.jiyehoo.com:81/version", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                MyLog.d(TAG, "请求新版本失败");
                view.showToast("网络请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String cloudVersion = Objects.requireNonNull(response.body()).string();
                MyLog.d(TAG, "云端 version:" + cloudVersion);

                PackageInfo pInfo = null;
                try {
                    pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                    MyLog.d(TAG, "本地 version:" + pInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (!TextUtils.isEmpty(cloudVersion)) {
                    assert pInfo != null;
                    if (!cloudVersion.equals(pInfo.versionName)) {
                        MyLog.d(TAG, "发现新版本 " + cloudVersion);
                        view.showUpdateDialog("发现新版本 v" + cloudVersion,
                                "当前版本 v" + pInfo.versionName + "，请尽快更新！");
                    }
                }
            }
        });
    }

    /**
     * 打开浏览器更新
     */
    public void updateByIntent() {
        Uri uri = Uri.parse("http://bs.jiyehoo.com:81/DisWarn.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

}
