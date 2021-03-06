package com.jiyehoo.informationentry.presenter;

import android.content.Context;

import com.jiyehoo.informationentry.adapter.DisasterAdapter;
import com.jiyehoo.informationentry.model.DisasterModel;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.model.IDisasterModel;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.IDisasterView;
import com.qweather.sdk.bean.WarningBean;
import com.qweather.sdk.view.QWeather;

import java.util.List;

/**
 * @author JiyeHoo
 * @description: 灾害模块处理逻辑
 * @date :2021/5/17 上午12:41
 */
public class DisasterPresenter {
    private static final String TAG = "###DisasterPresenter";

    private final Context context;
    private final IDisasterView view;
    private final IDisasterModel model;

    public DisasterPresenter(IDisasterView view) {
        context = (Context) view;
        this.view = view;
        model = new DisasterModel();
    }

    /**
     * 从 sp 获取经纬度，获取灾害数据
     */
    public void getDisasterInfo() {
        MyLog.d(TAG, "开始获取灾害数据");
        view.showLoading(true);
        // 获取经纬度
        double lon = HomeModel.getLon(context);
        double lat = HomeModel.getLat(context);
        String localStr = lon + "," + lat;
        MyLog.d(TAG, "开始请求预警数据，经纬度:" + localStr);

        // 获取数据
        QWeather.getWarning(context, localStr, new QWeather.OnResultWarningListener() {
            @Override
            public void onError(Throwable throwable) {
                MyLog.d(TAG, "请求灾害数据失败");
                view.showLoading(false);
            }

            @Override
            public void onSuccess(WarningBean warningBean) {
                MyLog.d(TAG, "请求灾害数据成功");
                view.showToast("数据获取成功");
                view.showLoading(false);
                List<WarningBean.WarningBeanBase> baseList = warningBean.getWarningList();

                view.showNoDisasterTip(null == baseList || baseList.size() == 0);

                // todo 将 预警信息 list 存入 model
                model.setBaseList(baseList);
                initAdapter(baseList);
//                baseList.forEach(warningBeanBase -> {
//                    MyLog.d(TAG, "发布时间：" + warningBeanBase.getPubTime());
//                    MyLog.d(TAG, "灾害标题：" + warningBeanBase.getTitle());
//                    MyLog.d(TAG, "发布单位：" + warningBeanBase.getSender());
//                    MyLog.d(TAG, "开始时间：" + warningBeanBase.getStartTime());
//                    MyLog.d(TAG, "结束时间：" + warningBeanBase.getEndTime());
//                    MyLog.d(TAG, "预警状态：" + warningBeanBase.getStatus());
//                    MyLog.d(TAG, "预警详情：" + warningBeanBase.getText());
//                    MyLog.d(TAG, "预警等级：" + warningBeanBase.getLevel());
//                });
            }
        });

    }

    /**
     * 将获取的数据放入 Adapter
     */
    private void initAdapter(List<WarningBean.WarningBeanBase> baseList) {
        DisasterAdapter adapter = new DisasterAdapter(baseList);
        view.showRv(adapter);
    }
}
