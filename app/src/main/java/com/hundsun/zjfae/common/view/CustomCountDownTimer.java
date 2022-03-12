package com.hundsun.zjfae.common.view;

import android.os.CountDownTimer;
import android.widget.TextView;

public class CustomCountDownTimer extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private TextView textView;






    public CustomCountDownTimer(long millisInFuture, long countDownInterval, TextView view) {
        super(millisInFuture, countDownInterval);
        this.textView = view;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setEnabled(false);
        textView.setClickable(false);
        textView.setText(millisUntilFinished/1000+"秒后可重发");
    }

    @Override
    public void onFinish() {
        textView.setEnabled(true);
        textView.setClickable(true);
        textView.setText("获取验证码");
    }

}
