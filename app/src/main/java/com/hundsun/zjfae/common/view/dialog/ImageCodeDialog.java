package com.hundsun.zjfae.common.view.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.api.ApiRetrofit;
import com.hundsun.zjfae.common.http.api.ApiServer;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;

import java.io.IOException;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ImageCodeDialog extends Dialog {
    public static ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    public ImageCodeDialog(@NonNull Context context) {
        super(context);
    }

    public ImageCodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context; //上下文对象
        private String confirm_btnText; //按钮名称“确定”
        private String cancel_btnText; //按钮名称“取消”
        /*按钮监听事件*/
        private OnClickListener cancel_btnClickListener;
        private ImageCodeCallback codeCallback;
        private float alpha = 1.0f;

        public Builder setContext(Context context) {
            this.context = context;
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


        public void setCodeCallback(String confirm_btnText, ImageCodeCallback codeCallback) {
            this.confirm_btnText = confirm_btnText;
            this.codeCallback = codeCallback;

        }

        @SuppressLint("InflateParams")
        public ImageCodeDialog create() {

            final ImageView imageCode;
            final EditText ed_imageCode;
            LayoutInflater inflater = LayoutInflater.from(context);
            final ImageCodeDialog dialog = new ImageCodeDialog(context, R.style.mystyle);
            View layout = inflater.inflate(R.layout.image_code_dialog_layout, null);
            imageCode = layout.findViewById(R.id.imageCode);
            imageCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadImageCode(imageCode);
                }
            });
            ed_imageCode = layout.findViewById(R.id.ed_imageCode);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            SupportDisplay.setChildViewParam((ViewGroup) layout, false);

            if (confirm_btnText != null) {
                ((TextView) layout.findViewById(R.id.confirm_btn)).setText(confirm_btnText);
                layout.findViewById(R.id.confirm_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (codeCallback != null) {
                            codeCallback.imageCodeDate(dialog, ed_imageCode.getText().toString());
                        }
                    }
                });
            } else {
                layout.findViewById(R.id.confirm_btn).setVisibility(View.GONE);
            }


            if (cancel_btnText != null) {
                ((TextView) layout.findViewById(R.id.cancel_btn)).setText(cancel_btnText);
                layout.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel_btnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }

            dialog.setCancelable(false);
            dialog.setContentView(layout);
            loadImageCode(imageCode);
            return dialog;
        }


        private void loadImageCode(final ImageView imageCode) {
            Map map = BasePresenter.getRequestMap();
            map.put("fh", BasePresenter.VIMGMZJ);
            map.put("type", "7");
            map.put("tdsourcetag", "s_pctim_aiomsg");
            map.put("version", "v2");

            String url = BasePresenter.parseUrl(BasePresenter.MZJ, BasePresenter.PBIMG, map);
            CCLog.e(url);
            apiServer.imageCode(url).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new BaseObserver<ResponseBody>() {
                        @Override
                        public void onSuccess(ResponseBody body) {
                            try {
                                ImageLoad.getImageLoad().LoadImage(context, body.bytes(), imageCode);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }


    public interface ImageCodeCallback {
        void imageCodeDate(DialogInterface dialog, String imageCode);
    }
}
