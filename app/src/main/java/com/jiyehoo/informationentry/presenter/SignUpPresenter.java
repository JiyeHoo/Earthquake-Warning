package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.util.Log;

import com.jiyehoo.informationentry.view.ISignUpView;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaUser;

public class SignUpPresenter {
    private final String TAG = "SignUpPresenter";

    private Context mContext;
    private ISignUpView mSignUpView;

    public SignUpPresenter(ISignUpView view) {
        mContext = (Context) view;
        mSignUpView = view;
    }

    /**
     * 发送验证码
     */
    public void getSignCode(String email) {
        TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", "86", 1, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                Log.d(TAG, "发送验证码失败:" + error);
                mSignUpView.showToast("验证码发送失败:" + error);
            }

            @Override
            public void onSuccess() {
                Log.d(TAG, "发送验证码成功:");
                mSignUpView.showToast("验证码发送成功");
            }
        });
    }

    /**
     * 注册
     */
    public void register(String email, String pwd, String code) {
        mSignUpView.showJumpBean();

        TuyaHomeSdk.getUserInstance().registerAccountWithEmail("86", email, pwd, code, new IRegisterCallback() {
            @Override
            public void onSuccess(User user) {
                mSignUpView.showToast("注册成功");
                mSignUpView.gotoLoginActivity();
            }

            @Override
            public void onError(String code, String error) {
                mSignUpView.showToast("注册失败:" + error);
            }
        });
    }
}
