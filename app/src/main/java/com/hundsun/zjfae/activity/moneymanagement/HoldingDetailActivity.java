package com.hundsun.zjfae.activity.moneymanagement;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.product.SpvProductDetailActivity;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;

/**
  * @Package:        com.hundsun.zjfae.activity.moneymanagement
  * @ClassName:      HoldingDetailActivity
  * @Description:    我的持仓详情
  * @Author:         moran
  * @CreateDate:     2020/10/29 19:19
  * @UpdateUser:     更新者：moran
  * @UpdateDate:     2020/10/29 19:19
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class HoldingDetailActivity extends BasicsActivity implements View.OnClickListener {
    private TextView mTitle;
    private TextView mState;
    private TextView mUnit;
    private TextView mFreeze;
    private TextView mLeftDays;
    private TextView mManageEndDate;
    private TextView mExpectedMaxAnnualRate;
    private TextView mIsTransfer;
    private TextView mCanTransferAmount;
    private TextView mNoTransferAmount;
    private TextView mPreProfit;
    private TextView mIfTimeChoose;
    private Button mSellBt;
    private TextView mDealBank;
    private LinearLayout mPayInterestLl,ll_isWholeTransfer;

    private TextView unitDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_holding_detail;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_holding_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("资产详情");
        mTitle = (TextView) findViewById(R.id.title);
        mState = (TextView) findViewById(R.id.state);
        mUnit = (TextView) findViewById(R.id.unit);
        mFreeze = (TextView) findViewById(R.id.freeze);
        mLeftDays = (TextView) findViewById(R.id.leftDays);
        mManageEndDate = (TextView) findViewById(R.id.manageEndDate);
        mExpectedMaxAnnualRate = (TextView) findViewById(R.id.expectedMaxAnnualRate);
        mIsTransfer = (TextView) findViewById(R.id.isTransfer);
        mCanTransferAmount = (TextView) findViewById(R.id.canTransferAmount);
        mNoTransferAmount = (TextView) findViewById(R.id.noTransferAmount);
        mPreProfit = (TextView) findViewById(R.id.preProfit);
        mIfTimeChoose = (TextView) findViewById(R.id.ifTimeChoose);
        unitDate = findViewById(R.id.unitDate);
        mSellBt = (Button) findViewById(R.id.bt_sell);
        mSellBt.setOnClickListener(this);
        ll_isWholeTransfer = findViewById(R.id.ll_isWholeTransfer);
        String isWholeTransfer = getIntent().getStringExtra("isWholeTransfer");
        //是否是整体转让产品
        if ("1".equals(isWholeTransfer)){
            ll_isWholeTransfer.setVisibility(View.VISIBLE);
        }
        else{
            ll_isWholeTransfer.setVisibility(View.GONE);
        }
        mTitle.setText(getIntent().getStringExtra("title"));
        Utils.setTextViewGravity(mTitle);
        mState.setText(getIntent().getStringExtra("state"));
        mUnit.setText(getIntent().getStringExtra("unit") + "元");
        mFreeze.setText(getIntent().getStringExtra("freeze") + "元");
        mLeftDays.setText(getIntent().getStringExtra("leftDays") + "天");
        mManageEndDate.setText(getIntent().getStringExtra("manageEndDate"));
        mExpectedMaxAnnualRate.setText(getIntent().getStringExtra("expectedMaxAnnualRate") + "%");
        unitDate.setText(getIntent().getStringExtra("unitDate"));
        if ("transfer".equals(getIntent().getStringExtra("isTransfer"))) {
            mIsTransfer.setText("可转让");
        } else {
            mIsTransfer.setText("不可转让");
        }
        if ("false".equals(getIntent().getStringExtra("ifCanTransfer"))) {
            mSellBt.setEnabled(false);
            mSellBt.setBackground(getResources().getDrawable(R.drawable.account_button_selector_enable));
        }
        mCanTransferAmount.setText(getIntent().getStringExtra("canTransferAmount") + "元");
        mNoTransferAmount.setText(getIntent().getStringExtra("noTransferAmount") + "元");
        mPreProfit.setText(getIntent().getStringExtra("preProfit") + "元");
        if (getIntent().getStringExtra("ifTimeChoose").equals("true")) {
            mIfTimeChoose.setText("是");
        } else if (getIntent().getStringExtra("ifTimeChoose").equals("false")) {
            mIfTimeChoose.setText("否");
        } else {
            mIfTimeChoose.setText("--");
        }
        mDealBank = (TextView) findViewById(R.id.bank_deal);
        mDealBank.setOnClickListener(this);
        mPayInterestLl = (LinearLayout) findViewById(R.id.ll_pay_interest);
        mPayInterestLl.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_sell:
                Intent intent2 = new Intent();
                intent2.putExtra("id", getIntent().getSerializableExtra("id"));
                intent2.putExtra("productCode", getIntent().getSerializableExtra("productCode"));
                intent2.setClass(this, SellActivity.class);
                startActivityForResult(intent2, 1);
                break;
            case R.id.bank_deal:// TODO 18/10/23

                String interestByTimeRange = getIntent().getStringExtra("interestByTimeRange");

                //跳转SPV详情
                if ("1".equals(interestByTimeRange)){

                    Intent intent = new Intent();
                    intent.putExtra("productCode", getIntent().getStringExtra("productCode"));
                    intent.putExtra("id",getIntent().getStringExtra("id"));
                    intent.setClass(this, LookSpvProductDetailActivity.class);
                    baseStartActivity(intent);
                }

                else {

                    Intent intent = new Intent();
                    intent.putExtra("id", getIntent().getSerializableExtra("id"));
                    intent.putExtra("productCode", getIntent().getSerializableExtra("productCode"));
                    intent.setClass(this, HoldProductCodeActivity.class);
                    baseStartActivity(intent);
                }


                break;
            case R.id.ll_pay_interest:// TODO 18/10/23
                Intent intent1 = new Intent();
                intent1.putExtra("id", getIntent().getSerializableExtra("id"));
                intent1.setClass(this, PayInterestDetailsActivity.class);
                baseStartActivity(intent1);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
