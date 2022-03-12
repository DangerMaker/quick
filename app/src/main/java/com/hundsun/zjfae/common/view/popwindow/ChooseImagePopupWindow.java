package com.hundsun.zjfae.common.view.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hundsun.zjfae.R;


/**
 * 拍照或者选择照片弹出框
 */
public class ChooseImagePopupWindow extends PopupWindow {
    private View mConentView;
    private Context mContext;
    private boolean chooseVideo;

    private OnPopWindowOnClickListener mOnClickListener;
    private boolean mNeedDismiss;

    public interface OnPopWindowOnClickListener {
        void onCameraClick();

        void onChooseImageClick();

        void onDismiss();
    }


    public ChooseImagePopupWindow(Context context, OnPopWindowOnClickListener onClickListener) {
        this.mContext = context;
        mOnClickListener = onClickListener;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mConentView = inflater.inflate(R.layout.layout_choose_image_popup_window, null);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
        initView();

        this.setContentView(mConentView);
    }

    public ChooseImagePopupWindow(Context context, OnPopWindowOnClickListener onClickListener, boolean chooseVideo) {
        this.chooseVideo = chooseVideo;
        this.mContext = context;
        mOnClickListener = onClickListener;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mConentView = inflater.inflate(R.layout.layout_choose_image_popup_window, null);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        setFocusable(true);
        setOutsideTouchable(false);
        setBackgroundDrawable(new BitmapDrawable());
        initView();

        this.setContentView(mConentView);
    }

    // 设置背景颜色变暗
    private void setBackColorGery() {
        final Activity activity = (Activity) mContext;

        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        activity.getWindow().setAttributes(lp);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                lp.alpha = 1f;
                activity.getWindow().setAttributes(lp);
            }
        });
    }


    private void initView() {
        if (chooseVideo) {
            ((TextView) mConentView.findViewById(R.id.tv_choose_take_photo)).setText("录制");
            ((TextView) mConentView.findViewById(R.id.tv_choose_image)).setText("从本地获取");
        }
        mConentView.findViewById(R.id.tv_choose_take_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNeedDismiss = false;
                dismiss();
                mOnClickListener.onCameraClick();
            }
        });
        mConentView.findViewById(R.id.tv_choose_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNeedDismiss = false;
                dismiss();
                mOnClickListener.onChooseImageClick();
            }
        });

        mConentView.findViewById(R.id.tv_show_more_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNeedDismiss = false;
                dismiss();
                mOnClickListener.onDismiss();
            }
        });
    }


    public void showPopupWindow(View view) {
        setBackColorGery();
        showAtLocation(view, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mNeedDismiss = true;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mNeedDismiss && mOnClickListener != null)
            mOnClickListener.onDismiss();
    }

}