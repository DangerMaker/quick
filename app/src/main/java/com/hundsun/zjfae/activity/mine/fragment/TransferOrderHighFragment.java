package com.hundsun.zjfae.activity.mine.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.fragment.presenter.TransferOrderHighPresenter;
import com.hundsun.zjfae.activity.mine.fragment.view.TransferOrderHighView;
import com.hundsun.zjfae.activity.product.TransferDetailActivity;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.common.adapter.OnItemClickListener;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.DropDownMenu;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.hundsun.zjfae.fragment.finance.adapter.ProductDefaultFilterAdapter;
import com.hundsun.zjfae.fragment.finance.adapter.ProductDefaultSortAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.ProductHighTransferOrderList;

public class TransferOrderHighFragment extends BaseFragment implements TransferOrderHighView , OnRefreshListener, OnLoadMoreListener  {


    private TransferOrderHighPresenter presenter;

    private DropDownMenu transfer_dropDownMenu;


    private SmartRefreshLayout transferLayout;

    private View  transfer_contentView;
    private ImageView transfer_no;

    private RecyclerView transfer_view;


    private int pageIndex = 1;//默认初始化为第一页产品列表

    private String uuids = "";

    private boolean isLoadMore = false;//是否下拉加载

    private TransferOrderHighAdapter transferOrderHighAdapter;

    private static  String headers[] = { "默认排序", "筛选", "分类"};

    private ProductDefaultSortAdapter defaultSortAdapter;
    private List<AllAzjProto.PBAPPSearchSortControl_l2> defaultSortList;

    private ProductDefaultFilterAdapter filterAdapter,sortAdapter;
    private List<AllAzjProto.PBAPPSearchSortControl_l2> defaultFilterList,sortList;
    private  List<View> productViewList = new ArrayList<>();


