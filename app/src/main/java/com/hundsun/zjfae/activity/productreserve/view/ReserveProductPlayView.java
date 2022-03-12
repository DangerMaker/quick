package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;

public interface ReserveProductPlayView extends BaseView {

    void init(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit, UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan quanBean);

    void playProduct(String returnCode, String returnMsg);

    void sendCode(String returnCode, String returnMsg, String serialNo);

    void onBankInfo(String bankName);//银行卡信息

    //查询是否需要短信验证码
    void querySms(String returnCode, String returnMsg, String needSms);
    /*
     * 银行卡信息
     * */
    void queryRechargeBankInfo( RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo);


    /*
     * 限额信息
     * */
    void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo);


    /**
     * 受让支付回调
     * @param returnCode 返回code码，0000成功
     * @param returnMsg 返回信息
     *
     * **/
    void playTransferDetail(String returnCode, String returnMsg);


    /**
     * 充值失败
     *  @param returnMsg 充值失败信息
     * */
    void onFundRechargeError(String returnMsg);




    /**
     * 引导用户绑卡
     * @param returnMsg 返回信息
     * */
    void onGuideAddBank(String returnMsg);


    /**
     * 卡券使用说明
     * @param dictionary 卡券使用说明
     * */
    void onKQDescription(Dictionary.Ret_PBAPP_dictionary dictionary);
}
