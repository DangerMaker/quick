package com.hundsun.zjfae.activity.forget;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;

import okhttp3.ResponseBody;

public class ForgetPasswordValidateCode extends CommActivity implements View.OnClickListener, ForgetPasswordView {
    private CustomCountDownTimer countDownTimer;
    private TextView mTvVerificationCode;//验证码按钮
    private EditText mEtCode;
    private ForgetPasswordPresenter mPresenter;

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new ForgetPasswordPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_authentication);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password_code;
    }

    @Override
    public void initView() {
        setTitle("找回登录密码");
        mTvVerificationCode = findViewById(R.id.tv_verification_code);
        mEtCode = findViewById(R.id.et_verification_code);
        mTvVerificationCode.setOnClickListener(this);
        countDownTimer = new CustomCountDownTimer(60000, 1000, mTvVerificationCode);
        findViewById(R.id.bt_next).setOnClickListener(this);
        mPresenter.sendCode();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verification_code:
                mPresenter.sendCode();
                break;
            case R.id.bt_next:
                if (StringUtils.isNotBlank(mEtCode.getText().toString())) {
                    //验证码输入框非空判断
                    mPresenter.checkCode(mEtCode.getText().toString());
                } else {
                    showDialog("验证码不能为空");
                }
                break;
        }
    }

    @Override
    public void imageCode(ResponseBody body) {

    }

    @Override
    public void refreshImageAuthCode(String msg) {

    }

    @Override
    public void checkUserName(String code, String msg) {

    }

    @Override
    public void sendCode(String code, String msg) {
        if ("0000".equals(code)) {
            countDownTimer.start();
        }
        showDialog(msg);
    }

    //提交验证码
    @Override
    public void submitCode(String code, String msg) {
        if ("0000".equals(code)) {
            baseStartActivity(this, ForgetPasswordSetActivity.class);
            finish();
        } else {
            showDialog(msg);
        }
    }

    @Override
    public void setPassword(String code, String msg) {

    }
}
