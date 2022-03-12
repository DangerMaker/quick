package com.hundsun.zjfae.common.base;


import android.content.Context;

import onight.zjfae.afront.gens.ProductSec;

/**
 * @author moran
 * 时间： 2016/12/27.13:56
 * 描述：
 * 来源：
 */

public interface BaseView  {
    /**
     * 显示dialog
     */
    void showLoading();

    /**
     * 显示dialog
     * @param content 提示内容
     */
    void showLoading(String content);


    /**
     * 隐藏 dialog
     */

    void hideLoading();

    /**
     * 显示错误信息
     *
     * @param msg
     */
    void showError(String msg);


    /**
     * 登录超时
     * @param msg 登录超时信息
     * */
    void loginTimeOut(String msg);

    /**
     * 上下文
     *
     * @return context
     */
    Context onAttach();


    /**
     * 是否显示dialog
     * @return true --默认显示
     * */
    boolean isShowLoad();

    /**
     * 关闭当前Activity
     * @param returnMsg 返回信息
     * */
    void isFinishActivity(String returnMsg);
}
