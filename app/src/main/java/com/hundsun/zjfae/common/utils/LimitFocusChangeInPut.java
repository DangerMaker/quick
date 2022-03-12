package com.hundsun.zjfae.common.utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

/**
 *
 * 未点击短信获取
 * */
public class LimitFocusChangeInPut implements View.OnFocusChangeListener {


    private boolean flag;

    private InPutState inPutState;

    private EditText editText;

    public LimitFocusChangeInPut(boolean flag, EditText editText){
        this.flag = flag;
        this.editText = editText;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setInPutState(InPutState inPutState) {
        this.inPutState = inPutState;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus){
            //禁止输入
            if (!flag){
                editText.clearFocus();
                if (inPutState != null){
                    inPutState.state();
                }

            }
        }
    }


    public  interface InPutState{

        void state();

    }

}
