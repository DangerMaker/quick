package com.hundsun.zjfae.activity.product.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.product.view
 * @ClassName:      ProductPlayView
 * @Description:     产品购买
 * @Author:         moran
 * @CreateDate:     2019/6/13 15:23
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/13 15:23
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface ProductPlayView extends BaseView {


    /**
     * 支付信息初始化
     * @param payInit 支付信息
     * */
    void onPlayInit(QueryPayInit.Ret_PBIFE_trade_queryPayInit payInit);


    /**
     * 查询用户卡券信息
     * @param myKqQuan 用户卡券
     * */
    void onUserKquanInfo(QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan myKqQuan);

    void playProduct(String returnCode, String returnMsg);







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
     * */
    void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo);


    /**
     * 卡券使用说明
     * @param dictionary 卡券使用说明
     * */
    void onKQDescription(Dictionary.Ret_PBAPP_dictionary dictionary);



    void rechargePlayAmount(QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit);
}
