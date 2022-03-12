package com.hundsun.zjfae.common.http.observer;

import com.hundsun.zjfae.common.base.BaseView;

import io.reactivex.observers.DisposableObserver;

public abstract class AppUpDateObserver<T> extends DisposableObserver<T> {
    protected BaseView view;
    public AppUpDateObserver(BaseView view) {

        this.view = view;
    }

    @Override
    protected void onStart() {
        if (view != null) {
            view.showLoading();
        }
    }

    @Override
    public void onComplete() {
        if (view != null) {
            view.hideLoading();
        }
    }


    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);
}
