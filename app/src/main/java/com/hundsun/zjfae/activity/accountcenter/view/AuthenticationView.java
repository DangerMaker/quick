package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.LoadMySecurityQuestionPB;
import onight.zjfae.afront.gens.VerifySecurityInfoPB;

/**
 * @Description:身份认证（View）
 * @Author: yangtianren
 */
public interface AuthenticationView extends BaseView {
    void getProblem(List<LoadMySecurityQuestionPB.PBIFE_securityquestionmanage_loadMySecurityQuestion.TcSecurityQuestionAnswerList> list);

    void verifySecurityInfo(VerifySecurityInfoPB.Ret_PBIFE_userbaseinfo_verifySecurityInfo data);

    void getVerificationCode(String code,String msg);

    void check(String code,String msg);
}
