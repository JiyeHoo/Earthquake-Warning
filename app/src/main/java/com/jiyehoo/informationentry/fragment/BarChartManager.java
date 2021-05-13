package com.jiyehoo.informationentry.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JiyeHoo
 * @date 21-1-28 下午3:44
 */
public class BarChartManager {

    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private DecimalFormat mFormat;

    public BarChartManager(BarChart barChart) {
        mBarChart = barChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
    }

    private void initLineChart() {
        mFormat = new DecimalFormat("#,###.##");
        // 背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        // 是否显示网格背景
        mBarChart.setDrawGridBackground(false);
        // 显示每条背景阴影
        mBarChart.setDrawBarShadow(false);
        // 设置图标边框的颜色
        mBarChart.setBorderColor(Color.parseColor("#ff0000"));
//        mBarChart.setHighlightFullBarEnabled(false);
        mBarChart.setTouchEnabled(true); // 所有触摸事件,默认true
        mBarChart.setDragEnabled(true);    // 可拖动,默认true
        mBarChart.setScaleEnabled(false);   // 两个轴上的缩放,X,Y分别默认为true
        mBarChart.setScaleXEnabled(false);  // X轴上的缩放,默认true
        mBarChart.setScaleYEnabled(false);  // Y轴上的缩放,默认true
        mBarChart.setPinchZoom(false);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        mBarChart.setDoubleTapToZoomEnabled(false); // 双击缩放,默认true
        mBarChart.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true

        // 显示边界
        mBarChart.setDrawBorders(false);
        // 设置XY动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);



//      不显示描述信息
        mBarChart.getDescription().setEnabled(false);
//         图例设置
        Legend legend = mBarChart.getLegend();

        legend.setDrawInside(false);
        legend.setFormSize(8);
        legend.setXEntrySpace(7f);
        legend.setYEntrySpace(0f);
        legend.setYOffset(0f);
        // legend.setForm(Legend.LegendForm.SQUARE);
        // 文字的大小
        legend.setTextSize(12);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);







        // XY轴的设置
        // X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//      X轴最小间距
        xAxis.setGranularity(1f);
//      不绘制网格线
        xAxis.setDrawGridLines(false);
//      X轴字体样式
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
//      设置X轴文字居中对齐
        xAxis.setCenterAxisLabels(true);
        // 角度
//        xAxis.setLabelRotationAngle(30);
//
//        xAxis.setDrawLabels(false);
//
//       保证Y轴从0开始，不然会上移一点
        leftAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setTextColor(Color.parseColor("#d5d5d5"));

        rightAxis.setEnabled(false); //右侧Y轴不显示
    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private String[] xValues;

        public MyYAxisValueFormatter(String[] yValues) {
            xValues = yValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
//            Log.e("TAG", "xValues[(int) value]====="+xValues[(int) value]);
            return mFormat.format(value) + "%";
        }

    }


    public class XAxisValueFormatter implements IAxisValueFormatter {

        private String[] xValues;

        public XAxisValueFormatter(String[] xValues) {
            this.xValues = xValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Log.e("TAG", "============"+value);
            return xValues[(int) value];
        }
    }

    public void showMoreBarChart(final List<Float> xAxisValues, List<List<Float>> yAxisValues, List<String> labels, List<String> labelList, List<Integer> colours) {
        initLineChart();
        BarData data = new BarData();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {

                entries.add(new BarEntry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, labels.get(i));

            barDataSet.setColor(colours.get(i));
            barDataSet.setValueTextColor(colours.get(i));
            barDataSet.setValueTextSize(7f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            // 是否显示数值
            barDataSet.setDrawValues(true);
            data.addDataSet(barDataSet);
        }
        int amount = yAxisValues.size();

        float groupSpace = 0.3f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.3) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.08 = 1.00 -> interval per "group"
        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        data.setBarWidth(barWidth);



        final String[] xValues = {"南方向", "东方向", "山顶", "西方向", "北方向"};
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                for (int i=0;i<xAxisValues.size();i++){
                    if(value==(xAxisValues.get(i)-1)) {
                        return labelList.get(i);
                    }
                }
                return "";
            }
        });
        data.groupBars(0, groupSpace, barSpace);
        mBarChart.setData(data);
    }

    /**
     * 设置Y轴值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setYAxis(float max, float min, int labelCount) {
        if (max < min) {
            return;
        }
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
        leftAxis.setLabelCount(labelCount, false);

        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
        rightAxis.setLabelCount(labelCount, false);
        mBarChart.invalidate();
    }

    /**
     * 设置X轴的值
     *
     * @param max
     * @param min
     * @param labelCount
     */
    public void setXAxis(float max, float min, int labelCount) {
        xAxis.setAxisMaximum(max);
        xAxis.setAxisMinimum(min);
        xAxis.setLabelCount(labelCount, false);

        mBarChart.invalidate();
    }

    /**
     * 设置高限制线
     *
     * @param high
     * @param name
     */
    public void setHighLimitLine(float high, String name, int color) {
        if (name == null) {
            name = "高限制线";
        }
        LimitLine hightLimit = new LimitLine(high, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(color);
        hightLimit.setTextColor(color);
        leftAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置低限制线
     *
     * @param low
     * @param name
     */
    public void setLowLimitLine(int low, String name) {
        if (name == null) {
            name = "低限制线";
        }
        LimitLine hightLimit = new LimitLine(low, name);
        hightLimit.setLineWidth(4f);
        hightLimit.setTextSize(10f);
        leftAxis.addLimitLine(hightLimit);
        mBarChart.invalidate();
    }

    /**
     * 设置描述信息
     *
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }

}
