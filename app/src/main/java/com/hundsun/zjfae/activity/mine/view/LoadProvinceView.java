package com.hundsun.zjfae.activity.mine.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gens.LoadCity;
import onight.zjfae.afront.gens.LoadProvince;
import onight.zjfae.afront.gens.LoadTmbBankInfo;

public interface LoadProvinceView extends BaseView {

     void provinceList(LoadProvince.Ret_PBIFE_chinacity_loadProvince province);

     void cityList(LoadCity.Ret_PBIFE_chinacity_loadCity loadCity);

     void branchNameList(LoadTmbBankInfo.Ret_PBIFE_bankcardmanage_loadTmbBankInfo loadTmbBankInfo);
}
