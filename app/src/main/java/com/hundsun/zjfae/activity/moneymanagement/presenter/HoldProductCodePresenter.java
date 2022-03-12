package com.hundsun.zjfae.activity.moneymanagement.presenter;

import com.hundsun.zjfae.activity.moneymanagement.HoldProductBean;
import com.hundsun.zjfae.activity.moneymanagement.view.HoldProductCodeView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.EntrustedDetails;
import onight.zjfae.afront.gens.ProductPre;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

public class HoldProductCodePresenter extends BasePresenter<HoldProductCodeView> {

    public HoldProductCodePresenter(HoldProductCodeView baseView) {
        super(baseView);
    }

    /**
     * 产品预检查,产品详情，附件列表，用户信息，四个接口合并
     */
    public void allProductRequest(String productCode, String productId) {
        final HoldProductBean productBean = new HoldProductBean();
        Observable observable = Observable.concatArray(checkProductCState(productCode),
                requestAttachment(productCode), notice(), requestentrustedDetails(productCode), getUserInfo(), requestPrdQueryProductDetails(productCode, productId));
        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {
                if (mClass instanceof ProductPre.Ret_PBIFE_trade_subscribeProductPre) {
                    productBean.setProductPre((ProductPre.Ret_PBIFE_trade_subscribeProductPre) mClass);
                } else if (mClass instanceof PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails) {
                    //产品详情
                    productBean.setProductDetails((PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails) mClass);
                    baseView.getProductBean(productBean);
                } else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) {
                    //附件列表
                    productBean.setAttachment((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass);
                } else if (mClass instanceof Notices.Ret_PBAPP_notice) {
                    //免责声明
                    productBean.setNotice((Notices.Ret_PBAPP_notice) mClass);
                } else if (mClass instanceof EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails) {
                    //受托管理报告
                    productBean.setEntrustedDetails((EntrustedDetails.Ret_PBIFE_prdquery_getQueryEntrustedDetails) mClass);
                } else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    //用户信息
                    productBean.setUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass);
                }

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


    /**
     * 产品购买预检查第二步
     */
    public void productBuyCheck(String productCode, String delegateNum) {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeProductSec, getRequestMap());

        ProductSec.REQ_PBIFE_trade_subscribeProductSec.Builder product = ProductSec.REQ_PBIFE_trade_subscribeProductSec.newBuilder();
        product.setProductCode(productCode);
        product.setDelegateNum(delegateNum);
        product.setVersion("1.0.1");
        addDisposable(apiServer.productBuyCheck(url, getBody(product.build().toByteArray())), new ProtoBufObserver<ProductSec.Ret_PBIFE_trade_subscribeProductSec>(baseView) {
            @Override
            public void onSuccess(ProductSec.Ret_PBIFE_trade_subscribeProductSec ret_pbife_trade_subscribeProductSec) {

                baseView.productCheckState(ret_pbife_trade_subscribeProductSec);
            }
        });


    }

    /**
     * 购买预检查第一步
     */
    private Observable checkProductCState(final String productCode) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ProductCState, getRequestMap());

        ProductPre.REQ_PBIFE_trade_subscribeProductPre.Builder productType = ProductPre.REQ_PBIFE_trade_subscribeProductPre.newBuilder();
        productType.setProductCode(productCode);
        return apiServer.checkProductCState(url, getBody(productType.build().toByteArray()));
    }

    /**
     * 产品详情
     *
     * @param productCode 产品code
     */
    private Observable requestPrdQueryProductDetails(String productCode, String unitId) {

        PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.Builder product = PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.newBuilder();
        product.setProductCode(productCode);
        product.setDifferent("03");
        product.setVersion("1.0.1");
        product.setUnitId(unitId);
        Map<String, String> map = getRequestMap();
        map.put("version", version);
        map.put("isNewInterface", "1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryProductDetails, map);
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), product.build().toByteArray());

        return apiServer.productCodeInfo(url, body);
    }

    /**
     * 获取用户信息
     */
    public void getUserDate() {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.getUserDetailInfo(userDetailInfo);
            }
        });

    }

    /**
     * 详情附件查询
     */
    private Observable requestAttachment(String productCode) {
        Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.Builder attachment = Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.newBuilder();
        attachment.setProductCode(productCode);
        attachment.setVisibleFlag("1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_prdQueryProductAttachment, getRequestMap());
        return apiServer.attachmentList(url, getBody(attachment.build().toByteArray()));
    }

    /**
     * 受托管理报告
     */
    private Observable requestentrustedDetails(String productCode) {
        EntrustedDetails.REQ_PBIFE_prdquery_getQueryEntrustedDetails.Builder builder = EntrustedDetails.REQ_PBIFE_prdquery_getQueryEntrustedDetails.newBuilder();
        builder.setProductCode(productCode);
        Map map = getRequestMap();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_getQueryEntrustedDetails);
        return apiServer.entrustedDetailsList(url, getBody(builder.build().toByteArray()));
    }

    /**
     * 022免责声明
     */
    private Observable notice() {
        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setType("022");

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Notice, map);

        return apiServer.notice(url, getBody(notice.build().toByteArray()));

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
