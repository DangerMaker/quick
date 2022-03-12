package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.view.SubscriptionDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.SubscribeCancelPb;
import onight.zjfae.afront.gens.SubscribeCancelPrePb;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:购买详情（presenter）
 * @Author: yangtianren
 */
public class SubscriptionDetailPresenter extends BasePresenter<SubscriptionDetailView> {

    private String productCode;
    private String delegationCode;

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public void setDelegationCode(String delegationCode) {
        this.delegationCode = delegationCode;
    }

    public SubscriptionDetailPresenter(SubscriptionDetailView baseView) {
        super(baseView);
    }

    public void getProductDetail() {
        PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.Builder product = PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.newBuilder();
        product.setProductCode(productCode);
        product.setVersion("1.0.1");
        product.setDifferent("08");
        Map<String, String> map = getRequestMap();
        map.put("version", version);
        map.put("isNewInterface","1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryProductDetails, map);


        addDisposable(apiServer.productCodeInfo(url, getBody(product.build().toByteArray())), new ProtoBufObserver<PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails>(baseView) {
            @Override
            public void onSuccess(PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails ret_pbife_prdquery_prdQueryProductDetails) {
                baseView.setDetail(ret_pbife_prdquery_prdQueryProductDetails);
            }

        });
    }

    /**
     * 失败原因
     */
    public void requestInvestorStatus(final String isRealInvestor) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo);
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

    //点击附件查询用户详细信息
    public void attachmentUserInfo(final String accreditedBuyIs, final Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, final String riskLevelLabelName, final String riskLevelLabelUrl, final Boolean isAttachment) {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.getAttachmentUserInfo(userDetailInfo, accreditedBuyIs, attachmentList, riskLevelLabelName, riskLevelLabelUrl, isAttachment);
            }
        });
    }


    //撤单预检测
    public void cancelPre() {
        SubscribeCancelPrePb.REQ_PBIFE_trade_subscribeCancelPre.Builder builder = SubscribeCancelPrePb.REQ_PBIFE_trade_subscribeCancelPre.newBuilder();
        builder.setProductCode(productCode);
        builder.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeCancelPre, getRequestMap());
        addDisposable(apiServer.cancelPre(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SubscribeCancelPrePb.Ret_PBIFE_trade_subscribeCancelPre>(baseView) {
            @Override
            public void onSuccess(SubscribeCancelPrePb.Ret_PBIFE_trade_subscribeCancelPre ret_pbife_trade_subscribeCancelPre) {
                baseView.cancelPre(ret_pbife_trade_subscribeCancelPre.getReturnCode(),ret_pbife_trade_subscribeCancelPre.getReturnMsg(), ret_pbife_trade_subscribeCancelPre.getData().getMsg());
            }
        });
    }

    //撤单
    public void cancel() {
        SubscribeCancelPb.REQ_PBIFE_trade_subscribeCancel.Builder builder = SubscribeCancelPb.REQ_PBIFE_trade_subscribeCancel.newBuilder();
        builder.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeCancel, getRequestMap());
        addDisposable(apiServer.cancelPlay(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<SubscribeCancelPb.Ret_PBIFE_trade_subscribeCancel>(baseView) {
            @Override
            public void onSuccess(SubscribeCancelPb.Ret_PBIFE_trade_subscribeCancel ret_pbife_trade_subscribeCancel) {
                baseView.cancel(ret_pbife_trade_subscribeCancel.getReturnCode(), ret_pbife_trade_subscribeCancel.getReturnMsg());
            }
        });
    }
}
