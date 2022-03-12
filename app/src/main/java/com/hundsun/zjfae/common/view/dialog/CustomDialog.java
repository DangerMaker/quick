package com.hundsun.zjfae.common.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }
    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        private String message; //对话框内容
        private SpannableString spannableString;
        private String confirm_btnText; //按钮名称“确定”
        private String cancel_btnText; //按钮名称“取消”
        @SuppressWarnings("unused")
        private View contentView; //对话框中间加载的其他布局界面
        /*按钮监听事件*/
        private OnClickListener confirm_btnClickListener;
        private OnClickListener cancel_btnClickListener;
        private float alpha = 1.0f;

        public Builder(Context context) {
            this.context = context;

        }
        public Builder(Context context, float alpha) {
            this.context = context;
            this.alpha = alpha;
        }
        /*设置对话框信息*/
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(SpannableString spannableString){
            this.spannableString = spannableString;
            return this;
        }



        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
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
         * Set the positive button resource and it's listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton(int confirm_btnText,
                                         OnClickListener listener) {
            this.confirm_btnText = (String) context
                    .getText(confirm_btnText);
            this.confirm_btnClickListener = listener;
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
         * Set the negative button resource and it's listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton(int cancel_btnText,
                                         OnClickListener listener) {
            this.cancel_btnText = (String) context
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

        @SuppressLint("InflateParams")
        public CustomDialog create() {
            LayoutInflater inflater = LayoutInflater.from(context);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            //((TextView) layout.findViewById(R.id.title)).getPaint().setFakeBoldText(true);;
            // set the confirm button


            if (confirm_btnText == null && cancel_btnText == null){
                layout.findViewById(R.id.confirm_btn).setVisibility(
                        View.GONE);
                layout.findViewById(R.id.cancel_btn).setVisibility(
                        View.GONE);
            }
            else if (cancel_btnText == null){
                layout.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
                ((TextView) layout.findViewById(R.id.confirm_btn)).setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    layout.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            confirm_btnClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }
            }
            else if (confirm_btnText == null){
                layout.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
                ((TextView) layout.findViewById(R.id.cancel_btn)).setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    layout.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            cancel_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            else {
                ((TextView) layout.findViewById(R.id.confirm_btn)).setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    layout.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            confirm_btnClickListener.onClick(dialog,DialogInterface.BUTTON_POSITIVE);
                        }
                    });
                }

                ((TextView) layout.findViewById(R.id.cancel_btn)).setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    layout.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            cancel_btnClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_NEGATIVE);
                        }
                    });
                }
            }
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            }
            else {
                ((TextView) layout.findViewById(R.id.message)).setText(spannableString);
            }



            SupportDisplay.setChildViewParam((ViewGroup)layout, false);
            dialog.setCancelable(false);
            dialog.setContentView(layout);

            return dialog;
        }
    }
    @Override
    public void show() {
        if (!isShowing()){
            super.show();
        }

    }
}
