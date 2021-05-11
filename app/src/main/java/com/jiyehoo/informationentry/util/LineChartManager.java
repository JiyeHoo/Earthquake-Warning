package com.jiyehoo.informationentry.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiyeHoo
 * @date 21-1-28 下午3:25
 */
public class LineChartManager {

    private LineChart lineChart;
    private YAxis leftAxis;   //左边Y轴
    private YAxis rightAxis;  //右边Y轴
    private XAxis xAxis;      //X轴

    public LineChartManager(LineChart mLineChart) {
        this.lineChart = mLineChart;
        leftAxis = lineChart.getAxisLeft();
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
    }

    private void initLineChart(boolean legendShow) {
        lineChart.setDrawGridBackground(false);
        //显示边界
        lineChart.setDrawBorders(false);

        //设置动画效果
        lineChart.animateX(1000);

        lineChart.setTouchEnabled(true); // 所有触摸事件,默认true
        lineChart.setDragEnabled(true);    // 可拖动,默认true
        lineChart.setScaleEnabled(true);   // 两个轴上的缩放,X,Y分别默认为true
        lineChart.setScaleXEnabled(true);  // X轴上的缩放,默认true
        lineChart.setScaleYEnabled(true);  // Y轴上的缩放,默认true
        lineChart.setPinchZoom(false);  // X,Y轴同时缩放，false则X,Y轴单独缩放,默认false
        lineChart.setDoubleTapToZoomEnabled(false); // 双击缩放,默认true
        lineChart.setDragDecelerationEnabled(true);    // 抬起手指，继续滑动,默认true

        //折线图例 标签 设置
        Legend legend = lineChart.getLegend();
        if (legendShow) {
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

        } else {
            legend.setForm(Legend.LegendForm.NONE);
        }

        //XY轴的设置
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        // 不绘制网格线

        xAxis.setDrawGridLines(true);
        xAxis.setGridColor(Color.parseColor("#d8d8d8"));
        //设置最后和第一个标签不超出x轴
        xAxis.setAvoidFirstLastClipping(true);
//        设置线的宽度
        xAxis.setAxisLineWidth(1.0f);
        xAxis.setAxisLineColor(Color.parseColor("#d5d5d5"));

        //保证Y轴从0开始，不然会上移一点
        leftAxis.setAxisMinimum(0f);
        // 显示数字但不显示线
        leftAxis.setDrawAxisLine(true);
        leftAxis.setTextColor(Color.parseColor("#d5d5d5"));

        leftAxis.setDrawGridLines(true);
//        设置网格为虚线
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setGridColor(Color.parseColor("#d8d8d8"));
        leftAxis.setAxisLineColor(Color.parseColor("#d5d5d5"));
        rightAxis.setAxisMinimum(0f);
        // 线跟数据都不显示
        rightAxis.setEnabled(false); //右侧Y轴不显示
    }


    /**
     * 展示线性图(多条)
     *
     * @param xAxisValues
     * @param yAxisValues 多条曲线Y轴数据集合的集合
     * @param labels
     * @param colours
     */
    public void showLineChart(List<Float> xAxisValues,
                              List<List<Float>> yAxisValues,
                              List<String> labels,
                              List<String> xLabels,
                              List<Integer> colours) {
        initLineChart(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < yAxisValues.size(); i++) {
            ArrayList<Entry> entries = new ArrayList<>();
            for (int j = 0; j < yAxisValues.get(i).size(); j++) {
                if (j >= xAxisValues.size()) {
                    j = xAxisValues.size() - 1;
                }
                entries.add(new Entry(xAxisValues.get(j), yAxisValues.get(i).get(j)));
            }
            LineDataSet lineDataSet = new LineDataSet(entries, labels.get(i));

            initLineDataSet(lineDataSet, colours.get(i), true);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            dataSets.add(lineDataSet);
        }
        LineData data = new LineData(dataSets);
        xAxis.setLabelCount(xAxisValues.size(), true);
//        String[] xValues = {"6:00", "9:00", "12:00", "15:00", "18:00"};
        xAxis.setValueFormatter(new XAxisValueFormatter(xLabels));

        lineChart.setData(data);
    }

    public static class XAxisValueFormatter implements IAxisValueFormatter {

        private final List<String> xValues;

        public XAxisValueFormatter(List<String> xValues) {
            this.xValues = xValues;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return xValues.get((int) value);
        }

    }


    /**
     * 初始化曲线 每一个LineDataSet代表一条线
     *
     * @param lineDataSet
     * @param color
     * @param mode        折线图是否填充
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color, boolean mode) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setValueTextSize(9f);

        // 显示具体值
        lineDataSet.setDrawValues(true);

//        lineDataSet.setHighlightEnabled(false);
        //设置折线图填充
        lineDataSet.setDrawFilled(mode);
        //设置填充颜色
        lineDataSet.setFillColor(color);
//        lineDataSet.setFormLineWidth(2f);
//        lineDataSet.setFormSize(15.f);
        //线模式为圆滑曲线（默认折线）
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
    }

    /**
     * 设置描述信息
     * @param str
     */
    public void setDescription(String str) {
        Description description = new Description();
        description.setText(str);
        lineChart.setDescription(description);
        lineChart.invalidate();
    }

}
