package com.hundsun.zjfae.activity.productreserve.presenter;

import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.ProductPlayBean;
import com.hundsun.zjfae.activity.product.bean.TransferDetailPlay;
import com.hundsun.zjfae.activity.productreserve.bean.ReserveProductPlay;
import com.hundsun.zjfae.activity.productreserve.view.ReserveProductPlayView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseProductPlayProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.AcquireBankSmsCheckCode4Recharge;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.ProductPlay;
import onight.zjfae.afront.gens.QueryMyKqQuan;
import onight.zjfae.afront.gens.QueryPayInit;
import onight.zjfae.afront.gens.QueryUserAccountDetail;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v5.PBIFETradeTransferOrder;
import onight.zjfae.afront.gensazj.Dictionary;

public class ReserveProductPlayPresenter extends BasePresenter<ReserveProductPlayView> {


    private QueryPayInit.Ret_PBIFE_trade_queryPayInit queryPayInit;//卡券金额初始化

    private QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan queryMyKqQuan;//卡券信息

    //重复提交码
    private String repeatCommitCheckCode = "";

    /**
     * 短信流水序列号
     */
    private String serialNo = "";

    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;


    public ReserveProductPlayPresenter(ReserveProductPlayView baseView) {
        super(baseView);
    }


    private String productCode, delegateNum;

    public void init(String productCode, String delegateNum, String serialNo) {

        this.productCode = productCode;
        this.delegateNum = delegateNum;

        Observable observable = Observable.concat(getUserInfo(), queryMyKqQuan(productCode, delegateNum, serialNo), queryPayInit(productCode, delegateNum), getDictionary());


        addDisposable(observable, new BaseProductPlayProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof QueryPayInit.Ret_PBIFE_trade_queryPayInit) {
                    queryPayInit = (QueryPayInit.Ret_PBIFE_trade_queryPayInit) o;
                    repeatCommitCheckCode = queryPayInit.getData().getPayInitWrap().getRepeatCommitCheckCode();
                    baseView.init(queryPayInit, userDetailInfo, queryMyKqQuan);
                } else if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    //用户信息
                    userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o;
                } else if (o instanceof QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan) {
                    queryMyKqQuan = (QueryMyKqQuan.Ret_PBIFE_trade_queryMyKqQuan) o;
                } else if (o instanceof Dictionary.Ret_PBAPP_dictionary) {

                    baseView.onKQDescription((Dictionary.Ret_PBAPP_dictionary) o);

                }
            }
        });
    }


    /***
     * 购买金额初始化及查询卡券
     * */
    private Observable queryPayInit(String productCode, String delegateNum) {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryPayInit, getRequestMap());

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
    public void playProduct(ReserveProductPlay playInfo) {
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
//        productPlay.setPayType(playInfo.getPayType());
        productPlay.setProductCode(playInfo.getProductCode());
        productPlay.setRepeatCommitCheckCode(repeatCommitCheckCode);

        productPlay.setPayType(playInfo.getPlayType());
        if (StringUtils.isNotBlank(playInfo.getCheckCode())) {
            productPlay.setCheckCode(playInfo.getCheckCode());
        }
        if (StringUtils.isNotBlank(playInfo.getCheckCodeSerialNo())) {
            productPlay.setCheckCodeSerialNo(playInfo.getCheckCodeSerialNo());
        }
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PlayProduct, getRequestMap());
        addDisposable(apiServer.playProduct(url, getBody(productPlay.build().toByteArray())), new BaseProductPlayProtoBufObserver<ProductPlay.Ret_PBIFE_trade_rengou>(baseView) {
            @Override
            public void onSuccess(ProductPlay.Ret_PBIFE_trade_rengou ret_pbife_trade_rengou) {
                baseView.playProduct(ret_pbife_trade_rengou.getReturnCode(), ret_pbife_trade_rengou.getReturnMsg());
            }
        });
    }

    /**
     * 转让购买
     */
    public void playTransferDetail(ProductPlayBean playInfo) {
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
        CCLog.e("playInfo.getPlayPassWord()", playInfo.getPlayPassWord());
        transPlay.setPayType(playInfo.getPlayType());
        transPlay.setRepeatCommitCheckCode(repeatCommitCheckCode);
        transPlay.setMobile(UserInfoSharePre.getMobile());
        //短信流水号
        transPlay.setCheckCodeSerialNo(serialNo);
        transPlay.setIgnoreCaptchaVerifier("subscribePaySPV");
//        if (StringUtils.isNotBlank(playInfo.getToken())) {
//            //上送token
//            transPlay.setToken(playInfo.getToken());
//        }
//        if (StringUtils.isNotBlank(playInfo.getAuthCode())) {
//            //上送图形验证码
//            transPlay.setAuthCode(playInfo.getAuthCode());
//        }
        Map map = getRequestMap();
        map.put("version", "v5");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TransferOrder, map);
        addDisposable(apiServer.playTransferDetail(url, getBody(transPlay.build().toByteArray())), new BaseProductPlayProtoBufObserver<PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder>(baseView) {
            @Override
            public void onSuccess(PBIFETradeTransferOrder.Ret_PBIFE_trade_transferOrder transferOrder) {
                baseView.playTransferDetail(transferOrder.getReturnCode(), transferOrder.getReturnMsg());
            }

        });

    }

    /**
     * 查询银行卡信息
     **/
    private String payChannelNo = "";//支付渠道
    private String bankCardNo = "";
    String bankName = "";
    String needSms = "";

    public void queryUserBankCard() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.BankInfo, getRequestMap());

        addDisposable(apiServer.getBankInfo(url), new ProtoBufObserver<PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard>(baseView) {
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
                querySms();//接着查询是否需要短信验证码
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

    //amount为当前需要充值的金额
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
                baseView.sendCode(data.getReturnCode(), data.getReturnMsg(), data.getData().getSerialNo());
            }

        });
    }
    //查询渠道关闭或查询银行卡信息

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

    /**
     * 卡券使用说明
     */
    private Observable getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("cardUse.rule");
        return apiServer.getDictionary(url, getBody(diction.build().toByteArray()));
    }

}
