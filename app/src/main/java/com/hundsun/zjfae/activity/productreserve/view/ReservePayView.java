package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.RechargeBankCardInfo;

/**
 * @Description:长期预约 短期预约 支付预约金（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface ReservePayView extends BaseView {
    //获取支付金额等金额
    void playInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit);

    //获取银行卡信息
    void onBankInfo(PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard);

    //查询是否需要短信验证码
    void querySms(String returnCode, String returnMsg, String needSms);

    //发送验证码
    void sendCode(String returnCode, String returnMsg,String SerialNo);

    /**
     * 支付结果
     * @param returnCode 返回code码
     * @param returnMsg 返回信息
     * @param data
     * */
    void playOrder(String returnCode, String returnMsg,String data);

    /*
     * 银行卡信息
     * */
    void queryRechargeBankInfo( RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo);
    /*
     * 限额信息
     * */
    void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo);



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


}
