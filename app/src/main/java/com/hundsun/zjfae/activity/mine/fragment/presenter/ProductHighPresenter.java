package com.hundsun.zjfae.activity.mine.fragment.presenter;

import com.hundsun.zjfae.activity.mine.fragment.view.ProductHighView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import io.reactivex.Observable;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.ProductHighTransferOrderList;

public class ProductHighPresenter extends BasePresenter<ProductHighView> {



    private long requestTime;
    private  AllAzjProto.PEARetControl productControl;

    private ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList orderList;

    public ProductHighPresenter(ProductHighView baseView) {
        super(baseView);
    }






    /******************************产品列表*******************************************/



    /**
     *获取产品列表
     *@param uuids 查询条件
     *@param pageIndex 查询页数
     * */

    public void initProduct(String uuids,String quanId,String quanCode,String quanSerCode, int pageIndex){


        long systemTime = System.currentTimeMillis();
        boolean isRequest = requestTime *(10 *60 *1000) -systemTime > 0;

        CCLog.e("吴波清quanId",quanId);
        CCLog.e("吴波清quanCode",quanCode);
        CCLog.e("吴波清quanSerCode",quanSerCode);
        if (isRequest){

            ProductHighTransferOrderList.REQ_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.Builder product = ProductHighTransferOrderList.REQ_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.newBuilder();

            product.setSpecialQryType("00");
            product.setPageIndex(String.valueOf(pageIndex));
            product.setPageSize("20");
            product.setUuids(uuids);
            product.setQuanDetailsId(quanId);
            product.setQuanUsedProductCode(quanCode);
            product.setQuanUsedSeriesCode(quanSerCode);
            String url = parseUrl(MZJ,PBIFE,VIFEMZJ, ConstantName.prdtransfer,getRequestMap());
            CCLog.e("请求链接",url);
            addDisposable(apiServer.productHighList(url, getBody(product.build().toByteArray())), new ProtoBufObserver<ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList>(baseView) {
                @Override
                public void onSuccess(ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList orderList) {

                    baseView.initProduct(orderList,productControl);
                }
            });
        }

        else {

            ProductHighTransferOrderList.REQ_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.Builder product = ProductHighTransferOrderList.REQ_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList.newBuilder();

            product.setSpecialQryType("00");
            product.setPageIndex(String.valueOf(pageIndex));
            product.setPageSize("20");
            product.setUuids(uuids);
            product.setQuanDetailsId(quanId);
            product.setQuanUsedProductCode(quanCode);
            product.setQuanUsedSeriesCode(quanSerCode);
            String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.prdtransfer,getRequestMap());

            CCLog.e("请求链接",url);
            String controlUrl = parseUrl(AZJ,PBCTL,VIFEMZJ,getRequestMap());
            AllAzjProto.PEAGetControl.Builder control =  AllAzjProto.PEAGetControl.newBuilder();
            control.setControlLocation("subscribeList");


            Observable observable = Observable.concat(apiServer.productHighList(url, getBody(product.build().toByteArray())),apiServer.subscribeProduct(controlUrl, getBody(control.build().toByteArray())));


            addDisposable(observable, new ProtoBufObserver(baseView) {
                @Override
                public void onSuccess(Object body) {
                    if (body instanceof  ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList){
                        //产品列表
                        orderList = (ProductHighTransferOrderList.Ret_PBIFE_prdtransferquery_prdQueryHighWorthSpecialTransferOrderList) body;
                    }
                    else if (body instanceof  AllAzjProto.PEARetControl){
                        //条件查询
                        requestTime = System.currentTimeMillis();
                        productControl = (AllAzjProto.PEARetControl) body;

                        baseView.initProduct(orderList,productControl);
                    }

                }
            });
        }



    }
}
