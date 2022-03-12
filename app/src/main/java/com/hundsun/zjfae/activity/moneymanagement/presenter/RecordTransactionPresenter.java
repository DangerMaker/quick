package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.RecordTransactionView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.PBIFEPrdqueryQueryUnifyPurchaseTradeList;
import onight.zjfae.afront.gens.SubscribeCancelPb;
import onight.zjfae.afront.gens.SubscribeCancelPrePb;

/**
 * @Description:交易记录（presenter）
 * @Author: yangtianren
 */
public class RecordTransactionPresenter extends BasePresenter<RecordTransactionView> {
    public RecordTransactionPresenter(RecordTransactionView baseView) {
        super(baseView);
    }

    //获取数据
    public void  loadData(int pageIndex) {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PurchaseTradeList);


        PBIFEPrdqueryQueryUnifyPurchaseTradeList.REQ_PBIFE_prdquery_queryUnifyPurchaseTradeList.Builder builder = PBIFEPrdqueryQueryUnifyPurchaseTradeList.REQ_PBIFE_prdquery_queryUnifyPurchaseTradeList.newBuilder();
        builder.setPageIndex(String.valueOf(pageIndex));
        builder.setPageSize("10");
        addDisposable(apiServer.getTransactionRecord(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PBIFEPrdqueryQueryUnifyPurchaseTradeList.Ret_PBIFE_prdquery_queryUnifyPurchaseTradeList>(baseView) {
            @Override
            public void onSuccess(PBIFEPrdqueryQueryUnifyPurchaseTradeList.Ret_PBIFE_prdquery_queryUnifyPurchaseTradeList tradeList) {

                baseView.getData(tradeList.getData().getMyTradeObjectsList());
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
