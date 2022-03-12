package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.view.ProductPlayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.ProductPlay;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;

public class ProductPlayPresenter extends BasePresenter<ProductPlayView> {


    /**
     * 支付渠道
     * */
    private String payChannelNo = "";

    /**
     * 用户是否绑卡
     * */
    private String  isBondedCard = "false";

    /**
     * 重复提交码
     * */
    private String repeatCommitCheckCode = "";



    public ProductPlayPresenter(ProductPlayView baseView) {
        super(baseView);
    }


    private String productCode, delegateNum;

    public void init(String productCode, String delegateNum, String serialNo) {

        this.productCode = productCode;
        this.delegateNum = delegateNum;

        Observable observable = Observable.mergeDelayError(getUserInfo(), queryPayInit(productCode, delegateNum), queryMyKqQuan(productCode, delegateNum, serialNo),getDictionary());

        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof QueryPayInit.Ret_PBIFE_trade_queryPayInit) {

                    QueryPayInit.Ret_PBIFE_trade_queryPayInit  queryPayInit = (QueryPayInit.Ret_PBIFE_trade_queryPayInit) o;

                    repeatCommitCheckCode = queryPayInit.getData().getPayInitWrap().getRepeatCommitCheckCode();

                    baseView.onPlayInit(queryPayInit);

                } else if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    //用户信息
                    UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o;
                    isBondedCard =  userDetailInfo.getData().getIsBondedCard();

                } else if (o instanceof QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan) {
                    baseView.onUserKquanInfo((QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan) o);

                }
                else if (o instanceof Dictionary.Ret_PBAPP_dictionary){

                    baseView.onKQDescription((Dictionary.Ret_PBAPP_dictionary) o);

                }
            }
        });
    }


    /***
     * 购买金额初始化
     * */
    private Observable queryPayInit(String productCode, String delegateNum) {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit);

        QueryPayInit.REQ_PBIFE_trade_queryPayInit.Builder querInit = QueryPayInit.REQ_PBIFE_trade_queryPayInit.newBuilder();
        querInit.setProductCode(productCode);
        querInit.setDelegateNum(delegateNum);
        querInit.setPayType("subscribePay");
        return apiServer.queryPayInit(url, getBody(querInit.build().toByteArray()));
    }


    /**
     * 查询卡券
     **/
    private Observable queryMyKqQuan(String productCode, String delegateNum, String serialNo) {
        QueryMyKqQuan.REQ_PBIFE_trade_queryMyKqQuan.Builder queryQuan = QueryMyKqQuan.REQ_PBIFE_trade_queryMyKqQuan.newBuilder();
        queryQuan.setProductCode(productCode);
        queryQuan.setDelegateNum(delegateNum);
        queryQuan.setSerialNo(serialNo);
        queryQuan.setBussType("01002");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryMyKqQuan, getRequestMap());
        return apiServer.queryMyKqQuan(url, getBody(queryQuan.build().toByteArray()));
    }


    /**
     * 购买购买
     */
    public void playProduct( ProductPlayBean playInfo) {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();
        ProductPlay.REQ_PBIFE_trade_rengou.Builder productPlay = ProductPlay.REQ_PBIFE_trade_rengou.newBuilder();

        String kqAddRatebj = "";
        String kqCode = "";
        String kqType = "";
        String kqValue = "";
        if (playBaoInfo != null) {
            kqAddRatebj = playInfo.getPlayBaoInfo().getKqAddRatebj();
            kqCode = playBaoInfo.allType(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), "id");
            kqType = playBaoInfo.allType(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), "type");
            kqValue = playBaoInfo.allValue(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), "value");
        }
        productPlay.setChannelNo(playInfo.getChannelNo());
        productPlay.setDelegateNum(playInfo.getPlayAmount());
        productPlay.setKqAddRatebj(kqAddRatebj);
        productPlay.setKqCode(kqCode);
        productPlay.setKqType(kqType);
        productPlay.setKqValue(kqValue);
        productPlay.setPassword(playInfo.getPlayPassWord());
        productPlay.setPayType(playInfo.getPayType());
        productPlay.setProductCode(playInfo.getProductCode());
        productPlay.setRepeatCommitCheckCode(repeatCommitCheckCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PlayProduct, getRequestMap());
        addDisposable(apiServer.playProduct(url, getBody(productPlay.build().toByteArray())), new BaseProductPlayProtoBufObserver<ProductPlay.Ret_PBIFE_trade_rengou>(baseView) {
            @Override
            public void onSuccess(ProductPlay.Ret_PBIFE_trade_rengou ret_pbife_trade_rengou) {
                baseView.playProduct(ret_pbife_trade_rengou.getReturnCode(), ret_pbife_trade_rengou.getReturnMsg());
            }

        });
    }


    /**
     * 查询渠道关闭或查询银行卡信息
     * */
    public void queryBankInfo() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadRechargeBankCardInfo, getRequestMap());
        addDisposable(apiServer.queryBankInfo(url), new BaseBankProtoBufObserver<RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo>(baseView) {
            @Override
            public void onSuccess(RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo) {
                payChannelNo = bankCardInfo.getData().getPayChannelNo();
                String bankName =  bankCardInfo.getData().getBankName();
                String bankCard = bankCardInfo.getData().getBankCardNo();
                String bankNo = bankCardInfo.getData().getBankNo();
                String showTips = bankCardInfo.getData().getShowTips();
                baseView.queryRechargeBankInfo(bankName,bankCard,bankNo,showTips);
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
        addDisposable(apiServer.queryFundBankInfo(url, getBody(fundBank.build().toByteArray())), new BaseBankProtoBufObserver<FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo>(baseView) {
            @Override
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
                baseView.bankCardManage(fundBankInfo);
            }
        });
    }


    /**
     * 余额不足时充值成功回调请求
     * */
    public void rechargePlayAmount() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit, getRequestMap());
        QueryPayInit.REQ_PBIFE_trade_queryPayInit.Builder querInit = QueryPayInit.REQ_PBIFE_trade_queryPayInit.newBuilder();
        querInit.setProductCode(productCode);
        querInit.setDelegateNum(delegateNum);
        querInit.setPayType("subscribePay");


        addDisposable(apiServer.queryPayInit(url, getBody(querInit.build().toByteArray())), new BaseProductPlayProtoBufObserver<QueryPayInit.Ret_PBIFE_trade_queryPayInit>(baseView) {
            @Override
            public void onSuccess(QueryPayInit.Ret_PBIFE_trade_queryPayInit ret_pbife_trade_queryPayInit) {

                baseView.rechargePlayAmount(ret_pbife_trade_queryPayInit);
            }
        });
    }


    /**
     * 卡券使用说明
     */
    private Observable getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("cardUse.rule");
        return apiServer.getDictionary(url,getBody(diction.build().toByteArray()));
    }


    public String getIsBondedCard() {
        return isBondedCard;
    }


    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }
}
