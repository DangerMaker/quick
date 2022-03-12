package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.EnvelopeView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.MyDiscount;
import onight.zjfae.afront.gens.Withdrawals;

public class EnvelopePresenter extends BasePresenter<EnvelopeView> {
    public EnvelopePresenter(EnvelopeView baseView) {
        super(baseView);
    }



    public void queryDiscount(String kqType,String status,int pageIndex){

        MyDiscount.REQ_PBIFE_kq_getMyDiscountPage.Builder discount = MyDiscount.REQ_PBIFE_kq_getMyDiscountPage.newBuilder();
        discount.setKqType(kqType);
        discount.setPageIndex(pageIndex+"");
        discount.setPageSize("10");
        discount.setStatus(status);


        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.DiscountPage, getRequestMap());


        addDisposable(apiServer.queryDiscount(url,getBody(discount.build().toByteArray())), new ProtoBufObserver<MyDiscount.Ret_PBIFE_kq_getMyDiscountPage>(baseView) {
            @Override
            public void onSuccess(MyDiscount.Ret_PBIFE_kq_getMyDiscountPage ret_pbife_kq_getMyDiscountPage) {
                baseView.onDictionaryBean(ret_pbife_kq_getMyDiscountPage);
            }
        });

    }


    //红包提现
    //Withdrawals
    public void withdraw(String quanDetailsId,final String quanValue){
        Withdrawals.REQ_PBIFE_kq_kqWithdrawals.Builder withdraw = Withdrawals.REQ_PBIFE_kq_kqWithdrawals.newBuilder();

        withdraw.setQuanDetailsId(quanDetailsId);
        withdraw.setQuanValue(quanValue);
        withdraw.setQuanType("R");

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.Withdrawals,getRequestMap());

        addDisposable(apiServer.withdraw(url, getBody(withdraw.build().toByteArray())), new ProtoBufObserver<Withdrawals.Ret_PBIFE_kq_kqWithdrawals>(baseView) {
            @Override
            public void onSuccess(Withdrawals.Ret_PBIFE_kq_kqWithdrawals ret_pbife_kq_kqWithdrawals) {
                baseView.withdraw(ret_pbife_kq_kqWithdrawals, quanValue);
            }
        });
    }



}
