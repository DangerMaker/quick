package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.EntrustDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.DelegateCancelPb;

/**
 * @Description:委托详情（presenter）
 * @Author: yangtianren
 */
public class EntrustDetailPresenter extends BasePresenter<EntrustDetailView> {
    public EntrustDetailPresenter(EntrustDetailView baseView) {
        super(baseView);
    }

    //获取数据
    public void cancel(String delegationCode) {
        DelegateCancelPb.REQ_PBIFE_trade_delegateCancel.Builder builder = DelegateCancelPb.REQ_PBIFE_trade_delegateCancel.newBuilder();
        builder.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.DelegateCancel, getRequestMap());
        addDisposable(apiServer.cancel(url, getBody(builder.build().toByteArray())), new ProtoBufObserver< DelegateCancelPb.Ret_PBIFE_trade_delegateCancel>(baseView) {
            @Override
            public void onSuccess(DelegateCancelPb.Ret_PBIFE_trade_delegateCancel ret_pbife_trade_delegateCancel) {
                baseView.cancelEntrust(ret_pbife_trade_delegateCancel.getReturnCode(), ret_pbife_trade_delegateCancel.getReturnMsg());
            }
        });
    }
}
