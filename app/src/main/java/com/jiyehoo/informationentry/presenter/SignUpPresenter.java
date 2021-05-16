package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.util.Log;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.ISignUpView;
import com.tuya.smart.android.user.api.IRegisterCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IResultCallback;

public class SignUpPresenter {
    private final String TAG = "SignUpPresenter";

    private final Context mContext;
    private final ISignUpView mSignUpView;
//    private final Activity activity;

    public SignUpPresenter(ISignUpView view) {
        mContext = (Context) view;
        mSignUpView = view;
//        activity = (Activity) view;
    }

    /**
     * 发送验证码
     */
    public void getSignCode(String email) {
        TuyaHomeSdk.getUserInstance().sendVerifyCodeWithUserName(email, "", mContext.getString(R.string.country_code), 1, new IResultCallback() {
            @Override
            public void onError(String code, String error) {
                MyLog.d(TAG, "发送验证码失败:" + error);
                mSignUpView.showToast(mContext.getString(R.string.sign_up_toast_code_error) + error);
            }

            @Override
            public void onSuccess() {
                MyLog.d(TAG, "发送验证码成功:");
                mSignUpView.showToast(mContext.getString(R.string.sign_up_toast_code_success));
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
                mSignUpView.showToast(mContext.getString(R.string.sign_up_success));
                mSignUpView.gotoLoginActivity();
            }

            @Override
            public void onError(String code, String error) {
                mSignUpView.showToast(mContext.getString(R.string.sign_up_error) + error);
            }
        });
    }
}
