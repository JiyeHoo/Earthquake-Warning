package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.DialogInterface;

import com.jiyehoo.informationentry.adapter.NoticeAdapter;
import com.jiyehoo.informationentry.adapter.OnNoticeItemClickListener;
import com.jiyehoo.informationentry.model.INoticeModel;
import com.jiyehoo.informationentry.model.NoticeModel;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.INoticeView;
import com.tuya.smart.android.user.api.IBooleanCallback;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.bean.message.MessageBean;
import com.tuya.smart.sdk.bean.message.MessageListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author JiyeHoo
 * @description: 通知功能的逻辑
 * @date :2021/5/18 上午12:42
 */
public class NoticePresenter {
    private final String TAG = "###NoticePresenter";
    private final Context context;
    private final INoticeView view;
    private final INoticeModel model;

    public NoticePresenter(INoticeView view) {
        context = (Context) view;
        this.view = view;
        model = new NoticeModel();
    }

    /**
     * 获取通知数据
     */
    public void initData() {
        view.showLoading(true);
//        TuyaHomeSdk.getMessageInstance().getMessageList(new ITuyaDataCallback<List<MessageBean>>() {
//            @Override
//            public void onSuccess(List<MessageBean> result) {
//                result.forEach(messageBean -> {
//                    MyLog.d(TAG, "消息内容:" + messageBean.getMsgContent());
//                    MyLog.d(TAG, "消息 icon:" + messageBean.getIcon());
//                    MyLog.d(TAG, "时间:" + messageBean.getDateTime());
//                });
//            }
//            @Override
//            public void onError(String errorCode, String errorMessage) {}
//        });

        // 获取 20 条就够了

        TuyaHomeSdk.getMessageInstance().getMessageList(20, 20, new ITuyaDataCallback<MessageListBean>() {
            @Override
            public void onSuccess(MessageListBean result) {
                view.showToast("获取通知成功");
                view.showLoading(false);
                if (null == result.getDatas() || result.getDatas().size() == 0) {
                    view.showNoNoticeTip(true);
                }
                // 存入 model
                model.setNoticeList(result.getDatas());
                // 将数据载入 rv 显示
                initAdapter();

                result.getDatas().forEach(messageBean -> {
                    MyLog.d(TAG, "消息内容:" + messageBean.getMsgContent());
                    MyLog.d(TAG, "消息类型名称:" + messageBean.getMsgTypeContent());
                });
            }
            @Override
            public void onError(String errorCode, String errorMessage) {
                MyLog.d(TAG, "获取消息失败:" + errorMessage);
                view.showToast("获取通知失败:" + errorMessage);
                view.showLoading(false);
                view.showNoNoticeTip(true);
            }
        });
    }

    /**
     * 数据显示到 rv
     */
    private void initAdapter() {
        NoticeAdapter adapter = new NoticeAdapter(model.getNoticeList());

        adapter.setOnNoticeItemClickListener(new OnNoticeItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                MyLog.d(TAG, "点击了:" + pos);
            }

            @Override
            public void onItemLongClick(int pos) {
                MyLog.d(TAG, "长按了:" + pos);
                // 删除 pos 的消息
                view.showDialog("是否删除该消息", "删除之后无法恢复!", (dialog, which) -> deleteNotice(pos));
            }
        });

        view.showRv(adapter);
    }

    /**
     * 清空所有通知
     */
    public void deleteAllNotice() {
        view.showLoading(true);
        if (null == model.getNoticeList() || model.getNoticeList().size() == 0) {
            MyLog.d(TAG, "消息为空，不执行清空操作");
            view.showToast("消息为空");
            view.showLoading(false);
            return;
        }

        List<String> mIdList = new ArrayList<>();
        model.getNoticeList().forEach(messageBean -> mIdList.add(messageBean.getId()));
        TuyaHomeSdk.getMessageInstance().deleteMessages(mIdList, new IBooleanCallback() {
            @Override
            public void onSuccess() {
                view.showLoading(false);
                MyLog.d(TAG, "清空消息成功");
                view.showToast("清空消息成功");
                view.rvRemoveAll();
                initData();
            }
            @Override
            public void onError(String code, String errorMsg) {
                view.showLoading(false);
                MyLog.d(TAG, "清空消息失败:" + errorMsg);
                view.showToast("清空消息失败:" + errorMsg);
            }
        });
    }

    /**
     * 删除指定消息
     */
    private void deleteNotice(int pos) {
        view.showLoading(true);
        List<String> mIdList = new ArrayList<>();
        mIdList.add(model.getNoticeList().get(pos).getId());
        TuyaHomeSdk.getMessageInstance().deleteMessages(mIdList, new IBooleanCallback() {
            @Override
            public void onSuccess() {
                view.showLoading(false);
                MyLog.d(TAG, "删除消息成功");
                view.showToast("删除消息成功");
                view.rvRemoveAll();
                initData();
            }
            @Override
            public void onError(String code, String errorMsg) {
                view.showLoading(false);
                MyLog.d(TAG, "删除消息失败:" + errorMsg);
                view.showToast("删除消息失败:" + errorMsg);
            }
        });
    }


}
