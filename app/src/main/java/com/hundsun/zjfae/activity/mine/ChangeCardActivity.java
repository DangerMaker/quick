package com.hundsun.zjfae.activity.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.AgreementInfo;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.ChangeCardPresenter;
import com.hundsun.zjfae.activity.mine.view.ChangeCardView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.LimitFocusChangeInPut;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.hundsun.zjfae.common.view.dialog.CustomProgressDialog;

import onight.zjfae.afront.gens.CancelApplication;

public class ChangeCardActivity extends CommActivity<ChangeCardPresenter> implements ChangeCardView , View.OnClickListener {

    private EditText bank_number,password,smsCode;

    private Button smsButton,change_bank_button;

    private LinearLayout smsCodeLayout;


    private CustomProgressDialog mCustomProgressDialog;

    private ConfigurationUtils faceUtils;

    private CheckBox recharge_check;

    private CustomCountDownTimer countDownTimer;

    private AgreementInfo agreementInfo;

    private TextView cancel_tv,tv_agreement,bank_title;

    private   String msg = "";

    private LimitFocusChangeInPut textWatcherInPut;

    @Override
    public void showLoading() {
    }


    private void customShowLoading(){
        if (mCustomProgressDialog != null && !mCustomProgressDialog.isShowing()){
            mCustomProgressDialog.show();
        }
    }

    @Override
    public void showError(String msg) {
        hideLoading();
        super.showError(msg);
    }

    @Override
    public void hideLoading() {

        if (mCustomProgressDialog != null && mCustomProgressDialog.isShowing()){
            mCustomProgressDialog.dismiss();
        }

    }

