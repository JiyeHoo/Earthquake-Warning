package com.jiyehoo.informationentry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;

public class ItemActivity3 extends AppCompatActivity {

    private Toolbar mTbTitle;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_item3);
        bindView();
        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("土壤化学性质");
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

    private void bindView() {
        mTbTitle = findViewById(R.id.tool_bar_3);
        mCollapsingToolbarLayout = findViewById(R.id.ctl_3);
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