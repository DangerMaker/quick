package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.productreserve.bean.ReservePlayInfoBean;
import com.hundsun.zjfae.activity.productreserve.view.ReservePayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.StringUtils;

import onight.zjfae.afront.gens.AcquireBankSmsCheckCode4Recharge;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.ProductOrderPay;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.QueryUserAccountDetail;
import onight.zjfae.afront.gens.RechargeBankCardInfo;

/**
 * @Description:产品预约模块 长期预约 短期预约 支付预约金（presenter）
 * @Author: zhoujianyu
 * @Time: 2018/9/11 15:52
 */
public class ReservePayPresenter extends BasePresenter<ReservePayView> {

    public ReservePayPresenter(ReservePayView baseView) {
        super(baseView);
    }

    /**
     * 获取支付数据
     */
    public void loadData(String productCode, String delegateNum) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit, getRequestMap());

        QueryPayInit.REQ_PBIFE_trade_queryPayInit.Builder querInit = QueryPayInit.REQ_PBIFE_trade_queryPayInit.newBuilder();
        querInit.setProductCode(productCode);
        querInit.setDelegateNum(delegateNum);
        querInit.setOrderType("1");
        querInit.setPayType("orderPay");
        addDisposable(apiServer.queryPayInit(url, getBody(querInit.build().toByteArray())), new BaseProductPlayProtoBufObserver<QueryPayInit.Ret_PBIFE_trade_queryPayInit>(baseView) {
            @Override
            public void onSuccess(QueryPayInit.Ret_PBIFE_trade_queryPayInit ret_pbife_trade_queryPayInit) {
                baseView.playInit(ret_pbife_trade_queryPayInit);
            }


        });
    }

    //获取银行卡信息
    public void getBankInfo() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.BankInfo, getRequestMap());

        addDisposable(apiServer.getBankInfo(url), new ProtoBufObserver<PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard) {
                baseView.onBankInfo(bankCard);
            }


        });
    }

    /**
     * 查询是否需要短信验证码接口  只有银行卡参与支付的过程才需要调用
     */
    public void querySms() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetailQuerySms, getRequestMap());
        addDisposable(apiServer.querySms(url), new ProtoBufObserver<QueryUserAccountDetail.Ret_PBIFE_fund_queryUserAccountDetail>(baseView) {
            @Override
            public void onSuccess(QueryUserAccountDetail.Ret_PBIFE_fund_queryUserAccountDetail data) {
                baseView.querySms(data.getReturnCode(), data.getReturnMsg(), data.getData().getNeedSms());
            }

        });

    }

    /**
     * 发送验证码接口
     */
    public void sendSms(String payChannelNo, String bankCardNo, String amount) {
        AcquireBankSmsCheckCode4Recharge.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge.Builder builder = AcquireBankSmsCheckCode4Recharge.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge.newBuilder();
        builder.setValidateType("3");
        builder.setPayChannelNo(payChannelNo);
        builder.setBankCardNo(bankCardNo);
        builder.setAmount(amount);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetailSms, getRequestMap());
        addDisposable(apiServer.sendRechargeSms(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge>(baseView) {
            @Override
            public void onSuccess(AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge data) {
                baseView.sendCode(data.getReturnCode(), data.getReturnMsg(), data.getData().getSerialNo());
            }

        });
    }

    /**
     * 预约支付购买
     */
    public void playOrder(ReservePlayInfoBean playInfo) {
        ProductOrderPay.REQ_PBIFE_trade_productOrderPay.Builder builder = ProductOrderPay.REQ_PBIFE_trade_productOrderPay.newBuilder();
        builder.setOrderType("1");
        builder.setOrderBuyAmount(playInfo.getOrderBuyAmount());
        builder.setPassword(playInfo.getPassword());
        builder.setRepeatCommitCheckCode(playInfo.getRepeatCommitCheckCode());
        builder.setOrderProductCode(playInfo.getOrderProductCode());
        if (StringUtils.isNotBlank(playInfo.getCheckCode())) {
            builder.setCheckCode(playInfo.getCheckCode());
        }
        if (StringUtils.isNotBlank(playInfo.getCheckCodeSerialNo())) {
            builder.setCheckCodeSerialNo(playInfo.getCheckCodeSerialNo());
        }
        builder.setPayType(playInfo.getPayType());
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetailOrderPay, getRequestMap());
        addDisposable(apiServer.playOrder(url, getBody(builder.build().toByteArray())), new BaseProductPlayProtoBufObserver<ProductOrderPay.Ret_PBIFE_trade_productOrderPay>(baseView) {
            @Override
            public void onSuccess(ProductOrderPay.Ret_PBIFE_trade_productOrderPay playState) {
                baseView.playOrder(playState.getReturnCode(), playState.getReturnMsg(), playState.getData().getMsg());
            }

        });
    }

    //查询渠道关闭或查询银行卡信息
    private String payChannelNo = "";

    public void queryBankInfo() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadRechargeBankCardInfo, getRequestMap());
        addDisposable(apiServer.queryBankInfo(url), new ProtoBufObserver<RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo>(baseView) {
            @Override
            public void onSuccess(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
                payChannelNo = bankCardInfo.getData().getPayChannelNo();
                baseView.queryRechargeBankInfo(bankCardInfo);
            }
        });
    }

    /**
     * 查询银行限额信息
     */

    public void queryFundBankInfo(final String bankNo) {
        FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.Builder fundBank = FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.newBuilder();
        fundBank.setBankCode(bankNo);
        fundBank.setPayChannel(payChannelNo);
        fundBank.setTransType("1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryFundBankInfo, getRequestMap());
        addDisposable(apiServer.queryFundBankInfo(url, getBody(fundBank.build().toByteArray())), new ProtoBufObserver<FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo>(baseView) {
            @Override
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
                baseView.bankCardManage(fundBankInfo);
            }
        });
    }
}
