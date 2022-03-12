package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.BankCardView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.utils.CCLog;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.DeleteBankCard;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind;
import onight.zjfae.afront.gens.PBIFEBankcardmanageChangeBankCardPre;
import onight.zjfae.afront.gens.PBIFEBankcardmanageDeleteBankCardPrere;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.PBIFEBankcardmanageUnbindBankCardForUserOper;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;

public class BankCardPresenter extends BasePresenter<BankCardView> {

    private String bankcardpic, postfix;
    //短信验证码流水号
    private String checkCodeSerialNo = "";

    private String payChannelNo = "", bankName = "";
    public boolean isNeedIdcard = false;
    //用户信息
    private UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo;

    public BankCardPresenter(BankCardView baseView) {
        super(baseView);
    }


    public void initUserBankInfo() {

        Observable observable = Observable.concatArrayDelayError(getUserInfo(), getDictionary(), getBankInfo());

        addDisposable(observable, new BaseBankProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {

                if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o;
                } else if (o instanceof Dictionary.Ret_PBAPP_dictionary) {
                    Dictionary.Ret_PBAPP_dictionary dictionary = (Dictionary.Ret_PBAPP_dictionary) o;
                    List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                    bankcardpic = parms.get(0).getKeyCode();
                    postfix = parms.get(1).getKeyCode();
                } else if (o instanceof PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard) {
                    PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard bankCard = (PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard) o;
                    if (!bankCard.getData().getTcCustomerChannelListList().isEmpty()) {
                        payChannelNo = bankCard.getData().getTcCustomerChannelList(0).getPayChannelNo();
                        bankName = bankCard.getData().getTcCustomerChannelList(0).getBankName();
                        isNeedIdcard = bankCard.getData().getTcCustomerChannelList(0).getNeedIdCard().equals("true");

                    }
                    baseView.onUserBankInfo(userDetailInfo, bankCard, payChannelNo, bankName);

                }

            }
        });
    }


    //获取银行卡信息
    private Observable getBankInfo() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.BankInfo, getRequestMap());

        return apiServer.getBankInfo(url);

    }


    /**
     * 查询银行卡渠道信息，是否限额，关闭
     *
     * @param bankCode 银行编号
     */
    public void bankChannelInfo(String bankCode) {
        FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.Builder fundBank = FundBankInfo.REQ_PBIFE_bankcardmanage_queryFundBankInfo.newBuilder();
        fundBank.setBankCode(bankCode);
        fundBank.setTransType("0");

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryFundBankInfo, getRequestMap());

        addDisposable(apiServer.queryFundBankInfo(url, getBody(fundBank.build().toByteArray())), new BaseBankProtoBufObserver<FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo>(baseView) {
            @Override
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo fundBankInfo) {
                baseView.bankChannelInfo();
            }
        });


    }


    /**
     * 获取银行卡图片下载地址
     **/
    private Observable getDictionary() {

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());

        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("bankcardpic.prefix");
        diction.addKeyNo("bankcardpic.suffix");
        return apiServer.getDictionary(url, getBody(diction.build().toByteArray()));

    }


    /**
     * 下载银行卡icon
     **/
    public String downloadImage(String bankCode) {
        String url = bankcardpic + bankCode + postfix;
        CCLog.e("图片地址", url);
        return url;
    }


    /***
     * 解绑短信
     * */
    public void unBindBankCardSMS(String notInterceptor) {
        PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind.Builder builder = PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind.newBuilder();

        builder.setPayChannelNo(payChannelNo);
        builder.setNotInterceptor(notInterceptor);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.unBindCardSMS, getRequestMap());

        addDisposable(apiServer.unBindBankCardSMS(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind bankSmsCheckCode) {

                if (bankSmsCheckCode.getReturnCode().equals("0000")) {
                    checkCodeSerialNo = bankSmsCheckCode.getData().getSerialNo();
                    baseView.onDeleteBankSms();
                } else {
                    baseView.showError(bankSmsCheckCode.getReturnMsg());
                }


            }
        });


    }


    //解绑银行卡方式
    public void deleteBankCardPrere() {
        PBIFEBankcardmanageDeleteBankCardPrere.REQ_PBIFE_bankcardmanage_deleteBankCardPrere.Builder deleteBank = PBIFEBankcardmanageDeleteBankCardPrere.REQ_PBIFE_bankcardmanage_deleteBankCardPrere.newBuilder();
        deleteBank.setIsVerifyAmount("1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.DeleteBankCardPrere, getRequestMap());
        addDisposable(apiServer.deleteBankCardPrere(url, getBody(deleteBank.build().toByteArray())), new BaseBankProtoBufObserver<PBIFEBankcardmanageDeleteBankCardPrere.Ret_PBIFE_bankcardmanage_deleteBankCardPrere>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageDeleteBankCardPrere.Ret_PBIFE_bankcardmanage_deleteBankCardPrere deleteBankCardPre) {
                baseView.onDeleteBank();
            }
        });
    }

    /**
     * 申请换卡方式
     */
    public void changeBankCardPrere() {
        PBIFEBankcardmanageChangeBankCardPre.REQ_PBIFE_bankcardmanage_changeBankCardPre.Builder deleteBank = PBIFEBankcardmanageChangeBankCardPre.REQ_PBIFE_bankcardmanage_changeBankCardPre.newBuilder();
        deleteBank.setIsVerifyAmount("1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.ChangeBankCardPrere, getRequestMap());

        addDisposable(apiServer.changeBankCardPrere(url, getBody(deleteBank.build().toByteArray())), new BaseBankProtoBufObserver<PBIFEBankcardmanageChangeBankCardPre.Ret_PBIFE_bankcardmanage_changeBankCardPre>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageChangeBankCardPre.Ret_PBIFE_bankcardmanage_changeBankCardPre ret_pbife_bankcardmanage_changeBankCardPre) {

                baseView.onChangeBank();
            }
        });
    }


    /**
     * 解绑银行卡
     *
     * @param passWord 交易密码
     * @param smsCode  短信code码，可为空
     */
    public void deleteBankCard(String passWord, final String smsCode) {
        DeleteBankCard.REQ_PBIFE_bankcardmanage_deleteBankCard.Builder deleteBank = DeleteBankCard.REQ_PBIFE_bankcardmanage_deleteBankCard.newBuilder();
        deleteBank.setCheckCodeSerialNo(checkCodeSerialNo);
        deleteBank.setPassword(passWord);
        deleteBank.setPayChannelNo(payChannelNo);
        deleteBank.setCheckCode(smsCode);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.DeleteBankCard, getRequestMap());
        addDisposable(apiServer.deleteBankCard(url, getBody(deleteBank.build().toByteArray())), new BaseBankProtoBufObserver<DeleteBankCard.Ret_PBIFE_bankcardmanage_deleteBankCard>(baseView, "银行处理中...") {
            @Override
            public void onSuccess(DeleteBankCard.Ret_PBIFE_bankcardmanage_deleteBankCard deleteBankCard) {
                baseView.onDeleteBankCardBean(deleteBankCard);
            }

        });

    }
    /**
     * 强制解绑银行卡
     *
     * @param passWord 交易密码
     * @param smsCode  短信code码，可为空
     */
    public void deleteBankCardForUserOper(String passWord, final String smsCode) {
        PBIFEBankcardmanageUnbindBankCardForUserOper.REQ_PBIFE_bankcardmanage_unbindBankCardForUserOper.Builder deleteBank = PBIFEBankcardmanageUnbindBankCardForUserOper.REQ_PBIFE_bankcardmanage_unbindBankCardForUserOper.newBuilder();
        deleteBank.setCheckCodeSerialNo(checkCodeSerialNo);
        deleteBank.setPassword(passWord);
        deleteBank.setPayChannelNo(payChannelNo);
        deleteBank.setCheckCode(smsCode);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.DeleteBankCardForUserOper, getRequestMap());
        addDisposable(apiServer.deleteBankCardForUserOper(url, getBody(deleteBank.build().toByteArray())), new BaseBankProtoBufObserver<PBIFEBankcardmanageUnbindBankCardForUserOper.Ret_PBIFE_bankcardmanage_unbindBankCardForUserOper>(baseView, "银行处理中...") {
            @Override
            public void onSuccess(PBIFEBankcardmanageUnbindBankCardForUserOper.Ret_PBIFE_bankcardmanage_unbindBankCardForUserOper deleteBankCard) {
                baseView.onDeleteBankCardForUserOper(deleteBankCard);
            }

        });

    }


}
