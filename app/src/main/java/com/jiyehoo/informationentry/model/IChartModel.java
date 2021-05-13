package com.jiyehoo.informationentry.model;

import com.jiyehoo.informationentry.bean.HistoryBean;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/11 上午11:38
 */
public interface IChartModel {
    void setHistoryBean(HistoryBean bean);
    HistoryBean getHistoryBean();

    // 湿度
    void setHumidityBean(HistoryBean bean);
    HistoryBean getHumidityBean();

    // 降水量
    void setRainHistoryBean(HistoryBean bean);
    HistoryBean getRainHistoryBean();

    // 地磁X
    void setMagnetismX(HistoryBean bean);
    HistoryBean getMagnetismX();

    // 地磁Y
    void setMagnetismY(HistoryBean bean);
    HistoryBean getMagnetismY();



}
