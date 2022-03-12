package com.hundsun.zjfae.common.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hundsun.zjfae.R;


public class CustomProgressDialog extends Dialog {
    private TextView tvMsg;
    private Context mContext;
    private View rootView;



    public CustomProgressDialog(Context context, String strMessage) {
        this(context, R.style.CustomProgressDialog, strMessage);
    }

    public void setMessage(String message) {
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }

    public CustomProgressDialog(Context context, int theme, String strMessage) {
        super(context, theme);
        mContext = context;
        setCancelable(false);
        rootView = LayoutInflater.from(context).inflate(R.layout.ccloudmi_common_progressdialog, null);
        this.setContentView(rootView);
        tvMsg = rootView
                .findViewById(R.id.tv_common_ccloudmi_loadingmsg);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
    }

    public void show(String strMessage) {
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        if (!isShowing()){
            super.show();
        }

    }
}
