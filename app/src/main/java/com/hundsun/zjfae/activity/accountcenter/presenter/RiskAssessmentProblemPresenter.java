package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.RiskAssessmentProblemView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.QueryRiskAssessmentQuestionPB;
import onight.zjfae.afront.gens.RiskAssessmentCommitPB;

/**
 * @Description:风险测评问题（presenter）
 * @Author: yangtianren
 */
public class RiskAssessmentProblemPresenter extends BasePresenter<RiskAssessmentProblemView> {
    public RiskAssessmentProblemPresenter(RiskAssessmentProblemView baseView) {
        super(baseView);
    }

    //获取风险测评问题答案
    public void getProblemList() {
        QueryRiskAssessmentQuestionPB.REQ_PBIFE_riskassessment_queryRiskAssessmentQuestion.Builder builder = QueryRiskAssessmentQuestionPB.REQ_PBIFE_riskassessment_queryRiskAssessmentQuestion.newBuilder();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryRiskAssessmentQuestion, getRequestMap());
        addDisposable(apiServer.getProblemList(url,getBody(builder.build().toByteArray())), new ProtoBufObserver< QueryRiskAssessmentQuestionPB.Ret_PBIFE_riskassessment_queryRiskAssessmentQuestion>(baseView) {
            @Override
            public void onSuccess(QueryRiskAssessmentQuestionPB.Ret_PBIFE_riskassessment_queryRiskAssessmentQuestion ret_pbife_riskassessment_queryRiskAssessmentQuestion) {
                baseView.loadList(ret_pbife_riskassessment_queryRiskAssessmentQuestion.getData().getTcAssessmentT().getTcAssessmentQListList());
            }
        });
    }

    //提交答案
    public void commit(String point) {
        RiskAssessmentCommitPB.REQ_PBIFE_riskassessment_riskAssessmentCommit.Builder builder = RiskAssessmentCommitPB.REQ_PBIFE_riskassessment_riskAssessmentCommit.newBuilder();
        builder.setPoint(point);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.RiskAssessmentCommit, getRequestMap());
        addDisposable(apiServer.commit(url,getBody( builder.build().toByteArray())), new ProtoBufObserver< RiskAssessmentCommitPB.Ret_PBIFE_riskassessment_riskAssessmentCommit>(baseView) {
            @Override
            public void onSuccess(RiskAssessmentCommitPB.Ret_PBIFE_riskassessment_riskAssessmentCommit ret_pbife_riskassessment_riskAssessmentCommit) {
                baseView.commit(ret_pbife_riskassessment_riskAssessmentCommit.getReturnCode(), ret_pbife_riskassessment_riskAssessmentCommit.getReturnMsg(), ret_pbife_riskassessment_riskAssessmentCommit.getData().getRiskExpiredDate(), ret_pbife_riskassessment_riskAssessmentCommit.getData().getRiskLevel());
            }
        });
    }
}
