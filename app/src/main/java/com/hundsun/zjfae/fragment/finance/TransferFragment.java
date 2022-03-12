package com.hundsun.zjfae.fragment.finance;

import com.google.gson.Gson;
import com.google.protobuf.Message;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.moneymanagement.MyHoldingActivity;
import com.hundsun.zjfae.activity.product.SelectConditionTransferListActivity;
import com.hundsun.zjfae.activity.product.TransferDetailActivity;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.product.config.TransferConfiguration;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.SolveClickTouchConflictLayout;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.hundsun.zjfae.fragment.finance.adapter.TransferAdapter;
import com.hundsun.zjfae.fragment.finance.bean.AmountNewSelectConditionEntity;
import com.hundsun.zjfae.fragment.finance.bean.ConditionEntity;
import com.hundsun.zjfae.fragment.finance.bean.TimeNewSelectConditionEntity;
import com.hundsun.zjfae.fragment.finance.bean.TransferIncome;
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeSelectEntity;
import com.hundsun.zjfae.fragment.finance.bean.TransferSelectEntity;
import com.hundsun.zjfae.fragment.finance.bean.TransferIncomeNewSelectEntity;
import com.hundsun.zjfae.fragment.finance.dialog.NoTransferListDataDialog;
import com.hundsun.zjfae.fragment.finance.dialog.OnItemIncomeSelectEntityListener;
import com.hundsun.zjfae.fragment.finance.dialog.OnItemSelectListener;
import com.hundsun.zjfae.fragment.finance.dialog.TransferAmountSelectDialog;
import com.hundsun.zjfae.fragment.finance.dialog.TransferIncomeSelectDialog;
import com.hundsun.zjfae.fragment.finance.widget.TransferSelectViewGroup;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v4.TransferList;

import static com.hundsun.zjfae.activity.product.config.TransferConfiguration.TRANSFER_ENTITY;

