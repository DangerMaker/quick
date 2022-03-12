package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class DeleteBankStataActivity extends BasicsActivity implements View.OnClickListener {


    @Override
    protected void initView() {
        setNoBack();
        findViewById(R.id.add_bank).setOnClickListener(this);
        findViewById(R.id.clean_bank).setOnClickListener(this);
    }

    @Override
    public void resetLayout() {

        LinearLayout layout = findViewById(R.id.delete_state_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_delete_bank_stata;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.add_bank:
                Intent intent = new Intent(this,AddBankActivity.class);
                intent.putExtra("isMine",true);
                baseStartActivity(intent);
                break;
            case R.id.clean_bank:
                HomeActivity.show(DeleteBankStataActivity.this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        HomeActivity.show(DeleteBankStataActivity.this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
        return true;
    }
}
