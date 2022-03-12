package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.activity.product.view.TransferDetailView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;
import com.zjfae.jpush.JPush;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.TransferBuyProfits;
import onight.zjfae.afront.gens.TransferOrderPre;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v2.PBIFEPrdtransferqueryPrdDeliveryInfoDetail;
import onight.zjfae.afront.gens.v2.TransferOrderSec;
import onight.zjfae.afront.gens.v3.UserDetailInfo;

/**
 * @author moran
 * 转让详情Presenter
 */
public class TransferDetailPresenter extends BasePresenter<TransferDetailView> {

    /**
     * 是否合格投资者购买
     **/
    private String accreditedBuyIs = "0";


    /**
     * 是否绑卡
     */
    private String isBondedCard = "false";


    /**
     * 是否真正合格投资者
     */
    private String isRealInvestor = "";


    public TransferDetailPresenter(TransferDetailView baseView) {
        super(baseView);
    }


    /**
     * 初始化产品信息
     */
    public void initTransferData(String productCode, String delegationCode) {

        Observable observable = Observable.mergeDelayError(getTransferOrderPre(delegationCode), getUserInfo(), getAttachmentList(productCode));


        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {
                //用户信息
                if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    baseView.onUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass, accreditedBuyIs);
                }
                //附件列表
                else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) {

                    baseView.onAttachmentInfo((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass, accreditedBuyIs);
                }
            }
        });
    }


    /**
     * 初始化产品信息
     */
    public void initTransferData(String productCode, String delegationCode, String delegateNum, String actualRate) {

        Observable observable = Observable.mergeArrayDelayError(getTransferOrderPre(delegationCode), getEarning(productCode, delegateNum, actualRate, delegationCode), getUserInfo(), getAttachmentList(productCode));
        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {
                //用户信息
                if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    baseView.onUserDetailInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass, accreditedBuyIs);
                }
                //本金请求
                else if (mClass instanceof TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits) {
                    baseView.onEarningsInfo((TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits) mClass);
                }
                //附件列表
                else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) {

                    baseView.onAttachmentInfo((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment) mClass, accreditedBuyIs);
                }
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

                isBondedCard = userDetailInfo.getData().getIsBondedCard();

                isRealInvestor = userDetailInfo.getData().getIsRealInvestor();

                baseView.getUserDetailInfo(userDetailInfo);
            }

        });
    }

    public String getIsBondedCard() {
        return isBondedCard;
    }

    public String getIsRealInvestor() {
        return isRealInvestor;
    }

    /**
     * 请求本金信息
     */
    public void getEarnings(String productCode, String delegateNum, String actualRate, String delegationCode) {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferBuyProfits);

        TransferBuyProfits.REQ_PBIFE_trade_queryTransferBuyProfits.Builder transfer
                = TransferBuyProfits.REQ_PBIFE_trade_queryTransferBuyProfits.newBuilder();
        delegateNum = delegateNum.replaceAll("元", "");
        delegateNum.replaceAll(",", "");
        transfer.setProductCode(productCode);
        transfer.setDelegateNum(delegateNum.trim());
        transfer.setActualRate(actualRate);
        transfer.setDelegationCode(delegationCode);
        addDisposable(apiServer.getEarnings(url, getBody(transfer.build().toByteArray())), new BaseProductPlayProtoBufObserver<TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits>(baseView) {
            @Override
            public void onSuccess(TransferBuyProfits.Ret_PBIFE_trade_queryTransferBuyProfits ret_pbife_trade_queryTransferBuyProfits) {
                baseView.onEarningsInfo(ret_pbife_trade_queryTransferBuyProfits);
            }


        });
    }


    /**
     * 获取转让产品详细信息
     */
    public void getTransferDetailInfo(String delegationId) {


        PBIFEPrdtransferqueryPrdDeliveryInfoDetail.REQ_PBIFE_prdtransferquery_prdDeliveryInfoDetail.Builder builder = PBIFEPrdtransferqueryPrdDeliveryInfoDetail.REQ_PBIFE_prdtransferquery_prdDeliveryInfoDetail.newBuilder();

        builder.setId(delegationId);

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferDetail, map);

        addDisposable(apiServer.getTransferDetailInfo(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail>(baseView) {
            @Override
            public void onSuccess(PBIFEPrdtransferqueryPrdDeliveryInfoDetail.Ret_PBIFE_prdtransferquery_prdDeliveryInfoDetail mClass) {
                accreditedBuyIs = (mClass).getData().getTaProductFinanceDetail().getAccreditedBuyIs();
                baseView.onTransferData(mClass);

            }
        });

    }

    /**
     * 获取产品附件
     */
    private Observable getAttachmentList(String productCode) {

        Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.Builder attachment = Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.newBuilder();
        attachment.setProductCode(productCode);
        attachment.setVisibleFlag("4");

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_prdQueryProductAttachment);

        return apiServer.attachmentList(url, getBody(attachment.build().toByteArray()));
    }


    /**
     * 转让本金信息
     */
    private Observable getEarning(String productCode, String delegateNum, String actualRate, String delegationCode) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferBuyProfits);
        TransferBuyProfits.REQ_PBIFE_trade_queryTransferBuyProfits.Builder transfer
                = TransferBuyProfits.REQ_PBIFE_trade_queryTransferBuyProfits.newBuilder();
        delegateNum = delegateNum.replaceAll("元", "");
        delegateNum = delegateNum.replaceAll(",", "");
        CCLog.e("productCode", productCode);
        CCLog.e("delegateNum", delegateNum);
        CCLog.e("actualRate", actualRate);
        transfer.setProductCode(productCode);
        transfer.setDelegateNum(delegateNum.trim());
        transfer.setActualRate(actualRate);
        transfer.setDelegationCode(delegationCode);
        return apiServer.getEarnings(url, getBody(transfer.build().toByteArray()));
    }

    /**
     * 转让产品预检查
     */
    private Observable getTransferOrderPre(String delegationCode) {
        TransferOrderPre.REQ_PBIFE_trade_transferOrderPre.Builder transFer
                = TransferOrderPre.REQ_PBIFE_trade_transferOrderPre.newBuilder();
        transFer.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrderPre);
        return apiServer.getTransferOrderPre(url, getBody(transFer.build().toByteArray()));


    }


    /**
     * @param buyNum         购买金额
     * @param delegationCode 产品代码
     * @return
     * @method
     * @description 确购买买预检查
     * @date 2019/6/10 12:20
     * @author 作者名
     */
    public void getTransferOrderSec(String buyNum, String delegationCode) {
        Map body = new HashMap();
        buyNum = buyNum.replaceAll(",", "").trim();
        body.put("buyNum", buyNum);
        body.put("version", "1.0.1");
        body.put("delegationCode", delegationCode);

        TransferOrderSec.REQ_PBIFE_trade_transferOrderSec.Builder tansFer = TransferOrderSec.REQ_PBIFE_trade_transferOrderSec.newBuilder();
        tansFer.setBuyNum(buyNum);
        tansFer.setVersion("1.0.1");
        tansFer.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrderSec);

        addDisposable(apiServer.getTransferOrderSec(url, getBody(tansFer.build().toByteArray())), new BaseProductPlayProtoBufObserver<TransferOrderSec.Ret_PBIFE_trade_transferOrderSec>(baseView) {
            @Override
            public void onSuccess(TransferOrderSec.Ret_PBIFE_trade_transferOrderSec transferOrderSec) {
                baseView.getTransferOrderSecBean(transferOrderSec);
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


    /**
     * 点击附件查询用户详细信息
     */
    public void attachmentUserInfo(final String accreditedBuyIs, final Attachment.PBIFE_prdquery_prdQueryProductAttachment.TaProductAttachmentList attachmentList, final String riskLevelLabelName, final String riskLevelLabelUrl, final Boolean isAttachment) {

        addDisposable(getUserInfo(), new BaseProductPlayProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                baseView.getAttachmentUserInfo(userDetailInfo, accreditedBuyIs, attachmentList, riskLevelLabelName, riskLevelLabelUrl, isAttachment);
            }
        });

    }

    //65周岁提交
    public void ageRequest() {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.AgeReminder);

        addDisposable(url, new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {

            }
        });
    }
}
