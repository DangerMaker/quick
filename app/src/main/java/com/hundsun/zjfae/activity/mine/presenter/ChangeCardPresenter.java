package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.ChangeCardView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import onight.zjfae.afront.gens.BankCardChangeManage;
import onight.zjfae.afront.gens.BankSmsCheckCod;
import onight.zjfae.afront.gens.CancelApplication;

public class ChangeCardPresenter extends BasePresenter<ChangeCardView> {


    private String payChannelNo = "";//支付渠道
    private String checkCodeSerialNo = "";//短信验证码流水
    public ChangeCardPresenter(ChangeCardView baseView) {
        super(baseView);
    }

    public void setPayChannelNo(String payChannelNo) {
        CCLog.e("payChannelNo",payChannelNo);
        this.payChannelNo = payChannelNo;
    }


    //发送短信验证码

    public void requestChangeCodeSmsCode(String bankCardNumber){

        BankSmsCheckCod.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4changeBind.Builder builder =  BankSmsCheckCod.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4changeBind.newBuilder();

        builder.setBankCard(bankCardNumber);
        builder.setPayChannelNo(payChannelNo);

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.ChangeCodeSmsCode);


        addDisposable(apiServer.changeCodeSmsCode(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<BankSmsCheckCod.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4changeBind>(baseView) {
            @Override
            public void onSuccess(BankSmsCheckCod.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4changeBind smsCode) {
                if (smsCode.getReturnCode().equals("0000")){
                    checkCodeSerialNo = smsCode.getData().getSerialNo();
                    baseView.onChangeCodeSmsCode();
                }
                else {
                    baseView.showError(smsCode.getReturnMsg());
                }
            }
        });
    }


    /**
     * 换卡
     * bankCard 银行卡号
     * password 交易密码
     * checkCode 短信验证码
     * **/
    public void onChangeBank(String bankCard,String password,String checkCode,String tencentfaceOrder ){


        BankCardChangeManage.REQ_PBIFE_bankcardmanage_changeBankCard.Builder builder = BankCardChangeManage.REQ_PBIFE_bankcardmanage_changeBankCard.newBuilder();

        builder.setBankCard(bankCard);
        builder.setPassword(password);
        builder.setCheckCode(checkCode);
        builder.setPayChannelNo(payChannelNo);
        builder.setCheckCodeSerialNo(checkCodeSerialNo);
        builder.setFaceId("1");
        builder.setTencentfaceOrder(tencentfaceOrder);

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.ChangeBankCardManage);


        addDisposable(apiServer.onChangeBank(url, getBody(builder.build().toByteArray())), new BaseBankProtoBufObserver<BankCardChangeManage.Ret_PBIFE_bankcardmanage_changeBankCard>(baseView) {
            @Override
            public void onSuccess(BankCardChangeManage.Ret_PBIFE_bankcardmanage_changeBankCard changeBankCard) {

                baseView.onChangeBank(changeBankCard.getReturnCode(),changeBankCard.getReturnMsg());
            }
        });

    }




    /**
     * 取消解绑申请
     * 取消unbindCard解绑银行卡
     * 取消changeCard换卡申请
     * type:区分解绑和换卡申请,0解绑卡，1申请换卡
     * */
    public void cleanUnbindBankCard(){
        CancelApplication.REQ_PBIFE_userinfomanage_cancelApplication.Builder builder =
                CancelApplication.REQ_PBIFE_userinfomanage_cancelApplication.newBuilder();
        builder.setCancelBusinessType("changeCard");
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.CancelApplication,getRequestMap());

        addDisposable(apiServer.cleanUnbindBankCard(url, getBody(builder.build().toByteArray())), new BaseBankProtoBufObserver<CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication>(baseView) {

            @Override
            public void onSuccess(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication ret_pbife_userinfomanage_cancelApplication) {

                baseView.cancelChangeCard(ret_pbife_userinfomanage_cancelApplication);
            }
        });
    }


}
