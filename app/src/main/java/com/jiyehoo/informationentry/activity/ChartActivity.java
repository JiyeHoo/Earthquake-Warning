package com.jiyehoo.informationentry.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

/**
 * @author JiyeHoo
 * @description: 图表界面
 */
public class ChartActivity extends AppCompatActivity implements IChartView, View.OnClickListener {

    private final String TAG = "###ShowActivity";
    private RadarChart mRadarChart;
    private BarChart mBarChart;
    private LineChart mLineChart;
    private PieChart mPieChart;

    private ChartPresenter mPresenter;
    private SwipeRefreshLayout mSrlChart;

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
        mPresenter.setDevId();
//        mPresenter.getDataBar();
//        mPresenter.getDataLine();
//        mPresenter.showPieChart();
//        mPresenter.showRadarChart();
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

        findViewById(R.id.fab_write_share_data).setOnClickListener(this);
//        findViewById(R.id.fab_delete_share_data).setOnClickListener(this);
        findViewById(R.id.fab_read_share_data).setOnClickListener(this);
        findViewById(R.id.fab_share_file).setOnClickListener(this);

        mSrlChart = findViewById(R.id.srl_chart_list);
        mSrlChart.setOnRefreshListener(refreshListener);
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

    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(this, msg, Toast.LENGTH_LONG).show());
    }

    @Override
    public void onClick(View v) {
        // 导出数据到文件
        if (v.getId() == R.id.fab_write_share_data) {
            mPresenter.shareDataFile();
        }
        // 删除保存的文件
//        if (v.getId() == R.id.fab_delete_share_data) {
//            mPresenter.deleteDataFile();
//        }
        // 读取保存的文件
        if (v.getId() == R.id.fab_read_share_data) {
            mPresenter.readDataFile();
        }

        // 分享、打开文件
        if (v.getId() == R.id.fab_share_file) {
            mPresenter.openDataFile();
        }
    }

    /**
     * 显示下拉刷新
     * @param isShow 是否显示
     */
    @Override
    public void showLoading(boolean isShow) {
        runOnUiThread(() -> mSrlChart.setRefreshing(isShow));
    }

    @Override
    public void showDialog(String title, String msg, DialogInterface.OnClickListener listener) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("OK", listener)
                    .show();
        });

    }

    @Override
    public void finishActivity() {
        finish();
    }

    /**
     * 下拉刷新监听
     */
    private final SwipeRefreshLayout.OnRefreshListener refreshListener = () -> {
        // 下拉刷新
        showLoading(true);
        mPresenter.getDataBar();
        mPresenter.getDataLine();
        mPresenter.showPieChart();
        mPresenter.showRadarChart();
    };
}