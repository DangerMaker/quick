package com.hundsun.zjfae.fragment.account;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;

import java.util.List;

import onight.zjfae.afront.gens.QueryFundEarningsLog;
import onight.zjfae.afront.gens.UserAssetsInfo;

/**
 * @Description:资产明细
 * @Author: zhoujianyu
 * @Time: 2018/9/18 9:13
 */
public class AssetDetailActivity extends CommActivity<TotalIncomePresenter> implements TotalIncomeView {

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_asset_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected TotalIncomePresenter createPresenter() {
        return new TotalIncomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_detail;
    }

    @Override
    public void initView() {
        setTitle("资产明细");

    }

    @Override
    protected void initData() {
        presenter.getData();
    }

    @Override
    public void loadData(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo data) {
        if (data.getData().getBalanceY() != null) {
            ((TextView) findViewById(R.id.tv_balanceY)).setText(data.getData().getBalanceY());
        }
        if (data.getData().getBalanceQ() != null) {
            ((TextView) findViewById(R.id.tv_balanceQ)).setText(data.getData().getBalanceQ());
        }
        if (data.getData().getAllUnit() != null) {
            ((TextView) findViewById(R.id.tv_allUnits)).setText(data.getData().getAllUnit());
        }
        if (data.getData().getFreezeTotal() != null) {
            ((TextView) findViewById(R.id.tv_freezeTotal)).setText(data.getData().getFreezeTotal());
        }
    }

    @Override
    public void loadListData(List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> list) {

    }
}
