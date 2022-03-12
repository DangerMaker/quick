package com.hundsun.zjfae.common.base.kotlin

import android.content.Context

interface KotlinBaseView {


    /**
     * 显示dialog
     */
     fun showLoading()

    /**
     * 显示dialog
     * @param content 提示内容
     */
     fun showLoading(content: String)


    /**
     * 隐藏 dialog
     */

     fun hideLoading()

    /**
     * 显示错误信息
     *
     * @param msg
     */
     fun showError(msg: String)


    /**
     * 登录超时
     * @param msg 登录超时信息
     */
     fun loginTimeOut(msg: String)

    /**
     * 上下文
     *
     * @return context
     */
     fun onAttach(): Context


    /**
     * 是否显示dialog
     * @return true --默认显示
     */
     fun isShowLoad(): Boolean

    /**
     * 关闭当前Activity
     * @param returnMsg 返回信息
     */
     fun isFinishActivity(returnMsg: String)
}