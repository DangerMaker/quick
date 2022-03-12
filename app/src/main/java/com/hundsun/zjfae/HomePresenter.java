package com.hundsun.zjfae;

import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.cache.app.AppCache;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.AppUpDateObserver;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserLevelShowTimeSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.zjfae.jpush.JPush;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import onight.zjfae.afront.gens.LoginOut;
import onight.zjfae.afront.gens.PBIFERegAddUsersRegisterPrivacyAgree;
import onight.zjfae.afront.gens.PBIFEUserbaseinfoGetFunctionMenus;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.PBAPPAndNativeWhiteScreen;
import onight.zjfae.afront.gensazj.PrivateNotice;
import onight.zjfae.afront.gensazj.StatisticsStepsPB;
import onight.zjfae.afront.gensazj.v2.Login;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae
 * @ClassName: HomePresenter
 * @Description: HomeActivity_Presenter
 * @Author: moran
 * @CreateDate: 2019/6/17 13:59
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/17 13:59
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class HomePresenter extends BasePresenter<HomeView> {


    public HomePresenter(HomeView baseView) {
        super(baseView);
    }

    /**
     * 获取头像接口
     */
    public void getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());

        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("userHeader.urlPrefix");
        diction.addKeyNo("userHeader.urlSuffix");

        addDisposable(apiServer.getDictionary(url, getBody(diction.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(baseView) {
            @Override
            public void onSuccess(Dictionary.Ret_PBAPP_dictionary dictionary) {
                List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                baseView.getPicDictionary(parms.get(0).getKeyCode(), parms.get(1).getKeyCode());
            }

        });

    }

    /**
     * 获取协议版本号
     */
    public void getAgreementVersion() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Dictionary, getRequestMap());

        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("privacy.agreement.version");

        addDisposable(apiServer.getDictionary(url, getBody(diction.build().toByteArray())), new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(baseView) {
            @Override
            public void onSuccess(Dictionary.Ret_PBAPP_dictionary dictionary) {
                List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                baseView.getAgreementVersion(parms.get(0).getKeyCode());
            }

        });

    }

    /**
     * 退出登录
     */
    public void outLogin() {
        String url = parseUrl(MZJ, PBMOO, VMOOMZJ, ConstantName.LoginOut, getRequestMap());

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
     * 申请合格投资者失败原因
     */
    public void getUserHighNetWorthInfo(final String isRealInvestor) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo, getRequestMap());
        UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.Builder builder =
                UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder();
        builder.setDynamicType1("highNetWorthUpload");

        addDisposable(apiServer.getUserHighNetWorthInfo(url, getBody(builder.build().toByteArray())), new BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo>(baseView) {

            @Override
            public void onSuccess(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo mClass) {
                baseView.getUserHighNetWorthInfo(mClass, isRealInvestor);
            }
        });
    }

    public void getUserInfoDetail() {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
                //用户信息
                baseView.getUserInfo(userDetailInfo);
            }
        });
    }


    /**
     * 查询app是否有更新
     */
    public void queryAppUpdate() {
        String url = parseUrl(AZJ, PBUPG, VUPGAZJ);
        CCLog.e("App更新Url", url);
        Map body = getRequestMap();
        body.put("platform", "1");
        body.put("versionid", APPVERSION);
        addDisposable(apiServer.appUpdate(url, parseBody(body)), new AppUpDateObserver<AppUpDate>(baseView) {
            @Override
            public void onSuccess(AppUpDate appUpDate) {

                baseView.onAppUpdate(appUpDate);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                baseView.onAppUpdateError();
            }
        });

    }


    /**
     * 登录
     */
    public void login(byte[] loginByte, final String loginMethod) {
        Map<String, String> map = getRequestMap();
        map.put("registrationId", JPush.getJPushRegistrationId(baseView.onAttach()));
        map.put("version", twoVersion);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.LOGIN_PBNAME, map);

        addDisposable(apiServer.login(url, getBody(loginByte)), new ProtoBufObserver<Login.Ret_PBAPP_login>(baseView) {
            @Override
            public void onSuccess(Login.Ret_PBAPP_login login) {
                onUserInfo(loginMethod, login.getData().getMobile());
            }
        });

    }

    /**
     * 上送用户确认隐私协议的状态及时间
     *
     * @return
     * @method
     * @date: 2020/8/13 15:22
     * @author: moran
     */
    public void onUpUserNoticeStatus(boolean isAuthorizePrivacy, String agreementVersion) {

        String status = "0";
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UP_USER_PRIVATE_NOTICE_STATUS, getRequestMap());
        PBIFERegAddUsersRegisterPrivacyAgree.REQ_PBIFE_reg_addUsersRegisterPrivacyAgree.Builder build = PBIFERegAddUsersRegisterPrivacyAgree.REQ_PBIFE_reg_addUsersRegisterPrivacyAgree.newBuilder();
        //表示同意上送
        if (isAuthorizePrivacy) {
            status = "1";
        }
        build.setIsAuthorizePrivacy(status);
        build.setPrivacyAgreementVersion(agreementVersion);
        build.setOperTime(Utils.getStringDate(System.currentTimeMillis()));

        addDisposable(apiServer.onUpUserNoticeStatus(url, getBody(build.build().toByteArray())), new ProtoBufObserver<PBIFERegAddUsersRegisterPrivacyAgree.Ret_PBIFE_reg_addUsersRegisterPrivacyAgree>() {

            @Override
            public void onSuccess(PBIFERegAddUsersRegisterPrivacyAgree.Ret_PBIFE_reg_addUsersRegisterPrivacyAgree usersRegisterPrivacyAgree) {
                CCLog.i("协议接口上送成功", "true");
            }
        });

    }

    public void onImageCode() {

        addDisposable(apiServer.downloadPicFromNet(BasePresenter.AccountImageCode), new BaseObserver<ResponseBody>(baseView) {
            @Override
            public void onSuccess(ResponseBody body) {
                try {
                    baseView.onImageCode(body.bytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传步数
     */
    public void upLoadSteps(String step, String stepTime) {
        StatisticsStepsPB.REQ_PBAPP_StatisticsSteps.Builder builder = StatisticsStepsPB.REQ_PBAPP_StatisticsSteps.newBuilder();
        StatisticsStepsPB.REQ_PBAPP_StatisticsSteps.StatisticsSteps.Builder statisticsStep = StatisticsStepsPB.REQ_PBAPP_StatisticsSteps.StatisticsSteps.newBuilder();
        if (StringUtils.isEmpty(UserInfoSharePre.getFundAccount())) {
            return;
        }
        statisticsStep.setFunAccount(UserInfoSharePre.getFundAccount());
        CCLog.e(UserInfoSharePre.getFundAccount());
        statisticsStep.setStep(step);
        CCLog.e(step);
        statisticsStep.setStepTime(stepTime);
        CCLog.e(stepTime);
        builder.addStatisticsSteps(statisticsStep);
        Map map = getRequestMap();
        map.put("version", twoVersion);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PBAPP_StatisticsSteps, map);
        addDisposable(apiServer.upLoadSteps(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<StatisticsStepsPB.Ret_PBAPP_StatisticsSteps>() {
            @Override
            public void onSuccess(StatisticsStepsPB.Ret_PBAPP_StatisticsSteps ret_pbapp_statisticsSteps) {
                CCLog.e("步数上传成功");
            }
        });


    }


    public void onUserInfo(final String loginMethod, final String mobile) {


        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfo_getUserDetailInfo) {
                UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userbaseinfo_getUserDetailInfo.getData();

                String brokerNo = userDetailInfo.getBrokerNo();

                //用户类型，personal(个人)，company(机构)
                String userType = userDetailInfo.getUserType();

                UserLevelShowTimeSharePre.saveUserAccount(userDetailInfo.getAccount());

                baseView.onLoginSuccess(userbaseinfo_getUserDetailInfo.getReturnMsg(), mobile, loginMethod, userDetailInfo.getFundAccount(), userType, brokerNo);
                //登录成功以后，异步获取用户WebView浏览器内核使用开关
                getWebViewType(userDetailInfo.getAccount());

                //onUpUserNoticeStatus();
            }
        });
    }

    public void getWebViewType(String account) {
        CCLog.e("TAGTAG", account);
        PBAPPAndNativeWhiteScreen.REQ_PBAPP_andNative_whiteScreen.Builder builder =
                PBAPPAndNativeWhiteScreen.REQ_PBAPP_andNative_whiteScreen.newBuilder();
        Map map = getRequestMap();
        map.put("version", twoVersion);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PBAPPAndNativeWhiteScreen, map);
        builder.setAccount(account);
        addDisposable(apiServer.getWebViewType(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<PBAPPAndNativeWhiteScreen.Ret_PBAPP_andNative_whiteScreen>(baseView) {
            @Override
            public void onSuccess(PBAPPAndNativeWhiteScreen.Ret_PBAPP_andNative_whiteScreen ret_pbapp_andNative_whiteScreen) {
                if (ret_pbapp_andNative_whiteScreen != null &&
                        ret_pbapp_andNative_whiteScreen.getReturnCode().equals(ConstantCode.RETURN_CODE)) {
                    UserInfoSharePre.setSkip(ret_pbapp_andNative_whiteScreen.getData().getWhiteScreen(0).getSkip());
                    UserInfoSharePre.setIsOpenChain(ret_pbapp_andNative_whiteScreen.getData().getWhiteScreenList().get(0).getIsOpenChain());

                }
            }

        });
    }


    public void getProductListTitle() {

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PRODUCT_NAME);


        addDisposable(apiServer.getProductListTitle(url), new ProtoBufObserver<PBIFEUserbaseinfoGetFunctionMenus.Ret_PBIFE_userbaseinfo_getFunctionMenus>() {
            @Override
            public void onSuccess(PBIFEUserbaseinfoGetFunctionMenus.Ret_PBIFE_userbaseinfo_getFunctionMenus ret_pbife_userbaseinfo_getFunctionMenus) {
                AppCache.INSTANCE.saveProductTitle(ret_pbife_userbaseinfo_getFunctionMenus.getData().getPurchaseZone());


            }
        });


    }

    /**
     * 获取用户隐私协议
     *
     * @param
     * @return
     * @method
     * @date: 2020/8/13 15:23
     * @author: moran
     */
    public void getUserPrivateNotice() {

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.USER_PRIVATE_NOTICE);

        addDisposable(apiServer.getUserPrivate(url), new ProtoBufObserver<PrivateNotice.Ret_PBAPP_privacyNotice>(baseView) {

            @Override
            public void onSuccess(PrivateNotice.Ret_PBAPP_privacyNotice ret_pbapp_privacyNotice) {

                PrivateNotice.PBAPP_privacyNotice.PrivacyNotice privacyNotice = ret_pbapp_privacyNotice.getData().getPrivacyNotice();

                baseView.onUserPrivateNotice(privacyNotice, ret_pbapp_privacyNotice.getData().getForce());

            }
        });

    }

}
