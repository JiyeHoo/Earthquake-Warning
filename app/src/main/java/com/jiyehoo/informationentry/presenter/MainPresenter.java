package com.jiyehoo.informationentry.presenter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.model.IMainModel;
import com.jiyehoo.informationentry.model.MainModel;
import com.jiyehoo.informationentry.util.HttpUtil;
import com.jiyehoo.informationentry.view.IMainView;
import com.tuya.smart.android.user.api.IReNickNameCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainPresenter {
    private final String TAG = "MainPresenter";

    private Context mContext;
    private IMainView view;
    private IMainModel model;

    public MainPresenter(IMainView view) {
        this.view = view;
        mContext = (Context) view;
        model = new MainModel();
    }

    public void updateNickName() {
        final View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_update_nikname, null);
        EditText editText = v.findViewById(R.id.et_nav_update_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("修改昵称")
                .setView(v)
                .setPositiveButton("确定", (dialog, which) -> {
                    String name = editText.getText().toString();
                    if (!TextUtils.isEmpty(name) && name.length() > 0) {
                        TuyaHomeSdk.getUserInstance().updateNickName(name, new IReNickNameCallback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "修改昵称成功");
                                view.showToast("修改成功");
                                setUserInfo();
                            }

                            @Override
                            public void onError(String code, String error) {
                                Log.d(TAG, "修改昵称失败");
                                view.showToast("修改失败：" + error);
                            }
                        });
                    } else {
                        view.showToast("昵称不得为空");
                    }
                })
                .show();
    }

    public void setOneText() {
        String url = "https://v1.hitokoto.cn/?c=i&encode=text&length=12";
        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG, "一言请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String s = Objects.requireNonNull(response.body()).string();
                model.setOneTextString(s);
                view.showOneTextToTv(model.getOneTextString());
            }
        });

    }

    public void setUserInfo() {
        model.getUserInfo();
        view.showUserInfo(model.getUserInfoName(), model.getUserInfoEmail(), model.getUserInfoHeadPicUrl());
    }
}
