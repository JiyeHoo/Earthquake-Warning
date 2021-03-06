package com.jiyehoo.informationentry.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jiyehoo.informationentry.LoginActivity;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.ResetPwdPresenter;
import com.jiyehoo.informationentry.util.MyLog;
import com.jiyehoo.informationentry.view.IResetPwdView;

import java.util.Objects;
/**
 * @author JiyeHoo
 * @description: 找回密码界面
 */
public class ResetPwdActivity extends AppCompatActivity implements IResetPwdView {
    private final String TAG = "ResetPwdActivity";

    private EditText mEtEmail, mEtPwd, mEtCode;
    private Button mBtnSendCode, mBtnReset;
    private TextView mTvGotoLogin;
    private TextInputLayout mTilEmail, mTilPwd, mTilCode;

    private ResetPwdPresenter pwdPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_reset_pwd);

        bindView();
        pwdPresenter = new ResetPwdPresenter(this);
        setClick();
        setEtListener();
        initEmail();
    }

    private void setEtListener() {
        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateEmail(getEmail())) {
                    mTilEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validatePassword(getPwd())) {
                    mTilPwd.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        mEtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateCode(getCode())) {
                    mTilCode.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setClick() {
        mBtnSendCode.setOnClickListener(v ->
                pwdPresenter.sendCode());

        mBtnReset.setOnClickListener(v ->
                pwdPresenter.resetPwd());

        mTvGotoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void bindView() {
        mEtEmail = findViewById(R.id.et_reset_pwd_email);
        mEtPwd = findViewById(R.id.et_reset_pwd);
        mEtCode = findViewById(R.id.et_reset_pwd_code);
        mBtnSendCode = findViewById(R.id.btn_reset_pwd_send_code);
        mBtnReset = findViewById(R.id.btn_reset_pwd);
        mTvGotoLogin = findViewById(R.id.tv_goto_login);
        mTilEmail = findViewById(R.id.text_input_layout_email);
        mTilPwd = findViewById(R.id.text_input_layout_password);
        mTilCode = findViewById(R.id.text_input_layout_code);
    }

    private void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initEmail() {
        String email = getIntent().getStringExtra("resetPwdEmail");
        if (!TextUtils.isEmpty(email) && email.length() > 0) {
            mEtEmail.setText(email);
        }
    }

    @Override
    public String getEmail() {
        return mEtEmail.getText().toString();
    }

    @Override
    public String getPwd() {
        return mEtPwd.getText().toString();
    }

    @Override
    public String getCode() {
        return mEtCode.getText().toString();
    }

    private void showError(TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);
        Objects.requireNonNull(textInputLayout.getEditText()).setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    /**
     * 验证邮箱
     */
    private boolean validateEmail(String account){
        if(TextUtils.isEmpty(account)){
            showError(mTilEmail, getString(R.string.reset_til_email_error_null));
            return false;
        }
        return true;
    }

    /**
     * 验证密码
     */
    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            showError(mTilPwd, getString(R.string.reset_til_pwd_error_null));
            return false;
        }

        if (password.length() < 6 || password.length() > 18) {
            showError(mTilPwd, getString(R.string.reset_til_pwd_error_length));
            return false;
        }

        return true;
    }

    /**
     * 验证验证码
     */
    private boolean validateCode(String code){
        if(TextUtils.isEmpty(code)){
            showError(mTilCode, getString(R.string.reset_til_code_error_null));
            return false;
        }

        if (code.length() != 6) {
            showError(mTilCode, getString(R.string.reset_til_code_error_length));
            return false;
        }
        return true;
    }

    @Override
    public void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else {
            MyLog.d(TAG, "Toast is null");
        }
    }

}