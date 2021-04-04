package com.jiyehoo.informationentry.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.jiyehoo.informationentry.LoginActivity;
import com.jiyehoo.informationentry.R;
import com.jiyehoo.informationentry.presenter.SignUpPresenter;
import com.jiyehoo.informationentry.view.ISignUpView;

import net.frakbot.jumpingbeans.JumpingBeans;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements ISignUpView {
    private final String TAG = "SignUpActivity";

    private TextInputLayout mTilEmail, mTilPhone, mTilPwd, mTilCode;
    private Button mBtnRegister, mBtnSendCode;
    private EditText mEtEmail, mEtPhone, mEtPwd, mEtCode;
    private TextView mTvHave;
    private TextView mTvTitle;

    private SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fullScreen();
        setContentView(R.layout.activity_sign_up);

        bindView();
        presenter = new SignUpPresenter(this);
        setListener();
    }

    @Override
    public String getEmail() {
        return mEtEmail.getText().toString();
    }

    @Override
    public String getPhone() {
        return mEtPhone.getText().toString();
    }

    @Override
    public String getPwd() {
        return mEtPwd.getText().toString();
    }

    @Override
    public String getCode() {
        return mEtCode.getText().toString();
    }

    public void setListener() {
        // 输入框检测
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validatePhone(getPhone())) {
                    mTilPhone.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateAccount(getEmail())) {
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

        // 发送验证码
        mBtnSendCode.setOnClickListener(v -> {
            if (validateAccount(getEmail())) {
                presenter.getSignCode(getEmail());
            } else {
                Log.d(TAG, "邮箱为空");
                Toast.makeText(this, "请输入正确邮箱", Toast.LENGTH_SHORT).show();
            }
        });

        // 注册
        mBtnRegister.setOnClickListener(v -> {

            mTilEmail.setErrorEnabled(false);
            mTilPwd.setErrorEnabled(false);

            if (validateAccount(getEmail()) &&
                    validatePassword(getPwd()) &&
                    validatePhone(getPhone()) &&
                    validateCode(getCode())) {

                presenter.register(getEmail(), getPwd(), getCode());
            }

        });

        // 已经有账号
        mTvHave.setOnClickListener(v ->
                gotoLoginActivity());
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
    public void gotoLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showJumpBean() {
        JumpingBeans.with(mTvTitle)
                .makeTextJump(0, mTvTitle.getText().toString().length())
                .setIsWave(true)
                .setLoopDuration(2500)
                .build();
    }

    private void bindView() {
        mEtEmail = findViewById(R.id.et_register_email);
        mEtPhone = findViewById(R.id.et_register_phone);
        mEtPwd = findViewById(R.id.et_register_pwd);
        mTilEmail = findViewById(R.id.text_input_layout_email);
        mTilPhone = findViewById(R.id.text_input_layout_phone);
        mTilPwd  =findViewById(R.id.text_input_layout_password);
        mTvTitle = findViewById(R.id.tv_sign_up_title);
        mBtnRegister = findViewById(R.id.btn_register);
        mTvHave = findViewById(R.id.tv_goto_sign_in);

        mEtCode = findViewById(R.id.et_register_code);
        mBtnSendCode = findViewById(R.id.btn_send_code);
        mTilCode = findViewById(R.id.text_input_layout_code);
    }

    private void fullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void showError(TextInputLayout textInputLayout, String error){
        textInputLayout.setError(error);
        Objects.requireNonNull(textInputLayout.getEditText()).setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }

    /**
     * 验证用户名
     */
    private boolean validateAccount(String account){
        if(TextUtils.isEmpty(account)){
            showError(mTilEmail,"邮箱不能为空");
            return false;
        }
        return true;
    }

    /**
     * 验证密码
     */
    private boolean validatePassword(String password) {
        if (TextUtils.isEmpty(password)) {
            showError(mTilPwd,"密码不能为空");
            return false;
        }

        if (password.length() < 6 || password.length() > 18) {
            showError(mTilPwd,"密码长度为6-18位");
            return false;
        }

        return true;
    }

    /**
     * 验证手机号
     */
    private boolean validatePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            showError(mTilPhone,"手机号不能为空");
            return false;
        }

        if (phone.length() != 11) {
            showError(mTilPhone,"请输入正确的手机号");
            return false;
        }

        return true;
    }

    /**
     * 验证验证码
     */
    private boolean validateCode(String code){
        if(TextUtils.isEmpty(code)){
            showError(mTilCode,"验证码不能为空");
            return false;
        }

        if (code.length() != 6) {
            showError(mTilCode,"请输入正确的验证码");
            return false;
        }
        return true;
    }
}