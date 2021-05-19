package com.jiyehoo.informationentry.view;

import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JiyeHoo
 * @description:
 * @date :2021/5/11 上午11:35
 */
public interface IChartView {
    void showBarChart(List<Float> xAxisValues,
                      List<List<Float>> yAxisValues,
                      List<String> labels,
                      List<String> xLabels,
                      List<Integer> colours);

    void showLineChart(ArrayList<Float> xValues,
                       List<List<Float>> yValues,
                       List<String> labelNameList,
                       List<String> xLabels,
                       List<Integer> colorList);

    void showPieChart(List<PieEntry> yValue,
                      List<Integer> colors);

    void showRadarChart(List<String> xData,
                        List<List<Float>> yDatas,
                        List<String> names,
                        List<Integer> colors);

    void showToast(String msg);
}
