package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.jiyehoo.informationentry.view.ISetView;
import com.tuya.smart.android.user.api.ILogoutCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public class SetPresenter {
    private final String TAG = "SetPresenter";

    private Context mContext;
    private ISetView view;
//    private ISetModel model;

    public SetPresenter(ISetView view) {
        this.view = view;
        mContext = (Context) view;
//        model = new SetModel();
    }

    public void loginOut() {
        TuyaHomeSdk.getUserInstance().logout(new ILogoutCallback() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "退出成功");
                Intent intent = new Intent("com.jiyehoo.broadcastoffline.OFFLINE");
                mContext.sendBroadcast(intent);
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                view.showToast("退出失败:" + errorMsg);
            }
        });
    }

}
