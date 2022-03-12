package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.activity.product.view.ProductCodeView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductPre;
import onight.zjfae.afront.gens.ProductSec;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.PBIFEPrdqueryPrdQueryProductDetails;
import onight.zjfae.afront.gens.v3.UserDetailInfo;


/**
 * @author moran
 * 产品详情
 * */
public class ProductCodePresenter extends BasePresenter<ProductCodeView> {

    /**
     * 是否合格投资者购买
     * **/
    private String accreditedBuyIs = "0";

    public ProductCodePresenter(ProductCodeView baseView) {
        super(baseView);
    }

    /**
     * 产品详情,产品预检查,附件列表,用户信息,四个接口合并
     * */
    public void initProductData(final String productCode,final String type) {
        final long startTime = System.currentTimeMillis();

        Observable observables = Observable.mergeArrayDelayError(requestPrdQueryProductDetails(productCode, type),checkProductCState(productCode),
                requestAttachment(productCode),getUserInfo());

        addDisposable(observables, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                if (mClass instanceof PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails){

                    accreditedBuyIs =  ((PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails) mClass).getData().getTaProductFinanceDetail().getAccreditedBuyIs();

                    final long stopTime = System.currentTimeMillis();

                    long requestTime = (stopTime - startTime) / 1000;

                    baseView.onProductData((PBIFEPrdqueryPrdQueryProductDetails.Ret_PBIFE_prdquery_prdQueryProductDetails) mClass,requestTime);
                }
                else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment){

                    baseView.onAttachmentInfo((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass,accreditedBuyIs);
                }

                else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo){

                    baseView.onUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass,accreditedBuyIs);
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
        map.put("version", version);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SubscribeProductSec, map);

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


    /**
     * 点击附件查询用户详细信息
     * */
    public void attachmentUserInfo(final String accreditedBuyIs, final Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, final String riskLevelLabelName, final String riskLevelLabelUrl, final boolean isAttachment) {

        addDisposable(getUserInfo(), new BaseProductPlayProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.getAttachmentUserInfo(userDetailInfo, accreditedBuyIs, attachmentList, riskLevelLabelName, riskLevelLabelUrl, isAttachment);
            }
        });
    }
    /**
     * 获取用户信息
     */
    public void getUserDate() {

        addDisposable(getUserInfo(), new BaseProductPlayProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.getUserDetailInfo(userDetailInfo);
            }
        });

    }


    /**
     * 购买预检查第一步
     * */
    private Observable checkProductCState(String productCode) {
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
    private Observable requestPrdQueryProductDetails(String productCode, String type) {

        PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.Builder product = PBIFEPrdqueryPrdQueryProductDetails.REQ_PBIFE_prdquery_prdQueryProductDetails.newBuilder();
        product.setProductCode(productCode);
        if (type.equals("")) {
            product.setDifferent("01");
        } else if (type.equals("SubscriptionDetailActivity")) {
            product.setDifferent("08");
        }
        product.setVersion("1.0.1");
        Map<String, String> map = getRequestMap();
        map.put("version", version);
        map.put("isNewInterface","1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryProductDetails, map);
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), product.build().toByteArray());

        return apiServer.productCodeInfo(url, body);
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
