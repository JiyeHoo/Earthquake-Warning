package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.util.MyLog;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.sdk.api.ITuyaDevice;

public class DpEnumItem extends CardView {
    private final String TAG = "###DpEnumItem";

    public DpEnumItem(Context context, SchemaBean schemaBean, Object enumValue, ITuyaDevice device) {
        super(context);
        String dpValue = enumValue.toString();

        CardView.inflate(context, R.layout.item_ctrl_enum, this);

        // 获取 dp 值
        String dpName = schemaBean.getName();

        // 绑定布局
        TextView mTvName = findViewById(R.id.tv_ctrl_item_enum_name);
        TextView mTvValue = findViewById(R.id.tv_ctrl_item_enum_value);

        // 显示
        mTvName.setText(dpName);
        if (!TextUtils.isEmpty(dpValue) && dpValue.length() > 0) {
            mTvValue.setText(dpValue);
        } else {
            MyLog.d(TAG, "value 为空:" + dpValue);
            mTvValue.setText("null");
        }

    }
}
