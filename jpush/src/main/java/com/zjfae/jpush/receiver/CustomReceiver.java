package com.zjfae.jpush.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.zjfae.jpush.Extras;
import com.zjfae.jpush.OpenJPushMessage;

import cn.jpush.android.api.JPushInterface;

public class CustomReceiver extends BroadcastReceiver {




    private static final String TAG = "TAG";

    private static OpenJPushMessage jPushMessage;



    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        Log.d("TAG", "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "用户点击打开了通知");
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            //扩展信息---根据TYPE分类，url--链接，message -- 消息，productList--产品列表，productDetail--产品详情
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);

            Gson gson = new Gson();
            Extras result  = gson.fromJson(extras,Extras.class);
            result.setNotificationId(notificationId);
            result.setTitle(title);
            result.setJPContent(content);
            openNotificationManager(context,result);

        }else {
            Log.d(TAG, "TAG" + intent.getAction());
        }
    }



    public static void setOpenJPushMessage(OpenJPushMessage openJPushMessage){

        if (jPushMessage == null){
            jPushMessage = openJPushMessage;
        }


    }



    private void openNotificationManager(Context context,Extras result ){



        if (jPushMessage != null){
            jPushMessage.openMessage(context,result);
        }
    }


}
