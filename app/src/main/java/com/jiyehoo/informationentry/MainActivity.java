package com.jiyehoo.informationentry;

import androidx.cardview.widget.CardView;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jiyehoo.informationentry.activity.ItemActivity1;
import com.jiyehoo.informationentry.activity.ItemActivity2;
import com.jiyehoo.informationentry.activity.ItemActivity3;
import com.jiyehoo.informationentry.activity.ItemActivity4;
import com.jiyehoo.informationentry.activity.MapActivity;
import com.jiyehoo.informationentry.activity.NoticeActivity;
import com.jiyehoo.informationentry.activity.SetActivity;
import com.jiyehoo.informationentry.activity.ShowActivity;
import com.jiyehoo.informationentry.util.BaseActivity;
import com.jiyehoo.informationentry.util.HttpUtil;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import io.alterac.blurkit.BlurLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.jiyehoo.informationentry.R.color.black;

public class MainActivity extends BaseActivity {

    private CardView cardView_1, cardView_2, cardView_3, cardView_4;
    private FlowingDrawer mDrawer;
    private TextView mTvOneText;
    private RelativeLayout mRlMain;
    private BlurLayout blurLayout;
    private FloatingActionsMenu floatingActionsMenu;
    private FloatingActionButton mFBtnMap, mFBtnShow, mFBtnSet;

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
        // 悬浮按钮点击
        FBtnClick();
        // 一言
        setOneText();
        // 侧栏跳转
        setNav();
    }

    private void FBtnClick() {
        mFBtnMap.setOnClickListener(v -> {
            floatingActionsMenu.collapse();
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

        mFBtnShow.setOnClickListener(v -> {
            floatingActionsMenu.collapse();
            Intent intent = new Intent(this, ShowActivity.class);
            startActivity(intent);
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
        mRlMain = findViewById(R.id.main_layout);
        blurLayout = findViewById(R.id.blurLayout);

        mDrawer = findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_FULLSCREEN);

        mTvOneText = findViewById(R.id.one_text);

        cardView_1 = findViewById(R.id.card_1);
        cardView_2 = findViewById(R.id.card_2);
        cardView_3 = findViewById(R.id.card_3);
        cardView_4 = findViewById(R.id.card_4);

        cardView_1.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity1.class)));
        cardView_2.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity2.class)));
        cardView_3.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity3.class)));
        cardView_4.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ItemActivity4.class)));

        floatingActionsMenu = findViewById(R.id.floating_menu);
        mFBtnMap = findViewById(R.id.fab_map);
        mFBtnShow = findViewById(R.id.fab_show);
        mFBtnSet = findViewById(R.id.fab_set);

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
                Log.i("MainActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });


    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);
    }

    /**
     * 调整窗口的透明度
     * @param from>=0&&from<=1.0f
     * @param to>=0&&to<=1.0f
     *
     * */
    private void dimBackground(final float from, final float to) {
        final Window window = getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(animation -> {
            WindowManager.LayoutParams params = window.getAttributes();
            params.alpha = (Float) animation.getAnimatedValue();
            window.setAttributes(params);
        });

        valueAnimator.start();
    }

    private void setOneText() {
        HttpUtil.sendOkHttpRequest("https://v1.hitokoto.cn/?c=i&encode=text&length=15", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("###autoText", "一言请求失败");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String s = response.body().string();
                runOnUiThread(() -> {
                    mTvOneText.setText(s);
                });
            }
        });
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
}