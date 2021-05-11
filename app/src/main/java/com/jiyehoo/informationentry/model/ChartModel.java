package com.jiyehoo.informationentry.model;

import android.util.Log;

import com.jiyehoo.informationentry.bean.HistoryBean;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/11 上午11:37
 */
public class ChartModel implements IChartModel{

    private final String TAG = "###ChartModel";
    private HistoryBean historyBean;

    private HistoryBean rainHistoryBean;
    private HistoryBean humidityBean;


    public ChartModel() {
        // todo 初始化图表数据
    }


    @Override
    public void setHistoryBean(HistoryBean bean) {
        historyBean = bean;
    }

    @Override
    public HistoryBean getHistoryBean() {
        if (null != historyBean) {
            return historyBean;
        } else {
            Log.d(TAG, "model 中 bean 为空");
            return null;
        }
    }

    @Override
    public void setHumidityBean(HistoryBean bean) {
        humidityBean = bean;
    }

    @Override
    public HistoryBean getHumidityBean() {
        return humidityBean;
    }

    @Override
    public void setRainHistoryBean(HistoryBean bean) {
        rainHistoryBean = bean;
    }

    @Override
    public HistoryBean getRainHistoryBean() {
        return rainHistoryBean;
    }
}
