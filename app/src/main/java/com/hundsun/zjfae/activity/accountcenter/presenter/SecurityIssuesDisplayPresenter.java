package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.SecurityIssuesDisplayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.SetSecurityQuestionPB;

/**
 * @Description:设置安保问题（presenter）
 * @Author: yangtianren
 */
public class SecurityIssuesDisplayPresenter extends BasePresenter<SecurityIssuesDisplayView> {
    public SecurityIssuesDisplayPresenter(SecurityIssuesDisplayView baseView) {
        super(baseView);
    }

    public void setProblem() {
        SetSecurityQuestionPB.REQ_PBIFE_securityquestionmanage_setSecurityQuestion.Builder builder = SetSecurityQuestionPB.REQ_PBIFE_securityquestionmanage_setSecurityQuestion.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SetSecurityQuestion, getRequestMap());
        addDisposable(apiServer.setProblem(url, getBody(builder.build().toByteArray())), new ProtoBufObserver< SetSecurityQuestionPB.Ret_PBIFE_securityquestionmanage_setSecurityQuestion>(baseView) {
            @Override
            public void onSuccess(SetSecurityQuestionPB.Ret_PBIFE_securityquestionmanage_setSecurityQuestion ret_pbife_securityquestionmanage_setSecurityQuestion) {
                baseView.setProblem(ret_pbife_securityquestionmanage_setSecurityQuestion.getReturnCode(), ret_pbife_securityquestionmanage_setSecurityQuestion.getReturnMsg());
            }
        });
    }
}
