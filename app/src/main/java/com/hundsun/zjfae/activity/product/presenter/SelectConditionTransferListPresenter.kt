package com.hundsun.zjfae.activity.product.presenter

import android.util.Log
import com.hundsun.zjfae.activity.product.view.SelectConditionTransferListView
import com.hundsun.zjfae.common.base.BasePresenter
import com.hundsun.zjfae.common.http.api.ConstantName
import com.hundsun.zjfae.common.http.observer.BaseObserver
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver
import com.hundsun.zjfae.common.utils.CCLog
import onight.zjfae.afront.gens.UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo
import onight.zjfae.afront.gens.UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo
import onight.zjfae.afront.gens.v3.UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
import onight.zjfae.afront.gens.v4.TransferList
import onight.zjfae.afront.gens.v4.TransferList.REQ_PBIFE_prdtransferquery_prdQueryTransferOrderListNew
import java.lang.StringBuilder

class SelectConditionTransferListPresenter(view: SelectConditionTransferListView) :
    BasePresenter<SelectConditionTransferListView>(view) {


    //起投金额的uuid
    var amountUuid = ""

    //剩余期限的uuid
    var timeUuid = ""


    //起投金额和剩余期限的uuid
    //var amountAndTimeUUid = ""

    //排序的uuid
    var incomeUUid = ""


    fun transferList(pageIndex: Int = 1) {

        CCLog.i("amountuuid", amountUuid)

        CCLog.i("timeuuid", timeUuid)

        CCLog.i("incomeuuid", incomeUUid)

        val uuidBuilder = StringBuilder()

        if (amountUuid != "" && timeUuid != "" && incomeUUid != "") {

            uuidBuilder.append(amountUuid).append("-")

            uuidBuilder.append(timeUuid).append("-")

            uuidBuilder.append(incomeUUid)
        } else if (amountUuid == "" && timeUuid != "" && incomeUUid != "") {
            uuidBuilder.append("-")
            uuidBuilder.append(timeUuid).append("-")
            uuidBuilder.append(incomeUUid)

        } else if (amountUuid == "" && timeUuid == "" && incomeUUid != "") {
            uuidBuilder.append("-")
            uuidBuilder.append("-")
            uuidBuilder.append(incomeUUid)
        } else if (amountUuid != "" && timeUuid == "" && incomeUUid != "") {
            uuidBuilder.append(amountUuid)
            uuidBuilder.append("-")
            uuidBuilder.append(incomeUUid)
        } else if (amountUuid != "" && timeUuid == "" && incomeUUid == "") {

            uuidBuilder.append(amountUuid)
            uuidBuilder.append("-")
            uuidBuilder.append("-")
        } else if (amountUuid == "" && timeUuid != "" && incomeUUid == "") {

            uuidBuilder.append("-")
            uuidBuilder.append(timeUuid)
            uuidBuilder.append("-")

        } else if (amountUuid != "" && timeUuid != "" && incomeUUid == "") {

            uuidBuilder.append(amountUuid).append("-")
            uuidBuilder.append(timeUuid)
            uuidBuilder.append("-")
        } else if (amountUuid == "" && timeUuid == "" && incomeUUid == "") {

            uuidBuilder.append("-")
            uuidBuilder.append("-")
            uuidBuilder.append("-")

        }


        Log.i("请求的uuid", uuidBuilder.toString())

        //转让专区
        val trans = REQ_PBIFE_prdtransferquery_prdQueryTransferOrderListNew.newBuilder()
        trans.pageIndex = pageIndex.toString()
        trans.pageSize = "20"
        trans.productName = ""
        trans.uuids = uuidBuilder.toString()
        val map = getRequestMap()
        map["version"] = FOUR_VERSION
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferOrderListNew, map)

        addDisposable(apiServer.transferList(url, getBody(trans.build().toByteArray())),
            object :
                ProtoBufObserver<TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew>(
                    baseView
                ) {
                override fun onSuccess(t: TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew) {

                    baseView.initTransfer(t)
                }


            })

    }


    fun getUserData(isAuthentication: Boolean) {
        addDisposable(
            userInfo,
            object : ProtoBufObserver<Ret_PBIFE_userbaseinfo_getUserDetailInfo?>(baseView) {


                override fun onSuccess(t: Ret_PBIFE_userbaseinfo_getUserDetailInfo?) {
                    baseView.onUserInfo(t, isAuthentication)
                }
            })
    }

    /**
     * 合格投资者申请失败原因
     */
    fun requestInvestorStatus(isRealInvestor: String?) {
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo, getRequestMap())
        val builder = REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder()
        builder.dynamicType1 = "highNetWorthUpload"
        addDisposable(
            apiServer.investorStatus(url, getBody(builder.build().toByteArray())),
            object : BaseObserver<Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?>(baseView) {

                override fun onSuccess(t: Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?) {
                    baseView.requestInvestorStatus(t, isRealInvestor)
                }
            })
    }


}