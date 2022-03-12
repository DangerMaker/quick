package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.PrdQuerySubscribeListPb;

/**
 * @Description:我的购买（View）
 * @Author: yangtianren
 */
public interface MySubscriptionView extends BaseView {
    void getData(List<PrdQuerySubscribeListPb.PBIFE_prdquery_prdQuerySubscribeList.SubscribeList> list);

    void cancelPre(String delegationCode, String code, String message,String data_message);

    void cancel(String code, String msg);
}
