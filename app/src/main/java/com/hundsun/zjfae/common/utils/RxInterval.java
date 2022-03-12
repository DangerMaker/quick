package com.hundsun.zjfae.common.utils;

import com.hundsun.zjfae.common.http.observer.BaseObserver;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class RxInterval {

    private  RxAction rxAction;

    private  long initialDelay;
    private  BaseObserver baseObserver ;
    private static RxInterval rxInterval = null;
    private boolean isInterval = false;


    private RxInterval(){

    }

    public static RxInterval getRxInterval() {
        if (rxInterval == null){
            rxInterval = new RxInterval();
        }
        return rxInterval;
    }

    public void setInterval(boolean interval) {
        isInterval = interval;
    }

    public  void intervalSeconds(long initialDelay, RxAction rxAction){
        this.rxAction = rxAction;
        this.initialDelay = initialDelay;
        isInterval = true;
    }



    public  void start(){
        clean();
        if (isInterval){
            baseObserver = new BaseObserver<Long>() {
                @Override
                public void onSuccess(Long aLong) {
                    if (rxAction != null){
                        rxAction.action();
                    }
                }
            };

            Observable.interval(0,initialDelay, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribeWith(baseObserver);

        }








    }

    public  void clean(){
        if (baseObserver != null &&!baseObserver.isDisposed()){
            baseObserver.dispose();
        }

    }



    public interface RxAction {

        void action();
    }

}
