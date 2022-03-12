package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.UnbindBankCardUploadView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gensazj.DictDynamics;

public class UnbindBankCardUploadPresenter extends BasePresenter<UnbindBankCardUploadView> {


    public UnbindBankCardUploadPresenter(UnbindBankCardUploadView baseView) {
        super(baseView);
    }





    public void dictDynamic(String dynamicType){
        DictDynamics.REQ_PBAPP_dictDynamic.Builder dict = DictDynamics.REQ_PBAPP_dictDynamic.newBuilder();
        dict.setDynamicType1(dynamicType);
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dynamic,getRequestMap());

        addDisposable(apiServer.dictDynamic(url, getBody(dict.build().toByteArray())), new ProtoBufObserver<DictDynamics.Ret_PBAPP_dictDynamic>(baseView) {
            @Override
            public void onSuccess(DictDynamics.Ret_PBAPP_dictDynamic ret_pbapp_dictDynamic) {
                baseView.dictData(ret_pbapp_dictDynamic);
            }
        });
    }
}
