package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.google.gson.Gson;

import com.hundsun.zjfae.activity.moneymanagement.view.ConfirmSellView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.List;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.CreateTransferOrderPB;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @Description:我要卖（presenter）
 * @Author: yangtianren
 */
public class ConfirmSellPresenter extends BasePresenter<ConfirmSellView> {
    public ConfirmSellPresenter(ConfirmSellView baseView) {
        super(baseView);
    }

    //获取数据
    public void getDate(String productCode, String delegateNum, String lilv, String password,String unitId) {
        CreateTransferOrderPB.REQ_PBIFE_trade_createTransferOrder.Builder builder = CreateTransferOrderPB.REQ_PBIFE_trade_createTransferOrder.newBuilder();
        builder.setProductCode(productCode);
        builder.setDelegateNum(delegateNum);
        builder.setActualRate(lilv);
        builder.setPassword(password);
        builder.setUnitId(unitId);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CreateTransferOrder);
        addDisposable(apiServer.getDate(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<CreateTransferOrderPB.Ret_PBIFE_trade_createTransferOrder>(baseView) {
            @Override
            public void onSuccess(CreateTransferOrderPB.Ret_PBIFE_trade_createTransferOrder ret_pbife_trade_createTransferOrder) {
                baseView.CreateTransferOrder(ret_pbife_trade_createTransferOrder.getReturnCode(), ret_pbife_trade_createTransferOrder.getReturnMsg());
            }

        });
    }



    public void getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());

        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("transfer.remarks");

        addDisposable(apiServer.getDictionary(url, getBody(diction.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>() {
            @Override
            public void onSuccess(Dictionary.Ret_PBAPP_dictionary dictionary) {

                List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();

                baseView.onTransferRemarks(parms.get(0).getKeyCode());

            }
        });
    }
}
