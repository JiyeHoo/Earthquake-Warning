package com.jiyehoo.informationentry.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.jiyehoo.informationentry.LoginActivity;

public class BaseActivity extends AppCompatActivity {

    private OfflineReceive offlineReceive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.jiyehoo.broadcastoffline.OFFLINE");
        offlineReceive = new OfflineReceive();
        registerReceiver(offlineReceive, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (offlineReceive != null) {
            unregisterReceiver(offlineReceive);
            offlineReceive = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    static class OfflineReceive extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("警告");
            builder.setMessage("您的账号已下线，请尝试重新登录!");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", (dialog, which) -> {
                // 销毁所有活动
                ActivityCollector.finishAll();
                Intent intent1 = new Intent(context, LoginActivity.class);
                // 重新启动LoginActivity
                context.startActivity(intent1);
            });
            builder.show();
        }
    }

}
