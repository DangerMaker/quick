package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.adapter.RecordTransactionAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.RecordTransactionPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.RecordTransactionView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.PBIFEPrdqueryQueryUnifyPurchaseTradeList;

/**
 * @Package: com.hundsun.zjfae.activity.moneymanagement
 * @ClassName: RecordTransactionActivity
 * @Description: 交易记录
 * @Author: moran
 * @CreateDate: 2020/11/1 14:24
 * @UpdateUser: 更新者：moran
 * @UpdateDate: 2020/11/1 14:24
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class RecordTransactionActivity extends CommActivity<RecordTransactionPresenter> implements OnRefreshListener, OnLoadMoreListener, RecordTransactionView {
    private RecordTransactionAdapter myRecordTransactionAdapter;//列表适配器
    private SmartRefreshLayout refreshLayout;//刷新控件
    private RecyclerView mRvRecordTransaction;
    private int page = 1;

    private int REQUEST_CODE = 0x1101;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_transaction;
    }


    @Override
    protected RecordTransactionPresenter createPresenter() {
        return new RecordTransactionPresenter(this);
    }


    @Override
    public void initView() {
        mRvRecordTransaction = findViewById(R.id.rv_record_transaction);
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        myRecordTransactionAdapter = new RecordTransactionAdapter();

        myRecordTransactionAdapter.setClickCallBack(new RecordTransactionAdapter.ItemClickCallBack() {
            /**
             * 撤单按钮点击事件回调
             *
             * @param productCode    产品代码
             * @param delegationCode 认购编号
             * @return
             * @date: 2020/11/1 16:47
             * @author: moran
             */
            @Override
            public void onCancelFlagClick(String productCode, String delegationCode) {

                presenter.cancelPre(productCode, delegationCode);

            }

            /**
             * 撤单倒计时结束事件回调
             *
             * @return
             * @date: 2020/11/1 16:47
             * @author: moran
             */
            @Override
            public void onCountDownSuccess() {
                page = 1;
                presenter.loadData(page);
            }

            /**
             * item点击事件回调
             *
             * @param productCode        产品代码
             * @param delegationCode     认购编号
             * @param special            特约标志
             * @param tradeAmount        交易金额
             * @param delegateStatusName 成交状态中文显示
             * @param spvFlag            该产品是否是Svp产品，是隐藏购买详情页面挂牌开始/结束时间，起息日布局
             * @param cancelFlag         是否可撤单，true-可撤单，false-不可撤单
             * @param kqType             卡券类型
             * @param kqValue            卡券值
             * @param kqAddRateBj        卡券加息本金
             * @return
             * @date: 2020/11/1 16:49
             * @author: moran
             */
            @Override
            public void onItemClick(String productCode, String delegationCode, String special, String tradeAmount, String delegateStatusName, String spvFlag, boolean cancelFlag, long countDown, String kqType, String kqValue, String kqAddRateBj) {


                Intent intent = new Intent(RecordTransactionActivity.this, SubscriptionDetailActivity.class);
                intent.putExtra("productCode", productCode);
                intent.putExtra("delegationCode", delegationCode);
                intent.putExtra("special", special);
                intent.putExtra("delegateAmount", MoneyUtil.formatMoney2(tradeAmount));
                intent.putExtra("delegateStatusName", delegateStatusName);
                intent.putExtra("spvFlag", spvFlag.equals("1"));
                intent.putExtra("cancelFlag", cancelFlag);
                intent.putExtra("countDown", countDown);
                intent.putExtra("kqType", kqType);
                intent.putExtra("kqValue", kqValue);
                intent.putExtra("kqAddRateBj", kqAddRateBj);
                startActivityForResult(intent, REQUEST_CODE);

            }


        });
        mRvRecordTransaction.setAdapter(myRecordTransactionAdapter);

    }

    @Override
    public void initData() {
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
        presenter.loadData(page);
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
        presenter.loadData(page);
    }

    @Override
    public void getData(List<PBIFEPrdqueryQueryUnifyPurchaseTradeList.PBIFE_prdquery_queryUnifyPurchaseTradeList.MyTradeObject> list) {
        if (page == 1) {
            if (list == null || list.size() == 0) {
                setNoDataViewVisiable();
                refreshLayout.setEnableLoadMore(false);

            } else {
                refreshLayout.setEnableLoadMore(true);
                setNoDataViewGone();
            }
            myRecordTransactionAdapter.setData(list);
            refreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                myRecordTransactionAdapter.addData(list);
            }
            refreshLayout.finishLoadMore();
        }
    }

    /**
     * 撤单预检查接口回调
     *
     * @param delegationCode 认购编号
     * @param code           请求返回code码
     * @param message        错误提示
     * @param data_message   弹框详情
     * @return
     * @method
     * @date: 2020/11/1 15:11
     * @author: moran
     */
    @Override
    public void cancelPre(String delegationCode, String code, String message, String data_message) {

        if (code.equals("0000")) {

            showDialog(data_message.replace("<br/>", "\n"), "确认", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    presenter.cancel(delegationCode);

                }
            });

        } else {
            showDialog(message);
        }
    }

    /**
     * 撤单接口请求回调
     *
     * @param code 请求返回code码
     * @param msg  请求返回提示
     * @return
     * @method
     * @date: 2020/11/1 15:14
     * @author: moran
     */
    @Override
    public void cancel(String code, String msg) {

        if ("0000".equals(code)) {
            onRefresh(refreshLayout);
        }
        showDialog(msg);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        page = 1;
        presenter.loadData(page);

    }
}
