package com.hundsun.zjfae.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.LimitFocusChangeInPut;
import com.hundsun.zjfae.common.view.CustomCountDownTimer;

public class SendSmsCodeDialog extends Dialog {
    private static LimitFocusChangeInPut limitFocusChangeInPut;
    private static Context mContext; //上下文对象

    public SendSmsCodeDialog(Context context) {
        super(context);
    }

    public SendSmsCodeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {


        //按钮名称“确定”
        private String confirm_btnText;
        //按钮名称“取消”
        private String cancel_btnText;
        private OnClickListener cancel_btnClickListener;

        private View layout;

        private SmsButtonOnClickListener smsButtonOnClickListener;

        private boolean flag = false;

        private SendSmsCodeDialog dialog;

        private float alpha = 1.0f;

        private String smsCode = "";

        private CharSequence msg = "";

        public Builder(Context context) {
            mContext = context;
        }


        /**
         * Set the negative button resource and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = (String) mContext
                    .getText(cancel_btnText);
            this.cancel_btnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(String cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = cancel_btnText;
            this.cancel_btnClickListener = listener;
            return this;
        }



        public Builder setSmsButtonOnClickListener(SmsButtonOnClickListener smsButtonOnClickListener) {
            this.smsButtonOnClickListener = smsButtonOnClickListener;
            return this;
        }


        public void setSmsButtonState(boolean flag) {
            this.flag = flag;
            if (layout != null){
                CustomCountDownTimer countDownTimer = new CustomCountDownTimer(60000, 1000, (Button) layout.findViewById(R.id.smsButton));
                countDownTimer.start();
            }
            if (limitFocusChangeInPut != null){
                limitFocusChangeInPut.setFlag(flag);
            }



        }

        public void  setMsg(CharSequence msg){

            if (msg == null){
               msg = "";
            }
            this.msg = msg;

        }



        public void show() {
            if (!flag){
                dialog = null;
                dialog = new SendSmsCodeDialog(mContext, R.style.mystyle);
                layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_bank_sms_code_layout, null);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.alpha = alpha;
                window.setAttributes(lp);
                dialog.addContentView(layout, new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


                final EditText smsCode = layout.findViewById(R.id.smsCode);
                final TextView message = layout.findViewById(R.id.msg);

                if (msg.equals("")){
                    message.setVisibility(View.GONE);
                }
                else {
                    message.setMovementMethod(LinkMovementMethod.getInstance());
                    message.setText(msg);
                }


                limitFocusChangeInPut = new LimitFocusChangeInPut(flag, smsCode);

                limitFocusChangeInPut.setInPutState(new LimitFocusChangeInPut.InPutState() {
                    @Override
                    public void state() {
                        showDialog("您还没有点击获取验证码，不能输入短信验证码哦~");
                    }
                });
                smsCode.setOnFocusChangeListener(limitFocusChangeInPut);

                layout.findViewById(R.id.smsButton).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (smsButtonOnClickListener != null) {

                            smsButtonOnClickListener.onSmsClick();
                        }
                    }
                });


                if (smsButtonOnClickListener != null) {
                    layout.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            smsButtonOnClickListener.onEditTextSms(dialog, DialogInterface.BUTTON_POSITIVE, smsCode.getText().toString());
                        }
                    });
                }

                if (cancel_btnClickListener != null) {
                    layout.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            cancel_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }

                dialog.setCancelable(false);
                SupportDisplay.setChildViewParam((ViewGroup) layout, false);
                dialog.setContentView(layout);

            }
            dialog.show();

        }

        public interface SmsButtonOnClickListener {

            void onEditTextSms(DialogInterface dialog, int which, String smsCode);

            void onSmsClick();

        }

    }


    public static void showDialog(String msg) {

        if (msg == null) {
            return;

        }
        CustomDialog.Builder builder = new CustomDialog.Builder(mContext);
        builder.setTitle("温馨提示");
        builder.setMessage(msg);
        builder.setNegativeButton("知道了", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
