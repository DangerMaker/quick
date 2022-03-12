package com.hundsun.zjfae.activity.mine;

import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class ChangeCardStateActivity extends BasicsActivity implements View.OnClickListener {


    @Override
    protected void initView() {
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
    protected int getLayoutId() {
        return R.layout.activity_change_card_stata;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bank_state_button:
                HomeActivity.show(this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        HomeActivity.show(this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
        return true;
    }
}
