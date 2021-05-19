package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.github.mikephil.charting.data.PieEntry;
import com.google.gson.Gson;
import com.jiyehoo.informationentry.bean.HistoryBean;
import com.jiyehoo.informationentry.model.ChartModel;
import com.jiyehoo.informationentry.model.IChartModel;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.IChartView;
import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDataCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author JiyeHoo
 * @description: 图表处理
 * @date :2021/5/11 上午11:35
 */
public class ChartPresenter {

    private final String TAG = "###ChartPresenter";

    private final Context mContext;
    private final IChartView mView;
    private final IChartModel mModel;

    private boolean isRainGetDate = false, isHumidityGetDate = false;
    private boolean isMagXGetDate = false, isMagYGetDate = false;

    public ChartPresenter(IChartView view) {
        mContext = (Context) view;
        mView = view;
        mModel = new ChartModel();
    }

    /**
     * 导出数据到文件
     */
    public void shareDataFile() {
        // todo 获取用于导出的数据，这里没有判断 null
        List<HistoryBean.Dps> rainList = mModel.getRainHistoryBean().getDps();
        List<HistoryBean.Dps> humidityList = mModel.getHumidityBean().getDps();
        List<HistoryBean.Dps> xList = mModel.getMagnetismX().getDps();
        List<HistoryBean.Dps> yList = mModel.getMagnetismY().getDps();

        StringBuilder builder = new StringBuilder();
        rainList.forEach(dps -> {
            builder.append("时间：").append(dps.getTimeStr())
                    .append("，DPId:").append(dps.getDpId())
                    .append("，Value：").append(dps.getValue()).append("\n");

            MyLog.d(TAG, "时间：" + dps.getTimeStr() + "，DpId:" + dps.getDpId() + "，内容：" + dps.getValue());
        });
        humidityList.forEach(dps -> {
            builder.append("时间：").append(dps.getTimeStr())
                    .append("，DPId:").append(dps.getDpId())
                    .append("，Value：").append(dps.getValue()).append("\n");

            MyLog.d(TAG, "时间：" + dps.getTimeStr() + "，DpId:" + dps.getDpId() + "，内容：" + dps.getValue());
        });
        xList.forEach(dps -> {
            builder.append("时间：").append(dps.getTimeStr())
                    .append("，DPId:").append(dps.getDpId())
                    .append("，Value：").append(dps.getValue()).append("\n");

            MyLog.d(TAG, "时间：" + dps.getTimeStr() + "，DpId:" + dps.getDpId() + "，内容：" + dps.getValue());
        });
        yList.forEach(dps -> {
            builder.append("时间：").append(dps.getTimeStr())
                    .append("，DPId:").append(dps.getDpId())
                    .append("，Value：").append(dps.getValue()).append("\n");

            MyLog.d(TAG, "时间：" + dps.getTimeStr() + "，DpId:" + dps.getDpId() + "，内容：" + dps.getValue());
        });

        FileOutputStream fos;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        try {
            fos = mContext.openFileOutput("raw_data", Context.MODE_APPEND);
            fos.write(builder.toString().getBytes());
            fos.close();
            MyLog.d(TAG, "保存文件成功");
//            mView.showToast("保存文件成功");
            dialogBuilder.setTitle("导出成功")
                    .setMessage("数据文件：/data/data/com.jiyehoo.InformationEntry/files/raw_data")
                    .setPositiveButton("OK", null)
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.d(TAG, "保存文件失败");
//            mView.showToast("保存文件失败");
            dialogBuilder.setTitle("导出失败")
                    .setMessage("保存文件发生错误！")
                    .setPositiveButton("确定", null)
                    .show();
        }
    }

