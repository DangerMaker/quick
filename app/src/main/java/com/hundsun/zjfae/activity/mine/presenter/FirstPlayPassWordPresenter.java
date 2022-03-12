package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.FirstPlayPassWordView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;

import onight.zjfae.afront.gens.TradePassword;

public class FirstPlayPassWordPresenter extends BasePresenter<FirstPlayPassWordView> {

    public FirstPlayPassWordPresenter(FirstPlayPassWordView baseView) {
        super(baseView);
    }

    public void setTradePassword(String playPassWord,String verifyPlayPassWord){
        TradePassword.REQ_PBIFE_passwordmanage_setTradePassword.Builder trade
                = TradePassword.REQ_PBIFE_passwordmanage_setTradePassword.newBuilder();
        trade.setPassword(EncDecUtil.AESEncrypt(playPassWord));//需要加密
        trade.setPasswordSure(EncDecUtil.AESEncrypt(verifyPlayPassWord));//需要加密

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.FIRST_TradePassword,getRequestMap());

        addDisposable(apiServer.setTradePassword(url, getBody(trade.build().toByteArray())), new ProtoBufObserver<TradePassword.Ret_PBIFE_passwordmanage_setTradePassword>(baseView) {
            @Override
            public void onSuccess(TradePassword.Ret_PBIFE_passwordmanage_setTradePassword tradePassword) {
                String returnCode = tradePassword.getReturnCode();
                String returnMsg = tradePassword.getReturnMsg();
                baseView.tradePassword(returnCode,returnMsg);
            }
        });

    }

}
