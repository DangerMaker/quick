package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.ModifySuccessActivity;
import com.hundsun.zjfae.activity.mine.presenter.FirstPlayPassWordPresenter;
import com.hundsun.zjfae.activity.mine.view.FirstPlayPassWordView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;

public class FirstPlayPassWordActivity extends CommActivity implements View.OnClickListener, FirstPlayPassWordView {

    private EditText play_first_pw, verify_play_pw;
    private FirstPlayPassWordPresenter presenter;
    private static final int PASSWORD_REQUEST_CODE = 0x77;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_first_play_pass_word;
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new FirstPlayPassWordPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.first_play_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("首次设置交易密码");
        play_first_pw = findViewById(R.id.play_first_pw);
        verify_play_pw = findViewById(R.id.verify_play_pw);
        findViewById(R.id.play_pw_commit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pw_commit:
                if (Utils.isViewEmpty(play_first_pw)) {
                    showDialog("请输入交易密码");
                    return;
                }
                if (Utils.isViewEmpty(verify_play_pw)) {
                    showDialog("二次密码不能为空");
                    return;
                }
                if (!play_first_pw.getText().toString().equals(verify_play_pw.getText().toString())) {
                    showDialog("两次密码输入不一致");
                    return;
                }
                presenter.setTradePassword(play_first_pw.getText().toString(), verify_play_pw.getText().toString());

                break;
        }
    }

    @Override
    public void tradePassword(String returnCode, String returnMsg) {
        if (returnCode.equals("0000")) {
            Intent intent = new Intent(this, ModifySuccessActivity.class);
            intent.putExtra("type", "firstsetplaypassword");
            startActivityForResult(intent,PASSWORD_REQUEST_CODE);
            //baseStartActivity(intent);

        } else {
            showDialog(returnMsg);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PASSWORD_REQUEST_CODE){
            setResult(RESULT_OK);
            finish();
        }
    }
}
