package com.jiyehoo.informationentry.view;

import android.content.DialogInterface;

import com.jiyehoo.informationentry.adapter.NoticeAdapter;
import com.tuya.smart.sdk.bean.message.MessageBean;
import com.tuya.smart.sdk.bean.message.MessageListBean;

import java.util.List;

/**
 * @author JiyeHoo
 * @description: 通知的 view
 * @date :2021/5/18 上午12:41
 */
public interface INoticeView {
    void showRv(NoticeAdapter adapter);
    // 没有信息时候显示 Tv 的提示
    void showNoNoticeTip(boolean isShow);
    // 下拉刷新显示
    void showLoading(boolean isSHow);
    // 吐司
    void showToast(String msg);
    // dialog
    void showDialog(String title, String msg, DialogInterface.OnClickListener listener);
    // 清空 rv
    void rvRemoveAll();
}
