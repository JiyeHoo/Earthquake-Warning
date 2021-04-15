package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.adapter.DeviceListAdapter;
import com.jiyehoo.informationentry.adapter.OnDeviceItemClickListener;
import com.jiyehoo.informationentry.presenter.DeviceListPresenter;
import com.jiyehoo.informationentry.view.IDeviceListView;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;

public class DeviceListActivity extends AppCompatActivity implements IDeviceListView {

    private final String TAG = "DeviceListActivity";

    private DeviceListPresenter presenter;
    private RecyclerView mRvDeviceList;
    private DeviceListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        fullScreen();
        bindView();
        initDeviceList();
    }

    private void initDeviceList() {
        presenter = new DeviceListPresenter(this);
        presenter.getDeviceList();
    }

    private void bindView() {
        Toolbar mTbTitle = findViewById(R.id.tb_toolbar_bt_ctl);
        setSupportActionBar(mTbTitle);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_collapsing_toolbar_layout);
        ImageView mImageView = findViewById(R.id.iv_pic_show_by_ctl);
        Glide.with(this).load(R.drawable.item_3).into(mImageView);
        mCollapsingToolbarLayout.setTitle("设备列表");
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mRvDeviceList = findViewById(R.id.rv_device_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRvDeviceList.setLayoutManager(layoutManager);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg) && msg.length() > 0) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showRv(List<DeviceBean> deviceBeanList) {
        adapter = new DeviceListAdapter(deviceBeanList);
        adapter.setOnDeviceItemClickListener(new OnDeviceItemClickListener() {
            @Override
            public void onItemClick(DeviceBean deviceBean) {
                Log.d(TAG, "点击");
            }

            @Override
            public void onItemLongClick(DeviceBean deviceBean) {
                Log.d(TAG, "长按");
            }
        });
        mRvDeviceList.setAdapter(adapter);
    }
}