package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.HoldingWaterView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.PrdQueryTaUnitFlowPb;

/**
 * @Description:持仓流水（presenter）
 * @Author: yangtianren
 */
public class HoldingWaterPresenter extends BasePresenter<HoldingWaterView> {
    public HoldingWaterPresenter(HoldingWaterView baseView) {
        super(baseView);
    }

    //获取数据
    public void loadData(String pageIndex, String startDate, String endDate) {
        PrdQueryTaUnitFlowPb.REQ_PBIFE_prdquery_prdQueryTaUnitFlow.Builder builder=PrdQueryTaUnitFlowPb.REQ_PBIFE_prdquery_prdQueryTaUnitFlow.newBuilder();
        builder.setPageIndex(pageIndex);
        builder.setPageSize("10");
        builder.setStartDate(startDate);
        builder.setEndDate(endDate);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryTaUnitFlow, getRequestMap());
        addDisposable(apiServer.holdLoadData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver< PrdQueryTaUnitFlowPb.Ret_PBIFE_prdquery_prdQueryTaUnitFlow>(baseView) {
            @Override
            public void onSuccess(PrdQueryTaUnitFlowPb.Ret_PBIFE_prdquery_prdQueryTaUnitFlow ret_pbife_prdquery_prdQueryTaUnitFlow) {
                baseView.getData(ret_pbife_prdquery_prdQueryTaUnitFlow.getData().getTaUnitFlowListList());
            }



        });
    }
}
