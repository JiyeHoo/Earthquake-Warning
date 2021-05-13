package com.jiyehoo.informationentry;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jiyehoo.informationentry.activity.ChartActivity;
import com.jiyehoo.informationentry.activity.DeviceListActivity;
import com.jiyehoo.informationentry.activity.ItemActivity4;
import com.jiyehoo.informationentry.activity.MapActivity;
import com.jiyehoo.informationentry.activity.NoticeActivity;
import com.jiyehoo.informationentry.activity.SetActivity;
import com.jiyehoo.informationentry.presenter.MainPresenter;
import com.jiyehoo.informationentry.util.BaseActivity;
import com.jiyehoo.informationentry.util.TimeUtil;
import com.jiyehoo.informationentry.view.IMainView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDataCallback;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.alterac.blurkit.BlurLayout;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author JiyeHoo
 * @description: 程序主界面
 */
public class MainActivity extends BaseActivity implements IMainView, EasyPermissions.PermissionCallbacks {

    private final String TAG = "###MainActivity";
    private TextView mTvOneText;
//    private RelativeLayout mRlMain;
    private BlurLayout blurLayout;
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton mFBtnMap, mFBtnShow, mFBtnSet;
    private TextView mTvNavName, mTvNavEmail;
    private ImageView mIvWeatherIcon;
    private CircleImageView mCivHeadPic;
    private MainPresenter presenter;
//    private LoadingDialogUtil loadingDialogUtil;

    // 权限
    String PERMISSION_STORAGE_MSG = "请授予权限，否则影响部分使用功能";
    int PERMISSION_STORAGE_CODE = 10001;
    String[] PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸状态栏
        fullScreen();
        // 加载布局
        setContentView(R.layout.activity_main);
        // 转场动画
        setupWindowAnimations();
        // 定位权限
        checkPermission();
        // 绑定View
        bindView();
        // 初始化显示信息
        initView();
        // 悬浮按钮点击
        FBtnClick();
        // 侧栏跳转
        setNav();
        // 获取 homeId，存入 Sp
        getHomeId();
        // 获取经纬度
//        getGps();
    }

//    private void getDeviceList() {
//        long homeId = HomeModel.getHomeId(this);
//        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
//            @Override
//            public void onSuccess(HomeBean bean) {
//                if (bean.getDeviceList() != null && bean.getDeviceList().size() > 0) {
//                    bean.getDeviceList().forEach(deviceBean ->
//                            Log.d(TAG, "设备名:" + deviceBean.getName() + " devId:" + deviceBean.getDevId()));
//                } else {
//                    Log.d(TAG, "设备列表为空");
//                }
//
//            }
//
//            @Override
//            public void onError(String errorCode, String errorMsg) {
//                Log.d(TAG, "设备列表获取失败");
//            }
//        });
//
//    }

    private void getHomeId() {
        // 获取 homeId，写入 sp
        presenter.setHomeId();

    }

    private void initView() {
        // P 的实现
        presenter = new MainPresenter(this);
        // 一言
        presenter.setOneText();

    }
    private void FBtnClick() {
        mFBtnMap.setOnClickListener(v -> {
            // todo 临时用于查询历史 上报
            // devID: 6ca4f3101238542849bago
            floatingActionsMenu.collapse();

            Log.d(TAG, "开始查询历史");
            Log.d(TAG, "ime stamp:" + TimeUtil.stampToDate(1620570646));
            Map<String, Object> map = new HashMap<>();
            map.put("devId", "6ca4f3101238542849bago");
            map.put("dpIds", "1,101,102,103,104,105,106,107,108,109,110"); // dp 点
            map.put("offset", 0); // 分页偏移量
            map.put("limit", 10); // 分页大小

            TuyaHomeSdk.getRequestInstance().requestWithApiName(
                    "tuya.m.smart.operate.all.log",
                    "1.0", map, String.class,
                    new ITuyaDataCallback<String>() {
                        @Override
                        public void onSuccess(String result) {
                            Log.d(TAG, "请求历史成功:" + result);
                        }

                        @Override
                        public void onError(String s, String s1) {
                            Log.d(TAG, "请求历史失败");
                        }
            });
        });

        mFBtnShow.setOnClickListener(v -> {
            // todo 临时用于获取设备列表
            floatingActionsMenu.collapse();

//            getDeviceList();
//            Intent intent = new Intent(this, ShowActivity.class);
//            startActivity(intent);
        });

        mFBtnSet.setOnClickListener(v -> {
            floatingActionsMenu.collapse();
            Intent intent = new Intent(this, SetActivity.class);
            startActivity(intent);
        });

    }

    private void setNav() {
        RelativeLayout item_1 = findViewById(R.id.rl_item_1);
        item_1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivity(intent);
        });

        RelativeLayout item_2 = findViewById(R.id.rl_item_2);
        item_2.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });

        RelativeLayout item_3 = findViewById(R.id.rl_item_3);
        item_3.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SetActivity.class);
            startActivity(intent);
        });

        RelativeLayout item_4 = findViewById(R.id.rl_item_4);
        item_4.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
            startActivity(intent);
        });
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void bindView() {
        mTvNavName = findViewById(R.id.tv_nav_user_name);
        mTvNavEmail = findViewById(R.id.tv_nav_user_email);
        mCivHeadPic = findViewById(R.id.civ_nav_head_pic);
        mTvOneText = findViewById(R.id.one_text);
        CardView cardView_1 = findViewById(R.id.card_1);
        CardView cardView_2 = findViewById(R.id.card_2);
        CardView cardView_3 = findViewById(R.id.card_3);
        CardView cardView_4 = findViewById(R.id.card_4);
//        mRlMain = findViewById(R.id.main_layout);
        blurLayout = findViewById(R.id.blurLayout);
        floatingActionsMenu = findViewById(R.id.floating_menu);
        mFBtnMap = findViewById(R.id.fab_map);
        mFBtnShow = findViewById(R.id.fab_show);
        mFBtnSet = findViewById(R.id.fab_set);
        FlowingDrawer mDrawer = findViewById(R.id.drawerlayout);
        mIvWeatherIcon = findViewById(R.id.iv_weather);

        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_FULLSCREEN);

        cardView_1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DeviceListActivity.class)));
        // todo 删除 ItemActivity
