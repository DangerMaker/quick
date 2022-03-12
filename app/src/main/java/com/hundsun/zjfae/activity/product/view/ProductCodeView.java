package com.hundsun.zjfae.activity.product.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @author moran
 * 产品详情View
 * **/
public interface ProductCodeView extends BaseView {


    /**
     * 立即购买
     * @param ret_pbife_trade_subscribeProductSec 立即购买
     * */
    void productCheckState(ProductSec.Ret_PBIFE_trade_subscribeProductSec ret_pbife_trade_subscribeProductSec);

    /**
     * 失败原因
     * @param body 合格投资者失败原因
     * @param isRealInvestor 是否是合格投资者
     * */
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor);


    /**
     * 立即购买获取用户信息
     * @param userDetailInfo 用户详细信息
     * */
    void getUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);


    /**
     * 点击附件查询用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     * @param attachmentList 附件列表
     * @param riskLevelLabelName 附加文件名称
     * @param riskLevelLabelUrl 附件加载Url
     * @param isAttachment 附件还是信用评级
     * **/
    void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, boolean isAttachment);




    /**
     * 产品详情
     * @param productDetails 产品详情信息
     * @param requestTime 请求时间差
     * **/

    void onProductData(PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails productDetails, long requestTime);



    /**
     * 用户详细信息
     * @param userDetailInfo 用户详细信息
     * @param accreditedBuyIs 是否合格投资者购买
     * **/

    void onUserDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo,String accreditedBuyIs);



    /**
     * 附件详细信息
     * @param attachmentList 附件列表信息
     * @param accreditedBuyIs 是否合格投资者购买
     * **/
    void onAttachmentInfo(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentList,String accreditedBuyIs);



    /**
     * 65周岁
     * */
    void onHighAge();




    /**
     * 新客专享购买提示
     * @param returnMsg 返回信息
     * */
    void onNovicePrompt(String returnMsg);


    /**
     * 更新用户可购买金额
     * @param subscribeProductSec
     * */
    void onUpDataUserPurchaseAmount(ProductSec.Ret_PBIFE_trade_subscribeProductSec subscribeProductSec);


    /**
     * 合格投资者申请
     * @param returnMsg 返回信息
     * */
    void onQualifiedMember(String returnMsg);


    /**
     *
     * 购买产品不可转让
     * @param returnMsg 返回信息
     * */

    void onPlayNoTransfer(String returnMsg);


    /**
     * 风险测试
     * @param returnMsg 返回信息
     * */
    void onRiskAssessment(String returnMsg);

    /**
     * 高净值会员
     * @param returnMsg 返回信息
     * */
    void onSeniorMember(String returnMsg);





}
