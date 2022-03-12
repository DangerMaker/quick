package com.hundsun.zjfae.activity.forget;

import com.hundsun.zjfae.common.base.BaseView;

import okhttp3.ResponseBody;

/**
 * @Description:忘记密码（View）
 * @Author: yangtianren
 */
public interface ForgetPasswordView extends BaseView {
    void imageCode(ResponseBody body);

    void refreshImageAuthCode(String msg);

    void checkUserName(String code, String msg);

    void sendCode(String code, String msg);

    void submitCode(String code, String msg);

    void setPassword(String code, String msg);

}
