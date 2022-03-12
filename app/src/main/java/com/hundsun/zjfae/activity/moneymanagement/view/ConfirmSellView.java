package com.hundsun.zjfae.activity.moneymanagement.view;

import com.hundsun.zjfae.common.base.BaseView;

/**
 * @Description:我要卖（View）
 * @Author: yangtianren
 */
public interface ConfirmSellView extends BaseView {
    void CreateTransferOrder(String code, String msg);


    void onTransferRemarks(String remakes);
}
