package com.jiyehoo.informationentry.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.jiyehoo.informationentry.activity.DeviceListActivity;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.model.IMapModel;
import com.jiyehoo.informationentry.model.MapModel;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.IMapView;
import com.tuya.smart.android.device.bean.SchemaBean;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;
import com.tuya.smart.sdk.bean.DeviceBean;

import java.util.List;
import java.util.Map;

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
            MyLog.d(TAG, "经度:" + location.getLongitude() + "，纬度:" + location.getLatitude());
        });

        // 设置标记点 气泡
        // todo 请求设备列表，获取经纬度，绘制
        getDeviceList();
    }

    /**
     * 获取设备列表
     */
    private void getDeviceList() {
        long homeId = HomeModel.getHomeId(context);
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                List<DeviceBean> deviceBeanList = bean.getDeviceList();
                if (deviceBeanList.isEmpty()) {
                    MyLog.d(TAG, "设备列表为空,return");
                    return;
                }
                deviceBeanList.forEach(deviceBean -> setInfoAndMarker(deviceBean));
            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                MyLog.d(TAG, "设备列表获取失败" + errorMsg);
            }
        });
    }

    /**
     * 根据传入的DeviceBean，获取这个设备的信息，绘制这个标记点
     */
    private void setInfoAndMarker(DeviceBean deviceBean) {
        MyLog.d(TAG, "设备信息:" + deviceBean.getName());

        // 获取经度
        String lonStr = (String) deviceBean.getDps().get("110");
        if (TextUtils.isEmpty(lonStr)) {
            lonStr = "0";
        }
        assert lonStr != null;
        double lon = Double.parseDouble(lonStr);
        MyLog.d(TAG, "设备 dps 经度:" + lonStr);

        // 获取纬度
        String latStr = (String) deviceBean.getDps().get("109");
        if (TextUtils.isEmpty(latStr)) {
            latStr = "0";
        }
        assert latStr != null;
        double lat = Double.parseDouble(latStr);
        MyLog.d(TAG, "设备 dps 纬度:" + latStr);

        // 存入经纬度类
        LatLng latLng = new LatLng(lon, lat);

        // 获取电量
        int electric = 0;
        if (null != deviceBean.getDps().get("1")) {
            electric = (int) deviceBean.getDps().get("1");
            MyLog.d(TAG, "电量:" + electric);
        }
        String electricStr = String.valueOf(electric);

        // 开始绘制标记点
        setMarker(latLng, deviceBean.getName(), "设备电量:" + electricStr + "%");
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
                MyLog.d(TAG, "标记点击：" + title);
                Intent intent = new Intent(context, DeviceListActivity.class);
                context.startActivity(intent);
                return true;
            }
            return false;
        });
    }


}
