package com.hundsun.zjfae.fragment.account;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.FundBankInfo;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.fragment.account
 * @ClassName:      AccountView
 * @Description:     账户中心
 * @Author:         moran
 * @CreateDate:     2019/6/17 14:03
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/17 14:03
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface AccountView extends BaseView {


    /**
     * 聚合接口
     * @param pwRetMerges 聚合用户详细信息，卡券，用户资金，消息
     * */
    void pWRetMerges(AllAzjProto.PWRetMerges pwRetMerges);


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
     * 银行卡信息
     * @param bankName 银行名字
     * @param bankCard 银行卡号
     * @param bankNo 银行编号
     * @param showTips 渠道是否关闭/暂停 0，不是，1是
     * */
    void queryRechargeBankInfo(String bankName, String bankCard,String bankNo,String showTips);

    /**
     * 限额信息
     * @param fundBankInfo 银行限额信息
     * */
    void bankCardManage(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo);

    /**
     * 我的邀请点击
     * @param url url
     * @param isShare 是否分享
     */
    void myInvitation(String url, String isShare);


    /**
     * 银行渠道关闭
     * */
    void onBankChannelClose( String bankName, String bankCard);

    /**
     * 退出登录
     * */
    void outLogin();


    /**
     * 人脸识别状态
     * @param isTencentFace 是否开启人脸识别
     * */
    void onFaceStatus(boolean isTencentFace);


}
