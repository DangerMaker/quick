package com.hundsun.zjfae.activity.product.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gensazj.v2.BannerProto;

public interface ProductPlayStateView extends BaseView {


    void banner(List<BannerProto.PBAPP_ads.Ads> adsList);
}
