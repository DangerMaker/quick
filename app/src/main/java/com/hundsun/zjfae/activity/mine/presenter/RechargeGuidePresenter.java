package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.RechargeGuideView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import onight.zjfae.afront.AllAzjProto;


public class RechargeGuidePresenter extends BasePresenter<RechargeGuideView> {

    public RechargeGuidePresenter(RechargeGuideView baseView) {
        super(baseView);
    }


    public void rechargeGuide(){
        AllAzjProto.PEAGetNotice.Builder builder = AllAzjProto.PEAGetNotice.newBuilder();
        builder.setType("2");
        String url = parseUrl(AZJ,PBNOT,VNOTAZJ);
        CCLog.e(url);
        addDisposable(apiServer.noticeIcon(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<AllAzjProto.PEARetNotice>(baseView) {
            @Override
            public void onSuccess(AllAzjProto.PEARetNotice peaRetNotice) {
                baseView.onRecharge(peaRetNotice);
            }
        });

    }
}
