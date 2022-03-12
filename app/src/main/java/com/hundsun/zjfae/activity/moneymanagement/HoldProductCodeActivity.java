package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity;
import com.hundsun.zjfae.activity.moneymanagement.adapter.EntrustedDetailsAdapter;
import com.hundsun.zjfae.activity.moneymanagement.presenter.HoldProductCodePresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.HoldProductCodeView;
import com.hundsun.zjfae.activity.product.ProductPlayActivity;
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.view.dialog.CustomDialog;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.EntrustedDetails;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

//交易产品详情无法购买只是展示一下
public class HoldProductCodeActivity extends CommActivity<HoldProductCodePresenter> implements HoldProductCodeView, View.OnClickListener {


    private String productCode;
    private TextView productName, expectedMaxAnnualRate, deadline, buyerSmallestAmount, riskLevel, buyTotalAmount, buyRemainAmount, buyStartDate, buyEndDate, manageStartDate, manageEndDate, leastHoldAmount, tradeLeastHoldDay, payStyle, canBuyNum, isTransferStr, remark, tradeStartDate_start_time, tradeStartDate_end_time, entrustedTips;
    private View riskLevel_view;

    private TransferDetailPlay playInfo;


    private LinearLayout remark_layout, lin_entrustedDetails;
    private NestedScrollView product_scroll;
    private RecyclerView attachment, entrustedDetails;
    private LinearLayout lin_transfer_start, lin_transfer_end, ll_raise_start_time, ll_raise_end_time;//转让开始时间和转让结束时间

    @Override
    public void productCheckState(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec) {
        String returnCode = subscribeProductSec.getReturnCode();
        String returnMsg = subscribeProductSec.getReturnMsg();
        if (returnCode.equals("0000")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("product_info", playInfo);
            intent.putExtra("product_bundle", bundle);
            intent.setClass(HoldProductCodeActivity.this, ProductPlayActivity.class);
            baseStartActivity(intent);
            finish();
        }

        //购买金额小于当前购买金额
        else if (returnCode.equals("3310")) {
            showDialog(returnMsg);
        } else if (returnCode.equals("3320")) {
            CustomDialog.Builder builder = new CustomDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(returnMsg);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("去风评", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    baseStartActivity(HoldProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
            builder.create().show();
        } else {
            showDialog(returnMsg);
        }
    }

    @Override
    public void getProductBean(HoldProductBean productBean) {

        //产品详情
        PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productInfo =
                productBean.getProductDetails();
        if (productInfo != null) {
            productDetails(productInfo);
        }


        //用户信息
        UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo = productBean.getUserDetailInfo();
        if (userInfo != null && productInfo != null) {
            getUserDate(userInfo, productInfo);
        }

        if (productInfo != null && productBean.getAttachment() != null) {
            //附件列表
            productAttachment(productBean);
        }

        if (productBean.getEntrustedDetails() != null) {
            //受托管理报告
            productEntrustedDetails(productBean, productCode);
        }
    }

    @Override
    public void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor) {

    }

    @Override
    public void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

    }


    //产品详情
    public void productDetails(PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productInfo) {
        if (productInfo.getReturnCode().equals("0000")) {
            PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail taProductFinanceDetail =
                    productInfo.getData().getTaProductFinanceDetail();
            setInfo(productInfo.getData());

        } else if (productInfo.getReturnCode().equals("0225")) {
            //#21918特殊名单用户限制查询产品列表和产品详情 直接退出当前界面
            showDialog(productInfo.getReturnMsg(), "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            return;
        } else {
            showDialog(productInfo.getReturnMsg(), "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

    }

    //获取用户信息
    public void getUserDate(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userInfo, final PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productData) {

        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userInfo.getData();
        String isRealInvestor = userDetailInfo.getIsRealInvestor();
        String userType = userDetailInfo.getUserType();

        CCLog.e("65", userDetailInfo.getIsOlderThan65());
        //是否是65周岁
        if (userDetailInfo.getIsOlderThan65().equals("true")) {
            //65周岁

        }
        if (userDetailInfo.getIsFundPasswordSet().equals("false")) {
            showDialog("您还尚未设置交易密码哦", "设置交易密码", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去设置交易密码
                    dialog.dismiss();
                    baseStartActivity(HoldProductCodeActivity.this, FirstPlayPassWordActivity.class);
                }
            });
        } else if (!userDetailInfo.getVerifyName().equals("1") && userType.equals("personal")) {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(HoldProductCodeActivity.this, AddBankActivity.class);
                }
            });
        }
        //合格投资者
        else if (productData.getData().getTaProductFinanceDetail().getAccreditedBuyIs().equals("1")
                && !userDetailInfo.getIsAccreditedInvestor().equals("1")) {
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {
                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(HoldProductCodeActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                // 0审核不通过
                if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                    presenter.requestInvestorStatus(isRealInvestor);
                } else if (!userDetailInfo.getHighNetWorthStatus().equals("-1")) {


                    showUserLevelDialog("000", isRealInvestor);


                }
            }

        } else if (userDetailInfo.getIsRiskTest().equals("false") && userType.equals("personal")) {
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    baseStartActivity(HoldProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (userDetailInfo.getIsRiskExpired().equals("true") && userType.equals("personal")) {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去风评
                    baseStartActivity(HoldProductCodeActivity.this, RiskAssessmentActivity.class);
                }
            });
        } else if (Integer.parseInt(!userDetailInfo.getRiskAssessment().equals("") ? userDetailInfo.getRiskAssessment() : "0") < 31) {

        }


    }


    //附件列表
    public void productAttachment(HoldProductBean productBean) {
        //产品详情
        final PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productInfo =
                productBean.getProductDetails();
        final String accreditedBuyIs = productInfo.getData().getTaProductFinanceDetail().getAccreditedBuyIs();

        //附件列表
        Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo = productBean.getAttachment();


        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentInfo.getData().getTaProductAttachmentListList();

        if (list == null || list.isEmpty()) {
            return;
        }
        AttachmentAdapter adapter = new AttachmentAdapter(HoldProductCodeActivity.this, list);
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {
                presenter.attachmentUserInfo(accreditedBuyIs, list.get(position), "", "", true);
            }
        });
        attachment.setLayoutManager(new LinearLayoutManager(HoldProductCodeActivity.this));
        attachment.setAdapter(adapter);
