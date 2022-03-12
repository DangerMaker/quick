package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.productreserve.view.ReserveListView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gens.ProductOrderValidate;
import onight.zjfae.afront.gensazj.ProductOrderInfoPB;

/**
 * @Description:产品预约模块 长期预约 短期预约（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class ReserveListPresenter extends BasePresenter<ReserveListView> {

    public ReserveListPresenter(ReserveListView baseView) {
        super(baseView);
    }

    /**
     * 获取长期或者短期列表数据
     */
    public void getReserveListData(int pageIndex, String orderType) {

        ProductOrderInfoPB.REQ_PBIFE_trade_queryProductOrderInfo.Builder builder = ProductOrderInfoPB.REQ_PBIFE_trade_queryProductOrderInfo.newBuilder();
        builder.setPageIndex(pageIndex + "");
        builder.setPageSize("10");
        builder.setOrderType(orderType);
        builder.setVersion("1.0.1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveList, getRequestMap());
        addDisposable(apiServer.reserveListGetReserveListData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<ProductOrderInfoPB.Ret_PBIFE_trade_queryProductOrderInfo>(baseView) {
            @Override
            public void onSuccess(ProductOrderInfoPB.Ret_PBIFE_trade_queryProductOrderInfo data) {
                List<ProductOrderInfoPB.PBIFE_trade_queryProductOrderInfo.TaProductOrderInfoWrapList> list = new ArrayList<>(data.getData().getTaProductOrderInfoWrapListList());
                baseView.loadData(list);
            }

        });
    }


    /**
     * 点击进入预约详情之前的请求接口判断
     */
    public void getReserveListValidate(final String orderProductCode) {
        ProductOrderValidate.REQ_PBIFE_trade_productOrderValidate.Builder builder = ProductOrderValidate.REQ_PBIFE_trade_productOrderValidate.newBuilder();
        builder.setOrderProductCode(orderProductCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListValidate, getRequestMap());
        addDisposable(apiServer.orderValidate(url, getBody(builder.build().toByteArray())), new BaseObserver<ProductOrderValidate.Ret_PBIFE_trade_productOrderValidate>(baseView) {
            @Override
            public void onSuccess(ProductOrderValidate.Ret_PBIFE_trade_productOrderValidate data) {
                String returnCode = data.getReturnCode();
                String returnMsg = data.getReturnMsg();
                if (returnCode.equals(ConstantCode.RESEVATION_CODE)){
                    baseView.showError(returnMsg);
                }
                else if (ConstantCode.RISK_ASSESSMENT_CODE .equals(returnCode) || ConstantCode.RISK_ASSESSMENT_CODE_1 .equals(returnCode) || ConstantCode.RISK_ASSESSMENT_CODE_2
                        .equals(returnCode)){
                    baseView.onRiskAssessment(returnMsg);
                }
                else if (returnCode.equals(ConstantCode.LOGIN_TIME_OUT)){
                    baseView.loginTimeOut(returnMsg);
                }
                else {
                    baseView.orderValidate(data.getReturnCode(), data.getReturnMsg(), orderProductCode);
                }



            }

        });
    }

}
