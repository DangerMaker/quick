package com.hundsun.zjfae.activity.login;

public interface LoginInfoListener {


    void login(String userName,String passWord, String loginMethod,String authCode,String needValidateAuthCode);

    void onRegister();

    void forgetPassWord();

    void refreshImageCode();

    void loginErrorInfo(String errMsg);

    /**
     * 获取定位权限
     * */
    void onLocationPermissions();

    void onFinish();


     /**
      * 区块链跳转webview
      * @method
      * @description 描述一下方法的作用
      * @date: 2019-09-26 11:07
      * @author: moran
      * @param url 链接
      * @return
      */
    void startWebActivity(String url);



}