//        attachment.addItemDecoration(new DividerItemDecorations());
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
                        baseStartActivity(HoldProductCodeActivity.this, AddBankActivity.class);
                    }
                });
            } else if (!isAccreditedInvestor.equals("1")) {
                // 合格投资者审核中
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。。";
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

                    showUserLevelDialog("000", isRealInvestor);

//                    Intent intent = new Intent(HoldProductCodeActivity.this, EligibleInvestGuideActivity.class);
//                    intent.putExtra("type", "000");
//                    intent.putExtra("isRealInvestor", isRealInvestor);
//                    baseStartActivity(intent);
                }
            } else {
                if (isAttachment) {
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "11");
                } else {
                    //信用评级
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
                        baseStartActivity(HoldProductCodeActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //附件单击
                    String id = attachmentList.getId();
                    String title = attachmentList.getTitle();
                    openAttachment(id, title, "11");
                } else {
                    //信用评级
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }
        }

    }


    //受托管理报告
    public void productEntrustedDetails(HoldProductBean productBean, final String productCode) {
        if (productBean.getEntrustedDetails().getData().getTcProductSeriesListList().size() > 0) {
            final List<EntrustedDetails.PBIFE_prdquery_getQueryEntrustedDetails.TcProductSeriesList>
                    list = productBean.getEntrustedDetails().getData().getTcProductSeriesListList();
            EntrustedDetailsAdapter adapter = new EntrustedDetailsAdapter(HoldProductCodeActivity.this, list);
            adapter.setOnItemClickListener(new EntrustedDetailsAdapter.OnItemListener() {
                @Override
                public void onItemClick(int position) {
                    //受托管理报告单击
                    String id = list.get(position).getEntrustedPath();
                    String title = list.get(position).getEntrustedReport();
                    openAttachment(id, title, "8", productCode);
                }
            });
            entrustedDetails.setLayoutManager(new LinearLayoutManager(HoldProductCodeActivity.this));
            entrustedDetails.setAdapter(adapter);
//            entrustedDetails.addItemDecoration(new DividerItemDecorations());
            entrustedTips.setText(Html.fromHtml(productBean.getNotice().getData().getNotice().getNoticeContent()));
            lin_entrustedDetails.setVisibility(View.VISIBLE);
        } else {
            lin_entrustedDetails.setVisibility(View.GONE);
        }
    }


    @Override
    protected HoldProductCodePresenter createPresenter() {

        return new HoldProductCodePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hold_product_code;
    }

    @Override
    public void initView() {
        setTitle("产品详情");
        productCode = getIntent().getStringExtra("productCode");
        productName = findViewById(R.id.productName);
        expectedMaxAnnualRate = findViewById(R.id.expectedMaxAnnualRate);
        deadline = findViewById(R.id.deadline);
        buyerSmallestAmount = findViewById(R.id.buyerSmallestAmount);
        riskLevel = findViewById(R.id.riskLevel);
        riskLevel_view = findViewById(R.id.riskLevel_view);
        buyTotalAmount = findViewById(R.id.buyTotalAmount);
        buyRemainAmount = findViewById(R.id.buyRemainAmount);
        buyStartDate = findViewById(R.id.buyStartDate);
        buyEndDate = findViewById(R.id.buyEndDate);
        manageStartDate = findViewById(R.id.manageStartDate);
        manageEndDate = findViewById(R.id.manageEndDate);
        leastHoldAmount = findViewById(R.id.leastHoldAmount);
        tradeLeastHoldDay = findViewById(R.id.tradeLeastHoldDay);
        payStyle = findViewById(R.id.payStyle);
        canBuyNum = findViewById(R.id.canBuyNum);
        isTransferStr = findViewById(R.id.isTransferStr);
        remark = findViewById(R.id.remark);
        remark_layout = findViewById(R.id.remark_layout);
        tradeStartDate_start_time = findViewById(R.id.tradeStartDate_start_time);
        tradeStartDate_end_time = findViewById(R.id.tradeStartDate_end_time);
        product_scroll = findViewById(R.id.product_scroll);
        findViewById(R.id.doubt).setOnClickListener(this);
        attachment = findViewById(R.id.attachment);
        lin_entrustedDetails = findViewById(R.id.lin_entrustedDetails);
        entrustedDetails = findViewById(R.id.entrustedDetails);
        entrustedTips = findViewById(R.id.entrustedTips);
        lin_transfer_start = findViewById(R.id.lin_transfer_start);
        lin_transfer_end = findViewById(R.id.lin_transfer_end);
        ll_raise_start_time = findViewById(R.id.ll_raise_start_time);
        ll_raise_end_time = findViewById(R.id.ll_raise_end_time);

    }

    @Override
    protected void initData() {
        String productId = getIntent().getStringExtra("id");
        presenter.allProductRequest(productCode, productId);
    }

    private void setInfo(final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails data) {
        final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail bean = data.getTaProductFinanceDetail();
        if (bean.getComparison().equals("2")) {
            findViewById(R.id.doubt).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.doubt).setVisibility(View.GONE);
        }

