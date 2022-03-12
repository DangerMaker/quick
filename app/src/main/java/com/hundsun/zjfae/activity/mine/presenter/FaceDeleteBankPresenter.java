package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.FaceDeleteBankView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import onight.zjfae.afront.gens.BankCardManageUnBindBankCard;
import onight.zjfae.afront.gens.PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind;

public class FaceDeleteBankPresenter extends BasePresenter<FaceDeleteBankView> {

    //短信验证码流水号
    private String checkCodeSerialNo = "";
    private String payChannelNo = "";

    public FaceDeleteBankPresenter(FaceDeleteBankView baseView) {
        super(baseView);
    }


    public void setPayChannelNo(String payChannelNo) {

        CCLog.e("payChannelNo", payChannelNo + "FaceDeleteBankPresenter");

        this.payChannelNo = payChannelNo;
    }

    /**
     * 强制解绑银行卡
     * **/
    public void deleteBankCard(final String smsCode, String reasonDetails, String unbindCardReason,String tencentfaceOrder) {

        BankCardManageUnBindBankCard.REQ_PBIFE_bankcardmanage_unbindBankCard.Builder builder = BankCardManageUnBindBankCard.REQ_PBIFE_bankcardmanage_unbindBankCard.newBuilder();

        builder.setCheckCode(smsCode);
        builder.setFaceId("1");
        builder.setPayChannelNo(payChannelNo);
        builder.setReasonDetails(reasonDetails);
        builder.setCheckCodeSerialNo(checkCodeSerialNo);
        builder.setUnbindCardReason(unbindCardReason);
        builder.setTencentfaceOrder(tencentfaceOrder);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.FaceUnbindBankCard, getRequestMap());
        addDisposable(apiServer.faceDeleteBank(url, getBody(builder.build().toByteArray())), new BaseBankProtoBufObserver<BankCardManageUnBindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCard>(baseView, "银行处理中...") {
            @Override
            public void onSuccess(BankCardManageUnBindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCard deleteBankCard) {
                baseView.onDeleteBankCard(deleteBankCard);
            }
        });

    }


    /***
     * 解绑短信
     * */
    public void unBindBankCardSMS() {
        PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind.Builder builder = PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind.newBuilder();

        builder.setPayChannelNo(payChannelNo);
        builder.setFaceId("1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.unBindCardSMS, getRequestMap());

        addDisposable(apiServer.unBindBankCardSMS(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind bankSmsCheckCode) {

                checkCodeSerialNo = bankSmsCheckCode.getData().getSerialNo();
                baseView.onDeleteBankSms();

            }
        });
    }
}
