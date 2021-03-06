package com.jiyehoo.informationentry.model;

import android.util.Log;

import com.jiyehoo.informationentry.bean.HistoryBean;
import com.jiyehoo.informationentry.util.MyLog;

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

    private HistoryBean magnetismXBean;
    private HistoryBean magnetismYBean;


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
            MyLog.d(TAG, "model 中 bean 为空");
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

    @Override
    public void setMagnetismX(HistoryBean bean) {
        magnetismXBean = bean;
    }

    @Override
    public HistoryBean getMagnetismX() {
        return magnetismXBean;
    }

    @Override
    public void setMagnetismY(HistoryBean bean) {
        magnetismYBean = bean;
    }

    @Override
    public HistoryBean getMagnetismY() {
        return magnetismYBean;
    }
}
