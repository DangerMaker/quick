package com.hundsun.zjfae.activity.myinvitation.presenter;

import com.hundsun.zjfae.activity.myinvitation.view.MyInvitationView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import onight.zjfae.afront.gensazj.UserRecommendInfoPB;
import onight.zjfae.afront.gensazj.v2.BannerProto;

/**
 * @Description:我的邀请（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class MyInvitationPresenter extends BasePresenter<MyInvitationView> {

    public MyInvitationPresenter(MyInvitationView baseView) {
        super(baseView);
    }

    /**
     * 我的邀请界面
     */
    public void getMyInvitationData() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.MyInvitation, getRequestMap());
        addDisposable(apiServer.getMyInvitationData(url), new ProtoBufObserver<UserRecommendInfoPB.Ret_PBIFE_friendsrecommend_userRecommendInfo>(baseView) {
            @Override
            public void onSuccess(UserRecommendInfoPB.Ret_PBIFE_friendsrecommend_userRecommendInfo data) {
                baseView.loadData(data.getData());
            }


        });
    }

    /**
     * 获取图片
     */
    public void getFuncURL() {
        BannerProto.REQ_PBAPP_ads.Builder builder = BannerProto.REQ_PBAPP_ads.newBuilder();
        builder.setShowType("6");
        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.BANNER_PBNAME, map);
        addDisposable(apiServer.homeBanner(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<BannerProto.Ret_PBAPP_ads>(baseView) {
            @Override
            public void onSuccess(BannerProto.Ret_PBAPP_ads ret_pbapp_ads) {
                baseView.getFuncURL(ret_pbapp_ads);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                baseView.getFuncURL(null);
            }
        });
    }
}
