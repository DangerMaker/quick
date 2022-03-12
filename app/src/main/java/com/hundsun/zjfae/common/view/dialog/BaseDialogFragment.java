package com.hundsun.zjfae.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.hundsun.zjfae.R;

public abstract class BaseDialogFragment extends DialogFragment {


    protected Dialog dialog;

    protected Context mContext;


    protected View mRootView;

    private static final float DEFAULT_DIM = 0.2f;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogStyle(DialogFragment.STYLE_NO_TITLE, R.style.BaseDialogFragmentStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dialog = getDialog();

        if (dialog != null){
            dialog.setCanceledOnTouchOutside(isCancel());
            dialog.setCancelable(isCancel());
        }

        mRootView = inflater.inflate(getLayoutId(),container,false);
        resetLayout();
        initView();
        initData();
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        baseInitLayoutParams();

    }

    protected  void resetLayout(){}



    protected  void baseInitLayoutParams(){}

    protected void initLayoutParams(){
        if(dialog==null){
            dialog = getDialog();
        }
        Window window = dialog.getWindow();
        if(window!=null){
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = getDimAmount();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            if (getHeight() > 0) {
                params.height = getHeight();
            } else {
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            params.gravity = Gravity.CENTER;
            window.setAttributes(params);
        }
    }


    public float getDimAmount() {
        return DEFAULT_DIM;
    }

    public int getHeight() {
        return -1;
    }


    protected void setAlpha(float alpha){
        dialog = getDialog();
        if (dialog != null){
            Window  window= dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
        }



    }


    public void show(FragmentManager transaction) {
        if (!isAdded()){
            super.show(transaction,BaseDialogFragment.class.getSimpleName());
        }

        //  super.show(transaction,BaseDialogFragment.class.getSimpleName());


    }



    public void dismissDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
            dialog = null;
        }
    }

    /**
     * 控件寻找id
     */
    public final <T extends View> T findViewById(int id) {

        return mRootView.findViewById(id);
    }



    public abstract void initView();


    public abstract int getLayoutId();


    protected void initData(){

    }



    /**
     * 设置是否可以cancel
     * @return
     */
    protected abstract boolean isCancel();

    //设置style风格
    public void setDialogStyle(int style, int theme) {
        super.setStyle(style, theme);
    }
}
