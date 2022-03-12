package com.hundsun.zjfae.activity.accountcenter;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.presenter.ModifyPasswordPresenter;
import com.hundsun.zjfae.activity.accountcenter.view.ModifyPasswordView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;

//修改登录密码与修改交易密码
public class ModifyPasswordActivity extends CommActivity implements View.OnClickListener, ModifyPasswordView {

    private EditText mEtOldPassword;//原密码
    private EditText mEtNewPassword;//新密码
    private EditText mEtNewPasswordAgain;//再次输入新密码
    private TextView mTvTip;//密码提示
    private ModifyPasswordPresenter mPresenter;

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new ModifyPasswordPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_password;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_modify_password);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        mEtOldPassword = findViewById(R.id.et_old_password);
        mEtNewPassword = findViewById(R.id.et_new_password);
        mEtNewPasswordAgain = findViewById(R.id.et_new_password_again);
        findViewById(R.id.bt_complete).setOnClickListener(this);
        mTvTip = findViewById(R.id.tv_tip);
        if (getIntent() != null) {
            if ("login".equals(getIntent().getStringExtra("type"))) {
                setTitle("修改登录密码");
                mEtNewPassword.setHint("请输入新的登录密码");
                mEtNewPasswordAgain.setHint("请确认新的登录密码");
                mTvTip.setText("登录密码由6~16位数字和字母组成");
            } else {
                if ("reset_transaction".equals(getIntent().getStringExtra("type"))) {
                    mEtOldPassword.setVisibility(View.GONE);
                }
                setTitle("修改交易密码");
                mEtOldPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                mEtNewPassword.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                mEtNewPasswordAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
                mEtNewPassword.setHint("请输入新的交易密码");
                mEtNewPasswordAgain.setHint("请确认新的交易密码");
                mTvTip.setText("交易密码由6位数字组成");
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt_complete) {
            if (!getIntent().getStringExtra("type").equals("reset_transaction")) {
                if (mEtOldPassword.getText().toString().isEmpty()) {
                    showDialog("原始密码不能为空");
                    return;
                }
            }
            if (mEtNewPassword.getText().toString().isEmpty()) {
                showDialog("新密码不能为空");
                return;
            } else if (mEtNewPasswordAgain.getText().toString().isEmpty()) {
                showDialog("二次密码不能为空");
                return;
            } else if (!mEtNewPassword.getText().toString().equals(mEtNewPasswordAgain.getText().toString())) {
                showDialog("二次密码不一致");
                return;
            } else {
                mPresenter.modifyPassword(getIntent().getStringExtra("type"), EncDecUtil.AESEncrypt(mEtOldPassword.getText().toString()), EncDecUtil.AESEncrypt(mEtNewPassword.getText().toString()), EncDecUtil.AESEncrypt(mEtNewPasswordAgain.getText().toString()));
            }
        }
    }

    @Override
    public void modify(String returnCode, String msg) {
        if (returnCode.equals("0000")) {
            Intent intent = new Intent(ModifyPasswordActivity.this, ModifySuccessActivity.class);
            intent.putExtra("type", getIntent().getStringExtra("type"));
            baseStartActivity(intent);
            setResult(RESULT_OK);
            finish();
        } else {
            showDialog(msg);
        }
    }
}
