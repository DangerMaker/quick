package com.hundsun.zjfae.activity.mine.presenter;

import com.hundsun.zjfae.activity.mine.view.BankCardManagementView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseBankProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.zjfae.jpush.JPush;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.gens.CancelApplication;
import onight.zjfae.afront.gens.PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind;
import onight.zjfae.afront.gens.PBIFEUserinfomanageCheckTradePassword;
import onight.zjfae.afront.gens.UserChangeCardInfo;
import onight.zjfae.afront.gens.UserUnbindCardInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.v2.Notices;
import onight.zjfae.afront.gensazj.TencentFace;
import onight.zjfae.afront.gensazj.TencentFaceCallback;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.mine.presenter
 * @ClassName: BankCardManagementPresenter
 * @Description: 银行卡管理Presenter
 * @Author: moran
 * @CreateDate: 2019/6/13 19:37
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/13 19:37
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BankCardManagementPresenter extends BasePresenter<BankCardManagementView> {


    private boolean isTencentFace = true;

    public BankCardManagementPresenter(BankCardManagementView baseView) {
        super(baseView);
    }


    /**
     * 获取用户详细信息
     **/

    public void getUserDate() {

        Observable observable = Observable.mergeDelayError(getUserInfo(), queryFaceStatus());


        addDisposable(observable, new ProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object o) {
                if (o instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {
                    baseView.onUserInfo((UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) o);
                } else if (o instanceof Dictionary.Ret_PBAPP_dictionary) {
                    Dictionary.Ret_PBAPP_dictionary ret_pbapp_dictionary = (Dictionary.Ret_PBAPP_dictionary) o;
                    String keyCode = "";
                    List<Dictionary.PBAPP_dictionary.Parms> parmsList = ret_pbapp_dictionary.getData().getParmsList();

                    for (Dictionary.PBAPP_dictionary.Parms parms : parmsList) {
                        keyCode = parms.getKeyCode();
                    }

                    if (!keyCode.equals("") && keyCode.equals("tencentface")) {
                        isTencentFace = true;
                    } else {
                        isTencentFace = false;
                    }
                }
            }
        });
    }

    public boolean isTencentFace() {
        return isTencentFace;
    }

    /***
     * 查询解绑不通过原因
     * unbindBankCardUpload解绑失败原因
     * changeBankCardUpload换卡申请失败原因
     * type:区分解绑和换卡申请,0解绑卡，1申请换卡
     *
     * */
    public void getUserUnbindCardInfo() {
        UserUnbindCardInfo.REQ_PBIFE_bankcardmanage_getUserUnbindCardInfo.Builder builder =
                UserUnbindCardInfo.REQ_PBIFE_bankcardmanage_getUserUnbindCardInfo.newBuilder();
        builder.setDynamicType1("unbindBankCardUpload");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserUnbindCardInfo, getRequestMap());

        addDisposable(apiServer.getUserUnbindCardInfo(url, getBody(builder.build().toByteArray())), new BaseObserver<UserUnbindCardInfo.Ret_PBIFE_bankcardmanage_getUserUnbindCardInfo>(baseView) {
            @Override
            public void onSuccess(UserUnbindCardInfo.Ret_PBIFE_bankcardmanage_getUserUnbindCardInfo ret_pbife_bankcardmanage_getUserUnbindCardInfo) {
                baseView.onUserUnbindCardInfo(ret_pbife_bankcardmanage_getUserUnbindCardInfo);
            }
        });
    }


    //UserChangeCardInfo
    public void getUserChangeCardInfo() {
        UserChangeCardInfo.REQ_PBIFE_bankcardmanage_getUserChangeCardInfo.Builder builder = UserChangeCardInfo.REQ_PBIFE_bankcardmanage_getUserChangeCardInfo.newBuilder();
        builder.setDynamicType1("changeBankCardUpload");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserChangeCardInfo, getRequestMap());

        addDisposable(apiServer.getUserChangeCardInfo(url, getBody(builder.build().toByteArray())), new BaseObserver<UserChangeCardInfo.Ret_PBIFE_bankcardmanage_getUserChangeCardInfo>(baseView) {

            @Override
            public void onSuccess(UserChangeCardInfo.Ret_PBIFE_bankcardmanage_getUserChangeCardInfo changeCardInfo) {

                baseView.onUserChangeCardInfo(changeCardInfo);
            }
        });
    }


    /**
     * 取消解绑申请
     * 取消unbindCard解绑银行卡
     * 取消changeCard换卡申请
     * type:区分解绑和换卡申请,0解绑卡，1申请换卡
     */
    public void cleanUnbindBankCard(String cancelBusinessType, final String type) {
        CancelApplication.REQ_PBIFE_userinfomanage_cancelApplication.Builder builder =
                CancelApplication.REQ_PBIFE_userinfomanage_cancelApplication.newBuilder();
        builder.setCancelBusinessType(cancelBusinessType);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CancelApplication, getRequestMap());

        addDisposable(apiServer.cleanUnbindBankCard(url, getBody(builder.build().toByteArray())), new BaseBankProtoBufObserver<CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication>(baseView) {

            @Override
            public void onSuccess(CancelApplication.Ret_PBIFE_userinfomanage_cancelApplication ret_pbife_userinfomanage_cancelApplication) {

                if (ret_pbife_userinfomanage_cancelApplication.getReturnCode().equals("0000")) {
                    baseView.cleanUnbindCard(ret_pbife_userinfomanage_cancelApplication, type);
                }
            }
        });
    }


    /**
     * 判断是否签签署人脸识别协议
     **/

    public void queryFaceAgreement(String certificateCodeAll) {

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Notice, map);
        Notices.REQ_PBAPP_notice.Builder builder = Notices.REQ_PBAPP_notice.newBuilder();
        builder.setCertificateCode(certificateCodeAll);
        builder.setMobile(UserInfoSharePre.getUserName());
        builder.setType("003");

        addDisposable(apiServer.notice(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<Notices.Ret_PBAPP_notice>(baseView) {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice notice) {
                baseView.queryFaceAgreement(notice);
            }
        });

    }


    /**
     * 校检支付密码
     *
     * @param playPassWord 交易密码
     * @param smsCode
     * @param faceFlags    是否是人脸识别
     **/
    public void checkPlayPassWord(String playPassWord, String smsCode, final boolean faceFlags) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.CheckTradePassword, getRequestMap());

        PBIFEUserinfomanageCheckTradePassword.REQ_PBIFE_userinfomanage_checkTradePassword.Builder builder = PBIFEUserinfomanageCheckTradePassword.REQ_PBIFE_userinfomanage_checkTradePassword.newBuilder();
        builder.setPassword(playPassWord);
        if (!smsCode.equals("")) {
            builder.setCheckCode(smsCode);
            builder.setIsVerifyCheckCode("1");
        }

        addDisposable(apiServer.checkPlayPassWord(url, getBody(builder.build().toByteArray())), new BaseBankProtoBufObserver<PBIFEUserinfomanageCheckTradePassword.Ret_PBIFE_userinfomanage_checkTradePassword>(baseView) {
            @Override
            public void onSuccess(PBIFEUserinfomanageCheckTradePassword.Ret_PBIFE_userinfomanage_checkTradePassword ret_pbife_userinfomanage_checkTradePassword) {
                baseView.checkTradePassword(ret_pbife_userinfomanage_checkTradePassword, faceFlags);
            }
        });
    }

    public void onTencentFace(final String idCardName, final String idCard) {
        TencentFace.REQ_PBAPP_tencentface.Builder builder = TencentFace.REQ_PBAPP_tencentface.newBuilder();
        builder.setName(idCardName);
        builder.setIdNo(idCard);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.TENCENT_FACE);

        addDisposable(apiServer.onTencentFace(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<TencentFace.Ret_PBAPP_tencentface>(baseView) {
            @Override
            public void onSuccess(TencentFace.Ret_PBAPP_tencentface ret_pbapp_tencentface) {
                baseView.onTencentFace(ret_pbapp_tencentface);
            }
        });
    }


    public void onTencentFaceCallback(String orderNo, String verifyscene) {
        TencentFaceCallback.REQ_PBAPP_tencentface_callback.Builder builder = TencentFaceCallback.REQ_PBAPP_tencentface_callback.newBuilder();
        builder.setOrderNo(orderNo);
        builder.setSessionFlag("face");
        builder.setVerifyScene(verifyscene);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.TENCENT_CALLBACK);
        addDisposable(apiServer.onTencentFaceCallback(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<TencentFaceCallback.Ret_PBAPP_tencentface_callback>(baseView) {
            @Override
            public void onSuccess(TencentFaceCallback.Ret_PBAPP_tencentface_callback tencentfaceCallback) {

            }
        });
    }


    /**
     * -
     * 查询人脸识别状态
     ***/
    private Observable queryFaceStatus() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary);
        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("face.type");

        return apiServer.getDictionary(url, getBody(diction.build().toByteArray()));


