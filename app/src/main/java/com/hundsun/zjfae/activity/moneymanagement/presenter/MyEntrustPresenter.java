package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.MyEntrustView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.DelegateCancelPb;
import onight.zjfae.afront.gens.PrdQueryTcDelegationFinanceListPb;

/**
 * @Description:我的委托（presenter）
 * @Author: yangtianren
 */
public class MyEntrustPresenter extends BasePresenter<MyEntrustView> {
    public MyEntrustPresenter(MyEntrustView baseView) {
        super(baseView);
    }

    //获取数据
    public void loadData(String pageIndex, String startDate, String endDate) {
        PrdQueryTcDelegationFinanceListPb.REQ_PBIFE_prdquery_prdQueryTcDelegationFinanceList.Builder builder = PrdQueryTcDelegationFinanceListPb.REQ_PBIFE_prdquery_prdQueryTcDelegationFinanceList.newBuilder();
        builder.setPageIndex(pageIndex);
        builder.setPageSize("10");
        builder.setStartDate(startDate);
        builder.setEndDate(endDate);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryTcDelegationFinanceList, getRequestMap());
        addDisposable(apiServer.myEntrustLoadData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PrdQueryTcDelegationFinanceListPb.Ret_PBIFE_prdquery_prdQueryTcDelegationFinanceList>(baseView) {
            @Override
            public void onSuccess(PrdQueryTcDelegationFinanceListPb.Ret_PBIFE_prdquery_prdQueryTcDelegationFinanceList ret_pbife_prdquery_prdQueryTcDelegationFinanceList) {
                baseView.getData(ret_pbife_prdquery_prdQueryTcDelegationFinanceList.getData().getTcDelegationFinaceListList());
            }



        });
    }

    //撤单按钮点击
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
