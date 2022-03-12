package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.DiscountCouponAdapter;
import com.hundsun.zjfae.activity.mine.bean.CardInfo;
import com.hundsun.zjfae.activity.mine.presenter.EnvelopePresenter;
import com.hundsun.zjfae.activity.mine.view.EnvelopeView;
import com.hundsun.zjfae.common.adapter.OnItemClickListener;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.MyDiscount;
import onight.zjfae.afront.gens.Withdrawals;

public class DiscountCouponActivity extends CommActivity implements View.OnClickListener,EnvelopeView,OnRefreshListener,OnLoadMoreListener {


    private TextView usable, lose_efficacy,usage;
    private RecyclerView discount_coupon_recycler;
    private EnvelopePresenter presenter;
    private SmartRefreshLayout refreshLayout;//刷新控件
    private DiscountCouponAdapter adapter;

    private int pageIndex = 1;

    private boolean isLoadMore = false;//是否下拉加载

    private String status = "10";

    private String pageCount = "0";
    private static String kqType =" F|L|A";

    private List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> listBeans;

    private View left_view,center_view,right_view;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_discount_coupon;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.discount_coupon_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new EnvelopePresenter(this);
    }

    @Override
    public void initView() {
        setTitle("优惠券");
        usable = findViewById(R.id.usable);
        usable.setOnClickListener(this);
        lose_efficacy = findViewById(R.id.lose_efficacy);
        refreshLayout = findViewById(R.id.refreshLayout);
        left_view = findViewById(R.id.left_view);
        center_view = findViewById(R.id.center_view);
        right_view = findViewById(R.id.right_view);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        lose_efficacy.setOnClickListener(this);
        usage = findViewById(R.id.usage);
        discount_coupon_recycler = findViewById(R.id.discount_coupon_recycler);
        usage.setOnClickListener(this);
        usable.setSelected(true);
        listBeans =new ArrayList<>();
    }

    @Override
    public void initData() {
        queryDiscount(status,pageIndex);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.usable:
                checkView(v);
                break;
            case R.id.lose_efficacy:
                checkView(v);
                break;
            case R.id.usage:
                checkView(v);
                break;
        }

    }

    private void checkView(View view){
        pageIndex = 1;
        pageCount = "0";
        isLoadMore = false;
        refreshLayout.setNoMoreData(false);
        if (adapter != null){
            adapter.cleanData();
        }

        switch (view.getId()){
            case R.id.usable:
                usable.setSelected(true);
                lose_efficacy.setSelected(false);
                usage.setSelected(false);
                left_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                center_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                right_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                if (listBeans != null && !listBeans.isEmpty()){
                    listBeans.clear();
                }
                status = "10";

                queryDiscount(status,pageIndex);
                break;
            case R.id.lose_efficacy:
                usable.setSelected(false);
                lose_efficacy.setSelected(true);
                usage.setSelected(false);

                left_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                center_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                right_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                status = "30";
                if (listBeans != null && !listBeans.isEmpty()){
                    listBeans.clear();
                }
                queryDiscount(status,pageIndex);
                break;
            case R.id.usage:
                usable.setSelected(false);
                lose_efficacy.setSelected(false);
                usage.setSelected(true);
                left_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                center_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                right_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                status = "20";
                if (listBeans != null && !listBeans.isEmpty()){
                    listBeans.clear();
                }
                queryDiscount(status,pageIndex);
                break;
        }

    }


    private void queryDiscount(String status,int pageIndex){
        presenter.queryDiscount(kqType,status,pageIndex);
    }

    //加载
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = true;
        pageIndex = pageIndex+1;
        int count = Integer.parseInt(pageCount.trim());
        if (pageIndex <= count){
            queryDiscount(status,pageIndex);
        }
        else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }


    }

    //刷新
    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isLoadMore = false;
        pageIndex = 1;
        refreshLayout.setNoMoreData(false);
        queryDiscount(status,pageIndex);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WEB_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            isLoadMore = false;
            pageIndex = 1;
            refreshLayout.setNoMoreData(false);
            queryDiscount(status,pageIndex);
        }
    }


    @Override
    public void onDictionaryBean(MyDiscount.Ret_PBIFE_kq_getMyDiscountPage discountPage) {
        //全部
        //  dictionaryBean.getBody().getData().getAllDetailsCount();
        //失效
        String hasFailedCount = discountPage.getData().getHasFailedCount();
        //可用
        String availableCount = discountPage.getData().getAvailableCount();
        //已使用
        String alreadyUsedCount = discountPage.getData().getAlreadyUsedCount();
        pageCount = discountPage.getData().getPageInfo().getPageCount();
        usable.setText("可用("+availableCount+")");
        lose_efficacy.setText("失效("+hasFailedCount+")");
        usage.setText("已使用("+alreadyUsedCount+")");


        List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> kaQuanList = discountPage.getData().getKaQuanListList();
        if (isLoadMore && kaQuanList.isEmpty()){
            //下拉加载无更多数据
            refreshLayout.finishLoadMoreWithNoMoreData();
            showToast("暂无更多产品");
        }
        else if (!isLoadMore && kaQuanList.isEmpty()){
            //刷新无数据
            refreshLayout.setVisibility(View.GONE);
        }
        else {
            refreshLayout.setVisibility(View.VISIBLE);
        }

        if (!isLoadMore && listBeans != null&&!listBeans.isEmpty()){
            listBeans.clear();
        }
        listBeans.addAll(kaQuanList);

        if (isLoadMore){
            adapter.rest(listBeans);
        }
        else {
            adapter =  new DiscountCouponAdapter(this,listBeans,status);
            adapter.setOnItemClickListener(new OnItemClickListener<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList>() {
                @Override
                public void onItemClickListener(View v, MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList kaQuanList, int position) {


                    //1内链,2:外链
                    if ( kaQuanList.getQuanCode().equals("1")){

                        CardInfo cardInfo = new CardInfo();
                        //卡券编号
                        cardInfo.setQuanDetailsId(kaQuanList.getQuanDetailsId());

                        //核销产品编号
                        cardInfo.setQuanUsedProductCode(kaQuanList.getQuanUsedProductCode());

                        //核销系列编号
                        cardInfo.setQuanUsedSeriesCode(kaQuanList.getQuanUsedSeriesCode());

                        Intent intent = new Intent(DiscountCouponActivity.this,ProductHighTransferOrderListActivity.class);
                        intent.putExtra("cardInfo",cardInfo);

                        baseStartActivity(intent);

                    }
                    else if (kaQuanList.getQuanCode().equals("2")){

                        quanDetailsId = kaQuanList.getQuanDetailsId();
                        quanType = kaQuanList.getQuanType();
                        quanValue = kaQuanList.getQuanValue();
                        quanTypeName = kaQuanList.getQuanName();
                        enableIncreaseInterestAmount = kaQuanList.getQuanIncreaseInterestAmount();
                        quanFullReducedAmount = kaQuanList.getQuanFullReducedAmount();
                        quanUsedProduct = kaQuanList.getQuanUsedProductCode();
                        quanUsedSeries = kaQuanList.getQuanUsedSeriesCode();
                        //外链地址
                        String linkAddressUrl =  kaQuanList.getLinkAddress();

                        parseUrl(linkAddressUrl);

                    }
                    else {

                        ProductDate.rest();
                        ProductDate.quanDetailsId = kaQuanList.getQuanDetailsId();
                        ProductDate.quanUsedProductCode = kaQuanList.getQuanUsedProductCode();
                        ProductDate.quanUsedSeriesCode = kaQuanList.getQuanUsedSeriesCode();


                        HomeActivity.show(DiscountCouponActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
                    }


                }
            });
            discount_coupon_recycler.setLayoutManager(new LinearLayoutManager(this));
            discount_coupon_recycler.setAdapter(adapter);
        }
        reset(isLoadMore);
    }

    //红包提现，卡券用不到
    @Override
    public void withdraw(Withdrawals.Ret_PBIFE_kq_kqWithdrawals ret_pbife_kq_kqWithdrawals,String quanValue) {

    }

    protected void reset(boolean isLoadMore){
        if (isLoadMore){
            refreshLayout.finishLoadMore();
        }
        else {

            refreshLayout.finishRefresh();
        }
        this.isLoadMore = false;
    }





    private String quanDetailsId = "";

    private String quanTypeName = "";
    private String type = "_kaquan";
    private String quanType = "";
    private String quanValue = "";
    private String enableIncreaseInterestAmount = "";
    private String quanFullReducedAmount = "";

    private String quanUsedProduct = "";

    private String quanUsedSeries = "";

    private void parseUrl(String linkAddressUrl){

        StringBuffer buffer = new StringBuffer(linkAddressUrl);

        buffer.append("quanDetailsId=").append(quanDetailsId).append("&")
                .append("quanTypeName=").append(quanTypeName).append("&")
                .append("quanType=").append(quanType).append("&")
                .append("quanValue=").append(quanValue).append("&")
                .append("type=").append(type).append("&")
                .append("enableIncreaseInterestAmount=").append(enableIncreaseInterestAmount).append("&")
                .append("quanFullReducedAmount=").append(quanFullReducedAmount).append("&")
                .append("productCodeAndSerialNo=");

        if ((quanUsedProduct != null && !quanUsedProduct.equals("")) && (quanUsedSeries != null && !quanUsedSeries.equals(""))){
            buffer.append(quanUsedProduct).append("|").append(quanUsedSeries);
        }

        else if (quanUsedProduct != null && !quanUsedProduct.equals("")){
            buffer.append(quanUsedProduct);
        }
        else if (quanUsedSeries != null && !quanUsedSeries.equals("")){
            buffer.append(quanUsedSeries);
        }

        ShareBean shareBean = new ShareBean();
        shareBean.setFuncUrl(buffer.toString());
        startWebActivity(shareBean);
    }
}
