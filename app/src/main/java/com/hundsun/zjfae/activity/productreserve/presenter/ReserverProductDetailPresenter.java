package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.productreserve.bean.ReserverProductBean;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductPre;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v2.ReserveProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

public class ReserverProductDetailPresenter extends BasePresenter<ReserveProductDetailView> {
    final ReserverProductBean productBean = new ReserverProductBean();

    public ReserverProductDetailPresenter(ReserveProductDetailView baseView) {
        super(baseView);
    }

    //产品预检查,产品详情，附件列表，用户信息，四个接口合并
    public void allProductRequest(String productCode) {
        Observable observable = Observable.concatArray(checkProductCState(productCode),
                requestAttachment(productCode), getUserInfo());
        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                if (mClass instanceof ProductPre.Ret_PBIFE_trade_subscribeProductPre) {
                    productBean.setProductPre((ProductPre.Ret_PBIFE_trade_subscribeProductPre) mClass);
                } else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) {
                    //附件列表
                    productBean.setAttachment((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass);
                } else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    //用户信息
                    productBean.setUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass);
                    baseView.getProductBean(productBean);
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

        Map map = getRequestMap();
        map.put("version",version);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeProductSec,map);

        ProductSec.REQ_PBIFE_trade_subscribeProductSec.Builder product = ProductSec.REQ_PBIFE_trade_subscribeProductSec.newBuilder();
        product.setProductCode(productCode);
        product.setDelegateNum(delegateNum);
        product.setVersion("1.0.1");
        addDisposable(apiServer.productBuyCheck(url, getBody(product.build().toByteArray())), new BaseProductPlayProtoBufObserver<ProductSec.Ret_PBIFE_trade_subscribeProductSec>(baseView) {
            @Override
            public void onSuccess(ProductSec.Ret_PBIFE_trade_subscribeProductSec ret_pbife_trade_subscribeProductSec) {

                baseView.productCheckState(ret_pbife_trade_subscribeProductSec);
            }
        });


    }

    //购买预检查第一步
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
    public void requestPrdQueryProductDetails(String productCode, String orderType, String orderNum) {

        ReserveProductDetails.REQ_PBIFE_prdquery_prdQueryOrderProductDetails.Builder product = ReserveProductDetails.REQ_PBIFE_prdquery_prdQueryOrderProductDetails.newBuilder();
        product.setOrderProductCode(productCode);
        product.setOrderType(orderType);
        product.setOrderNum(orderNum);
        if (orderType.equals("0")) {
            product.setDifferent("04");
        } else {
            product.setDifferent("05");
        }
        product.setDifferent("1.0.1");
        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);
        String url = parseUrl(MZJ, PBIFE, VIFEMZJ, ConstantName.ReserveOrderListProductDetail, map);
        addDisposable(apiServer.requestPrdQueryProductDetails(url, getBody(product.build().toByteArray())), new BaseProductPlayProtoBufObserver<ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails>(baseView) {
            @Override
            public void onSuccess(ReserveProductDetails.Ret_PBIFE_prdquery_prdQueryOrderProductDetails ret_pbife_prdquery_prdQueryOrderProductDetails) {
                productBean.setProductDetails(ret_pbife_prdquery_prdQueryOrderProductDetails);
                baseView.getProductDetail(ret_pbife_prdquery_prdQueryOrderProductDetails);
            }
        });
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
        attachment.setVisibleFlag("2");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_prdQueryProductAttachment, getRequestMap());
        return apiServer.attachmentList(url, getBody(attachment.build().toByteArray()));
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
    //65周岁提交
    public void ageRequest(){

        String url = parseUrl(MZJ,PBIFE,VREGMZJ, ConstantName.AgeReminder);

        addDisposable(url, new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {

            }
        });
    }
}
