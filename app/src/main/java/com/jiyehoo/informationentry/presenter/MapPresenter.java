package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.jiyehoo.informationentry.activity.DeviceListActivity;
import com.jiyehoo.informationentry.model.IMapModel;
import com.jiyehoo.informationentry.model.MapModel;
import com.jiyehoo.informationentry.view.IMapView;

/**
 * @author JiyeHoo
 * @description: 地图处理
 * @date :21-4-27 下午10:25
 */
public class MapPresenter {
    private final String TAG = "###MapPresenter";

    private final Context context;
    private final IMapView view;
    private final IMapModel model;

    private final AMap mAMap;

    public MapPresenter(IMapView view, AMap mAMap) {
        context = (Context) view;
        this.view = view;
        model = new MapModel();
        this.mAMap = mAMap;
    }

    /**
     * 初始化 map 视图
     */
    public void initMapView() {
        // 设置地图的放缩级别
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(14));

        // 卫星地图模式
        mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);

        // 初始化定位蓝点样式类
        MyLocationStyle mMyLocationStyle = new MyLocationStyle();

        // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mMyLocationStyle.interval(1000);

        // 设置默认定位按钮是否显示，非必需设置。
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);

        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mAMap.setMyLocationEnabled(true);
        // 定位一次，且将视角移动到地图中心点。
        mMyLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        mMyLocationStyle.showMyLocation(true);
        // 设置定位蓝点的Style
        mAMap.setMyLocationStyle(mMyLocationStyle);

        mAMap.setOnMyLocationChangeListener(location -> {
            // 从location对象中获取经纬度信息，地址描述信息，拿到位置之后调用逆地理编码接口获取
            Log.d(TAG, "经度:" + location.getLongitude() + "，纬度:" + location.getLatitude());
        });

        // 显示设备绘制点
        LatLng latLng = new LatLng(25.058895,110.303917);
        setMarker(latLng, "滑坡检测设备A", "状态正常(test)");
    }

    /**
     * 设置标记点
     */
    private void setMarker(@NonNull LatLng latLng, String title, String msg) {
        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(latLng).title(title).snippet(msg);
        Marker marker = mAMap.addMarker(markerOption);
        // 显示信息
        marker.showInfoWindow();
        // 返回 true 则表示接口已响应事件，否则返回false
        mAMap.setOnMarkerClickListener(markerClick -> {
            if (markerClick.getId().equals(marker.getId())) {
                Log.d(TAG, "标记点击：" + title);
                Intent intent = new Intent(context, DeviceListActivity.class);
                context.startActivity(intent);
                return true;
            }
            return false;
        });
    }


}
