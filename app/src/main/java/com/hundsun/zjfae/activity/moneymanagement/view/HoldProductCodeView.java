package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.activity.moneymanagement.HoldProductBean;
import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public interface HoldProductCodeView extends BaseView {


    //立即购买
    void productCheckState(ProductSec.Ret_PBIFE_trade_subscribeProductSec ret_pbife_trade_subscribeProductSec);

    //聚合接口
    void getProductBean(HoldProductBean productBean);

    //失败原因
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor);


    //用户信息
    void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    //点击附件查询用户详细信息
    void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment);


}
