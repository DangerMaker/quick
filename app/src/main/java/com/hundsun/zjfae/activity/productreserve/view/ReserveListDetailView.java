package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.productreserve.view
 * @ClassName:      ReserveListDetailView
 * @Description:     长期预约 短期预约详情（View）
 * @Author:         moran
 * @CreateDate:     2019/6/14 19:50
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/14 19:50
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public interface ReserveListDetailView extends BaseView {

    /**
     * 预约第一步检查预约金额结果
     * @param commit 是否确认提交
     * @param orderProductCode 产品code码
     * */
    void reserveProductPre(boolean commit, String orderProductCode);



    /**
     * 预约接口
     * @param returnCode 返回Code码
     * @param returnMsg 返回提示信息
     * @param msg 预约提示信息
     * */
    void reserveProduct(String returnCode, String returnMsg, String msg);


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
     * 获取长期预约或短期预约产品详情
     * @param productOrderInfoDetail 长期预约或短期预约产品详情
     * */
    void onProductOrderInfo(ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail productOrderInfoDetail);

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
     * 合格投资者申请
     * @param returnMsg 返回信息
     * */
    void onQualifiedMember(String returnMsg);


    /**
     * 新客专享购买提示
     * @param returnMsg 返回信息
     * */
    void onNovicePrompt(String returnMsg);


    /**
     * 风险测试
     * @param returnMsg 返回信息
     * */
    void onRiskAssessment(String returnMsg);
    /**
     *
     * 购买产品不可转让
     * @param returnMsg 返回信息
     * */
    void onPlayNoTransfer(String returnMsg);

    /**
     * 高净值会员
     * @param returnMsg 返回信息
     * */
    void onSeniorMember(String returnMsg);
}
