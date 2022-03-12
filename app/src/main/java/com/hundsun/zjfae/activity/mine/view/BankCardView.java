package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.DeleteBankCard;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.PBIFEBankcardmanageUnbindBankCardForUserOper;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine.view
 * @ClassName:      BankCardView
 * @Description:     银行卡View
 * @Author:         moran
 * @CreateDate:     2019/6/13 19:28
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/13 19:28
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface BankCardView extends BaseView {

    void onDeleteBank();

    void onDeleteBankCardBean(DeleteBankCard.Ret_PBIFE_bankcardmanage_deleteBankCard deleteBankCard);

    void onDeleteBankCardForUserOper(PBIFEBankcardmanageUnbindBankCardForUserOper.Ret_PBIFE_bankcardmanage_unbindBankCardForUserOper deleteBankCard);

    /**
     * 用户绑定银行卡信息
     * @param userDetailInfo 用户详细信息
     * @param userBankCard
     * @param bankName
     * @param payChannelNo
     * */
    void onUserBankInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard userBankCard, String payChannelNo, String bankName);

    /**
     * 查询银行卡渠道信息，是否限额，关闭
     * */
    void bankChannelInfo();


    /**
     * 银行渠道关闭
     * */
    void onBankChannelClose();

    void onDeleteBankSms();




    /**
     * 申请换卡
     * */
    void onChangeBank();


    /**
     * 人脸识别换卡
     * */
    void onFaceChangeBank();
    /**
     * 解绑银行卡异常
     * @param returnCode 返回code
     * @param returnMsg 返回信息
     * */
    void onDeleteBankError(String returnCode,String returnMsg);


    /**
     * 人脸识别或资料上传解绑
     * @date: 2019/6/11 14:59
     * @author: moran
     * @param returnCode 返回code
     * @param returnMsg 返回信息
     */
    void onFaceDeleteBank(String returnCode,String returnMsg);
}


