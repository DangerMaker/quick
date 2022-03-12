package com.hundsun.zjfae.activity.product.util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.text.DecimalFormat;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CancelTimerUtils {

    private static CancelTimerUtils executorUtils = new CancelTimerUtils();


    private static long oldTime;

    private static ScheduledExecutorService executorService;


    private static final Bundle bundle = new Bundle();

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


    private static CountDownListener executorListener;

    private CancelTimerUtils() {

    }


    public static void setCountTime(long countTime) {

        CancelTimerUtils.countTime = countTime - delta;

    }


    public static void setExecutorListener(CountDownListener executorListener) {
        CancelTimerUtils.executorListener = executorListener;
    }

    public static void setInitialDelay(long initialDelay) {
        CancelTimerUtils.initialDelay = initialDelay;
    }

    public static void setPeriod(long period) {
        CancelTimerUtils.period = period;
    }

    private static CancelTimerUtils getInstance() {

        return executorUtils;
    }


    public static synchronized void startTime() {
        cancel();
        oldTime = getSystemTime();
        executorService = new ScheduledThreadPoolExecutor(1);
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage(what);
                bundle.putLong("time", getSystemTime());
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }, initialDelay, period, TimeUnit.SECONDS);
    }


    /**
     * 获取系统时间
     **/
    private static long getSystemTime() {

        return System.currentTimeMillis();
    }


    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();

            if (countTime <= 0) {
                cancel();
                if (executorListener != null) {

                    executorListener.onSuccess();
                }
            } else {

                StringBuffer timeBuf = new StringBuffer();
                DecimalFormat decimalFormat = new DecimalFormat("00");

                int hour = (int) (Math.floor(countTime / (60 * 60)));
                timeBuf.append(decimalFormat.format(hour)).append(":");

                int minute = (int) (Math.floor((countTime / second) % second));
                timeBuf.append(decimalFormat.format(minute)).append(":");

                int seconds_time = (int) (countTime % second);

                timeBuf.append(decimalFormat.format(seconds_time));

                if (executorListener != null) {

                    executorListener.onNoClick(countTime, timeBuf.toString());
                }


                long currentTime = bundle.getLong("time", getSystemTime());

                if ((currentTime - oldTime) - DIFFERENCE_TIME > DIFFERENCE_TIME) {
                    --countTime;
                }


                oldTime = currentTime;
                --countTime;
            }

            // executorService.shutdown();

        }
    };


    public static void cancel() {
        if (executorService != null) {
            executorService.shutdown();
        }

    }


    public interface CountDownListener {
        void onSuccess();

        void onNoClick(long countDown, String tv);

    }
}
