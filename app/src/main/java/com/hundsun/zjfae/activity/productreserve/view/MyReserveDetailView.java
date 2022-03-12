package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:产品预约模块列表详情（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface MyReserveDetailView extends BaseView {
    void getAllData(String oderType, PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails data0, ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail data1, Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo, UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    //失败原因
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor);

    //点击附件查询用户详细信息
    void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment);
}
