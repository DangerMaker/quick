package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.QueryRiskAssessmentQuestionPB;

/**
 * @Description:风险测评问题（View）
 * @Author: yangtianren
 */
public interface RiskAssessmentProblemView extends BaseView {
    void loadList(List<QueryRiskAssessmentQuestionPB.PBIFE_riskassessment_queryRiskAssessmentQuestion.TcAssessmentT.TcAssessmentQList> list);

    void commit(String code,String msg,String riskExpiredDate,String riskLevel);
}
