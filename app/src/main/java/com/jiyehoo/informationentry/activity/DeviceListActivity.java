package com.jiyehoo.informationentry.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.adapter.DeviceListAdapter;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.presenter.DeviceListPresenter;
import com.jiyehoo.informationentry.util.LoadingDialogUtil;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.IDeviceListView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
/**
 * @author JiyeHoo
 * @description: 设备列表界面
 */
public class DeviceListActivity extends AppCompatActivity implements IDeviceListView, View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private final String TAG = "###DeviceListActivity";
    private static final int REQUEST_CODE = 0;
    private static final int ACTIVITY_RESULT = 1;

    private DeviceListPresenter presenter;
    private RecyclerView mRvDeviceList;
//    private LoadingDialogUtil loadingDialogUtil;
    private TextView mTvNoDeviceTip;
    private SwipeRefreshLayout mSwipeLayout;

    // 权限
    String PERMISSION_STORAGE_MSG = "请授予权限，否则影响部分使用功能";
    int PERMISSION_STORAGE_CODE = 10001;
    String[] PERMS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_device_list);
        initView();
        initDeviceList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        rvRemoveAll();
        presenter.getDeviceList();
    }

    private void initDeviceList() {
        presenter = new DeviceListPresenter(this);
        presenter.getDeviceList();
    }

    private void initView() {
        Toolbar mTbTitle = findViewById(R.id.tb_toolbar_device_list);
        setSupportActionBar(mTbTitle);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_collapsing_toolbar_layout);
        ImageView mImageView = findViewById(R.id.iv_pic_show_device_list);
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
//        loadingDialog = new LoadingDialog.Builder(this).loadText("加载中...").build();
//        loadingDialogUtil = new LoadingDialogUtil(this);

        mTvNoDeviceTip = findViewById(R.id.tv_no_device_tip);
        mSwipeLayout = findViewById(R.id.srl_device_list);
        mSwipeLayout.setColorSchemeResources(R.color.tab_color);
        mSwipeLayout.setOnRefreshListener(refreshListener);
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
            // 点击悬浮按钮，添加设备，目前实现扫码
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
                MyLog.d(TAG, "内容:" + obj.originalValue);
                // todo 在这里开始获取经纬度，用于配网
//                double lon = HomeModel.getLon(context);
//                double lat = HomeModel.getLat(context);
//                MyLog.d(TAG, "SP获取GPS：" + lon + "," + lat);
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
                    // 删除设备
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

    @Override
    public void showNoDeviceTip(boolean haveShow) {
        if (haveShow) {
            mTvNoDeviceTip.setVisibility(View.VISIBLE);
        } else {
            mTvNoDeviceTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void showSwipeRefresh(boolean havShow) {
        mSwipeLayout.setRefreshing(havShow);
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = () -> {
        // 下拉刷新
        rvRemoveAll();
        presenter.getDeviceList();
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果转发给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 申请成功时调用
     * @param requestCode 请求权限的唯一标识码
     * @param perms 一系列权限
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NotNull List<String> perms) {
        MyLog.d(TAG, "权限申请成功");
        // 开始扫码
        presenter.startQR();
    }

    /**
     * 申请拒绝时调用
     * @param requestCode 请求权限的唯一标识码
     * @param perms 一系列权限
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NotNull List<String> perms) {
        MyLog.d(TAG, "权限申请失败");
        showToast("缺少权限");
    }

}