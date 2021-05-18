package com.jiyehoo.informationentry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.adapter.NoticeAdapter;
import com.jiyehoo.informationentry.presenter.NoticePresenter;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.INoticeView;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.ITuyaDataCallback;
import com.tuya.smart.sdk.bean.message.MessageBean;
import com.tuya.smart.sdk.bean.message.MessageListBean;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JiyeHoo
 * @description: 通知界面
 */
public class NoticeActivity extends AppCompatActivity implements INoticeView, View.OnClickListener {

    private final String TAG = "###NoticeActivity";
    private NoticePresenter presenter;
    private RecyclerView mRvNoticeList;
    private SwipeRefreshLayout mSrlNoticeList;
    private TextView mTvNoNoticeTip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_notice);

        initView();
        presenter = new NoticePresenter(this);
        presenter.initData();
    }

    private void initView() {
        Toolbar mTbTitle = findViewById(R.id.tool_bar_notice);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_notice);

        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("系统通知");
        setSupportActionBar(mTbTitle);

        mRvNoticeList = findViewById(R.id.rv_notice_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRvNoticeList.setLayoutManager(layoutManager);

        mSrlNoticeList = findViewById(R.id.srl_notice_list);
        mSrlNoticeList.setOnRefreshListener(refreshListener);

        mTvNoNoticeTip = findViewById(R.id.tv_no_notice_tip);

        findViewById(R.id.fab_delete_notice_list).setOnClickListener(this);
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
    public void showRv(NoticeAdapter adapter) {
        runOnUiThread(() ->
                mRvNoticeList.setAdapter(adapter));
    }

    /**
     * 没有预警信息时候提示
     * @param isShow 是否显示
     */
    @Override
    public void showNoNoticeTip(boolean isShow) {
        runOnUiThread(() -> {
            if (isShow) {
                mTvNoNoticeTip.setVisibility(View.VISIBLE);
            } else {
                mTvNoNoticeTip.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 显示下拉刷新
     * @param isShow 是否显示
     */
    @Override
    public void showLoading(boolean isShow) {
        runOnUiThread(() -> mSrlNoticeList.setRefreshing(isShow));
    }

    /**
     * 下拉刷新监听
     */
    private final SwipeRefreshLayout.OnRefreshListener refreshListener = () -> {
        // 下拉刷新
        rvRemoveAll();
        presenter.initData();
    };

    /**
     * 清空 Rv 所有
     */
    public void rvRemoveAll() {
        runOnUiThread(() ->
                mRvNoticeList.removeAllViews());
    }

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg) && msg.length() > 0) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        // 清空通知
        if (v.getId() == R.id.fab_delete_notice_list) {
            showDialog("清空通知",
                    "是否删除所有通知？删除之后无法恢复！",
                    (dialog, which) -> presenter.deleteAllNotice());
        }
    }

    @Override
    public void showDialog(String title, String msg, DialogInterface.OnClickListener listener) {
        if (TextUtils.isEmpty(title)) {
            title = "提示";
        }
        if (TextUtils.isEmpty(msg)) {
            msg = "msg is null";
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null)
                .show();
        alertDialog.show();
    }
}