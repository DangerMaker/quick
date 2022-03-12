package com.hundsun.zjfae.activity.forget;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.StringUtils;

import okhttp3.ResponseBody;

//忘记密码
public class ForgetPasswordSetActivity extends CommActivity implements View.OnClickListener, ForgetPasswordView {

    private ForgetPasswordPresenter mPresenter;
    private EditText mNewPasswordEt;
    private EditText mNewPasswordAgainEt;
    private Button mCompleteBt;

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new ForgetPasswordPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_password_set;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_forget_password_set);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("找回登录密码");
        mNewPasswordEt = (EditText) findViewById(R.id.et_new_password);
        mNewPasswordAgainEt = (EditText) findViewById(R.id.et_new_password_again);
        mCompleteBt = (Button) findViewById(R.id.bt_complete);
        mCompleteBt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_complete:
                if(!StringUtils.isNotBlank(mNewPasswordEt.getText().toString())){
                    showDialog("请输入密码");
                    return;
                }
                if(!StringUtils.isNotBlank(mNewPasswordAgainEt.getText().toString())){
                    showDialog("请再次输入密码");
                    return;
                }
                mPresenter.setPassword(mNewPasswordEt.getText().toString(), mNewPasswordAgainEt.getText().toString());
                break;
            default:
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
    }

    @Override
    public void submitCode(String code, String msg) {

    }

    @Override
    public void setPassword(String code, String msg) {
        if ("0000".equals(code)) {
            showToast(msg);
            finish();
        } else {
            showDialog(msg);
        }
    }
}
