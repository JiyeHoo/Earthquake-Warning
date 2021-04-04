package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.jiyehoo.informationentry.LoginActivity;
import com.jiyehoo.informationentry.activity.ResetPwdActivity;
import com.jiyehoo.informationentry.view.IResetPwdView;
import com.tuya.smart.android.user.api.IResetPasswordCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

public class ResetPwdPresenter {
    private final String TAG = "ResetPwdPresenter";

    private final Context mContext;
    private final IResetPwdView view;

    public ResetPwdPresenter(IResetPwdView view) {
        this.view = view;
        mContext = (Context) view;
    }

    // 发送验证码
    public void sendCode() {
        String email = view.getEmail();
        if (!TextUtils.isEmpty(email) && email.length() > 0) {
            TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", "86", 3, new IResultCallback() {
                @Override
                public void onError(String code, String error) {
                    Log.d(TAG, "发送验证码失败:" + error);
                    view.showToast("验证码发送失败:" + error);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "发送验证码成功");
                    view.showToast("验证码发送成功");
                }
            });
        } else {
            Log.d(TAG, "Email 为空");
            view.showToast("邮箱不能为空");
        }
    }

    // 重置密码
    public void resetPwd() {
        String email = view.getEmail();
        String pwd = view.getPwd();
        String code = view.getCode();

        if (TextUtils.isEmpty(email)) {
            view.showToast("邮箱不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 18) {
            view.showToast("密码长度不符合");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            view.showToast("请输入验证码");
            return;
        }

        TuyaHomeSdk.getUserInstance().resetEmailPassword("86", email, code, pwd,
                new IResetPasswordCallback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "密码重置成功");
                        view.showToast("密码重置成功");
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onError(String code, String error) {
                        Log.d(TAG, "密码重置失败:" + error);
                        view.showToast("失败:" + error);
                    }
                });
    }


}
