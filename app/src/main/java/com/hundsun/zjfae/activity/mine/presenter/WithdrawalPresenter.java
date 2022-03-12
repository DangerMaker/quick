package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.WithdrawalView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;

import java.util.List;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.LoadWithDrawBankInfo;
import onight.zjfae.afront.gens.PBIFEBankcardmanageQueryUserBankCard;
import onight.zjfae.afront.gens.UserBankInfo;
import onight.zjfae.afront.gens.WithDraw;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.WithdrawalsCoupon;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.activity.mine.presenter
 * @ClassName:      WithdrawalPresenter
 * @Description:     提现Activity
 * @Author:         moran
 * @CreateDate:     2019/6/14 17:00
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/6/14 17:00
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class WithdrawalPresenter extends BasePresenter<WithdrawalView> {

    private String bankcardpic = "", postfix = "";

    private String repeatCommitCheckCode = "";


    //提现银行卡编号
    private String branchId = "";

    private String bankCode = "",branchNo = "",branchName = "";

    private LoadWithDrawBankInfo.Ret_PBIFE_fund_loadWithDrawBankInfo loadWithDrawBankInfo;

    private Dictionary.Ret_PBAPP_dictionary dictionary;



    public WithdrawalPresenter(WithdrawalView baseView) {
        super(baseView);
    }


    public void initData(String userType) {


        Observable observable = null;
        if (userType.equals("personal")){

            observable = Observable.mergeDelayError(getDictionary(),queryWithDrawBankInfo(),  getWithdrawalsCoupon(), queryUserBankCard());
        }

        else {

            observable = Observable.mergeDelayError(getDictionary(),queryWithDrawBankInfo(),  getWithdrawalsCoupon());
        }




        addDisposable(observable, new BaseBankProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof LoadWithDrawBankInfo.Ret_PBIFE_fund_loadWithDrawBankInfo) {
                    loadWithDrawBankInfo = (LoadWithDrawBankInfo.Ret_PBIFE_fund_loadWithDrawBankInfo) o;
                    repeatCommitCheckCode = loadWithDrawBankInfo.getData().getRepeatCommitCheckCode();
                    bankCode = loadWithDrawBankInfo.getData().getBankCode();
                    branchName = loadWithDrawBankInfo.getData().getBranchName();
                    branchNo = loadWithDrawBankInfo.getData().getBranchNo();
                    baseView.onWithDrawBankInfo(loadWithDrawBankInfo);

                } else if (o instanceof Dictionary.Ret_PBAPP_dictionary) {
                    dictionary = (Dictionary.Ret_PBAPP_dictionary) o;
                    List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                    bankcardpic = parms.get(0).getKeyCode();
                    postfix = parms.get(1).getKeyCode();

                }
                //可用卡券
                else if (o instanceof WithdrawalsCoupon.Ret_PBAPP_kqWithdrawals) {

                    WithdrawalsCoupon.Ret_PBAPP_kqWithdrawals   kqWithdrawals = (WithdrawalsCoupon.Ret_PBAPP_kqWithdrawals) o;

                    baseView.onWithdrawalsCoupon(kqWithdrawals);
                }
                //用户银行卡信息
                else if (o instanceof PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard) {
                    PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard   userBankCard = (PBIFEBankcardmanageQueryUserBankCard.Ret_PBIFE_bankcardmanage_queryUserBankCard) o;
                    if (!userBankCard.getData().getTcCustomerChannelListList().isEmpty()){
                        String bankName = "";
                        for (PBIFEBankcardmanageQueryUserBankCard.PBIFE_bankcardmanage_queryUserBankCard.TcCustomerChannelList channelList : userBankCard.getData().getTcCustomerChannelListList()) {
                            bankName = channelList.getBankName();
                        }
                        baseView.onUserBankInfo(bankName);
                    }



                }


            }
        });
    }


    //获取提现余额
    private Observable queryWithDrawBankInfo() {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadWithDrawBankInfo, getRequestMap());

        return apiServer.queryWithDrawBankInfo(url);


    }

    /**
     * 获取银行卡图片下载地址
     ***/
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
    public String downloadImage() {
        String url = bankcardpic + bankCode + postfix;
        return url;
    }


    /**
     * 获取银行渠道
     */
    private Observable queryUserBankCard() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QueryUserBankCard, getRequestMap());

        return apiServer.getBankInfo(url);

    }


    //机构用户银行卡信息
    public void onUserBankInfo(String branchId){

        this.branchId = branchId;

        UserBankInfo.REQ_PBIFE_bankcardmanage_queryUserBankInfo.Builder userBankInfo = UserBankInfo.REQ_PBIFE_bankcardmanage_queryUserBankInfo.newBuilder();

        userBankInfo.setBankcard(branchId);

        String url = parseUrl(MZJ,PBIFE,VREGMZJ,ConstantName.USER_BANK_INFO);

        addDisposable(apiServer.onUserBankInfo(url, getBody(userBankInfo.build().toByteArray())), new BaseBankProtoBufObserver<UserBankInfo.Ret_PBIFE_bankcardmanage_queryUserBankInfo>(baseView) {


            @Override
            public void onSuccess(UserBankInfo.Ret_PBIFE_bankcardmanage_queryUserBankInfo queryUserBankInfo) {

                branchNo = queryUserBankInfo.getData().getBranchNo();

                branchName = queryUserBankInfo.getData().getBranchName();

                baseView.onCompanyUserBankInfo(queryUserBankInfo.getData().getBranchName());

            }
        });
    }




    /**
     * 提现
     * @param amount 提现金额
     * @param password 交易密码
     **/
    public void withDrawal(String amount, String password) {
        amount = amount.replaceAll(",", "").trim();
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.WithDraw, getRequestMap());
        WithDraw.REQ_PBIFE_fund_withDraw.Builder withDraw = WithDraw.REQ_PBIFE_fund_withDraw.newBuilder();
        withDraw.setAmount(amount);
        withDraw.setBranchName(branchName);
        withDraw.setPassword(password);
        withDraw.setRepeatCommitCheckCode(repeatCommitCheckCode);
        withDraw.setBranchNo(branchNo);
        withDraw.setBankCard(branchId);

        addDisposable(apiServer.withDrawal(url, getBody(withDraw.build().toByteArray())), new BaseBankProtoBufObserver<WithDraw.Ret_PBIFE_fund_withDraw>(baseView, "银行处理中...") {
            @Override
            public void onSuccess(WithDraw.Ret_PBIFE_fund_withDraw fundWithDraw) {
                baseView.onWithDrawBean(fundWithDraw);
            }

        });
    }


    /***
     *卡券是否用
     *
     * */

    private Observable getWithdrawalsCoupon() {

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.kqWithdrawals, getRequestMap());

        WithdrawalsCoupon.REQ_PBAPP_kqWithdrawals.Builder builder = WithdrawalsCoupon.REQ_PBAPP_kqWithdrawals.newBuilder();

        builder.setIsavailablekq("3");

        return apiServer.getWithdrawalsCoupon(url, getBody(builder.build().toByteArray()));

    }


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBranchNo() {
        return branchNo;
    }

    public void setBranchNo(String branchNo) {
        this.branchNo = branchNo;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
