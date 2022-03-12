package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.view.MessageView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.AllAzjProto;

public class MessagePresenter extends BasePresenter<MessageView> {
    public MessagePresenter(MessageView baseView) {
        super(baseView);
    }



    public void requestMessageIcon(){
        String url = parseUrl(AZJ,PBICO,VICOAZJ,getRequestMap());
        AllAzjProto.PEAGetIcons.Builder icons = AllAzjProto.PEAGetIcons.newBuilder();
        icons.setIconsLocation("messageCenter");


        addDisposable(apiServer.requestMessageIcon(url, getBody(icons.build().toByteArray())), new ProtoBufObserver< AllAzjProto.PEARetIcons>(baseView) {
            @Override
            public void onSuccess(AllAzjProto.PEARetIcons retIcons) {
                baseView.userMessageIcon( retIcons.getIconsListList());
            }
        });

    }

}
