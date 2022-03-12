package com.hundsun.zjfae.activity.moneymanagement;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.adapter.MyHoldingAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.MyHoldingPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.MyHoldingView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.view.ListViewPopupDropDownWindow;
import com.hundsun.zjfae.common.view.dialog.CustomProgressDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v3.PrdQueryTaUnitFinanceNewPb;

/**
  * @Package:        com.hundsun.zjfae.activity.moneymanagement
  * @ClassName:      MyHoldingActivity
  * @Description:    我的资产
  * @Author:         moran
  * @CreateDate:     2019-12-02 16:33
  * @UpdateUser:     更新者：moran
  * @UpdateDate:     2019-12-02 16:33
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class MyHoldingActivity extends CommActivity<MyHoldingPresenter> implements View.OnClickListener, OnRefreshListener, OnLoadMoreListener, MyHoldingView {
    private MyHoldingAdapter myHoldingAdapter;//列表适配器
    private RecyclerView mRvMyHold;//列表控件
    private SmartRefreshLayout refreshLayout;//刷新控件
    private ListViewPopupDropDownWindow mPopupWindow;
    private ImageView mQueryAIv;
    private ImageView mQueryBUpIv;
    private ImageView mQueryBDownIv;
    private ImageView mQueryCUpIv;
    private ImageView mQueryCDownIv;
    private LinearLayout mQueryLl;
    private List<String> mQuery = new ArrayList<>();//筛选列表
    private String mCurrent;//当前选中
    private LinearLayout mQueryALl;
    private LinearLayout mQueryBLl;
    private LinearLayout mQueryCLl;
    private TextView mQueryATv;
    private TextView mQueryBTv;
    private TextView mQueryCTv;
    private Boolean QueryBUp = false;
    private Boolean QueryCUp = false;
    private int page = 1;
    private TextView mNumberHold;
    private TextView mIncomeTotal;
    private int mTolPage;
    private String mUuid = "";
    private List<PrdQueryTaUnitFinanceNewPb.PBIFE_prdquery_prdQueryTaUnitFinanceNew.TaUnitFinanceList> mList = new ArrayList<>();
    private List<AllAzjProto.PBAPPSearchSortControl_l2> mQur = new ArrayList<>();
    private List<AllAzjProto.PBAPPSearchSortControl_l2> mQuA = new ArrayList<>();
    private List<AllAzjProto.PBAPPSearchSortControl_l2> mQuB = new ArrayList<>();
    private List<AllAzjProto.PBAPPSearchSortControl_l2> mQuC = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_holding;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_my_holding);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected MyHoldingPresenter createPresenter() {
        return  new MyHoldingPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("我的资产");

        refreshLayout = findViewById(R.id.refreshLayout);
        mRvMyHold = findViewById(R.id.rv_my_holding);
        myHoldingAdapter = new MyHoldingAdapter(this, mList);
        mRvMyHold.setLayoutManager(new LinearLayoutManager(this));
        mRvMyHold.setAdapter(myHoldingAdapter);
        myHoldingAdapter.setClickCallBack(new MyHoldingAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos, String type) {
                //表示的是基金交易 跳转基金详情
                if (type.equals("13")) {
                    Intent intent = new Intent(MyHoldingActivity.this, HoldingFundDetailActivity.class);
                    intent.putExtra("id", mList.get(pos).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MyHoldingActivity.this, HoldingDetailActivity.class);
                    intent.putExtra("productCode", mList.get(pos).getProductCode());
                    intent.putExtra("id", mList.get(pos).getId());
                    intent.putExtra("title", mList.get(pos).getProductName());
                    intent.putExtra("state", mList.get(pos).getStatusStr());
                    intent.putExtra("unit", mList.get(pos).getUnit());
                    intent.putExtra("freeze", mList.get(pos).getFreeze());
                    intent.putExtra("leftDays", mList.get(pos).getLeftDays());
                    intent.putExtra("manageEndDate", mList.get(pos).getManageEndDate());
                    intent.putExtra("expectedMaxAnnualRate", mList.get(pos).getExpectedMaxAnnualRate());
                    intent.putExtra("isTransfer", mList.get(pos).getIsTransfer());
                    intent.putExtra("canTransferAmount", mList.get(pos).getCanTransferAmount());
                    intent.putExtra("noTransferAmount", mList.get(pos).getNoTransferAmount());
                    intent.putExtra("preProfit", mList.get(pos).getPreProfit());
                   intent.putExtra("isWholeTransfer", mList.get(pos).getIsWholeTransfer());
                    intent.putExtra("ifTimeChoose", mList.get(pos).getIfTimeChoose());
                    intent.putExtra("ifCanTransfer", mList.get(pos).getIfCanTransfer());
                    intent.putExtra("unitDate",mList.get(pos).getUnitDate());
                   //intent.putExtra("leftDay", mList.get(pos).getLeftDays());
                    intent.putExtra("interestByTimeRange", mList.get(pos).getInterestByTimeRange());
                    startActivityForResult(intent, 0);
                }
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        mQueryAIv = (ImageView) findViewById(R.id.iv_queryA);
        mQueryBUpIv = (ImageView) findViewById(R.id.iv_queryB_up);
        mQueryBDownIv = (ImageView) findViewById(R.id.iv_queryB_down);
        mQueryCUpIv = (ImageView) findViewById(R.id.iv_queryC_up);
        mQueryCDownIv = (ImageView) findViewById(R.id.iv_queryC_down);
        mQueryLl = (LinearLayout) findViewById(R.id.ll_query);
        mPopupWindow = new ListViewPopupDropDownWindow(this);
        mPopupWindow.setItemClickListener(new ListViewPopupDropDownWindow.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mCurrent = mQuA.get(position).getControlName();
                mQueryATv.setText(mQuA.get(position).getControlName());
                mPopupWindow.initData(mQuery, mCurrent);
                mPopupWindow.dismiss();
                searchList(mQuery.get(position), "first");
            }
        });
        mQueryALl = (LinearLayout) findViewById(R.id.ll_queryA);
        mQueryBLl = (LinearLayout) findViewById(R.id.ll_queryB);
        mQueryCLl = (LinearLayout) findViewById(R.id.ll_queryC);
        mQueryALl.setOnClickListener(this);
        mQueryBLl.setOnClickListener(this);
        mQueryCLl.setOnClickListener(this);
        mQueryATv = (TextView) findViewById(R.id.tv_queryA);
        mQueryBTv = (TextView) findViewById(R.id.tv_queryB);
        mQueryCTv = (TextView) findViewById(R.id.tv_queryC);
        mNumberHold = (TextView) findViewById(R.id.hold_number);
        mIncomeTotal = (TextView) findViewById(R.id.total_income);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_queryA:
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                } else {
                    showPopupWindow();
                }
                mQueryATv.setTextColor(getResources().getColor(R.color.colorPrimary));
                mQueryBTv.setTextColor(getResources().getColor(R.color.main_black));
                mQueryCTv.setTextColor(getResources().getColor(R.color.main_black));
                mQueryAIv.setImageResource(R.drawable.arrow_down);
                mQueryBUpIv.setImageResource(R.drawable.arrow_up_black);
                mQueryBDownIv.setImageResource(R.drawable.arrow_down_black);
                mQueryCUpIv.setImageResource(R.drawable.arrow_up_black);
                mQueryCDownIv.setImageResource(R.drawable.arrow_down_black);
                searchList(mQueryATv.getText().toString(), "first");
                break;
            case R.id.ll_queryB:
                if (!QueryBUp) {
                    QueryBUp = true;
                    mQueryBUpIv.setImageResource(R.drawable.arrow_up);
                    mQueryBDownIv.setImageResource(R.drawable.arrow_down_black);
                } else {
                    QueryBUp = false;
                    mQueryBUpIv.setImageResource(R.drawable.arrow_up_black);
                    mQueryBDownIv.setImageResource(R.drawable.arrow_down);
                }
                mQueryATv.setTextColor(getResources().getColor(R.color.main_black));
                mQueryBTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                mQueryCTv.setTextColor(getResources().getColor(R.color.main_black));
                mQueryAIv.setImageResource(R.drawable.arrow_down_black);
                mQueryCUpIv.setImageResource(R.drawable.arrow_up_black);
                mQueryCDownIv.setImageResource(R.drawable.arrow_down_black);
                searchList(mQueryBTv.getText().toString(), "second");
                break;
            case R.id.ll_queryC:
                if (!QueryCUp) {
                    QueryCUp = true;
                    mQueryCUpIv.setImageResource(R.drawable.arrow_up);
                    mQueryCDownIv.setImageResource(R.drawable.arrow_down_black);
                } else {
                    QueryCUp = false;
                    mQueryCUpIv.setImageResource(R.drawable.arrow_up_black);
                    mQueryCDownIv.setImageResource(R.drawable.arrow_down);
                }
                mQueryATv.setTextColor(getResources().getColor(R.color.main_black));
                mQueryBTv.setTextColor(getResources().getColor(R.color.main_black));
                mQueryCTv.setTextColor(getResources().getColor(R.color.colorPrimary));
                mQueryAIv.setImageResource(R.drawable.arrow_down_black);
                mQueryBUpIv.setImageResource(R.drawable.arrow_up_black);
                mQueryBDownIv.setImageResource(R.drawable.arrow_down_black);
                searchList(mQueryCTv.getText().toString(), "third");
                break;
            default:
                break;
        }
    }

    private void searchList(String search, String sign) {
        switch (sign) {
            case "first":
                for (int i = 0; i < mQuA.size(); i++) {
                    if (search.equals(mQuA.get(i).getControlName())) {
                        mUuid = mQuA.get(i).getUuid();
                    }
                }
                break;
            case "second":
                for (int i = 0; i < mQuB.size(); i++) {
                    if (QueryBUp) {
                        if ("0".equals(mQuB.get(i).getControlSort())) {
                            mUuid = mQuB.get(i).getUuid();
                        }
                    } else {
                        if ("1".equals(mQuB.get(i).getControlSort())) {
                            mUuid = mQuB.get(i).getUuid();
                        }
                    }
                }
                break;
            case "third":
                for (int i = 0; i < mQuC.size(); i++) {
                    if (QueryCUp) {
                        if ("0".equals(mQuC.get(i).getControlSort())) {
                            mUuid = mQuC.get(i).getUuid();
                        }
                    } else {
                        if ("1".equals(mQuC.get(i).getControlSort())) {
                            mUuid = mQuC.get(i).getUuid();
                        }
                    }
                }
                break;
            default:
                break;
        }
        page = 1;
        presenter.initProduct(page + "", mUuid);
    }

    @Override
    public void initData() {
        presenter.init(1+"","");
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            refreshLayout.finishLoadMore();
            return;
        }
        if (page < mTolPage) {
            page++;
            presenter.initProduct(page + "", mUuid);
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            refreshLayout.finishRefresh();
            return;
        }
        page = 1;
        presenter.initProduct(page + "", mUuid);
    }

    @Override
    public void queryCriteriaList(List<AllAzjProto.PBAPPSearchSortControl_l1> control_list) {
        mQur = control_list.get(0).getControlsList();
        for (int i = 0; i < mQur.size(); i++) {
            switch (mQur.get(i).getControlPosition()) {
                case "first":
                    mQuA.add(mQur.get(i));
                    mQuery.add(mQur.get(i).getControlName());
                    break;
                case "second":
                    mQuB.add(mQur.get(i));
                    break;
                case "third":
                    mQuC.add(mQur.get(i));
                    break;
                default:
                    break;
            }
        }
        mCurrent = mQuA.get(0).getControlName();
        mQueryATv.setText(mQuA.get(0).getControlName());
        mUuid = mQuA.get(0).getUuid();
        //presenter.getImage();//先获取图片资源
    }



    @Override
    public void onPrdQueryList(String pageCount, String totalCount, String sumPreProfit, List<PrdQueryTaUnitFinanceNewPb.PBIFE_prdquery_prdQueryTaUnitFinanceNew.TaUnitFinanceList> list) {


        mTolPage = Integer.parseInt(pageCount);
        mNumberHold.setText(totalCount);
        mIncomeTotal.setText(sumPreProfit);
        if (page == 1) {
            mList.clear();
            if (list.isEmpty()) {
                findViewById(R.id.no_data_layout).setVisibility(View.VISIBLE);
                mRvMyHold.setVisibility(View.GONE);
                refreshLayout.setEnableLoadMore(false);
            } else {
                mRvMyHold.setVisibility(View.VISIBLE);
                findViewById(R.id.no_data_layout).setVisibility(View.GONE);
                refreshLayout.setEnableLoadMore(true);
                mList.addAll(list);
            }
            myHoldingAdapter.notifyDataSetChanged();
            refreshLayout.finishRefresh();
        } else {
            if (list == null || list.size() == 0) {
                refreshLayout.setEnableLoadMore(false);
            } else {
                mList.addAll(list);
                myHoldingAdapter.notifyDataSetChanged();
            }
            refreshLayout.finishLoadMore();
        }

    }

    @Override
    public void getImage(List<AllAzjProto.PBAPPIcons> iconList) {
        myHoldingAdapter.setIconList(iconList);
        // presenter.loadData(page + "", mUuid);
    }



    //弹出下拉框
    private void showPopupWindow() {
        mPopupWindow.initData(mQuery, mCurrent);
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.show(mQueryLl);
            //mPopupWindow.showAtLocation(mQueryLl, Gravity.BOTTOM, 0,  0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
