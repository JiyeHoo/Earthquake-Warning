package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.jiyehoo.informationentry.LoginActivity;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.util.MyLog;
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
            TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", mContext.getString(R.string.country_code), 3, new IResultCallback() {
                @Override
                public void onError(String code, String error) {
                    MyLog.d(TAG, "发送验证码失败:" + error);
                    view.showToast(mContext.getString(R.string.reset_send_code_error) + error);
                }

                @Override
                public void onSuccess() {
                    MyLog.d(TAG, "发送验证码成功");
                    view.showToast(mContext.getString(R.string.reset_send_code_success));
                }
            });
        } else {
            MyLog.d(TAG, "Email 为空");
            view.showToast(mContext.getString(R.string.reset_toast_email_error));
        }
    }

    // 重置密码
    public void resetPwd() {
        String email = view.getEmail();
        String pwd = view.getPwd();
        String code = view.getCode();

        if (TextUtils.isEmpty(email)) {
            view.showToast(mContext.getString(R.string.reset_toast_email_error_null));
            return;
        }
        if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 18) {
            view.showToast(mContext.getString(R.string.reset_toast_pwd_error));
            return;
        }
        if (TextUtils.isEmpty(code)) {
            view.showToast(mContext.getString(R.string.reset_toast_code_error_null));
            return;
        }

        TuyaHomeSdk.getUserInstance().resetEmailPassword(mContext.getString(R.string.country_code), email, code, pwd,
                new IResetPasswordCallback() {
                    @Override
                    public void onSuccess() {
                        MyLog.d(TAG, "密码重置成功");
                        view.showToast(mContext.getString(R.string.reset_toast_reset_success));
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onError(String code, String error) {
                        MyLog.d(TAG, "密码重置失败:" + error);
                        view.showToast(mContext.getString(R.string.reset_toast_reset_error) + error);
                    }
                });
    }


}
