package com.hundsun.zjfae.common.base;

import com.hundsun.zjfae.common.http.observer.BaseObserver;

import okhttp3.ResponseBody;

public abstract class ImageObserver extends BaseObserver<ResponseBody> {


    public ImageObserver(BaseView view) {
        super(view);
    }


    @Override
    protected void onStart() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void onNext(ResponseBody body) {
        if (body != null ){
            onSuccess(body);
        }
        else {
            view.showError("图片流下载失败");
        }


    }

    @Override
    public void onComplete() {
        if (view != null) {
            view.hideLoading();
        }
    }



}
