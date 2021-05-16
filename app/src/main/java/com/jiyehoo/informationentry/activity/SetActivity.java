package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.model.SetSpModel;
import com.jiyehoo.informationentry.presenter.SetPresenter;
import com.jiyehoo.informationentry.util.BaseActivity;
import com.jiyehoo.informationentry.util.FingerUtil;
import com.jiyehoo.informationentry.view.ISetView;
import com.nightonke.jellytogglebutton.JellyToggleButton;
import com.nightonke.jellytogglebutton.State;
/**
 * @author JiyeHoo
 * @description: 设置界面
 */
public class SetActivity extends BaseActivity implements ISetView, View.OnClickListener {

    private final String TAG = "###SetActivity";
    private SetPresenter presenter;
    private JellyToggleButton mSbFinger, mSbClear, mSbLocalMap, mSbCould, mSbNotice;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_set);

        bindView();
        setListener();
        presenter = new SetPresenter(this);
        initSwitch();

    }

    /**
     * 初始化开关状态
     */
    private void initSwitch() {
        presenter.initSwitchState();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setListener() {
        // 退出
        findViewById(R.id.btn_logout).setOnClickListener(this);

        // 更新
        findViewById(R.id.rl_update).setOnClickListener(this);

        // 指纹
        mSbFinger.setOnClickListener(v -> {
            if (mSbFinger.isChecked()) {
                // 开启指纹
                Log.d(TAG, "点击按钮，企图开启指纹");
                presenter.openFinger();
            } else {
                // 关闭指纹
                Log.d(TAG, "点击按钮，企图关闭指纹");
                presenter.closeFinger();
            }
        });


//        mSbFinger.setOnStateChangeListener((process, state, jtb) -> {
//            if (state == State.LEFT) {
//                // 关闭指纹
//                Log.d(TAG, "关闭指纹");
//                presenter.closeFinger();
//            } else if (state == State.RIGHT) {
//                // 开启指纹
//                Log.d(TAG, "开启指纹");
//                presenter.openFinger();
//            }
//        });

        // 退出清理缓存
        mSbClear.setOnStateChangeListener((process, state, jtb) -> {
            if (state == State.LEFT) {
                // 关闭
                Log.d(TAG, "关闭退出清理缓存");
                presenter.openClear();
            } else if (state == State.RIGHT) {
                // 开启
                Log.d(TAG, "开启退出清理缓存");
                presenter.closeClear();
            }
        });

        // 离线地图
        mSbLocalMap.setOnStateChangeListener((process, state, jtb) -> {
            if (state == State.LEFT) {
                // 关闭
                Log.d(TAG, "关闭离线地图");
                presenter.openLocalMap();
            } else if (state == State.RIGHT) {
                // 开启
                Log.d(TAG, "开启离线地图");
                presenter.closeLocalMap();
            }
        });

        // 云端同步
        mSbCould.setOnStateChangeListener((process, state, jtb) -> {
            if (state == State.LEFT) {
                // 关闭
                Log.d(TAG, "关闭云端同步");
                presenter.openCloud();
            } else if (state == State.RIGHT) {
                // 开启
                Log.d(TAG, "开启云端同步");
                presenter.closeCloud();
            }
        });

        // 消息通知
        mSbNotice.setOnStateChangeListener((process, state, jtb) -> {
            if (state == State.LEFT) {
                // 关闭
                Log.d(TAG, "关闭消息通知");
                presenter.openNotice();
            } else if (state == State.RIGHT) {
                // 开启
                Log.d(TAG, "开启消息通知");
                presenter.closeNotice();
            }
        });
    }

    private void bindView() {
        Toolbar mTbTitle = findViewById(R.id.tool_bar_set);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_set);

        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("设置");
        setSupportActionBar(mTbTitle);

        mSbFinger = findViewById(R.id.sb_finger);
        mSbClear = findViewById(R.id.sb_clear);
        mSbLocalMap = findViewById(R.id.sb_local_map);
        mSbCould = findViewById(R.id.sb_cloud);
        mSbNotice = findViewById(R.id.sb_notice);

        // 不能拖动
        mSbFinger.setDraggable(false);
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

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg) && msg.length() > 0) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showUpdateDialog(String title, String msg) {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title)
                    .setMessage(msg)
                    .setPositiveButton("更新", (dialog, which) -> {
                        Log.d(TAG, "打开浏览器更新");
                        presenter.updateByIntent();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });

    }

    /**
     * 开关指纹按钮
     */
    @Override
    public void setSbFinger(boolean isChecked) {
        mSbFinger.setChecked(isChecked);
    }

    /**
     * 立即更新指纹按钮
     */
    @Override
    public void setSbFingerImmediately(boolean isChecked) {
        mSbFinger.setCheckedImmediately(isChecked);
    }

    @Override
    public void finishSetActivity() {
        finish();
    }

    @Override
    public void onClick(View v) {
        // 退出
        if (v.getId() == R.id.btn_logout) {
            presenter.loginOut();
        }

        // 更新
        if (v.getId() == R.id.rl_update) {
            presenter.updateVersion();
        }
    }
}