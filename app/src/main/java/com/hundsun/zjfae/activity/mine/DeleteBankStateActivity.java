package com.hundsun.zjfae.activity.mine;

import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class DeleteBankStateActivity extends BasicsActivity implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_delete_bank_state;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.delete_bank_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("解绑成功");
        findViewById(R.id.delete_bank_button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delete_bank_button:
                finish();
                break;
        }
    }
}
