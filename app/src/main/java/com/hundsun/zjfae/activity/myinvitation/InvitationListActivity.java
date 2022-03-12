package com.hundsun.zjfae.activity.myinvitation;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.myinvitation.adapter.InvitationListAdapter;
import com.hundsun.zjfae.activity.myinvitation.presenter.MyInvitationListPresenter;
import com.hundsun.zjfae.activity.myinvitation.view.MyInvitationListView;
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

import onight.zjfae.afront.gensazj.RecommendDetailsInfoPB;

/**
 * @Description:推荐明细列表
 * @Author: zhoujianyu
 * @Time: 2018/9/18 10:44
 */
public class InvitationListActivity extends CommActivity implements OnRefreshListener, OnLoadMoreListener, MyInvitationListView {
    private RecyclerView mRecyclerView;//列表控件
    private SmartRefreshLayout mRefreshLayout;//刷新控件
    private InvitationListAdapter adapter;
    private List<RecommendDetailsInfoPB.PBIFE_friendsrecommend_recommendDetailsInfo.RecommendDetailsList> mList = new ArrayList<>();
    private PopupWindowDate popupWindowDate;
    private String startDate = "", endDate = "", frmobile = "";
    private MyInvitationListPresenter mPresenter;
    private int page = 1;

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MyInvitationListPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_invitation_list);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invitation_list;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("推荐明细");
        popupWindowDate = new PopupWindowDate(this, true, new PopupWindowDate.SearchItemClick() {
            @Override
            public void onSearchClick(String start_time, String end_time, String phoneNumber) {
                CCLog.e(start_time + "-" + end_time + "-" + phoneNumber);
                startDate = start_time;
                endDate = end_time;
                frmobile = phoneNumber;
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
        mPresenter.getMyInvitationListData(page, startDate, endDate, frmobile);
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
        mPresenter.getMyInvitationListData(page, startDate, endDate, frmobile);
    }

    //数据请求完成回调
    @Override
    public void loadData(List<RecommendDetailsInfoPB.PBIFE_friendsrecommend_recommendDetailsInfo.RecommendDetailsList> list) {
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
                mList.clear();
                mList = list;
                adapter = new InvitationListAdapter(this, mList);
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
