package com.hundsun.zjfae.activity.moneymanagement;

import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

//我的理财
public class MyMoneyManagementActivity extends BasicsActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_money_management;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_my_money_management);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("我的理财");
        findViewById(R.id.my_holding).setOnClickListener(this);
        findViewById(R.id.my_subscription).setOnClickListener(this);
        findViewById(R.id.my_entrust).setOnClickListener(this);
        findViewById(R.id.record_of_transaction).setOnClickListener(this);
        findViewById(R.id.holding_water).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_holding://我的资产
                baseStartActivity(this, MyHoldingActivity.class);
                break;
            case R.id.my_subscription://我的购买
                baseStartActivity(this, MySubscriptionActivity.class);
                break;
            case R.id.my_entrust://我的委托
                baseStartActivity(this, MyEntrustActivity.class);
                break;
            case R.id.record_of_transaction://交易记录
                baseStartActivity(this, RecordTransactionActivity.class);
                break;
            case R.id.holding_water://交易流水
                baseStartActivity(this, HoldingWaterActivity.class);
                break;
            default:
        }
    }
}
