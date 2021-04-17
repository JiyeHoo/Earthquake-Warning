package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

import com.jiyehoo.informationentry.R;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.sdk.api.ITuyaDevice;

public class DpValueItem extends CardView {
    private final String TAG = "###DpValueItem";

    public DpValueItem(Context context, SchemaBean schemaBean, Object intValue, ITuyaDevice device) {
        super(context);
        int dpValue = (int)intValue;

        CardView.inflate(context, R.layout.item_ctrl_value, this);

        // 获取 dp 值
        String dpName = schemaBean.getName();


        // 绑定布局
        TextView mTvName = findViewById(R.id.tv_ctrl_item_value_name);
        TextView mTvValue = findViewById(R.id.tv_ctrl_item_value_int);

        // 显示
        mTvName.setText(dpName);
        if (!TextUtils.isEmpty(String.valueOf(dpValue))) {
            mTvValue.setText(String.valueOf(dpValue));
        } else {
            Log.d(TAG, "value 为空:" + dpName);
            mTvValue.setText("null");
        }

    }
}
