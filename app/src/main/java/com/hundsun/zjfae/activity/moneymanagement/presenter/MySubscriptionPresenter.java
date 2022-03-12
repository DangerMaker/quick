package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.MySubscriptionView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.PrdQuerySubscribeListPb;
import onight.zjfae.afront.gens.SubscribeCancelPb;
import onight.zjfae.afront.gens.SubscribeCancelPrePb;

/**
 * @Description:我的购买（presenter）
 * @Author: yangtianren
 */
public class MySubscriptionPresenter extends BasePresenter<MySubscriptionView> {
    public MySubscriptionPresenter(MySubscriptionView baseView) {
        super(baseView);
    }

    //获取数据
    public void loadData(String pageIndex) {
        PrdQuerySubscribeListPb.REQ_PBIFE_prdquery_prdQuerySubscribeList.Builder builder = PrdQuerySubscribeListPb.REQ_PBIFE_prdquery_prdQuerySubscribeList.newBuilder();
        builder.setPageIndex(pageIndex);
        builder.setPageSize("10");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeList, getRequestMap());
        addDisposable(apiServer.loadData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PrdQuerySubscribeListPb.Ret_PBIFE_prdquery_prdQuerySubscribeList>(baseView) {
            @Override
            public void onSuccess(PrdQuerySubscribeListPb.Ret_PBIFE_prdquery_prdQuerySubscribeList ret_pbife_prdquery_prdQuerySubscribeList) {
                baseView.getData(ret_pbife_prdquery_prdQuerySubscribeList.getData().getSubscribeListList());
            }
        });
    }

    //撤单预检测
    public void cancelPre(String productCode, final String delegationCode) {
        SubscribeCancelPrePb.REQ_PBIFE_trade_subscribeCancelPre.Builder builder = SubscribeCancelPrePb.REQ_PBIFE_trade_subscribeCancelPre.newBuilder();
        builder.setProductCode(productCode);
        builder.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeCancelPre, getRequestMap());
        addDisposable(apiServer.cancelPre(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SubscribeCancelPrePb.Ret_PBIFE_trade_subscribeCancelPre>(baseView) {
            @Override
            public void onSuccess(SubscribeCancelPrePb.Ret_PBIFE_trade_subscribeCancelPre ret_pbife_trade_subscribeCancelPre) {
                baseView.cancelPre(delegationCode, ret_pbife_trade_subscribeCancelPre.getReturnCode(),ret_pbife_trade_subscribeCancelPre.getReturnMsg(), ret_pbife_trade_subscribeCancelPre.getData().getMsg());
            }
        });
    }

    //撤单
    public void cancel(String delegationCode) {
        SubscribeCancelPb.REQ_PBIFE_trade_subscribeCancel.Builder builder = SubscribeCancelPb.REQ_PBIFE_trade_subscribeCancel.newBuilder();
        builder.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeCancel, getRequestMap());
        addDisposable(apiServer.cancelPlay(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SubscribeCancelPb.Ret_PBIFE_trade_subscribeCancel>(baseView) {
            @Override
            public void onSuccess(SubscribeCancelPb.Ret_PBIFE_trade_subscribeCancel ret_pbife_trade_subscribeCancel) {
                baseView.cancel(ret_pbife_trade_subscribeCancel.getReturnCode(), ret_pbife_trade_subscribeCancel.getReturnMsg());
            }
        });
    }
}
