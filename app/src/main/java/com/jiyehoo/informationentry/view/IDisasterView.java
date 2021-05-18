package com.jiyehoo.informationentry.view;

import com.jiyehoo.informationentry.adapter.DisasterAdapter;

/**
 * @author JiyeHoo
 * @description: 灾害模块的视图接口
 * @date :2021/5/17 上午12:42
 */
public interface IDisasterView {
    // 将数据显示到 Rv
    void showRv(DisasterAdapter adapter);
    // 没有信息时候显示 Tv 的提示
    void showNoDisasterTip(boolean isShow);
    // 下拉刷新显示
    void showLoading(boolean isShow);
    // 吐司
    void showToast(String msg);
}
