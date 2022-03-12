package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.adapter.MySubscriptionAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.MySubscriptionPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.MySubscriptionView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.PrdQuerySubscribeListPb;

//我的购买
public class MySubscriptionActivity extends CommActivity implements OnRefreshListener, OnLoadMoreListener, MySubscriptionView {
    private MySubscriptionAdapter mySubscriptionAdapter;//列表适配器
    private RecyclerView mRvMySubscription;//列表控件
    private SmartRefreshLayout refreshLayout;//刷新控件
    private int page = 1;
    private MySubscriptionPresenter mPresenter;
    private List<PrdQuerySubscribeListPb.PBIFE_prdquery_prdQuerySubscribeList.SubscribeList> mList = new ArrayList<>();
    CustomDialog.Builder builder = new CustomDialog.Builder(MySubscriptionActivity.this);

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_subscription;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_my_subscription);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MySubscriptionPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("我的购买");
        builder.setTitle("温馨提示");
//        builder.setMessage("是否确认要撤单吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        refreshLayout = findViewById(R.id.refreshLayout);
        mRvMySubscription = findViewById(R.id.rv_my_subscription);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        mySubscriptionAdapter = new MySubscriptionAdapter(mList);
        mRvMySubscription.setLayoutManager(new LinearLayoutManager(this));
        mRvMySubscription.setAdapter(mySubscriptionAdapter);
        mySubscriptionAdapter.setClickCallBack(new MySubscriptionAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(MySubscriptionActivity.this, SubscriptionDetailActivity.class);
                intent.putExtra("productCode", mList.get(pos).getProductCode());
                intent.putExtra("special", mList.get(pos).getSpecialFlag());
                intent.putExtra("delegateAmount", mList.get(pos).getDelegateAmount());
                intent.putExtra("delegateStatusName", mList.get(pos).getDelegateStatusName());
                baseStartActivity(intent);
            }
        });
        mySubscriptionAdapter.setCancel(new MySubscriptionAdapter.Cancel() {
            @Override
            public void onCancel(final int pos) {
                mPresenter.cancelPre(mList.get(pos).getProductCode(), mList.get(pos).getDelegationCode());
            }
        });
        mySubscriptionAdapter.setOnKqClick(new MySubscriptionAdapter.OnKqClick() {
            @Override
            public void onKqClcik(String msg) {
                showDialog(msg);
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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            refreshLayout.finishLoadMore();
            return;
        }
        page++;
        mPresenter.loadData(page + "");
    }

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
        mPresenter.loadData(page + "");
    }

    @Override
    public void getData(List<PrdQuerySubscribeListPb.PBIFE_prdquery_prdQuerySubscribeList.SubscribeList> list) {
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
            mySubscriptionAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                mList.addAll(list);
                mySubscriptionAdapter.notifyDataSetChanged();
            }
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void cancelPre(final String delegationCode, String code, String message, String data_message) {
        if (code.equals("0000")) {
            builder.setMessage(data_message.replace("<br/>", "\n"));
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mPresenter.cancel(delegationCode);
                }
            });
            builder.create().show();
        } else {
            showDialog(message);
        }
    }

    @Override
    public void cancel(String code, String msg) {
        if ("0000".equals(code)) {
            onRefresh(refreshLayout);
        }
        showDialog(msg);
    }
}
