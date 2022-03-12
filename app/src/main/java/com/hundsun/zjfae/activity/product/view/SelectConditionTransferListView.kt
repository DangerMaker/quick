package com.hundsun.zjfae.activity.product.view

import com.hundsun.zjfae.common.base.BaseView
import onight.zjfae.afront.gens.UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo
import onight.zjfae.afront.gens.v3.UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
import onight.zjfae.afront.gens.v4.TransferList.Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew

interface SelectConditionTransferListView : BaseView {


    /**
     * 转让列表
     * @param listNew 转让列表
     */
    fun initTransfer(listNew: Ret_PBIFE_prdtransferquery_prdQueryTransferOrderListNew)


    /**
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     */
    fun onUserInfo(
        userDetailInfo: Ret_PBIFE_userbaseinfo_getUserDetailInfo?,
        isAuthentication: Boolean
    )

    /**
     * 失败原因
     * @param body 用户详细信息
     * @param isRealInvestor  是否真正合格投资者
     */
    fun requestInvestorStatus(
        body: Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo?,
        isRealInvestor: String?
    )


    /**
     * 列表为空时展示的图片
     *
     * @param isjump = 是否跳转
     * @param jumpurl  跳转形式，url，合格投资者，身份认证
     * @param returnMsg 图片链接
     * @param isShare 是否分享
     *
     */
    fun onInvestmentState(isjump: String?, jumpurl: String?, returnMsg: String?, isShare: String?)

}