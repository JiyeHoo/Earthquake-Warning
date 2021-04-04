package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.view.ILoginView;
import com.tuya.smart.android.user.api.ILoginCallback;
import com.tuya.smart.android.user.bean.User;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import java.time.LocalDate;

public class LoginPresenter {
    private final String TAG = "LoginPresenter";

    private Context mContext;
    private final ILoginView view;
    private SharedPreferences preferences;


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
                // 记住密码
                rememberPwd();
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
    /**
     * 初始化本地储存
     */
    public void initPref() {
        preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean haveRemember = preferences.getBoolean("haveRemember", false);
        if (haveRemember) {
            Log.d(TAG, "开始load 记住的密码");
            String userName = preferences.getString("userName", "");
            String pwd = preferences.getString("pwd", "");
            view.loadPwd(userName, pwd);
        } else {
            Log.d(TAG, "没有记住的密码");
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
            Log.d(TAG, "登录之后记住密码");
        } else {
            prefEdit.clear();
        }
        prefEdit.apply();
    }

}
