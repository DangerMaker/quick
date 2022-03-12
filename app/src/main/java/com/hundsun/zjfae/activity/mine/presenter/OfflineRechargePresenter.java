package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.OfflineRechargeView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.v2.Notices;

public class OfflineRechargePresenter extends BasePresenter<OfflineRechargeView> {

    public OfflineRechargePresenter(OfflineRechargeView baseView) {
        super(baseView);
    }




    public void offline(String bankCard,String bankName,String type){

        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setBankCard(bankCard);
        notice.setBankName(bankName);
        notice.setType(type);

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Notice, map);
        addDisposable(apiServer.offline(url, getBody(notice.build().toByteArray())), new ProtoBufObserver<Notices.Ret_PBAPP_notice>(baseView) {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice notice) {
                baseView.onOfflineRecharge(notice);
            }
        });

    }



    /*
     * 获取信息
     * */
    public void offlineRechargeDictionary(){
        Dictionary.REQ_PBAPP_dictionary.Builder builder = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        //["recharge.name", "recharge.bankAccount", "recharge.bankName", "recharge.tipsPage"]
        builder.addKeyNo("recharge.name");
        builder.addKeyNo("recharge.bankAccount");
        builder.addKeyNo("recharge.bankName");
        builder.addKeyNo("recharge.tipsPage");

        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dictionary,getRequestMap());

        addDisposable(apiServer.getDictionary(url,getBody( builder.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(baseView) {
            @Override
            public void onSuccess(Dictionary.Ret_PBAPP_dictionary dictionary) {
                baseView.onDictionary(dictionary);
            }
        });

    }

}
