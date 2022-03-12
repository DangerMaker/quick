package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.PrdQueryInterestPayDetailsPb;

/**
 * @Description:付息明细（View）
 * @Author: yangtianren
 */
public interface PayInterestView extends BaseView {
    void getData(String totalIncome, List<PrdQueryInterestPayDetailsPb.PBIFE_prdquery_prdQueryInterestPayDetails.ProductCashAddInterestPay> list);
}
