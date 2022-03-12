package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.ChooseBankView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.QueryBankInfo;

public class ChooseBankPresenter extends BasePresenter<ChooseBankView> {
    public ChooseBankPresenter(ChooseBankView baseView) {
        super(baseView);
    }



    //查询银行卡渠道
    public void getCheckBankType(){
        String url = parseUrl(MZJ,PBIFE,VIFEMZJ, ConstantName.QueryBankInfo,getRequestMap());
        QueryBankInfo.REQ_PBIFE_bankcardmanage_queryBankInfo.Builder query =   QueryBankInfo.REQ_PBIFE_bankcardmanage_queryBankInfo.newBuilder();
        query.setBankCardNo("6");
        addDisposable(apiServer.getCheckBankType(url, getBody(query.build().toByteArray())), new ProtoBufObserver<QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo>(baseView) {
            @Override
            public void onSuccess(QueryBankInfo.Ret_PBIFE_bankcardmanage_queryBankInfo ret_pbife_bankcardmanage_queryBankInfo) {
                //List<QueryBankInfo.PBIFE_bankcardmanage_queryBankInfo.TcBankDitchList> lists = ret_pbife_bankcardmanage_queryBankInfo.getData().getTcBankDitchListList();
                baseView.onBindingBankBean(ret_pbife_bankcardmanage_queryBankInfo);
            }

        });

    }

}
