package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.productreserve.view.MyReserveDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @Description:我的预约预约模块详情（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class MyReserveDetailPresenter extends BasePresenter<MyReserveDetailView> {
    private PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails data0;
    private ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail data1;
    private Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment attachment;
    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;

    public MyReserveDetailPresenter(MyReserveDetailView baseView) {
        super(baseView);
    }


    public void allReserveDetailRequest(final String orderType, String productCode) {
        Observable observable = Observable.concatArray(requestAttachmen(productCode), getUserInfo(), getReserveListDetailData(orderType, productCode));
        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                if (mClass instanceof PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails) {
                    //产品详情
                    data0 = (PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails) mClass;
                    baseView.getAllData(orderType, data0, data1, attachment, userDetailInfo);
                } else if (mClass instanceof ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail) {
                    //产品详情
                    data1 = (ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail) mClass;
                    baseView.getAllData(orderType, data0, data1, attachment, userDetailInfo);
                } else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) {
                    //附件列表
                    attachment = (Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass;
                } else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    //用户信息
                    userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass;
                }
            }
        });
    }


    /**
     * 获取我的预约详情数据
     */
    public Observable getReserveListDetailData(final String orderType, String productCode) {
//        Map map = new HashMap();
//        map.put("productCode", productCode);
//        String body = parseBody(map);
        if (orderType.equals("0")) {
            //特约接口
            PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.Builder builder = PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.newBuilder();
            builder.setProductCode(productCode);
            builder.setDifferent("04");
            builder.setVersion("1.0.1");
            Map map = getRequestMap();
            map.put("version", version);
            map.put("isNewInterface","1");
            String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryProductDetails, map);
            CCLog.e(url);
            return apiServer.productCodeInfo(url, getBody(builder.build().toByteArray()));
        } else {
            //非特约接口
            ProductOrderInfoDetailPB.REQ_PBIFE_trade_queryProductOrderInfoDetail.Builder builder = ProductOrderInfoDetailPB.REQ_PBIFE_trade_queryProductOrderInfoDetail.newBuilder();
            builder.setProductCode(productCode);
            builder.setVersion("1.0.1");
            builder.setDifferent("05");
            Map map = getRequestMap();
            map.put("version", version);
            String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.MyReserveOrderListDetailType1, map);
            CCLog.e(url);
            return apiServer.getReserveListDetailData(url, getBody(builder.build().toByteArray()));
        }

    }

    /**
     * 详情附件查询
     */
    public Observable requestAttachmen(String productCode) {

        Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.Builder attachment = Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.newBuilder();
        attachment.setProductCode(productCode);
        attachment.setVisibleFlag("1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_prdQueryProductAttachment, getRequestMap());

        return apiServer.attachmentList(url, getBody(attachment.build().toByteArray()));
    }

    /**
     * 获取用户信息
     */
    public Observable getUserDate() {

        return getUserInfo();
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

    //点击附件查询用户详细信息
    public void attachmentUserInfo(final String accreditedBuyIs, final Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, final String riskLevelLabelName, final String riskLevelLabelUrl, final Boolean isAttachment) {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.getAttachmentUserInfo(userDetailInfo, accreditedBuyIs, attachmentList, riskLevelLabelName, riskLevelLabelUrl, isAttachment);
            }
        });

    }
}
