package com.hundsun.zjfae.activity.productreserve;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.OfflineRechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeActivity;
import com.hundsun.zjfae.activity.mine.RechargeGuideActivity;
import com.hundsun.zjfae.activity.product.ProductPlayStateActivity;
import com.hundsun.zjfae.activity.productreserve.bean.ReservePlayInfoBean;
import com.hundsun.zjfae.activity.productreserve.presenter.ReservePayPresenter;
import com.hundsun.zjfae.activity.productreserve.view.ReservePayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.RechargeBankCardInfo;

/**
 * @Description:长短预约详情预约支付界面
 * @Author: zhoujianyu
 * @Time: 2018/10/26 14:20
 */
public class ReservePayActivity extends CommActivity implements ReservePayView {
    private String id = "", money = "", title = "";
    private TextView tv_title, tv_product_money, tv_total_money, tv_pay_money, tv_bankcard_money, tv_card_name;
    private LinearLayout lin_bank_card_pay, lin_bank_card, lin_yue, lin_sms;
    private CheckBox checkBox;
    private ReservePayPresenter mPresenter;
    private QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit;
    private PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard;
    private Float pay_money = 0f;//产品预约价格
    private Float total_money = 0f;//可用余额
    //支付信息类
    private ReservePlayInfoBean playInfo = new ReservePlayInfoBean();
    private CustomCountDownTimer countDownTimer;
    private TextView mTvVerificationCode;//验证码按钮
    private EditText mEtCode;
    private String type = "";//用于区别是长期预约还是短期预约

    private Boolean sendmessage = false;


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.lin_reserve_pay);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve_pay;
    }

    @Override
    protected BasePresenter createPresenter() {
        return mPresenter = new ReservePayPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("预约保证金支付");
        id = getIntent().getStringExtra("id");
        money = getIntent().getStringExtra("money");
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_product_money = findViewById(R.id.tv_product_money);
        tv_total_money = findViewById(R.id.tv_total_money);
        tv_pay_money = findViewById(R.id.tv_pay_money);
        tv_bankcard_money = findViewById(R.id.tv_bankcard_money);
        tv_card_name = findViewById(R.id.tv_card_name);
        lin_bank_card_pay = findViewById(R.id.lin_bank_card_pay);
        lin_bank_card = findViewById(R.id.lin_bank_card);
        lin_yue = findViewById(R.id.lin_yue);
        checkBox = findViewById(R.id.checkbox);
        lin_sms = findViewById(R.id.lin_sms);
        mTvVerificationCode = findViewById(R.id.tv_verification_code);
        mEtCode = findViewById(R.id.et_verification_code);
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lin_sms.getVisibility() == View.VISIBLE && !sendmessage) {
                    showDialog("请先获取短信验证码");
                    return;
                }
                if (lin_sms.getVisibility() == View.VISIBLE && mEtCode.getText().toString().equals("")) {
                    showDialog("短信验证码不能为空");
                    return;
                }
                playInfo.setOrderType(type.replace("0", ""));
                playInfo.setOrderBuyAmount(money);
                playInfo.setCheckCode(mEtCode.getText().toString());//设置验证码
                playInfo.setRepeatCommitCheckCode(queryPayInit.getData().getPayInitWrap().getRepeatCommitCheckCode());
                playInfo.setOrderProductCode(id);
                //设置支付方式 1为有混合支付 0为纯银行卡支付
                if (pay_money > total_money) {//需要银行卡支付
                    if (total_money > 0) {//说明余额大于0
                        if (lin_yue.getVisibility() == View.VISIBLE) {
                            if (checkBox.isChecked()) {
                                playInfo.setPayType("1");
                            } else {
                                playInfo.setPayType("0");
                            }
                        } else {
                            playInfo.setPayType("1");
                        }
                    } else {
                        playInfo.setPayType("0");
                    }
                } else {
                    playInfo.setPayType("1");
                }
