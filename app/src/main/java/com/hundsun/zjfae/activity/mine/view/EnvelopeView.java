package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.MyDiscount;
import onight.zjfae.afront.gens.Withdrawals;

public interface EnvelopeView extends BaseView {

    void onDictionaryBean( MyDiscount.Ret_PBIFE_kq_getMyDiscountPage discountPage);


    void withdraw(Withdrawals.Ret_PBIFE_kq_kqWithdrawals ret_pbife_kq_kqWithdrawals,String quanValue);
}
