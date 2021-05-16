package com.jiyehoo.informationentry.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.DisasterPresenter;
import com.jiyehoo.informationentry.view.IDisasterView;

/**
 * @author JiyeHoo
 * @description: 灾害发布界面
 */
public class DisasterActivity extends AppCompatActivity implements IDisasterView {

    private final String TAG = "###DisasterActivity";
    private DisasterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_item4);
        bindView();

        // 获取灾害数据
        presenter = new DisasterPresenter(this);
        presenter.getDisasterInfo();
    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    private void bindView() {
        Toolbar mTbTitle = findViewById(R.id.tool_bar_4);
        CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.ctl_4);
        mCollapsingToolbarLayout.setTitle("灾害发布");
        setSupportActionBar(mTbTitle);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}