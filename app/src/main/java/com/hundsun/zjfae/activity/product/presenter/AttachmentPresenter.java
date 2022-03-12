package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.activity.product.view.AttachmentView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.BaseObserver;

import java.io.IOException;

import okhttp3.ResponseBody;

public class AttachmentPresenter extends BasePresenter<AttachmentView> {

    public AttachmentPresenter(AttachmentView baseView) {
        super(baseView);
    }


    public void openPDF(String url){

        addDisposable(apiServer.attachment(url), new BaseObserver<ResponseBody>(baseView) {
            @Override
            public void onSuccess(ResponseBody body) {
                try {
                    baseView.responseBodyData(body.bytes());
                } catch (IOException e) {
                    baseView.showError("附件加载失败！");
                    e.printStackTrace();
                }
            }
        });
    }
}
