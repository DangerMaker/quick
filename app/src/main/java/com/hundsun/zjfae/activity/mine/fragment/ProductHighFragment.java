package com.hundsun.zjfae.activity.mine.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.CardInfo;
import com.hundsun.zjfae.activity.mine.fragment.presenter.ProductHighPresenter;
import com.hundsun.zjfae.activity.mine.fragment.view.ProductHighView;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
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

public class ProductHighFragment extends BaseFragment  implements OnRefreshListener, OnLoadMoreListener , ProductHighView {

    private DropDownMenu product_dropDownMenu;
    private View product_contentView;
    private SmartRefreshLayout productLayout;
    private TextView no_date_tv;
    private RecyclerView product_view;

    private static  String headers[] = { "默认排序", "筛选", "分类"};


    private ProductHighAdapter productsAdapter;//适配器

    protected String quanDetailsId = "";
    protected String quanUsedProductCode = "";
    protected String quanUsedSeriesCode = "";
    private int pageIndex = 1;//默认初始化为第一页产品列表

    private String uuids = "";

    private boolean isLoadMore = false;//是否下拉加载


    private ProductDefaultSortAdapter defaultSortAdapter;
    private List<AllAzjProto.PBAPPSearchSortControl_l2> defaultSortList;

    private ProductDefaultFilterAdapter filterAdapter,sortAdapter;
    private List<AllAzjProto.PBAPPSearchSortControl_l2> defaultFilterList,sortList;
    private  List<View> productViewList = new ArrayList<>();

    private static final int requestCodes = 1030;
    private static final int RESULT_OK = 0;


    private ProductHighPresenter productHighPresenter;

    private  String default_conditions = "",conditions = "",grouping_conditions = "";//选择条件
    @Override
    protected BasePresenter createPresenter() {
        return productHighPresenter = new ProductHighPresenter(this);
    }


    @Override
    public void initData() {
        if (productHighPresenter != null){
            pageIndex = 1;
            initProduct();
        }

    }

    /**
     *设置卡券id
     * */
    public void setCardInfo(CardInfo cardInfo){
        quanDetailsId = cardInfo.getQuanDetailsId();
        quanUsedProductCode = cardInfo.getQuanUsedProductCode();
        quanUsedSeriesCode = cardInfo.getQuanUsedSeriesCode();
    }

    @Override
    protected void resetLayout() {

    }


    private void initProduct(){
        productHighPresenter.initProduct(uuids,quanDetailsId,quanUsedProductCode,quanUsedSeriesCode,pageIndex);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_product_high_layout;
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

    //    //购买列表
    private List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList>
            productList = null;//数据装载
    /*
     * 产品列表数据初始化
     * */
    private void initProductList(final List<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList> productFinanceDetailList){

        if (productList == null) {
            productList = new ArrayList<>();
        }
        if (isLoadMore && productFinanceDetailList.isEmpty()){
            //下拉加载无更多数据
            productLayout.finishLoadMoreWithNoMoreData();
            showToast("暂无更多产品");
        }
        else if (!isLoadMore && productFinanceDetailList.isEmpty()){
            //刷新无数据
            productLayout.setVisibility(View.GONE);
            StringBuffer buffer = new StringBuffer("当前的筛选条件");
            buffer.append("\""+product_dropDownMenu.getTabText(0)+"\"").append("、");
            buffer.append("\""+product_dropDownMenu.getTabText(1)+"\"").append("、");
            buffer.append("\""+product_dropDownMenu.getTabText(2)+"\"");
            buffer.append("下没有产品，请清除筛选条件下搜索产品试试");
            no_date_tv.setText(buffer.toString());
        }
        else {
            productLayout.setVisibility(View.VISIBLE);
        }

        if (!isLoadMore && productList != null&&!productList.isEmpty()){
            productList.clear();
        }
        productList.addAll(productFinanceDetailList);

        if (isLoadMore){
            productsAdapter.rest(productList);
        }
        else {
            productsAdapter =  new ProductHighAdapter(mActivity,productList);
            product_view.setAdapter(productsAdapter);

            productsAdapter.setOnItemClickListener(new OnItemClickListener<ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList>() {
                @Override
                public void onItemClickListener(View v, ProductHighTransferOrderList.PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.ProductTradeInfoList productTradeInfoList, int position) {



                    if (productTradeInfoList.getProductSpecialArea().equals("00")){
                        //转让列表
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
                        startActivityForResult(intent,requestCodes);

                    }
                    else if (productTradeInfoList.getProductSpecialArea().equals("02")){
                        Intent intent = new Intent();
                        intent.putExtra("productCode", productTradeInfoList.getProductCode());
                        intent.putExtra("sellingStatus",productTradeInfoList.getSellingStatus());
                        intent.setClass(getContext(), ProductCodeActivity.class);
                        startActivityForResult(intent,requestCodes);
                    }


                }
            });

        }

        reset(isLoadMore);
    }


    private void reset( boolean isLoadMore){
        if (isLoadMore){
            productLayout.finishLoadMore();
        }
        else {
            productLayout.finishRefresh();
        }
        this.isLoadMore = false;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CCLog.e("111requestCodes",requestCodes);
        CCLog.e("1111resultCode",resultCode);
        if (requestCodes == requestCode && resultCode == RESULT_OK){
            this.pageIndex = 1;
            productHighPresenter.initProduct(uuids,quanDetailsId,quanUsedProductCode,quanUsedSeriesCode,pageIndex);
        }
    }


    @Override
    protected void initWidget() {
        product_dropDownMenu = (DropDownMenu) findViewById(R.id.product_high_dropDownMenu);
        product_contentView = mActivity.getLayoutInflater().inflate(R.layout.product_content_layout,null);
        productLayout =  product_contentView.findViewById(R.id.smartLayout);
        no_date_tv = product_contentView.findViewById(R.id.no_date_tv);
        productLayout.setOnRefreshListener(this);
        productLayout.setOnLoadMoreListener(this);
        product_view = product_contentView.findViewById(R.id.recyclerView);
        product_view.setLayoutManager(new LinearLayoutManager(mActivity));


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
                product_dropDownMenu.setTabText(0,defaultSortList.get(position).getControlName());
                product_dropDownMenu.closeMenu();
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
                product_dropDownMenu.setTabText( 1,defaultFilterList.get(position).getControlHName());
                product_dropDownMenu.closeMenu();
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
                product_dropDownMenu.setTabText(2,sortList.get(position).getControlHName());
                product_dropDownMenu.closeMenu();
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
        product_dropDownMenu.setDropDownMenu(Arrays.asList(headers), productViewList, product_contentView);
        product_dropDownMenu.closeMenu();
    }


    private RecyclerView getRecyclerView(Context context){
        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return recyclerView;
    }

}
