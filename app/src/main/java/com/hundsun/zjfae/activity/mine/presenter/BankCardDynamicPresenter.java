package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.BankCardDynamicView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.LoadTcBindingBankInfo;


public class BankCardDynamicPresenter extends BasePresenter<BankCardDynamicView> {
    public BankCardDynamicPresenter(BankCardDynamicView baseView) {
        super(baseView);
    }

    public void loadTcBindingBank(int pageIndex){
        LoadTcBindingBankInfo.REQ_PBIFE_bankcardmanage_loadTcBindingBankInfo.Builder loadBing
                = LoadTcBindingBankInfo.REQ_PBIFE_bankcardmanage_loadTcBindingBankInfo.newBuilder();
        loadBing.setPageIndex(String.valueOf(pageIndex));
        loadBing.setPageSize("10");
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.LoadTcBindingBankInfo,getRequestMap());

        addDisposable(apiServer.loadTcBindingBank(url,getBody(loadBing.build().toByteArray())), new ProtoBufObserver< LoadTcBindingBankInfo.Ret_PBIFE_bankcardmanage_loadTcBindingBankInfo>(baseView) {
            @Override
            public void onSuccess(LoadTcBindingBankInfo.Ret_PBIFE_bankcardmanage_loadTcBindingBankInfo ret_pbife_bankcardmanage_loadTcBindingBankInfo) {
                baseView.onBankCardManageBean(ret_pbife_bankcardmanage_loadTcBindingBankInfo);
            }
        });

    }

}
