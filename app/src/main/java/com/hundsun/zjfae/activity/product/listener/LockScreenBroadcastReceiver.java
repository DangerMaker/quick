package com.hundsun.zjfae.activity.product.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockScreenBroadcastReceiver extends BroadcastReceiver {
    private LockScreenListener lockScreenListener;

    public LockScreenBroadcastReceiver(LockScreenListener lockScreenListener) {
        super();
        this.lockScreenListener = lockScreenListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String action=intent.getAction();
        // 开屏
        if(action.equals(Intent.ACTION_SCREEN_ON)){

            lockScreenListener.onScreenOn();
        }
        // 锁屏
        else if(action.equals(Intent.ACTION_SCREEN_OFF)){
            lockScreenListener.onScreenOff();
        }
        // 解锁
        else if(action.equals(Intent.ACTION_USER_PRESENT)){
            lockScreenListener.onUserPresent();
        }
    }
}
