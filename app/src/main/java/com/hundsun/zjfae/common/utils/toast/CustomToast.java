package com.hundsun.zjfae.common.utils.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class CustomToast extends Toast {



    private static CustomToast customViewToast = null;


    public static CustomToast getInstance(Context context) {

        if (customViewToast == null){
            customViewToast = new CustomToast(context);
        }

        return customViewToast;
    }


    private Context mContext;

    private CustomToast(Context context) {
        super(context);
        mContext = context;

    }


    @Override
    public void setDuration(int duration) {
        super.setDuration(duration);
    }


    @Override
    public void setView(View view) {
        super.setView(view);
    }




    @Override
    public void show() {
        this.setGravity(Gravity.CENTER,0,0);
        super.show();
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}
