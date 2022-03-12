package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.SettingSecurityIssuesView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import onight.zjfae.afront.gens.LoadSecurityQuestionPB;
import onight.zjfae.afront.gens.v2.SetSecurityQuestionPrePB;

/**
 * @Description:设置安保问题（presenter）
 * @Author: yangtianren
 */
public class SettingSecurityIssuesPresenter extends BasePresenter<SettingSecurityIssuesView> {
    public SettingSecurityIssuesPresenter(SettingSecurityIssuesView baseView) {
        super(baseView);
    }

    //下载问题
    public void loadQuestion() {
        LoadSecurityQuestionPB.REQ_PBIFE_securityquestionmanage_loadSecurityQuestion.Builder builder = LoadSecurityQuestionPB.REQ_PBIFE_securityquestionmanage_loadSecurityQuestion.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadSecurityQuestion, getRequestMap());
        addDisposable(apiServer.loadQuestion(url,getBody(builder.build().toByteArray())),  new ProtoBufObserver< LoadSecurityQuestionPB.Ret_PBIFE_securityquestionmanage_loadSecurityQuestion>(baseView) {
            @Override
            public void onSuccess(LoadSecurityQuestionPB.Ret_PBIFE_securityquestionmanage_loadSecurityQuestion ret_pbife_securityquestionmanage_loadSecurityQuestion) {
                baseView.loadQuestion(ret_pbife_securityquestionmanage_loadSecurityQuestion.getData().getTcSecurityQuestionTemplateListList());
            }
        });
    }

    //提交问题
    public void submitQuestion(String questionA, String answerA, String questionB, String answerB, String questionC, String answerC) {
        CCLog.e(questionA+":"+answerA);
        CCLog.e(questionB+":"+answerB);
        CCLog.e(questionC+":"+answerC);
        SetSecurityQuestionPrePB.REQ_PBIFE_securityquestionmanage_setSecurityQuestionPre.Builder builder = SetSecurityQuestionPrePB.REQ_PBIFE_securityquestionmanage_setSecurityQuestionPre.newBuilder();
        builder.setQuestionCount("3");
        builder.setQuestionId1(questionA);
        builder.setQuestionAnswer1(answerA);
        builder.setQuestionId2(questionB);
        builder.setQuestionAnswer2(answerB);
        builder.setQuestionId3(questionC);
        builder.setQuestionAnswer3(answerC);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SetSecurityQuestionPre, getRequestMap());
        addDisposable(apiServer.submitQuestion(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< SetSecurityQuestionPrePB.Ret_PBIFE_securityquestionmanage_setSecurityQuestionPre>(baseView) {
            @Override
            public void onSuccess(SetSecurityQuestionPrePB.Ret_PBIFE_securityquestionmanage_setSecurityQuestionPre ret_pbife_securityquestionmanage_setSecurityQuestionPre) {
                baseView.submitQuestion(ret_pbife_securityquestionmanage_setSecurityQuestionPre.getReturnCode(), ret_pbife_securityquestionmanage_setSecurityQuestionPre.getReturnMsg());
            }
        });
    }
}
