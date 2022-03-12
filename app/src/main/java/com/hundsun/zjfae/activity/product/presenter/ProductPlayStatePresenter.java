package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.activity.product.view.ProductPlayStateView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gensazj.v2.BannerProto;

public class ProductPlayStatePresenter extends BasePresenter<ProductPlayStateView> {

    public ProductPlayStatePresenter(ProductPlayStateView baseView) {
        super(baseView);
    }


    /**
     * banner请求
     * show_type = 4;购买banner图
     */
    public void requestBanner(String show_type) {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.BANNER_PBNAME,getRequestMap());

        BannerProto.REQ_PBAPP_ads.Builder bannerRequest = BannerProto.REQ_PBAPP_ads.newBuilder();
        bannerRequest.setShowType(show_type.trim());


        addDisposable(apiServer.homeBanner(url, getBody(bannerRequest.build().toByteArray())), new ProtoBufObserver<BannerProto.Ret_PBAPP_ads>(baseView) {
            @Override
            public void onSuccess(BannerProto.Ret_PBAPP_ads ret_pbapp_ads) {

                if (ret_pbapp_ads.getReturnCode().equals("0000")){
                    baseView.banner(ret_pbapp_ads.getData().getAdsList());
                }
                else {
                    baseView.showError(ret_pbapp_ads.getReturnMsg());
                }


            }
        });

    }
}
