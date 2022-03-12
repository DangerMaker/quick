package com.hundsun.zjfae.common.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hundsun.zjfae.common.utils.Utils;

public class RechargeAmountConversion implements TextWatcher {

    private TextView amount, recharge_money;
    private int beforeDot = 9;
    private int afterDot = 2;

    //RechargeAmountConversion
    public RechargeAmountConversion(EditText recharge_money, TextView amount) {
        this.recharge_money = recharge_money;
        this.amount = amount;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        if (s.length() != 0) {
//            String money = Utils.number2CNMontrayUnit(s.toString());
//            amount.setText(money);
//        } else {
//            amount.setText("零元整");
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        judge(s);
        if (s.length() != 0) {
            String money = Utils.number2CNMontrayUnit(s.toString());
            amount.setText(money);
        } else {
            amount.setText("零元整");
        }
//        String text = s.toString();
//        int len = s.toString().length();
//        if (len == 1 && text.equals(".")) {
//            s.clear();
//        }
    }

    private void judge(Editable s) {
        String temp = s.toString();
        int posDot = temp.indexOf(".");
        //直接输入小数点的情况
        if (posDot == 0) {
            s.insert(0, ".");
            return;
        }
        //连续输入0
        if (temp.equals("00")) {
            s.delete(1, 2);
            return;
        }
        //输入08等类似情况
        if (temp.startsWith("0") && temp.length() > 1 && (posDot == -1 || posDot > 1)) {
            s.delete(0, 1);
            return;
        }
        //不包含小数点
        if (posDot < 0) {
            if (temp.length() > beforeDot) {
                s.delete(beforeDot, beforeDot + 1);
            }
        } else {
            //包含小数点
            if (temp.length() - posDot - 1 > afterDot) {
                s.delete(posDot + afterDot + 1, posDot + afterDot + 2);
            }
            //整数位
            if (posDot > beforeDot) {
                s.delete(beforeDot, posDot);
            }
        }

//        StringBuilder sb = new StringBuilder(temp);
//        //千分位
//        if (posDot == -1 && temp.length() > 3) {
//            int length = temp.length();
//            for (int i = 0; i < length / 3; i++) {
//                sb.insert(length - 3 * (i + 1), ",");
//            }
//        } else if (posDot > 3) {
//            for (int i = 0; i < posDot / 3; i++) {
//                sb.insert(posDot - 3 * (i + 1), ",");
//            }
//        }
//        recharge_money.setText(sb.toString());
    }
}
