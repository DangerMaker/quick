package com.hundsun.zjfae.fragment.account;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.utils.CCLog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.FundBankInfo;
import onight.zjfae.afront.gens.LoginOut;
import onight.zjfae.afront.gens.RechargeBankCardInfo;
import onight.zjfae.afront.gens.UserAssetsInfo;
import onight.zjfae.afront.gens.v2.CountMessage;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.MyDiscountCount;
import onight.zjfae.afront.gensazj.UserAppList;

/**
  *  @ProjectName:
  * @Package:        com.hundsun.zjfae.fragment.account
  * @ClassName:      AccountPresenter
  * @Description:     账户中心Presenter
  * @Author:         moran
  * @CreateDate:     2019/6/13 13:33
  * @UpdateUser:     更新者：
  * @UpdateDate:     2019/6/13 13:33
  * @UpdateRemark:   更新说明：
  * @Version:        1.0
 */
public class AccountPresenter extends BasePresenter<AccountView> {

    /**
     * 支付渠道
     * */
    private String payChannelNo = "";

    private String certificateStatusType = "";

    /**
     * 是否实名
     * **/
    private String verifyName;

    public AccountPresenter(AccountView baseView) {
        super(baseView);
    }

    /**
     * 退出登录
     * */
    public void outLogin() {
        String url = parseUrl(MZJ, PBMOO, VMOOMZJ, ConstantName.LoginOut);

        LoginOut.REQ_PBIFE_logout.Builder loginOut = LoginOut.REQ_PBIFE_logout.newBuilder();

        addDisposable(apiServer.outLogin(url, getBody(loginOut.build().toByteArray())), new ProtoBufObserver<LoginOut.Ret_PBIFE_logout>(baseView) {
            @Override
            public void onSuccess(LoginOut.Ret_PBIFE_logout ret_pbife_logout) {
                baseView.outLogin();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                baseView.outLogin();
            }
        });


    }


    /**
     * 聚合接口
     * */
    public void queryAccount() {

        String url = parseUrl(AZJ, PBMER, VMERAZJ);
        AllAzjProto.PWMergeProxy.Builder pwmerge = AllAzjProto.PWMergeProxy.newBuilder();
        AllAzjProto.PBFramePacket.Builder userInfoPacket = AllAzjProto.PBFramePacket.newBuilder();
        userInfoPacket.setPbname(ConstantName.TotalIncome);
        userInfoPacket.setPackId("0");
        UserAssetsInfo.REQ_PBIFE_fund_loadUserAssetsInfo.Builder userInfo = UserAssetsInfo.REQ_PBIFE_fund_loadUserAssetsInfo.newBuilder();
        userInfoPacket.setPbbody(userInfo.getDefaultInstanceForType().toByteString());
        pwmerge.addPackets(userInfoPacket);

        AllAzjProto.PBFramePacket.Builder messagePacket = AllAzjProto.PBFramePacket.newBuilder();
        messagePacket.setPbname(ConstantName.CountMessage);
        messagePacket.setJsbody("1.0.0");
        messagePacket.setVersion("v2");
        messagePacket.setPackId("1");
        CountMessage.REQ_PBIFE_messagemanage_countMessage.Builder message = CountMessage.REQ_PBIFE_messagemanage_countMessage.newBuilder();
        messagePacket.setPbbody(message.getDefaultInstanceForType().toByteString());
        pwmerge.addPackets(messagePacket);


        AllAzjProto.PBFramePacket.Builder userDetailInfoPacket = AllAzjProto.PBFramePacket.newBuilder();
        UserDetailInfo.REQ_PBIFE_userbaseinfo_getUserDetailInfo.Builder info = UserDetailInfo.REQ_PBIFE_userbaseinfo_getUserDetailInfo.newBuilder();

        userDetailInfoPacket.setPbname(ConstantName.USERBASEINFO);
        userDetailInfoPacket.setPackId("2");
        userDetailInfoPacket.setVersion("v3");
        userDetailInfoPacket.setJsbody("1.0.1");
        userDetailInfoPacket.setPbbody(info.getDefaultInstanceForType().toByteString());
        pwmerge.addPackets(userDetailInfoPacket);

        AllAzjProto.PBFramePacket.Builder myDiscountCount = AllAzjProto.PBFramePacket.newBuilder();
        MyDiscountCount.REQ_PBIFE_kq_getMyDiscountCount.Builder msgCount = MyDiscountCount.REQ_PBIFE_kq_getMyDiscountCount.newBuilder();

        myDiscountCount.setPbname(ConstantName.MyDiscountCount);
        myDiscountCount.setPackId("3");
        myDiscountCount.setPbbody(msgCount.getDefaultInstanceForType().toByteString());
        pwmerge.addPackets(myDiscountCount);


        Observable observable = Observable.mergeArrayDelayError(apiServer.queryAccount(url, getBody(pwmerge.build().toByteArray())),queryFaceStatus());


        addDisposable(observable, new BaseObserver() {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof AllAzjProto.PWRetMerges){
                    baseView.pWRetMerges((AllAzjProto.PWRetMerges) o);
                }
                else if (o instanceof Dictionary.Ret_PBAPP_dictionary){

                    String keyCode = "";
                    List<Dictionary.PBAPP_dictionary.Parms> parmsList = ((Dictionary.Ret_PBAPP_dictionary)o).getData().getParmsList();

                    for (Dictionary.PBAPP_dictionary.Parms parms :parmsList){
                        keyCode = parms.getKeyCode();
                    }

                    if (!keyCode.equals("") && keyCode.equals("tencentface")){
                        baseView.onFaceStatus(true);
                    }
                    else {
                        baseView.onFaceStatus(false);
                    }
                }
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
     * 我的邀请点击
     * */
    public void myInvitationClick() {
        AllAzjProto.PEAGetUrl.Builder builder = AllAzjProto.PEAGetUrl.newBuilder();
        builder.setKeyNo("myinvite");
        String url = parseUrl(AZJ, PBURL, VURLAZJ, getRequestMap());
        addDisposable(apiServer.getFeedbackUrl(url, getBody(builder.build().toByteArray())), new BaseObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof AllAzjProto.PEARetUrl) {
                    //用户消息
                    AllAzjProto.PEARetUrl retUrl = (AllAzjProto.PEARetUrl) o;
                    if (retUrl.getUrlsList().size() > 0) {
                        baseView.myInvitation(retUrl.getUrlsList().get(0).getBackendUrl(), retUrl.getUrlsList().get(0).getIsShare());
                    } else {
                        baseView.myInvitation("", "");
                    }
                }
            }
        });
    }


    /**
     * 查询人脸识别状态
     ***/
    public Observable queryFaceStatus(){
        String url = parseUrl(AZJ,PBAFT,VAFTAZJ, ConstantName.Dictionary);
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("face.type");

        return apiServer.getDictionary(url, getBody(diction.build().toByteArray()));
    }


    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }


    public void setCertificateStatusType(String certificateStatusType) {
        this.certificateStatusType = certificateStatusType;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public String getCertificateStatusType() {
        return certificateStatusType;
    }



}
