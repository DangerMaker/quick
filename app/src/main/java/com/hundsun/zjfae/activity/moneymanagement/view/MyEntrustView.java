package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.PrdQueryTcDelegationFinanceListPb;

/**
 * @Description:我的委托（View）
 * @Author: yangtianren
 */
public interface MyEntrustView extends BaseView {
    void getData(List<PrdQueryTcDelegationFinanceListPb.PBIFE_prdquery_prdQueryTcDelegationFinanceList.TcDelegationFinaceList> list);

    //撤单请求返回
    void cancelEntrust(String code, String msg);
}
