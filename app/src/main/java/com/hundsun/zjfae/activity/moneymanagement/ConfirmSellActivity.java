package com.hundsun.zjfae.activity.moneymanagement;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.presenter.ConfirmSellPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.ConfirmSellView;
import com.hundsun.zjfae.activity.product.ProductPlayStateActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;

//确认转让
public class ConfirmSellActivity extends CommActivity<ConfirmSellPresenter> implements View.OnClickListener, ConfirmSellView {
    private TextView mTitle;
    private TextView mBenjin;
    private TextView mLilv;
    private TextView mFloatingProfit;
    private TextView mTransferIncome;
    private TextView mAmount;
    private TextView mExtraCost;
    private Button mSellBt;
    private PlayWindow play;
    private TextView tv_remakes;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_confirm_sell;
    }

    @Override
    public void initView() {
        setTitle("确认转让");
        mTitle = (TextView) findViewById(R.id.title);
        mBenjin = (TextView) findViewById(R.id.benjin);
        mLilv = (TextView) findViewById(R.id.lilv);
        mFloatingProfit = (TextView) findViewById(R.id.floatingProfit);
        mTransferIncome = (TextView) findViewById(R.id.transferIncome);
        mAmount = (TextView) findViewById(R.id.amount);
        mExtraCost = (TextView) findViewById(R.id.extraCost);
        mSellBt = (Button) findViewById(R.id.bt_sell);
        mSellBt.setOnClickListener(this);
        mTitle.setText(getIntent().getStringExtra("title"));
        Utils.setTextViewGravity(mTitle);
        tv_remakes = findViewById(R.id.tv_remakes);
        mBenjin.setText(getIntent().getStringExtra("benjin") + "元");
        mLilv.setText(getIntent().getStringExtra("lilv") + "%");
        mFloatingProfit.setText(getIntent().getStringExtra("floatingProfit") + "元");
        mTransferIncome.setText(getIntent().getStringExtra("transferIncome") + "元");
        mAmount.setText(getIntent().getStringExtra("amount") + "元");
        mExtraCost.setText(getIntent().getStringExtra("extraCost") + "元");

    }

    @Override
    protected void initData() {
        presenter.getDictionary();
    }

    @Override
    protected ConfirmSellPresenter createPresenter() {
        return new ConfirmSellPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_confirm_sell);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sell:
                play = new PlayWindow(ConfirmSellActivity.this);
                play.showAtLocation(findViewById(R.id.ll_confirm_sell), Gravity.BOTTOM, 0, 0);
                play.setPayListener(new PlayWindow.OnPayListener() {
                    @Override
                    public void onSurePay(String password) {
                        presenter.getDate(getIntent().getStringExtra("productCode"), getIntent().getStringExtra("benjin"), getIntent().getStringExtra("lilv"), password, getIntent().getStringExtra("id"));
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void CreateTransferOrder(String code, String msg) {
        if ("0000".equals(code)) {
            Intent intent = new Intent(ConfirmSellActivity.this, ProductPlayStateActivity.class);
            intent.putExtra("playState", "挂卖成功");
            baseStartActivity(intent);
            setResult(RESULT_OK);
            finish();
        } else {
            showDialog(msg);
        }
    }

    @Override
    public void onTransferRemarks(String remakes) {

        tv_remakes.setText(remakes);
    }
}
