package com.hundsun.zjfae.common.view.popwindow;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class MaterialsUploadedPopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {

    private Activity activity;

    public MaterialsUploadedPopupWindow(Activity activity){
        this.activity = activity;
    }


    public void setCreateView(CreateView createView){
        this.setContentView(createView.contentView());
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setOnDismissListener(this);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.update();
    }

    public void showAsDropDown(View parent) {
        backgroundAlpha();
        super.showAsDropDown(parent, Gravity.BOTTOM, 0, 0);
    }

    public interface CreateView{
        View contentView();
    }
    @Override
    public void onDismiss() {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 1f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);

    }


    public void backgroundAlpha() {
        WindowManager.LayoutParams lp = activity.getWindow()
                .getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setAttributes(lp);
    }

    public void dismiss() {
        if (isShowing()){
            super.dismiss();
        }

    }
}
