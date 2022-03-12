package com.hundsun.zjfae.common.http.observer;


public abstract class UpdateObserver <T> extends BaseObserver<T> {


    @Override
    public void onNext(T t) {

        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }


}
