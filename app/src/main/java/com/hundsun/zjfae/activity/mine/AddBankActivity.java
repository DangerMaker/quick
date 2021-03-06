package com.hundsun.zjfae.activity.mine;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.AddBanInfo;
import com.hundsun.zjfae.activity.mine.bean.AgreementInfo;
import com.hundsun.zjfae.activity.mine.bean.FaceIdCardBean;
import com.hundsun.zjfae.activity.mine.presenter.AddBankPresenter;
import com.hundsun.zjfae.activity.mine.view.AddBankView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.LimitFocusChangeInPut;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;
import com.zjfae.captcha.face.IDCardResult;
import com.zjfae.captcha.face.TencentOCRCallBack;
import com.zjfae.captcha.face.TencentWbCloudOCRSDK;

import java.util.List;

import onight.zjfae.afront.gens.AcquireBankSmsCheckCode;
import onight.zjfae.afront.gens.AddBankCard;
import onight.zjfae.afront.gens.BasicInfo;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryBankInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.TencentOcrId;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;


/**
 *
 * ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine
 * @ClassName:      AddBankActivity
 * @Description:     ???????????????
 * @Author:         moran
 * @CreateDate:     2019/6/13 15:05
 * @UpdateUser:     ????????????
 * @UpdateDate:     2019/6/13 15:05
 * @UpdateRemark:   ???????????????
 * @Version:        1.0
 *
 */
@RuntimePermissions
public class AddBankActivity extends CommActivity<AddBankPresenter> implements View.OnClickListener, AddBankView {


    private EditText id_name, phone_number, bank_card, id_card, needSms_ed;
    private LinearLayout needSms_layout;
    private TextView bank_name, bank_memo, spdb_protocol, hMaxAmount_tv, maxAmount_tv, mMaxAmount_tv;
    private Button needSms_button;
    private CustomCountDownTimer countDownTimer;
    private ImageView bank_icon;
    private CheckBox add_card_check;

    private ImageView id_card_name_layout, id_card_layout;


    private static final int REQUESTCODE = 0x1011;
    private static final int RESULT_CODE = 0x780;
    private Button determine_binding;

    private AgreementInfo agreementInfo;

    /**
     *?????????????????????T+1????????????????????????????????????????????????????????????????????????????????????
     * */
    private String availableFlag = "";
    private String returnStr = "";

    private boolean isRegister = false;

    private boolean isPlay = false;

    private LimitFocusChangeInPut limitFocusChangeInPut;

    private String bankName = "";

    private TextView tv_agreement;

    @Override
    public void initData() {
        presenter.initUserCardData();
        agreementInfo = new AgreementInfo();
        isRegister = getIntent().getBooleanExtra("register", false);
        isPlay = getIntent().getBooleanExtra("isPlay",false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //???????????????
            case R.id.id_card_name_layout:
            case R.id.id_card_layout:
                AddBankActivityPermissionsDispatcher.onFaceIdCardDiscernWithPermissionCheck(AddBankActivity.this);
                //network();
                break;
            //????????????
            case R.id.determine_binding:
                if (Utils.isViewEmpty(id_name)) {
                    showDialog("?????????????????????");
                } else if (Utils.isViewEmpty(phone_number)) {
                    showDialog("??????????????????");
                } else if (Utils.isViewEmpty(bank_card)) {
                    showDialog("?????????????????????");
                } else if (bank_name.getText().toString().contains("???????????????")) {
                    showDialog("????????????????????????");

                } else if (Utils.isViewEmpty(id_card)) {
                    showDialog("?????????????????????");
                } else if (needSms_layout.getVisibility() == View.VISIBLE && TextUtils.isEmpty(needSms_ed.getText().toString())) {
                    showDialog("????????????????????????");
                } else if (!add_card_check.isChecked()) {
                    showDialog("?????????????????????????????????");
                } else {
                    if (availableFlag.equals("1")) {
                        showDialog(returnStr, "???????????????????????????", "????????????", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                bank_card.setText("");
                                bank_name.setText("???????????????");

                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                mCustomProgressDialog.show("???????????????...");
                                presenter.addBankCard(bank_card.getText().toString(),bankName,
                                        needSms_ed.getText().toString(), id_name.getText().toString(), id_card.getText().toString());
                            }
                        });

                    } else {
                        mCustomProgressDialog.show("???????????????...");
                        presenter.addBankCard(bank_card.getText().toString(),bankName,
                                needSms_ed.getText().toString(), id_name.getText().toString(), id_card.getText().toString());
                    }
                }
                break;
            //???????????????
            case R.id.bank_deal:
                baseStartActivity(this, SupportBankActivity.class);
                break;
            //??????????????????????????????????????????????????????????????????
            case R.id.spdb_protocol:
                if (Utils.isViewEmpty(id_name)) {
                    showDialog("??????????????????????????????????????????~");
                } else if (Utils.isViewEmpty(phone_number)) {
                    showDialog("??????????????????????????????????????????~");
                } else if (Utils.isViewEmpty(bank_card)) {
                    showDialog("??????????????????????????????????????????~");
                } else if (Utils.isViewEmpty(id_card)) {
                    showDialog("??????????????????????????????????????????~");
                } else if (bank_name.getText().toString().contains("???????????????")) {

                    showDialog("??????????????????????????????????????????~");
                }
                else {
                    Intent intent = new Intent(this,OpenAssetsAttachmentActivity.class);
                    intent.putExtra("assetsName","BankofShanghai.pdf");
                    intent.putExtra("title","????????????????????????????????????????????????");
                    baseStartActivity(intent);
                }

