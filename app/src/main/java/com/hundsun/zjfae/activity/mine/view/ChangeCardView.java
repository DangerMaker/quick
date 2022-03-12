package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.CancelApplication;


public interface ChangeCardView extends BaseView {





    void onChangeCodeSmsCode();

    void onChangeBank(String returnCode,String returnMsg);


    void cancelChangeCard(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication cancelApplication);



}
