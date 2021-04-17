package com.jiyehoo.informationentry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.CtrlPresenter;
import com.jiyehoo.informationentry.util.LoadingDialogUtil;
import com.jiyehoo.informationentry.view.ICtrlView;

public class DeviceCtrlActivity extends AppCompatActivity implements ICtrlView {

    private CtrlPresenter presenter;
    private LinearLayout mLlDpRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_device_ctrl);

        initView();
        initData();
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
        Glide.with(this).load(R.drawable.item_3).into(mImageView);
        mCollapsingToolbarLayout.setTitle("设备详情");
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mLlDpRoot = findViewById(R.id.ll_device_dp);
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
}