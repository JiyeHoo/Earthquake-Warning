package com.jiyehoo.informationentry.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.fragment.BaseFragment;
import com.jiyehoo.informationentry.fragment.BaseFragment2;
import com.jiyehoo.informationentry.fragment.BaseFragment3;
import com.jiyehoo.informationentry.fragment.MyFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity1 extends AppCompatActivity {

    private Toolbar mTbTitle;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mImageView;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private BaseFragment baseFragment;
    private BaseFragment2 baseFragment2;
    private BaseFragment3 baseFragment3;
    private String[] titles = {"采集", "施肥"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item1);
        fullScreen();
        bindView();
        setSupportActionBar(mTbTitle);

        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(true);
        }
        mCollapsingToolbarLayout.setTitle("林业基础信息");
        Glide.with(this).load(R.drawable.item_3).into(mImageView);
    }

    private void bindView() {
        mTbTitle = findViewById(R.id.tb_toolbar_bt_ctl);
        mCollapsingToolbarLayout = findViewById(R.id.ctl_collapsing_toolbar_layout);
        mImageView = findViewById(R.id.iv_pic_show_by_ctl);

        viewPager2 = findViewById(R.id.view_pager_2);
        tabLayout = findViewById(R.id.tab_layout);

        baseFragment = new BaseFragment();
        baseFragment2 = new BaseFragment2();
        //baseFragment3 = new BaseFragment3();

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(baseFragment);
        fragmentList.add(baseFragment2);
        //fragmentList.add(baseFragment3);

        viewPager2.setAdapter(new MyFragmentAdapter(this, fragmentList));

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles[position]);
            }
        }).attach();

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
}