    private  String default_conditions = "",conditions = "",grouping_conditions = "";//选择条件

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new TransferOrderHighPresenter(this);
    }

    private void initProduct(){
        CCLog.e(uuids);
        CCLog.e(pageIndex);
        presenter.initProduct(uuids,pageIndex);
    }


    @Override
    public void initProduct(ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList orderList, AllAzjProto.PEARetControl productControl) {
        initProductDownMenu(productControl.getControlListList());
        initProductList(orderList.getData().getProductTradeInfoListList());
    }




    //设置状态栏
    private void initProductDownMenu(List<AllAzjProto.PBAPPSearchSortControl_l1> control_list){

        if (defaultSortList == null){
            defaultSortList = new ArrayList<>();
        }
        else {
            defaultSortList.clear();
        }

        if (defaultFilterList == null){
            defaultFilterList = new ArrayList<>();
        }
        else {
            defaultFilterList.clear();
        }
        if (sortList == null){
            sortList = new ArrayList<>();
        }
        else {
            sortList.clear();
        }
        defaultSortList.addAll(control_list.get(0).getControlsList());
        defaultFilterList.addAll(control_list.get(1).getControlsList());
        sortList.addAll(control_list.get(2).getControlsList());
        defaultSortAdapter.refresh(defaultSortList);
        filterAdapter.refresh(defaultFilterList);
        sortAdapter.refresh(sortList);

    }


    //转让列表
    private List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList>
            productList = null;//数据装载

    /*
     * 产品列表数据初始化
     * */
    private void initProductList(final List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList> productFinanceDetailList) {

        if (productList == null) {
            productList = new ArrayList<>();
        }

        if (isLoadMore && productFinanceDetailList.isEmpty()){
            //下拉加载无更多数据
            transferLayout.finishLoadMoreWithNoMoreData();
            showToast("暂无更多产品");
        }
        else if (!isLoadMore && productFinanceDetailList.isEmpty()){
            transferLayout.setVisibility(View.GONE);
            transfer_no.setVisibility(View.VISIBLE);
        }
        else {
            transfer_no.setVisibility(View.GONE);
            transferLayout.setVisibility(View.VISIBLE);
        }

        if (!isLoadMore && productList != null&&!productList.isEmpty()){
            productList.clear();
        }
        productList.addAll(productFinanceDetailList);

        if (isLoadMore){
            transferOrderHighAdapter.rest(productList);
        }
        else {
            transferOrderHighAdapter = new TransferOrderHighAdapter(mActivity,productList);
            transfer_view.setAdapter(transferOrderHighAdapter);
            transferOrderHighAdapter.setOnItemClickListener(new OnItemClickListener<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList>() {
                @Override
                public void onItemClickListener(View v, ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList productTradeInfoList, int position) {
                    Intent intent = new Intent(mActivity, TransferDetailActivity.class);
                    Bundle bundle = new Bundle();
                    TransferDetailPlay playBean = new TransferDetailPlay();
                    playBean.setProductCode(productTradeInfoList.getProductCode());
                    playBean.setNextPayDate(productTradeInfoList.getNextPayDate());
                    playBean.setCanBuyNum(productTradeInfoList.getCanBuyNum());
                    playBean.setHoldDayNum(productTradeInfoList.getHoldDayNum());
                    playBean.setDelegateNum(productTradeInfoList.getDelegateNum());
                    playBean.setTargetRate(productTradeInfoList.getTargetRate());
                    playBean.setLeftDays(productTradeInfoList.getLeftDays());
                    playBean.setDelegationCode(productTradeInfoList.getDelegationCode());
                    playBean.setLeastTranAmount(productTradeInfoList.getLeastTranAmount());
                    playBean.setActualRate(productTradeInfoList.getActualRate());
                    playBean.setIfAllBuy(productTradeInfoList.getIfAllBuy());
                    if (UserInfoSharePre.getTradeAccount().equals( productTradeInfoList.getTradeAccount())){
                        playBean.setMyEntry(true);
                    }
                    else {
                        playBean.setMyEntry(false);
                    }
                    bundle.putParcelable("playInfo",playBean);
                    intent.putExtra("playBundle",bundle);
                    baseStartActivity(intent);
                }
            });
        }

        reset(isLoadMore);
    }



    private void reset( boolean isLoadMore){
        if (isLoadMore){
            transferLayout.finishLoadMore();
        }
        else {
            transferLayout.finishRefresh();
        }
        this.isLoadMore = false;
    }
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = true;
        pageIndex = pageIndex+1;
        initProduct();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = false;
        pageIndex = 1;
        refreshLayout.setNoMoreData(false);
        initProduct();
    }

    @Override
    public void initData() {

        if (presenter != null){
            pageIndex = 1;
            initProduct();
        }

    }

    @Override
    protected void initWidget() {
        transfer_dropDownMenu = (DropDownMenu) findViewById(R.id.product_high_dropDownMenu);
        transfer_contentView = mActivity.getLayoutInflater().inflate(R.layout.fragment_high_transfer_content_layout,null);
        transferLayout = transfer_contentView.findViewById(R.id.smartLayout);
        transferLayout.setOnRefreshListener(this);
        transferLayout.setOnLoadMoreListener(this);
        transfer_no =  transfer_contentView.findViewById(R.id.transfer_no);
        transfer_view =  transfer_contentView.findViewById(R.id.recyclerView);
        transfer_view.setLayoutManager(new LinearLayoutManager(mActivity));

        //默认排序
        RecyclerView defaultSort = getRecyclerView(mActivity);
        //默认筛选
        RecyclerView defaultFilter = getRecyclerView(mActivity);
        //分类
        RecyclerView sort = getRecyclerView(mActivity);


        defaultSortAdapter = new ProductDefaultSortAdapter(mActivity);
        defaultSortAdapter.setItemOnClick(new ProductDefaultSortAdapter.ItemOnClick() {
            @Override
            public void onItemClick(int position) {
                default_conditions = defaultSortList.get(position).getUuid();
                defaultSortAdapter.setCheckItem(position);
                transfer_dropDownMenu.setTabText(0,defaultSortList.get(position).getControlName());
                transfer_dropDownMenu.closeMenu();
                uuids = default_conditions+"-"+conditions+"-"+grouping_conditions;
                pageIndex = 1;
                initProduct();
            }
            @Override
            public void setKeyWordName(String keyWordName) {

            }
        });
        defaultSort.setAdapter(defaultSortAdapter);

        filterAdapter = new ProductDefaultFilterAdapter(mActivity);
        filterAdapter.setItemOnClick(new ProductDefaultFilterAdapter.ItemOnClick() {
            @Override
            public void onItemClick(int position) {
                conditions = defaultFilterList.get(position).getUuid();
                filterAdapter.setCheckItem(position);
                transfer_dropDownMenu.setTabText( 1,defaultFilterList.get(position).getControlHName());
                transfer_dropDownMenu.closeMenu();
                uuids = default_conditions+"-"+conditions+"-"+grouping_conditions;
                pageIndex = 1;
                initProduct();
            }

            @Override
            public void setKeyWordName(String keyWordName) {

            }
        });
        defaultFilter.setAdapter(filterAdapter);

        sortAdapter = new ProductDefaultFilterAdapter(mActivity);
        sortAdapter.setItemOnClick(new ProductDefaultFilterAdapter.ItemOnClick() {
            @Override
            public void onItemClick(int position) {
                sortAdapter.setCheckItem(position);
                grouping_conditions = sortList.get(position).getUuid();
                transfer_dropDownMenu.setTabText(2,sortList.get(position).getControlHName());
                transfer_dropDownMenu.closeMenu();
                uuids = default_conditions+"-"+conditions+"-"+grouping_conditions;
                pageIndex = 1;
                initProduct();
            }
            @Override
            public void setKeyWordName(String keyWordName) {

            }
        });
        sort.setAdapter(sortAdapter);


        productViewList.add(defaultSort);
        productViewList.add(defaultFilter);
        productViewList.add(sort);
        transfer_dropDownMenu.setDropDownMenu(Arrays.asList(headers), productViewList, transfer_contentView);
        transfer_dropDownMenu.closeMenu();
    }

    @Override
    protected void resetLayout() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_high_layout;
    }

    private RecyclerView getRecyclerView(Context context){
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return recyclerView;
    }
}
