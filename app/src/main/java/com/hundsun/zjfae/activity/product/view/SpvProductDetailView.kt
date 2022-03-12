package com.hundsun.zjfae.activity.product.view

import com.hundsun.zjfae.common.base.BaseView
import onight.zjfae.afront.gens.Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList
import onight.zjfae.afront.gens.Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment
import onight.zjfae.afront.gens.ProductSec.Ret_PBIFE_trade_subscribeProductSec
import onight.zjfae.afront.gens.UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail
import onight.zjfae.afront.gens.v2.TransferOrderSec
import onight.zjfae.afront.gens.v3.UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo

interface SpvProductDetailView : BaseView {

    /**
     * 预检查第二步
     * @param orderSecBean 转让购买检查
     */
    fun getTransferOrderSecBean(orderSecBean: TransferOrderSec.Ret_PBIFE_trade_transferOrderSec?)

    /**
     * 失败原因
     * @param body 合格投资者失败原因
     * @param isRealInvestor 是否是合格投资者
     */
    fun requestInvestorStatus(
        body: Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?,
        isRealInvestor: String?
    )


    /**
     * 立即购买获取用户信息
     * @param userDetailInfo 用户详细信息
     */
    fun getUserDetailInfo(userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?)


    /**
     * 点击附件查询用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     * @param attachmentList 附件列表
     * @param riskLevelLabelName 附加文件名称
     * @param riskLevelLabelUrl 附件加载Url
     * @param isAttachment 附件还是信用评级
     */
    fun getAttachmentUserInfo(
        userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        accreditedBuyIs: String?,
        attachmentList: TaProductAttachmentList?,
        riskLevelLabelName: String?,
        riskLevelLabelUrl: String?,
        isAttachment: Boolean
    )


    /**
     * 产品详情
     * @param productDetails 产品详情信息
     */
    fun onProductData(
        productDetails: PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail,
        requestTime: Long
    )


    /**
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     */
    fun onUserDetailInfo(
        userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        accreditedBuyIs: String?
    )


    /**
     * 附件详细信息
     * @param attachmentList 附件列表信息
     * @param accreditedBuyIs 是否合格投资者购买
     */
    fun onAttachmentInfo(
        attachmentList: Ret_PBIFE_prdquery_prdQueryProductAttachment?,
        accreditedBuyIs: String?
    )


    /**
     * 65周岁
     */
    fun onHighAge()


    /**
     * 新客专享购买提示
     * @param returnMsg 返回信息
     */
    fun onNovicePrompt(returnMsg: String?)


    /**
     * 更新用户可购买金额
     * @param subscribeProductSec
     */
    fun onUpDataUserPurchaseAmount(subscribeProductSec: TransferOrderSec.Ret_PBIFE_trade_transferOrderSec?)


    /**
     * 合格投资者申请
     * @param returnMsg 返回信息
     */
    fun onQualifiedMember(returnMsg: String?)


    /**
     *
     * 购买产品不可转让
     * @param returnMsg 返回信息
     */
    fun onPlayNoTransfer(returnMsg: String?)


    /**
     * 风险测试
     * @param returnMsg 返回信息
     */
    fun onRiskAssessment(returnMsg: String?)

    /**
     * 高净值会员
     * @param returnMsg 返回信息
     */
    fun onSeniorMember(returnMsg: String?)


}