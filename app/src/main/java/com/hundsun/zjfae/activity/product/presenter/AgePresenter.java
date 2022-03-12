package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;

public class AgePresenter extends BasePresenter<BaseView> {


    public AgePresenter(BaseView baseView) {
        super(baseView);
    }

    //65周岁提交
    public void ageRequest(){

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.AgeReminder);

        addDisposable(url, new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {

            }
        });
    }
}
