package com.hundsun.zjfae.activity.moneymanagement.presenter;

import android.content.DialogInterface;

import com.hundsun.zjfae.activity.moneymanagement.view.SellView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.Attachment;
import onight.zjfae.afront.gens.CheckSmsValidateCodeWithInnerMobile;
import onight.zjfae.afront.gens.PBIFEPrdqueryPrdQueryTaUnitFinanceById;
import onight.zjfae.afront.gens.SMSTransferRateLowSendValidateCode;
import onight.zjfae.afront.gens.v2.QueryTransferSellProfitsPB;
import onight.zjfae.afront.gensazj.Dictionary;

/**
 * @Description:我要卖（presenter）
 * @Author: yangtianren
 */
public class SellPresenter extends BasePresenter<SellView> {

    private String unitId;
    private String keyCode = "0";
    public SellPresenter(SellView baseView) {
        super(baseView);
    }

    public void initData(String productId,String productCode){
        unitId = productId;

        Observable observable= Observable.mergeDelayError(getDictionary(),getDate(productId),getAttachmentList(productCode));

        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                if (mClass instanceof PBIFEPrdqueryPrdQueryTaUnitFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceById){

                    baseView.getDetail(((PBIFEPrdqueryPrdQueryTaUnitFinanceById.Ret_PBIFE_prdquery_prdQueryTaUnitFinanceById)mClass).getData(),keyCode);
                }
                else if (mClass instanceof Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment){
                    baseView.productAttachment((Attachment.Ret_PBIFE_prdquery_prdQueryProductAttachment)mClass);
                }
                else if (mClass instanceof Dictionary.Ret_PBAPP_dictionary){

                    Dictionary.Ret_PBAPP_dictionary dictionary = (Dictionary.Ret_PBAPP_dictionary) mClass;
                    keyCode = dictionary.getData().getParmsList().get(0).getKeyCode();

                }
            }
        });
    }

    /**
     * 获取数据
     * */
    private Observable getDate(String productId) {
        PBIFEPrdqueryPrdQueryTaUnitFinanceById.REQ_PBIFE_prdquery_prdQueryTaUnitFinanceById.Builder builder = PBIFEPrdqueryPrdQueryTaUnitFinanceById.REQ_PBIFE_prdquery_prdQueryTaUnitFinanceById.newBuilder();
        builder.setId(productId);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PrdQueryTaUnitFinanceById, getRequestMap());
        return apiServer.sellGetDate(url, getBody(builder.build().toByteArray()));
    }

    /**
     * 详情附件查询
     **/
    private Observable getAttachmentList(String productCode) {
        Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.Builder attachment = Attachment.REQ_PBIFE_prdquery_prdQueryProductAttachment.newBuilder();
        attachment.setProductCode(productCode);
        attachment.setVisibleFlag("3");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PBIFE_prdquery_prdQueryProductAttachment, getRequestMap());

        return apiServer.attachmentList(url, getBody(attachment.build().toByteArray()));
    }


    private Observable getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());

        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("companyUser.rateSwitch");
        return apiServer.getDictionary(url,getBody(diction.build().toByteArray()));
    }


    /**
     * 挂卖金额测算
     * svp产品且不需要利率调用
     */
    public void onSellProfits(String productCode, String buyerSmallestAmount, String delegateNum, String actualRate,boolean isEstimatedAmount) {
        QueryTransferSellProfitsPB.REQ_PBIFE_trade_queryTransferSellProfits.Builder builder = QueryTransferSellProfitsPB.REQ_PBIFE_trade_queryTransferSellProfits.newBuilder();
        builder.setProductCode(productCode);
        builder.setBuyerSmallestAmount(buyerSmallestAmount.replace(",", ""));
        builder.setDelegateNum(delegateNum);
        builder.setActualRate(actualRate);
        builder.setUnitId(unitId);
        Map map = getRequestMap();
        map.put("version",twoVersion);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferSellProfits, map);

        addDisposable(apiServer.sellProfits(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits>() {
            @Override
            public void onSuccess(QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits ret_pbife_trade_queryTransferSellProfits) {

               if (ret_pbife_trade_queryTransferSellProfits.getReturnCode().equals(ConstantCode.INCOME)){

                   baseView.onSellProfitsStatus(ret_pbife_trade_queryTransferSellProfits.getData(),ret_pbife_trade_queryTransferSellProfits.getReturnMsg(),isEstimatedAmount);
               }
               else {
                   baseView.sellProfits(ret_pbife_trade_queryTransferSellProfits.getData(),isEstimatedAmount);
               }

            }
        });
    }

    /**
     * 挂卖金额测算
     */
    public void sellProfits(String productCode, String buyerSmallestAmount, String delegateNum, String actualRate) {
        QueryTransferSellProfitsPB.REQ_PBIFE_trade_queryTransferSellProfits.Builder builder = QueryTransferSellProfitsPB.REQ_PBIFE_trade_queryTransferSellProfits.newBuilder();
        builder.setProductCode(productCode);
        builder.setBuyerSmallestAmount(buyerSmallestAmount.replace(",", ""));
        builder.setDelegateNum(delegateNum);
        builder.setActualRate(actualRate);
        builder.setUnitId(unitId);
        Map map = getRequestMap();
        map.put("version",twoVersion);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryTransferSellProfits, map);

        addDisposable(apiServer.sellProfits(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits>(baseView) {
            @Override
            public void onSuccess(QueryTransferSellProfitsPB.Ret_PBIFE_trade_queryTransferSellProfits ret_pbife_trade_queryTransferSellProfits) {

                baseView.sellProfits(ret_pbife_trade_queryTransferSellProfits.getData(),true);
            }
        });
    }


    //发送短信验证码
    public void sendSmsCode(String productName,String abandonEstimateProfit){

        SMSTransferRateLowSendValidateCode.REQ_PBIFE_smsValidateCode_transferRateLowSendValidateCode.Builder builder = SMSTransferRateLowSendValidateCode.REQ_PBIFE_smsValidateCode_transferRateLowSendValidateCode.newBuilder();
        builder.setProductName(productName);
        builder.setAbandonEstimateProfit(abandonEstimateProfit);


        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SendValidateCode);

        addDisposable(apiServer.onSellSendSms(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<SMSTransferRateLowSendValidateCode.Ret_PBIFE_smsValidateCode_transferRateLowSendValidateCode>(baseView) {
            @Override
            public void onSuccess(SMSTransferRateLowSendValidateCode.Ret_PBIFE_smsValidateCode_transferRateLowSendValidateCode entity) {

                baseView.onSmsCodeStatusSuccess(entity.getReturnMsg());

            }
        });

    }

    //校验短信验证码
    public void onVerifySmsCode(String smsCode, DialogInterface dialog){

        CheckSmsValidateCodeWithInnerMobile.REQ_PBIFE_smsValidateCode_checkSmsValidateCodeWithInnerMobile.Builder builder = CheckSmsValidateCodeWithInnerMobile.REQ_PBIFE_smsValidateCode_checkSmsValidateCodeWithInnerMobile.newBuilder();

        builder.setSmsValidateCode(smsCode);
        builder.setSmsValidateCodeType("TR");

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CheckSmsValidateCodeWithInnerMobile);


        addDisposable(apiServer.onVerifySmsCode(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<CheckSmsValidateCodeWithInnerMobile.Ret_PBIFE_smsValidateCode_checkSmsValidateCodeWithInnerMobile>() {
            @Override
            public void onSuccess(CheckSmsValidateCodeWithInnerMobile.Ret_PBIFE_smsValidateCode_checkSmsValidateCodeWithInnerMobile entity) {

                String code = entity.getReturnCode();

                if (code.equals(ConstantCode.RETURN_CODE)){
                    baseView.onSmsCodeVerifyStatus(true,entity.getReturnMsg(),dialog);
                }
                //验证码校验不通过
                else if (code.equals("0100")){
                    baseView.onSmsCodeVerifyStatus(false,entity.getReturnMsg(),dialog);


                }
            }
        });



    }

    //预期利率
    private String expectedMaxAnnualRate = "";

    public String getExpectedMaxAnnualRate() {
        return expectedMaxAnnualRate;
    }

    public void setExpectedMaxAnnualRate(String expectedMaxAnnualRate) {
        this.expectedMaxAnnualRate = expectedMaxAnnualRate;
    }
}
