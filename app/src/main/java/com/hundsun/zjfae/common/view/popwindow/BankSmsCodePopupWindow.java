package com.hundsun.zjfae.common.view.popwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class BankSmsCodePopupWindow extends PopupWindow {

    private Activity mContext;


    private View mConentView;


    private View  parent;


    public BankSmsCodePopupWindow(Activity context){
        this.mContext = context;
        LayoutInflater inflater =  LayoutInflater.from(context);

        mConentView = inflater.inflate(R.layout.dialog_bank_sms_code_layout, null);



        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setOutsideTouchable(false);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        this.update();
        SupportDisplay.resetAllChildViewParam((ViewGroup) mConentView,false);
        this.setContentView(mConentView);
    }


    public void setParent(View parent) {
        this.parent = parent;
    }

    private void initView(){

    }





    // 设置背景颜色变暗
    private void setBackColorGery() {

        WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
        lp.alpha = 0.7f;
        mContext.getWindow().setAttributes(lp);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mContext.getWindow().getAttributes();
                lp.alpha = 1f;
                mContext.getWindow().setAttributes(lp);
            }
        });
    }

    public void showPopupWindow() {
        setBackColorGery();
        int xoff = getWidth()/2-parent.getWidth()/2;
        showAtLocation(parent, Gravity.CENTER, xoff, 0);
    }

    @Override
    public void dismiss() {

        super.dismiss();
    }

}
