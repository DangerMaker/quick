package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gensazj.ProductOrderInfoPB;

/**
 * @Description:长期预约 短期预约（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface ReserveListView extends BaseView {

    void loadData(List<ProductOrderInfoPB.PBIFE_trade_queryProductOrderInfo.TaProductOrderInfoWrapList> list);

    void orderValidate(String returnCode, String returnMsg, String orderProductCode);





    /**
     * 风险测试
     * @param returnMsg 返回信息
     * */
    void onRiskAssessment(String returnMsg);
}
