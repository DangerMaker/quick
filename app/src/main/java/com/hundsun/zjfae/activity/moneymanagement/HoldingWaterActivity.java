package com.hundsun.zjfae.activity.moneymanagement;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.adapter.HoldingWaterAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.HoldingWaterPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.HoldingWaterView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.view.popwindow.PopupWindowDate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.PrdQueryTaUnitFlowPb;

//资产流水
public class HoldingWaterActivity extends CommActivity implements OnRefreshListener, OnLoadMoreListener,HoldingWaterView {
    private HoldingWaterAdapter mHoldAdapter;//列表适配器
    private RecyclerView mRvHoldingWater;//列表控件
    private SmartRefreshLayout refreshLayout;//刷新控件
    private int page = 1;
    private String startDate = "", endDate = "";
    private PopupWindowDate popupWindowDate;
    private HoldingWaterPresenter mPresenter;
    private List<PrdQueryTaUnitFlowPb.PBIFE_prdquery_prdQueryTaUnitFlow.TaUnitFlow> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_holding_water;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter=new HoldingWaterPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_holding_water);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("资产流水");
        refreshLayout = findViewById(R.id.refreshLayout);
        mRvHoldingWater = findViewById(R.id.rv_holding_water);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        popupWindowDate = new PopupWindowDate(this, false, new PopupWindowDate.SearchItemClick() {
            @Override
            public void onSearchClick(String start_time, String end_time, String phoneNumber) {
                CCLog.e(start_time + "-" + end_time + "-" + phoneNumber);
                startDate = start_time;
                endDate = end_time;
                onRefresh(refreshLayout);
            }
        });
        findViewById(R.id.lin_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowDate.show(view);
            }
        });
        mHoldAdapter = new HoldingWaterAdapter(mList);
        mRvHoldingWater.setLayoutManager(new LinearLayoutManager(this));
        mRvHoldingWater.setAdapter(mHoldAdapter);
    }

    @Override
    public void initData() {
        super.initData();
        if (!NetworkCheck.isNetworkAvailable(this)) {
            setNoNetViewVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefresh(refreshLayout);
                }
            });
            return;
        }
        onRefresh(refreshLayout);
    }

    //下拉加载
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            refreshLayout.finishLoadMore();
            return;
        }
        page++;
        mPresenter.loadData(page + "", startDate, endDate);
    }

    //上拉刷新
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            refreshLayout.finishRefresh();
            return;
        } else {
            setNoNetViewGone();
        }
        page = 1;
        mPresenter.loadData(page + "", startDate, endDate);
    }

    @Override
    public void getData(List<PrdQueryTaUnitFlowPb.PBIFE_prdquery_prdQueryTaUnitFlow.TaUnitFlow> list) {
        if (page == 1) {
            mList.clear();
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                refreshLayout.setEnableLoadMore(false);
            } else {
                refreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
                mList.addAll(list);
            }
            mHoldAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                mList.addAll(list);
                mHoldAdapter.notifyDataSetChanged();
            }
            refreshLayout.finishLoadMore();
        }
    }
}
