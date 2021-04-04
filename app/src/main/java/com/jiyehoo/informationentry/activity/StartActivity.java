package com.jiyehoo.informationentry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.jiyehoo.informationentry.LoginActivity;
import com.jiyehoo.informationentry.R;

import me.wangyuwei.particleview.ParticleView;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();

        setContentView(R.layout.activity_start);

        // 单例
        Intent intent = getIntent();
        if (!isTaskRoot()
                && intent != null
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.getAction() != null
                && intent.getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }

        ParticleView mParticleView = findViewById(R.id.pv);
        mParticleView.startAnim();

        mParticleView.setOnParticleAnimListener(() -> {
            startActivity(new Intent(this, LoginActivity.class));
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
//            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        });

    }

    private void fullScreen() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }
}