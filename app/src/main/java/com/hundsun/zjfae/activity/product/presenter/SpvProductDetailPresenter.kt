package com.hundsun.zjfae.activity.product.presenter

import com.hundsun.zjfae.activity.product.view.SpvProductDetailView
import com.hundsun.zjfae.common.base.BasePresenter
import com.hundsun.zjfae.common.http.api.ConstantName
import com.hundsun.zjfae.common.http.observer.BaseObserver
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver
import com.hundsun.zjfae.common.utils.CCLog
import io.reactivex.Observable
import onight.zjfae.afront.gens.Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList
import onight.zjfae.afront.gens.Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment
import onight.zjfae.afront.gens.Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment
import onight.zjfae.afront.gens.TransferOrderPre.REQ_PBIFE_trade_transferOrderPre
import onight.zjfae.afront.gens.UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo
import onight.zjfae.afront.gens.UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail
import onight.zjfae.afront.gens.v2.TransferOrderSec
import onight.zjfae.afront.gens.v3.UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo

class SpvProductDetailPresenter(view: SpvProductDetailView) :
    BasePresenter<SpvProductDetailView>(view) {


    /**
     * 产品编号
     * */
    var productCode = ""
        set(value) {
            CCLog.i("productCode", value)
            field = value
        }
        get() = field

    /**
     * 委托编号
     * */
    var delegationCode = ""
        set(value) {
            CCLog.i("delegationCode", value)
            field = value
        }
        get() = field


    /**
     * 委托表PK
     * */
    var delegationId = ""
        set(value) {
            field = value
        }
        get() = field

    /**
     * 是否合格投资者购买
     */
    private var accreditedBuyIs = "0"

    /**
     * 产品详情,产品预检查,附件列表,用户信息,四个接口合并
     */
    fun initProductData() {
        val startTime = System.currentTimeMillis()
        val observables: Observable<*> = Observable.mergeArrayDelayError<Any>(
            getTransferDetailInfo(), getTransferOrderPre(),
            requestAttachment(productCode), userInfo
        )
        addDisposable(observables, object : BaseProductPlayProtoBufObserver<Any?>(baseView) {
            override fun onSuccess(mClass: Any?) {
                if (mClass is PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail) {

                    val stopTime = System.currentTimeMillis()

                    val requestTime: Long = (stopTime - startTime) / 1000
                    accreditedBuyIs = mClass.data.taProductFinanceDetail.accreditedBuyIs
                    baseView.onProductData(mClass, requestTime)
                } else if (mClass is Ret_PBIFE_prdquery_prdQueryProductAttachment) {
                    baseView.onAttachmentInfo(mClass, accreditedBuyIs)
                } else if (mClass is Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    baseView.onUserDetailInfo(mClass, accreditedBuyIs)
                }
            }
        })
    }


    /**
     * 转让产品预检查
     */
    private fun getTransferOrderPre(): Observable<*>? {
        val transFer = REQ_PBIFE_trade_transferOrderPre.newBuilder()
        transFer.delegationCode = delegationCode
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrderPre)
        return apiServer.getTransferOrderPre(url, getBody(transFer.build().toByteArray()))
    }

    /**
     * 获取转让产品详细信息
     */
    private fun getTransferDetailInfo(): Observable<*>? {

        val product =
            PBIFEPrdtransferqueryPrdDeliveryInfoDetail.REQ_PBIFE_prdtransferquery_prdDeliveryInfoDetail.newBuilder()
        product.id = delegationId
        val map = getRequestMap()
        map["version"] = twoVersion
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferDetail,map)
        return apiServer.getSvpProductDetail(url, getBody(product.build().toByteArray()))
    }


    /**
     * 详情附件查询
     */
    private fun requestAttachment(productCode: String): Observable<*>? {
        val attachment = REQ_PBIFE_prdquery_prdQueryProductAttachment.newBuilder()
        attachment.productCode = productCode
        attachment.visibleFlag = "4"
        val url = parseUrl(
            MZJ,
            PBIFE,
            VREGMZJ,
            ConstantName.PBIFE_prdquery_prdQueryProductAttachment,
            getRequestMap()
        )
        return apiServer.attachmentList(url, getBody(attachment.build().toByteArray()))
    }

    /**
     * 产品购买预检查第二步
     * @param buyNum 购买金额
     */
    fun getTransferOrderSec(buyNum: String) {
        var buyNum = buyNum
        buyNum = buyNum.replace(",".toRegex(), "").trim { it <= ' ' }

        val tansFer = TransferOrderSec.REQ_PBIFE_trade_transferOrderSec.newBuilder()
        tansFer.buyNum = buyNum
        tansFer.version = "1.0.1"
        tansFer.delegationCode = delegationCode
        val map = getRequestMap()
        map["version"] = twoVersion
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrderSec, map)
        addDisposable(
            apiServer.getTransferOrderSec(url, getBody(tansFer.build().toByteArray())),
            object :
                BaseProductPlayProtoBufObserver<TransferOrderSec.Ret_PBIFE_trade_transferOrderSec?>(
                    baseView
                ) {
                override fun onSuccess(transferOrderSec: TransferOrderSec.Ret_PBIFE_trade_transferOrderSec?) {
                    baseView.getTransferOrderSecBean(transferOrderSec)
                }
            })
    }

    /**
     * 点击附件查询用户详细信息
     */
    fun attachmentUserInfo(
        accreditedBuyIs: String?,
        attachmentList: TaProductAttachmentList?,
        riskLevelLabelName: String?,
        riskLevelLabelUrl: String?,
        isAttachment: Boolean
    ) {
        addDisposable(
            userInfo,
            object :
                BaseProductPlayProtoBufObserver<Ret_PBIFE_userbaseinfo_getUserDetailInfo?>(baseView) {
                override fun onSuccess(userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?) {
                    baseView.getAttachmentUserInfo(
                        userDetailInfo,
                        accreditedBuyIs,
                        attachmentList,
                        riskLevelLabelName,
                        riskLevelLabelUrl,
                        isAttachment
                    )
                }
            })
    }

    /**
     * 获取用户信息
     */
    fun getUserDate() {
        addDisposable(
            userInfo,
            object :
                BaseProductPlayProtoBufObserver<Ret_PBIFE_userbaseinfo_getUserDetailInfo?>(baseView) {
                override fun onSuccess(userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?) {
                    baseView.getUserDetailInfo(userDetailInfo)
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
                override fun onSuccess(body: Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?) {
                    baseView.requestInvestorStatus(body, isRealInvestor)
                }
            })
    }


    //65周岁提交
    fun ageRequest() {
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.AgeReminder)
        addDisposable(url, object : BaseObserver<Any?>(baseView) {
            override fun onSuccess(o: Any?) {}
        })
    }
}