package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.AcquireBankSmsCheckCode4Recharge;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gens.v2.PBIFEFundRecharge;
import onight.zjfae.afront.gens.v3.UserDetailInfo;


/**
 * @author moran 充值回调
 */
public interface RechargeView extends BaseView {


    /**
     * 获取用户充值银行卡信息
     *
     * @param bankCardInfo 用户充值银行卡信息
     */
    void onUserBankInfo(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo);

    /**
     * 获取用户资产信息
     *
     * @param userAssetsInfo 用户资产信息
     */
    void onUserAssetsInfo(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo userAssetsInfo);

    /**
     * 获取用户详细信息
     *
     * @param userDetailInfo 用户信息
     */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    /**
     * 限额信息
     *
     * @param fundBankInfo 银行卡信息
     */
    void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo);

    /**
     * 充值成功回调
     *
     * @param fundRecharge 充值成功信息
     */
    void onFundRecharge(PBIFEFundRecharge.Ret_PBIFE_fund_recharge fundRecharge);


    /**
     * 发送短信验证码
     *
     * @param sendSms 发送短信验证码信息
     */
    void sendSmsState(AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge sendSms);


    /**
     * 充值失败
     *
     * @param returnCode 返回code码
     * @param returnMsg  充值失败信息
     */
    void onFundRechargeError(String returnCode, String returnMsg);


    /**
     * 设置交易密码
     *
     * @param returnMsg 返回信息
     **/
    void onSettingUserPlayPassWord(String returnMsg);


    /**
     *
     *
     * @param OnlineChannel     是否线上充值
     * @param amount     总余额
     * @param accredited 合格投资者差额
     * @return
     * @date: 1/4/21 5:46 PM
     * @author: moran
     */
    void onRechargeChannelsStatus(boolean OnlineChannel, String amount, String accredited);


}
