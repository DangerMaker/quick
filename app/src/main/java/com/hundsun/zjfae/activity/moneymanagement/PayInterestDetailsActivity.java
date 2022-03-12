package com.hundsun.zjfae.activity.moneymanagement;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.adapter.PayInterestAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.PayInterestPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.PayInterestView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.PrdQueryInterestPayDetailsPb;

//付息明细
public class PayInterestDetailsActivity extends CommActivity implements PayInterestView {
    private RecyclerView mRvPayInterest;//列表控件
    private TextView mInterestTotal;
    private PayInterestPresenter mPresenter;
    private List<PrdQueryInterestPayDetailsPb.PBIFE_prdquery_prdQueryInterestPayDetails.ProductCashAddInterestPay> mList = new ArrayList<>();
    private PayInterestAdapter mAdapter;

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new PayInterestPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("付息明细");
        mRvPayInterest = findViewById(R.id.rv_pay_interest);
//        refreshLayout = findViewById(R.id.refreshLayout);
        mInterestTotal = (TextView) findViewById(R.id.total_interest);
//        refreshLayout.setOnRefreshListener(this);
//        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_pay_interest);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_interest_details;
    }

    @Override
    public void initData() {
        super.initData();
        if (!NetworkCheck.isNetworkAvailable(this)) {
            setNoNetViewVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefresh();
                }
            });
            return;
        }
        onRefresh();
    }


    public void onRefresh() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            return;
        } else {
            setNoNetViewGone();
        }
        mPresenter.loadData(getIntent().getStringExtra("id"));
    }

    @Override
    public void getData(String totalIncome, List<PrdQueryInterestPayDetailsPb.PBIFE_prdquery_prdQueryInterestPayDetails.ProductCashAddInterestPay> list) {
        mInterestTotal.setText(totalIncome);
        mList.clear();
        if (list == null || list.size() == 0) {
            setNoDataViewVisiable();
        } else {
            setNoDataViewGone();
            mList.addAll(list);
            mAdapter = new PayInterestAdapter(mList);
            mRvPayInterest.setLayoutManager(new LinearLayoutManager(this));
            mRvPayInterest.setAdapter(mAdapter);
        }
    }
}
