package com.jiyehoo.informationentry.util;

import android.content.Context;

import com.jiajie.load.LoadingDialog;

public class LoadingDialogUtil {
    private final LoadingDialog loadingDialog;

    public LoadingDialogUtil(Context context) {
        loadingDialog = new LoadingDialog.Builder(context).loadText("加载中...").build();

    }
    public void showLoading(boolean haveShow) {
        if (haveShow) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }
}
