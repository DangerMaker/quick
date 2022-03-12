package com.hundsun.zjfae.activity.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.adapter.OtherCardVoucherAdapter;
import com.hundsun.zjfae.activity.mine.presenter.EnvelopePresenter;
import com.hundsun.zjfae.activity.mine.view.EnvelopeView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.MyDiscount;
import onight.zjfae.afront.gens.Withdrawals;

public class OtherVouchersActivity extends CommActivity implements EnvelopeView, OnRefreshListener, OnLoadMoreListener, View.OnClickListener {

    private EnvelopePresenter presenter;
    private TextView usable, lose_efficacy, usage;
    private RecyclerView discount_coupon_recycler;
    private SmartRefreshLayout refreshLayout;//刷新控件
    private OtherCardVoucherAdapter adapter;
    private static String kqType = "T";
    private int pageIndex = 1;
    private boolean isLoadMore = false;//是否下拉加载

    private String status = "10";
    private String pageCount = "0";

    private List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> listBeans;

    private View left_view, center_view, right_view;

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new EnvelopePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_other_vouchers;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.envelope_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void initView() {
        setTitle("其他卡券");
        usable = findViewById(R.id.usable);
        usable.setOnClickListener(this);
        lose_efficacy = findViewById(R.id.lose_efficacy);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        lose_efficacy.setOnClickListener(this);
        usage = findViewById(R.id.usage);
        left_view = findViewById(R.id.left_view);
        center_view = findViewById(R.id.center_view);
        right_view = findViewById(R.id.right_view);
        discount_coupon_recycler = findViewById(R.id.discount_coupon_recycler);
        discount_coupon_recycler.setLayoutManager(new LinearLayoutManager(this));
        usage.setOnClickListener(this);
        usable.setSelected(true);
        listBeans = new ArrayList<>();
    }

    @Override
    public void initData() {
        queryDiscount(status, pageIndex);
    }

    @Override
    public void onClick(View v) {
        pageIndex = 1;
        pageCount = "0";
        isLoadMore = false;
        refreshLayout.setNoMoreData(false);
        switch (v.getId()) {
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

    private void checkView(View view) {
        pageIndex = 1;
        pageCount = "0";
        isLoadMore = false;

        if (adapter != null){
            adapter.cleanData();
        }

        switch (view.getId()) {
            case R.id.usable:
                usable.setSelected(true);
                lose_efficacy.setSelected(false);
                usage.setSelected(false);
                left_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                center_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                right_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                if (listBeans != null && !listBeans.isEmpty()) {
                    listBeans.clear();
                }
                status = "10";
                queryDiscount(status, pageIndex);
                break;
            case R.id.lose_efficacy:
                usable.setSelected(false);
                lose_efficacy.setSelected(true);
                usage.setSelected(false);
                status = "30";

                left_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                center_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                right_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                if (listBeans != null && !listBeans.isEmpty()) {
                    listBeans.clear();
                }
                queryDiscount(status, pageIndex);
                break;
            case R.id.usage:
                usable.setSelected(false);
                lose_efficacy.setSelected(false);
                usage.setSelected(true);
                status = "20";
                left_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                center_view.setBackgroundColor(getResources().getColor(android.R.color.white));
                right_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (listBeans != null && !listBeans.isEmpty()) {
                    listBeans.clear();
                }
                queryDiscount(status, pageIndex);
                break;
        }

    }

    private void queryDiscount(String status, int pageIndex) {
        presenter.queryDiscount(kqType, status, pageIndex);
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
        usable.setText(String.format(getResources().getString(R.string.usable), availableCount));
        lose_efficacy.setText(String.format(getResources().getString(R.string.lose_efficacy), hasFailedCount));
        usage.setText(String.format(getResources().getString(R.string.usage), alreadyUsedCount));

        List<MyDiscount.PBIFE_kq_getMyDiscountPage.KaQuanList> kaQuanList = discountPage.getData().getKaQuanListList();

        if (isLoadMore && kaQuanList.isEmpty()) {
            //下拉加载无更多数据
            refreshLayout.finishLoadMoreWithNoMoreData();
        } else if (!isLoadMore && kaQuanList.isEmpty()) {
            //刷新无数据
            refreshLayout.setVisibility(View.GONE);
        } else {
            refreshLayout.setVisibility(View.VISIBLE);
        }

        if (!isLoadMore && listBeans != null && !listBeans.isEmpty()) {
            listBeans.clear();
        }
        listBeans.addAll(kaQuanList);


        if (isLoadMore) {
            adapter.rest(listBeans);
        } else {
            adapter = new OtherCardVoucherAdapter(this, listBeans, status);
            discount_coupon_recycler.setAdapter(adapter);
            adapter.setItemClickCopy(new OtherCardVoucherAdapter.OnItemClickCopy() {
                @Override
                public void onCopyValue(String copyCode) {
                    copy(copyCode);
                }

                @Override
                public void onCopyPassWord(String passWord) {
                    copy(passWord);
                }

                @Override
                public void onUse(String linkAddress) {
                    ShareBean shareBean = new ShareBean();
                    shareBean.setFuncUrl(linkAddress);
                    startWebActivity(shareBean);
                }
            });
        }
        reset(isLoadMore);
    }

    //提现红包，其他卡券用不到
    @Override
    public void withdraw(Withdrawals.Ret_PBIFE_kq_kqWithdrawals ret_pbife_kq_kqWithdrawals,String quanValue) {

    }

    private void copy(String value) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("zjfae_tv",
                value);

        cm.setPrimaryClip(clip);
        CustomDialog.Builder builder = new CustomDialog.Builder(OtherVouchersActivity.this);
        builder.setTitle("复制成功");
        builder.setMessage(value);
        builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        isLoadMore = true;
        pageIndex = pageIndex + 1;
        int count = Integer.parseInt(pageCount.trim());
        if (pageIndex <= count) {
            queryDiscount(status, pageIndex);
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        isLoadMore = false;
        pageIndex = 1;
        refreshLayout.setNoMoreData(false);
        queryDiscount(status, pageIndex);

    }

    protected void reset(boolean isLoadMore) {
        if (isLoadMore) {
            refreshLayout.finishLoadMore();
        } else {

            refreshLayout.finishRefresh();
        }
        this.isLoadMore = false;
    }
}
