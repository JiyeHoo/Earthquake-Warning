package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import androidx.cardview.widget.CardView;

import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.util.MyLog;
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

        String valueStr = String.valueOf(dpValue);
        if (!TextUtils.isEmpty(valueStr)) {
            mTvValue.setText(setUnit(dpName, valueStr));
        } else {
            MyLog.d(TAG, "value 为空:" + dpName);
            mTvValue.setText("null");
        }


    }

    // 根据name处理单位
    private String setUnit(String dpName, String valueStr) {
        if (dpName.equals("电池电量")) {
            MyLog.d(TAG, "找到电量");
            valueStr = valueStr + " %";
            return valueStr;
        }
        // 没找到相应名字则返回原来的数值
        return valueStr;
    }

}
