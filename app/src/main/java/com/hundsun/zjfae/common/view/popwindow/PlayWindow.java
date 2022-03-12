package com.hundsun.zjfae.common.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.XNumberKeyboardView;

import java.util.List;

public class PlayWindow extends PopupWindow implements XNumberKeyboardView.IOnKeyboardListener,PopupWindow.OnDismissListener {

    private Activity activity;
    private XNumberKeyboardView view_keyboard;
    private ImageView image_back;

    private TextView box1;
    private TextView box2;
    private TextView box3;
    private TextView box4;
    private TextView box5;
    private TextView box6;

    private OnPayListener payListener;

    private View parent;
    public PlayWindow(Activity activity){
        this.activity = activity;
        close();
        init();
    }


    private void close(){

        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void setPayListener(OnPayListener payListener){
        this.payListener = payListener;
    }
    private void init(){
        View rootView = LayoutInflater.from(activity).inflate(R.layout.paly_window_layout,null);
        view_keyboard = (XNumberKeyboardView) rootView.findViewById(R.id.view_keyboard);
        image_back = rootView.findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (parent != null){

                    parent.setEnabled(true);
                }

                dismiss();
            }
        });
        view_keyboard.shuffleKeyboard();
        view_keyboard.setIOnKeyboardListener(this);
        box1 = (TextView) rootView.findViewById(R.id.pay_box1);
        box2 = (TextView) rootView.findViewById(R.id.pay_box2);
        box3 = (TextView) rootView.findViewById(R.id.pay_box3);
        box4 = (TextView) rootView.findViewById(R.id.pay_box4);
        box5 = (TextView) rootView.findViewById(R.id.pay_box5);
        box6 = (TextView) rootView.findViewById(R.id.pay_box6);
        this.setContentView(rootView);
        SupportDisplay.resetAllChildViewParam((ViewGroup) rootView);
        backgroundAlpha();
        this.setOnDismissListener(this);
        //自定义基础，设置我们显示控件的宽，高，焦点，点击外部关闭PopupWindow操作
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        //点击外部消失
        this.setOutsideTouchable(true);
        //更新试图
        this.update();
    }
    public void backgroundAlpha() {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }





    @Override
    public void onInsertKeyEvent(List text) {

        updateUi(text);
    }

    //框框里面的数字
    private void updateUi(List<String> mList) {
        // TODO Auto-generated method stub
        if(mList.size()==0){
            box1.setText("");
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==1){
            box1.setText(mList.get(0));
            box2.setText("");
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==2){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText("");
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==3){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText("");
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==4){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText("");
            box6.setText("");
        }else if(mList.size()==5){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText("");
        }else if(mList.size()==6){
            box1.setText(mList.get(0));
            box2.setText(mList.get(1));
            box3.setText(mList.get(2));
            box4.setText(mList.get(3));
            box5.setText(mList.get(4));
            box6.setText(mList.get(5));
            StringBuffer pass = new StringBuffer();
            for (String s: mList){
                pass.append(s);
            }
            if (payListener != null){
                dismiss();
                payListener.onSurePay(pass.toString());
            }
        }
    }
    @Override
    public void onDeleteKeyEvent(List text) {
        updateUi(text);

    }

    @Override
    public void onDismiss() {
        CCLog.i("dismiss","onDismiss");
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 1f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);

    }

    public void showAtLocation(View parent) {
        this.parent = parent;
        parent.setEnabled(false);
        super.showAtLocation(parent,  Gravity.BOTTOM,0,0);
    }

    @Override
    public void dismiss() {
        CCLog.i("dismiss","dismiss");
        if (parent != null){
            parent.setEnabled(true);
        }
        super.dismiss();
    }

    public interface OnPayListener{
        void onSurePay(String password);
    }

}
