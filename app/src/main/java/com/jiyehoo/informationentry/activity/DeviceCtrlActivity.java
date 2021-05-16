package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.CtrlPresenter;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.ICtrlView;

public class DeviceCtrlActivity extends AppCompatActivity implements ICtrlView, View.OnClickListener {

    private final String TAG = "###DeviceCtrlActivity";

    private CtrlPresenter presenter;
    private LinearLayout mLlDpRoot;
    private SwipeRefreshLayout mSwipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_device_ctrl);

        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unregisterListener();
        presenter.destroyDevice();
    }

    private void initData() {
        String devId = getIntent().getStringExtra("devId");
        presenter = new CtrlPresenter(this, devId);
        presenter.getSchemaMap();
        presenter.showDp();
    }

    private void initView() {
        Toolbar mTbTitle = findViewById(R.id.tb_toolbar_bt_ctl);
        setSupportActionBar(mTbTitle);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_collapsing_toolbar_layout);
        ImageView mImageView = findViewById(R.id.iv_pic_show_by_ctl);
        Glide.with(this).load(R.drawable.item_2).into(mImageView);
        mCollapsingToolbarLayout.setTitle("设备详情");
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        FloatingActionButton mFabReset = findViewById(R.id.fab_reset_device);
        mFabReset.setOnClickListener(this);
        FloatingActionButton mFabRename = findViewById(R.id.fab_rename);
        mFabRename.setOnClickListener(this);

        mLlDpRoot = findViewById(R.id.ll_device_dp);

        mSwipeLayout = findViewById(R.id.srl_ctrl);
        mSwipeLayout.setColorSchemeResources(R.color.tab_color);
        mSwipeLayout.setOnRefreshListener(refreshListener);
        // 暂时禁用手动
        mSwipeLayout.setEnabled(false);
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
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
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg) && msg.length() > 0) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addView(View view) {
        mLlDpRoot.addView(view);
    }



    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void onClick(View v) {
        // 设备恢复出厂
        if (v.getId() == R.id.fab_reset_device) {
            MyLog.d(TAG, "fab 点击");
            presenter.resetFactory();
        }
        // 重命名
        if (v.getId() == R.id.fab_rename) {
            MyLog.d(TAG, "fab 点击");
            presenter.rename();
        }
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = () -> {
        // todo 下拉刷新
    };

    @Override
    public void showSwipeRefresh(boolean havShow) {
        mSwipeLayout.setRefreshing(havShow);
    }

    /**
     * 清空列表
     */
    @Override
    public void clearList() {
        MyLog.d(TAG, "清空 views");
        mLlDpRoot.removeAllViews();
    }
}