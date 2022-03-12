package com.hundsun.zjfae.activity.accountcenter.view;

import com.hundsun.zjfae.common.base.BaseView;

import okhttp3.ResponseBody;

/**
 * @Description:绑定新手机号（View）
 * @Author: yangtianren
 */
public interface BindNewPhoneView extends BaseView {
    void imageCode(ResponseBody body);

    void getVerificationCode(String code, String msg);

    void modifyUserMobile(String code, String msg);

    void refreshImageAuthCode(String msg);
}