//        cardView_2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity2.class)));
        cardView_2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MapActivity.class)));
        cardView_3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ChartActivity.class)));
        cardView_4.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity4.class)));

        mTvNavName.setOnClickListener(v -> presenter.updateNickName());


        // 侧栏
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_DRAGGING_OPEN) {
                    blurLayout.startBlur();
                    blurLayout.setVisibility(View.VISIBLE);
                }
                if (newState == ElasticDrawer.STATE_DRAGGING_CLOSE) {
                    blurLayout.setVisibility(View.GONE);
                    blurLayout.pauseBlur();
                }
                if (newState == ElasticDrawer.STATE_CLOSING) {
                    if (blurLayout.getVisibility() == View.VISIBLE) {
                        blurLayout.setVisibility(View.GONE);
                        blurLayout.pauseBlur();
                    }
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
//                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });


    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        blurLayout.pauseBlur();
    }

    @Override
    public void showOneTextToTv(String text) {
        runOnUiThread(() -> {
            if (!TextUtils.isEmpty(text) && text.length() > 0) {
                mTvOneText.setText(text);
            }
        });
    }

    @Override
    public void showToast(String msg) {
        runOnUiThread(() -> {
            if (!TextUtils.isEmpty(msg) && msg.length() > 0) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void showUserInfo(String nickname, String email, String headPicUrl) {
        if (!TextUtils.isEmpty(nickname)) {
            mTvNavName.setText(nickname);
        }
        if (!TextUtils.isEmpty(email)) {
            mTvNavEmail.setText(email);
        }
        if (!TextUtils.isEmpty(headPicUrl)) {
            Glide.with(this).load(headPicUrl).into(mCivHeadPic);
        }
    }

    @Override
    public void showWeatherIcon(String url) {
        Glide.with(this).load(url).into(mIvWeatherIcon);
        mIvWeatherIcon.setColorFilter(Color.WHITE);
    }


    /**
     * 权限请求
     */
    private void checkPermission() {

        if (EasyPermissions.hasPermissions(this, PERMS)) {
            // 已经申请过权限，做想做的事
            Log.d(TAG, "定位权限拥有");
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
        presenter.getGps();
    }

    /**
     * 申请拒绝时调用
     * @param requestCode 请求权限的唯一标识码
     * @param perms 一系列权限
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NotNull List<String> perms) {
        Log.d(TAG, "权限申请失败");
        showToast("缺少定位权限");
    }
}