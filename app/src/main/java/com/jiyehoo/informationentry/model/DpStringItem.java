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

/**
 * @author JiyeHoo
 * @description: String 类型的 dp item
 * @date :21-4-28 上午12:29
 */
public class DpStringItem extends CardView {
    private final String TAG = "###DpStringItem";

    public DpStringItem(Context context, SchemaBean schemaBean, Object strValue, ITuyaDevice device) {
        super(context);
        String dpValue = "null";
        if (null == strValue) {
            dpValue = "null";
            MyLog.d(TAG, "dp 为空：" + schemaBean.getName());
        } else {
            try {
                dpValue = String.valueOf(strValue);
            } catch (Exception e) {
                MyLog.e(TAG, "数据类型错误:" + schemaBean.getName() + strValue);
            }
        }

        CardView.inflate(context, R.layout.item_ctrl_string, this);

        // 获取 dp 值
        String dpName = schemaBean.getName();


        // 绑定布局
        TextView mTvName = findViewById(R.id.tv_ctrl_item_string_name);
        TextView mTvValue = findViewById(R.id.tv_ctrl_item_value_string);

        // 显示
        mTvName.setText(dpName);
        mTvValue.setText(dpValue);

    }


}
