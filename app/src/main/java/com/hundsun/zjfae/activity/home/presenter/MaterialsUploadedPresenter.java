package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.view.MaterialsUploadedView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gensazj.DictDynamics;

public class MaterialsUploadedPresenter extends BasePresenter<MaterialsUploadedView> {

    private UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo ret_pbife_fund_loadUserAssetsInfo;

    private DictDynamics.Ret_PBAPP_dictDynamic ret_pbapp_dictDynamic;

    public MaterialsUploadedPresenter(MaterialsUploadedView baseView) {
        super(baseView);
    }



    public void init(){

        Observable observable = Observable.concat(getData(),dictDynamic());


        addDisposable(observable, new ProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo){
                    ret_pbife_fund_loadUserAssetsInfo = (UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo) o;
                }
                else if (o instanceof DictDynamics.Ret_PBAPP_dictDynamic){

                    ret_pbapp_dictDynamic = (DictDynamics.Ret_PBAPP_dictDynamic) o;
                    baseView.init(ret_pbife_fund_loadUserAssetsInfo,ret_pbapp_dictDynamic);
                }
            }
        });

    }

    private Observable getData() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TotalIncome, getRequestMap());

        return  apiServer.getData(url);
//        addDisposable(apiServer.getData(url), new ProtoBufObserver<UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo>(baseView) {
//            @Override
//            public void onSuccess(UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo ret_pbife_fund_loadUserAssetsInfo) {
//                baseView.loadData(ret_pbife_fund_loadUserAssetsInfo.getData().getAmount(),ret_pbife_fund_loadUserAssetsInfo.getData().getAccreditedDiff());
//            }
//        });
    }

    private Observable dictDynamic(){
        DictDynamics.REQ_PBAPP_dictDynamic.Builder dict = DictDynamics.REQ_PBAPP_dictDynamic.newBuilder();
        dict.setDynamicType1("highNetWorthUpload");
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dynamic,getRequestMap());

        return  apiServer.dictDynamic(url, getBody(dict.build().toByteArray()));
//        addDisposable(apiServer.dictDynamic(url, getBody(dict.build().toByteArray())), new ProtoBufObserver< DictDynamics.Ret_PBAPP_dictDynamic>(baseView) {
//            @Override
//            public void onSuccess(DictDynamics.Ret_PBAPP_dictDynamic ret_pbapp_dictDynamic) {
//                baseView.dictData(ret_pbapp_dictDynamic);
//            }
//        });
    }
}