//        //是否是svp产品
//        final String interestByTimeRange = bean.getInterestByTimeRange();
//
//        if (interestByTimeRange.equals("1")){
//
//            ll_raise_start_time.setVisibility(View.GONE);
//            ll_raise_end_time.setVisibility(View.GONE);
//        }
//        else {
//            ll_raise_start_time.setVisibility(View.VISIBLE);
//            ll_raise_end_time.setVisibility(View.VISIBLE);
//        }

        playInfo = new TransferDetailPlay();
        playInfo.setSerialNoStr(bean.getSerialNoStr());
        playInfo.setProductCode(bean.getProductCode());
        productName.setText(bean.getProductName());
        playInfo.setProductName(bean.getProductName());
        playInfo.setExpectedMaxAnnualRate(bean.getExpectedMaxAnnualRate());
        playInfo.setDeadline(bean.getDeadline());
        playInfo.setBuyerSmallestAmount(bean.getBuyerSmallestAmount());
        playInfo.setBuyRemainAmount(bean.getBuyRemainAmount());
        expectedMaxAnnualRate.setText(bean.getExpectedMaxAnnualRate() + "%");
        deadline.setText(bean.getDeadline());
        buyerSmallestAmount.setText(bean.getBuyerSmallestAmount());
        //设置风险等级数据
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelValue())) {
            riskLevel.setText(bean.getRiskLevelLabelValue());
            riskLevel.setVisibility(View.VISIBLE);
            riskLevel_view.setVisibility(View.VISIBLE);
        } else {
            riskLevel.setVisibility(View.GONE);
            riskLevel_view.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(bean.getRiskLevelLabelUrl())) {
            riskLevel.setTextColor(getResources().getColor(R.color.blue));
            riskLevel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = bean.getAccreditedBuyIs();
                    presenter.attachmentUserInfo(accreditedBuyIs, null, data.getRatingTitle(), bean.getRiskLevelLabelUrl(), false);
                }
            });
        }
        buyTotalAmount.setText(bean.getBuyTotalAmount() + "元");
        buyRemainAmount.setText(bean.getBuyRemainAmount() + "元");
        isTransferStr.setText(bean.getIsTransferStr());
        buyStartDate.setText(bean.getBuyStartDate());
        buyEndDate.setText(bean.getBuyEndDate());
        manageStartDate.setText(bean.getManageStartDate());
        manageEndDate.setText(bean.getManageEndDate());
        leastHoldAmount.setText(bean.getUnActualPriceIncreases() + "元");
        if (bean.getIsTransferStr().equals("可转让")) {
            tradeLeastHoldDay.setText(bean.getTradeLeastHoldDay() + "个交易日");
        } else {
            tradeLeastHoldDay.setText("--");
        }
        payStyle.setText(bean.getPayStyle());
        canBuyNum.setText(bean.getCanBuyNum());
        tradeStartDate_start_time.setText(bean.getTradeStartDate());
        tradeStartDate_end_time.setText(bean.getTradeEndDate());
        if (bean.getIsTransferStr().equals("可转让")) {
            lin_transfer_start.setVisibility(View.VISIBLE);
            lin_transfer_end.setVisibility(View.VISIBLE);

        } else {
            lin_transfer_start.setVisibility(View.GONE);
            lin_transfer_end.setVisibility(View.GONE);
        }
        if (bean.getRemark() != null && !bean.getRemark().equals("")) {
            remark_layout.setVisibility(View.VISIBLE);
            remark.setText(bean.getRemark());
        }
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.product_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doubt:
                showDialog("提前售罄即于售罄当日的下一工作日起息");
                break;
        }
    }

}
