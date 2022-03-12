package com.hundsun.zjfae.activity.splash;

import com.hundsun.zjfae.common.base.BaseView;

public interface StartView extends BaseView {

    @Override
    void showLoading();

    void onSuccess();
    void onError();

}
