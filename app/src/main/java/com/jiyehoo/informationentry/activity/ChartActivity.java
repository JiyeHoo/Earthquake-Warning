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
import com.jiyehoo.informationentry.presenter.ChartPresenter;
import com.jiyehoo.informationentry.util.LineChartManager;
import com.jiyehoo.informationentry.util.PieChartManager;
import com.jiyehoo.informationentry.util.RadarChartManager;
import com.jiyehoo.informationentry.view.IChartView;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements IChartView {

    private final String TAG = "###ShowActivity";

    private RadarChart mRadarChart;
    private BarChart mBarChart;
    private LineChart mLineChart;
    private PieChart mPieChart;

    private ChartPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_show);

        bindView();
        mPresenter = new ChartPresenter(this);

        // 获取历史 DP 数据
//        mPresenter.getHistory();

        // 显示图表
//        mPresenter.showBarChartMore();
        mPresenter.getDataBar();
        mPresenter.getDataLine();
        mPresenter.showPieChart();
        mPresenter.showRadarChart();
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
        Toolbar mTbTitle = findViewById(R.id.tool_bar_show);
        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_show);

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("数据分析");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showBarChart(List<Float> xAxisValues, List<List<Float>> yAxisValues, List<String> labels, List<String> labelList, List<Integer> colours) {
        BarChartManager barChartManager = new BarChartManager(mBarChart);
        barChartManager.setXAxis(5, 0, 5);
        barChartManager.showMoreBarChart(xAxisValues, yAxisValues, labels, labelList, colours);
    }

    @Override
    public void showLineChart(ArrayList<Float> xValues, List<List<Float>> yValues, List<String> labelNameList, List<String> xLabels, List<Integer> colorList) {
        LineChartManager lineChartManager = new LineChartManager(mLineChart);
        lineChartManager.showLineChart(xValues, yValues, labelNameList, xLabels, colorList);
        lineChartManager.setDescription("");
    }

    @Override
    public void showPieChart(List<PieEntry> yValue, List<Integer> colors) {
        PieChartManager pieChartManager = new PieChartManager(mPieChart);
        pieChartManager.showSolidPieChart(yValue, colors);
    }

    @Override
    public void showRadarChart(List<String> xData, List<List<Float>> yDatas, List<String> names, List<Integer> colors) {
        RadarChartManager radarChartManager = new RadarChartManager(this, mRadarChart);
        radarChartManager.showRadarChart(xData, yDatas, names, colors);
    }
}