package com.jiyehoo.informationentry.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.jiyehoo.informationentry.R;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.sdk.api.ITuyaDevice;

/**
 * @author JiyeHoo
 * @description: String 类型的 dp item
 * @date :21-4-28 上午12:29
 */
public class DpStringItem extends CardView {
    private final String TAG = "###DpStringItem";

    public DpStringItem(Context context, SchemaBean schemaBean, Object intValue, ITuyaDevice device) {
        super(context);
        String dpValue = (String) intValue;

        CardView.inflate(context, R.layout.item_ctrl_string, this);

        // 获取 dp 值
        String dpName = schemaBean.getName();


        // 绑定布局
        TextView mTvName = findViewById(R.id.tv_ctrl_item_string_name);
        TextView mTvValue = findViewById(R.id.tv_ctrl_item_value_string);

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
