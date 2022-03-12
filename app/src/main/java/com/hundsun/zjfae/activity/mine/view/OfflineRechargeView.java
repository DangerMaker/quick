package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.v2.Notices;

public interface OfflineRechargeView extends BaseView {

    void onOfflineRecharge(Notices.Ret_PBAPP_notice notice);

    void onDictionary(Dictionary.Ret_PBAPP_dictionary dictionary);
}
