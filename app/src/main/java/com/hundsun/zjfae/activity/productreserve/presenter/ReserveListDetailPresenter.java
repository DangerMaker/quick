package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.productreserve.view.ReserveListDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.ProductOrderValidate;
import onight.zjfae.afront.gens.ProductReservePB;
import onight.zjfae.afront.gens.ProductReservePrePB;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v3.ProductOrderInfoDetailPB;

/**
 * @Description:产品预约模块 长期预约 短期预约 详情（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class ReserveListDetailPresenter extends BasePresenter<ReserveListDetailView> {

    /**
     * 是否合格投资者购买
     * **/
    private String accreditedBuyIs = "0";
    public ReserveListDetailPresenter(ReserveListDetailView baseView) {
        super(baseView);
    }


    /**
     * 预约金额检查接口
     */
    public void reserveProductPre(final boolean commit, final String orderProductCode, String orderBuyAmount) {
        ProductReservePrePB.REQ_PBIFE_trade_queryProductOrderDeposit.Builder builder = ProductReservePrePB.REQ_PBIFE_trade_queryProductOrderDeposit.newBuilder();
        builder.setOrderProductCode(orderProductCode);
        builder.setOrderBuyAmount(orderBuyAmount);
        builder.setVersion("1.0.1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetailReservePre, getRequestMap());
        addDisposable(apiServer.reserveProductPre(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<ProductReservePrePB.Ret_PBIFE_trade_queryProductOrderDeposit>() {
            @Override
            public void onSuccess(ProductReservePrePB.Ret_PBIFE_trade_queryProductOrderDeposit data) {
                if (data.getReturnCode().equals(ConstantCode.RETURN_CODE)){
                    baseView.reserveProductPre(commit, orderProductCode);
                }
                else {
                    baseView.showError(data.getReturnMsg());
                }

            }
        });
    }

    /**
     * 预约接口
     */
    public void reserveProduct(String orderProductCode) {
        ProductReservePB.REQ_PBIFE_trade_queryProductOrderTips.Builder builder = ProductReservePB.REQ_PBIFE_trade_queryProductOrderTips.newBuilder();
        builder.setOrderProductCode(orderProductCode);
        builder.setVersion("1.0.1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetailReserve);
        addDisposable(apiServer.reserveProduct(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<ProductReservePB.Ret_PBIFE_trade_queryProductOrderTips>(baseView) {
            @Override
            public void onSuccess(ProductReservePB.Ret_PBIFE_trade_queryProductOrderTips data) {
                baseView.reserveProduct(data.getReturnCode(), data.getReturnMsg(), data.getData().getMsg());
            }


        });
    }

    /**
     * 预约产品预检查,预约产品详情，附件列表，用户信息，四个接口合并
     * */
    public void allProductRequest(String productCode) {
        Observable observable = Observable.mergeArrayDelayError(getReserveListDetailData(productCode),checkProductCState(productCode),
                getUserInfo(),requestAttachment(productCode));
        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                //长期预约或短期预约产品详情
                if (mClass instanceof ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail ){
                    ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail productOrderInfoDetail = (ProductOrderInfoDetailPB.Ret_PBIFE_trade_queryProductOrderInfoDetail) mClass;
                    accreditedBuyIs =  productOrderInfoDetail.getData().getTaProductOrderInfo().getAccreditedBuyIs();
                    baseView.onProductOrderInfo(productOrderInfoDetail);
                }
                else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo){

                    baseView.onUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass,accreditedBuyIs);
                }

                else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment){
                    baseView.onAttachmentInfo((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass,accreditedBuyIs);
                }


            }
        });
    }

    /**
     * 购买预检查第一步
     * */
    private Observable checkProductCState(final String productCode) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListValidate, getRequestMap());

        ProductOrderValidate.REQ_PBIFE_trade_productOrderValidate.Builder  productType = ProductOrderValidate.REQ_PBIFE_trade_productOrderValidate.newBuilder();

        productType.setOrderProductCode(productCode);

        return apiServer.productOrderValidateState(url, getBody(productType.build().toByteArray()));
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

    /**
     * 获取长期或者短期产品详情
     */
    private Observable getReserveListDetailData(String productCode) {
        ProductOrderInfoDetailPB.REQ_PBIFE_trade_queryProductOrderInfoDetail.Builder builder = ProductOrderInfoDetailPB.REQ_PBIFE_trade_queryProductOrderInfoDetail.newBuilder();
        builder.setProductCode(productCode);
        builder.setDifferent("05");
        builder.setVersion("1.0.1");
        Map map = getRequestMap();
        map.put("version", version);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetail, map);
        return apiServer.getReserveListDetailData(url, getBody(builder.build().toByteArray()));
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
     * 点击附件查询用户详细信息
     * */
    public void attachmentUserInfo(final String accreditedBuyIs, final Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, final String riskLevelLabelName, final String riskLevelLabelUrl, final boolean isAttachment) {

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
