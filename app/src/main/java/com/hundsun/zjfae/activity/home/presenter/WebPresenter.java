package com.hundsun.zjfae.activity.home.presenter;

import com.hundsun.zjfae.activity.home.view.WebView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public class WebPresenter extends BasePresenter<WebView> {
    public WebPresenter(WebView baseView) {
        super(baseView);
    }



    /**
     * @param isCertification 是否实名认证
     * */
    public void  onUserInfo(final boolean isCertification){
        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>() {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
                baseView.onUserInfo(userDetailInfo,isCertification);
            }
        });

    }


    /**
     * 合格投资者申请失败原因
     */
    public void requestInvestorStatus(final String isRealInvestor) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo, getRequestMap());
        UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.Builder builder =
                UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder();
        builder.setDynamicType1("highNetWorthUpload");

        addDisposable(apiServer.investorStatus(url, getBody(builder.build().toByteArray())), new BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo>(baseView) {
            @Override
            public void onSuccess(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body) {
                baseView.requestInvestorStatus(body, isRealInvestor);
            }
        });
    }
}
