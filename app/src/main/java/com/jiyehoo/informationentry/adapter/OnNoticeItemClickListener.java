package com.jiyehoo.informationentry.adapter;

import com.tuya.smart.sdk.bean.DeviceBean;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/18 下午2:14
 */
public interface OnNoticeItemClickListener {
    void onItemClick(int pos);

    void onItemLongClick(int pos);
}
