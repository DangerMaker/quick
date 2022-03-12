package com.hundsun.zjfae.activity.product.presenter;

import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.product.view.TransferDetailPlayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.AcquireBankSmsCheckCode4Recharge;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.QueryUserAccountDetail;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.v3.QuerySystemDict;
import onight.zjfae.afront.gens.v5.PBIFETradeTransferOrder;
import onight.zjfae.afront.gensazj.Dictionary;

public class TransferDetailPlayPresenter extends BasePresenter<TransferDetailPlayView> {




    /**
     * 重复提交码
     * */
    private String repeatCommitCheckCode = "";

    /**
     * 短信流水序列号
     * */
    private String serialNo = "";

    public TransferDetailPlayPresenter(TransferDetailPlayView baseView) {
        super(baseView);
    }


    public void init(String productCode, String delegateNum, String serialNo, String delegationCode) {
        delegateNum = delegateNum.replaceAll(",", "").trim();
        CCLog.e("productCode", productCode);
        CCLog.e("delegateNum", delegateNum);
        CCLog.e("serialNo", serialNo);
        CCLog.e("delegationCode", delegationCode);

        Observable observable = Observable.mergeArrayDelayError(queryPayInit(delegationCode, delegateNum),queryMyKqQuan(productCode, delegateNum, serialNo, delegationCode),initCaptcha(),getDictionary());

        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof QueryPayInit.Ret_PBIFE_trade_queryPayInit) {

                    QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit = (QueryPayInit.Ret_PBIFE_trade_queryPayInit) o;

                    repeatCommitCheckCode = queryPayInit.getData().getPayInitWrap().getRepeatCommitCheckCode();
                    baseView.onTransferPlayInit(queryPayInit);
                } else if (o instanceof QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan) {

                    baseView.onQuanInfo((QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan) o);
                }
                else if (o instanceof QuerySystemDict.Ret_PBIFE_query_querySystemDict){

                    QuerySystemDict.Ret_PBIFE_query_querySystemDict querySystemDict = (QuerySystemDict.Ret_PBIFE_query_querySystemDict) o;
                    //querySystemDict.getData().getSystemDict().
                    baseView.initCaptcha(querySystemDict.getData().getSystemDict().getParaValue(),querySystemDict.getData().getSenseFlag());
                }
                else if (o instanceof Dictionary.Ret_PBAPP_dictionary){

                    baseView.onKQDescription((Dictionary.Ret_PBAPP_dictionary) o);

                }
            }
        });
    }


    /***
     * 购买金额初始化及查询卡券
     * */
    private Observable queryPayInit(String delegationCode, String delegateNum) {
        QueryPayInit.REQ_PBIFE_trade_queryPayInit.Builder querInit = QueryPayInit.REQ_PBIFE_trade_queryPayInit.newBuilder();
        querInit.setProductCode(delegationCode);
        querInit.setDelegateNum(delegateNum);
        querInit.setOrderType("");
        querInit.setPayType("transferPay");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit, getRequestMap());
        return apiServer.queryPayInit(url, getBody(querInit.build().toByteArray()));

    }


    /**
     * 查询卡券
     **/
    private Observable queryMyKqQuan(String productCode, String delegateNum, String serialNo, String delegationCode) {
        CCLog.e("productCode", productCode);
        CCLog.e("delegateNum", delegateNum);
        CCLog.e("serialNo", serialNo);
        CCLog.e("delegationCode", delegationCode);

        QueryMyKqQuan.REQ_PBIFE_trade_queryMyKqQuan.Builder queryQuan = QueryMyKqQuan.REQ_PBIFE_trade_queryMyKqQuan.newBuilder();
        queryQuan.setProductCode(productCode);
        queryQuan.setDelegateNum(delegateNum);
        queryQuan.setSerialNo(serialNo);
        queryQuan.setBussType("01003");
        queryQuan.setKqType("F|L|R");
        queryQuan.setDelegationCode(delegationCode);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryMyKqQuan, getRequestMap());

        return apiServer.queryMyKqQuan(url, getBody(queryQuan.build().toByteArray()));

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


    /**
     * 查询银行卡信息
     **/
    private String payChannelNo = "";
    private String bankCardNo = "";
    String bankName = "";
    String needSms = "";

    public void queryUserBankCard() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.BankInfo, getRequestMap());

        addDisposable(apiServer.getBankInfo(url), new BaseBankProtoBufObserver<PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard) {

                List<PBIFEBankcardmanageQueryUserBankCard.PBIFE_bankcardmanage_queryUserBankCard.TcCustomerChannelList> lists = bankCard.getData().getTcCustomerChannelListList();

                if (!lists.isEmpty()) {
                    for (PBIFEBankcardmanageQueryUserBankCard.PBIFE_bankcardmanage_queryUserBankCard.TcCustomerChannelList channel : lists) {
                        bankName = channel.getBankName();
                        payChannelNo = channel.getPayChannelNo();
                        bankCardNo = channel.getBankCard();
                    }
                }
                //接着查询是否需要短信验证码
                querySms();
                baseView.onBankInfo(bankName);
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
                needSms = data.getData().getNeedSms();
                baseView.querySms(data.getReturnCode(), data.getReturnMsg(), data.getData().getNeedSms());
            }

        });

    }

    /**
     * 发送短信验证码
     * @param amount 当前充值金额
     * */
    public void sendSms(String amount) {
        AcquireBankSmsCheckCode4Recharge.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge.Builder builder = AcquireBankSmsCheckCode4Recharge.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge.newBuilder();
        builder.setValidateType("3");
        builder.setPayChannelNo(payChannelNo);
        builder.setBankCardNo(bankCardNo);
        builder.setAmount(amount.replace(",", ""));
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ReserveListDetailSms, getRequestMap());
        addDisposable(apiServer.sendRechargeSms(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge>(baseView) {
            @Override
            public void onSuccess(AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge data) {

                serialNo = data.getData().getSerialNo();
                baseView.sendCode(data.getReturnCode(), data.getReturnMsg());

            }


        });
    }

    /**
     * 转让购买
     */
    public void playTransferDetail(TransferDetailPlay playInfo) {
        PlayBaoInfo playBaoInfo = playInfo.getPlayBaoInfo();
        String kqCode = "";
        String kqType = "";
        String kqValue = "";
        if (playBaoInfo != null) {
            kqCode = playBaoInfo.allType(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), "id");
            kqType = playBaoInfo.allType(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), "type");
            kqValue = playBaoInfo.allValue(playBaoInfo.getPlayMap(), playBaoInfo.getPlayList(), "value");
        }
        PBIFETradeTransferOrder.REQ_PBIFE_trade_transferOrder.Builder transPlay = PBIFETradeTransferOrder.REQ_PBIFE_trade_transferOrder.newBuilder();
        transPlay.setChannelNo("12");
        transPlay.setBuyNum(playInfo.getPlayAmount());
        transPlay.setDelegationCode(playInfo.getDelegationCode());
        transPlay.setKqCode(kqCode);
        transPlay.setKqType(kqType);
        transPlay.setKqValue(kqValue);
        transPlay.setCheckCode(playInfo.getCheckCode());
        transPlay.setPassword(playInfo.getPlayPassWord());
        CCLog.e("playInfo.getPlayType()",playInfo.getPlayType());
        transPlay.setPayType(playInfo.getPlayType());
        transPlay.setRepeatCommitCheckCode(repeatCommitCheckCode);
        transPlay.setMobile(playInfo.getMobile());
        //短信流水号
        transPlay.setCheckCodeSerialNo(serialNo);
        if (StringUtils.isNotBlank(playInfo.getToken())) {
            //上送token
            transPlay.setToken(playInfo.getToken());
        }
        if (StringUtils.isNotBlank(playInfo.getAuthCode())) {
            //上送图形验证码
            transPlay.setAuthCode(playInfo.getAuthCode());
        }
        Map map = getRequestMap();
        map.put("version", "v4");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrder, map);
        addDisposable(apiServer.playTransferDetail(url, getBody(transPlay.build().toByteArray())), new BaseProductPlayProtoBufObserver<PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder>(baseView) {
            @Override
            public void onSuccess(PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder transferOrder) {
                baseView.playTransferDetail(transferOrder.getReturnCode(), transferOrder.getReturnMsg());
            }

        });

    }


    /**
     * 检测网易云盾
     * */
    private Observable initCaptcha() {
        QuerySystemDict.REQ_PBIFE_query_querySystemDict.Builder builder = QuerySystemDict.REQ_PBIFE_query_querySystemDict.newBuilder();
        builder.setParamCode("k7M5AzY0xhvBV+O3HMD+Dw==");
        builder.setVersion("1.0.1");
        Map map = getRequestMap();
        map.put("version",version);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QuerySystemDict, map);


        return apiServer.initCaptcha(url, getBody(builder.build().toByteArray()));
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

    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }
}
