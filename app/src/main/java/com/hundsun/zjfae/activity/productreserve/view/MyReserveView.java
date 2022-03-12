package com.hundsun.zjfae.activity.productreserve.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gens.PBIFETradeQueryMyOrderList;


/**
 * @Description:产品预约模块列表（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface MyReserveView extends BaseView {
    void loadData(List<PBIFETradeQueryMyOrderList.PBIFE_trade_queryMyOrderList.TaProductOrderDetailWrapList> list);

    void cancleOrderRefreshPre(String orderNum, String code, String message,String datamessage);

    void cancleOrderRefresh(String code, String message,String datamessage);
}
