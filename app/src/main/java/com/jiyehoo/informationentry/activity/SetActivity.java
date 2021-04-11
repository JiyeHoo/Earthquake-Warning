package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.SetPresenter;
import com.jiyehoo.informationentry.util.BaseActivity;
import com.jiyehoo.informationentry.view.ISetView;

public class SetActivity extends BaseActivity implements ISetView {

    private Button mBtnLogout;
    private Toolbar mTbTitle;
    private SetPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_set);

        bindView();
        setListener();
        presenter = new SetPresenter(this);
        setSupportActionBar(mTbTitle);

    }

    private void setListener() {
        // 退出
        mBtnLogout.setOnClickListener(v ->
                presenter.loginOut());
    }

    private void bindView() {
        mBtnLogout = findViewById(R.id.btn_logout);
        mTbTitle = findViewById(R.id.tool_bar_set);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_set);



        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("图表展示");

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

}