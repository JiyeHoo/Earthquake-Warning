package com.jiyehoo.informationentry.activity;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.MapPresenter;
import com.jiyehoo.informationentry.view.IMapView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MapActivity extends AppCompatActivity implements IMapView, EasyPermissions.PermissionCallbacks {

    private final String TAG = "###MapActivity";

    private MapView mMapView = null;
    private AMap mAMap = null;
    private MapPresenter presenter;

    // 权限
    String PERMISSION_STORAGE_MSG = "请授予权限，否则影响部分使用功能";
    int PERMISSION_STORAGE_CODE = 10001;
    String[] PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_map);
        // 初始化视图
        initView(savedInstanceState);
        // 申请权限
        checkPermission();


    }

    /**
     * 权限请求
     */
    private void checkPermission() {

        if (EasyPermissions.hasPermissions(this, PERMS)) {
            // 已经申请过权限，做想做的事
            Log.d(TAG, "定位权限拥有");
            // 初始化地图视图
            presenter.initMapView();
        } else {
            // 没有申请过权限，现在去申请
            /*
             @param host Context对象
             @param rationale  权限弹窗上的提示语。
             @param requestCode 请求权限的唯一标识码
             @param perms 一系列权限
             */
            Log.d(TAG, "没有权限，开始申请");
            EasyPermissions.requestPermissions(this, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMS);
        }
    }

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
        Log.d(TAG, "权限申请成功");
        // 初始化地图视图
        presenter.initMapView();
    }

    /**
     * 申请拒绝时调用
     * @param requestCode 请求权限的唯一标识码
     * @param perms 一系列权限
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NotNull List<String> perms) {
        Log.d(TAG, "权限申请失败");
        Toast.makeText(this, "没有定位权限", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
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

    private void initView(Bundle savedInstanceState) {
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }
        // 初始化地图视图
        presenter = new MapPresenter(this, mAMap);
    }


}