    @Override
    public void initData() {



        Bundle bundle = getIntent().getBundleExtra("faceBundle");

        if (bundle != null){
            faceUtils = bundle.getParcelable("face");
            if (faceUtils != null){
                //?????????????????????
                if (faceUtils.isNeedSms()){
                    smsCodeLayout.setVisibility(View.VISIBLE);
                }
                else {
                    smsCodeLayout.setVisibility(View.GONE);
                }

                agreementInfo.setAccount(faceUtils.getAccount());
                agreementInfo.setCertificateCode(faceUtils.getCertificateCodeAll());
                agreementInfo.setName(faceUtils.getName());
                agreementInfo.setBankName(faceUtils.getBankName());

                presenter.setPayChannelNo(faceUtils.getPayChannelNo());

                //???????????????????????????????????????
                if ( faceUtils.getBusinessType().equals("")){
                    cancel_tv.setVisibility(View.GONE);
                }
                else {
                    cancel_tv.setVisibility(View.VISIBLE);

                    if (faceUtils.getBusinessType().equals("true")){

                        msg = "?????????????????????????????????????????????????????????????????????????????????????????????";
                    }
                    else if (faceUtils.getBusinessType().equals("false")){

                        msg = "?????????????????????????????????????????????????????????????????????????????????";
                    }

                }
            }
        }


    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.smsButton:

                if (Utils.isViewEmpty(bank_number)){
                    showDialog("?????????????????????");
                }
                else {
                    customShowLoading();
                    presenter.requestChangeCodeSmsCode(bank_number.getText().toString());
                }

                break;
            case R.id.change_bank_button:


                if (!recharge_check.isChecked()){
                    showDialog("??????????????????????????????");
                    return;
                }

                if (Utils.isViewEmpty(bank_number)){
                    showDialog("?????????????????????");
                    return;
                }


                if (Utils.isViewEmpty(password)){
                    showDialog("?????????????????????");
                    return;
                }
                if (faceUtils != null && faceUtils.isNeedSms()){
                    if (Utils.isViewEmpty(smsCode)){
                        showDialog("????????????????????????");
                        return;
                    }
                }

                customShowLoading();
                presenter.onChangeBank(bank_number.getText().toString(), EncDecUtil.AESEncrypt(password.getText().toString()),smsCode.getText().toString(),faceUtils.getTencentfaceOrder());
                break;

            case R.id.recharge_check:

                if (recharge_check.isChecked()){
                    change_bank_button.setClickable(true);
                    change_bank_button.setBackgroundResource(R.drawable.product_buy_clickable);
                }
                else {
                    change_bank_button.setClickable(false);
                    change_bank_button.setBackgroundResource(R.drawable.product_buy);
                }

                break;



            case R.id.cancel_tv:
                showDialog(msg, "??????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                        if (faceUtils.getBusinessType().equals("false")){
                            presenter.cleanUnbindBankCard();
                        }
                        else {
                            HomeActivity.show(ChangeCardActivity.this,HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                        }




                    }
                });

                break;
                default:
                    break;
        }
    }


    @Override
    public void initView() {

        setTitle("???????????????");
        bank_number = findViewById(R.id.bank_number);
        password = findViewById(R.id.password);
        smsCode = findViewById(R.id.smsCode);
        smsButton = findViewById(R.id.smsButton);
        smsButton.setOnClickListener(this);
        smsCodeLayout = findViewById(R.id.smsCodeLayout);
        change_bank_button = findViewById(R.id.change_bank_button);
        change_bank_button.setOnClickListener(this);
        recharge_check = findViewById(R.id.recharge_check);
        recharge_check.setOnClickListener(this);
        tv_agreement = findViewById(R.id.tv_agreement);
        bank_title = findViewById(R.id.bank_title);
        cancel_tv = findViewById(R.id.cancel_tv);
        cancel_tv.setOnClickListener(this);
        mCustomProgressDialog = new CustomProgressDialog(this,"??????????????????");
        mCustomProgressDialog.setCanceledOnTouchOutside(false);

        agreementInfo = new AgreementInfo();

        countDownTimer = new CustomCountDownTimer(60000, 1000,smsButton );
        textWatcherInPut = new LimitFocusChangeInPut(false,smsCode);
        smsCode.setOnFocusChangeListener(textWatcherInPut);
        textWatcherInPut.setInPutState(new LimitFocusChangeInPut.InPutState() {
            @Override
            public void state() {
                showDialog("??????????????????????????????????????????????????????????????????~");
            }
        });
        initAgreement(tv_agreement);
        initTitle(bank_title);
    }


    private void initAgreement(TextView view){

        String str = "?????????????????????????????????";

        String agreement = "?????????????????????";

        StringBuffer buffer = new StringBuffer(str);
        buffer.append(agreement);

        SpannableStringBuilder spanString = new SpannableStringBuilder();

        spanString.append(buffer.toString());

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {


                if (Utils.isViewEmpty(bank_number)){
                    showDialog("?????????????????????");
                }
                else {

                    agreementInfo.setBankCard(bank_number.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("agreementInfo", agreementInfo);
                    Intent intent = new Intent();
                    intent.putExtra("data", bundle);
                    intent.setClass(ChangeCardActivity.this, AgreementActivity.class);
                    baseStartActivity(intent);

                }


            }

            @Override
            public void updateDrawState( TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        //????????????????????????????????????Span

        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.agreement));
        //?????????Span??????????????????????????????
        spanString.setSpan(span, buffer.length() - agreement.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE );

        spanString.setSpan(clickableSpan, buffer.length() - agreement.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanString);

    }


    private void initTitle(TextView view){


        String str = "??????????????????????????????????????????????????????";

        String agreement = "?????????";

        StringBuffer buffer = new StringBuffer(str);
        buffer.append(agreement);

        SpannableStringBuilder spanString = new SpannableStringBuilder();

        spanString.append(buffer.toString());


        //????????????????????????????????????Span

        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.colorRed));
        //?????????Span??????????????????????????????
        spanString.setSpan(span, buffer.length() - agreement.length(), buffer.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE );


        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(spanString);

    }


    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.change_card_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_card;
    }

    @Override
    protected ChangeCardPresenter createPresenter() {
        return new ChangeCardPresenter(this);
    }




    @Override
    public void onChangeCodeSmsCode() {
        countDownTimer.start();
        textWatcherInPut.setFlag(true);
        showDialog("???????????????????????????");
    }

    @Override
    public void onChangeBank(String returnCode, String returnMsg) {
        hideLoading();
        if (returnCode.equals("0000")){

            baseStartActivity(this,ChangeCardStateActivity.class);
            finish();

        }
        else {
            showDialog(returnMsg);
        }


    }

    //????????????
    @Override
    public void cancelChangeCard(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication cancelApplication) {

        String returnMsg = cancelApplication.getReturnMsg();

        if (cancelApplication.getReturnCode().equals("0000")){
            returnMsg = " ??????????????????????????????";

            showDialog(returnMsg, "?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    HomeActivity.show(ChangeCardActivity.this,HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                }
            });
        }
        else {
            showDialog(returnMsg);
        }




    }
}