                break;
            //??????????????????
            case R.id.add_card_check:
                if (add_card_check.isChecked()){
                    determine_binding.setClickable(true);
                    determine_binding.setBackgroundResource(R.drawable.product_buy_clickable);
                }
                else {
                    determine_binding.setClickable(false);
                    determine_binding.setBackgroundResource(R.drawable.product_buy);
                }

                break;
            default:
                break;
        }


    }

    /**
     * ??????????????????
     * */
    @Override
    public void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        agreementInfo.setAccount(userDetailInfo.getData().getAccount());
    }

    /**
     * ??????????????????
     * */
    @Override
    public void onUserBasicInfo(BasicInfo.Ret_PBIFE_userbaseinfo_getBasicInfo basicInfo) {


        BasicInfo.PBIFE_userbaseinfo_getBasicInfo data = basicInfo.getData();
        //???GR???????????????
        if (!data.getMobile().contains(BasePresenter.DEFAULT_KEY)) {
            phone_number.setText(data.getMobile());
        }
        if (!data.getName().contains(BasePresenter.DEFAULT_KEY)) {
            id_name.setEnabled(false);
            id_name.setText(data.getName());
            id_card_name_layout.setVisibility(View.GONE);

        }
        else {
            id_card_name_layout.setVisibility(View.VISIBLE);
        }
        if (!data.getCertificateCode().contains(BasePresenter.DEFAULT_KEY)) {
            id_card.setEnabled(false);
            id_card.setText(data.getCertificateCode());
            id_card_layout.setVisibility(View.GONE);
        }
        else {
            id_card_layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     *????????????????????????????????????????????????????????????
     * **/
    @Override
    public void onBindingBankBean(QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo queryBankInfo) {

        List<QueryBankInfo.PBIFE_bankcardmanage_queryBankInfo.TcBankDitchList> ditchLists = queryBankInfo.getData().getTcBankDitchListList();

        if (queryBankInfo.getReturnCode().equals("0000")){
            if (ditchLists.size() == 0) {
                QueryBankInfo.PBIFE_bankcardmanage_queryBankInfo.TCBankDitch tcBankDitch = queryBankInfo.getData().getTcBankDitch();
                bankName = tcBankDitch.getBankName();
                bank_name.setText(tcBankDitch.getBankName());
                bank_name.setClickable(false);
                //???????????????????????????????????????
                presenter.queryFundBankInfo(tcBankDitch.getBankCode());
                //???????????????icon
                String url = presenter.downloadImage(tcBankDitch.getBankCode());
                ImageLoad.getImageLoad().LoadImage(this, url, bank_icon);
            }
            else {
                bank_memo.setText("");
                bank_memo.setVisibility(View.GONE);
                hMaxAmount_tv.setText("");
                maxAmount_tv.setText("");
                mMaxAmount_tv.setText("");


                spdb_protocol.setVisibility(View.GONE);
                bank_name.setClickable(true);
                bank_name.setText("???????????????>");
                bank_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AddBankActivity.this, ChooseBankActivity.class);
                        startActivityForResult(intent, REQUESTCODE);
                    }
                });//????????????
                String url = presenter.downloadImage("0000");
                ImageLoad.getImageLoad().LoadImage(this, url, bank_icon);
            }
        }
        else {
            spdb_protocol.setVisibility(View.GONE);
            bank_name.setClickable(true);
            bank_name.setText("???????????????>");
            bank_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddBankActivity.this, ChooseBankActivity.class);
                    startActivityForResult(intent, REQUESTCODE);
                }
            });//????????????
            String url = presenter.downloadImage("0000");
            ImageLoad.getImageLoad().LoadImage(this, url, bank_icon);

        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE && resultCode == RESULT_CODE && data != null) {
            needSms_layout.setVisibility(View.GONE);
            Bundle bundle = data.getBundleExtra("bankInfoBundle");
            if (bundle != null) {
                AddBanInfo banInfo = bundle.getParcelable("bankInfo");
                if (banInfo != null) {
                    bank_name.setText(banInfo.getBankName());
                    bankName = banInfo.getBankName();
                    if (banInfo.getBankCodeNo().equals("0008")) {
                        //???????????????
                        spdb_protocol.setVisibility(View.VISIBLE);
                    } else {
                        spdb_protocol.setVisibility(View.GONE);
                    }

                    String url = presenter.downloadImage(banInfo.getBankCodeNo());
                    ImageLoad.getImageLoad().LoadImage(this, url, bank_icon);
                    presenter.setBankCode(banInfo.getBankCodeNo());
                    presenter.queryFundBankInfo(banInfo.getBankCodeNo());
                    String iconUrl = presenter.downloadImage(banInfo.getBankCodeNo());
                    ImageLoad.getImageLoad().LoadImage(this, iconUrl, bank_icon);


                }
            }
        }

    }

    /**
     * ??????????????????????????????????????????????????????????????????
     * */
    @Override
    public void onFundBankInfoBean(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
        if (fundBankInfo.getReturnCode().equals("0000")){
            FundBankInfo.PBIFE_bankcardmanage_queryFundBankInfo dataBean = fundBankInfo.getData();
            //????????????????????????
            String bankCode = dataBean.getFundBankDict().getBankId();
            //?????????????????????????????????????????????
            availableFlag = dataBean.getAvailableFlag();
            returnStr = dataBean.getReturnStr();
            if (dataBean.getFundBankDict().getMemo() != null) {
                bank_memo.setVisibility(View.VISIBLE);
                bank_memo.setText(dataBean.getFundBankDict().getMemo());
            }

            if (!bankCode.equals("")) {
                if (bankCode.equals("0008")) {
                    //???????????????
                    spdb_protocol.setVisibility(View.VISIBLE);
                } else {
                    spdb_protocol.setVisibility(View.GONE);
                }
            }


            //?????????????????????????????????
            if (dataBean.getFundBankDict().getNeedSms().equals("true")) {
                needSms_layout.setVisibility(View.VISIBLE);
                final FundBankInfo.PBIFE_bankcardmanage_queryFundBankInfo.FundBankDict fundBankDict = dataBean.getFundBankDict();
                needSms_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCustomProgressDialog.show();
                        presenter.requestAddBankNeedSms(bank_card.getText().toString(),
                                fundBankDict.getBankNo(), fundBankDict.getBankName(),
                                id_card.getText().toString(), id_name.getText().toString());
                    }
                });
            } else {
                if (needSms_layout.getVisibility() == View.VISIBLE) {
                    needSms_layout.setVisibility(View.GONE);
                }

            }
            hMaxAmount_tv.setText(fundBankInfo.getData().getFundBankDict().getHMaxAmount().length() != 0 ? fundBankInfo.getData().getFundBankDict().getHMaxAmount() : "?????????");
            maxAmount_tv.setText(fundBankInfo.getData().getFundBankDict().getMaxAmount().length() != 0 ? fundBankInfo.getData().getFundBankDict().getMaxAmount() : "?????????");
            mMaxAmount_tv.setText(fundBankInfo.getData().getFundBankDict().getMMaxAmount().length() != 0 ? fundBankInfo.getData().getFundBankDict().getMMaxAmount() : "?????????");

        }
        else {
            showDialog(fundBankInfo.getReturnMsg());
        }

    }


    /**
     * ???????????????????????????
     * */
    @Override
    public void onBankSmsCheckCodeBean(AcquireBankSmsCheckCode.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode acquireBankSmsCheckCode) {
        String returnCode = acquireBankSmsCheckCode.getReturnCode();
        String returnMsg = acquireBankSmsCheckCode.getReturnMsg();
        if (returnCode.equals("0000")) {
            countDownTimer.start();
            returnMsg = "???????????????????????????";
            limitFocusChangeInPut.setFlag(true);
        }
        showDialog(returnMsg);
    }

    @Override
    public void onAddBank(AddBankCard.Ret_PBIFE_bankcardmanage_addBankCard addBankCard) {
        if (addBankCard.getReturnCode().equals("0000")) {
            //????????????
            if (isRegister) {
                setResult(RESULT_OK);
                finish();
            }

            else if (isPlay){
                HomeActivity.show(this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
            }
            else {
                Intent intent = new Intent(this, AddBankStateActivity.class);
                intent.putExtra("isMine",getIntent().getBooleanExtra("isMine",false));
                baseStartActivity(intent);
                finish();

            }

        }


    }



    @Override
    public void onAddBankError(String returnCode, String returnMsg) {
        if (returnCode.equals(ConstantCode.ADD_BANK_TIME_OUT)){

            showDialog(returnMsg, "?????????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    if (isRegister) {
                        HomeActivity.show(AddBankActivity.this, HomeActivity.HomeFragmentType.HOME_FRAGMENT);
                    }
                    else {
                        HomeActivity.show(AddBankActivity.this, HomeActivity.HomeFragmentType.MINE_FRAGMENT);
                    }


                }
            });
        }

    }

    @Override
    public void showErrorDialog(String msg) {
        super.showErrorDialog(msg);
        bank_card.setFocusable(true);
    }

    //???????????????
    @Override
    public void onIdCardImage(FaceIdCardBean cardBean) {

        if (cardBean.getBody().getReturnCode() == null) {
            id_name.setText(cardBean.getBody().getName());
            id_card.setText(cardBean.getBody().getId_card_number());
        } else {
            showDialog(cardBean.getBody().getReturnMsg().toString());
        }
    }



    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onFaceIdCardDiscern() {
        presenter. queryFaceStatus();



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AddBankActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onFaceIdCardDiscernDenied() {

        showDialog(R.string.camera_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AddBankActivityPermissionsDispatcher.onFaceIdCardDiscernWithPermissionCheck(AddBankActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onFaceIdCardDiscernAgain() {

        showDialog(R.string.camera_permission_hint, R.string.setting, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(AddBankActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    private class BankIdTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() >= 15) {
                presenter.getCheckBankType(s.toString());
            }

        }
    }


    @Override
    public void onFaceStatus(boolean isTencentFace) {
        presenter.initTencentOcrId();


    }

    @Override
    public void onTencentOcrId(TencentOcrId.Ret_PBAPP_tencentID pbapp_tencentID) {
        final TencentOcrId.PBAPP_tencentID tencentID =  pbapp_tencentID.getData();
        presenter.setOrderNo(tencentID.getOrderNO());
        TencentWbCloudOCRSDK.getInstance().init(this).execute(tencentID.getOrderNO(), tencentID.getAppid(), tencentID.getNonce(), tencentID.getUserID(), tencentID.getSign(), new TencentOCRCallBack() {
            @Override
            public void onSuccess(IDCardResult idCardResult) {

                id_name.setText(idCardResult.getName());
                id_card.setText(idCardResult.getCardNum());
            }

            @Override
            public void onError(String errorMsg) {
                showError(errorMsg);
            }

            @Override
            public void onLoginTimeOut(String errorMsg) {
                showError(errorMsg);
            }

            @Override
            public void onTencentCallBack(String orderNo) {
                presenter.onTencentFaceCallback(orderNo,"IdCard");

            }
        },true);
    }

    private int side = 0;
    private boolean isVertical = false;
    private static final int INTO_IDCARDSCAN_PAGE = 100;



    @Override
    protected AddBankPresenter createPresenter() {
        return  new AddBankPresenter(this);
    }

    @Override
    public void initView() {
        setTitle("???????????????");
        id_name = findViewById(R.id.id_name);
        phone_number = findViewById(R.id.phone_number);
        bank_card = findViewById(R.id.bank_card);
        id_card = findViewById(R.id.id_card);
        id_card_name_layout = findViewById(R.id.id_card_name_layout);
        //???????????????
        id_card_name_layout.setOnClickListener(this);
        id_card_layout = findViewById(R.id.id_card_layout);
        //???????????????
        id_card_layout.setOnClickListener(this);
        determine_binding = findViewById(R.id.determine_binding);
        determine_binding.setOnClickListener(this);
        findViewById(R.id.bank_deal).setOnClickListener(this);
        bank_icon = findViewById(R.id.bank_icon);
        spdb_protocol = findViewById(R.id.spdb_protocol);
        spdb_protocol.setOnClickListener(this);
        hMaxAmount_tv = findViewById(R.id.hMaxAmount_tv);
        maxAmount_tv = findViewById(R.id.maxAmount_tv);
        mMaxAmount_tv = findViewById(R.id.mMaxAmount_tv);
        bank_name = findViewById(R.id.bank_name);
        bank_memo = findViewById(R.id.bank_memo);
        needSms_layout = findViewById(R.id.needSms_layout);
        needSms_ed = findViewById(R.id.needSms_ed);
        limitFocusChangeInPut = new LimitFocusChangeInPut(false,needSms_ed);

        limitFocusChangeInPut.setInPutState(new LimitFocusChangeInPut.InPutState() {
            @Override
            public void state() {
                showDialog("??????????????????????????????????????????????????????????????????~");
            }
        });
        needSms_ed.setOnFocusChangeListener(limitFocusChangeInPut);
        needSms_button = findViewById(R.id.needSms_button);
        add_card_check = findViewById(R.id.add_card_check);
        add_card_check.setOnClickListener(this);
        needSms_button.setOnClickListener(this);
        countDownTimer = new CustomCountDownTimer(60000, 1000, needSms_button);
        bank_card.addTextChangedListener(new BankIdTextWatcher());

        tv_agreement = findViewById(R.id.tv_agreement);

        initAgreement(tv_agreement);

        mCustomProgressDialog = getCustomProgressDialog(this,"???????????????...");
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

                if (Utils.isViewEmpty(id_name)) {
                    showDialog("?????????????????????????????????????????????");
                } else if (Utils.isViewEmpty(phone_number)) {
                    showDialog("?????????????????????????????????????????????");
                } else if (Utils.isViewEmpty(bank_card)) {
                    showDialog("?????????????????????????????????????????????");
                } else if (Utils.isViewEmpty(id_card)) {
                    showDialog("?????????????????????????????????????????????");
                } else if (bank_name.getText().toString().contains("???????????????")) {

                    showDialog("?????????????????????????????????????????????");
                } else {
                    agreementInfo.setName(id_name.getText().toString());
                    agreementInfo.setCertificateCode(id_card.getText().toString());
                    agreementInfo.setBankCard(bank_card.getText().toString());
                    agreementInfo.setBankName(bank_name.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("agreementInfo", agreementInfo);
                    Intent intent = new Intent();
                    intent.putExtra("data", bundle);
                    intent.setClass(AddBankActivity.this, AgreementActivity.class);
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

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.add_bank_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void showError(String msg) {

        showDialog(msg);
        bank_name.setClickable(true);
        //bank_name.setText("???????????????>");
        bank_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBankActivity.this, ChooseBankActivity.class);
                startActivityForResult(intent, REQUESTCODE);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_bank;
    }
}
