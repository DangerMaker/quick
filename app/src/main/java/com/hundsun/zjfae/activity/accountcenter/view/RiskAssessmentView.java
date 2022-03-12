package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:评测View
 * @Author: yangtianren
 */
public interface RiskAssessmentView extends BaseView {
    //用户信息
    void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);




    /**
     * 我要重测是否跳转web界面
     * @param url url
     * @param isShare 是否分享
     */
    void myInvitation(String url, String isShare);
}
