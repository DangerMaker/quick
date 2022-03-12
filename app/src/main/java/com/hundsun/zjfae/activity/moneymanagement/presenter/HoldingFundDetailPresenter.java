package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.HoldingFundDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.PrdQueryTaUnitFundFinanceById;

/**
 * @Description:基金持仓详情(presenter)
 * @Author: zhoujianyu
 * @Time: 2019/2/22 14:02
 */
public class HoldingFundDetailPresenter extends BasePresenter<HoldingFundDetailView> {
    public HoldingFundDetailPresenter(HoldingFundDetailView baseView) {
        super(baseView);
    }

    //获取数据
    public void loadData(String id) {
        PrdQueryTaUnitFundFinanceById.REQ_PBIFE_prdquery_prdQueryTaUnitFundFinanceById.Builder builder = PrdQueryTaUnitFundFinanceById.REQ_PBIFE_prdquery_prdQueryTaUnitFundFinanceById.newBuilder();
        builder.setId(id);
        String url = parseUrl(MZJ, PBIFE, VIFEMZJ, ConstantName.PBIFE_prdquery_prdQueryTaUnitFundFinanceById, getRequestMap());
        addDisposable(apiServer.holdingFundDetailData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PrdQueryTaUnitFundFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFundFinanceById>(baseView) {
            @Override
            public void onSuccess(PrdQueryTaUnitFundFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFundFinanceById data) {
                baseView.getData(data);
            }

        });
    }

}
