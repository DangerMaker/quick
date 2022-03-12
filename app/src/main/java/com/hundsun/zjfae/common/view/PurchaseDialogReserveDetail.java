package com.hundsun.zjfae.common.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.MoneyUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.toast.ToastUtil;
import com.hundsun.zjfae.common.utils.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @Description:长短预约详情 确认预约弹输入金额的框
 * @Author: zhoujianyu
 * @Time: 2018/10/23 14:30
 */
public class PurchaseDialogReserveDetail extends Dialog {
    public PurchaseDialogReserveDetail(Context context) {
        super(context);
    }

    public PurchaseDialogReserveDetail(Context context, int theme) {
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

        private PurchaseMoney purchaseMoney;
        private float alpha = 1.0f;
        //起购金额
        private String mSmallAmount = "";
        //最高预约金额
        private String mMostAmount = "";
        private TextView tv_baozheng;//保证金控件
        private String depositRate = "";//保证金比例

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


        public Builder setPurchaseMoney(PurchaseMoney purchaseMoney) {
            this.purchaseMoney = purchaseMoney;
            return this;
        }

        public Builder setSmallAndMostMoney(String smallAmount, String mostAmount, String depositRate) {
            this.mSmallAmount = smallAmount;
            this.mMostAmount = mostAmount;
            this.depositRate = depositRate;
            return this;
        }

        @SuppressLint("InflateParams")
        public PurchaseDialogReserveDetail create() {
            final EditText money;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PurchaseDialogReserveDetail dialog = new PurchaseDialogReserveDetail(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.purchase_reservedetail_layout, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            money = layout.findViewById(R.id.message);
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((TextView) layout.findViewById(R.id.tv_depositRate)).setText("保证金比例" + depositRate + "%");
            tv_baozheng = layout.findViewById(R.id.tv_baozheng);
            // set the confirm button

            layout.findViewById(R.id.confirm_btn)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                                    confirm_btnClickListener.onClick(dialog,
//                                            DialogInterface.BUTTON_POSITIVE);
                            if (Utils.isViewEmpty(money)) {
                                ToastUtil.showToast(context, "金额不能为空");
                                return;
                            }
                            if (purchaseMoney != null) {
                                if (Utils.isFastDoubleClick()) {
                                    return;
                                }
                                purchaseMoney.purchaseMoney(true,dialog, money.getText().toString());
                                dialog.dismiss();
                            }
                        }
                    });
            // set the cancel button
            if (cancel_btnText != null) {
                layout.findViewById(R.id.cancel_btn);

                ((TextView) layout.findViewById(R.id.cancel_btn))
                        .setText(cancel_btnText);
                if (cancel_btnClickListener != null) {
                    layout.findViewById(R.id.cancel_btn)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancel_btnClickListener.onClick(dialog,
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }

            }
            money.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {


                    //输入金额
                    String tv_money = editable.toString();

                    if (!StringUtils.isEmpty(tv_money)) {
//                        if (!MoneyUtil.moneyCompare(tv_money, mMostAmount)) {
                        if (MoneyUtil.moneyCompare(tv_money, mSmallAmount)) {
                            String rate = moneydiv(depositRate, "100.00");
                            String account = MoneyUtil.moneyMul(tv_money, rate);
                            tv_baozheng.setText("保证金:" + account + "元");
                            //去外层调用接口
                            purchaseMoney.purchaseMoney(false,dialog, money.getText().toString());
                        } else {
                            tv_baozheng.setText("保证金:元");
                        }
//                        } else {
//                            //去外层调用接口
//                            purchaseMoney.purchaseMoney(dialog, money.getText().toString());
//                        }
                    }


//
//                    if (StringUtils.isNotBlank(money.getText().toString())) {
//
//
//                        String rate = MoneyUtil.moneydiv(depositRate,"100.00");
//
//                        String account =  MoneyUtil.moneyMul(tv_money,rate);
//
//                        //Float number = Float.parseFloat(money.getText().toString());
//
//
//
//                        if ( MoneyUtil.moneyComp(String.valueOf(mMostAmount),tv_money)){
//                            //去外层调用接口
//                            purchaseMoney.purchaseMoney(dialog, money.getText().toString());
//                        }
//                        else {
//
//                            if (MoneyUtil.moneyComp(tv_money,String.valueOf(mMostAmount))){
//                                tv_baozheng.setText("保证金:" + account + "元");
//                            }
//                            else {
//                                tv_baozheng.setText("保证金:元");
//                            }
//                        }
//
//
//
////                        if (number <= mMostAmount) {
////                            if (number >= mSmallAmount) {
////                                DecimalFormat df2 = new DecimalFormat("###.00");
////                                String result = df2.format(number * (Float.parseFloat(depositRate) / 100));
////                                tv_baozheng.setText("保证金:" + account + "元");
////                            } else {
////                                tv_baozheng.setText("保证金:元");
////                            }
////
////                        } else {
////                            //去外层调用接口
////                            purchaseMoney.purchaseMoney(dialog, money.getText().toString());
//////                            CustomDialog.Builder builder = new CustomDialog.Builder(context);
//////                            builder.setTitle("温馨提示");
////////                            builder.setMessage("预约金额【" + number + "】不能大于该预约产品最高购买金额【" + mMostAmount + "】。");
//////                            builder.setMessage("预约产品剩余可预约金额【" + mMostAmount + "】小于您预约金额，请重新预约 。");
//////                            builder.setNegativeButton("知道了", new DialogInterface.OnClickListener() {
//////                                @Override
//////                                public void onClick(DialogInterface dialog, int which) {
//////                                    money.setText("");
////////                                    money.setHint(mSmallAmount + "");
////////                                    money.setSelection(money.getText().toString().length());//光标设置到最后
//////                                    tv_baozheng.setText("保证金:元");
//////                                    dialog.dismiss();
//////                                }
//////                            });
//////                            builder.create().show();
////                        }
//                    }

                }
            });
            // set the content message
            if (message != null) {
//                money.setHint("请输入预约金额");
            }


            dialog.setCancelable(false);
            SupportDisplay.setChildViewParam((ViewGroup) layout, false);
            dialog.setContentView(layout);

            return dialog;
        }

    }

    public interface PurchaseMoney {

        /**
         * @param commit 是否确认提交
         * @param dialog  dialog对象
         * @param account  金额
         * */
        void purchaseMoney(boolean commit,DialogInterface dialog, String account);

    }

    /**
     * 金额相除 <br/> 保留四位小数
     * 精确小位小数
     *
     * @param valueStr  基础值
     * @param divideStr 被乘数
     * @return
     */
    public static String moneydiv(String valueStr, String divideStr) {
        DecimalFormat fnum = new DecimalFormat("0.0000");
        BigDecimal value = new BigDecimal(valueStr);
        BigDecimal divideValue = new BigDecimal(divideStr);
        return fnum.format(value.divide(divideValue, 4, BigDecimal.ROUND_HALF_UP));
    }
}
