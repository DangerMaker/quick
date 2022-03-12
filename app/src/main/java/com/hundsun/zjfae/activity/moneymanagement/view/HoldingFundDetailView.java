package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.PrdQueryTaUnitFundFinanceById;

/**
 * @Description:基金持仓详情（View）
 * @Author: yangtianren
 */
public interface HoldingFundDetailView extends BaseView {
    void getData(PrdQueryTaUnitFundFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFundFinanceById data);
}
