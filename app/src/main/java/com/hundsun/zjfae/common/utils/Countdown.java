package com.hundsun.zjfae.common.utils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class Countdown {


    private static Countdown countdown;

    public static Countdown getInstance() {
        if (countdown == null){
            countdown = new Countdown();
        }
        return countdown;
    }


    private Countdown(){

    }

    private Disposable mdDisposable;

    public void start(final long millisInFuture,final ActionInterFace actionInterFace){
        Observable observable = Observable.intervalRange(0,millisInFuture,0,1, TimeUnit.SECONDS);

        mdDisposable =  observable .observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                //start_but.setText("跳过" + (millisInFuture - aLong ) + "s");
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                actionInterFace.action();
            }
        }).subscribe();
    }

    public void clean(){
        if (mdDisposable != null) {
            mdDisposable.dispose();
            mdDisposable = null;
        }
    }

    public interface ActionInterFace{
        void action();
    }
}
