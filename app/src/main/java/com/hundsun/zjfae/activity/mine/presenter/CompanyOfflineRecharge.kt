package com.hundsun.zjfae.activity.mine.presenter

import com.hundsun.zjfae.activity.mine.view.CompanyOfflineRechargeView
import com.hundsun.zjfae.common.base.BasePresenter
import com.hundsun.zjfae.common.http.api.ConstantName
import com.hundsun.zjfae.common.http.observer.BaseObserver
import onight.zjfae.afront.gensazj.v2.Notices

class CompanyOfflineRecharge(baseView: CompanyOfflineRechargeView) : BasePresenter<CompanyOfflineRechargeView>(baseView) {




    fun initData(){

        val notice = Notices.REQ_PBAPP_notice.newBuilder()
        notice.type = "021"

        val map = getRequestMap()
        map["version"] = twoVersion

        val url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Notice, map)

        addDisposable(apiServer.notice(url, getBody(notice.build().toByteArray())),object :BaseObserver<Notices.Ret_PBAPP_notice>(baseView){

            override fun onSuccess(t: Notices.Ret_PBAPP_notice?) {

               baseView.onNoticeContent( t!!.data.notice.noticeContent)

            }

        })





    }









}