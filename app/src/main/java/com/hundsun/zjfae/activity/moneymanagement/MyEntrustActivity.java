package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.adapter.MyEntrustAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.MyEntrustPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.MyEntrustView;
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

import onight.zjfae.afront.gens.PrdQueryTcDelegationFinanceListPb;

//我的委托
public class MyEntrustActivity extends CommActivity implements OnRefreshListener, OnLoadMoreListener, MyEntrustView {
    private MyEntrustAdapter myEntrustAdapter;//列表适配器
    private RecyclerView mRvMyEntrust;//列表控件
    private SmartRefreshLayout refreshLayout;//刷新控件
    private int page = 1;
    private String startDate = "", endDate = "";
    private PopupWindowDate popupWindowDate;
    private MyEntrustPresenter mPresenter;
    private List<PrdQueryTcDelegationFinanceListPb.PBIFE_prdquery_prdQueryTcDelegationFinanceList.TcDelegationFinaceList> mList = new ArrayList<>();

    private static final int MyEntrustActivity_REQUEST = 0X1020;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_entrust;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_my_entrust);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MyEntrustPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("我的委托");
        refreshLayout = findViewById(R.id.refreshLayout);
        mRvMyEntrust = findViewById(R.id.rv_my_entrust);
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
        myEntrustAdapter = new MyEntrustAdapter(mList);
        mRvMyEntrust.setLayoutManager(new LinearLayoutManager(this));
        mRvMyEntrust.setAdapter(myEntrustAdapter);
        myEntrustAdapter.setClickCallBack(new MyEntrustAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(MyEntrustActivity.this, EntrustDetailActivity.class);
                intent.putExtra("title", mList.get(pos).getProductName());
                intent.putExtra("type", mList.get(pos).getDelegateTypeName());
                intent.putExtra("state", mList.get(pos).getDelegateStatusName());
                intent.putExtra("transferRate", mList.get(pos).getActualRate() + "%");
                intent.putExtra("delegateNum", mList.get(pos).getDelegateNum() + "元");
                intent.putExtra("transactionNum", mList.get(pos).getTransactionNum() + "元");
                intent.putExtra("expectedMaxAnnualRate", mList.get(pos).getExpectedMaxAnnualRate() + "%");
                intent.putExtra("TransactionAmount", mList.get(pos).getTransactionAmount() + "元");
                intent.putExtra("DelegateTime", mList.get(pos).getDelegateTime());
                intent.putExtra("delegationCode", mList.get(pos).getDelegationCode());
                intent.putExtra("delegateStatus", mList.get(pos).getDelegateStatus());
                intent.putExtra("transferIsfloat", mList.get(pos).getTransferIsfloat());
                intent.putExtra("discountRate", mList.get(pos).getDiscountRate() + "折");
                startActivityForResult(intent, MyEntrustActivity_REQUEST);
            }

            @Override
            public void onCancleClick(final String delegationCode) {
                showDialog("您确定撤单该产品吗？", "确认", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.cancel(delegationCode);
                        dialogInterface.dismiss();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
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
    public void getData(List<PrdQueryTcDelegationFinanceListPb.PBIFE_prdquery_prdQueryTcDelegationFinanceList.TcDelegationFinaceList> list) {
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
            myEntrustAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                mList.addAll(list);
                myEntrustAdapter.notifyDataSetChanged();
            }
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void cancelEntrust(String code, String msg) {

        onRefresh(refreshLayout);
        showDialog(msg, "确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MyEntrustActivity_REQUEST && resultCode == RESULT_OK) {
            //说明详情页已经撤单 当前页面需要刷新
            onRefresh(refreshLayout);
        }
    }
}
