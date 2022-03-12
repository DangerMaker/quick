package com.hundsun.zjfae.activity.productreserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.productreserve.adapter.MyReserveAdapter;
import com.hundsun.zjfae.activity.productreserve.presenter.MyReservePresenter;
import com.hundsun.zjfae.activity.productreserve.view.MyReserveView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.PBIFETradeQueryMyOrderList;

/**
 * @Description:我的个性化预约
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:58
 */
public class MyReserveActivity extends CommActivity implements MyReserveAdapter.OnItemClickListener, MyReserveAdapter.OnCancleReserveListener, OnRefreshListener, OnLoadMoreListener, MyReserveView {
    private RecyclerView mRecyclerView;//列表控件
    private SmartRefreshLayout mRefreshLayout;//刷新控件
    private MyReserveAdapter adapter;
    private List<PBIFETradeQueryMyOrderList.PBIFE_trade_queryMyOrderList.TaProductOrderDetailWrapList> mList = new ArrayList<>();
    private MyReservePresenter mPresenter;
    private int page = 1;

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_my_reserve);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MyReservePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_reserve;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("我的个性化预约");
        mRecyclerView = findViewById(R.id.recyclerView);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setEnableRefresh(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
//
//    @Override
//    public void initData() {
//        super.initData();
//    }

    @Override
    public void onItemClick(int position) {
        //点击进入详情
        if (mList.get(position).getOrderType().equals("4")) {
            Intent intent = new Intent(this, SpvReserveProductDetailActivity.class);
            intent.putExtra("productCode", mList.get(position).getOrderProductCode());
            intent.putExtra("ifAllBuy", mList.get(position).getIfAllBuy());
            intent.putExtra("delegationCode",mList.get(position).getTeaoId());
            intent.putExtra("delegationId",mList.get(position).getTeaoId());
            intent.putExtra("isHideButton",true);
            baseStartActivity(intent);
        } else {
            Intent intent = new Intent(this, MyReserveDetailActivity.class);
            intent.putExtra("id", mList.get(position).getOrderProductCode());
            intent.putExtra("orderType", mList.get(position).getOrderType());
            baseStartActivity(intent);
        }
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
        mPresenter.getReserveListData(page);
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
        mPresenter.getReserveListData(page);
    }

    //数据请求完成回调
    @Override
    public void loadData(List<PBIFETradeQueryMyOrderList.PBIFE_trade_queryMyOrderList.TaProductOrderDetailWrapList> list) {
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
                mList.clear();
                mList = list;
                adapter = new MyReserveAdapter(this, mList);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(this);
                adapter.setOnCancleReserveListener(this);
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
    public void cancleOrderRefreshPre(final String orderNum, String code, String message, String datamessage) {
        if (code != null && code.equals("0000")) {
            showDialog(datamessage, "确认", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.cancelOrder(orderNum);
                    dialogInterface.dismiss();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                }
            });
        } else {
            showDialog(message);
        }
    }

    @Override
    public void cancleOrderRefresh(String code, String message,String datamessage) {
        showDialog(datamessage, "确认");
        if (code != null && code.equals("0000")) {
            onRefreshData();
        }
    }

    @Override
    public void cancleReserve(final String orderNum) {
        if (!NetworkCheck.isNetworkAvailable(MyReserveActivity.this)) {
            showToast("网络异常");
            return;
        } else {
            mPresenter.cancelOrderPre(orderNum);
        }
    }
}
