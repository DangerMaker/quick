package com.hundsun.zjfae.activity.accountcenter;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.SettingSecurityIssuesAuthenticationPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.SettingSecurityIssuesAuthenticationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;

import onight.zjfae.afront.gens.VerifySecurityInfoPB;

public class SettingSecurityIssuesAuthenticationActivity extends CommActivity implements View.OnClickListener, SettingSecurityIssuesAuthenticationView {
    private CustomCountDownTimer countDownTimer;
    private TextView mTvVerificationCode;//验证码按钮
    private EditText mIdEt;
    private EditText mLoginEt;
    private EditText mVerificationCodeEt;
    private SettingSecurityIssuesAuthenticationPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_authentication;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new SettingSecurityIssuesAuthenticationPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_setting_authentication);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("身份验证");
        mTvVerificationCode = findViewById(R.id.tv_verification_code);
        mTvVerificationCode.setOnClickListener(this);
        countDownTimer = new CustomCountDownTimer(60000, 1000, mTvVerificationCode);
        findViewById(R.id.bt_next).setOnClickListener(this);
        mIdEt = (EditText) findViewById(R.id.et_id);
        mLoginEt = (EditText) findViewById(R.id.et_login);
        mVerificationCodeEt = (EditText) findViewById(R.id.et_verification_code);
        if ("99".equals(getIntent().getStringExtra("certificateType"))) {
            //99的话说明没有绑定身份证 所以身份证布局隐藏
            mIdEt.setVisibility(View.GONE);
        } else {
            mIdEt.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verification_code:
                if (mIdEt.getVisibility() == View.VISIBLE && mIdEt.getText().toString().isEmpty()) {
                    showDialog("身份证号码不能为空");
                    return;
                }
                if (mLoginEt.getText().toString().isEmpty()) {
                    showDialog("登录密码不能为空");
                } else {
                    mPresenter.verifySecurityInfo(getIntent().getStringExtra("certificateType"), mIdEt.getText().toString(), EncDecUtil.AESEncrypt(mLoginEt.getText().toString()));
                }
                break;
            case R.id.bt_next:
                if (mIdEt.getVisibility() == View.VISIBLE && mIdEt.getText().toString().isEmpty()) {
                    showDialog("身份证号码不能为空");
                    return;
                }
                if (mLoginEt.getText().toString().isEmpty()) {
                    showDialog("登录密码不能为空");
                } else if (mVerificationCodeEt.getText().toString().isEmpty()) {
                    showDialog("验证码不能为空");
                } else {
                    mPresenter.check(mIdEt.getText().toString(), EncDecUtil.AESEncrypt(mLoginEt.getText().toString()), mVerificationCodeEt.getText().toString());
                }
                break;
        }
    }

    @Override
    public void verifySecurityInfo(VerifySecurityInfoPB.PBIFE_userbaseinfo_verifySecurityInfo securityInfo) {
        if (mIdEt.getVisibility() == View.VISIBLE) {//身份证输入框显示
            if ("true".equals(securityInfo.getCardValid()) && "true".equals(securityInfo.getPasswordValid())) {
                mPresenter.getVerificationCode();
            } else {
                if ("false".equals(securityInfo.getCardValid())) {
                    showDialog("输入的身份证号有误");
                    return;
                }
                if ("false".equals(securityInfo.getPasswordValid())) {
                    showDialog("输入的登录密码错误");
                }
            }
        } else {//身份证输入框不显示
            if ("true".equals(securityInfo.getPasswordValid())) {
                mPresenter.getVerificationCode();
            } else {
                showDialog("输入的登录密码错误");
            }
        }
    }

    @Override
    public void getVerificationCode(String code, String msg) {
        if ("0000".equals(code)) {
            countDownTimer.start();
        }
        showDialog(msg);
    }

    @Override
    public void check(String code, String msg) {
        if ("0000".equals(code)) {
            baseStartActivity(SettingSecurityIssuesAuthenticationActivity.this, SettingSecurityIssuesActivity.class);
        } else {
            showDialog(msg);
        }
    }
}
