package com.jiyehoo.informationentry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.jiyehoo.informationentry.R;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView = null;
    private AMap mAMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_map);

        //申请权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }

        mMapView = findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
        }

        //设置地图的放缩级别
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        // 卫星地图模式
        mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);

        //初始化定位蓝点样式类
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        //连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        //（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        //定位蓝点
        MyLocationStyle mMyLocationStyle = new MyLocationStyle();

        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mMyLocationStyle.interval(5000);
        //设置定位蓝点的Style
        mAMap.setMyLocationStyle(mMyLocationStyle);

        //设置默认定位按钮是否显示，非必需设置。
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setMyLocationEnabled(true);

        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。

        mMyLocationStyle.showMyLocation(true);
        mAMap.setOnMyLocationChangeListener(location -> {
            //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
        if (Build.VERSION.SDK_INT >= 24) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            Toast.makeText(this, "版本过低，无法渲染状态栏", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}