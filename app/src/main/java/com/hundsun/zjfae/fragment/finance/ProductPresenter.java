package com.hundsun.zjfae.fragment.finance;

import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew;
import onight.zjfae.afront.gens.v3.UserDetailInfo;


/**
 * @author moran
 * 产品列表Presenter
 */
public class ProductPresenter extends BasePresenter<ProductView> {


    private long requestTime;
    private AllAzjProto.PEARetControl productControl;


    public ProductPresenter(ProductView baseView) {
        super(baseView);
    }


    /******************************产品列表*******************************************/
    /**
     * 获取产品列表
     *
     * @param uuids     查询条件
     * @param pageIndex 查询页数
     */
    public void initProduct(String uuids, String productName, String quanDetailsId, String quanUsedProductCode, String quanUsedSeriesCode, int pageIndex, String count) {
        if (!BaseActivity.isLogin) {
            return;
        }
        long systemTime = System.currentTimeMillis();

        boolean isRequest = requestTime + (10 * 60 * 1000) - systemTime > 0;
        if (isRequest && productControl != null) {
            addDisposable(productList(uuids, productName, quanDetailsId, quanUsedProductCode, quanUsedSeriesCode, pageIndex, count), new ProtoBufObserver<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew>(baseView) {
                @Override
                public void onSuccess(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew productListNew) {
                    baseView.onControl(productControl);
                    baseView.onProductList(productListNew);

                }
            });
        } else {
            Observable observable = Observable.mergeDelayError(controlList(), productList(uuids, productName, quanDetailsId, quanUsedProductCode, quanUsedSeriesCode, pageIndex, count));

            addDisposable(observable, new ProtoBufObserver(baseView) {
                @Override
                public void onSuccess(Object mClass) {
                    if (mClass instanceof PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew) {
                        //产品列表
                        baseView.onProductList((PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew) mClass);
                    } else if (mClass instanceof AllAzjProto.PEARetControl) {
                        //条件查询
                        requestTime = System.currentTimeMillis();
                        productControl = (AllAzjProto.PEARetControl) mClass;
                        baseView.onControl(productControl);
                    }

                }
            });
        }


    }


    /**
     * 用户详细信息
     *
     * @param isAuthentication 是否身份认证调用
     */
    public void getUserData(final boolean isAuthentication) {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.onUserInfo(userDetailInfo, isAuthentication);
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
     * 购买列表
     **/
    private Observable productList(String uuids, String productName, String quanDetailsId, String quanUsedProductCode, String quanUsedSeriesCode, int pageIndex, String count) {


        PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.REQ_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.Builder product = PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNew.REQ_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNew.newBuilder();
        product.setPageIndex(String.valueOf(pageIndex));
        product.setPageSize(count);
        product.setTerminalNo("12");
        product.setMarketingChannel(UserInfoSharePre.getBrokerNo());
        product.setUuids(uuids);
        product.setProductName(productName);
        product.setQuanDetailsId(quanDetailsId);
        product.setQuanUsedProductCode(quanUsedProductCode);
        product.setQuanUsedSeriesCode(quanUsedSeriesCode);
        product.setSpecialQryType("00");

        Map<String, String> map = getRequestMap();
        map.put("version", FOUR_VERSION);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdsubscribequery_prdQuerySubscribeProductListNew, map);

        return apiServer.productList(url, getBody(product.build().toByteArray()));

    }


    /**
     * 购买查询条件
     **/
    private Observable controlList() {

        String controlUrl = parseUrl(AZJ, PBCTL, VCTLAZJ, getRequestMap());
        AllAzjProto.PEAGetControl.Builder control = AllAzjProto.PEAGetControl.newBuilder();
        control.setControlLocation("subscribeList");
        control.setVersionClassify("00,03");

        return apiServer.subscribeProduct(controlUrl, getBody(control.build().toByteArray()));
    }


    public AllAzjProto.PEARetControl getProductControl() {


        return productControl;
    }


}
