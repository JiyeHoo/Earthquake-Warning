package com.jiyehoo.informationentry;

import android.app.Application;
import android.util.Log;

import com.jiyehoo.informationentry.util.MyLog;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public final class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyLog.d("###BaseApplication", "start");
        TuyaHomeSdk.init(this);
        TuyaHomeSdk.setDebugMode(true);

    }
}
