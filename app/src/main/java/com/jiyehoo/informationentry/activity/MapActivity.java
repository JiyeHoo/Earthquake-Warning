package com.jiyehoo.informationentry.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.MapPresenter;
import com.jiyehoo.informationentry.view.IMapView;

import org.jetbrains.annotations.NotNull;

public class MapActivity extends AppCompatActivity implements IMapView {

    private final String TAG = "###MapActivity";

    private MapView mMapView = null;
    private AMap mAMap = null;
    private MapPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_map);

        // 申请权限
        checkPermission();
        // 初始化视图
        initView(savedInstanceState);
        // 初始化地图视图
        presenter.initMapView();

    }


    /**
     * 权限请求
     */
    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
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