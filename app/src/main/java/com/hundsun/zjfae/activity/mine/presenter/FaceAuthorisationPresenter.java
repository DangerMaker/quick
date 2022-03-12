package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.FaceAuthorisationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import onight.zjfae.afront.gensazj.FaceProtol;

public class FaceAuthorisationPresenter extends BasePresenter<FaceAuthorisationView> {


    public FaceAuthorisationPresenter(FaceAuthorisationView baseView) {
        super(baseView);
    }


    public void getFaceProtol(String certificateCode,String mobile){

        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Faceprotol,getRequestMap());
        FaceProtol.REQ_PBAPP_faceprotol.Builder builder = FaceProtol.REQ_PBAPP_faceprotol.newBuilder();

        CCLog.e("certificateCode",certificateCode);
        CCLog.e("mobile",mobile);
        builder.setCertificateCode(certificateCode);
        builder.setMobile(mobile);
        addDisposable(apiServer.getFaceProtol(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<FaceProtol.Ret_PBAPP_faceprotol>(baseView) {
            @Override
            public void onSuccess(FaceProtol.Ret_PBAPP_faceprotol ret_pbapp_faceprotol) {
                baseView.getFaceProtol(ret_pbapp_faceprotol);
            }
        });

    }
}
