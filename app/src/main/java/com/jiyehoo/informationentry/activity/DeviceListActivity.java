package com.jiyehoo.informationentry.activity;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.adapter.DeviceListAdapter;
import com.jiyehoo.informationentry.presenter.DeviceListPresenter;
import com.jiyehoo.informationentry.view.IDeviceListView;
import com.tuya.smart.sdk.bean.DeviceBean;

public class DeviceListActivity extends AppCompatActivity implements IDeviceListView, View.OnClickListener {

    private final String TAG = "###DeviceListActivity";
    private static final int REQUEST_CODE = 0;
    private static final int ACTIVITY_RESULT = 1;

    private DeviceListPresenter presenter;
    private RecyclerView mRvDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        fullScreen();
        initView();
        initDeviceList();
    }

    private void initDeviceList() {
        presenter = new DeviceListPresenter(this);
        presenter.getDeviceList();
    }

    private void initView() {
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

        FloatingActionButton mFabAddDevice = findViewById(R.id.fab_add_device);
        mFabAddDevice.setOnClickListener(this);
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
    public void showRv(DeviceListAdapter adapter) {
        mRvDeviceList.setAdapter(adapter);
    }

    /**
     * 点击悬浮按钮，添加设备，目前实现扫码
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add_device) {
            // todo 点击悬浮按钮，添加设备，目前实现扫码
            presenter.startQR();
        }
    }

    /**
     * 扫码回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == ACTIVITY_RESULT) {
            HmsScan obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj != null) {
                // 展示解码结果
                Log.d(TAG, "内容:" + obj.originalValue);

                presenter.qrGetUuid(obj.originalValue);

            }
        }
    }

    @Override
    public void showDialog(String title, String msg, String devId) {
        if (TextUtils.isEmpty(title)) {
            title = "提示";
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "msg is null";
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", (dialog, which) -> {
                    // todo 删除设备
                    presenter.removeDevice(devId);
                })
                .setNegativeButton("取消", null)
                .show();
        alertDialog.show();
    }

    @Override
    public void rvRemoveAll() {

            mRvDeviceList.removeAllViews();

    }


}