package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.CancelApplication;
import onight.zjfae.afront.gens.PBIFEUserinfomanageCheckTradePassword;
import onight.zjfae.afront.gens.UserChangeCardInfo;
import onight.zjfae.afront.gens.UserUnbindCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;
import onight.zjfae.afront.gensazj.TencentFace;

public interface BankCardManagementView extends BaseView {


    /**
     * 查询是否签签署人脸识别协议
     * @param notice 0000未签署，1208已经签署
     * **/
    void queryFaceAgreement(Notices.Ret_PBAPP_notice notice);

    /**
     * 已签署人脸协议
     * */
    void onUserIsFaceAgreement();

    /**
     *校检支付密码
     * @param checkTradePassword 交易密码
     * @param faceFlags 是否是人脸识别
     * **/
    void checkTradePassword(PBIFEUserinfomanageCheckTradePassword.Ret_PBIFE_userinfomanage_checkTradePassword checkTradePassword, boolean faceFlags);

    /**
     * 获取用户详细信息
     * */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    /**
     * 查询解绑失败原因
     * */
    void onUserUnbindCardInfo(UserUnbindCardInfo.Ret_PBIFE_bankcardmanage_getUserUnbindCardInfo unbindCardInfo);


    /**
     * 查询换卡失败原因
     * */
    void onUserChangeCardInfo(UserChangeCardInfo.Ret_PBIFE_bankcardmanage_getUserChangeCardInfo changeCardInfo);


    /**
     * 取消解绑申请
     * */

    void cleanUnbindCard(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication cancelApplication,String type);


    /**
     * 腾讯人脸识别
     * */
    void onTencentFace(TencentFace.Ret_PBAPP_tencentface ret_pbapp_tencentface );


    void onDeleteBankSms();
}
