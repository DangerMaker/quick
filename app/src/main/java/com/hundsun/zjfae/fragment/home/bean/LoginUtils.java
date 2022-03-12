package com.hundsun.zjfae.fragment.home.bean;

public class LoginUtils {

    private static LoginUtils loginUtils = null;

    /****************/
    private boolean isLogin = false;

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public static LoginUtils getInstance(){
        if (loginUtils == null){
            loginUtils = new LoginUtils();
        }
        return loginUtils;
    }




}
