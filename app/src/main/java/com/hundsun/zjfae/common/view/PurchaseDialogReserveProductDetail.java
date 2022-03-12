package com.hundsun.zjfae.common.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Utils;

public class PurchaseDialogReserveProductDetail extends Dialog {
    public PurchaseDialogReserveProductDetail(Context context) {
        super(context);
    }

    public PurchaseDialogReserveProductDetail(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String title; //对话框标题
        private String message; //对话框内容
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
         *
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
        public PurchaseDialogReserveProductDetail create() {
            TextView account;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PurchaseDialogReserveProductDetail dialog = new PurchaseDialogReserveProductDetail(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.purchase_normal_layout_reserveproductdetail, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            account = layout.findViewById(R.id.account);
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((TextView) layout.findViewById(R.id.message)).setText("交易金额：" + message);
            // set the confirm button
            if (confirm_btnText != null) {
                layout.findViewById(R.id.confirm_btn);

                ((TextView) layout.findViewById(R.id.confirm_btn))
                        .setText(confirm_btnText);
                if (confirm_btnClickListener != null) {
                    layout.findViewById(R.id.confirm_btn)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    confirm_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }

            }
            // set the cancel button
            if (cancel_btnText != null) {
                layout.findViewById(R.id.cancel_btn);

                ((TextView) layout.findViewById(R.id.cancel_btn))
                        .setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    layout.findViewById(R.id.cancel_btn)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    cancel_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }

            }
            // set the content message
            if (message != null) {
                String accounts = Utils.numberCNMontrayUnit(message);
                CCLog.e("金额", accounts);
                account.setText(accounts+"整");
            }

            dialog.setCancelable(false);
            SupportDisplay.setChildViewParam((ViewGroup) layout, false);
            dialog.setContentView(layout);

            return dialog;
        }

    }

}
