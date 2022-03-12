package com.hundsun.zjfae.fragment.account;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.fragment.account.adapter.TotalIncomeAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.QueryFundEarningsLog;
import onight.zjfae.afront.gens.UserAssetsInfo;

/**
 * @Description:总收益
 * @Author: zhoujianyu
 * @Time: 2018/9/17 16:46
 */
public class TotalIncomeActivity extends CommActivity implements OnRefreshListener, OnLoadMoreListener, TotalIncomeView {
    private RecyclerView mRecyclerView;//列表控件
    private SmartRefreshLayout mRefreshLayout;//刷新控件
    private TotalIncomeAdapter adapter;
    private List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> mList = new ArrayList<>();
    private TotalIncomePresenter mPresenter;
    private int page = 1;
    private TextView tv_allProfit, tv_maturityProfitSum;

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_total_income);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new TotalIncomePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_total_income;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("总收益");
        tv_allProfit = findViewById(R.id.tv_allProfit);
        tv_maturityProfitSum = findViewById(R.id.tv_maturityProfitSum);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        if (!NetworkCheck.isNetworkAvailable(this)) {
            setNoNetViewVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPresenter.getData();
                    onRefreshData();
                }
            });
            return;
        }
        mPresenter.getData();
        onRefreshData();
    }

    //上拉刷新
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefreshData();
    }

    private void onRefreshData() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            mRefreshLayout.finishRefresh();
            return;
        } else {
            setNoNetViewGone();
        }
        page = 1;
        mPresenter.getListData(page);
    }

    //下拉加载
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            mRefreshLayout.finishLoadMore();
            return;
        }
        page++;
        mPresenter.getListData(page);
    }


    @Override
    public void loadData(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo data) {
        tv_allProfit.setText(data.getData().getAllProfit());
        tv_maturityProfitSum.setText(data.getData().getMaturityProfitSum());
    }

    @Override
    public void loadListData(List<QueryFundEarningsLog.PBIFE_statistic_queryFundEarningsLog.FundEarningsLogList> list) {
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
                mList.clear();
                mList = list;
                adapter = new TotalIncomeAdapter(this, mList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.addItemDecoration(new DividerItemDecorations());
            }
            mRefreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mList.addAll(list);
                adapter.notifyDataSetChanged();
            }
            mRefreshLayout.finishLoadMore();
        }
    }
}
