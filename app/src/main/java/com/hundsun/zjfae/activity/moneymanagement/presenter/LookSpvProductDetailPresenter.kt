package com.hundsun.zjfae.activity.moneymanagement.presenter

import com.hundsun.zjfae.activity.moneymanagement.view.LookSpvProductDetailView
import com.hundsun.zjfae.common.base.BasePresenter
import com.hundsun.zjfae.common.http.api.ConstantName
import com.hundsun.zjfae.common.http.observer.BaseObserver
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver
import io.reactivex.Observable
import onight.zjfae.afront.gens.Attachment
import onight.zjfae.afront.gens.EntrustedDetails
import onight.zjfae.afront.gens.EntrustedDetails.REQ_PBIFE_prdquery_getQueryEntrustedDetails
import onight.zjfae.afront.gens.UserHighNetWorthInfo
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails
import onight.zjfae.afront.gens.v3.UserDetailInfo
import onight.zjfae.afront.gensazj.v2.Notices
import onight.zjfae.afront.gensazj.v2.Notices.REQ_PBAPP_notice

class LookSpvProductDetailPresenter(view: LookSpvProductDetailView) :
    BasePresenter<LookSpvProductDetailView>(view) {

    /**
     * 是否合格投资者购买
     */
    private var accreditedBuyIs = "0"


    /**
     * 产品编号
     * */
    var productCode = ""

    /**
     * 持仓列表带进来的id编号
     * */
    var id = ""


    /**
     * 产品详情,产品预检查,附件列表,用户信息,四个接口合并
     */
    fun initProductData() {
        val observables: Observable<*> = Observable.mergeArrayDelayError<Any>(
            initProductDetails(productCode),
            requestAttachment(productCode), notice(), requestEntrustedDetails(productCode), userInfo
        )
        addDisposable(observables, object : BaseProductPlayProtoBufObserver<Any?>(baseView) {
            override fun onSuccess(t: Any?) {
                if (t is PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails?) {

                    accreditedBuyIs = t?.data?.taProductFinanceDetail?.accreditedBuyIs!!
                    baseView.onProductData(t)
                } else if (t is Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) {
                    baseView.onAttachmentInfo(t, accreditedBuyIs)
                } else if (t is Notices.Ret_PBAPP_notice) {
                    baseView.getEntrustedDetailsNotice(t)
                } else if (t is EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails) {
                    baseView.getEntrustedDetailsAttachment(t)
                } else if (t is UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    baseView.onUserDetailInfo(t, accreditedBuyIs)
                }
            }


        })
    }

    /**
     * 产品详情
     *
     * @param productCode 产品code
     */
    private fun initProductDetails(productCode: String): Observable<*>? {
        val product =
            PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.newBuilder()
        product.productCode = productCode
        product.different = "01"
        product.version = "1.0.1"
        product.unitId = id
        val map = getRequestMap()
        map["version"] = version
        map["isNewInterface"] = "1"
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryProductDetails, map)

        return apiServer.productCodeInfo(url, getBody(product.build().toByteArray()))
    }


    /**
     * 详情附件查询
     */
    private fun requestAttachment(productCode: String): Observable<*>? {
        val attachment = Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.newBuilder()
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
     * 022免责声明
     */
    private fun notice(): Observable<*>? {
        val notice = REQ_PBAPP_notice.newBuilder()
        notice.type = "022"
        val map = getRequestMap()
        map["version"] = twoVersion
        val url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Notice, map)
        return apiServer.notice(url, getBody(notice.build().toByteArray()))
    }

    /**
     * 受托管理报告
     */
    private fun requestEntrustedDetails(productCode: String): Observable<*>? {
        val builder = REQ_PBIFE_prdquery_getQueryEntrustedDetails.newBuilder()
        builder.productCode = productCode
        val map: Map<*, *> = getRequestMap()
        val url =
            parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_getQueryEntrustedDetails)
        return apiServer.entrustedDetailsList(url, getBody(builder.build().toByteArray()))
    }

    /**
     * 点击附件查询用户详细信息
     */
    fun attachmentUserInfo(
        accreditedBuyIs: String?,
        attachmentList: Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList?,
        riskLevelLabelName: String?,
        riskLevelLabelUrl: String?,
        isAttachment: Boolean
    ) {
        addDisposable(
            userInfo,
            object :
                BaseProductPlayProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?>(
                    baseView
                ) {
                override fun onSuccess(userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?) {
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
     * 合格投资者申请失败原因
     */
    fun requestInvestorStatus(isRealInvestor: String?) {
        val url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo, getRequestMap())
        val builder =
            UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder()
        builder.dynamicType1 = "highNetWorthUpload"
        addDisposable(
            apiServer.investorStatus(url, getBody(builder.build().toByteArray())),
            object :
                BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?>(
                    baseView
                ) {
                override fun onSuccess(body: UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?) {
                    baseView.requestInvestorStatus(body, isRealInvestor)
                }
            })
    }
}