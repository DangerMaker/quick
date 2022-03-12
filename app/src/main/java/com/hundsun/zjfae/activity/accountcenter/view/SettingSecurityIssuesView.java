package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.LoadSecurityQuestionPB;

/**
 * @Description:设置安保问题（View）
 * @Author: yangtianren
 */
public interface SettingSecurityIssuesView extends BaseView {
    void loadQuestion(List<LoadSecurityQuestionPB.PBIFE_securityquestionmanage_loadSecurityQuestion.TcSecurityQuestionTemplateList> list);

    void submitQuestion(String code, String msg);
}
