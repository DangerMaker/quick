package com.hundsun.zjfae.activity.assetstream;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.assetstream.adapter.AssetStreamAdapter;
import com.hundsun.zjfae.activity.assetstream.presenter.AssetStreamPresenter;
import com.hundsun.zjfae.activity.assetstream.view.AssetStreamView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.view.popwindow.PopupWindowDate;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.FundAccountLogPB;

/**
 * @Description:资金流水
 * @Author: zhoujianyu
 * @Time: 2018/9/18 9:35
 */
public class AssetStreamActivity extends CommActivity<AssetStreamPresenter> implements OnRefreshListener, OnLoadMoreListener, AssetStreamView {
    private RecyclerView mRecyclerView;//列表控件
    private SmartRefreshLayout mRefreshLayout;//刷新控件
    private AssetStreamAdapter adapter;
    private List<FundAccountLogPB.PBIFE_fund_queryFundAccountLog.FundAccountLogList> mList = new ArrayList<>();
    private PopupWindowDate popupWindowDate;
    private String startDate = "", endDate = "";
    private int page = 1;
    private String id = "";

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_asset_stream);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected AssetStreamPresenter createPresenter() {
        return new AssetStreamPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_asset_stream;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("资金流水");
        popupWindowDate = new PopupWindowDate(this, false, new PopupWindowDate.SearchItemClick() {
            @Override
            public void onSearchClick(String start_time, String end_time, String phoneNumber) {
                startDate = start_time;
                endDate = end_time;
                onRefreshData();
            }
        });
        findViewById(R.id.lin_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowDate.show(view);
            }
        });
        mRecyclerView = findViewById(R.id.recyclerView);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        if (StringUtils.isNotBlank(getIntent().getStringExtra("id"))) {
            id = getIntent().getStringExtra("id");
        }
    }

    @Override
    public void initData() {
        super.initData();
        if (!NetworkCheck.isNetworkAvailable(this)) {
            setNoNetViewVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefreshData();
                }
            });
            return;
        }
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
        presenter.getReserveListData(1, startDate, endDate,id);
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
        presenter.getReserveListData(page, startDate, endDate,id);
    }

    @Override
    public void loadData(List<FundAccountLogPB.PBIFE_fund_queryFundAccountLog.FundAccountLogList> list) {
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
                mList.clear();
                mList.addAll(list);
                adapter = new AssetStreamAdapter(this, mList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(adapter);
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
