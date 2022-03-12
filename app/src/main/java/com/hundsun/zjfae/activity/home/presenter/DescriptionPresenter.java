package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.view.DescriptionView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gensazj.DictDynamics;

public class DescriptionPresenter extends BasePresenter<DescriptionView> {

    public DescriptionPresenter(DescriptionView baseView) {
        super(baseView);
    }


    public void dictDynamicState(){

        DictDynamics.REQ_PBAPP_dictDynamic.Builder dict = DictDynamics.REQ_PBAPP_dictDynamic.newBuilder();
        dict.setDynamicType1("highNetWorthState");
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dynamic,getRequestMap());

        addDisposable(apiServer.dictDynamic(url, getBody(dict.build().toByteArray())), new ProtoBufObserver<DictDynamics.Ret_PBAPP_dictDynamic>(baseView) {
            @Override
            public void onSuccess(DictDynamics.Ret_PBAPP_dictDynamic ret_pbapp_dictDynamic) {
                baseView.dictState(ret_pbapp_dictDynamic);
            }
        });
    }

}
