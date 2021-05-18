package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.adapter.DisasterAdapter;
import com.jiyehoo.informationentry.presenter.DisasterPresenter;
import com.jiyehoo.informationentry.view.IDisasterView;

/**
 * @author JiyeHoo
 * @description: 灾害发布界面
 */
public class DisasterActivity extends AppCompatActivity implements IDisasterView, View.OnClickListener {

    private final String TAG = "###DisasterActivity";
    private DisasterPresenter presenter;
    private RecyclerView mRvDisasterList;
    private TextView mTvNoDisasterTip;
    private SwipeRefreshLayout mSrlDisasterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_disaster);
        bindView();

        // 获取灾害数据
        presenter = new DisasterPresenter(this);
        presenter.getDisasterInfo();
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void bindView() {
        Toolbar mTbTitle = findViewById(R.id.tool_bar_4);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_4);
        mCollapsingToolbarLayout.setTitle("灾害发布");
        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRvDisasterList = findViewById(R.id.rv_disaster_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRvDisasterList.setLayoutManager(layoutManager);

        mTvNoDisasterTip = findViewById(R.id.tv_no_disaster_tip);
        mSrlDisasterList = findViewById(R.id.srl_disaster_list);

        mSrlDisasterList.setOnRefreshListener(refreshListener);

        findViewById(R.id.fab_refresh_disaster_list).setOnClickListener(this);
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
    public void showRv(DisasterAdapter adapter) {
        runOnUiThread(() ->
                mRvDisasterList.setAdapter(adapter));
    }

    /**
     * 没有预警信息时候提示
     * @param isShow 是否显示
     */
    @Override
    public void showNoDisasterTip(boolean isShow) {
        runOnUiThread(() -> {
            if (isShow) {
                mTvNoDisasterTip.setVisibility(View.VISIBLE);
            } else {
                mTvNoDisasterTip.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 显示下拉刷新
     * @param isShow 是否显示
     */
    @Override
    public void showLoading(boolean isShow) {
        runOnUiThread(() -> mSrlDisasterList.setRefreshing(isShow));
    }

    /**
     * 下拉刷新监听
     */
    private final SwipeRefreshLayout.OnRefreshListener refreshListener = () -> {
        // 下拉刷新
        rvRemoveAll();
        presenter.getDisasterInfo();
    };

    /**
     * 清空 Rv 所有
     */
    private void rvRemoveAll() {
        mRvDisasterList.removeAllViews();
    }

    @Override
    public void onClick(View v) {
        // 刷新列表
        if (v.getId() == R.id.fab_refresh_disaster_list) {
            rvRemoveAll();
            presenter.getDisasterInfo();
        }
    }

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg) && msg.length() > 0) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}