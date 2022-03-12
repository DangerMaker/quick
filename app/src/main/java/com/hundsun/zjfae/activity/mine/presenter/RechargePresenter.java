package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.RechargeView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;

import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.AcquireBankSmsCheckCode4Recharge;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gens.v2.PBIFEFundRecharge;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.v2.Notices;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.mine.presenter
 * @ClassName: RechargePresenter
 * @Description: 充值Presenter
 * @Author: moran
 * @CreateDate: 2019/6/13 18:17
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/13 18:17
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class RechargePresenter extends BasePresenter<RechargeView> {
    /**
     * 支付渠道
     */
    private String payChannelNo = "";
    /**
     * 重复提交码
     */
    private String repeatCommitCheckCode = "";
    private String bankCardNo = "";

    private String isRealInvestor = "";
    private static Pattern pattern = Pattern.compile("[^0-9.]");

    public RechargePresenter(RechargeView baseView) {
        super(baseView);
    }


    public void initUserDate_BankInfo() {
        Observable observable = Observable.mergeDelayError(queryBankInfo(), getUserInfo(), getUserAssets());

        addDisposable(observable, new BaseBankProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo) {
                    RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo bankCardInfo = (RechargeBankCardInfo.Ret_PBIFE_fund_loadRechargeBankCardInfo) o;
                    payChannelNo = bankCardInfo.getData().getPayChannelNo();
                    repeatCommitCheckCode = bankCardInfo.getData().getRepeatCommitCheckCode();
                    bankCardNo = bankCardInfo.getData().getBankCardNo();
                    baseView.onUserBankInfo(bankCardInfo);
                } else if (o instanceof UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo) {

                    baseView.onUserAssetsInfo((UserAssetsInfo.Ret_PBIFE_fund_loadUserAssetsInfo) o);

                } else if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {

                    UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o;
                    isRealInvestor = userDetailInfo.getData().getIsRealInvestor();
                    baseView.onUserInfo(userDetailInfo);
                }
            }
        });
    }

    /**
     * 000非合格投资者弹框 004恭喜弹框 010伪合格投资者 isShow 是否是线上 1线下 else 线上
     *
     * @param amount     总余额
     * @param accredited 合格投资者差额
     */
    public void notice(String amount, String accredited) {
        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setType("000");

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(BasePresenter.AZJ, BasePresenter.PBAFT, BasePresenter.VAFTAZJ, ConstantName.Notice, map);

        addDisposable(apiServer.notice(url, BasePresenter.getBody(notice.build().toByteArray())), new BaseObserver<Notices.Ret_PBAPP_notice>(baseView) {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice notice) {

                String isShow = notice.getData().getNotice().getIsShow();

                baseView.onRechargeChannelsStatus(!isShow.equals("1") , amount, accredited);

            }
        });
    }


    /**
     * 查询银行卡信息
     */
    private Observable queryBankInfo() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.LoadRechargeBankCardInfo, getRequestMap());
        return apiServer.queryBankInfo(url);
    }


    /**
     * 查询用户资产
     */
    private Observable getUserAssets() {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.TotalIncome, getRequestMap());
        return apiServer.getData(url);
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
            public void onSuccess(FundBankInfo.Ret_PBIFE_bankcardmanage_queryFundBankInfo ret_pbife_bankcardmanage_queryFundBankInfo) {
                baseView.bankCardManage(ret_pbife_bankcardmanage_queryFundBankInfo);
            }
        });
    }


    /**
     * 发送短信验证码
     *
     * @param amount 充值金额
     */
    public void sendRechargeSms(String amount) {
        AcquireBankSmsCheckCode4Recharge.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge.Builder builder =
                AcquireBankSmsCheckCode4Recharge.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge.newBuilder();
        builder.setAmount(amount);
        builder.setBankCardNo(bankCardNo);
        builder.setPayChannelNo(payChannelNo);
        builder.setValidateType("3");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.SENDSMS, getRequestMap());


        addDisposable(apiServer.sendRechargeSms(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge>(baseView) {
            @Override
            public void onSuccess(AcquireBankSmsCheckCode4Recharge.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4recharge sendSms) {
                baseView.sendSmsState(sendSms);
            }
        });
    }


    /**
     * 充值
     */
    public void fundRecharge(String amount, String checkCode, String password, String checkCodeSerialNo) {


        amount = pattern.matcher(amount).replaceAll("").trim();

        if (amount.indexOf(".") > 0) {
            //去掉多余的0
            amount = amount.replaceAll("0+?$", "");
            //如最后一位是.则去掉
            amount = amount.replaceAll("[.]$", "");
        }


        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.FundRecharge, getRequestMap());
        PBIFEFundRecharge.REQ_PBIFE_fund_recharge.Builder rechargePlay = PBIFEFundRecharge.REQ_PBIFE_fund_recharge.newBuilder();
        rechargePlay.setAmount(amount);
        rechargePlay.setCheckCode(checkCode);
        rechargePlay.setPassword(password);
        rechargePlay.setPayChannelNo(payChannelNo);
        rechargePlay.setRepeatCommitCheckCode(repeatCommitCheckCode);
        rechargePlay.setCheckCodeSerialNo(checkCodeSerialNo);

        addDisposable(apiServer.fundRecharge(url, getBody(rechargePlay.build().toByteArray())), new BaseBankProtoBufObserver<PBIFEFundRecharge.Ret_PBIFE_fund_recharge>(baseView, "银行处理中...") {
            @Override
            public void onSuccess(PBIFEFundRecharge.Ret_PBIFE_fund_recharge ret_pbife_fund_recharge) {
                baseView.onFundRecharge(ret_pbife_fund_recharge);
            }
        });

    }


    public String getIsRealInvestor() {
        return isRealInvestor;
    }
}
