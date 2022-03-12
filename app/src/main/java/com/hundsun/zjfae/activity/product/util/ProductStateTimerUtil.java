package com.hundsun.zjfae.activity.product.util;

import android.widget.Button;

import com.hundsun.zjfae.common.utils.CCLog;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProductStateTimerUtil {


    private static   Disposable mDisposable;

    private static long hour = 24L;//24小时

    private static long minute = 60L;//一小时

    private static long second = 60L;//一分钟

    private static long day = hour * minute * second;





    public static void timer(long seconds, final Button view){
        cancel();
        final long period = seconds-2;

        Observable.interval(1, TimeUnit.SECONDS).subscribeOn(Schedulers.trampoline()).observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Boolean>() {
                    @Override
                    public Boolean apply(Long aLong) {

                        CCLog.e("aLong",aLong);

                        if (aLong == period){
                            return true;
                        }
                        else {
                            countDown(period - aLong,  view);
                        }

                        return false;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        //倒计时完成
                        if (aBoolean){
                            view.setEnabled(true);
                        }
                        else {
                            view.setEnabled(false);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        CCLog.e("e",e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private static void countDown(long seconds, final Button view){

        //大于/等于1天
        if (seconds - day >= 0 ){
            StringBuffer timeBuf = new StringBuffer();
            long day_time = seconds / day;
            timeBuf.append(day_time).append("天");
            //几小时
            long minute_time = (seconds % day) / (minute * second);
            timeBuf.append(minute_time).append("小时");
            view.setText(timeBuf.toString());
        }
        else {
            //小于1小时
            if (seconds <= (minute * second)){
                StringBuffer timeBuf = new StringBuffer();
                long minute_time = seconds / second;
                timeBuf.append(minute_time).append("分钟");
                long second_item = (seconds % second) / second;
                timeBuf.append(second_item).append("秒");
                view.setText(timeBuf.toString());
            }
            //几个小时
            else {

                StringBuffer timeBuf = new StringBuffer();
                //几小时
                long minute_time = seconds / (minute * second);
                timeBuf.append(minute_time).append("小时");
                //几分钟
                long second_item = seconds % (minute * second) / second;
                timeBuf.append(second_item).append("分钟");
                view.setText(timeBuf.toString());
            }

        }
    }


    public static   void cancel(){
        if (mDisposable != null && mDisposable.isDisposed()){
            mDisposable.dispose();
        }
    }
}
