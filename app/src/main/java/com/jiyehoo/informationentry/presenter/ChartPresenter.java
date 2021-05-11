package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.jiyehoo.informationentry.bean.HistoryBean;
import com.jiyehoo.informationentry.model.ChartModel;
import com.jiyehoo.informationentry.model.IChartModel;
import com.jiyehoo.informationentry.util.TimeUtil;
import com.jiyehoo.informationentry.view.IChartView;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDataCallback;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/11 上午11:35
 */
public class ChartPresenter {

    private final String TAG = "###ChartPresenter";

    private final Context mContext;
    private final IChartView mView;
    private final IChartModel mModel;

    public ChartPresenter(IChartView view) {
        mContext = (Context) view;
        mView = view;
        mModel = new ChartModel();
    }

    public void getDataLine() {
        // 获取 土壤湿度 和 降雨量指数 两个 dp 的历史数据
        Log.d(TAG, "获取 line 的数据");

        Map<String, Object> map = new HashMap<>();
        map.put("devId", "6ca4f3101238542849bago");
        map.put("dpIds", "104"); // dp 点
        map.put("offset", 0); // 分页偏移量
        map.put("limit", 5); // 分页大小,即多少个 dp 数据

        TuyaHomeSdk.getRequestInstance().requestWithApiName(
                "tuya.m.smart.operate.all.log",
                "1.0", map, String.class,
                new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, "请求湿度历史成功:" + result);
                        Gson gson = new Gson();
                        HistoryBean humidityBean = gson.fromJson(result, HistoryBean.class);
                        // 存入 model
                        mModel.setHumidityBean(humidityBean);

                        map.put("dpIds", "105"); // dp 点
                        TuyaHomeSdk.getRequestInstance().requestWithApiName(
                                "tuya.m.smart.operate.all.log",
                                "1.0", map, String.class,
                                new ITuyaDataCallback<String>() {
                                    @Override
                                    public void onSuccess(String result) {
                                        Log.d(TAG, "请求降水量历史成功:" + result);

                                        Gson gson = new Gson();
                                        HistoryBean rainBean = gson.fromJson(result, HistoryBean.class);
                                        // 存入 bean
                                        mModel.setRainHistoryBean(rainBean);

                                        showLineChartMore();
                                    }

                                    @Override
                                    public void onError(String s, String s1) {
                                        Log.d(TAG, "请求降水量历史失败");
                                    }
                                });
                    }

