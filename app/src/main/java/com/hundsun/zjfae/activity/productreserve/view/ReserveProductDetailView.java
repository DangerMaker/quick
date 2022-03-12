package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.activity.productreserve.bean.ReserverProductBean;
import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v2.ReserveProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public  interface ReserveProductDetailView extends BaseView {



    //立即购买
    void productCheckState(ProductSec.Ret_PBIFE_trade_subscribeProductSec ret_pbife_trade_subscribeProductSec);

    //聚合接口
    void getProductBean(ReserverProductBean productBean);

    //失败原因
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body,String isRealInvestor);


    //用户信息
    void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);

    //产品详情
    void getProductDetail(ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails productDetails);

    //点击附件查询用户详细信息
    void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList,String riskLevelLabelName,String riskLevelLabelUrl,Boolean isAttachment);


    /**
     * 65周岁
     * */
    void onHighAge();


    /**
     * 高净值会员
     * @param returnMsg 返回信息
     * */
    void onSeniorMember(String returnMsg);

    /**
     * 合格投资者申请
     * @param returnMsg 返回信息
     * */
    void onQualifiedMember(String returnMsg);


    /**
     * 更新用户可购买金额
     * @param subscribeProductSec 更新数据
     * */
    void onUpDataUserPurchaseAmount(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec);

    /**
     * 风险测试
     * @param returnMsg 返回信息
     * */
    void onRiskAssessment(String returnMsg);

    /**
     * 新客专享购买提示
     * @param returnMsg 返回信息
     * */
    void onNovicePrompt(String returnMsg);

    /**
     *
     * 购买产品不可转让
     * @param returnMsg 返回信息
     *
     * */

    void onPlayNoTransfer(String returnMsg);
}
