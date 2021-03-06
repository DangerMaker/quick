package com.hundsun.zjfae.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.assetstream.AssetStreamActivity;
import com.hundsun.zjfae.activity.mine.bean.AgreementInfo;
import com.hundsun.zjfae.activity.mine.presenter.RechargePresenter;
import com.hundsun.zjfae.activity.mine.view.RechargeView;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.LimitFocusChangeInPut;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.RechargeAmountConversion;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;

import onight.zjfae.afront.gens.AcquireBankSmsCheckCode4Recharge;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gens.v2.PBIFEFundRecharge;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.mine
 * @ClassName: RechargeActivity
 * @Description: ????????????
 * @Author: moran
 * @CreateDate: 2019/6/13 18:16
 * @UpdateUser: ????????????
 * @UpdateDate: 2019/6/13 18:16
 * @UpdateRemark: ???????????????
 * @Version: 1.0
 */
public class RechargeActivity extends CommActivity<RechargePresenter> implements RechargeView, View.OnClickListener {

    private ImageView bank_icon;
    private TextView bank_id, money_number, bank_name, memo, accreditedDiff, tv_agreement;
    private EditText recharge_money, sms_edit;
    private CheckBox recharge_check;
    private TextView hMaxAmount_tv, maxAmount_tv, mMaxAmount_tv, recharge_mode;
    private PlayWindow play;
    private TextView sms_button;
    private LinearLayout agreement_layout;

    private RelativeLayout sms_layout;

    private CustomCountDownTimer countDownTimer;
    //?????????????????????
    private String serialNo = "";

    private Button recharge_button;

    private AgreementInfo agreementInfo;

    //??????????????????
    private String payAmount = "";

    //????????????
    private String tag = "";

    private boolean isNeedSms = false;

    //?????????????????????????????????
    private boolean checkNeedSms = false;
    //???????????????????????????
    private LimitFocusChangeInPut textWatcherInPut;

    //??????????????????
    private boolean isRegister = false;

    private final int REQUEST_CODE = 20199;

    @Override
    public void initData() {

        payAmount = getIntent().getStringExtra("payAmount");
        tag = getIntent().getStringExtra("tag") == null ? "" : getIntent().getStringExtra("tag");
        isRegister = getIntent().getBooleanExtra("isRegister", false);
        recharge_money.setText(payAmount);

        presenter.initUserDate_BankInfo();
    }

    @Override
    public void onUserBankInfo(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {

        if (bankCardInfo.getData().getIsWithhoidingAgreement().equals("1")) {
            //????????????????????????
            agreement_layout.setVisibility(View.GONE);
        } else {
            agreementInfo.setName(bankCardInfo.getData().getRealName());
            agreementInfo.setCertificateCode(bankCardInfo.getData().getCertificateCode());
            agreementInfo.setBankCard(bankCardInfo.getData().getBankCard());
            agreementInfo.setBankName(bankCardInfo.getData().getBankName());
            agreement_layout.setVisibility(View.VISIBLE);
            recharge_button.setBackgroundResource(R.drawable.product_buy);
            recharge_button.setClickable(false);
        }

        bank_name.setText(bankCardInfo.getData().getBankName());
        bank_id.setText(bankCardInfo.getData().getBankCardNo());
        ImageLoad.getImageLoad().LoadImage(this, bankCardInfo.getData().getIconUrl(), bank_icon);
        //?????????????????????
        presenter.queryFundBankInfo(bankCardInfo.getData().getBankNo());

    }

    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        agreementInfo.setAccount(userDetailInfo.getData().getAccount());
    }

    @Override
    public void onUserAssetsInfo(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo userAssetsInfo) {
        String accredited = userAssetsInfo.getData().getAccreditedDiff();

        boolean isRealInvestor = presenter.getIsRealInvestor().equals("") || !presenter.getIsRealInvestor().equals("1") && !accredited.equals("0");

        if (isRealInvestor) {
            presenter.notice(userAssetsInfo.getData().getAmount(), accredited);
        } else {
            final SpannableStringBuilder style = new SpannableStringBuilder();
            style.append("1.????????????????????????????????????????????????????????????????????????????????????");
            //??????????????????????????????
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {

                    baseStartActivity(RechargeActivity.this, RechargeGuideActivity.class);
                }
            };
            style.setSpan(clickableSpan, 20, 25, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //????????????????????????
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
            style.setSpan(foregroundColorSpan, 20, 25, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //?????????TextView
            recharge_mode.setMovementMethod(LinkMovementMethod.getInstance());
            recharge_mode.setHighlightColor(Color.TRANSPARENT);
            recharge_mode.setText(style);
            //?????????????????????
            accreditedDiff.setVisibility(View.GONE);
        }
    }


    /**
     * ?????????????????????
     */
    @Override
    public void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
        if (fundBankInfo.getData().getFundBankDict().getNeedSms().equals("true")) {
            isNeedSms = true;
            sms_layout.setVisibility(View.VISIBLE);
            //????????????????????????edittext ?????????????????????????????????????????????

            countDownTimer = new CustomCountDownTimer(60000, 1000, sms_button);
        } else {
            isNeedSms = false;
            sms_layout.setVisibility(View.GONE);
        }
        hMaxAmount_tv.setText(fundBankInfo.getData().getFundBankDict().getHMaxAmount().length() != 0 ? fundBankInfo.getData().getFundBankDict().getHMaxAmount() : "?????????");
        maxAmount_tv.setText(fundBankInfo.getData().getFundBankDict().getMaxAmount().length() != 0 ? fundBankInfo.getData().getFundBankDict().getMaxAmount() : "?????????");
        mMaxAmount_tv.setText(fundBankInfo.getData().getFundBankDict().getMMaxAmount().length() != 0 ? fundBankInfo.getData().getFundBankDict().getMMaxAmount() : "?????????");
        if (StringUtils.isNotBlank(fundBankInfo.getData().getFundBankDict().getMemo())) {
            memo.setText(fundBankInfo.getData().getFundBankDict().getMemo());
            memo.setVisibility(View.VISIBLE);
        } else {
            memo.setVisibility(View.GONE);
        }


    }

