package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.PayInterestView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.PrdQueryInterestPayDetailsPb;

/**
 * @Description:付息明细（presenter）
 * @Author: yangtianren
 */
public class PayInterestPresenter extends BasePresenter<PayInterestView> {
    public PayInterestPresenter(PayInterestView baseView) {
        super(baseView);
    }

    //获取数据
    public void loadData(String id) {
        PrdQueryInterestPayDetailsPb.REQ_PBIFE_prdquery_prdQueryInterestPayDetails.Builder builder = PrdQueryInterestPayDetailsPb.REQ_PBIFE_prdquery_prdQueryInterestPayDetails.newBuilder();
        builder.setId(id);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryInterestPayDetails, getRequestMap());
        addDisposable(apiServer.payInterestLoadData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PrdQueryInterestPayDetailsPb.Ret_PBIFE_prdquery_prdQueryInterestPayDetails>(baseView) {
            @Override
            public void onSuccess(PrdQueryInterestPayDetailsPb.Ret_PBIFE_prdquery_prdQueryInterestPayDetails data) {
                baseView.getData(data.getData().getTotalIncome(), data.getData().getProductCashAddInterestPayListList());
            }



        });
    }
}
