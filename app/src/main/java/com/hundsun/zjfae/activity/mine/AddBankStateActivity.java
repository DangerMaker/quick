package com.hundsun.zjfae.activity.mine;

import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class AddBankStateActivity extends BasicsActivity implements View.OnClickListener {




    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_bank_state;
    }

    @Override
    public void initView() {
        setNoBack();
        setTitle("绑卡成功");
        findViewById(R.id.bank_state_button).setOnClickListener(this);
    }



    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.bank_state_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bank_state_button:

                if (getIntent().getBooleanExtra("isMine",false)){
                    HomeActivity.show(AddBankStateActivity.this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                }
                else {
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
