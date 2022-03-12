package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.BankCardManageUnBindBankCard;

/**
 * @author moran
 * */
public interface FaceDeleteBankView extends BaseView {



    /**
     * 解绑银行卡信息
     * @param deleteBankCard 解绑银行卡信息
     * **/
    void onDeleteBankCard(BankCardManageUnBindBankCard.Ret_PBIFE_bankcardmanage_unbindBankCard deleteBankCard);

    /**
     * 解绑银行卡短信
     * */
    void onDeleteBankSms();


    /**
     * 解绑银行卡异常
     * @param returnCode 返回code
     * @param returnMsg 返回信息
     * */
    void onDeleteBankError(String returnCode,String returnMsg);

}
