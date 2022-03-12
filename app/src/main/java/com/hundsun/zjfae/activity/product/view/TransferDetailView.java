package com.hundsun.zjfae.activity.product.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.TransferBuyProfits;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail;
import onight.zjfae.afront.gens.v2.TransferOrderSec;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @author moran
 * 转让详情View
 */
public interface TransferDetailView extends BaseView {

    /**
     * 预检查第二步
     *
     * @param orderSecBean 转让购买检查
     */
    void getTransferOrderSecBean(TransferOrderSec.Ret_PBIFE_trade_transferOrderSec orderSecBean);

    /**
     * 申请合格投资者失败原因
     *
     * @param userHighNetWorthInfo 实体类
     * @param isRealInvestor       当前产品是否合格投资者购买
     */
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo userHighNetWorthInfo, String isRealInvestor);


    /**
     * 转让购买检查
     *
     * @param userDetailInfo 用户详细信息
     */
    void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    /**
     * 点击附件查询用户详细信息
     *
     * @param userDetailInfo     用户详细信息
     * @param accreditedBuyIs    是否合格投资者购买
     * @param attachmentList     附件列表
     * @param riskLevelLabelName 附加文件名称
     * @param riskLevelLabelUrl  附件加载Url
     * @param isAttachment       是附件还是信用评级
     **/
    void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment);


    /**
     * 转让产品详细信息
     *
     * @param productDetails 转让产品详细信息类
     */
    void onTransferData(PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail productDetails);


    /**
     * 用户详细信息
     *
     * @param userDetailInfo  用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     **/

    void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs);


    /**
     * 本金信息
     *
     * @param transferBuyProfits 转让本金信息
     */
    void onEarningsInfo(TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits transferBuyProfits);

    /**
     * 附件详细信息
     *
     * @param attachmentList  附件列表信息
     * @param accreditedBuyIs 是否合格投资者购买
     **/
    void onAttachmentInfo(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentList, String accreditedBuyIs);


    /**
     * 65周岁
     */
    void onHighAge();


    /**
     * 合格投资者申请
     *
     * @param returnMsg 返回信息
     */
    void onQualifiedMember(String returnMsg);


    /**
     * 风险测试
     *
     * @param returnMsg 返回信息
     */
    void onRiskAssessment(String returnMsg);

    /**
     * 高净值会员
     *
     * @param returnMsg 返回信息
     */
    void onSeniorMember(String returnMsg);


}
