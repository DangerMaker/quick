package com.hundsun.zjfae.activity.moneymanagement;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.presenter.SellPresenter;
import com.hundsun.zjfae.activity.moneymanagement.view.SellView;
import com.hundsun.zjfae.activity.product.adapter.AttachmentAdapter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.DividerItemDecorations;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.view.SellAmountDialog;
import com.hundsun.zjfae.common.view.dialog.SendSmsCodeDialog;

import java.util.List;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.PBIFEPrdqueryPrdQueryTaUnitFinanceById;
import onight.zjfae.afront.gens.v2.QueryTransferSellProfitsPB;


/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.moneymanagement
 * @ClassName: SellActivity
 * @Description: 我要卖界面
 * @Author: moran
 * @CreateDate: 2019/6/17 9:03
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/17 9:03
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class SellActivity extends CommActivity<SellPresenter> implements View.OnClickListener, SellView {
    private TextView mTitle;
    private TextView mExpectedMaxAnnualRate;
    private TextView mLeastHoldAmount;
    private TextView mOfInterestRatesTransfer_title;
    private TextView mOfInterestRatesTransfer;
    private TextView mDeadline;
    private TextView mManageEndDate;
    private TextView mLeftDays;
    private TextView mCanTransferAmount;
    private TextView mYearDay;
    private TextView mLeastTranAmount;
    private TextView mTradeIncrease;
    private TextView mBenjin;
    private TextView mLilv;
    private TextView mTargetRate;
    private TextView mFloatingProfit;
    private TextView mTransferIncome;
    private TextView mAmount;
    private TextView mExtraCost;
    private TextView tv_annualizedIncome;
    private CheckBox product_check;
    private Button mRegisterBt;
    private RecyclerView attachment;
    private SellAmountDialog.Builder builder;
    private LinearLayout mBenjinLl;
    private LinearLayout mLilvLl;
    private LinearLayout attach_layout;
    private String mMoney = "";
    private LinearLayout ll_isWholeTransfer;

    /**
     * 预期收益率
     */
    private String mRate = "";
    private String title;
    /**
     * 浮动盈亏
     */
    private String floatingProfit = "";
    /**
     * 转让收益
     */
    private String transferIncome;
    /**
     * 转让总价
     */
    private String amount;

    /**
     * 手续费
     */
    private String extraCost;

    /**
     * 是否需要短信验证码
     */
    private boolean rateLowSendMessage = false;


    /**
     * 当前是svp产品且不需要输入利率，挂单利率是否不符提示
     */
    private boolean isShowSellProfitsDialogStatus = false;


    private SendSmsCodeDialog.Builder sendSmsDialog;

    private LinearLayout lin_leftDays, lin_targetRate, lin_floatingProfit, lin_transferIncome, lin_amount, lin_extraCost;
    private String yearDayValue = "360";
    private NestedScrollView product_scroll;
    private TextView tv_agreement;//免责声明显示text 不需要免责声明逻辑暂时注释
    private TextView tv_tips;//温馨提示


    @Override
    protected int getLayoutId() {
        return R.layout.activity_sell;
    }

    @Override
    public void initView() {
        setTitle("我要卖");
        mTitle = (TextView) findViewById(R.id.title);
        mExpectedMaxAnnualRate = (TextView) findViewById(R.id.expectedMaxAnnualRate);
        mLeastHoldAmount = (TextView) findViewById(R.id.leastHoldAmount);
        mOfInterestRatesTransfer_title = (TextView) findViewById(R.id.tv_transfer_of_interest_rates);
        mOfInterestRatesTransfer = (TextView) findViewById(R.id.transfer_of_interest_rates);
        mDeadline = (TextView) findViewById(R.id.deadline);
        mManageEndDate = (TextView) findViewById(R.id.manageEndDate);
        mLeftDays = (TextView) findViewById(R.id.leftDays);
        mCanTransferAmount = (TextView) findViewById(R.id.canTransferAmount);
        mYearDay = (TextView) findViewById(R.id.yearDay);
        mLeastTranAmount = (TextView) findViewById(R.id.leastTranAmount);
        mTradeIncrease = (TextView) findViewById(R.id.tradeIncrease);
        mBenjin = (TextView) findViewById(R.id.benjin);
        mLilv = (TextView) findViewById(R.id.lilv);
        mTargetRate = (TextView) findViewById(R.id.targetRate);
        mFloatingProfit = (TextView) findViewById(R.id.floatingProfit);
        mTransferIncome = (TextView) findViewById(R.id.transferIncome);
        mAmount = (TextView) findViewById(R.id.amount);
        mExtraCost = (TextView) findViewById(R.id.extraCost);
        product_check = (CheckBox) findViewById(R.id.product_check);
        mRegisterBt = (Button) findViewById(R.id.bt_register);
        mRegisterBt.setOnClickListener(this);
        attachment = findViewById(R.id.attachment);
        mBenjinLl = (LinearLayout) findViewById(R.id.ll_benjin);
        mLilvLl = (LinearLayout) findViewById(R.id.ll_lilv);
        attach_layout = (LinearLayout) findViewById(R.id.attach_layout);
        ll_isWholeTransfer = findViewById(R.id.ll_isWholeTransfer);
        tv_annualizedIncome = findViewById(R.id.tv_annualizedIncome);
        tv_agreement = findViewById(R.id.tv_agreement);
        tv_tips = findViewById(R.id.tv_tips);
        product_scroll = findViewById(R.id.product_scroll);
        lin_leftDays = findViewById(R.id.lin_leftDays);
        lin_leftDays.setOnClickListener(this);
        lin_targetRate = findViewById(R.id.lin_targetRate);
        lin_targetRate.setOnClickListener(this);
        lin_floatingProfit = findViewById(R.id.lin_floatingProfit);
        lin_floatingProfit.setOnClickListener(this);
        lin_transferIncome = findViewById(R.id.lin_transferIncome);
        lin_transferIncome.setOnClickListener(this);
        lin_amount = findViewById(R.id.lin_amount);
        lin_amount.setOnClickListener(this);
        lin_extraCost = findViewById(R.id.lin_extraCost);
        lin_extraCost.setOnClickListener(this);
        mBenjinLl.setOnClickListener(this);
        mLilvLl.setOnClickListener(this);
        setDisclaimer();

    }


    @Override
    protected void initData() {
        String productId = getIntent().getStringExtra("id");
        String productCode = getIntent().getStringExtra("productCode");
        presenter.initData(productId, productCode);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.ll_sell);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected SellPresenter createPresenter() {
        return new SellPresenter(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_register:// TODO 18/10/24
                if (attach_layout.getVisibility() == View.VISIBLE) {
                    if (!product_check.isChecked()) {
                        product_scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
                        showDialog("请先阅读并同意本产品相关协议");
                        return;
                    }
                }

                if (floatingProfit.isEmpty()) {
                    if (builder != null) {
                        builder.setMessageRate(mMoney, mRate);
                        builder.create().show();
                    }
                } else {
                    if (attach_layout.getVisibility() == View.VISIBLE) {
                        if (!product_check.isChecked()) {
                            showDialog("请先阅读并同意本产品相关协议");
                            return;
                        }
                    }

                    //需要短信验证码
                    if (rateLowSendMessage && !isVerify) {
                        mMoney = "";
                        mRate = "";
                        if (builder != null) {
                            builder.setMessageRate(mMoney, mRate);
                            builder.create().show();
                        }
                        return;
                    }

                    //svp产品且不需要输入利率，点击挂单按钮后，提示弹框，点确定后跳转挂单页面
                    if (isShowSellProfitsDialogStatus) {

                        onSellProfitsShowDialog(profits, returnMsg);

                    } else {
                        startConfirmSellActivity();

                    }


                }
                break;
            case R.id.ll_benjin:
//                if (lin_product_check.getVisibility() == View.VISIBLE) {
//                    if (!product_check.isChecked()) {
//                        product_scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
//                        showDialog("请先阅读并同意本产品相关协议");
//                        return;
//                    }
//                }
                if (builder != null) {
                    builder.setMessageRate(mMoney, mRate);
                    builder.create().show();
                }
                break;
            case R.id.ll_lilv:
//                if (lin_product_check.getVisibility() == View.VISIBLE) {
//                    if (!product_check.isChecked()) {
//                        product_scroll.fullScroll(NestedScrollView.FOCUS_DOWN);
//                        showDialog("请先阅读并同意本产品相关协议");
//                        return;
//                    }
//                }
                if (builder != null) {
                    builder.setMessageRate(mMoney, mRate);
                    builder.create().show();
                }
                break;
            case R.id.attach_layout:
                if (product_check.isChecked()) {
                    product_check.setChecked(false);
                } else {
                    product_check.setChecked(true);
                }
                break;
            case R.id.lin_leftDays://本期存续天数
                showDialog("指本次付息周期计息起始日至交易当天前一自然日的实际天数。");
                break;
            case R.id.lin_targetRate://受让方目标收益率
                showDialog("受让方受让后参考收益率");
                break;
            case R.id.lin_floatingProfit://本次交易浮动盈亏
                showDialog("本次交易浮动盈亏：本次付息周期内，以当前转让利率所获收益相比较产品预期年化收益率所获收益的差额。计算公式：转让本金 × (转让利率 - 预期年化收益率) × 本期存续天数 ÷ "+yearDayValue);
                break;
            case R.id.lin_transferIncome://转让收益
                showDialog("转让收益 = 转让本金 × 转让利率 × 本期存续天数 ÷ "+yearDayValue);
                break;
            case R.id.lin_amount://转让总价
                showDialog("转让总价 = 转让本金 + 转让收益");
                break;
            case R.id.lin_extraCost://手续费
                showDialog("转让本金小于5万元手续费2元，大于等于5万元手续费5元");
                break;
            default:
                break;

        }
    }

    @Override
    public void getDetail(final PBIFEPrdqueryPrdQueryTaUnitFinanceById.PBIFE_prdquery_prdQueryTaUnitFinanceById finance, final String keyCode) {

        final PBIFEPrdqueryPrdQueryTaUnitFinanceById.PBIFE_prdquery_prdQueryTaUnitFinanceById.TaUnitFinance financeTaUnitFinance = finance.getTaUnitFinance();

        //是否是svp产品
        final String isImmediateInterest = finance.getIsImmediateInterest();

        //是否是整体转让产品
        if ("1".equals(finance.getIsWholeTransfer())) {
            ll_isWholeTransfer.setVisibility(View.VISIBLE);
        } else {
            ll_isWholeTransfer.setVisibility(View.GONE);
        }
        if (StringUtils.isNotBlank(financeTaUnitFinance.getYearDay())) {
            yearDayValue = financeTaUnitFinance.getYearDay().substring(4);
        }
        mTitle.setText(financeTaUnitFinance.getProductName());
        mRate = financeTaUnitFinance.getExpectedMaxAnnualRate();
        presenter.setExpectedMaxAnnualRate(financeTaUnitFinance.getExpectedMaxAnnualRate());
        mExpectedMaxAnnualRate.setText(financeTaUnitFinance.getExpectedMaxAnnualRate() + "%");
        mLeastHoldAmount.setText(financeTaUnitFinance.getLeastHoldAmount() + "元");
        if (!financeTaUnitFinance.getTransferIsfloat().equals("float")) {
            mOfInterestRatesTransfer_title.setText("转让利率");
            mOfInterestRatesTransfer.setText(financeTaUnitFinance.getTransferFloatBegin() + "%");
        } else {
            mOfInterestRatesTransfer_title.setText("转让利率范围");
            mOfInterestRatesTransfer.setText(financeTaUnitFinance.getTransferFloatBegin() + "%-" + financeTaUnitFinance.getTransferFloatEnd() + "%");
        }
        mDeadline.setText(financeTaUnitFinance.getDeadline() + "天");
        mManageEndDate.setText(financeTaUnitFinance.getManageEndDate());
        mLeftDays.setText(financeTaUnitFinance.getLeftDays() + "天");
        mCanTransferAmount.setText(financeTaUnitFinance.getCanTransferAmount() + "元");
        mYearDay.setText(financeTaUnitFinance.getYearDay());
        mLeastTranAmount.setText(financeTaUnitFinance.getLeastTranAmount() + "元");
        mTradeIncrease.setText(financeTaUnitFinance.getTradeIncrease() + "元");
        builder = new SellAmountDialog.Builder(this, financeTaUnitFinance.getExpectedMaxAnnualRate() + "%", financeTaUnitFinance.getTransferFloat() + "%", financeTaUnitFinance.getTransferFloatBegin() + "%-" + financeTaUnitFinance.getTransferFloatEnd() + "%", financeTaUnitFinance.getLeastTranAmount());


        String userType = UserInfoSharePre.getUserType();
        //svp产品且不需要利率
        if (isImmediateInterest.equals("1") && keyCode.equals("1") && userType.equals("company")) {
            //tv_annualizedIncome.setText("预期年化收益率");
            mLilv.setText("0%(不可修改)");
            mLilvLl.setEnabled(false);
            builder.setIsHiddenExpectedMaxAnnualRate(true);
            builder.setHidden("float");
            lin_floatingProfit.setVisibility(View.GONE);
            lin_transferIncome.setVisibility(View.GONE);
            //mMoney = financeTaUnitFinance.getCanTransferAmount();
            mRate = financeTaUnitFinance.getTransferFloatBegin();
            presenter.onSellProfits(financeTaUnitFinance.getProductCode(), financeTaUnitFinance.getBuyerSmallestAmount(), mMoney, mRate, false);
        }

        //是否全部挂单
        if (finance.getIfAllSell().equals("1")) {
            mMoney = financeTaUnitFinance.getCanTransferAmount();
            mBenjin.setText(mMoney + "元(不可修改)");
            mBenjinLl.setEnabled(false);
            builder.setHidden("AllSell");
            //是否是浮动利率 false = 固定利率，true=浮动利率
            if (!financeTaUnitFinance.getTransferIsfloat().equals("float")) {
                mMoney = financeTaUnitFinance.getCanTransferAmount();
                mRate = financeTaUnitFinance.getTransferFloatBegin();
                mLilv.setText(mRate + "%(不可修改)");
                mLilvLl.setEnabled(false);
                builder.setHidden("float");
                presenter.onSellProfits(financeTaUnitFinance.getProductCode(), financeTaUnitFinance.getBuyerSmallestAmount(), mMoney, mRate, true);
            }
        } else {
            if (!financeTaUnitFinance.getTransferIsfloat().equals("float")) {
                mRate = financeTaUnitFinance.getTransferFloatBegin();
                mLilv.setText(mRate + "%(不可修改)");
                mLilvLl.setEnabled(false);
                builder.setHidden("float");
            }
        }

        //是否展示提前兑付相关信息
        if (finance.getIsShowHonourAdvance().equals("1")) {
            tv_tips.setVisibility(View.VISIBLE);
            tv_tips.setText(finance.getHonourAdvanceTips());
        } else {
            tv_tips.setVisibility(View.GONE);
        }

        builder.setPurchaseMoney(new SellAmountDialog.PurchaseMoney() {
            @Override
            public void purchaseMoney(String money, String rate) {
                if (StringUtils.isNotBlank(money)) {

                    mMoney = money;
                } else {
                    if (finance.getIfAllSell().equals("1")) {

                    } else {
                        mMoney = "";
                        showDialog("请输入转让金额");
                        clearFloatingProfit();
                        return;
                    }
                }

                if (isImmediateInterest.equals("1") && keyCode.equals("1")) {
                    mRate = "0";

                    presenter.sellProfits(financeTaUnitFinance.getProductCode(), financeTaUnitFinance.getBuyerSmallestAmount(), money, mRate);
                } else {
                    if (StringUtils.isNotBlank(rate)) {
                        //判断输入的转让利率是否符合规则
                        if (Float.parseFloat(rate) < Float.parseFloat(financeTaUnitFinance.getTransferFloatBegin()) || Float.parseFloat(rate) > Float.parseFloat(financeTaUnitFinance.getTransferFloatEnd())) {
                            //输入的转让利率不在符合的范围里面
                            showDialog("请输入" + financeTaUnitFinance.getTransferFloatBegin() + "%-" + financeTaUnitFinance.getTransferFloatEnd() + "%之间的利率");
                            mRate = "";
                            clearFloatingProfit();
                            return;
                        }
                        if (Float.parseFloat(MoneyUtil.moneySub(MoneyUtil.moneyMul(financeTaUnitFinance.getTransferFloatEnd(), "100.00"), MoneyUtil.moneyMul(rate, "100.00"))) % Float.parseFloat(MoneyUtil.moneyMul(financeTaUnitFinance.getTransferFloat(), "100.00")) != 0 && financeTaUnitFinance.getTransferIsfloat().equals("float")) {
                            //输入转让利率不符合浮动步长，点击确定提示 您输入的转让利率的最小精度应为xxx 最高的减去输入的再对步长取余数
                            showDialog("您输入的转让利率的最小精度应为" + financeTaUnitFinance.getTransferFloat() + "%");
                            mRate = "";
                            clearFloatingProfit();
                            return;
                        }
                    } else {
                        if (!financeTaUnitFinance.getTransferIsfloat().equals("float")) {

                        } else {
                            showDialog("请输入转让利率");
                            mRate = "";
                            clearFloatingProfit();
                            return;
                        }
                    }
                    if (finance.getIfAllSell().equals("1")) {
                        //代表固定金额
                        mMoney = financeTaUnitFinance.getCanTransferAmount();
                        money = financeTaUnitFinance.getCanTransferAmount();
                    } else {
                        mMoney = money;
                    }
                    if (!financeTaUnitFinance.getTransferIsfloat().equals("float")) {
                        //代表固定利率
                        mRate = financeTaUnitFinance.getTransferFloatBegin();
                        rate = financeTaUnitFinance.getTransferFloatBegin();
                    } else {
                        mRate = rate;
                    }

                    presenter.sellProfits(financeTaUnitFinance.getProductCode(), financeTaUnitFinance.getBuyerSmallestAmount(), money, rate);
                }


            }
        });
        title = financeTaUnitFinance.getProductName();
    }

    @Override
    public void productAttachment(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo) {
        //我要卖附件点击不需要判断
        final List<Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList>
                list = attachmentInfo.getData().getTaProductAttachmentListList();

        if (list == null || list.isEmpty()) {
            attach_layout.setVisibility(View.GONE);
            return;
        }

        AttachmentAdapter adapter = new AttachmentAdapter(SellActivity.this, list);
        adapter.setOnItemClickListener(new AttachmentAdapter.OnItemListener() {
            @Override
            public void onItemClick(int position) {
                //附件单击
                String id = list.get(position).getId();
                String title = list.get(position).getTitle();
                openAttachment(id, title, "11");
            }
        });
        attachment.setLayoutManager(new LinearLayoutManager(SellActivity.this));
        attachment.setAdapter(adapter);
        attachment.addItemDecoration(new DividerItemDecorations());
    }

    /**
     * 挂单利率
     *
     * @param profits 挂单利率详情
     */
    @Override
    public void sellProfits(QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits, boolean isEstimatedAmount) {
        if (isEstimatedAmount) {
            if (!mBenjin.getText().toString().contains("不可修改")) {
                mBenjin.setText(mMoney + "元(点击修改)");
            }
        }
        if (!mLilv.getText().toString().contains("不可修改")) {
            mLilv.setText(mRate + "%(点击修改)");
        }
        mTargetRate.setText(profits.getTargetRate() + "%");
        if (profits.getFloatingProfit().contains("-")) {
            mFloatingProfit.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mFloatingProfit.setTextColor(getResources().getColor(R.color.colorGeneral));
        }
        mFloatingProfit.setText(profits.getFloatingProfit() + "元");
        mTransferIncome.setText(profits.getTransferIncome() + "元");
        mAmount.setText(profits.getAmount() + "元");
        mExtraCost.setText(profits.getExtraCost() + "元");
        floatingProfit = profits.getFloatingProfit();
        transferIncome = profits.getTransferIncome();
        amount = profits.getAmount();
        extraCost = profits.getExtraCost();
        rateLowSendMessage = Boolean.parseBoolean(profits.getRateLowSendMessage());
        CCLog.i("rateLowSendMessage", rateLowSendMessage);
        //获取短信验证码
        if (rateLowSendMessage) {
            sendSmsDialog = new SendSmsCodeDialog.Builder(SellActivity.this);

            sendSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //mRate = presenter.getExpectedMaxAnnualRate();
                    dialog.dismiss();
                }
            });


            sendSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                @Override
                public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                    if (Utils.isStringEmpty(smsCode)) {
                        showDialog("请输入短信验证码");
                    } else {
                        //校验短信验证码
                        presenter.onVerifySmsCode(smsCode, dialog);

                    }
                }

                //发送短信验证码接口
                @Override
                public void onSmsClick() {
                    presenter.sendSmsCode(mTitle.getText().toString(), profits.getAbandonEstimateProfit());

                }
            });

            sendSmsDialog.show();
        }
    }


    @Override
    public void onSellProfits(final QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits, String returnMsg) {
        isShowSellProfitsDialogStatus = false;
        floatingProfit = profits.getFloatingProfit();
        isVerify = false;
        rateLowSendMessage = Boolean.parseBoolean(profits.getRateLowSendMessage());
        CCLog.i("rateLowSendMessage", rateLowSendMessage);
        showDialog(Html.fromHtml(returnMsg).toString(), "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                if (!mBenjin.getText().toString().contains("不可修改")) {
                    mBenjin.setText(mMoney + "元(点击修改)");
                }
                if (!mLilv.getText().toString().contains("不可修改")) {
                    mLilv.setText(mRate + "%(点击修改)");
                }
                mTargetRate.setText(profits.getTargetRate() + "%");
                if (profits.getFloatingProfit().contains("-")) {
                    mFloatingProfit.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    mFloatingProfit.setTextColor(getResources().getColor(R.color.colorGeneral));
                }
                mFloatingProfit.setText(profits.getFloatingProfit() + "元");
                mTransferIncome.setText(profits.getTransferIncome() + "元");
                mAmount.setText(profits.getAmount() + "元");
                mExtraCost.setText(profits.getExtraCost() + "元");

                transferIncome = profits.getTransferIncome();
                amount = profits.getAmount();
                extraCost = profits.getExtraCost();
                //获取短信验证码
                if (rateLowSendMessage) {
                    sendSmsDialog = new SendSmsCodeDialog.Builder(SellActivity.this);
                    if (!profits.getAbandonEstimateProfit().equals("0") && !profits.getAbandonEstimateProfit().equals("")) {

                        //您确定放弃"+()+"元预期收益吗？如确定，请将发送至手机的验证码填写并提交。
                        SpannableString spanStrStart = new SpannableString("您确定放弃");

                        SpannableString spanStrCenter = new SpannableString(profits.getAbandonEstimateProfit());

                        SpannableString spanStrEnd = new SpannableString("元预期收益吗？如确定，请将发送至手机的验证码填写并提交。");

                        SpannableStringBuilder spanString = new SpannableStringBuilder();
                        spanString.append(spanStrStart);
                        spanString.append(spanStrCenter);
                        spanString.append(spanStrEnd);

                        ForegroundColorSpan span = new ForegroundColorSpan(SellActivity.this.getResources().getColor(R.color.colorRed));
                        spanString.setSpan(span, spanStrStart.length(), spanString.length() - spanStrEnd.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                        sendSmsDialog.setMsg(spanString);
                    }

                    sendSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //mRate = presenter.getExpectedMaxAnnualRate();
                            dialog.dismiss();
                        }
                    });


                    sendSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                        @Override
                        public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                            if (Utils.isStringEmpty(smsCode)) {
                                showDialog("请输入短信验证码");
                            } else {
                                //校验短信验证码
                                presenter.onVerifySmsCode(smsCode, dialog);

                            }
                        }

                        //发送短信验证码接口
                        @Override
                        public void onSmsClick() {
                            presenter.sendSmsCode(mTitle.getText().toString(), profits.getAbandonEstimateProfit());

                        }
                    });

                    sendSmsDialog.show();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
                if (mBenjin.getText().toString().contains("不可修改") && mLilv.getText().toString().contains("不可修改")) {
                    mTargetRate.setText(profits.getTargetRate() + "%");
                    if (profits.getFloatingProfit().contains("-")) {
                        mFloatingProfit.setTextColor(getResources().getColor(R.color.colorPrimary));
                    } else {
                        mFloatingProfit.setTextColor(getResources().getColor(R.color.colorGeneral));
                    }
                    mFloatingProfit.setText(profits.getFloatingProfit() + "元");
                    mTransferIncome.setText(profits.getTransferIncome() + "元");
                    mAmount.setText(profits.getAmount() + "元");
                    mExtraCost.setText(profits.getExtraCost() + "元");
                    floatingProfit = profits.getFloatingProfit();
                    transferIncome = profits.getTransferIncome();
                    amount = profits.getAmount();
                    extraCost = profits.getExtraCost();
                }
                if (!mBenjin.getText().toString().contains("不可修改")) {
                    mMoney = "";
                    clearFloatingProfit();
                }
                if (!mLilv.getText().toString().contains("不可修改")) {
                    mRate = presenter.getExpectedMaxAnnualRate();
                    clearFloatingProfit();
                }
            }
        });
    }


    private QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits;
    private String returnMsg;

    /**
     * 挂单利率不符
     *
     * @param profits   挂单利率详情
     * @param returnMsg 返回信息
     */
    @Override
    public void onSellProfitsStatus(QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits, String returnMsg, boolean isEstimatedAmount) {
        this.profits = profits;
        this.returnMsg = returnMsg;
        isShowSellProfitsDialogStatus = true;
        transferIncome = profits.getTransferIncome();
        amount = profits.getAmount();
        extraCost = profits.getExtraCost();
        floatingProfit = profits.getFloatingProfit();
        isVerify = false;
        rateLowSendMessage = Boolean.parseBoolean(profits.getRateLowSendMessage());
        CCLog.i("rateLowSendMessage", rateLowSendMessage);
        if (isEstimatedAmount) {
            if (!mBenjin.getText().toString().contains("不可修改")) {
                mBenjin.setText(mMoney + "元(点击修改)");
            }
        }
        if (!mLilv.getText().toString().contains("不可修改")) {
            mLilv.setText(mRate + "%(点击修改)");
        }
        mTargetRate.setText(profits.getTargetRate() + "%");
        if (profits.getFloatingProfit().contains("-")) {
            mFloatingProfit.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            mFloatingProfit.setTextColor(getResources().getColor(R.color.colorGeneral));
        }
        mFloatingProfit.setText(profits.getFloatingProfit() + "元");
        mTransferIncome.setText(profits.getTransferIncome() + "元");
        mAmount.setText(profits.getAmount() + "元");
        mExtraCost.setText(profits.getExtraCost() + "元");
    }

    /**
     * 挂单利率不符，点击挂单按钮提示弹框--固定利率和交易金额
     */
    private void onSellProfitsShowDialog(final QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits, String returnMsg) {

        showDialog(Html.fromHtml(returnMsg).toString(), "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //获取短信验证码
                if (rateLowSendMessage) {
                    //您确定放弃"+()+"元预期收益吗？如确定，请将发送至手机的验证码填写并提交。
                    SpannableString spanStrStart = new SpannableString("您确定放弃");

                    SpannableString spanStrCenter = new SpannableString(profits.getAbandonEstimateProfit());

                    SpannableString spanStrEnd = new SpannableString("元预期收益吗？如确定，请将发送至手机的验证码填写并提交。");

                    SpannableStringBuilder spanString = new SpannableStringBuilder();
                    spanString.append(spanStrStart);
                    spanString.append(spanStrCenter);
                    spanString.append(spanStrEnd);

                    ForegroundColorSpan span = new ForegroundColorSpan(SellActivity.this.getResources().getColor(R.color.colorRed));
                    spanString.setSpan(span, spanStrStart.length(), spanString.length() - spanStrEnd.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

                    sendSmsDialog = new SendSmsCodeDialog.Builder(SellActivity.this);
                    sendSmsDialog.setMsg(spanString);
                    sendSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


                    sendSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                        @Override
                        public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                            if (Utils.isStringEmpty(smsCode)) {
                                showDialog("请输入短信验证码");
                            } else {
                                //校验短信验证码
                                presenter.onVerifySmsCode(smsCode, dialog);

                            }
                        }

                        //发送短信验证码接口
                        @Override
                        public void onSmsClick() {
                            presenter.sendSmsCode(mTitle.getText().toString(), profits.getAbandonEstimateProfit());

                        }
                    });
                    sendSmsDialog.show();
                } else {
                    //不需要验证码，直接跳转下一个界面
                    startConfirmSellActivity();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 发送短信验证码回调
     *
     * @param msg 发送短信验证码提醒
     * @date: 2020/6/24 11:02
     * @author: moran
     */
    @Override
    public void onSmsCodeStatusSuccess(String msg) {
        sendSmsDialog.setSmsButtonState(true);
        showToast(msg);
    }

    /**
     * 短信验证码是否校验通过
     */
    private boolean isVerify = false;


    /**
     * 短信验证码弹框
     *
     * @param msg 短信验证码提示语
     */
    private void showSmsDialog(String msg) {


        sendSmsDialog = new SendSmsCodeDialog.Builder(SellActivity.this);

        sendSmsDialog.setMsg(msg);

        sendSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //mRate = presenter.getExpectedMaxAnnualRate();
                dialog.dismiss();
            }
        });


        sendSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
            @Override
            public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                if (Utils.isStringEmpty(smsCode)) {
                    showDialog("请输入短信验证码");
                } else {
                    //校验短信验证码
                    presenter.onVerifySmsCode(smsCode, dialog);

                }
            }

            //发送短信验证码接口
            @Override
            public void onSmsClick() {
                presenter.sendSmsCode(mTitle.getText().toString(), profits.getAbandonEstimateProfit());

            }
        });

        sendSmsDialog.show();


    }


    /**
     * 验证码是否通过
     *
     * @param isVerify 验证码是否校验通过
     * @return
     * @date: 2020/6/24 15:34
     * @author: moran
     */
    @Override
    public void onSmsCodeVerifyStatus(boolean isVerify, String msg, DialogInterface dialog) {
        this.isVerify = isVerify;
        if (isVerify) {
            dialog.dismiss();
            //svp产品且不需要利率点击挂单按钮后，提示弹框，点确定后跳转挂单页面
            if (isShowSellProfitsDialogStatus) {

                startConfirmSellActivity();

            }
        } else {
            showDialog(msg);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    /**
     * 设置免责声明或者没有免责声明显示我已阅读并接受相关协议
     */
    public void setDisclaimer() {

        String clicktext = "我已阅读并接受相关协议";
        tv_agreement.setText(clicktext);
        attach_layout.setOnClickListener(this);
        product_check.setClickable(false);


    }

    /**
     * 当输入的内容不符合弹框限制的规则时 清空原有的数据
     */
    private void clearFloatingProfit() {
        if (!mBenjin.getText().toString().contains("不可修改")) {
            mBenjin.setText("请输入转让本金");
        }
        if (!mLilv.getText().toString().contains("不可修改")) {
            mLilv.setText("请输入转让利率");
        }
        mTargetRate.setText("%");
        mFloatingProfit.setTextColor(getResources().getColor(R.color.colorGeneral));
        mFloatingProfit.setText("元");
        mTransferIncome.setText("元");
        mAmount.setText("元");
        mExtraCost.setText("元");
        floatingProfit = "";
        transferIncome = "";
        amount = "";
        extraCost = "";

    }


    private void startConfirmSellActivity() {
        Intent intent = new Intent(SellActivity.this, ConfirmSellActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("productCode", getIntent().getStringExtra("productCode"));
        intent.putExtra("benjin", mMoney);
        intent.putExtra("lilv", mRate);
        intent.putExtra("floatingProfit", floatingProfit);
        intent.putExtra("transferIncome", transferIncome);
        intent.putExtra("amount", amount);
        intent.putExtra("extraCost", extraCost);
        startActivityForResult(intent, 2);

    }
}
