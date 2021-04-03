package com.jiyehoo.informationentry;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputLayout;
import com.jiyehoo.informationentry.activity.SignUpActivity;
import com.jiyehoo.informationentry.presenter.LoginPresenter;
import com.jiyehoo.informationentry.util.BaseActivity;
import com.jiyehoo.informationentry.util.OnSwipeTouchListener;
import com.jiyehoo.informationentry.view.ILoginView;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends BaseActivity implements ILoginView {
    private final String TAG = "LoginActivity";

    private TextInputLayout mTilUsername, mTilPwd;
    private ImageView imageView;
    private TextView textView, mTvComp;
    private Button mBtnSignIn, mBtnSignUp;
    private EditText mEtUser, mEtPwd;
    private int count = 0;
    private SpinKitView spinKitView;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 全屏
        fullScreen();
        // 加载布局
        setContentView(R.layout.activity_login);
        setupWindowAnimations();
        // 绑定控件
        bindView();
        // P 的实现
        presenter = new LoginPresenter(this);
        // 按钮
        btnClick();
        // 时间背景
        autoPic();
        // 滑动
        swipeChangePic();

    }

    private void toMorning() {
        imageView.setImageResource(R.drawable.good_morning_img);
        textView.setText(getString(R.string.tv_login_title_right_morning));

        count = 0;
    }

    private void toNight() {
        imageView.setImageResource(R.drawable.good_night_img);
        textView.setText(getString(R.string.tv_login_title_right_night));
        count = 1;
    }

    private void bindView() {
        mTilUsername = findViewById(R.id.til_login_username);
        mTilPwd = findViewById(R.id.til_login_pwd);
        spinKitView = findViewById(R.id.loading_anim);
        mEtUser = findViewById(R.id.et_login_user);
        mEtPwd = findViewById(R.id.et_login_pwd);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        mBtnSignIn = findViewById(R.id.btn_sign_in);
        mBtnSignUp = findViewById(R.id.btn_sign_up);
        mTvComp = findViewById(R.id.tv_comp);

        // 设置错误颜色
        mTilPwd.setErrorTextColor(ColorStateList.valueOf(getColor(R.color.white)));
        mTilUsername.setErrorTextColor(ColorStateList.valueOf(getColor(R.color.white)));
    }

    private void btnClick() {
        mBtnSignIn.setOnClickListener(v -> {

            if (validateAccount(getUserName()) && validatePwd(getPwd())) {
                // 登录
                presenter.login(getUserName(), getPwd());
            } else {
                Log.d(TAG, "请输入合适的账号密码");
            }

        });

        mBtnSignUp.setOnClickListener(v -> {
            gotoSignInActivity();
        });

        mTvComp.setOnClickListener(v -> {
            final Uri uri = Uri.parse("http://blog.jiyehoo.com:81/");
            final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    private void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void autoPic() {
//        过时
//        Date date = new Date();
//        Log.d("Time", String.valueOf(date.getHours()));
        Calendar cal = Calendar.getInstance();
        int hours = cal.get( Calendar.HOUR_OF_DAY );
        Log.d("Time", String.valueOf(hours));

        if (hours < 12) {
            //上午
            toMorning();
        } else {
            toNight();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void swipeChangePic() {
        imageView.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
            }

            public void onSwipeRight() {
                if (count == 0) {
                    toNight();
                } else {
                    toMorning();
                }
            }

            public void onSwipeLeft() {
                if (count == 0) {
                    toNight();
                } else {
                    toMorning();
                }
            }

            public void onSwipeBottom() {
            }

        });
    }

    private void setupWindowAnimations() {
        Fade fade = new Fade();
        fade.setDuration(1000);
        getWindow().setEnterTransition(fade);

        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setReturnTransition(slide);
    }

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Toast is null");
        }
    }

    @Override
    public void showLoading() {
        spinKitView.setVisibility(View.VISIBLE);
    }

    @Override
    public void disShowLoading() {
        spinKitView.setVisibility(View.GONE);
    }

    @Override
    public void ableBtn() {
        mBtnSignIn.setEnabled(true);
    }

    @Override
    public void disableBtn() {
        mBtnSignIn.setEnabled(false);
    }

    @Override
    public void gotoMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void gotoSignInActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public String getUserName() {
        return mEtUser.getText().toString();
    }

    @Override
    public String getPwd() {
        return mEtPwd.getText().toString();
    }

    /**
     * 验证用户名
     */
    private boolean validateAccount(String account){
        if(TextUtils.isEmpty(account)){
            showError(mTilUsername,"请输入账号");
            return false;
        }
        mTilUsername.setErrorEnabled(false);
        return true;
    }

    /**
     * 验证密码
     */
    private boolean validatePwd(String pwd){
        if(TextUtils.isEmpty(pwd)){
            showError(mTilPwd,"请输入密码");
            return false;
        }
        mTilPwd.setErrorEnabled(false);
        return true;
    }

    private void showError(TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }
}