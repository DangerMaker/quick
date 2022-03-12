package com.hundsun.zjfae.activity.moneymanagement.view;

import android.content.DialogInterface;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.PBIFEPrdqueryPrdQueryTaUnitFinanceById;
import onight.zjfae.afront.gens.v2.QueryTransferSellProfitsPB;

/**
 * @Description:我要卖（View）
 * @Author: yangtianren
 */
public interface SellView extends BaseView {
    void getDetail(PBIFEPrdqueryPrdQueryTaUnitFinanceById.PBIFE_prdquery_prdQueryTaUnitFinanceById data, String keyCode);

    void productAttachment(Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachmentInfo);

    /**
     * 挂单利率
     * @param profits 挂单利率详情
     * @param isEstimatedAmount 是否修改测算金额
     * */
    void sellProfits(QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits,boolean isEstimatedAmount);

    /**
     * 挂单利率不符
     * @param profits 挂单利率详情
     * @param returnMsg 返回信息
     * */
    void onSellProfits(QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits,String returnMsg);

    /**
     * 挂单利率不符
     * @param profits 挂单利率详情
     * @param returnMsg 返回信息
     * @param isEstimatedAmount 是否修改测算金额
     * */
    void onSellProfitsStatus(QueryTransferSellProfitsPB.PBIFE_trade_queryTransferSellProfits profits,String returnMsg,boolean isEstimatedAmount);

    /**
      * 发送短信验证码回调
      * @date: 2020/6/24 11:02
      * @author: moran
      * @param msg 发送短信验证码提醒
      */
    void onSmsCodeStatusSuccess(String msg);

     /**
      * 验证码是否通过
      * @date: 2020/6/24 15:34
      * @author: moran
      * @param isVerify 验证码是否校验通过
      * @param msg 短信校验失败提示
      * @param dialog 验证成功，弹框消失
      * @return
      */
    void onSmsCodeVerifyStatus(boolean isVerify,String msg, DialogInterface dialog);
}
