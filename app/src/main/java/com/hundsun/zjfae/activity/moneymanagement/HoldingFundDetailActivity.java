package com.hundsun.zjfae.activity.moneymanagement;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.presenter.HoldingFundDetailPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.HoldingFundDetailView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.Utils;

import onight.zjfae.afront.gens.PrdQueryTaUnitFundFinanceById;

/**
 * @Description:基金资产详情
 * @Author: zhoujianyu
 * @Time: 2019/2/22 9:37
 */
public class HoldingFundDetailActivity extends CommActivity<HoldingFundDetailPresenter> implements HoldingFundDetailView {
    private TextView tv_title, tv_type, tv_manageperson, tv_mount, tv_createday, tv_manageEndDate, tv_isTransfer, tv_content;
    private HoldingFundDetailPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_holding_fund_detail;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_holding_fund_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("基金资产详情");
        tv_title = findViewById(R.id.tv_title);
        tv_type = findViewById(R.id.tv_type);
        tv_manageperson = findViewById(R.id.tv_manageperson);
        tv_mount = findViewById(R.id.tv_mount);
        tv_createday = findViewById(R.id.tv_createday);
        tv_manageEndDate = findViewById(R.id.tv_manageEndDate);
        tv_isTransfer = findViewById(R.id.tv_isTransfer);
        tv_content = findViewById(R.id.tv_content);
    }

    @Override
    public void initData() {
        mPresenter.loadData(getIntent().getStringExtra("id"));
    }

    @Override
    protected HoldingFundDetailPresenter createPresenter() {
        return mPresenter = new HoldingFundDetailPresenter(this);
    }

    @Override
    public void getData(PrdQueryTaUnitFundFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFundFinanceById data) {
        if (data.getReturnCode().equals("0000")) {
            setData(data);
        } else {
            showDialog(data.getReturnMsg());
        }
    }

    //填充数据
    private void setData(PrdQueryTaUnitFundFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFundFinanceById data) {
        PrdQueryTaUnitFundFinanceById.PBIFE_prdquery_prdQueryTaUnitFundFinanceById.TaUnitFundFinance taUnitFundFinance = data.getData().getTaUnitAndProductInfoObject();
        tv_title.setText(taUnitFundFinance.getProductName());
        Utils.setTextViewGravity(tv_title);
        tv_type.setText(data.getData().getSubjectTypeStr());
        tv_manageperson.setText(taUnitFundFinance.getManagerShortname());
        tv_mount.setText(taUnitFundFinance.getUnit());
        tv_createday.setText(taUnitFundFinance.getEstablishDate());
        tv_manageEndDate.setText(taUnitFundFinance.getDueDate());
        if (taUnitFundFinance.getIsTransfer().equals("transfer")) {
            tv_isTransfer.setText("可转让");
        } else {
            tv_isTransfer.setText("不可转让");
        }
        tv_content.setText(taUnitFundFinance.getMeritPay());
    }
}
