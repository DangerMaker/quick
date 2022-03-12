package com.hundsun.zjfae.activity.mine;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.BankCardManageAdapter;
import com.hundsun.zjfae.activity.mine.presenter.BankCardDynamicPresenter;
import com.hundsun.zjfae.activity.mine.view.BankCardDynamicView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.LoadTcBindingBankInfo;

public class BankCardDynamicActivity extends CommActivity<BankCardDynamicPresenter> implements BankCardDynamicView, OnRefreshListener,OnLoadMoreListener {

    private BankCardManageAdapter adapter;
    private RecyclerView bank_card_RecyclerView;
    private SmartRefreshLayout refreshLayout;//刷新控件
    private int pageIndex = 1;
    private boolean isLoadMore = false;//是否下拉加载
    private  List<LoadTcBindingBankInfo.PBIFE_bankcardmanage_loadTcBindingBankInfo.TcBindingBankInfoList> tcBindingBankInfoList;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_card_dynamic;
    }

    @Override
    protected BankCardDynamicPresenter createPresenter() {
        return  new BankCardDynamicPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("银行卡流水");
        tcBindingBankInfoList = new ArrayList<>();
        bank_card_RecyclerView = findViewById(R.id.bank_card_RecyclerView);
        refreshLayout =findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initData() {
        loadTcBindingBank(pageIndex);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.bank_card_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }
    @Override
    public void onBankCardManageBean( LoadTcBindingBankInfo.Ret_PBIFE_bankcardmanage_loadTcBindingBankInfo loadTcBindingBankInfo) {
        reset();
        List<LoadTcBindingBankInfo.PBIFE_bankcardmanage_loadTcBindingBankInfo.TcBindingBankInfoList> tcBindingBankInfoList = loadTcBindingBankInfo.getData().getTcBindingBankInfoListList();
        if (isLoadMore && tcBindingBankInfoList == null){
            refreshLayout.finishLoadMoreWithNoMoreData();
            showToast("暂无更多产品");
            return;
        }
        else {
            if (tcBindingBankInfoList == null){
                refreshLayout.setVisibility(View.GONE);
                showToast("暂无银行流水信息");
            }
            else {
                refreshLayout.setVisibility(View.VISIBLE);
            }
        }
        this.tcBindingBankInfoList.addAll(tcBindingBankInfoList);
        if (isLoadMore){
            adapter.notifyDataSetChanged();
        }
        else {
            adapter = new BankCardManageAdapter(this,tcBindingBankInfoList);
            bank_card_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
            bank_card_RecyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = true;
        pageIndex = pageIndex+1;
        loadTcBindingBank(pageIndex);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = false;
        pageIndex = 1;
        refreshLayout.setNoMoreData(false);
        loadTcBindingBank(pageIndex);
    }

    public void reset(){
        if (isLoadMore){
            refreshLayout.finishLoadMore();
        }
        else {
            if (tcBindingBankInfoList != null && !tcBindingBankInfoList.isEmpty()){
                tcBindingBankInfoList.clear();
            }
            refreshLayout.finishRefresh();
        }
    }

    private void loadTcBindingBank(int pageIndex){
        presenter.loadTcBindingBank(pageIndex);
    }
}
