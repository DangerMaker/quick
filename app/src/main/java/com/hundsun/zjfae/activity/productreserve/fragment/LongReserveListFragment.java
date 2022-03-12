package com.hundsun.zjfae.activity.productreserve.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity;
import com.hundsun.zjfae.activity.productreserve.LongorShortReserveDetailActivity;
import com.hundsun.zjfae.activity.productreserve.adapter.LongReserveAdapter;
import com.hundsun.zjfae.activity.productreserve.presenter.ReserveListPresenter;
import com.hundsun.zjfae.activity.productreserve.view.ReserveListView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.ProductOrderInfoPB;

/**
 * @Description:长期预约界面
 * @Author: zhoujianyu
 * @Time: 2018/9/17 13:45
 */
public class LongReserveListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, ReserveListView {
    private RecyclerView recyclerView;
    private SmartRefreshLayout mRefreshLayout;
    private LongReserveAdapter adapter;
    private List<ProductOrderInfoPB.PBIFE_trade_queryProductOrderInfo.TaProductOrderInfoWrapList> mList = new ArrayList<>();
    private ReserveListPresenter mPresenter;
    private int page = 1;

    @Override
    protected void initWidget(View root) {
        recyclerView = root.findViewById(R.id.recyclerView);
        mRefreshLayout = root.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new ReserveListPresenter(this);
    }

    @Override
    protected void resetLayout() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_long_reserve_list;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!NetworkCheck.isNetworkAvailable(getActivity())) {
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


    private void onRefreshData() {
        if (!NetworkCheck.isNetworkAvailable(getActivity())) {
            showToast("网络异常");
            mRefreshLayout.finishRefresh();
            return;
        } else {
            setNoNetViewGone();
        }
        page = 1;
        mPresenter.getReserveListData(page, "01");
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onRefreshData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(getActivity())) {
            showToast("网络异常");
            mRefreshLayout.finishLoadMore();
            return;
        }
        page++;
        mPresenter.getReserveListData(page, "01");
    }


    private String orderProductCode = "";
    @Override
    public void loadData(List<ProductOrderInfoPB.PBIFE_trade_queryProductOrderInfo.TaProductOrderInfoWrapList> list) {
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
                mList.clear();
                mList = list;
                adapter = new LongReserveAdapter(getActivity(), mList, "01", new LongReserveAdapter.onItemClicker() {
                    @Override
                    public void OnItemClick(String orderProductCode) {
                        mPresenter.getReserveListValidate(orderProductCode);


                    }
                });
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
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

    @Override
    public void orderValidate(String returnCode, String returnMsg, String orderProductCode) {
        Intent intent = new Intent(mContext, LongorShortReserveDetailActivity.class);
        intent.putExtra("id", orderProductCode);
        intent.putExtra("type", "01");
        mContext.startActivity(intent);
    }

    @Override
    public void onRiskAssessment(String returnMsg) {
        showDialog(returnMsg, "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //去风评
                dialog.dismiss();
                baseStartActivity(mActivity, RiskAssessmentActivity.class);
            }
        });
    }
}
