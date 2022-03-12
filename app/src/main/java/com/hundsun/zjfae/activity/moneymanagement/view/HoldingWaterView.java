package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.PrdQueryTaUnitFlowPb;

/**
 * @Description:持仓流水（View）
 * @Author: yangtianren
 */
public interface HoldingWaterView extends BaseView {
    void getData(List<PrdQueryTaUnitFlowPb.PBIFE_prdquery_prdQueryTaUnitFlow.TaUnitFlow> list);
}
