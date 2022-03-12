package com.hundsun.zjfae.activity.mine.view;

import android.widget.ImageView;

import com.hundsun.zjfae.common.base.BaseView;

public interface CertificationView extends BaseView {


    /**
     * 腾讯ocr识别
     * */
    void onTencentOcrId(String orderNO, String appid ,String nonce ,String userID ,String sign , ImageView imageView,boolean ocrTypeMode);



    void onUserIdCardUpload(String returnMsg);
}
