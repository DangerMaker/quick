package com.hundsun.zjfae.common.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * @author moran
 * 倒计时
 * */
public class RxCountdown {


    private static final int COUNT = 3;



    private static RxCountdown rxCountdown = null;

    private Observable timer = null;

    private DisposableObserver observer;


    public static RxCountdown getInstance() {
        if (rxCountdown == null){
            rxCountdown = new RxCountdown();
        }
        return rxCountdown;
    }

    private RxCountdown(){

    }

    public void interval(final LoadListener listener){
        clean();
        timer = Observable.interval(0,1,TimeUnit.SECONDS).take(COUNT)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        observer = new DisposableObserver<Long>() {
            @Override
            public void onNext(Long aLong) {
                CCLog.e("along",aLong);
                if (aLong == COUNT-1){
                    if (listener != null){
                        listener.isShowDialog(true);
                    }
                }
            }
            @Override
            public void onError(Throwable e) {
                clean();
            }

            @Override
            public void onComplete() {
                clean();
            }
        };

        timer.subscribeWith(observer);
    }


    public void  clean(){
        CCLog.e("1111","取消");
        if (timer != null && observer != null){
            if (!observer.isDisposed()){
                observer.dispose();
            }
            timer = null;
            observer = null;
        }
    }

    public interface LoadListener{

        /**
         * 是否显示dialog
         * @param isShow true显示，false不显示
         * */
        void isShowDialog(boolean isShow);
    }
}
