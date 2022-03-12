package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:购买详情（View）
 * @Author: yangtianren
 */
public interface SubscriptionDetailView extends BaseView {
    void setDetail(PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails product);

    //失败原因
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body, String isRealInvestor);
    //点击附件查询用户详细信息
    void getAttachmentUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo, String accreditedBuyIs, Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, String riskLevelLabelName, String riskLevelLabelUrl, Boolean isAttachment);




    /**
     * 撤单预检查接口请求回调
     * @method
     * @date: 2020/11/1 15:11
     * @author: moran
     * @param code 请求返回code码
     * @param message 错误提示
     * @param data_message 弹框详情
     * @return
     */
    void cancelPre( String code, String message,String data_message);

    /**
     * 撤单接口请求回调
     * @method
     * @date: 2020/11/1 15:14
     * @author: moran
     * @param code 请求返回code码
     * @param msg 请求返回提示
     * @return
     */
    void cancel(String code, String msg);
}
