package com.jiyehoo.informationentry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.LoginActivity;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.util.BaseActivity;

public class SetActivity extends BaseActivity {

    private Button mBtnLogout;
    private Toolbar mTbTitle;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_set);

        bindView();
        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("设置");
    }

    private void bindView() {
        mBtnLogout = findViewById(R.id.btn_logout);
        mTbTitle = findViewById(R.id.tool_bar_set);
        mCollapsingToolbarLayout = findViewById(R.id.ctl_set);

        mBtnLogout.setOnClickListener(v -> {
            Intent intent = new Intent("com.jiyehoo.broadcastoffline.OFFLINE");
            sendBroadcast(intent);
        });
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