//        addDisposable(apiServer.getDictionary(url, getBody(diction.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(  ) {
//            @Override
//            public void onSuccess(Dictionary.Ret_PBAPP_dictionary ret_pbapp_dictionary) {
//                String keyCode = "";
//                List<Dictionary.PBAPP_dictionary.Parms> parmsList = ret_pbapp_dictionary.getData().getParmsList();
//
//                for (Dictionary.PBAPP_dictionary.Parms parms :parmsList){
//                    keyCode = parms.getKeyCode();
//                }
//
//                if (!keyCode.equals("") && keyCode.equals("tencentface")){
//                    baseView.onFaceStatus(true);
//                }
//                else {
//                    baseView.onFaceStatus(false);
//                }
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                baseView.onFaceStatus(true);
//            }
//        });
    }

    /***
     * 解绑短信
     * */
    public void unBindBankCardSMS(String payChannelNo, String notInterceptor) {
        PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind.Builder builder = PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.REQ_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind.newBuilder();

        builder.setPayChannelNo(payChannelNo);
        builder.setNotInterceptor(notInterceptor);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.unBindCardSMS, getRequestMap());

        addDisposable(apiServer.unBindBankCardSMS(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind>(baseView) {
            @Override
            public void onSuccess(PBIFEBankcardmanageAcquireBankSmsCheckCode4UnBind.Ret_PBIFE_bankcardmanage_acquireBankSmsCheckCode4unBind bankSmsCheckCode) {

                if (bankSmsCheckCode.getReturnCode().equals("0000")) {
                    baseView.onDeleteBankSms();
                } else {
                    baseView.showError(bankSmsCheckCode.getReturnMsg());
                }


            }
        });


    }


}
