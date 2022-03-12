package com.hundsun.zjfae.common.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.AmountOfConversion;

public class AssigneePaymentDialog extends Dialog {
    public AssigneePaymentDialog(@NonNull Context context) {
        super(context);
    }

    public AssigneePaymentDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        private String message; //对话框内容
        private String confirm_btnText; //按钮名称“确定”
        private String cancel_btnText; //按钮名称“取消”
        private String url;//更新图片的链接
        private View contentView; //对话框中间加载的其他布局界面
        /*按钮监听事件*/
        private OnClickListener confirm_btnClickListener;
        private OnClickListener cancel_btnClickListener;
        private float alpha = 1.0f;

        private EarningsCallback callback;

        public Builder(Context context) {
            this.context = context;

        }
        public Builder(Context context, float alpha) {
            this.context = context;
            this.alpha = alpha;
        }

        public void setEarningsCallback(String confirm_btnText,EarningsCallback callback){
            this.confirm_btnText = confirm_btnText;
            this.callback = callback;
        }

        public Builder setMessage(String message){
            this.message = message;
            return this;
        }
        /**
         *
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         *
         *
         * @param title
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }



        /**
         * 设置对话框界面
         * @param v View
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        /**
         * Set the positive button and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(String confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = confirm_btnText;
            this.confirm_btnClickListener = listener;
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

        @SuppressLint("InflateParams")
        public CustomDialog create() {
            final EditText message;
            final TextView account;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.dialog_assignee_payment, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            message = layout.findViewById(R.id.message);
            account = layout.findViewById(R.id.account);
            message.setHint(this.message);
            message.addTextChangedListener(new AmountOfConversion(account));
            // set the confirm button
            if (cancel_btnText != null){
                layout.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel_btnClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }
            if (confirm_btnText != null){
                layout.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        confirm_btnClickListener.onClick(dialog,
//                                DialogInterface.BUTTON_POSITIVE);
                        if (callback != null){
                            callback.earnings(dialog,message.getText().toString().trim());
                        }
                    }
                });
            }


            SupportDisplay.setChildViewParam((ViewGroup)layout, false);
            dialog.setCancelable(false);
            dialog.setContentView(layout);

            return dialog;
        }

    }


    public interface EarningsCallback{
        void earnings(DialogInterface dialog, String money);
    }
}
