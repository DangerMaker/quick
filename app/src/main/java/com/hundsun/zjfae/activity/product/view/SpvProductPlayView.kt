package com.hundsun.zjfae.activity.product.view

import com.hundsun.zjfae.common.base.BaseView
import onight.zjfae.afront.gens.FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo
import onight.zjfae.afront.gens.QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan
import onight.zjfae.afront.gens.QueryPayInit.Ret_PBIFE_trade_queryPayInit
import onight.zjfae.afront.gensazj.Dictionary.Ret_PBAPP_dictionary


interface SpvProductPlayView : BaseView {


    /**
     * 支付信息初始化
     * @param payInit 支付信息
     */
    fun onPlayInit(payInit: Ret_PBIFE_trade_queryPayInit?)


    /**
     * 查询用户卡券信息
     * @param myKqQuan 用户卡券
     */
    fun onUserKquanInfo(myKqQuan: Ret_PBIFE_trade_queryMyKqQuan?)

    fun playProduct(returnCode: String?, returnMsg: String?)


    /**
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     */
    fun queryRechargeBankInfo(bankName: String?, bankCard: String?, bankNo: String?, showTips: String?)

    /**
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     * @param payChannelNo 支付渠道
     */
    fun queryRechargeBankInfo(bankName: String?, bankCard: String?, bankNo: String?, showTips: String?, payChannelNo: String?)

    /**
     * 限额信息
     */
    fun bankCardManage(fundBankInfo: Ret_PBIFE_bankcardmanage_queryFundBankInfo?)


    /**
     * 卡券使用说明
     * @param dictionary 卡券使用说明
     */
    fun onKQDescription(dictionary: Ret_PBAPP_dictionary?)


    fun rechargePlayAmount(queryPayInit: Ret_PBIFE_trade_queryPayInit?)


}