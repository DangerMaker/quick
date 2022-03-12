package com.hundsun.zjfae.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.MoneyUtil;

/*
 * 计算收益
 * */
public class EarningsDialog extends Dialog {


    public EarningsDialog(Context context) {
        super(context);
    }

    public EarningsDialog(Context context, int themeResId) {
        super(context, themeResId);
    }


    public static class Builder {
        private String amount;// 金额

        private String rate;//利率

        private String timeLimit;//期限

        private Context context;//上下文对象

        public Builder(Context context) {
            this.context = context;
        }


        public void setAmount(String amount) {
            this.amount = amount.replaceAll(",", "");
//            this.amount = MoneyUtil.formatMoney(amount);
        }

        public void setRate(String rate) {
            this.rate = rate.replaceAll(",", "");
        }

        public void setTimeLimit(String timeLimit) {
            this.timeLimit = timeLimit.replaceAll(",", "");
        }


        public EarningsDialog create() {
            final EditText amount_ed, timeLimit_ed, rate_ed;
            final TextView count_tv;
            final EarningsDialog dialog = new EarningsDialog(context, R.style.mystyle);
            View rootView = LayoutInflater.from(context).inflate(R.layout.earnings_dialog_layout, null);
            dialog.addContentView(rootView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            count_tv = rootView.findViewById(R.id.count_tv);
            amount_ed = rootView.findViewById(R.id.amount);
            amount_ed.setText(addComma(amount, amount_ed));
            amount_ed.setSelection(addComma(amount, amount_ed).length());
            amount_ed.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (!touzi_ed_values22.equals(amount_ed.getText().toString().trim().replaceAll(",", ""))) {
                        amount_ed.setText(addComma(amount_ed.getText().toString().trim().replaceAll(",", ""), amount_ed));
                        amount_ed.setSelection(addComma(amount_ed.getText().toString().trim().replaceAll(",", ""), amount_ed).length());
                    }
                }
            });

            timeLimit_ed = rootView.findViewById(R.id.timeLimit);
            timeLimit_ed.setText(timeLimit);

            rate_ed = rootView.findViewById(R.id.rate);
            rate_ed.setText(rate);


            rootView.findViewById(R.id.count_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = amount_ed.getText().toString();
                    rate = rate_ed.getText().toString();
                    timeLimit = timeLimit_ed.getText().toString();
                    count(count_tv);
                }
            });

            rootView.findViewById(R.id.image_clean).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            SupportDisplay.setChildViewParam((ViewGroup) rootView, false);
            return dialog;
        }

        private void count(TextView count_tv) {
            amount = amount.replaceAll(",", "");
            if (amount == null || amount.equals("") || amount.equals(".")) {
                amount = "0.00";
            }
            if (timeLimit == null || timeLimit.equals("") || timeLimit.equals(".")) {
                timeLimit = "0.00";
            }
            if (rate == null || rate.equals("") || rate.equals(".")) {
                rate = "0.00";
            }


//            rate = MoneyUtil.moneydiv(rate,"100.00");
//
//            amount = MoneyUtil.moneyMul(amount,timeLimit);
//
//            String counts = MoneyUtil.moneyMul(rate,amount);
//
//            String earnings =  MoneyUtil.moneydiv(counts,"365");
//
//            count_tv.setText(earnings);


            double at = Double.parseDouble(amount.trim());
            double tm = Double.parseDouble(timeLimit.trim());
            double rt = Double.parseDouble(rate.trim());
            double counts = (at * tm * (rt / 100)) / 365;
            String earnings = String.valueOf(counts);
            count_tv.setText(MoneyUtil.fmtMicrometer(earnings));

        }


    }

    /**
     *      * 在数字型字符串千分位加逗号
     *      * @param str
     *      * @param edtext
     *      * @return sb.toString()
     *     
     */
    public static String touzi_ed_values22 = "";

    public static String addComma(String str, EditText edtext) {

        touzi_ed_values22 = edtext.getText().toString().trim().replaceAll(",", "");

        boolean neg = false;
        if (str.startsWith("-")) {//处理负数  
            str = str.substring(1);
            neg = true;
        }
        String tail = null;
        if (str.indexOf('.') != -1) { //处理小数点  
            tail = str.substring(str.indexOf('.'));
            str = str.substring(0, str.indexOf('.'));
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, ',');
        }
        sb.reverse();
        if (neg) {
            sb.insert(0, '-');
        }
        if (tail != null) {
            sb.append(tail);
        }
        return sb.toString();
    }

}
