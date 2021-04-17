package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jiyehoo.informationentry.R;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.sdk.api.ITuyaDevice;

/**
 * 使用构造函数传入 dp 值
 */

public class DpBooleanItem extends CardView {

    public DpBooleanItem(Context context, SchemaBean schemaBean, Object boolValue, ITuyaDevice device) {
        super(context);
        boolean dpValue = (Boolean)boolValue;

        CardView.inflate(context, R.layout.item_ctrl_boolean, this);

        // 获取 dp 值
        String dpName = schemaBean.getName();


        // 绑定布局
        TextView mTvName = findViewById(R.id.tv_ctrl_item_boolean_name);

        // 显示
        mTvName.setText(dpName);

    }
}
