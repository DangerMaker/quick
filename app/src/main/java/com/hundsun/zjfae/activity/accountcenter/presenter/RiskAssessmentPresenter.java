package com.hundsun.zjfae.activity.accountcenter.presenter;

import com.hundsun.zjfae.activity.accountcenter.view.RiskAssessmentView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:评测presenter
 * @Author: zhoujianyu
 * @Time: 2019/1/30 10:30
 */
public class RiskAssessmentPresenter extends BasePresenter<RiskAssessmentView> {
    public RiskAssessmentPresenter(RiskAssessmentView baseView) {
        super(baseView);
    }

    /**
     * 获取用户信息
     */
    public void getUserDate() {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
                baseView.getUserDetailInfo(userDetailInfo);
            }
        });

    }


    /**
     * 我要重测是否跳转web界面
     * */
    public void myInvitationClick() {
        AllAzjProto.PEAGetUrl.Builder builder = AllAzjProto.PEAGetUrl.newBuilder();
        builder.setKeyNo("newReview");
        String url = parseUrl(AZJ, PBURL, VURLAZJ, getRequestMap());
        addDisposable(apiServer.getFeedbackUrl(url, getBody(builder.build().toByteArray())), new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof AllAzjProto.PEARetUrl) {
                    //用户消息
                    AllAzjProto.PEARetUrl retUrl = (AllAzjProto.PEARetUrl) o;
                    if (!retUrl.getUrlsList().isEmpty()) {
                        baseView.myInvitation(retUrl.getUrlsList().get(0).getBackendUrl(), retUrl.getUrlsList().get(0).getIsShare());
                    } else {
                        baseView.myInvitation("", "");
                    }
                }
            }
        });
    }
}
