package com.jiyehoo.informationentry;

import android.app.Application;
import android.util.Log;

import com.tuya.smart.home.sdk.TuyaHomeSdk;

public final class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("###BaseApplication", "start");
        TuyaHomeSdk.init(this);
        TuyaHomeSdk.setDebugMode(true);

    }
}
