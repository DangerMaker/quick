package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.LoadWithDrawBankInfo;
import onight.zjfae.afront.gens.WithDraw;
import onight.zjfae.afront.gensazj.WithdrawalsCoupon;
import onight.zjfae.afront.gensazj.v2.BannerProto;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine.view
 * @ClassName:      WithdrawalView
 * @Description:     提现回调View
 * @Author:         moran
 * @CreateDate:     2019/6/14 17:34
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/14 17:34
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface WithdrawalView extends BaseView {


    /**
     * 提现
     * @param fundWithDraw 提现结果
     * */
    void onWithDrawBean( WithDraw.Ret_PBIFE_fund_withDraw fundWithDraw);

    /**
     * 提现信息
     * @param loadWithDrawBankInfo 提现信息
     * */
    void onWithDrawBankInfo(LoadWithDrawBankInfo.Ret_PBIFE_fund_loadWithDrawBankInfo loadWithDrawBankInfo);

    /**
     * 获取用户银行卡信息
     * @param bankName 用户银行名称
     * */
    void onUserBankInfo(String bankName);

    void onCompanyUserBankInfo(String bankName);


    /**
     * 用户卡券信息
     * @param kqWithdrawals 卡券信息
     * */
    void onWithdrawalsCoupon(WithdrawalsCoupon.Ret_PBAPP_kqWithdrawals kqWithdrawals);
}