    /**
     * ?????????????????????
     */
    @Override
    public void sendSmsState(AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge sendSms) {
        if (sendSms.getReturnCode().equals("0000")) {
            serialNo = sendSms.getData().getSerialNo();
            showDialog("???????????????????????????");
            checkNeedSms = true;
            textWatcherInPut.setFlag(true);
            countDownTimer.start();
        } else {
            showDialog(sendSms.getReturnMsg());
        }
    }

    @Override
    public void onFundRechargeError(String returnCode, String returnMsg) {
        //????????????????????????
        if (returnCode.equals(ConstantCode.ADD_BANK_TIME_OUT)) {
            showDialog(returnMsg, "?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    if (tag.equals("isTransfer") || tag.equals("isProduct")) {
                        HomeActivity.show(RechargeActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
                    } else if (tag.equals("isReserve") || tag.equals("isReserveSubscribe")) {
                        HomeActivity.show(RechargeActivity.this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                    } else {
                        baseStartActivity(RechargeActivity.this, AssetStreamActivity.class);
                        finish();
                    }


                }
            });
        } else if (ConstantCode.PASSWORD_ERROR_CODE.equals(returnCode)||ConstantCode.UNQUALIFIED_MEMBER_CODE.equals(returnCode)) {
            showDialog(returnMsg);
        } else {
            showDialog(getHtml(returnMsg), "???", "???", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //????????????
                    dialog.dismiss();
                    baseStartActivity(RechargeActivity.this, RechargeGuideActivity.class);
                }
            });
        }


    }

    //??????????????????
    @Override
    public void onSettingUserPlayPassWord(String returnMsg) {

        showDialog(returnMsg, "??????????????????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivityForResult(new Intent(RechargeActivity.this, FirstPlayPassWordActivity.class), REQUEST_CODE);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });

    }

    /**
     * ???????????????????????????
     *
     * @param OnlineChannel     ??????????????????
     * @param amount     ?????????
     * @param accredited ?????????????????????
     * @return
     * @date: 1/4/21 5:46 PM
     * @author: moran
     */
    @Override
    public void onRechargeChannelsStatus(boolean OnlineChannel, String amount, String accredited) {
        if (OnlineChannel) {
            //???????????????????????????????????????
            StringBuffer buffer = new StringBuffer("1.???????????????????????????");
            buffer.append(amount).append("???");
            buffer.append("?????????????????????").append(accredited).append("????????????????????????????????????????????????????????????");
            accreditedDiff.setText(buffer.toString());
            final SpannableStringBuilder style = new SpannableStringBuilder();
            style.append("2.????????????????????????????????????????????????????????????????????????????????????");
            //??????????????????????????????
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    baseStartActivity(RechargeActivity.this, RechargeGuideActivity.class);
                }
            };
            style.setSpan(clickableSpan, 20, 25, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //????????????????????????
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
            style.setSpan(foregroundColorSpan, 20, 25, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //?????????TextView
            recharge_mode.setMovementMethod(LinkMovementMethod.getInstance());
            recharge_mode.setHighlightColor(Color.TRANSPARENT);
            recharge_mode.setText(style);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            presenter.initUserDate_BankInfo();
        }
    }

    /**
     * ??????
     */
    @Override
    public void onFundRecharge(PBIFEFundRecharge.Ret_PBIFE_fund_recharge fundRecharge) {
        String returnMsg = fundRecharge.getReturnMsg();
        showDialog(returnMsg, "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (isRegister) {
                    HomeActivity.show(RechargeActivity.this, HomeActivity.HomeFragmentType.HOME_FRAGMENT);
                } else {
                    setResult(RESULT_OK);
                    finish();
                }

            }
        });

    }


    private SpannableString getHtml(String content) {

        String str = "??????????????????????????????????????????????????????????????????";

        StringBuffer buffer = new StringBuffer(content);

        buffer.append("\n").append("??????????????????????????????????????????????????????????????????");

        SpannableString spannableString = new SpannableString(buffer.toString());

        StyleSpan span = new StyleSpan(Typeface.BOLD);

        spannableString.setSpan(span, buffer.length() - str.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spannableString;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recharge_button:
                if (Utils.isViewEmpty(recharge_money)) {
                    showDialog("?????????????????????");
                    return;
                }
                if (agreement_layout.getVisibility() == View.VISIBLE && !recharge_check.isChecked()) {
                    showDialog("??????????????????????????????");
                    return;
                }
                if (isNeedSms && !checkNeedSms) {
                    showDialog("???????????????????????????");
                    return;
                }
                if (isNeedSms && Utils.isViewEmpty(sms_edit)) {
                    showDialog("????????????????????????");
                    return;
                }


                play = new PlayWindow(RechargeActivity.this);
                play.setPayListener(new PlayWindow.OnPayListener() {
                    @Override
                    public void onSurePay(String password) {
                        presenter.fundRecharge(recharge_money.getText().toString(), sms_edit.getText().toString(), EncDecUtil.AESEncrypt(password), serialNo);
                    }
                });
                play.showAtLocation(findViewById(R.id.recharge_layout));
                break;
            case R.id.sms_button:
                if (Utils.isViewEmpty(recharge_money)) {
                    showDialog("?????????????????????");
                    return;
                } else {
                    presenter.sendRechargeSms(recharge_money.getText().toString());
                }
                break;

            case R.id.recharge_check:
                if (recharge_check.isChecked()) {
                    recharge_button.setClickable(true);
                    recharge_button.setBackgroundResource(R.drawable.product_buy_clickable);
                } else {
                    recharge_button.setClickable(false);
                    recharge_button.setBackgroundResource(R.drawable.product_buy);
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void initView() {
        setTitle("??????");
        strMessage = "???????????????";
        bank_icon = findViewById(R.id.bank_icon);
        bank_id = findViewById(R.id.bank_id);
        recharge_money = findViewById(R.id.recharge_money);
        bank_name = findViewById(R.id.bank_name);
        money_number = findViewById(R.id.money_number);
        sms_edit = findViewById(R.id.sms_edit);
        accreditedDiff = findViewById(R.id.accreditedDiff);
        recharge_mode = findViewById(R.id.recharge_mode);
        recharge_check = findViewById(R.id.recharge_check);
        recharge_check.setOnClickListener(this);
        hMaxAmount_tv = findViewById(R.id.hMaxAmount_tv);
        maxAmount_tv = findViewById(R.id.maxAmount_tv);
        mMaxAmount_tv = findViewById(R.id.mMaxAmount_tv);
        sms_button = findViewById(R.id.sms_button);
        sms_button.setOnClickListener(this);
        sms_layout = findViewById(R.id.sms_layout);
        memo = findViewById(R.id.memo);
        agreement_layout = findViewById(R.id.agreement_layout);

        recharge_money.addTextChangedListener(new RechargeAmountConversion(recharge_money,money_number));
        recharge_button = findViewById(R.id.recharge_button);
        recharge_button.setOnClickListener(this);
        tv_agreement = findViewById(R.id.tv_agreement);
        agreementInfo = new AgreementInfo();
        textWatcherInPut = new LimitFocusChangeInPut(false, sms_edit);
        sms_edit.setOnFocusChangeListener(textWatcherInPut);
        textWatcherInPut.setInPutState(new LimitFocusChangeInPut.InPutState() {
            @Override
            public void state() {
                showDialog("??????????????????????????????????????????????????????????????????~");
            }
        });

        mCustomProgressDialog = getCustomProgressDialog(this, "???????????????...");

        initAgreement(tv_agreement);
    }


    private void initAgreement(TextView view) {


        String str = "?????????????????????????????????";

        String agreement = "?????????????????????";

        StringBuffer buffer = new StringBuffer(str);
        buffer.append(agreement);

        SpannableStringBuilder spanString = new SpannableStringBuilder();

        spanString.append(buffer.toString());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("agreementInfo", agreementInfo);
                Intent intent = new Intent();
                intent.putExtra("data", bundle);
                intent.setClass(RechargeActivity.this, AgreementActivity.class);
                baseStartActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //????????????????????????????????????Span

        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.agreement));
        //?????????Span??????????????????????????????
        spanString.setSpan(span, buffer.length() - agreement.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spanString.setSpan(clickableSpan, buffer.length() - agreement.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanString);

    }


    @Override
    protected RechargePresenter createPresenter() {
        return presenter = new RechargePresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.recharge_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge;
    }
}
