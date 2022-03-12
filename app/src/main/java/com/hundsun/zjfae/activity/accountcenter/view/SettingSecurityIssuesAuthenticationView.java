package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.VerifySecurityInfoPB;

/**
 * @Description:设置安保问题（View）
 * @Author: yangtianren
 */
public interface SettingSecurityIssuesAuthenticationView extends BaseView {
    void verifySecurityInfo(VerifySecurityInfoPB.PBIFE_userbaseinfo_verifySecurityInfo securityInfo);

    void getVerificationCode(String code, String msg);

    void check(String code,String msg);
}
