package com.jiyehoo.informationentry.util;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * @author JiyeHoo
 * @description: 指纹工具类
 * @date :2021/5/14 下午11:11
 */
public class FingerUtil {
    private final String TAG = "###FingerUtil";
    private final Context context;
    private final String title;
    private final String text;
    private final BiometricPrompt.AuthenticationCallback callback;
    private final DialogInterface.OnClickListener cancelCallback;

    public FingerUtil(Context context, String title, String text,
                      BiometricPrompt.AuthenticationCallback callback,
                      DialogInterface.OnClickListener cancelCallback) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.callback = callback;
        this.cancelCallback = cancelCallback;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void startFinger() {


        BiometricPrompt mBiometricPrompt = new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setDescription(text)
                .setNegativeButton("取消",
                        context.getMainExecutor(),
                        cancelCallback)
                .build();

        CancellationSignal mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(() -> {
            // 取消
            MyLog.d(TAG, "Canceled");
        });

        mBiometricPrompt.authenticate(mCancellationSignal,
                context.getMainExecutor(),
                callback);

    }
}