//                if (lin_bank_card_pay.getVisibility() == View.VISIBLE) {
//                    playInfo.setPayType("1");
//                } else {
//                    playInfo.setPayType("0");
//                }
                isPlay();
            }
        });
        mTvVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bankmoney = tv_bankcard_money.getText().toString().replace("元", "").replace(",", "");
                mPresenter.sendSms(bankCard.getData().getTcCustomerChannelListList().get(0).getPayChannelNo(), bankCard.getData().getTcCustomerChannelListList().get(0).getBankCard(), bankmoney);
            }
        });
        countDownTimer = new CustomCountDownTimer(60000, 1000, mTvVerificationCode);
    }


    @Override
    protected void initData() {

        mPresenter.loadData(id, money);
    }

    @Override
    public void playInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit) {
        if (queryPayInit.getData() == null) {
            findViewById(R.id.play).setVisibility(View.GONE);
            return;
        }
        findViewById(R.id.play).setVisibility(View.VISIBLE);
        this.queryPayInit = queryPayInit;
        tv_product_money.setText(queryPayInit.getData().getPayInitWrap().getPayAmount() + "元");
        tv_total_money.setText("账户余额: " + queryPayInit.getData().getPayInitWrap().getBalanceY() + "元");
        tv_pay_money.setText(queryPayInit.getData().getPayInitWrap().getPayAmount() + "元");

        if (StringUtils.isNotBlank(queryPayInit.getData().getPayInitWrap().getPayAmount())) {
            pay_money = Float.parseFloat(queryPayInit.getData().getPayInitWrap().getPayAmount().replace(",", ""));
        } else {
            pay_money = Float.parseFloat("0.00");
        }
        if (StringUtils.isNotBlank(queryPayInit.getData().getPayInitWrap().getBalanceY())) {
            total_money = Float.parseFloat(queryPayInit.getData().getPayInitWrap().getBalanceY().replace(",", ""));
        } else {
            total_money = Float.parseFloat("0.00");
        }

        if (!queryPayInit.getData().getPayInitWrap().getInAmountWithBalance().equals("0.00")){
            String userType = UserInfoSharePre.getUserType();
            if (userType.equals("company")){
                showDialog("您的账户余额不足，请通过线下充值的方式进行充值！", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });
                return;

            }



        }
        //上海银行提示去充值
          if (queryPayInit.getData().getPayInitWrap().getIsJumpToInFund().equals("1") ) {



            String userType = UserInfoSharePre.getUserType();

            if (userType.equals("personal")) {

                showDialog("您的账户余额不足，请先对账户进行充值！", "去充值", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.queryBankInfo();

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

            }
            else {
                showDialog("您的账户余额不足，请通过线下充值的方式进行充值！", "知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                });

            }



        } else {
            if (pay_money > total_money) {//需要银行卡支付
                if (total_money > 0) {//说明余额大于0
                    tv_pay_money.setText(MoneyUtil.fmtMicrometer(MoneyUtil.formatMoney(String.valueOf(total_money))) + "元");
                    tv_bankcard_money.setText(MoneyUtil.fmtMicrometer(MoneyUtil.formatMoney(String.valueOf(pay_money - total_money))) + "元");
                    checkBox.setChecked(true);
                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                tv_pay_money.setText(MoneyUtil.fmtMicrometer(MoneyUtil.formatMoney(String.valueOf(total_money))) + "元");
                                tv_bankcard_money.setText(MoneyUtil.fmtMicrometer(MoneyUtil.formatMoney(String.valueOf(pay_money - total_money))) + "元");
                            } else {
                                tv_pay_money.setText("0.00元");
                                tv_bankcard_money.setText(MoneyUtil.fmtMicrometer(MoneyUtil.formatMoney(String.valueOf(pay_money))) + "元");
                            }
                        }
                    });
                    lin_yue.setVisibility(View.VISIBLE);
                } else {//说明没有余额
                    tv_pay_money.setText("0.00元");
                    tv_bankcard_money.setText(MoneyUtil.fmtMicrometer(pay_money + "") + "元");
                    lin_yue.setVisibility(View.GONE);
                }


                mPresenter.getBankInfo();
                mPresenter.querySms();


            } else {
                lin_yue.setVisibility(View.GONE);
                lin_bank_card.setVisibility(View.GONE);
                lin_bank_card_pay.setVisibility(View.GONE);
            }
        }

    }

    //设置银行卡信息
    @Override
    public void onBankInfo(PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard) {
        this.bankCard = bankCard;
        if (bankCard.getData().getTcCustomerChannelListList() != null && bankCard.getData().getTcCustomerChannelListList().size() > 0) {
            tv_card_name.setText(bankCard.getData().getTcCustomerChannelList(0).getBankName());
            lin_bank_card_pay.setVisibility(View.VISIBLE);
            lin_bank_card.setVisibility(View.VISIBLE);
        } else {
            lin_bank_card_pay.setVisibility(View.VISIBLE);
            lin_bank_card.setVisibility(View.GONE);
        }
    }

    //检验是否需要短信验证码
    @Override
    public void querySms(String returnCode, String returnMsg, String needSms) {
        if (returnCode.equals("0000")) {
            if (needSms.equals("false")) {
                lin_sms.setVisibility(View.GONE);
            } else {
                lin_sms.setVisibility(View.VISIBLE);
            }
        } else {
            showDialog(returnMsg);
        }

    }

    //发送验证码
    @Override
    public void sendCode(String code, String msg, String SerialNo) {
        if ("0000".equals(code)) {
            sendmessage = true;
            countDownTimer.start();
            playInfo.setCheckCodeSerialNo(SerialNo);
        }
        showDialog(msg);
    }

    private void isPlay() {
        final PlayWindow play = new PlayWindow(this);
        play.showAtLocation(findViewById(R.id.lin_reserve_pay));
        play.setPayListener(new PlayWindow.OnPayListener() {
            @Override
            public void onSurePay(String password) {
                //支付
                playInfo.setPassword(EncDecUtil.AESEncrypt(password));
                mPresenter.playOrder(playInfo);
            }
        });
    }

    @Override
    public void playOrder(String returnCode, String returnMsg, String data) {

        if (returnCode.equals("0000")) {
            showDialog(data, "确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(ReservePayActivity.this, ProductPlayStateActivity.class);
                    intent.putExtra("playState", "支付成功");
                    baseStartActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                }
            });

        }

    }

    private String bankName, mbankCard;

    @Override
    public void queryRechargeBankInfo(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
        bankName = bankCardInfo.getData().getBankName();
        mbankCard = bankCardInfo.getData().getBankCardNo();
        //充值渠道关闭
        if (bankCardInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", mbankCard);
            intent.putExtra("type", "301");
            baseStartActivity(intent);
        } else {
            //查询银行卡限额
            mPresenter.queryFundBankInfo(bankCardInfo.getData().getBankNo());
        }
    }

    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
        //限额
        if (fundBankInfo.getData().getShowTips().equals("1")) {
            Intent intent = new Intent(this, OfflineRechargeActivity.class);
            String totalPayAmount = queryPayInit.getData().getPayInitWrap().getPayAmount();
            String balance = queryPayInit.getData().getPayInitWrap().getBalanceY();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            intent.putExtra("payAmount", payBanlance);
            intent.putExtra("bankName", bankName);
            intent.putExtra("bankCard", mbankCard);
            intent.putExtra("type", "302");
            intent.putExtra("tag", "isReserve");
            baseStartActivity(intent);
        } else {
            String totalPayAmount = queryPayInit.getData().getPayInitWrap().getPayAmount();
            String balance = queryPayInit.getData().getPayInitWrap().getBalanceY();
            String payBanlance = MoneyUtil.moneySub(totalPayAmount, balance);
            Intent intent = new Intent(this, RechargeActivity.class);
            intent.putExtra("payAmount", payBanlance);
            intent.putExtra("tag", "isReserve");
            baseStartActivity(intent);
        }
    }

    @Override
    public void onFundRechargeError(String returnMsg) {
        showDialog(getHtml(returnMsg), "是", "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //线下充值
                dialog.dismiss();
                baseStartActivity(ReservePayActivity.this, RechargeGuideActivity.class);
            }
        });
    }

    @Override
    public void onGuideAddBank(String returnMsg) {

        String userType = UserInfoSharePre.getUserType();

        if (userType.equals("personal")){

            showDialog(returnMsg, "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(ReservePayActivity.this, AddBankActivity.class);
                }
            });
        }

        else {

            showDialog("您的账户余额不足，请通过线下充值的方式进行充值！", "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
        }


    }

    private SpannableString getHtml(String content) {

        String str = "您也可通过“银行卡转账”方式充值，是否前往？";

        StringBuffer buffer = new StringBuffer(content);

        buffer.append("\n").append("您也可通过“银行卡转账”方式充值，是否前往？");

        SpannableString spannableString = new SpannableString(buffer.toString());

        StyleSpan span = new StyleSpan(Typeface.BOLD);

        spannableString.setSpan(span, buffer.length() - str.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spannableString;
    }
}
