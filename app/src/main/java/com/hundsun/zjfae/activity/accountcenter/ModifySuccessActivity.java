package com.hundsun.zjfae.activity.accountcenter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class ModifySuccessActivity extends BasicsActivity implements View.OnClickListener {
    private TextView mSuccessTv;
    private Button mCompleteBt;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_success;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_modify_success);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected void topDefineCancel() {
        baseStartActivity(this, AccountCenterActivity.class);
        finish();
    }


    @Override
    public void initView() {
        mTopDefineCancel = true;
        setTitle("成功");
        findViewById(R.id.ll_commonn_title_back).setVisibility(View.INVISIBLE);
        mSuccessTv = (TextView) findViewById(R.id.tv_success);
        mCompleteBt = (Button) findViewById(R.id.bt_complete);
        mCompleteBt.setOnClickListener(this);
        if ("login".equals(getIntent().getStringExtra("type"))) {
            mSuccessTv.setText("修改登录密码成功");
        } else if ("firstsetplaypassword".equals(getIntent().getStringExtra("type"))) {
            mSuccessTv.setText("设置交易密码成功");
        } else if ("reset_transaction".equals(getIntent().getStringExtra("type"))) {
            mSuccessTv.setText("重置交易密码成功");
        } else if ("setProblem".equals(getIntent().getStringExtra("type"))) {
            mSuccessTv.setText("安保问题设置成功");
        } else {
            mSuccessTv.setText("修改交易密码成功");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_complete:
                if (!mSuccessTv.getText().toString().equals("设置交易密码成功")) {
                    baseStartActivity(this, AccountCenterActivity.class);
                }
                finish();
                break;
            default:
                break;
        }
    }
}
