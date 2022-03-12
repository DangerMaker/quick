package com.hundsun.zjfae.common.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.hundsun.zjfae.common.utils.Utils;

public class AmountOfConversion implements TextWatcher {

    private TextView amount;


    //RechargeAmountConversion
    public AmountOfConversion(TextView amount){
        this.amount = amount;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() !=0 ){
            String money =  Utils.number2CNMontrayUnit(s.toString());
            amount.setText(money);
        }
        else {
            amount.setText("零元整");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        int len = s.toString().length();
//        if (len == 1 && text.equals("0")) {
//            s.clear();
//        }
//        else
            if (len == 1 && text.equals(".")){

            s.clear();
        }
    }
}
