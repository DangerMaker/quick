package com.hundsun.zjfae.activity.mymessage;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mymessage.adapter.MyMessageAdapter;
import com.hundsun.zjfae.activity.mymessage.bean.MyMessageBean;
import com.hundsun.zjfae.activity.mymessage.presenter.MyMessagePresenter;
import com.hundsun.zjfae.activity.mymessage.view.MyMessageView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.view.dialog.CustomProgressDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.ListMessagePB;

/**
 * @Description:我的消息
 * @Author: zhoujianyu
 * @Time: 2018/9/18 10:07
 */
public class MyMessageActivity extends CommActivity implements OnRefreshListener, OnLoadMoreListener, MyMessageAdapter.onCheckBoxChangelistener, MyMessageAdapter.onGoDetailListener, MyMessageView {
    private TextView tv_read, tv_nums;
    private Button btn_check_all;
    private LinearLayout lin_bottom;
    private RecyclerView mRecyclerView;//列表控件
    private SmartRefreshLayout mRefreshLayout;//刷新控件
    private MyMessageAdapter adapter;
    private List<MyMessageBean.BodyBean.DataBean.MessageListListBean> mListSource = new ArrayList<>();//代表原始全部数据
    private List<MyMessageBean.BodyBean.DataBean.MessageListListBean> mList = new ArrayList<>();//代表所有适配器数据
    private List<MyMessageBean.BodyBean.DataBean.MessageListListBean> mListunRead = new ArrayList<>();//代表所有未读的数据
    private int page = 1;
    private MyMessagePresenter mPresenter;
    private String id = "", msgtype = "";//用于保存点击未读消息之前的id和msgtype 便于回来的时候请求接口
    private Boolean type = false;//用于判断点击详情时 请求接口
    private int position = 0;
    private CustomProgressDialog myCustomProgressDialog;

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MyMessagePresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_my_message);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_message;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("我的消息");
        myCustomProgressDialog = new CustomProgressDialog(MyMessageActivity.this, strMessage);
        myCustomProgressDialog.setCanceledOnTouchOutside(false);
        myCustomProgressDialog.setCancelable(true);
        mRefreshLayout = findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setEnableLoadMore(false);
        mRecyclerView = findViewById(R.id.recyclerView);
        lin_bottom = findViewById(R.id.lin_bottom);
        tv_nums = findViewById(R.id.tv_nums);
        btn_check_all = findViewById(R.id.btn_check_all);
        tv_read = findViewById(R.id.tv_read);
        //批量已读点击
        tv_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_read.getText().equals("批量已读")) {
                    if (adapter != null) {
                        addUnReadList();
                        btn_check_all.setText("全选");
                        tv_nums.setText("已选 0 条");


                        lin_bottom.setVisibility(View.VISIBLE);
                        mList.clear();
                        mList.addAll(mListunRead);
                        adapter.setIsDelete(true);
                        adapter.selectOfRemoveAll();
                        tv_read.setText("置为已读");
                        mRecyclerView.scrollToPosition(0);
                    }
                } else if (tv_read.getText().equals("置为已读")) {
                    //请求接口置消息为已读
                    if (adapter.getSelectList().size() <= 0) {
                        lin_bottom.setVisibility(View.GONE);
                        tv_read.setText("批量已读");
                        adapter.setIsDelete(false);
                        onRefreshData();
                        return;
                    }
                    if (!NetworkCheck.isNetworkAvailable(MyMessageActivity.this)) {
                        showToast("网络异常");
                        return;
                    }
//                    showToast(adapter.getSelectListString());
                    mPresenter.setReadMessage(adapter.getSelectListString());
//                    if (adapter != null) {
//                        lin_bottom.setVisibility(View.GONE);
//                        mList.clear();
//                        mList.addAll(mListSource);//这步赋值需要修改原始数据里面的未读数据变为已读
//                        adapter.setIsDelete(false);
//                        adapter.notifyDataSetChanged();
//                        showToast(adapter.getSelectListString());
//                        //如果源数据里面没有包含已读了的就隐藏批量已读按钮
//                        for (int i = 0; i < mListSource.size(); i++) {
//                            if (mListSource.get(i).getReadStatus().equals("0")) {
//                                tv_read.setText("批量已读");
//                                break;
//                            }
//                            if (i == mListSource.size() - 1) tv_read.setVisibility(View.GONE);
//                        }
//                    }
                }
            }
        });
        //全选点击
        btn_check_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_check_all.getText().equals("全选")) {
                    adapter.selectOfSelectAll();
                    btn_check_all.setText("取消全选");
                    tv_nums.setText("已选 " + adapter.getSelectList().size() + " 条");
//                    showToast(adapter.getSelectListString());
                } else if (btn_check_all.getText().equals("取消全选")) {
                    adapter.selectOfRemoveAll();
                    btn_check_all.setText("全选");
                    tv_nums.setText("已选 0 条");
//                    showToast(adapter.getSelectListString());
                }
            }
        });
    }

    @Override
    public void initData() {
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
            myCustomProgressDialog.dismiss();
            return;
        } else {
            setNoNetViewGone();
        }
        page = 1;
        mPresenter.getMyMessageListData(page);
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
        mPresenter.getMyMessageListData(page);
    }

    @Override
    public void onCheckBoxChange() {
        CCLog.e(adapter.getUnReadCount().size() + "全部未读消息数");
        CCLog.e(adapter.getSelectList().size() + "当前选中的未读消息个数");
        CCLog.e(adapter.getSelectListString());
//        showToast(adapter.getSelectListString());
        tv_nums.setText("已选 " + adapter.getSelectList().size() + " 条");
        if (adapter.getSelectList().size() == adapter.getUnReadCount().size()) {
            btn_check_all.setText("取消全选");
        } else {
            if (btn_check_all.getText().equals("取消全选")) btn_check_all.setText("全选");
        }
    }

    @Override
    public void loadData(List<ListMessagePB.PBIFE_messagemanage_listMessage.MessageListList> data, String unreadMessageCount) {
        List<MyMessageBean.BodyBean.DataBean.MessageListListBean> list = new ArrayList<>();
        list.clear();
        for (int i = 0; i < data.size(); i++) {
            MyMessageBean.BodyBean.DataBean.MessageListListBean messageListListBean = new MyMessageBean.BodyBean.DataBean.MessageListListBean();
            messageListListBean.setId(data.get(i).getId());
            messageListListBean.setReadStatus(data.get(i).getReadStatus());
            messageListListBean.setCreateTime(data.get(i).getCreateTime());
            messageListListBean.setTitle(data.get(i).getTitle());
            messageListListBean.setContentFilter(data.get(i).getContentFilter());
            messageListListBean.setContent(data.get(i).getContent());
            messageListListBean.setTitle(data.get(i).getTitle());
            messageListListBean.setMsgPublishType(data.get(i).getMsgPublishType());
            messageListListBean.setCheck(false);
            list.add(messageListListBean);
        }
        if (!unreadMessageCount.equals("0")) {
            if (tv_read.getVisibility() == View.GONE) tv_read.setVisibility(View.VISIBLE);
        } else {
            if (tv_read.getVisibility() == View.VISIBLE) tv_read.setVisibility(View.GONE);
        }
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mRefreshLayout.setEnableLoadMore(false);
                setNoDataViewGone();
                mListSource.clear();
                mListSource = list;
//                addUnReadList();
                mList.clear();
                if (lin_bottom.getVisibility() == View.GONE) {
                    mList.addAll(list);
                    adapter = new MyMessageAdapter(this, mList);
                } else {
                    mList.addAll(mListunRead);
                    adapter = new MyMessageAdapter(this, mList);
                    if (lin_bottom.getVisibility() == View.VISIBLE) {
                        adapter.setIsDelete(true);
                    }
                }
                adapter.setCheckBoxChangelistener(this);
                adapter.setOnGoDetailListener(this);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(adapter);
            }
            mRefreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                mRefreshLayout.setEnableLoadMore(false);
            } else {
                mListSource.addAll(list);
//                addUnReadList();
                if (lin_bottom.getVisibility() == View.GONE) {
                    mList.addAll(list);
                } else {
                    mList.clear();
                    mList.addAll(mListunRead);
                }
                adapter.notifyDataSetChanged();
            }
            mRefreshLayout.finishLoadMore();
        }
        myCustomProgressDialog.dismiss();
    }

    @Override
    public void setReadMessageResponse(String code, String message) {
        if (type) {
            if (code != null && code.equals("0000")) {
                mListSource.get(position).setReadStatus("1");
                mList.get(position).setReadStatus("1");
                adapter.notifyItemChanged(position);
            } else {
                showToast(message);
            }
        } else {
            showToast(message);
            if (code != null && code.equals("0000")) {
                lin_bottom.setVisibility(View.GONE);
                tv_read.setText("批量已读");
                adapter.setIsDelete(false);
                myCustomProgressDialog.show();
                onRefreshData();
            }
        }
        type = false;
    }

    private void addUnReadList() {
        mListunRead.clear();
        for (int i = 0; i < mListSource.size(); i++) {
            if (mListSource.get(i).getReadStatus().equals("0")) {
                mListunRead.add(mListSource.get(i));
                if (btn_check_all.getText().equals("取消全选")) {//上拉加载的时候 如果原来以及是批量已读的状态下 并且底部以及全选 则加数据的时候默认把数据全部选中
                    mListSource.get(i).setCheck(true);
                }
            }
        }
        if (btn_check_all.getText().equals("全选")) {
            tv_nums.setText("已选 0 条");
        } else {
            tv_nums.setText("已选 " + mListunRead.size() + " 条");
        }

    }

    @Override
    public void goMessageDetail(int position, String id, String msgtype, String title, String content) {
        this.position = position;
        this.id = id;
        this.msgtype = msgtype;
        Intent intent = new Intent(this, MyMessageDetailActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                type = true;
                mPresenter.setReadMessage("[" + id + "|" + msgtype + "]");
        }
    }
}
