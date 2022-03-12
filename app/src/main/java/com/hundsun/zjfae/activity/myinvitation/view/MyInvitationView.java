package com.hundsun.zjfae.activity.myinvitation.view;

import com.hundsun.zjfae.common.base.BaseView;

import onight.zjfae.afront.gensazj.UserRecommendInfoPB;
import onight.zjfae.afront.gensazj.v2.BannerProto;

/**
 * @Description:我的邀请（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface MyInvitationView extends BaseView {
    void loadData(UserRecommendInfoPB.PBIFE_friendsrecommend_userRecommendInfo bean);
    //获取二维码的前缀地址
    void getFuncURL(BannerProto.Ret_PBAPP_ads ret_pbapp_ads);

}
