package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.presenter.EntrustDetailPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.EntrustDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;

public class EntrustDetailActivity extends CommActivity implements EntrustDetailView {

    private TextView mTitle;
    private TextView mType;
    private TextView mState;
    private TextView mRateTransfer;
    private TextView mPrincipal;
    private TextView mPrincipalBusiness;
    private TextView mYieldAnnualized;
    private TextView mAmountTransaction;
    private TextView mTimeEntrustment;
    private TextView tv_mRateTransfer;
    private Button mCancelOrderBt;
    private EntrustDetailPresenter mPresenter;
    private Boolean refreshType = false;//当前页面已经撤销 用于返回回去刷新列表

    @Override
    protected int getLayoutId() {
        return R.layout.activity_entrust_detail;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_entrust_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new EntrustDetailPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("委托详情");
        mTitle = (TextView) findViewById(R.id.title);
        mType = (TextView) findViewById(R.id.type);
        mState = (TextView) findViewById(R.id.state);
        mRateTransfer = (TextView) findViewById(R.id.transfer_rate);
        tv_mRateTransfer = (TextView) findViewById(R.id.tv_transfer_rate);
        mPrincipal = (TextView) findViewById(R.id.principal);
        mPrincipalBusiness = (TextView) findViewById(R.id.business_principal);
        mYieldAnnualized = (TextView) findViewById(R.id.annualized_yield);
        mAmountTransaction = (TextView) findViewById(R.id.transaction_amount);
        mTimeEntrustment = (TextView) findViewById(R.id.entrustment_time);
        mCancelOrderBt = (Button) findViewById(R.id.bt_cancel_order);
        mCancelOrderBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog("您确定撤单该产品吗？", "确认", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.cancel(getIntent().getStringExtra("delegationCode"));
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        mTitle.setText(getIntent().getStringExtra("title"));
        Utils.setTextViewGravity(mTitle);
        mType.setText(getIntent().getStringExtra("type"));
        mState.setText(getIntent().getStringExtra("state"));
        mPrincipal.setText(getIntent().getStringExtra("delegateNum"));
        mPrincipalBusiness.setText(getIntent().getStringExtra("transactionNum"));
        mYieldAnnualized.setText(getIntent().getStringExtra("expectedMaxAnnualRate"));
        mAmountTransaction.setText(getIntent().getStringExtra("TransactionAmount"));
        mTimeEntrustment.setText(getIntent().getStringExtra("DelegateTime"));
        if (getIntent().getStringExtra("transferIsfloat").equals("unfloat")) {
            tv_mRateTransfer.setText("转让折扣");
            mRateTransfer.setText(getIntent().getStringExtra("discountRate"));
        } else {
            tv_mRateTransfer.setText("转让利率");
            mRateTransfer.setText(getIntent().getStringExtra("transferRate"));
        }
        if (getIntent().getStringExtra("delegateStatus").equals("DELEGATEING")) {
            mCancelOrderBt.setVisibility(View.VISIBLE);
        } else {
            mCancelOrderBt.setVisibility(View.GONE);
        }
    }


    @Override
    public void cancelEntrust(String code, String msg) {
        if ("0000".equals(code)) {
            refreshType = true;
            showDialog(msg, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        } else {
            showDialog(msg);
        }
    }

    @Override
    public void finish() {
        if (refreshType) {
            setResult(RESULT_OK);
        }
        super.finish();
    }
}
