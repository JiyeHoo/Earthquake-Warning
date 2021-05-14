package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.model.SetSpModel;
import com.jiyehoo.informationentry.util.FingerUtil;
import com.jiyehoo.informationentry.view.ILoginView;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class LoginPresenter {
    private final String TAG = "LoginPresenter";

    private final Context mContext;
    private final ILoginView view;
    private SharedPreferences preferences;

    public LoginPresenter(ILoginView view) {
        this.view = view;
        mContext = (Context) view;
    }

    /**
     * 指纹登录
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void fingerLogin() {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean haveRemember = preferences.getBoolean("haveRemember", false);
        // 没有记住密码则不进入指纹
        if (!haveRemember) {
            return;
        }

        if (SetSpModel.getIsFingerOpen(mContext)) {
            boolean isFingerOpen = SetSpModel.getIsFingerOpen(mContext);
            Log.d(TAG, "获取指纹设置:" + isFingerOpen);
            if (isFingerOpen) {
                BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        // 5次属于错误
                        Log.d(TAG, "指纹错误 " + errString);
                        view.showToast("指纹开启失败，过段时间再试吧！");
                    }

                    @Override
                    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // 成功
                        Log.d(TAG, "指纹通过 " + result.toString());
                        login(view.getUserName(), view.getPwd());
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        // 单次失败
                        Log.d(TAG, "指纹失败");
                    }
                };
                FingerUtil fingerUtil = new FingerUtil(mContext, "验证指纹", "用于验证身份自动登录", callback);
                fingerUtil.startFinger();
            }
        }

    }

    /**
     * 登录
     */
    public void login(String email, String pwd) {
        // 禁用按钮
        view.disableBtn();
        // 显示加载
        view.showLoading();

        TuyaHomeSdk.getUserInstance().loginWithEmail(mContext.getString(R.string.country_code), email, pwd, new ILoginCallback() {
            @Override
            public void onSuccess(User user) {
                Log.d(TAG, "登录成功");
                // 记住密码
                rememberPwd();
                view.disShowLoading();
                view.showToast(mContext.getString(R.string.login_success));
                view.gotoMainActivity();
            }

            @Override
            public void onError(String code, String error) {
                Log.d(TAG, "登录失败:" + error);
                view.disShowLoading();
                view.ableBtn();
                view.showToast(mContext.getString(R.string.login_error) + error);
            }
        });
    }
    /**
     * 初始化本地储存
     */
    public void initPref() {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean haveRemember = preferences.getBoolean("haveRemember", false);
        if (haveRemember) {
            String userName = preferences.getString("userName", "");
            String pwd = preferences.getString("pwd", "");
            view.loadPwd(userName, pwd);
        }
    }

    private void rememberPwd() {
        Log.d(TAG, "进入记住密码功能");
        SharedPreferences.Editor prefEdit = preferences.edit();
//        final View v = LayoutInflater.from(mContext).inflate(R.layout.activity_login, null);
//        CheckBox mCbRememberPwd = v.findViewById(R.id.cb_login_remember_pwd);
        if (view.getCheckBoxState()) {
            prefEdit.putBoolean("haveRemember", true);
            prefEdit.putString("userName", view.getUserName());
            prefEdit.putString("pwd", view.getPwd());
        } else {
            prefEdit.clear();
        }
        prefEdit.apply();
    }

}
