package com.hundsun.zjfae.activity.product.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.hundsun.zjfae.common.utils.CCLog;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorUtils {

    private static ScheduledExecutorUtils executorUtils = new ScheduledExecutorUtils();


    private static long oldTime;

    private static  ScheduledExecutorService executorService;


    private static final  Bundle bundle = new Bundle();

    private static long initialDelay = 0;

    //间隔时间
    private static long period = 1;

    private static long countTime;
    private static final long delta = 1;

    private static final int what = 0;

    //差值
    private static final int DIFFERENCE_TIME = 800;



    private static long hour = 24L;//24小时

    private static long minute = 60L;//一小时

    private static long second = 60L;//一分钟

    private static long day = hour * minute * second;


    private static ScheduledExecutorListener executorListener;

    private ScheduledExecutorUtils(){

    }


    public static void setCountTime(long countTime) {

        ScheduledExecutorUtils.countTime = countTime - delta;

    }


    public static void setExecutorListener(ScheduledExecutorListener executorListener) {
        ScheduledExecutorUtils.executorListener = executorListener;
    }

    public static void setInitialDelay(long initialDelay) {
        ScheduledExecutorUtils.initialDelay = initialDelay;
    }

    public static void setPeriod(long period) {
        ScheduledExecutorUtils.period = period;
    }

    private static ScheduledExecutorUtils getInstance(){

        return executorUtils;
    }


    public static synchronized void startTime(){
        cancel();
        oldTime = getSystemTime();
        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage(what);
                bundle.putLong("time",getSystemTime());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }




    /**
     * 获取系统时间
     * **/
    private static long getSystemTime(){

        return System.currentTimeMillis();
    }






    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle =msg.getData();

            if (countTime <= 0){
                cancel();
                if (executorListener != null){

                    executorListener.onSuccess();
                }
            }
            else {

                StringBuffer timeBuf = new StringBuffer();
                if (countTime - day > 0) {
                    //有几天

                    int day_time =(int) (Math.floor(countTime / day));

                    timeBuf.append(day_time).append("天");
                    //几小时
                    int minute_time = (int)((countTime % day) / (minute * second));
                    timeBuf.append(minute_time).append("小时");
                }
                else {
                    //1小时以内
                    if (countTime < (minute * second)) {

                        int minute =  (int )(Math.floor(countTime / second));
                        timeBuf.append(minute).append("分钟");

                        int seconds_time =(int) (countTime % second);

                        timeBuf.append(seconds_time).append("秒");
                    }
                    //一天以内
                    else {

                        int minute_time =(int) (countTime / (minute * second));

                        timeBuf.append(minute_time).append("小时");
                        //几分钟
                        int second_item = (int) (countTime % (minute * second) / second);
                        timeBuf.append(second_item).append("分钟");
                    }
                }

                if (executorListener != null){

                    executorListener.onNoClick(timeBuf.toString());
                }


                long currentTime = bundle.getLong("time",getSystemTime());

                if ((currentTime - oldTime) - DIFFERENCE_TIME > DIFFERENCE_TIME){
                    --countTime;
                }



                oldTime = currentTime;
                --countTime;
            }

            // executorService.shutdown();

        }
    };


    public static void cancel(){
        if (executorService != null){
            executorService.shutdown();
        }

    }




    public interface ScheduledExecutorListener{
        void onSuccess();
        void onNoClick(String tv);

    }
}
