package com.jiyehoo.informationentry;

import android.app.Application;

import com.jiyehoo.informationentry.util.MyLog;
import com.qweather.sdk.view.HeConfig;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

public final class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        String TAG = "###BaseApplication";

        // 初始化 tuya
        MyLog.d(TAG, "Tuya init");
        TuyaHomeSdk.init(this);
        TuyaHomeSdk.setDebugMode(true);

        // 初始化HeWeather
        MyLog.d(TAG, "HeWeather init");
        HeConfig.init("HE2105162328331063", "49af3d17899f4b509d4b39a2b77e5e19");
        HeConfig.switchToDevService();
    }
}
