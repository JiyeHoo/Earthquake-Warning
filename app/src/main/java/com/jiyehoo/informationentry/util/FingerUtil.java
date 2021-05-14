package com.jiyehoo.informationentry.util;

import android.content.Context;
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
    private String title, text;
    private BiometricPrompt.AuthenticationCallback callback;

    public FingerUtil(Context context, String title, String text, BiometricPrompt.AuthenticationCallback callback) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void startFinger() {
        BiometricPrompt mBiometricPrompt = new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setDescription(text)
                .setNegativeButton("取消",
                        context.getMainExecutor(),
                        (dialogInterface, i) -> Log.i(TAG, "Cancel button clicked"))
                .build();

        CancellationSignal mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(() -> {
            // 取消
            Log.d(TAG, "Canceled");
        });

        mBiometricPrompt.authenticate(mCancellationSignal,
                context.getMainExecutor(),
                callback);

    }
}