/**
 * @Package: com.hundsun.zjfae.fragment.finance
 * @ClassName: TransferFragment
 * @Description: 转让列表
 * @Author: moran
 * @CreateDate: 2021/5/10 13:42
 * @UpdateUser: 更新者：moran
 * @UpdateDate: 2021/5/10 13:42
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class TransferFragment extends BaseFragment<TransferPresenter> implements OnRefreshListener, TransferView {


    private SmartRefreshLayout transferLayout;
    //是否下拉加载
    private boolean isLoadMore = false;


    private ImageView image_not_date;

    private TextView no_date_tv;

    //我要卖图标
    private SolveClickTouchConflictLayout layout;
    private RecyclerView transfer_view;
    /**
     * 转让列表适配器
     */
    private TransferAdapter transferAdapter;
    //购买列表
    private List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList> tradeInfoList;

    /**
     * 默认初始化为第一页产品列表
     */
    private int pageIndex = 1;
    /**
     * 查询条件
     */
    private String uuids = "";


    private static final int requestCodes = 1031;
    private static final int transfer_success_requestCodes = 1032;

    private Button btn_top;

    private Button bt_select_condition;


    /**
     * 选择起投金额
     */
    private TransferSelectViewGroup amount_viewGroup;

    /**
     * 选择剩余时间
     */
    private TransferSelectViewGroup surplusDayViewGroup;


    /**
     * 受让后参考收益率最高布局
     */
    private LinearLayout ll_income_layout;

    private TextView tvIncome;

    /**
     * 选择起投金额弹框
     */
    private TransferAmountSelectDialog amountSelectDialog = null;
    /**
     * 选择剩余期限弹框
     */
    private TransferAmountSelectDialog timeSelectDialog = null;

    /**
     * 参考收益率排序
     */
    private TransferIncomeSelectDialog incomeDialog = null;

    //跳转携带数据封装类
    private final TransferSelectEntity transferSelectEntity = new TransferSelectEntity();

    @Override
    protected TransferPresenter createPresenter() {
        return new TransferPresenter(this);
    }

    private void init() {
        transferLayout.setNoMoreData(false);
        isLoadMore = false;
        pageIndex = 1;
        presenter.initTransfer(uuids, pageIndex);

        presenter.controlList("0");
        presenter.controlList("1");


    }

    @Override
    public void initData() {
        if (presenter != null && BaseActivity.isLogin) {
            cleanSelectCondition();

            init();
        }
    }

    public void setNullKeywordName(String keywordName) {
        pageIndex = 1;
        this.uuids = "";


    }


    private void initTransferList(final List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList> productTradeInfoList) {

        if (tradeInfoList == null) {
            tradeInfoList = new ArrayList<>();
        }
        //加载更多无数据
        if (isLoadMore && productTradeInfoList.isEmpty()) {
            //初始化为空
            transferLayout.finishLoadMoreWithNoMoreData();
        }
        //下拉刷新无数据
        else if (!isLoadMore && productTradeInfoList.isEmpty()) {
            no_date_tv.setVisibility(View.GONE);
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            // 屏幕宽度（像素）
            ViewGroup.LayoutParams layoutParams = image_not_date.getLayoutParams();
            layoutParams.width = 640;
            layoutParams.height = 438;
            image_not_date.setVisibility(View.VISIBLE);
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
            image_not_date.setLayoutParams(layoutParams);
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.transfer_bg_kong, image_not_date);
            transfer_view.setVisibility(View.GONE);
            transferLayout.finishLoadMoreWithNoMoreData();
        } else {
            image_not_date.setVisibility(View.GONE);
            no_date_tv.setVisibility(View.GONE);
            transferLayout.setNoMoreData(false);
            transfer_view.setVisibility(View.VISIBLE);
            if (!isLoadMore && tradeInfoList != null && !tradeInfoList.isEmpty()) {
                tradeInfoList.clear();
            }
            tradeInfoList.addAll(productTradeInfoList);
            if (isLoadMore) {
                tradeInfoList = removeDuplicateKeepOrder(tradeInfoList);
                transferAdapter.refresh(tradeInfoList);
            } else {
                transferAdapter = new TransferAdapter(mActivity, tradeInfoList);
                transfer_view.setAdapter(transferAdapter);
                transferAdapter.setClickCallBack(new TransferAdapter.ItemClickCallBack() {
                    @Override
                    public void onItemClick(int position) {

                        if (Utils.isFastDoubleClick()) {
                            return;
                        }

                        Intent intent = new Intent(mActivity, TransferDetailActivity.class);
                        Bundle bundle = new Bundle();
                        TransferDetailPlay playBean = new TransferDetailPlay();
                        playBean.setDelegationId(tradeInfoList.get(position).getDelegationId());
                        playBean.setIfAllBuy(tradeInfoList.get(position).getIfAllBuy());
                        if (UserInfoSharePre.getTradeAccount().equals(tradeInfoList.get(position).getTradeAccount())) {
                            playBean.setMyEntry(true);
                        } else {
                            playBean.setMyEntry(false);
                        }

                        bundle.putParcelable("playInfo", playBean);
                        intent.putExtra("playBundle", bundle);

                        startActivityForResult(intent, requestCodes);
                    }
                });
            }
        }

    }


    @Override
    public void initTransfer(TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew listNew) {

        initTransferList(listNew.getData().getProductTradeInfoListList());
        reset(isLoadMore);


    }

    /**
     * 转让列表集合数量
     *
     * @param body 转让列表产品
     */
    @Override
    public void onQueryTransferList(TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew body) {

        List<TransferList.PBIFE_prdtransferquery_prdQueryTransferOrderListNew.ProductTradeInfoList> tradeInfoLists = body.getData().getProductTradeInfoListList();
        CCLog.i("body", body.toString());


        if (tradeInfoLists.size() > 0) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    TransferIncomeSelectEntity transferIncomeSelectEntity = transferSelectEntity.getSelectEntity();
                    CCLog.i("transferSelectEntity", transferSelectEntity.getAmountAndTimeUUid());
                    //代表没有选择起投金额，剩余期限，排序方式
                    // if (transferIncomeSelectEntity.getSelectName().isEmpty() && transferSelectEntity.getAmountAndTimeUUid().equals("--")) {
                    if (transferIncomeSelectEntity.getSelectName().isEmpty()) {
                        transferIncomeSelectEntity = new TransferIncomeSelectEntity();
                        transferIncomeSelectEntity.setControlSort("0");
                        List<String> selectIncomeList = new ArrayList<>();
                        selectIncomeList.add("剩余期限");
                        transferIncomeSelectEntity.setSelectName(selectIncomeList);
                        transferSelectEntity.setSelectEntity(transferIncomeSelectEntity);
                    }

                    Intent intent = new Intent(getActivity(), SelectConditionTransferListActivity.class);
                    intent.putExtra(TRANSFER_ENTITY, new Gson().toJson(transferSelectEntity));
                    startActivityForResult(intent, TransferConfiguration.TransferRequestCodeConfiguration.INSTANCE.getSELECT_TRANSFER_REQUEST_CODE());

                }
            }, 300l);

        } else {

            final NoTransferListDataDialog noTransferListData = new NoTransferListDataDialog();


            noTransferListData.show(getFragmentManager());
        }

    }


    @Override
    public void onControl(AllAzjProto.PEARetControl productControl, String controlType) {

        List<AllAzjProto.PBAPPSearchSortControl_l1> controlListList = productControl.getControlListList();
        //筛选
        if (controlType.equals("1")) {

            if (amountSelectDialog == null) {

                amountSelectDialog = new TransferAmountSelectDialog(getContext());
                amountSelectDialog.setSelectTypeName("请选择起投金额");

                //点击确认按钮回调事件
                amountSelectDialog.setOnItemClickListener(new OnItemSelectListener() {

                    @Override
                    public void onSelectItem(@NotNull List<String> mutableList, String uuidValue, @NotNull Map<Integer, Boolean> selectMap) {
                        transferSelectEntity.setAmountUUids(uuidValue);
                        transferSelectEntity.setAmountSelectConditionName(new ArrayList<>(mutableList));
                        transferSelectEntity.setAmountSelectMap(new HashMap<>(selectMap));
                        //如果返回的集合是空的，则表示没有筛选条件，将提示信息设置为默认提示，否则就返显选择的筛选条件
                        if (mutableList.isEmpty()) {
                            amount_viewGroup.addDefaultView("请选择起投金额");
                        } else {
                            amount_viewGroup.addChildView(mutableList, "请选择起投金额");

                        }


                    }
                });
            }

            //筛选剩余期限弹框
            if (timeSelectDialog == null) {

                timeSelectDialog = new TransferAmountSelectDialog(getContext());

                timeSelectDialog.setSelectTypeName("请选择剩余期限");

                timeSelectDialog.setOnItemClickListener(new OnItemSelectListener() {
                    @Override
                    public void onSelectItem(@NotNull List<String> mutableList, String uuidValue, @NotNull Map<Integer, Boolean> selectMap) {
                        transferSelectEntity.setTimeUUids(uuidValue);
                        transferSelectEntity.setTimeSelectConditionName(new ArrayList<>(mutableList));
                        transferSelectEntity.setTimeSelectMap(new HashMap<>(selectMap));
                        //如果返回的集合是空的，则表示没有筛选条件，将提示信息设置为默认提示，否则就返显选择的筛选条件
                        if (mutableList.isEmpty()) {
                            surplusDayViewGroup.addDefaultView("请选择剩余期限");
                        } else {
                            surplusDayViewGroup.addChildView(mutableList, "请选择剩余期限");

                        }


                    }
                });
            }

            //金额筛选集合
            List<ConditionEntity> amountTransferSort = new ArrayList<>();

            //时间筛选集合
            List<ConditionEntity> timeTransferSort = new ArrayList<>();

            if (!controlListList.isEmpty()) {
                List<AllAzjProto.PBAPPSearchSortControl_l2> controlList = new ArrayList<>(controlListList.get(0).getControlsList());
                //过滤默认筛选条件
                for (AllAzjProto.PBAPPSearchSortControl_l2 value : controlList) {


                    if (!value.getControlHName().contains("默认筛选")) {

                        //buyerSmallestAmountQuery 是起投金额筛选选项
                        if (value.getControlValue().equals("buyerSmallestAmountQuery")) {

                            ConditionEntity entity = new ConditionEntity();
                            entity.setConditionName(value.getControlHName());
                            entity.setUuid(value.getUuid());
                            amountTransferSort.add(entity);
                        }

                        //期限 -> deadLineQuery 是剩余期限筛选选项
                        else {
                            ConditionEntity entity = new ConditionEntity();
                            entity.setConditionName(value.getControlHName());
                            entity.setUuid(value.getUuid());
                            timeTransferSort.add(entity);
                        }

                    }

                }

                //给起投金额弹框设置数据
                amountSelectDialog.setTransferDefaultSortList(amountTransferSort);
                transferSelectEntity.setAmountTransferSortList(new ArrayList<>(amountTransferSort));

                //给剩余期限弹框设置数据
                timeSelectDialog.setTransferDefaultSortList(timeTransferSort);
                transferSelectEntity.setTimeTransferSortList(new ArrayList<>(timeTransferSort));
            }

            //起投金额点击事件
            amount_viewGroup.setOnClickListener(v -> {


                amountSelectDialog.showDialog();

            });

            //剩余期限点击事件
            surplusDayViewGroup.setOnClickListener(v -> {

                timeSelectDialog.showDialog();

            });

        }
        //排序
        else if (controlType.equals("0")) {

            //排序弹框
            if (incomeDialog == null) {

                incomeDialog = new TransferIncomeSelectDialog(getContext());

                incomeDialog.setSelectTypeName("请选择排序方式");

                incomeDialog.setOnItemIncomeSelectEntityListener(new OnItemIncomeSelectEntityListener() {
                    @Override
                    public void onItemIncomeSelectEntity(@NotNull TransferIncomeSelectEntity entity) {

                        transferSelectEntity.setSelectEntity(entity);
                        List<String> selectName = entity.getSelectName();

                        //如果返回的集合是空的，则表示没有筛选条件，将提示信息设置为默认提示，否则就返显选择的筛选条件
                        if (!selectName.isEmpty()) {

                            StringBuilder stringBuffer = new StringBuilder();

                            for (String str : selectName) {

                                stringBuffer.append(str);
                            }

                            tvIncome.setText(stringBuffer.toString());
                        } else {
                            tvIncome.setText("请选择排序方式");
                        }

                    }


                });


            }

            //设置排序数据集合
            List<AllAzjProto.PBAPPSearchSortControl_l2> controlList = new ArrayList<>();
            controlList.addAll(controlListList.get(0).getControlsList());

            //过滤默认排序的新排序数据集合
            List<ConditionEntity> incomeControlList = new ArrayList<>();

            //封装排序展示数据集合
            List<TransferIncome> transferIncomeList = new ArrayList<>();


            //过滤排序重复数据，点击确认跳转界面用到
            String oldControlHName = "";
            //过滤默认排序选项
            /**
             * 升序、降序 uuid集合
             * */
            Map<String, String> controlSortUUIDMap = new HashMap<>(controlList.size());
            for (AllAzjProto.PBAPPSearchSortControl_l2 value : controlList) {
                //添加非默认排序选项数据
                ConditionEntity conditionEntity = new ConditionEntity();
                conditionEntity.setConditionName(value.getControlName());
                conditionEntity.setUuid(value.getUuid());
                conditionEntity.setControlSort(value.getControlSort());
                incomeControlList.add(conditionEntity);
                TransferIncome entity = new TransferIncome();

                controlSortUUIDMap.put(value.getControlSort(), value.getUuid());

                entity.setControlNname(value.getControlHName());

                //过滤重复数据
                if (oldControlHName.equals(value.getControlHName())) {
                    Log.i("controlSortList", new Gson().toJson(controlSortUUIDMap));
                    entity.setControlSortUUIDMap(new HashMap<>(controlSortUUIDMap));
                    transferIncomeList.add(entity);
                } else {
                    oldControlHName = value.getControlHName();

                }
            }

            //设置跳转排序选项集合
            transferSelectEntity.setTransferIncomeList(transferIncomeList);

            //排序设置数据
            incomeDialog.setTransferDefaultSortList(incomeControlList);


            //排序点击事件
            ll_income_layout.setOnClickListener(v -> {

                incomeDialog.showDialog();

            });

        }


    }

    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, boolean isAuthentication) {

        if (isAuthentication) {

            certificateStatusType(userDetailInfo.getData().getVerifyName(), userDetailInfo.getData().getCertificateStatusType());

        } else {
            final String highNetWorthStatus = userDetailInfo.getData().getHighNetWorthStatus();
            final String isRealInvestor = userDetailInfo.getData().getIsRealInvestor();
            String userType = userDetailInfo.getData().getUserType();
            if (userDetailInfo.getData().getIsBondedCard().equals("false") && userType.equals("personal")) {

                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(mActivity, AddBankActivity.class);
                    }
                });

            } else if (!userDetailInfo.getData().getIsAccreditedInvestor().equals("1")) {

                // 合格投资者审核中
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getData().getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }
                    showDialog(pmtInfo);
                }
                // 合格投资者审核不通过
                else if (highNetWorthStatus.equals("0")) {
                    //查询原因
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                }
            }
        }

    }

    @Override
    public void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, final String isRealInvestor) {
        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : body.getData().getDictDynamicListList()) {
            if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                buffer.append(dynamic.getAuditComment()).append("\n");
            }
        }
        showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("isRealInvestor", isRealInvestor);
                intent.setClass(mActivity, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });
    }


    @Override
    public void onInvestmentState(String isjump, final String jumpurl, String returnMsg, final String isShare) {

        image_not_date.setClickable(true);
        image_not_date.setEnabled(true);
        image_not_date.setVisibility(View.VISIBLE);
        no_date_tv.setVisibility(View.GONE);
        transfer_view.setVisibility(View.GONE);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        int width = dm.widthPixels;

        ViewGroup.LayoutParams params = image_not_date.getLayoutParams();
        params.width = width - 50;
        params.height = width - 50;
        image_not_date.setLayoutParams(params);
        if (!returnMsg.equals("")) {
            ImageLoad.getImageLoad().LoadImage(this, returnMsg, image_not_date);
        } else {
            // 屏幕宽度（像素）
            ViewGroup.LayoutParams layoutParams = image_not_date.getLayoutParams();
            layoutParams.width = 640;
            layoutParams.height = 438;
            image_not_date.setVisibility(View.VISIBLE);
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
            image_not_date.setLayoutParams(layoutParams);
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.transfer_bg_kong, image_not_date);
        }

        //跳转
        if (isjump.equals("true")) {
            image_not_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //url跳转
                    if (jumpurl.contains("http")) {
                        ShareBean shareBean = new ShareBean();
                        shareBean.setFuncUrl(jumpurl);
                        shareBean.setIsShare(isShare);
                        startWebActivity(shareBean);
                    }
                    //身份认证
                    else if (jumpurl.equals("authentication")) {
                        presenter.getUserData(true);
                    }
                    //合格投资者
                    else if (jumpurl.equals("callPhoneQualifiedInvestor")) {

                        if (!Utils.isFastDoubleClick()) {
                            presenter.getUserData(false);
                        }
                    } else {
                        image_not_date.setClickable(false);
                        image_not_date.setEnabled(false);
                    }

                }
            });

        } else {
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
        }
        reset(isLoadMore);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCodes == requestCode && resultCode == RESULT_OK) {
            this.uuids = "";

            init();
        } else if (transfer_success_requestCodes == requestCode && resultCode == RESULT_OK) {
            this.uuids = "";

            init();
        } else if (requestCode == TransferConfiguration.TransferRequestCodeConfiguration.INSTANCE.getSELECT_TRANSFER_REQUEST_CODE() && resultCode == TransferConfiguration.TransferRequestCodeConfiguration.INSTANCE.getSELECT_TRANSFER_RESULT_CODE()) {

            String amount_new_select_condition_json = data.getStringExtra(TransferConfiguration.AMOUNT_NEW_SELECT_ENTITY);

            String time_new_select_condition_json = data.getStringExtra(TransferConfiguration.TIME_NEW_SELECT_ENTITY);

            //选择的新起投金额条件
            AmountNewSelectConditionEntity amountNewSelectConditionEntity = new Gson().fromJson(amount_new_select_condition_json, AmountNewSelectConditionEntity.class);

            amountSelectDialog.setSelectMap(amountNewSelectConditionEntity.getAmountSelectMap());

            //给下次跳转设置新的起投金额筛选条件
            transferSelectEntity.setAmountSelectMap(amountNewSelectConditionEntity.getAmountSelectMap());
            transferSelectEntity.setAmountSelectConditionName(amountNewSelectConditionEntity.getAmountSelectConditionName());
            transferSelectEntity.setAmountUUids(amountNewSelectConditionEntity.getAmountUuid());
            //界面返显
            amount_viewGroup.addChildView(amountNewSelectConditionEntity.getAmountSelectConditionName(), "请选择起投金额");

            //新的剩余期限筛选条件
            TimeNewSelectConditionEntity timeNewSelectConditionEntity = new Gson().fromJson(time_new_select_condition_json, TimeNewSelectConditionEntity.class);
            timeSelectDialog.setSelectMap(timeNewSelectConditionEntity.getTimeSelectMap());
            //给下次跳转设置新的剩余期限筛选条件
            transferSelectEntity.setTimeSelectMap(timeNewSelectConditionEntity.getTimeSelectMap());
            transferSelectEntity.setTimeSelectConditionName(timeNewSelectConditionEntity.getTimeSelectConditionName());
            transferSelectEntity.setTimeUUids(timeNewSelectConditionEntity.getTimeUuid());
            //界面返显
            surplusDayViewGroup.addChildView(timeNewSelectConditionEntity.getTimeSelectConditionName(), "请选择剩余期限");
            //设置新的uuid，需要判断当前有没有选择条件
            String uuidBuilder = amountNewSelectConditionEntity.getAmountUuid() +
                    timeNewSelectConditionEntity.getTimeUuid();
            transferSelectEntity.setAmountAndTimeUUid(uuidBuilder);

            String json = data.getStringExtra(TransferConfiguration.CONDITION_NAME);
            TransferIncomeNewSelectEntity selectStatus = new Gson().fromJson(json, TransferIncomeNewSelectEntity.class);
            String selectConditionName = selectStatus.getSelectConditionName();
            TransferIncomeSelectEntity transferIncomeSelectEntity = new TransferIncomeSelectEntity();
            List<String> selectNameList = new ArrayList<>();
            selectNameList.add(selectConditionName);
            transferIncomeSelectEntity.setSelectName(selectNameList);
            transferIncomeSelectEntity.setControlSort(selectStatus.getControlSort());
            List<String> uuidList = new ArrayList<>();
            uuidList.add(selectStatus.getSelectUUID());
            transferIncomeSelectEntity.setSelectUUID(uuidList);
            transferSelectEntity.setSelectEntity(transferIncomeSelectEntity);
            if (!selectConditionName.equals("")) {
                tvIncome.setText(selectConditionName);
                incomeDialog.setSelectStatusEntity(selectStatus);
            }


        }

    }


    private void reset(boolean isLoadMore) {
        if (isLoadMore) {
            transferLayout.finishLoadMore();
        } else {
            transferLayout.finishRefresh();
        }
        this.isLoadMore = false;
    }


    private void onLoadMoreData() {
        isLoadMore = true;
        pageIndex = pageIndex + 1;
        presenter.initTransfer(uuids, pageIndex);
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        init();
    }


    @Override
    protected void initWidget() {
        transferLayout = findViewById(R.id.smartLayout);
        transferLayout.setOnRefreshListener(this);
        transferLayout.setEnableLoadMore(false);
        image_not_date = findViewById(R.id.image_not_date);
        no_date_tv = findViewById(R.id.no_date_tv);
        transfer_view = findViewById(R.id.recyclerView);
        layout = findViewById(R.id.layout);
        btn_top = findViewById(R.id.btn_up);

        amount_viewGroup = findViewById(R.id.amount_viewGroup);


        amount_viewGroup.addDefaultView("请选择起投金额");


        surplusDayViewGroup = findViewById(R.id.surplus_day_view_group);

        surplusDayViewGroup.addDefaultView("请选择剩余期限");


        ll_income_layout = findViewById(R.id.ll_income_layout);

        tvIncome = findViewById(R.id.tv_income);


        View contentView = getLayoutInflater().inflate(R.layout.child_layout, null);
        contentView.findViewById(R.id.text_view).setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, MyHoldingActivity.class);
            startActivityForResult(intent, transfer_success_requestCodes);
        });
        layout.setContentView(contentView);
        transfer_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    int position = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
                    if (position > 1) {
                        btn_top.setVisibility(View.VISIBLE);
                        btn_top.setOnClickListener(view -> {
                            transfer_view.scrollToPosition(0);
                            btn_top.setVisibility(View.GONE);

                            init();
                        });
                    } else {
                        if (btn_top.getVisibility() == View.VISIBLE) {
                            btn_top.setVisibility(View.GONE);
                        }

                    }
                    int firstItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                    if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {

                    }
                }
            }

        });

        //清除所选条件
        findViewById(R.id.ll_clean_select_condition).setOnClickListener(v -> {


            cleanSelectCondition();

        });


        //确认，跳转带条件筛选的转让列表
        bt_select_condition = findViewById(R.id.bt_select_condition);
        bt_select_condition.setOnClickListener(v -> {

            StringBuilder uuidBuilder = new StringBuilder();

            String amountUuid = transferSelectEntity.getAmountUUids();
            String timeUuid = transferSelectEntity.getTimeUUids();
            String incomeUUid = "";
            //排序条件，如果TransferIncomeSelectEntity = null，表示当前没有选择排序条件，否个直接取第0个元素
            TransferIncomeSelectEntity entity = transferSelectEntity.getSelectEntity();

            List<String> uuidList = entity.getSelectUUID();
            if (!uuidList.isEmpty()) {
                incomeUUid = entity.getSelectUUID().get(0);
            } else {
                incomeUUid = "";
            }

            if (!amountUuid.equals("") && !timeUuid.equals("") && !incomeUUid.equals("")) {

                uuidBuilder.append(amountUuid).append("-");

                uuidBuilder.append(timeUuid).append("-");

                uuidBuilder.append(incomeUUid);
            } else if (amountUuid.equals("") && !timeUuid.equals("") && !incomeUUid.equals("")) {
                uuidBuilder.append("-");
                uuidBuilder.append(timeUuid).append("-");
                uuidBuilder.append(incomeUUid);

            } else if (amountUuid.equals("") && timeUuid.equals("") && !incomeUUid.equals("")) {
                uuidBuilder.append("-");
                uuidBuilder.append("-");
                uuidBuilder.append(incomeUUid);
            } else if (!amountUuid.equals("") && timeUuid.equals("") && !incomeUUid.equals("")) {
                uuidBuilder.append(amountUuid);
                uuidBuilder.append("-");
                uuidBuilder.append(incomeUUid);
            } else if (!amountUuid.equals("") && timeUuid.equals("") && incomeUUid.equals("")) {

                uuidBuilder.append(amountUuid);
                uuidBuilder.append("-");
                uuidBuilder.append("-");
            } else if (amountUuid.equals("") && !timeUuid.equals("") && incomeUUid.equals("")) {

                uuidBuilder.append("-");
                uuidBuilder.append(timeUuid);
                uuidBuilder.append("-");

            } else if (!amountUuid.equals("") && !timeUuid.equals("") && incomeUUid.equals("")) {

                uuidBuilder.append(amountUuid).append("-");
                uuidBuilder.append(timeUuid);
                uuidBuilder.append("-");
            } else if (amountUuid.equals("") && timeUuid.equals("") && incomeUUid.equals("")) {

                uuidBuilder.append("-");
                uuidBuilder.append("-");
                uuidBuilder.append("-");

            }

            Log.i("uuidBuilder", uuidBuilder.toString());

            //起投金额和剩余期限的uuid
            transferSelectEntity.setAmountAndTimeUUid(uuidBuilder.toString());


            presenter.onQueryTransferList(uuidBuilder.toString());


        });

        //查看全部转让产品
        findViewById(R.id.ll_all_transfer_product).setOnClickListener(v -> {
            cleanSelectCondition();
            TransferIncomeSelectEntity transferIncomeSelectEntity = transferSelectEntity.getSelectEntity();
            //代表没有选择起投金额，剩余期限，排序方式
            // if (transferIncomeSelectEntity.getSelectName().isEmpty() && transferSelectEntity.getAmountAndTimeUUid().equals("--")) {
            if (transferIncomeSelectEntity.getSelectName().isEmpty()) {
                transferIncomeSelectEntity = new TransferIncomeSelectEntity();
                transferIncomeSelectEntity.setControlSort("0");
                List<String> selectIncomeList = new ArrayList<>();
                selectIncomeList.add("剩余期限");
                transferIncomeSelectEntity.setSelectName(selectIncomeList);
                transferSelectEntity.setSelectEntity(transferIncomeSelectEntity);
            }

            Intent intent = new Intent(getActivity(), SelectConditionTransferListActivity.class);
            intent.putExtra(TRANSFER_ENTITY, new Gson().toJson(transferSelectEntity));
            startActivityForResult(intent, TransferConfiguration.TransferRequestCodeConfiguration.INSTANCE.getSELECT_TRANSFER_REQUEST_CODE());

        });
    }

    //清除所有筛选条件
    private void cleanSelectCondition() {
        tvIncome.setText("请选择排序方式");
        transferSelectEntity.setAmountAndTimeUUid("");
        transferSelectEntity.setAmountUUids("");
        transferSelectEntity.setTimeUUids("");
        transferSelectEntity.setAmountSelectConditionName(new ArrayList<>());
        transferSelectEntity.setTimeSelectConditionName(new ArrayList<>());
        transferSelectEntity.setAmountSelectMap(new HashMap<>());
        transferSelectEntity.setTimeSelectMap(new HashMap<>());
        transferSelectEntity.setSelectEntity(new TransferIncomeSelectEntity());
        amount_viewGroup.addDefaultView("请选择起投金额");
        surplusDayViewGroup.addDefaultView("请选择剩余期限");
        if (amountSelectDialog != null) {

            amountSelectDialog.cleanSelectCondition();
        }

        if (timeSelectDialog != null) {

            timeSelectDialog.cleanSelectCondition();
        }

        if (incomeDialog != null) {

            incomeDialog.cleanSelectCondition();
        }

    }


    @Override
    protected void onRefresh() {
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transfer_layout;
    }


    @Override
    public boolean isShowLoad() {
        return false;
    }
}

