package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.LoadProvinceView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.LoadCity;
import onight.zjfae.afront.gens.LoadProvince;
import onight.zjfae.afront.gens.LoadTmbBankInfo;

public class LoadProvincePresenter extends BasePresenter<LoadProvinceView> {
    public LoadProvincePresenter(LoadProvinceView baseView) {
        super(baseView);
    }


    //获取省份信息
    public void province(){
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.Province,getRequestMap());
        addDisposable(apiServer.province(url), new ProtoBufObserver<LoadProvince.Ret_PBIFE_chinacity_loadProvince>(baseView) {
            @Override
            public void onSuccess(LoadProvince.Ret_PBIFE_chinacity_loadProvince ret_pbife_chinacity_loadProvince) {
                baseView.provinceList(ret_pbife_chinacity_loadProvince);
            }
        });
    }

    //根据省份pno获取城市
    public void city(String pno){
        LoadCity.REQ_PBIFE_chinacity_loadCity.Builder city = LoadCity.REQ_PBIFE_chinacity_loadCity.newBuilder();
        city.setPno(pno);
        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.city,getRequestMap());

        addDisposable(apiServer.city(url, getBody(city.build().toByteArray())), new ProtoBufObserver< LoadCity.Ret_PBIFE_chinacity_loadCity>(baseView) {
            @Override
            public void onSuccess(LoadCity.Ret_PBIFE_chinacity_loadCity ret_pbife_chinacity_loadCity) {
                baseView.cityList(ret_pbife_chinacity_loadCity);
            }
        });
    }

    //根据省份，城市查询支行信息
    public void queryBranchName(String bankCode,String branchName,String pno,String cno){
        LoadTmbBankInfo.REQ_PBIFE_bankcardmanage_loadTmbBankInfo.Builder tmb = LoadTmbBankInfo.REQ_PBIFE_bankcardmanage_loadTmbBankInfo.newBuilder();
        tmb.setBankCode(bankCode);
        tmb.setBranchName(branchName);
        tmb.setPno(pno);
        tmb.setCno(cno);
        tmb.setPageSize("10");
        tmb.setPageIndex("1");

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.loadTmbBankInfo,getRequestMap());

        addDisposable(apiServer.queryBranchName(url, getBody(tmb.build().toByteArray())), new ProtoBufObserver<LoadTmbBankInfo.Ret_PBIFE_bankcardmanage_loadTmbBankInfo>(baseView) {
            @Override
            public void onSuccess(LoadTmbBankInfo.Ret_PBIFE_bankcardmanage_loadTmbBankInfo loadTmbBankInfo) {
                baseView.branchNameList(loadTmbBankInfo);
            }
        });

    }
}
