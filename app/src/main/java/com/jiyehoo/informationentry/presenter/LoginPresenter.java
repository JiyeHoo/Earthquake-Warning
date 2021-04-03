package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.util.Log;

import com.jiyehoo.informationentry.view.ILoginView;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class LoginPresenter {
    private final String TAG = "LoginPresenter";

    private Context mContext;
    private ILoginView view;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        mContext = (Context) view;
    }

    /**
     * 登录
     */
    public void login(String email, String pwd) {
        Log.d(TAG, "开始登录");
        // 禁用按钮
        view.disableBtn();
        // 显示加载
        view.showLoading();
        TuyaHomeSdk.getUserInstance().loginWithEmail("86", email, pwd, new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "登录成功");
                // todo sp本地记住密码
                view.disShowLoading();
                view.showToast("登录成功");
                view.gotoMainActivity();
            }

            @Override
            public void onError(String code, String error) {
                Log.d(TAG, "登录失败:" + error);
                view.disShowLoading();
                view.ableBtn();
                view.showToast("登录失败:" + error);
            }
        });
    }

}
