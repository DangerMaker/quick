package com.hundsun.zjfae.activity.myinvitation.view;

import com.hundsun.zjfae.common.base.BaseView;

import java.util.List;

import onight.zjfae.afront.gensazj.RecommendDetailsInfoPB;

/**
 * @Description:推荐明细（View）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:53
 */
public interface MyInvitationListView extends BaseView {
    void loadData(List<RecommendDetailsInfoPB.PBIFE_friendsrecommend_recommendDetailsInfo.RecommendDetailsList> list);

}
