package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.fragment.BarChartManager;
import com.jiyehoo.informationentry.util.LineChartManager;
import com.jiyehoo.informationentry.util.PieChartManager;
import com.jiyehoo.informationentry.util.RadarChartManager;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private RadarChart mRadarChart;
    private BarChart mBarChart;
    private LineChart mLineChart;
    private PieChart mPieChart;
    private Toolbar mTbTitle;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_show);

        bindView();


        showBarChartMore();
        showLineChartMore();
        showChartFull();
        showRadarChart();
    }

    private void showBarChartMore() {
        BarChartManager barChartManager = new BarChartManager(mBarChart);

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
        barChartManager.showMoreBarChart(xAxisValues, yAxisValues, labels, colours);
        barChartManager.setXAxis(5, 0, 5);
    }

    private void showChartFull() {
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

        PieChartManager pieChartManager = new PieChartManager(mPieChart);
        pieChartManager.showSolidPieChart(yVals, colors);
    }

    private void showLineChartMore() {
        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            xValues.add((float) i);
        }

        //设置y轴的数据()
        List<List<Float>> yValues = new ArrayList<>();

        List<Float> y1Value = new ArrayList<>();
        List<Float> y2Value = new ArrayList<>();

        List<String> lableNameList = new ArrayList<>();
        lableNameList.add("迎风面地表温度");
        lableNameList.add("背风面地表温度");

        for (int j = 0; j <= 4; j++) {
            float value = (float) (15 + Math.random() * 20);
            y1Value.add(value);
            y2Value.add(value-5);
        }
        yValues.add(y1Value);
        yValues.add(y2Value);

        List<Integer> colorList = new ArrayList<>();
        colorList.add(Color.parseColor("#6785f2"));
        colorList.add(Color.parseColor("#eecc44"));

        LineChartManager lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.showLineChart(xValues, yValues, lableNameList, colorList);
        lineChartManager.setDescription("");

    }

    private void showRadarChart() {
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

        RadarChartManager radarChartManager = new RadarChartManager(this, mRadarChart);
        radarChartManager.showRadarChart(xData, yDatas, names, colors);
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void bindView() {
        mRadarChart = findViewById(R.id.radar_chart);
        mBarChart = findViewById(R.id.bar_chart);
        mLineChart = findViewById(R.id.line_chart);
        mPieChart = findViewById(R.id.pie_chart);
        mTbTitle = findViewById(R.id.tool_bar_show);
        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        mCollapsingToolbarLayout = findViewById(R.id.ctl_show);

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("图表展示");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}