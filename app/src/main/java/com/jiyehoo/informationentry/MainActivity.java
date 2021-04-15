package com.jiyehoo.informationentry;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;
import com.jiyehoo.informationentry.activity.DeviceListActivity;
import com.jiyehoo.informationentry.activity.ItemActivity2;
import com.jiyehoo.informationentry.activity.ItemActivity3;
import com.jiyehoo.informationentry.activity.ItemActivity4;
import com.jiyehoo.informationentry.activity.MapActivity;
import com.jiyehoo.informationentry.activity.NoticeActivity;
import com.jiyehoo.informationentry.activity.SetActivity;
import com.jiyehoo.informationentry.activity.ShowActivity;
import com.jiyehoo.informationentry.model.HomeModel;
import com.jiyehoo.informationentry.presenter.MainPresenter;
import com.jiyehoo.informationentry.util.BaseActivity;
import com.jiyehoo.informationentry.view.IMainView;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.api.ITuyaDevice;

import de.hdodenhof.circleimageview.CircleImageView;
import io.alterac.blurkit.BlurLayout;

public class MainActivity extends BaseActivity implements IMainView {

    private final String TAG = "###MainActivity";

    private static final int REQUEST_CODE = 0;
    private static final int ACTIVITY_RESULT = 1;

    private TextView mTvOneText;
//    private RelativeLayout mRlMain;
    private BlurLayout blurLayout;
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton mFBtnMap, mFBtnShow, mFBtnSet;
    private TextView mTvNavName, mTvNavEmail;
    private CircleImageView mCivHeadPic;
    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 沉浸状态栏
        fullScreen();
        // 加载布局
        setContentView(R.layout.activity_main);
        // 转场动画
        setupWindowAnimations();
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


    }

    private void getDeviceList() {
        long homeId = HomeModel.getHomeId(this);
        TuyaHomeSdk.newHomeInstance(homeId).getHomeDetail(new ITuyaHomeResultCallback() {
            @Override
            public void onSuccess(HomeBean bean) {
                if (bean.getDeviceList() != null && bean.getDeviceList().size() > 0) {
                    bean.getDeviceList().forEach(deviceBean ->
                            Log.d(TAG, "设备名:" + deviceBean.getName() + " devId:" + deviceBean.getDevId()));
                } else {
                    Log.d(TAG, "设备列表为空");
                }

            }

            @Override
            public void onError(String errorCode, String errorMsg) {
                Log.d(TAG, "设备列表获取失败");
            }
        });

    }

    private void getHomeId() {
        // 获取 homeId，写入 sp
        presenter.setHomeId();

    }

    private void startScan() {
        Log.d(TAG, "开始扫描");
        HmsScanAnalyzerOptions options = new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE ).create();
        ScanUtil.startScan(this, ACTIVITY_RESULT, options);
    }

    private void initView() {
        // P 的实现
        presenter = new MainPresenter(this);
        // 一言
        presenter.setOneText();
        // 侧栏用户数据
        presenter.setUserInfo();
    }

    private void FBtnClick() {
        mFBtnMap.setOnClickListener(v -> {
            // todo 临时用于删除设备
            floatingActionsMenu.collapse();

            ITuyaDevice mDevice = TuyaHomeSdk.newDeviceInstance("6c0bd58978e0b41a66epyi");
            mDevice.removeDevice(new IResultCallback() {
                @Override
                public void onError(String code, String msg) {
                    Log.d(TAG, "移除失败:" + msg);
                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "移除成功");
                }
            });

//            Intent intent = new Intent(this, MapActivity.class);
//            startActivity(intent);
        });

        mFBtnShow.setOnClickListener(v -> {
            // todo 临时用于获取设备列表
            floatingActionsMenu.collapse();

            getDeviceList();
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
            Intent intent = new Intent(MainActivity.this, ShowActivity.class);
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

        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_FULLSCREEN);

        cardView_1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DeviceListActivity.class)));
        cardView_2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity2.class)));
        cardView_3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity3.class)));
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


    // 请求权限回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0) {
                // 因为是多个权限，所以需要一个循环获取每个权限的获取情况
                for (int i = 0; i < grantResults.length; i++) {
                    // PERMISSION_DENIED 这个值代表是没有授权，我们可以把被拒绝授权的权限显示出来
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(MainActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (i == grantResults.length - 1) {
                            // 有所有权限，扫码
                            startScan();
                        }
                    }
                }
            }
        }
    }
}