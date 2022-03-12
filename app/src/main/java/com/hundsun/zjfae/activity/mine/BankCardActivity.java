package com.hundsun.zjfae.activity.mine;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.bean.ConfigurationUtils;
import com.hundsun.zjfae.activity.mine.presenter.BankCardPresenter;
import com.hundsun.zjfae.activity.mine.view.BankCardView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.dialog.SendSmsCodeDialog;
import com.hundsun.zjfae.common.view.popwindow.PlayWindow;

import onight.zjfae.afront.gens.DeleteBankCard;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.PBIFEBankcardmanageUnbindBankCardForUserOper;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.mine
 * @ClassName: BankCardActivity
 * @Description: 用户银行卡管理界面
 * @Author: moran
 * @CreateDate: 2019/6/11 15:02
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/11 15:02
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BankCardActivity extends CommActivity<BankCardPresenter> implements BankCardView, View.OnClickListener {


    private LinearLayout isBankCode;
    private RelativeLayout add_bank_layout;

    private TextView bank_name, bank_id, bank_type;
    private PlayWindow play;
    private ImageView bank_logo;
    private Button replace_bank;


    private boolean isPlayPassWordState = false, isNeedSms = false, isZjSendSms = false, isNeedIdCard = false;


    //身份证号,渠道信息，银行卡名字
    private String certificateCodeAll, bankName;


    private LinearLayout close_bank_layout;

    private TextView close_bank_tv_hint;

    private SendSmsCodeDialog.Builder bankSmsDialog;
    //渠道信息
    private String payChannelNo = "";
    //姓名
    private String name = "";
    //资金账号
    private String account = "";
    private String changeCardStatus = "";
    private String unbindCardStatus = "";

    /**
     * 短信验证码
     **/
    private String smsCode = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bank_card;
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.initUserBankInfo();
    }

    @Override
    public void initView() {
        setTitle("银行卡管理");
        isBankCode = findViewById(R.id.isBankCode);
        add_bank_layout = findViewById(R.id.add_bank_layout);
        add_bank_layout.setOnClickListener(this);
        findViewById(R.id.bank_deal).setOnClickListener(this);
        findViewById(R.id.unbind_bank).setOnClickListener(this);
        bank_name = findViewById(R.id.bank_name);
        bank_id = findViewById(R.id.bank_id);
        bank_type = findViewById(R.id.bank_type);
        bank_logo = findViewById(R.id.bank_logo);
        replace_bank = findViewById(R.id.replace_bank);
        replace_bank.setOnClickListener(this);
        close_bank_layout = findViewById(R.id.close_bank_layout);
        close_bank_tv_hint = findViewById(R.id.close_bank_tv_hint);
        findViewById(R.id.bank_support).setOnClickListener(this);

        mCustomProgressDialog = getCustomProgressDialog(this, "银行处理中...");
    }

    @Override
    protected BankCardPresenter createPresenter() {
        return new BankCardPresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.bank_manage_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    /**
     * 用户银行卡信息
     */
    @Override
    public void onUserBankInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard, String payChannelNo, String bankName) {

        if (userDetailInfo == null) {
            return;
        }
        //支付渠道
        this.payChannelNo = payChannelNo;
        //银行名字
        this.bankName = bankName;
        //需要身份证
        this.isNeedIdCard = presenter.isNeedIdcard;

        //银行卡换卡申请状态
        changeCardStatus = userDetailInfo.getData().getChangeCardStatus();
        //银行卡解卡审核状态
        unbindCardStatus = userDetailInfo.getData().getUnbindCardStatus();

        //身份证名字
        name = userDetailInfo.getData().getName();
        //身份证号码
        certificateCodeAll = userDetailInfo.getData().getCertificateCodeAll();
        //资金账号
        account = userDetailInfo.getData().getAccount();

        CCLog.e("account", account);

        CCLog.e("payChannelNo", payChannelNo + "");


        UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfoData = userDetailInfo.getData();
        //绑卡了
        if (userDetailInfoData.getIsBondedCard().equals("true")) {

            isBankCode.setVisibility(View.VISIBLE);
            add_bank_layout.setVisibility(View.GONE);

            //设置交易密码
            if (userDetailInfo.getData().getIsFundPasswordSet().equals("true")) {
                isPlayPassWordState = true;
            }

            PBIFEBankcardmanageQueryUserBankCard.PBIFE_bankcardmanage_queryUserBankCard bankInfo = bankCard.getData();

            for (PBIFEBankcardmanageQueryUserBankCard.PBIFE_bankcardmanage_queryUserBankCard.TcCustomerChannelList channelList : bankInfo.getTcCustomerChannelListList()) {
                bank_name.setText(channelList.getBankName());
                bank_id.setText(channelList.getBankCard());
                String iconUrl = presenter.downloadImage(channelList.getBankCode());
                ImageLoad.getImageLoad().LoadImage(this, iconUrl, bank_logo);

                //查询银行渠道信息
                presenter.bankChannelInfo(channelList.getBankCode());
                String needSms = channelList.getNeedSms();
                if (needSms.equals("true")) {
                    //需要
                    isNeedSms = true;
                    isZjSendSms = channelList.getIsZjSendSms().equals("true");
                } else {
                    //不需要短信验证码
                    isNeedSms = false;
                }

                if (channelList.getBankCode().equals("0002") && channelList.getNeedUpdate().equals("false")) {
                    replace_bank.setVisibility(View.VISIBLE);
                } else {
                    replace_bank.setVisibility(View.GONE);
                }
            }

        }
        //未绑卡
        else if (userDetailInfoData.getIsBondedCard().equals("false")) {
            add_bank_layout.setVisibility(View.VISIBLE);
            isBankCode.setVisibility(View.GONE);
        }
    }


    @Override
    public void bankChannelInfo() {

        /**
         * 渠道关闭code码（1067）在BaseBankProtoBufObserver拦截并回调onBankChannelClose()方法
         * */

    }

    @Override
    public void onBankChannelClose() {

        close_bank_layout.setVisibility(View.VISIBLE);

        StringBuffer buffer = new StringBuffer("您绑定的");

        buffer.append(bankName).append("卡解绑后将不再支持重新绑定,建议您在解绑后绑定以下")
                .append("“可支持银行卡”。");

        close_bank_tv_hint.setText(buffer.toString());
    }

    /**
     * 获取短信验证码成功回调
     */
    @Override
    public void onDeleteBankSms() {

        bankSmsDialog.setSmsButtonState(true);


    }

    @Override
    public void onChangeBank() {

        changeBankCard("", ChangeCardActivity.class);

    }

    @Override
    public void onFaceChangeBank() {
        if (unbindCardStatus.equals("3")) {
            String msg = "您还有待完成的解绑卡流程，请先完成解绑卡流程再进行此操作。";
            showDialog( msg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
        } else {
            //  资料/人脸识别换卡
            startBankCardManagementActivity("changeCard", BankCardManagementActivity.class);
        }
    }

    @Override
    public void onDeleteBank() {
        //是否需要短信验证码
        if (isNeedSms) {
            bankSmsDialog = new SendSmsCodeDialog.Builder(this);
            bankSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
                @Override
                public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                    if (Utils.isStringEmpty(smsCode)) {
                        showDialog("请输入短信验证码");
                    } else {
                        dialog.dismiss();
                        BankCardActivity.this.smsCode = smsCode;
                        play = new PlayWindow(BankCardActivity.this);
                        play.setPayListener(new PlayWindow.OnPayListener() {
                            @Override
                            public void onSurePay(String password) {
                                presenter.deleteBankCard(EncDecUtil.AESEncrypt(password), smsCode);
                            }
                        });

                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        play.showAtLocation(findViewById(R.id.bank_manage_layout));

                    }
                }

                @Override
                public void onSmsClick() {

                    presenter.unBindBankCardSMS("");

                }
            });

            bankSmsDialog.show();

        } else {
            play = new PlayWindow(BankCardActivity.this);
            play.setPayListener(new PlayWindow.OnPayListener() {
                @Override
                public void onSurePay(String password) {
                    BankCardActivity.this.smsCode = "";
                    presenter.deleteBankCard(EncDecUtil.AESEncrypt(password), "");
                }
            });
            play.showAtLocation(findViewById(R.id.bank_manage_layout));


            //play.showAsDropDown(findViewById(R.id.bank_manage_layout));
        }

    }

    @Override
    public void onFaceDeleteBank(String returnCode, String returnMsg) {
        if (unbindCardStatus.equals("3")) {
            deleteBankCardForUserOper();
        } else {
            startBankCardManagementActivity("unbindCard", BankCardManagementActivity.class);
        }
    }

    @Override
    public void onDeleteBankCardBean(DeleteBankCard.Ret_PBIFE_bankcardmanage_deleteBankCard deleteBankCard) {
        String returnCode = deleteBankCard.getReturnCode();
        CCLog.e(returnCode);
        String returnMsg = deleteBankCard.getReturnMsg();
        if (returnCode.equals("0000")) {
            bankSmsDialog.setSmsButtonState(false);
            baseStartActivity(this, DeleteBankStateActivity.class);
        }


    }

    @Override
    public void onDeleteBankCardForUserOper(PBIFEBankcardmanageUnbindBankCardForUserOper.Ret_PBIFE_bankcardmanage_unbindBankCardForUserOper deleteBankCard) {
        String returnCode = deleteBankCard.getReturnCode();
        CCLog.e(returnCode);
        String returnMsg = deleteBankCard.getReturnMsg();
        if (returnCode.equals("0000")) {
            bankSmsDialog.setSmsButtonState(false);
            baseStartActivity(this, DeleteBankStateActivity.class);
        }

    }


    @Override
    public void onDeleteBankError(String returnCode, String returnMsg) {
        if (returnCode.equals(ConstantCode.ADD_BANK_TIME_OUT)) {
            showDialog(returnMsg, "知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        }

    }

    @Override
    public void showErrorDialog(String msg) {
        if (!smsCode.equals("")) {
            bankSmsDialog.show();
        }
        super.showErrorDialog(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //添加银行卡
            case R.id.add_bank_layout:
                baseStartActivity(this, AddBankActivity.class);
                break;
            //银行卡流水
            case R.id.bank_deal:
                baseStartActivity(this, BankCardDynamicActivity.class);
                break;
            //解绑银行卡
            case R.id.unbind_bank:

                if (!isPlayPassWordState) {
                    showDialog("您还未设置交易密码，不能解绑操作", "设置交易密码", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            //去设置交易密码
                            dialog.dismiss();
                            baseStartActivity(BankCardActivity.this, FirstPlayPassWordActivity.class);
                        }
                    });
                } else {
                    presenter.deleteBankCardPrere();
                }

                break;
            //解绑短信验证码
            case R.id.smsButton:
                presenter.unBindBankCardSMS("");
                break;
            //更换银行卡
            case R.id.replace_bank:

                if (changeCardStatus.equals("1")) {

                    changeBankCard("false", ChangeCardActivity.class);
                } else {
                    presenter.changeBankCardPrere();
                }


                break;
            case R.id.bank_support:

                baseStartActivity(this, SupportBankActivity.class);

                break;
            default:
                break;
        }
    }


    /**
     * @param verifyscene 业务场景，解绑卡or换卡 --"unbindCard"//解绑,"changeCard"//换卡
     * @param cls         跳转Activity
     **/
    private void startBankCardManagementActivity(String verifyscene, Class<?> cls) {

        Intent intent = new Intent(this, cls);
        ConfigurationUtils faceUtils = new ConfigurationUtils();
        faceUtils.setName(name);
        faceUtils.setCertificateCodeAll(certificateCodeAll);
        faceUtils.setAccount(account);
        faceUtils.setNeedSms(isNeedSms);
        faceUtils.setZjSendSms(isZjSendSms);
        faceUtils.setNeedIdCard(isNeedIdCard);
        faceUtils.setBankName(bankName);
        faceUtils.setVerifyscene(verifyscene);
        faceUtils.setPartValue(verifyscene);
        faceUtils.setPayChannelNo(payChannelNo);

        Bundle bundle = new Bundle();
        bundle.putParcelable("face", faceUtils);
        intent.putExtra("faceBundle", bundle);
        baseStartActivity(intent);
    }


    private void changeBankCard(String businessType, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        ConfigurationUtils faceUtils = new ConfigurationUtils();
        faceUtils.setName(name);
        faceUtils.setCertificateCodeAll(certificateCodeAll);
        faceUtils.setAccount(account);
        faceUtils.setNeedSms(isNeedSms);
        faceUtils.setZjSendSms(isZjSendSms);
        faceUtils.setNeedIdCard(isNeedIdCard);
        faceUtils.setBankName(bankName);
        faceUtils.setPayChannelNo(payChannelNo);
        faceUtils.setBusinessType(businessType);
        Bundle bundle = new Bundle();
        bundle.putParcelable("face", faceUtils);
        intent.putExtra("faceBundle", bundle);
        baseStartActivity(intent);
    }

    //审核通过强制解绑
    private void deleteBankCardForUserOper() {
        bankSmsDialog = new SendSmsCodeDialog.Builder(this);
        bankSmsDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        bankSmsDialog.setSmsButtonOnClickListener(new SendSmsCodeDialog.Builder.SmsButtonOnClickListener() {
            @Override
            public void onEditTextSms(DialogInterface dialog, int which, final String smsCode) {
                if (Utils.isStringEmpty(smsCode)) {
                    showDialog("请输入短信验证码");
                } else {
                    dialog.dismiss();
                    BankCardActivity.this.smsCode = smsCode;
                    play = new PlayWindow(BankCardActivity.this);
                    play.setPayListener(new PlayWindow.OnPayListener() {
                        @Override
                        public void onSurePay(String password) {
                            presenter.deleteBankCardForUserOper(EncDecUtil.AESEncrypt(password), smsCode);
                        }
                    });

                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    play.showAtLocation(findViewById(R.id.bank_manage_layout));

                }
            }

            @Override
            public void onSmsClick() {

                presenter.unBindBankCardSMS("1");

            }
        });

        bankSmsDialog.show();
    }


}
