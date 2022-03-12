package com.hundsun.zjfae.activity.mine.fragment.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.ProductHighTransferOrderList;

public interface TransferOrderHighView extends BaseView {



    void initProduct(ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList orderList, AllAzjProto.PEARetControl productControl);

}
