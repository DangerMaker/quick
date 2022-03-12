package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.productreserve.view.MyReserveView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.CancelOrderPB;
import onight.zjfae.afront.gens.CancelOrderPrePB;
import onight.zjfae.afront.gens.PBIFETradeQueryMyOrderList;

/**
 * @Description:我的预约预约模块（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class MyReservePresenter extends BasePresenter<MyReserveView> {

    public MyReservePresenter(MyReserveView baseView) {
        super(baseView);
    }

    /**
     * 获取我的预约列表数据
     */
    public void getReserveListData(int pageIndex) {
        PBIFETradeQueryMyOrderList.REQ_PBIFE_trade_queryMyOrderList.Builder builder = PBIFETradeQueryMyOrderList.REQ_PBIFE_trade_queryMyOrderList.newBuilder();
        builder.setPageIndex(pageIndex + "");
        builder.setPageSize("10");
        builder.setIsQuerySpvSpecial("1");

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.MyReserveOrderList, getRequestMap());
        addDisposable(apiServer.myReserveGetReserveListData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PBIFETradeQueryMyOrderList.Ret_PBIFE_trade_queryMyOrderList>(baseView) {
            @Override
            public void onSuccess(PBIFETradeQueryMyOrderList.Ret_PBIFE_trade_queryMyOrderList data) {
                List<PBIFETradeQueryMyOrderList.PBIFE_trade_queryMyOrderList.TaProductOrderDetailWrapList> list = new ArrayList<>(data.getData().getTaProductOrderDetailWrapListList());
                baseView.loadData(list);
            }


        });
    }

    /**
     * 取消预约预检查接口
     */
    public void cancelOrderPre(final String orderNum) {
        CancelOrderPrePB.REQ_PBIFE_trade_cancelOrderPre.Builder builder = CancelOrderPrePB.REQ_PBIFE_trade_cancelOrderPre.newBuilder();
        builder.setOrderNum(orderNum);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CancelOrderPre, getRequestMap());
        addDisposable(apiServer.cancelOrderPre(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<CancelOrderPrePB.Ret_PBIFE_trade_cancelOrderPre>(baseView) {
            @Override
            public void onSuccess(CancelOrderPrePB.Ret_PBIFE_trade_cancelOrderPre data) {
                baseView.cancleOrderRefreshPre(orderNum, data.getReturnCode(), data.getReturnMsg(), data.getData().getMsg());
            }

        });

    }

    /**
     * 取消预约接口
     */
    public void cancelOrder(String orderNum) {
        CancelOrderPB.REQ_PBIFE_trade_cancelOrder.Builder builder = CancelOrderPB.REQ_PBIFE_trade_cancelOrder.newBuilder();
        builder.setOrderNum(orderNum);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CancelOrder, getRequestMap());
        addDisposable(apiServer.cancelOrder(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<CancelOrderPB.Ret_PBIFE_trade_cancelOrder>(baseView) {
            @Override
            public void onSuccess(CancelOrderPB.Ret_PBIFE_trade_cancelOrder data) {
                baseView.cancleOrderRefresh(data.getReturnCode(), data.getReturnMsg(), data.getData().getMsg());
            }


        });

    }
}
