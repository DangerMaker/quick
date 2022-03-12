package com.hundsun.zjfae.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.WebActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.Calendar;

public class HomeAdvertisingDialog extends Dialog {


    private static BaseCacheBean baseCacheBean;

    public HomeAdvertisingDialog(Context context) {
        super(context, R.style.mystyle);
    }

    public HomeAdvertisingDialog(Context context, int themeResId) {

        super(context, themeResId);

        if (ADSharePre.getConfiguration(ADSharePre.homeAd, BaseCacheBean.class) != null) {
            baseCacheBean = ADSharePre.getConfiguration(ADSharePre.homeAd, BaseCacheBean.class);
        }
    }


    public static class Builder {
        private Context context; //上下文对象
        private float alpha = 1.0f;


        public Builder(Context context) {
            this.context = context;

        }

        public Builder(Context context, float alpha) {
            this.context = context;
            this.alpha = alpha;
        }


        public HomeAdvertisingDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            final HomeAdvertisingDialog dialog = new HomeAdvertisingDialog(context, R.style.mystyle);

            View layout = inflater.inflate(R.layout.home_advertising_dialog_layout, null);
            Window window = dialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.setAttributes(lp);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ImageView ad_image = layout.findViewById(R.id.ad_image);
            final ImageView home_close_image = layout.findViewById(R.id.home_close_image);


            if (baseCacheBean != null && baseCacheBean.getIconsAddress() != null && StringUtils.isNotBlank(baseCacheBean.getIconsAddress())) {
                CCLog.e("用户广告图" + baseCacheBean.getIconsAddress());
                ImageLoad.getImageLoad().LoadImageWithSignature((HomeActivity) context, baseCacheBean.getIconsAddress(), ad_image, baseCacheBean.getResTime());
                ad_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        UserSetting.setShowHomeAd();
                        dialog.dismiss();
                        if (StringUtils.isNotBlank(baseCacheBean.getFuncUrl())) {//跳转链接不为空
                            Intent intent = new Intent(context, WebActivity.class);

                            ShareBean shareBean = new ShareBean();
                            shareBean.setFuncUrl(baseCacheBean.getFuncUrl());
                            shareBean.setIsShare(baseCacheBean.getIsShare());
                            intent.putExtra("shareBean", shareBean);

                            //intent.putExtra("url", homeAd.getFunc_url());
                            context.startActivity(intent);
                        }
                    }
                });
            }

            home_close_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    baseCacheBean.getResTime();
//                    UserSetting.setShowHomeAd();
                }
            });
            dialog.setCancelable(false);
            SupportDisplay.setChildViewParam((ViewGroup) layout, false);
            dialog.setContentView(layout);
            return dialog;
        }
    }


    @Override
    public void show() {

        if (baseCacheBean == null || baseCacheBean.getIs_show().equals("0") || baseCacheBean.getIs_show().equals("")) {
            return;
        }
        if (StringUtils.isNotBlank(baseCacheBean.getIconsAddress())) {


            final Calendar calendar = Calendar.getInstance();
            StringBuffer buffer = new StringBuffer();
            //获取系统的日期
            //年
            int year = calendar.get(Calendar.YEAR);
            buffer.append(year);
            //月
            int month = calendar.get(Calendar.MONTH) + 1;
            buffer.append(month);
            //日
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            buffer.append(day);

//            if (!baseCacheBean.getShowTime().equals(buffer.toString())){
//                ADUtils adUtils = ADSharePre.getADUtilsConfiguration(ADSharePre.homeAd);
//                baseCacheBean.setShowTime(buffer.toString());
//                Gson gson = new Gson();
//                adUtils.setContent(gson.toJson(baseCacheBean));
//                adUtils.setType(ADSharePre.homeAd);
//                ADSharePre.saveConfiguration(ADSharePre.homeAd,adUtils);
//                super.show();
//            }

        }

    }
}