    /**
     * 读取文件数据
     */
    public void readDataFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fis = mContext.openFileInput("raw_data");
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            stringBuilder.append(new String(buffer));
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.d(TAG, "读取失败");
        }
        MyLog.d(TAG, stringBuilder.toString());
        // 为空则不显示
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        if (TextUtils.isEmpty(stringBuilder.toString()) || stringBuilder.toString().length() == 0) {
//            mView.showToast("文件为空");
            dialogBuilder.setTitle("查看文件")
                    .setMessage("文件不存在，请先将数据导出至文件！")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            // 显示文件内容
            dialogBuilder.setTitle("查看文件\n/data/data/com.jiyehoo.InformationEntry/files/")
                    .setMessage(stringBuilder.toString())
                    .setPositiveButton("确定", null)
                    .setNegativeButton("删除", (dialog, which) -> deleteDataFile())
                    .show();
        }

    }

    /**
     * 删除保存文件
     */
    public void deleteDataFile() {
        MyLog.d(TAG, "开始删除文件");
        File file = new File(mContext.getFilesDir(), "raw_data");
        boolean isDelete = false;
        if (file.exists()) {
            isDelete = file.delete();
        }
        if (isDelete) {
            mView.showToast("删除文件成功");
        } else {
            mView.showToast("删除文件失败");
        }


//        FileOutputStream fos;
//        String s = "";
//        try {
//            fos = mContext.openFileOutput("chart_data", Context.MODE_PRIVATE);
//            fos.write(s.getBytes());
//            fos.close();
//            MyLog.d(TAG, "清空文件成功");
//            mView.showToast("清空文件成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//            MyLog.d(TAG, "清空文件失败");
//            mView.showToast("保存文件失败");
//        }
    }

    /**
     * 分享/打开文件
     */
    public void openDataFile() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        File file = new File(mContext.getFilesDir(), "raw_data");

        if (!file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("分享文件")
                    .setMessage("文件不存在，请先将数据导出至文件")
                    .setPositiveButton("确定", null)
                    .show();
        } else {
            // /data/data/com.jiyehoo.informationentry/files/chart_data
            uri = FileProvider.getUriForFile(mContext, "com.jiyehoo.informationentry.datafile", file);
            //pdf文件要被读取所以加入读取权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "text/plain");

            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 获取 line 图表所需的数据
     */
    public void getDataLine() {
        // 获取 土壤湿度 和 降雨量指数 两个 dp 的历史数据
        MyLog.d(TAG, "获取 line 的数据");

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
                        MyLog.d(TAG, "请求湿度历史成功:" + result);
                        HistoryBean humidityBean = new Gson().fromJson(result, HistoryBean.class);
                        // 存入
                        mModel.setHumidityBean(humidityBean);
                        isHumidityGetDate = true;
                        showLineChartMore();
                    }

                    @Override
                    public void onError(String s, String s1) {
                        MyLog.d(TAG, "请求湿度历史失败");
                        mView.showToast("请求湿度历史失败");
                        mView.showLoading(false);
                    }
                });

        map.put("dpIds", "105"); // 请求另一个 dp 点
        TuyaHomeSdk.getRequestInstance().requestWithApiName(
                "tuya.m.smart.operate.all.log",
                "1.0", map, String.class,
                new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog.d(TAG, "请求降水量历史成功:" + result);
                        HistoryBean rainBean = new Gson().fromJson(result, HistoryBean.class);
                        // 存入 bean
                        mModel.setRainHistoryBean(rainBean);
                        isRainGetDate = true;
                        showLineChartMore();
                        // 显示图表
//                        showLineChartMore();
                    }

                    @Override
                    public void onError(String s, String s1) {
                        MyLog.d(TAG, "请求降水量历史失败");
                        mView.showToast("请求降水量历史失败");
                        mView.showLoading(false);
                    }
                });
    }

    /**
     * 获取 bar 图表所需数据
     */
    public void getDataBar() {
        MyLog.d(TAG, "获取 bar 数据");
        Map<String, Object> map = new HashMap<>();
        map.put("devId", "6ca4f3101238542849bago");
        map.put("dpIds", "101"); // dp 点
        map.put("offset", 0); // 分页偏移量
        map.put("limit", 5); // 分页大小,即多少个 dp 数据

        TuyaHomeSdk.getRequestInstance().requestWithApiName(
                "tuya.m.smart.operate.all.log",
                "1.0", map, String.class,
                new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog.d(TAG, "请求地磁 X 历史成功:" + result);
                        // json 解析到实体类 HistoryBean
                        Gson gson = new Gson();
                        HistoryBean bean = gson.fromJson(result, HistoryBean.class);
                        // 将数据放入 model
                        mModel.setMagnetismX(bean);
                        isMagXGetDate = true;
                        showBarChartMore();
                    }

                    @Override
                    public void onError(String s, String s1) {
                        MyLog.d(TAG, "请求 地磁 X 历史失败");
                        mView.showToast("请求 地磁 X 历史失败");
                        mView.showLoading(false);
                    }
                });

        map.put("dpIds", "102"); // dp 点
        TuyaHomeSdk.getRequestInstance().requestWithApiName(
                "tuya.m.smart.operate.all.log",
                "1.0", map, String.class,
                new ITuyaDataCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        MyLog.d(TAG, "请求地磁 Y 历史成功:" + result);
                        // json 解析到实体类 HistoryBean
                        Gson gson = new Gson();
                        HistoryBean bean = gson.fromJson(result, HistoryBean.class);
                        // 将数据放入 model
                        mModel.setMagnetismY(bean);
                        isMagYGetDate = true;
                        showBarChartMore();
                    }

                    @Override
                    public void onError(String s, String s1) {
                        MyLog.d(TAG, "请求 地磁 Y 历史失败");
                        mView.showToast("请求 地磁 Y 历史失败");
                        mView.showLoading(false);
                    }
                });
    }

    /**
     * 获取历史 DP 数据
     */
    public void getHistory() {
        // 返回的数据是从近到远
        // 设备2 devId:6ca4f3101238542849bago
        MyLog.d(TAG, "开始查询历史");
        // 测试时间工具，这里需要加上000(ms)，放在了 Util 里了
//        MyLog.d(TAG, "stamp to time:" + TimeUtil.stampToDate(1620570646));
//        try {
//            MyLog.d(TAG, "time to stamp:" + TimeUtil.dateToStamp("2021-05-09 22:30:46"));
//        } catch (Exception e) {
//            MyLog.d(TAG, "time to stamp error");
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
                        MyLog.d(TAG, "请求历史成功:" + result);
                        // json 解析到实体类 HistoryBean
                        MyLog.d(TAG, "开始解析 JSON");
                        Gson gson = new Gson();
                        HistoryBean historyBean = gson.fromJson(result, HistoryBean.class);
                        // 将数据放入 model
                        mModel.setHistoryBean(historyBean);
                    }

                    @Override
                    public void onError(String s, String s1) {
                        MyLog.d(TAG, "请求历史失败");
                    }
                });
    }

    /**
     * 显示条状图
     */
    public void showBarChartMore() {
        if (!isMagXGetDate || !isMagYGetDate) {
            MyLog.d(TAG, "bar 数据未获取完");
            return;
        } else {
            MyLog.d(TAG, "bar 数据获取完整");
            isMagYGetDate = false;
            isMagXGetDate = false;
            mView.showLoading(false);
        }

        MyLog.d(TAG, "开始处理 bar 图表");

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
        // 获取 x 的日期
        List<String> labelList = new ArrayList<>();
        List<HistoryBean.Dps> magXDpsList = mModel.getMagnetismX().getDps();
        magXDpsList.forEach(dps -> {
            // 处理每一个 地磁 x 数据
            MyLog.d(TAG, "地磁x数据:" + dps.getValue());
            x1.add(Float.parseFloat(dps.getValue()) * -1);

            String timeStr = dps.getTimeStr().substring(11);
            MyLog.d(TAG, "日期:" + timeStr);
            labelList.add(timeStr);
        });

        List<HistoryBean.Dps> maxYDpsList = mModel.getMagnetismY().getDps();
        maxYDpsList.forEach(dps -> {
            // 处理每一个 地磁 Y 数据
            MyLog.d(TAG, "地磁y数据:" + dps.getValue());
            x2.add(Float.parseFloat(dps.getValue()) * -1);
        });

//        x1.add(10f);
//        x1.add(20f);
//        x1.add(30f);
//        x1.add(40f);
//        x1.add(50f);
//
//        x2.add(50f);
//        x2.add(40f);
//        x2.add(30f);
//        x2.add(20f);
//        x2.add(10f);

        yAxisValues.add(x1);
        yAxisValues.add(x2);
        labels.add("地磁 X 轴");
        labels.add("地磁 Y 轴");
        colours.add(Color.parseColor("#123456"));
        colours.add(Color.parseColor("#987654"));

        mView.showBarChart(xAxisValues, yAxisValues, labels, labelList, colours);
    }

    /**
     * 显示线性图
     */
    public void showLineChartMore() {
        if (!isRainGetDate || !isHumidityGetDate) {
            // 判断两个数据是否都获取，只要有一个没有获取到，就不加载图表
            MyLog.d(TAG, "line 数据未获取完");
            return;
        } else {
            MyLog.d(TAG, "line 数据获取完整");
            isHumidityGetDate = false;
            isRainGetDate = false;
            mView.showLoading(false);
        }
        MyLog.d(TAG, "开始处理 Line 图表");

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
            MyLog.d(TAG, rainTime);
            xLabels.add(rainTime);

            String rainValue = dps.getValue();
//            MyLog.d(TAG, "降雨量：" + rainValue);
            y2Value.add(Float.parseFloat(rainValue));
        });

        // 湿度
        List<HistoryBean.Dps> humidityList = mModel.getHumidityBean().getDps();
        List<Float> y1Value = new ArrayList<>();
        humidityList.forEach(dps -> {
            String humidityValue = dps.getValue();
//            MyLog.d(TAG, "湿度：" + humidityValue);
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
        MyLog.d(TAG, "开始处理 Pie 图表");

        //设置每份所占数量
        List<PieEntry> yVals = new ArrayList<>();
        yVals.add(new PieEntry(2.0f, "晴"));
        yVals.add(new PieEntry(6.0f, "阴"));
        yVals.add(new PieEntry(4.0f, "小雨"));
        yVals.add(new PieEntry(3.0f, "中雨"));
        yVals.add(new PieEntry(1.0f, "暴雨"));
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
        MyLog.d(TAG, "开始处理 Radar 图表");

        List<String> xData = new ArrayList<>();
        List<List<Float>> yDatas = new ArrayList<>();
        List<String> names = new ArrayList<>();

        names.add("设备 A 地区");
        names.add("设备 B 地区");
//        names.add("钾肥");
//        names.add("复合肥");
//        names.add("农家肥");
//        names.add("其他");

        xData.add("地形");
        xData.add("岩性");
        xData.add("地质构造");
        xData.add("外部活动");
        xData.add("水文");
        xData.add("降水");

        List<Integer> colors = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            List<Float> nData = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                nData.add((float) (Math.random() * 20));
            }
            yDatas.add(nData);
        }

//        colors.add(Color.parseColor("#fbd06a"));
        colors.add(Color.parseColor("#f69a40"));
//        colors.add(Color.parseColor("#ff5d52"));
        colors.add(Color.parseColor("#e71f19"));
//        colors.add(Color.parseColor("#ff9b43"));
//        colors.add(Color.parseColor("#8eb9fb"));

        mView.showRadarChart(xData, yDatas, names, colors);
    }

}
