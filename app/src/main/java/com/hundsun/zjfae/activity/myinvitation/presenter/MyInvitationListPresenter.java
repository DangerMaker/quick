package com.hundsun.zjfae.activity.myinvitation.presenter;

import com.hundsun.zjfae.activity.myinvitation.view.MyInvitationListView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.RecommendDetailsInfoPB;

/**
 * @Description:推荐明细（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class MyInvitationListPresenter extends BasePresenter<MyInvitationListView> {

    public MyInvitationListPresenter(MyInvitationListView baseView) {
        super(baseView);
    }

    /**
     * 我的邀请界面
     */
    public void getMyInvitationListData(int pageIndex, String startDate, String endDate, String frmobile) {
        RecommendDetailsInfoPB.REQ_PBIFE_friendsrecommend_recommendDetailsInfo.Builder builder = RecommendDetailsInfoPB.REQ_PBIFE_friendsrecommend_recommendDetailsInfo.newBuilder();
        builder.setPageIndex(pageIndex + "");
        builder.setPageSize("10");
        builder.setStartDate(startDate);
        builder.setEndDate(endDate);
        builder.setFrmobile(frmobile);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.MyInvitationList, getRequestMap());
        addDisposable(apiServer.getMyInvitationListData(url,getBody(builder.build().toByteArray())), new ProtoBufObserver<RecommendDetailsInfoPB.Ret_PBIFE_friendsrecommend_recommendDetailsInfo>(baseView) {
            @Override
            public void onSuccess(RecommendDetailsInfoPB.Ret_PBIFE_friendsrecommend_recommendDetailsInfo data) {
                List<RecommendDetailsInfoPB.PBIFE_friendsrecommend_recommendDetailsInfo.RecommendDetailsList> list = new ArrayList<>(data.getData().getRecommendDetailsListList());
                baseView.loadData(list);
            }


        });
    }
}