                    @Override
                    public void onError(String s, String s1) {
                        Log.d(TAG, "请求湿度历史失败");
                    }
                });
    }

    /**
     * 获取历史 DP 数据
     */
    public void getHistory() {
        // 返回的数据是从近到远
        // 设备2 devId:6ca4f3101238542849bago
        Log.d(TAG, "开始查询历史");
        // 测试时间工具，这里需要加上000(ms)，放在了 Util 里了
//        Log.d(TAG, "stamp to time:" + TimeUtil.stampToDate(1620570646));
//        try {
//            Log.d(TAG, "time to stamp:" + TimeUtil.dateToStamp("2021-05-09 22:30:46"));
//        } catch (Exception e) {
//            Log.d(TAG, "time to stamp error");
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("devId", "6ca4f3101238542849bago");
        map.put("dpIds", "1,101,102,103,104,105,106,107,108,109,110"); // dp 点
        map.put("offset", 0); // 分页偏移量
        map.put("limit", 20); // 分页大小,即多少个 dp 数据
        // 当前时间的时间戳
//        long timeMillis = Calendar.getInstance().getTimeInMillis();
//        String timeMsString = String.valueOf(timeMillis);
//        map.put("startTime", timeMsString); // 开始时间的时间戳，为 ms，String


        TuyaHomeSdk.getRequestInstance().requestWithApiName(
                "tuya.m.smart.operate.all.log",
                "1.0", map, String.class,
                new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, "请求历史成功:" + result);
                        // json 解析到实体类 HistoryBean
                        Log.d(TAG, "开始解析 JSON");
                        Gson gson = new Gson();
                        HistoryBean historyBean = gson.fromJson(result, HistoryBean.class);
                        // 将数据放入 model
                        mModel.setHistoryBean(historyBean);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        Log.d(TAG, "请求历史失败");
                    }
                });
    }

    /**
     * 显示条状图
     */
    public void showBarChartMore() {
        Log.d(TAG, "开始处理 bar 图表");

        List<Float> xAxisValues = new ArrayList<>();
        List<List<Float>> yAxisValues = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<Integer> colours = new ArrayList<>();
        List<Float> x1 = new ArrayList<>();
        List<Float> x2 = new ArrayList<>();
        xAxisValues.add(1.0f);
        xAxisValues.add(2.0f);
        xAxisValues.add(3.0f);
        xAxisValues.add(4.0f);
        xAxisValues.add(5.0f);

        x1.add(10f);
        x1.add(20f);
        x1.add(30f);
        x1.add(40f);
        x1.add(50f);

        x2.add(50f);
        x2.add(40f);
        x2.add(30f);
        x2.add(20f);
        x2.add(10f);

        yAxisValues.add(x1);
        yAxisValues.add(x2);
        labels.add("");
        labels.add("");
        colours.add(Color.parseColor("#123456"));
        colours.add(Color.parseColor("#987654"));

        mView.showBarChart(xAxisValues, yAxisValues, labels, colours);
    }

    /**
     * 显示线性图
     */
    public void showLineChartMore() {
        Log.d(TAG, "开始处理 Line 图表");

        // todo x文字载入，这里使用日期时间
        List<String> xLabels = new ArrayList<>();
        List<HistoryBean.Dps> rainList = mModel.getRainHistoryBean().getDps();

        List<Float> y2Value = new ArrayList<>();

        // 设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();

        // x 标题和降水量
        rainList.forEach(dps -> {
            // 遍历 dps
            String rainTime = dps.getTimeStr().substring(11);
            Log.d(TAG, rainTime);
            xLabels.add(rainTime);

            String rainValue = dps.getValue();
            Log.d(TAG, "降雨量：" + rainValue);
            y2Value.add(Float.parseFloat(rainValue));
        });

        // 湿度
        List<HistoryBean.Dps> humidityList = mModel.getHumidityBean().getDps();
        List<Float> y1Value = new ArrayList<>();
        humidityList.forEach(dps -> {
            String humidityValue = dps.getValue();
            Log.d(TAG, "湿度：" + humidityValue);
            y1Value.add(Float.parseFloat(humidityValue));
        });

        // x 的位置
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            xValues.add((float) i);
        }

        List<String> labelNameList = new ArrayList<>();
        labelNameList.add("土壤湿度");
        labelNameList.add("降雨量指数");

        yValues.add(y1Value);
        yValues.add(y2Value);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#6785f2"));
        colorList.add(Color.parseColor("#eecc44"));

        mView.showLineChart(xValues, yValues, labelNameList, xLabels, colorList);
    }

    /**
     * 显示饼图
     */
    public void showPieChart() {
        Log.d(TAG, "开始处理 Pie 图表");

        //设置每份所占数量
        List<PieEntry> yVals = new ArrayList<>();
        yVals.add(new PieEntry(2.0f, "针叶树类"));
        yVals.add(new PieEntry(6.0f, "阔叶乔木类"));
        yVals.add(new PieEntry(4.0f, "阔叶灌木类"));
        yVals.add(new PieEntry(3.0f, "匍匐类"));
        yVals.add(new PieEntry(1.0f, "其它"));
        //设置每份的颜色
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#6785f2"));
        colors.add(Color.parseColor("#675cf2"));
        colors.add(Color.parseColor("#496cef"));
        colors.add(Color.parseColor("#aa63fa"));
        colors.add(Color.parseColor("#f5a658"));

        mView.showPieChart(yVals, colors);
    }

    /**
     * 显示雷达图
     */
    public void showRadarChart() {
        Log.d(TAG, "开始处理 Radar 图表");

        List<String> xData = new ArrayList<>();
        List<List<Float>> yDatas = new ArrayList<>();
        List<String> names = new ArrayList<>();

        names.add("氮肥");
        names.add("磷肥");
        names.add("钾肥");
        names.add("复合肥");
        names.add("农家肥");
        names.add("其他");

        xData.add("总体");
        xData.add("针叶树类");
        xData.add("阔叶乔木类");
        xData.add("阔叶灌木类");
        xData.add("匍匐类");
        xData.add("其它");

        List<Integer> colors = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            List<Float> nData = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                nData.add((float) (Math.random() * 20));
            }
            yDatas.add(nData);
        }

        colors.add(Color.parseColor("#fbd06a"));
        colors.add(Color.parseColor("#f69a40"));
        colors.add(Color.parseColor("#ff5d52"));
        colors.add(Color.parseColor("#e71f19"));
        colors.add(Color.parseColor("#ff9b43"));
        colors.add(Color.parseColor("#8eb9fb"));

        mView.showRadarChart(xData, yDatas, names, colors);
    }

}
