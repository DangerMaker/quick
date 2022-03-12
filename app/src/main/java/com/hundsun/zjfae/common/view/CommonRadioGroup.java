package com.hundsun.zjfae.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.hundsun.zjfae.R;

public class CommonRadioGroup extends RadioGroup {

    public CommonRadioGroup(Context context) {
        super(context);
    }

    public CommonRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        changeButtonsImages();
    }

    private void changeButtonsImages(){
        int count = super.getChildCount();
        if(count > 1){
            super.getChildAt(0).setBackgroundResource(R.drawable.radiobutton_left);
            super.getChildAt(0).setPadding(0, 0, 0, 0);
            for(int i=1; i < count-1; i++){
                super.getChildAt(i).setBackgroundResource(R.drawable.radiobutton_center);
                super.getChildAt(i).setPadding(0, 0, 0, 0);
            }
            super.getChildAt(count-1).setBackgroundResource(R.drawable.radiobutton_right);
            super.getChildAt(count-1).setPadding(0, 0, 0, 0);
        }else if (count == 1){
            super.getChildAt(0).setBackgroundResource(R.drawable.radiobutton_sigle);
            super.getChildAt(0).setPadding(0, 0, 0, 0);
        }
    }
}