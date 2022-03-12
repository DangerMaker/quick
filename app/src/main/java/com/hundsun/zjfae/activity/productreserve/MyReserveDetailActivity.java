package com.hundsun.zjfae.activity.productreserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter;
import com.hundsun.zjfae.activity.productreserve.presenter.MyReserveDetailPresenter;
import com.hundsun.zjfae.activity.productreserve.view.MyReserveDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.NetworkCheck;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;

/**
 * @Description:我的个性化预约详情
 * @Author: zhoujianyu
 * @Time: 2018/9/12 14:46
 */
public class MyReserveDetailActivity extends CommActivity implements MyReserveDetailView {
    private MyReserveDetailPresenter mPresenter;
    private String id, orderType;
    private RecyclerView attachment;
    private LinearLayout lin_pdf;
    private PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails data0;
    private ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail data1;
    private Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo;
    private LinearLayout lin_risk_level,ll_isWholeTransfer;
    private TextView tv_risk_level_name, tv_risk_level;
    private View lin_risk_level_view;


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_my_reserve_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_reserve_detail;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new MyReserveDetailPresenter(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("我的个性化预约详情");
        id = getIntent().getStringExtra("id");
        orderType = getIntent().getStringExtra("orderType");
        attachment = findViewById(R.id.attachment);
        lin_pdf = findViewById(R.id.lin_pdf);
        lin_risk_level = findViewById(R.id.lin_risk_level);
        tv_risk_level_name = findViewById(R.id.tv_risk_level_name);
        tv_risk_level = findViewById(R.id.tv_risk_level);
        lin_risk_level_view = findViewById(R.id.lin_risk_level_view);
        ll_isWholeTransfer = findViewById(R.id.ll_isWholeTransfer);
        if (!NetworkCheck.isNetworkAvailable(this)) {
            setNoNetViewVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    refreshData();
                }
            });
            return;
        }
        refreshData();
    }

    private void refreshData() {
        if (!NetworkCheck.isNetworkAvailable(this)) {
            showToast("网络异常");
            return;
        } else {
            setNoNetViewGone();
            mPresenter.allReserveDetailRequest(orderType, id);
        }
    }

    //特约接口返回处理
    public void loadDataType0(final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails data) {
        final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail bean = data.getTaProductFinanceDetail();
        //产品状态
        ((TextView) findViewById(R.id.tv_product_status)).setText(bean.getProductStatusStr());
        //产品类型
        ((TextView) findViewById(R.id.tv_product_type)).setText(bean.getSubjectTypeName());
        //起息日
        ((TextView) findViewById(R.id.tv_manage_start)).setText(bean.getManageStartDate());
        //到期日
        ((TextView) findViewById(R.id.tv_manage_end)).setText(bean.getManageEndDate());
        //收益类型
        ((TextView) findViewById(R.id.tv_income_type)).setText(bean.getIncomeTypeName());

        //产品标题
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(bean.getProductName());
        Utils.setTextViewGravity(tv_title);
        //起购金额隐藏
        findViewById(R.id.lin_money_buy).setVisibility(View.GONE);
        findViewById(R.id.line_money_buy).setVisibility(View.GONE);
        //递增金额隐藏
        findViewById(R.id.lin_money_add).setVisibility(View.GONE);
        findViewById(R.id.line_money_add).setVisibility(View.GONE);
        //预期年化收益率
        ((TextView) findViewById(R.id.tv_percent)).setText(bean.getExpectedMaxAnnualRate() + "%");
        //预约开始时间隐藏
        findViewById(R.id.lin_reserve_time_start).setVisibility(View.GONE);
        findViewById(R.id.line_reserve_time_start).setVisibility(View.GONE);
        //预约结束时间隐藏
        findViewById(R.id.lin_reserve_time_end).setVisibility(View.GONE);
        findViewById(R.id.line_reserve_time_end).setVisibility(View.GONE);
        //募集开始时间
        ((TextView) findViewById(R.id.tv_raise_time_start)).setText(bean.getBuyStartDate());
        //募集结束时间
        ((TextView) findViewById(R.id.tv_raise_time_end)).setText(bean.getBuyEndDate());
        //是否允许转让
        ((TextView) findViewById(R.id.tv_transfer)).setText(bean.getIsTransferStr());
        //付息方式
        ((TextView) findViewById(R.id.tv_payment_way)).setText(bean.getPayStyle());
        //付息频率
        if (bean.getPayFrequency() == null || bean.getPayFrequency().equals("")) {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText("--");
        } else {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText(bean.getPayFrequency());
        }
        //风险级别
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelValue())) {
            tv_risk_level.setText(bean.getRiskLevelLabelValue());
            lin_risk_level.setVisibility(View.VISIBLE);
            lin_risk_level_view.setVisibility(View.VISIBLE);
        } else {
            lin_risk_level_view.setVisibility(View.GONE);
            lin_risk_level.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelName())) {
            tv_risk_level_name.setText(bean.getRiskLevelLabelName());
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelUrl())) {
            tv_risk_level.setTextColor(getResources().getColor(R.color.blue));
            tv_risk_level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = bean.getAccreditedBuyIs();
                    mPresenter.attachmentUserInfo(accreditedBuyIs, null, data.getRatingTitle(), bean.getRiskLevelLabelUrl(), false);
                }
            });
        }
        //计息方式
        ((TextView) findViewById(R.id.tv_interest_way)).setText(bean.getYearDay());

        findViewById(R.id.NestedScrollView).setVisibility(View.VISIBLE);

        String isWholeTransfer = bean.getIsWholeTransfer();
        //是否是整体转让产品
        if ("1".equals(isWholeTransfer)){
            ll_isWholeTransfer.setVisibility(View.VISIBLE);
        }
        else{
            ll_isWholeTransfer.setVisibility(View.GONE);
        }

    }

    //非特约接口返回
    public void loadDataType1(final ProductOrderInfoDetailPB.PBIFE_trade_queryProductOrderInfoDetail data) {
        final ProductOrderInfoDetailPB.PBIFE_trade_queryProductOrderInfoDetail.TaProductOrderInfo bean = data.getTaProductOrderInfo();
        //产品状态隐藏
        findViewById(R.id.lin_product_status).setVisibility(View.GONE);
        findViewById(R.id.line_product_status).setVisibility(View.GONE);
        //产品类型隐藏
        findViewById(R.id.lin_product_type).setVisibility(View.GONE);
        findViewById(R.id.line_product_type).setVisibility(View.GONE);
        //起息日隐藏
        findViewById(R.id.lin_manage_start).setVisibility(View.GONE);
        findViewById(R.id.line_manage_start).setVisibility(View.GONE);
        //到期日隐藏
        findViewById(R.id.lin_manage_end).setVisibility(View.GONE);
        findViewById(R.id.line_manage_end).setVisibility(View.GONE);
        //收益类型隐藏
        findViewById(R.id.lin_income_type).setVisibility(View.GONE);
        findViewById(R.id.line_income_type).setVisibility(View.GONE);
        //产品标题
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(bean.getProductName());
        Utils.setTextViewGravity(tv_title);
        //起购金额
        ((TextView) findViewById(R.id.tv_money_buy)).setText(bean.getBuyerSmallestAmount() + "元");
        //递增金额
        ((TextView) findViewById(R.id.tv_money_add)).setText(bean.getUnActualPriceIncreases() + "元");
        //预期年化收益率
        ((TextView) findViewById(R.id.tv_percent)).setText(bean.getExpectedMaxAnnualRate() + "%");
        //预约开始时间
        ((TextView) findViewById(R.id.tv_reserve_time_start)).setText(bean.getOrderStartDate());
        //预约结束时间
        ((TextView) findViewById(R.id.tv_reserve_time_end)).setText(bean.getOrderEndDate());
        //募集开始时间
        ((TextView) findViewById(R.id.tv_raise_time_start)).setText(bean.getBuyStartDate());
        //募集结束时间
        ((TextView) findViewById(R.id.tv_raise_time_end)).setText(bean.getBuyEndDate());
        //是否允许转让
        ((TextView) findViewById(R.id.tv_transfer)).setText(bean.getIsTransferStr());
        //付息方式
        ((TextView) findViewById(R.id.tv_payment_way)).setText(bean.getPayStyle());
        //付息频率
        if (bean.getPayFrequency() == null || bean.getPayFrequency().equals("")) {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText("--");
        } else {
            ((TextView) findViewById(R.id.tv_payment_frequency)).setText(bean.getPayFrequency());
        }
        //风险级别
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelValue())) {
            tv_risk_level.setText(bean.getRiskLevelLabelValue());
            lin_risk_level.setVisibility(View.VISIBLE);
        } else {
            lin_risk_level.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelName())) {
            tv_risk_level_name.setText(bean.getRiskLevelLabelName());
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelUrl())) {
            tv_risk_level.setTextColor(getResources().getColor(R.color.blue));
            tv_risk_level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = bean.getAccreditedBuyIs();
                    mPresenter.attachmentUserInfo(accreditedBuyIs, null, data.getRatingTitle(), bean.getRiskLevelLabelUrl(), false);
                }
            });
        }
        //计息方式
        ((TextView) findViewById(R.id.tv_interest_way)).setText(bean.getYearDay());

        findViewById(R.id.NestedScrollView).setVisibility(View.VISIBLE);


    }

    public void productAttachmen() {
        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentInfo.getData().getTaProductAttachmentListList();
        if (list == null || list.size() <= 0) {
            lin_pdf.setVisibility(View.GONE);
        } else {
            lin_pdf.setVisibility(View.VISIBLE);
        }
        String accreditedBuyIs = "";
        if (orderType.equals("0")) {
            accreditedBuyIs = data0.getData().getTaProductFinanceDetail().getAccreditedBuyIs();
        } else if (orderType.equals("1")) {
            accreditedBuyIs = data1.getData().getTaProductOrderInfo().getAccreditedBuyIs();
        }
        AttachmentAdapter adapter = new AttachmentAdapter(this, list);
        final String finalAccreditedBuyIs = accreditedBuyIs;
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {
                mPresenter.attachmentUserInfo(finalAccreditedBuyIs, list.get(position), "", "", true);
            }
        });
        attachment.setLayoutManager(new LinearLayoutManager(this));
        attachment.setAdapter(adapter);
        attachment.addItemDecoration(new DividerItemDecorations());
    }

    @Override
    public void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment) {
        final String isAccreditedInvestor = userDetailInfo.getData().getIsAccreditedInvestor();
        final String isRealInvestor = userDetailInfo.getData().getIsRealInvestor();
        final String highNetWorthStatus = userDetailInfo.getData().getHighNetWorthStatus();
        String userType = userDetailInfo.getData().getUserType();
        if (accreditedBuyIs.equals("1") && !isAccreditedInvestor.equals("1")) {

            if (userDetailInfo.getData().getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(MyReserveDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else if (!isAccreditedInvestor.equals("1")) {
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
                    mPresenter.requestInvestorStatus(isRealInvestor);
                } else {
                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                    //showUserLevelDialog("000", isRealInvestor);
//                    Intent intent = new Intent(MyReserveDetailActivity.this, EligibleInvestGuideActivity.class);
//                    intent.putExtra("type", "000");
//                    intent.putExtra("isRealInvestor", isRealInvestor);
//                    baseStartActivity(intent);
                }
            } else {
                if (isAttachment) {
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    if (orderType.equals("0")) {
                        //特约
                        openAttachment(id, title, "9");
                    } else {
                        openAttachment(id, title, "12");
                    }
                } else {
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }

        } else {
            if (!userDetailInfo.getData().getVerifyName().equals("1") && userType.equals("personal")) {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(MyReserveDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    if (orderType.equals("0")) {
                        //特约
                        openAttachment(id, title, "9");
                    } else {
                        openAttachment(id, title, "12");
                    }
                } else {
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }
        }
    }

    @Override
    public void getAllData(String oderType, PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails data0, ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail data1, Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo, UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        if (data0 != null) {
            this.data0 = data0;
            if (data0.getReturnCode().equals("0225")) {
                //#21918特殊名单用户限制查询产品列表和产品详情 直接退出当前界面
                showDialog(data0.getReturnMsg(), "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                return;
            }
        }
        if (data1 != null) {
            this.data1 = data1;
            if (data1.getReturnCode().equals("0225")) {
                //#21918特殊名单用户限制查询产品列表和产品详情 直接退出当前界面
                showDialog(data1.getReturnMsg(), "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                return;
            }
        }

        if (orderType.equals("0")) {
            if (data0 != null) {
                loadDataType0(data0.getData());
            }
        } else if (orderType.equals("1")) {
            if (data1 != null) {
                loadDataType1(data1.getData());
            }
        }
        if (attachmentInfo != null) {
            this.attachmentInfo = attachmentInfo;
            productAttachmen();
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
                intent.setClass(MyReserveDetailActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });
    }
}
