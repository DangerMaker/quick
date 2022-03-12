package com.hundsun.zjfae.common.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RxTimerUtil {

    private static RxTimerListener timerListener;

    private static Bundle bundle ;

    private static ScheduledExecutorService executorService;

    private static final int what = 0;

    //间隔时间
    private static long period = 5;

    //线程池个数
    private static final int corePoolSize = 1;

    private static boolean isStart = false;//是否开启定时器

    private RxTimerUtil(){

    }

    public static void setPeriod(long period) {
        RxTimerUtil.period = period;
        isStart = true;
    }

    public static void setTimerListener(RxTimerListener timerListener) {
        RxTimerUtil.timerListener = timerListener;
    }

    public static void start(){
        cancel();
        if (isStart){
            bundle = new Bundle();
            handler.removeMessages(what);
            executorService = new ScheduledThreadPoolExecutor(corePoolSize);
            executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    Message message = handler.obtainMessage(what);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            }, period, period, TimeUnit.SECONDS);
        }

    }



    public static void cancel(){
        if (executorService != null){
            executorService.shutdown();
            bundle = null;
            executorService = null;
        }

    }


    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){

                case what:
                    if (timerListener != null){
                        timerListener.doNext();
                    }
                    break;
            }




        }
    };





    public interface RxTimerListener{
        void doNext();
    }
}
