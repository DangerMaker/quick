package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.SupportView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import onight.zjfae.afront.AllAzjProto;

public class SupportPresenter extends BasePresenter<SupportView> {
    public SupportPresenter(SupportView baseView) {
        super(baseView);
    }


    public void getSupportBankIcon(){

      String url =  parseUrl(AZJ,PBGBP,VGBPAZJ,getRequestMap());
        CCLog.e(url);


        AllAzjProto.PEAGetBankPic.Builder bankPic = AllAzjProto.PEAGetBankPic.newBuilder();
        bankPic.setBankCode(" ");
        bankPic.setIsRecommend(" ");
        addDisposable(apiServer.getSupportBankIcon(url, getBody(bankPic.build().toByteArray())), new ProtoBufObserver<AllAzjProto.PEARetBankPic>(baseView) {
            @Override
            public void onSuccess(AllAzjProto.PEARetBankPic peaRetBankPic) {
                baseView.onSupportBankBean(peaRetBankPic);
            }

        });
    }

}
