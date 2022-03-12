package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.moneymanagement.presenter.SubscriptionDetailPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.SubscriptionDetailView;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.activity.product.util.CancelTimerUtils;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public class SubscriptionDetailActivity extends CommActivity<SubscriptionDetailPresenter> implements View.OnClickListener, SubscriptionDetailView {
    private TextView mTitleTv;
    private TextView mStateTv;
    private TextView mTypeTv;
    private TextView mModeOfInterestPaymentTv;
    private TextView mStartTimeRaise;
    private TextView mEndTimeRaise;
    private TextView mRateOfReturnTv;
    private TextView mTransferTv;
    private TextView mDayOfInterestTv;
    private TextView mDueDateTv;
    private TextView mSubscribedAmountTv;
    private TextView mSubscribedStateTv;

    //撤单按钮
    private TextView tv_cancelFlag;

    //卡券类型
    private TextView tv_key_value;

    private ImageView doubt;

    private LinearLayout lin_risk_level, raise_start_time_layout, raise_end_time_layout, day_of_interest_layout;
    private TextView tv_risk_level_name, tv_risk_level;
    private View lin_risk_level_view;
    private boolean cancelFlag;
    private long countDown;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_subscription_detail;
    }

    @Override
    protected SubscriptionDetailPresenter createPresenter() {
        return new SubscriptionDetailPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_subscription_detail);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected void initData() {

        String productCode = getIntent().getStringExtra("productCode");

        presenter.setProductCode(productCode);

        String delegationCode = getIntent().getStringExtra("delegationCode");

        presenter.setDelegationCode(delegationCode);

        String special = getIntent().getStringExtra("special");

        boolean spvFlag = getIntent().getBooleanExtra("spvFlag", false);

        cancelFlag = getIntent().getBooleanExtra("cancelFlag", false);

        countDown = getIntent().getLongExtra("countDown", 0L);


        if (special.equals("FALSE") || special.equals("false")) {

            findViewById(R.id.bank_deal).setVisibility(View.VISIBLE);
            findViewById(R.id.bank_deal).setOnClickListener(this);

        } else {

            findViewById(R.id.bank_deal).setVisibility(View.GONE);
        }

        /**
         * 是Spv产品
         * */
        if (spvFlag) {
            raise_start_time_layout.setVisibility(View.GONE);

            raise_end_time_layout.setVisibility(View.GONE);

            day_of_interest_layout.setVisibility(View.GONE);

            //右上角详情
            findViewById(R.id.bank_deal).setVisibility(View.GONE);
        }
        /**
         * 是否可撤单
         * */
        if (cancelFlag) {
            tv_cancelFlag.setVisibility(View.VISIBLE);
            CancelTimerUtils.setCountTime(countDown);
            CancelTimerUtils.setExecutorListener(new CancelTimerUtils.CountDownListener() {
                @Override
                public void onSuccess() {
                    tv_cancelFlag.setVisibility(View.GONE);
                    presenter.getProductDetail();
                }

                @Override
                public void onNoClick(long countDown, String tv) {
                    tv_cancelFlag.setText("撤单 （" + tv + "）");
                }
            });
            CancelTimerUtils.startTime();


        } else {
            tv_cancelFlag.setVisibility(View.GONE);
        }


        String kqType = getIntent().getStringExtra("kqType");

        String kqValue = getIntent().getStringExtra("kqValue");

        if (null != kqType && null != kqValue) {

            if (kqType.equals("A")) {
                doubt.setVisibility(View.VISIBLE);
                tv_key_value.setText("(+" + kqValue + "%)");
                doubt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String kqAddRateBj = getIntent().getStringExtra("kqAddRateBj");

                        String buffer = "加息" + kqValue +
                                "%" +
                                "针对" +
                                MoneyUtil.formatMoney2(kqAddRateBj) +
                                "元本金";
                        showDialog(buffer);

                    }
                });
            } else {

                doubt.setVisibility(View.GONE);
            }


        }


        presenter.getProductDetail();
    }

    @Override
    public void initView() {
        setTitle("交易详情");
        mTopDefineCancel = true;
        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mStateTv = (TextView) findViewById(R.id.tv_state);
        mTypeTv = (TextView) findViewById(R.id.tv_type);
        mModeOfInterestPaymentTv = (TextView) findViewById(R.id.tv_mode_of_interest_payment);
        mStartTimeRaise = (TextView) findViewById(R.id.raise_start_time);
        mEndTimeRaise = (TextView) findViewById(R.id.raise_end_time);
        mRateOfReturnTv = (TextView) findViewById(R.id.tv_rate_of_return);
        mTransferTv = (TextView) findViewById(R.id.tv_transfer);
        mDayOfInterestTv = (TextView) findViewById(R.id.tv_day_of_interest);
        mDueDateTv = (TextView) findViewById(R.id.tv_due_date);
        //mIncomeTypeTv = (Texttv_subscribed_stateView) findViewById(R.id.tv_income_type);
        mSubscribedAmountTv = (TextView) findViewById(R.id.tv_subscribed_amount);
        mSubscribedStateTv = (TextView) findViewById(R.id.tv_subscribed_state);
        lin_risk_level = findViewById(R.id.lin_risk_level);
        tv_risk_level_name = findViewById(R.id.tv_risk_level_name);
        tv_risk_level = findViewById(R.id.tv_risk_level);
        lin_risk_level_view = findViewById(R.id.lin_risk_level_view);
        tv_cancelFlag = findViewById(R.id.tv_cancelFlag);
        tv_cancelFlag.setOnClickListener(this);
        raise_start_time_layout = findViewById(R.id.raise_start_time_layout);
        raise_end_time_layout = findViewById(R.id.raise_end_time_layout);
        day_of_interest_layout = findViewById(R.id.day_of_interest_layout);

        tv_key_value = findViewById(R.id.tv_key_value);

        doubt = findViewById(R.id.doubt);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bank_deal:
                Intent intent = new Intent();
                intent.putExtra("productCode", getIntent().getSerializableExtra("productCode"));
                intent.putExtra("sellingStatus", "1");
                intent.putExtra("type", "SubscriptionDetailActivity");
                intent.setClass(this, ProductCodeActivity.class);
                baseStartActivity(intent);
                break;
            //撤单按钮
            case R.id.tv_cancelFlag:

                presenter.cancelPre();

                break;
        }
    }

    @Override
    public void setDetail(final PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails bean) {

        if (bean.getReturnCode().equals("0225")) {
            //#21918特殊名单用户限制查询产品列表和产品详情 直接退出当前界面
            showDialog(bean.getReturnMsg(), "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            return;
        }
        final PBIFEPrdqueryPrdQueryProductDetails.PBIFE_prdquery_prdQueryProductDetails.TaProductFinanceDetail product = bean.getData().getTaProductFinanceDetail();
        mTitleTv.setText(product.getProductName());
        Utils.setTextViewGravity(mTitleTv);
        mStateTv.setText(product.getProductStatusStr());
        mTypeTv.setText(product.getSubjectTypeName());
        mModeOfInterestPaymentTv.setText(product.getPayStyle());
        mStartTimeRaise.setText(product.getBuyStartDate());
        mEndTimeRaise.setText(product.getBuyEndDate());
        mRateOfReturnTv.setText(product.getExpectedMaxAnnualRate() + "%");
        mTransferTv.setText(product.getIsTransferStr());
        mDayOfInterestTv.setText(product.getManageStartDate());
        mDueDateTv.setText(product.getManageEndDate());
        //风险级别
        if (StringUtils.isNotBlank(product.getRiskLevelLabelValue())) {
            tv_risk_level.setText(product.getRiskLevelLabelValue());
            lin_risk_level.setVisibility(View.VISIBLE);
            lin_risk_level_view.setVisibility(View.VISIBLE);
        } else {
            lin_risk_level_view.setVisibility(View.GONE);
            lin_risk_level.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(product.getRiskLevelLabelName())) {
            tv_risk_level_name.setText(product.getRiskLevelLabelName());
        }
        if (StringUtils.isNotBlank(product.getRiskLevelLabelUrl())) {
            tv_risk_level.setTextColor(getResources().getColor(R.color.blue));
            tv_risk_level.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String accreditedBuyIs = product.getAccreditedBuyIs();
                    presenter.attachmentUserInfo(accreditedBuyIs, null, bean.getData().getRatingTitle(), product.getRiskLevelLabelUrl(), false);
                }
            });
        }
        //mIncomeTypeTv.setText(product.getIncomeTypeName());
        mSubscribedAmountTv.setText(getIntent().getStringExtra("delegateAmount") + "元");
        mSubscribedStateTv.setText(getIntent().getStringExtra("delegateStatusName"));


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
                intent.setClass(SubscriptionDetailActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
                finish();
            }
        });
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
                        baseStartActivity(SubscriptionDetailActivity.this, AddBankActivity.class);
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
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {

                    if (userType.equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }
                    //showUserLevelDialog("000",isRealInvestor);

//                    Intent intent = new Intent(SubscriptionDetailActivity.this, EligibleInvestGuideActivity.class);
//                    intent.putExtra("type", "000");
//                    intent.putExtra("isRealInvestor", isRealInvestor);
//                    baseStartActivity(intent);

                }
            } else {
                if (isAttachment) {
                    //附件单击

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
                        baseStartActivity(SubscriptionDetailActivity.this, AddBankActivity.class);
                    }
                });
            } else {
                if (isAttachment) {
                    //附件单击

                } else {
                    //信用评级
                    openAttachment(riskLevelLabelName, riskLevelLabelUrl);
                }
            }
        }
    }

    /**
     * 撤单预检查接口请求回调
     *
     * @param code         请求返回code码
     * @param message      错误提示
     * @param data_message 弹框详情
     * @return
     * @method
     * @date: 2020/11/1 15:11
     * @author: moran
     */
    @Override
    public void cancelPre(String code, String message, String data_message) {

        if (code.equals("0000")) {

            showDialog(data_message.replace("<br/>", "\n"), "确认", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    presenter.cancel();

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

        tv_cancelFlag.setVisibility(View.GONE);

        showDialog(msg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

    }


    @Override
    protected void topDefineCancel() {

        boolean cancelFlag = getIntent().getBooleanExtra("cancelFlag", false);

        if (cancelFlag && tv_cancelFlag.getVisibility() == View.GONE) {

            setResult(RESULT_OK);
        } else {

            setResult(RESULT_CANCELED);
        }
        finish();
    }


}
