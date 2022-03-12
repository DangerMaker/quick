package com.hundsun.zjfae.activity.product.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @author moran
 * 受让支付
 * */
public interface TransferDetailPlayView extends BaseView {

    /**
     * 受让支付回调
     * @param returnCode 返回code码，0000成功
     * @param returnMsg 返回信息
     *
     * **/
    void playTransferDetail(String returnCode, String returnMsg);


    /**
     * 银行卡信息
     * @param bankName 银行名字
     * */
    void onBankInfo(String bankName);

    /**
     * 初始化受让支付信息
     * @param queryPayInit 用户可用余额，当前支付金额
     * */
    void onTransferPlayInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit);

    /**
     * 初始化卡券信息
     * @param queryQuanInfo 卡券信息
     * */
    void onQuanInfo(QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryQuanInfo);

    /**
     * 开启网易云盾
     * @param paraValue 是否开启网易云盾 00开启
     * @param senseFlag 是否智能无感知 true开启智能无感知，false 默认网易云盾验证模式
     * */
    void initCaptcha(String paraValue,String senseFlag );


    /**
     * 发送短信验证码
     * @param returnCode 返回code码
     * @param returnMsg 返现信息
     * **/
    void sendCode(String returnCode,String returnMsg);

    /**
     * 查询是否需要短信验证码
     * @param returnCode 返回code码
     * @param returnMsg 返回信息
     * @param needSms 是否需要短信验证码
     * */
    void querySms(String returnCode, String returnMsg, String needSms);




    /**
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     * */
    void queryRechargeBankInfo(String bankName, String bankCard,String bankNo,String showTips);

    /**
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     * @param payChannelNo 支付渠道
     * */
    void queryRechargeBankInfo(String bankName, String bankCard,String bankNo,String showTips,String payChannelNo);

    /**
     * 限额信息
     * @param fundBankInfo 银行限额信息
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

    /**
     * 卡券使用说明
     * @param dictionary 卡券使用说明
     * */
    void onKQDescription(Dictionary.Ret_PBAPP_dictionary dictionary);



}
