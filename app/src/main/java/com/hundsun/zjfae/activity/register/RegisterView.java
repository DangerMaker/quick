package com.hundsun.zjfae.activity.register;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.v3.UserDetailInfo;

public interface RegisterView extends BaseView {





    void mobileSMSCode(String returnCode, String returnMsg);


    void registerState(String returnCode, String returnMsg,String mobile);


    void initCaptcha(String paraValue );



    /**
     *
     * @method
     * @description 用户详细信息
     * @date: 2019/7/22 10:23
     * @author: moran
     * @param userDetailInfo 用户详细信息
     * @return
     */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo,String mobile);
}
