package com.jiyehoo.informationentry.util;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiyeHoo
 * @date 21-1-28 下午4:18
 */
public class RadarChartManager {

    private RadarChart mRadarChart;
    private Context mContext;
    private YAxis yAxis;
    private XAxis xAxis;

    public RadarChartManager(Context context, RadarChart radarChart) {
        this.mContext = context;
        this.mRadarChart = radarChart;
        yAxis = mRadarChart.getYAxis();
        xAxis = mRadarChart.getXAxis();
    }

    private void initRadarChart() {
        // 绘制线条宽度，圆形向外辐射的线条
        mRadarChart.setWebLineWidth(1f);
//        线条的颜色
        mRadarChart.setWebColor(Color.parseColor("#d5d5d5"));
        mRadarChart.setWebColorInner(Color.parseColor("#d5d5d5"));
        // 内部线条宽度，外面的环状线条
        mRadarChart.setWebLineWidthInner(0.4f);
        // 所有线条WebLine透明度
        mRadarChart.setWebAlpha(100);
        //取消图标右下角的描述
        mRadarChart.getDescription().setEnabled(false);
//        图显示的位置
        mRadarChart.setExtraOffsets(0, 2, 110, 0);
        //点击点弹出的标签
//        RadarMarkerView mv = new RadarMarkerView(mContext, R.layout.radar_marker_view);
//        mRadarChart.setMarkerView(mv);
        XAxis xAxis = mRadarChart.getXAxis();
        // X坐标值字体大小
        xAxis.setTextSize(8f);
        xAxis.setGridColor(Color.parseColor("#a1a1a1"));
        xAxis.setTextColor(Color.parseColor("#a1a1a1"));
        xAxis.setAxisLineColor(Color.parseColor("#a1a1a1"));
        // Y坐标值字体样式

        // Y坐标值标签个数
        yAxis.setLabelCount(6, false);
        // Y坐标值字体大小
        yAxis.setTextSize(15f);
        yAxis.setTextColor(Color.parseColor("#a1a1a1"));
        yAxis.setAxisLineColor(Color.parseColor("#a1a1a1"));
        // Y坐标值是否从0开始
        yAxis.setStartAtZero(true);
        yAxis.setEnabled(false);
        yAxis.setDrawLabels(false);
        yAxis.setDrawTopYLabelEntry(false);
        yAxis.setGridColor(Color.parseColor("#a1a1a1"));
        xAxis.setDrawLimitLinesBehindData(false);
        //设置X上的显示labal
        xAxis.setDrawLabels(true);
        Legend l = mRadarChart.getLegend();
        // 图例位置
//        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        // 图例X间距
        l.setXEntrySpace(1f);
        // 图例Y间距
        l.setYEntrySpace(1f);
//        图例文字的大小
        l.setTextSize(12f);
//        图例文字颜色
        l.setTextColor(Color.parseColor("#a1a1a1"));
//        图例相对于Y的偏移
        l.setYOffset(1f);
//        图例相对于X的偏移
        l.setXOffset(20f);
    }

    public void showRadarChart(final List<String> xData, List<List<Float>> yDatas, List<String> names, List<Integer> colors) {
        initRadarChart();
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return xData.get((int) value % xData.size());
            }
        });
        //Y轴字体颜色
        yAxis.setTextColor(Color.parseColor("#a1a1a1"));
//        是否绘制
        yAxis.setDrawLabels(false);
        List<IRadarDataSet> sets = new ArrayList<>();
        for (int i = 0; i < yDatas.size(); i++) {
            ArrayList<RadarEntry> yValues = new ArrayList<>();
            for (int j = 0; j < yDatas.get(i).size(); j++) {
                yValues.add(new RadarEntry(yDatas.get(i).get(j), j));
            }
            RadarDataSet radarDataSet = new RadarDataSet(yValues, names.get(i));
            radarDataSet.setColor(colors.get(i));
            //设置是否填充区域
            radarDataSet.setDrawFilled(true);
            //填充区域的颜色
            radarDataSet.setFillColor(colors.get(i));
            //填充区域同颜色的alpha值
            radarDataSet.setFillAlpha(255);
            // 数据线条宽度
            radarDataSet.setLineWidth(1f);
            sets.add(radarDataSet);
        }

        RadarData data = new RadarData(sets);
        // 数据字体大小
        data.setValueTextSize(8f);
        // 是否绘制Y值到图表
        data.setDrawValues(false);
        mRadarChart.setData(data);
        mRadarChart.invalidate();
    }

    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        mRadarChart.setDescription(description);
        mRadarChart.invalidate();
    }

    public void setYscale(float max, float min, int labelCount) {
        yAxis.setLabelCount(labelCount, false);
        yAxis.setAxisMinimum(min);
        yAxis.setAxisMaximum(max);
        mRadarChart.invalidate();
    }
}
