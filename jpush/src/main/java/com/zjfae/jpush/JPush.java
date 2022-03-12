package com.zjfae.jpush;

import android.app.Application;
import android.content.Context;

import com.zjfae.jpush.receiver.CustomReceiver;

import cn.jpush.android.api.JPushInterface;

public class JPush {


    private static JPush jPush = null;

    private static String registrationId = "";

    public static JPush getInstance(Application context,boolean debug) {
        if (jPush == null){
            JPushInterface.init(context);
            JPushInterface.setDebugMode(debug);
            registrationId = JPushInterface.getRegistrationID(context);
            jPush = new JPush();
        }
        return jPush;
    }

    private JPush(){

    }

    public void setOpenJpushMessage(OpenJPushMessage message){
        CustomReceiver.setOpenJPushMessage(message);
    }


    public static String getJPushRegistrationId(Context context){

        if (null == registrationId || registrationId.equals("")){
            return JPushInterface.getRegistrationID(context);
        }
        return registrationId;

    }
}
