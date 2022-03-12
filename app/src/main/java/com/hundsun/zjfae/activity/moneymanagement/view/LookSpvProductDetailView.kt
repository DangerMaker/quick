package com.hundsun.zjfae.activity.moneymanagement.view

import com.hundsun.zjfae.common.base.BaseView
import onight.zjfae.afront.gens.Attachment
import onight.zjfae.afront.gens.EntrustedDetails
import onight.zjfae.afront.gens.UserHighNetWorthInfo
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails
import onight.zjfae.afront.gens.v3.UserDetailInfo
import onight.zjfae.afront.gensazj.v2.Notices

interface LookSpvProductDetailView : BaseView {



    /**
     * 产品详情
     * @param productDetails 产品详情信息
     * @param requestTime 请求时间差
     */
    fun onProductData(productDetails: PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails?)


    /**
     * 附件详细信息
     * @param attachmentList 附件列表信息
     * @param accreditedBuyIs 是否合格投资者购买
     */
    fun onAttachmentInfo(attachmentList: Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment?, accreditedBuyIs: String?)


    /**
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     */
    fun onUserDetailInfo(userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?, accreditedBuyIs: String?)



    /**
     * 点击附件查询用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     * @param attachmentList 附件列表
     * @param riskLevelLabelName 附加文件名称
     * @param riskLevelLabelUrl 附件加载Url
     * @param isAttachment 附件还是信用评级
     */
    fun getAttachmentUserInfo(userDetailInfo: UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo?, accreditedBuyIs: String?, attachmentList: Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList?, riskLevelLabelName: String?, riskLevelLabelUrl: String?, isAttachment: Boolean)



    /**
     * 失败原因
     * @param body 合格投资者失败原因
     * @param isRealInvestor 是否是合格投资者
     */
    fun requestInvestorStatus(body: UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?, isRealInvestor: String?)

     /**
      * 受托管理报告列表
      * @method
      * @date: 2020/10/29 14:41
      * @author: moran
      * @param entrustedDetails 受托管理报告列表实体类
      * @return
      */
    fun getEntrustedDetailsAttachment(entrustedDetails: EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails)
     /**
      * 受托管理报告免责声明
      * @method
      * @date: 2020/10/29 14:41
      * @author: moran
      * @param notice 受托管理报告免责声明实体类
      * @return
      */
    fun getEntrustedDetailsNotice(notice: Notices.Ret_PBAPP_notice)

}