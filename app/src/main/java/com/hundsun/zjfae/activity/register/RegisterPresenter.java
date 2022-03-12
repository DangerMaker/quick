package com.hundsun.zjfae.activity.register;

import android.os.Build;

import com.google.gson.Gson;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.fragment.home.bean.ADUtils;
import com.zjfae.jpush.JPush;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.gens.CheckIfMobileUseful;
import onight.zjfae.afront.gens.RegistrationSMS;
import onight.zjfae.afront.gens.v3.QuerySystemDict;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.v2.Login;

public class RegisterPresenter extends BasePresenter<RegisterView> {


    public RegisterPresenter(RegisterView baseView) {
        super(baseView);
    }


    /**
     * 检测手机号是否被注册
     */
    @Deprecated
    public void checkMobile(String phoneNumber) {

        CheckIfMobileUseful.REQ_PBIFE_reg_checkIfMobileUseful.Builder mobile =
                CheckIfMobileUseful.REQ_PBIFE_reg_checkIfMobileUseful.newBuilder();
        mobile.setMobile(phoneNumber);
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.PHONE_IS_REGISTER_PBNAME, getRequestMap());

        addDisposable(apiServer.checkMobile(url, getBody(mobile.build().toByteArray())), new ProtoBufObserver<CheckIfMobileUseful.Ret_PBIFE_reg_checkIfMobileUseful>(baseView) {
            @Override
            public void onSuccess(CheckIfMobileUseful.Ret_PBIFE_reg_checkIfMobileUseful ret_pbife_reg_checkIfMobileUseful) {
            }

        });
    }


    /**
     * 获取短信验证码
     */

    public void mobileNumberCode(final String mobileNumber, String authCode, String token) {

        RegistrationSMS.REQ_PBIFE_smsvalidatecode_activityRegistration.Builder sms =
                RegistrationSMS.REQ_PBIFE_smsvalidatecode_activityRegistration.newBuilder();
        sms.setMobile(mobileNumber);
        if (!authCode.equals("")) {
            sms.setAuthCode(authCode);
        } else {
            sms.setToken(token);
        }

        String url = parseUrl(MZJ, PBIFE, VIFEMZJ, ConstantName.PHONE_REGISTER_NUMBER);

        addDisposable(apiServer.mobileNumberCode(url, getBody(sms.build().toByteArray())), new ProtoBufObserver<RegistrationSMS.Ret_PBIFE_smsvalidatecode_activityRegistration>(baseView) {
            @Override
            public void onSuccess(RegistrationSMS.Ret_PBIFE_smsvalidatecode_activityRegistration registration) {
                baseView.mobileSMSCode(registration.getReturnCode(), registration.getReturnMsg());
            }


        });

    }


    /**
     * 注册
     **/
    public void register(byte[] body) {
        Map map = getRequestMap();
        map.put("registrationId", JPush.getJPushRegistrationId(baseView.onAttach()));
        map.put("deviceNo", Build.MODEL);

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.REGISTER_PBNAME, map);

        addDisposable(apiServer.register(url, getBody(body)), new ProtoBufObserver<Login.Ret_PBAPP_login>(baseView) {
            @Override
            public void onSuccess(Login.Ret_PBAPP_login ret_pbife_reg_register_new) {
                baseView.registerState(ret_pbife_reg_register_new.getReturnCode(), ret_pbife_reg_register_new.getReturnMsg(),ret_pbife_reg_register_new.getData().getMobile());
            }

        });

    }


    //检测网易云盾
    public void initCaptcha() {
        QuerySystemDict.REQ_PBIFE_query_querySystemDict.Builder builder = QuerySystemDict.REQ_PBIFE_query_querySystemDict.newBuilder();
        builder.setParamCode("k7M5AzY0xhvBV+O3HMD+Dw==");
        builder.setVersion("1.0.1");

        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.QuerySystemDict, getRequestMap());

        addDisposable(apiServer.initCaptcha(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<QuerySystemDict.Ret_PBIFE_query_querySystemDict>(baseView) {
            @Override
            public void onSuccess(QuerySystemDict.Ret_PBIFE_query_querySystemDict ret_pbife_query_querySystemDict) {
                baseView.initCaptcha(ret_pbife_query_querySystemDict.getData().getSystemDict().getParaValue());
            }
        });
    }


    public void onUserInfo(final String mobile){

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

                if (!UserInfoSharePre.getIconsUserType().equals(userDetailInfo.getData().getUserType())) {
                    updateUserIcon();
                    financeSection();
                }
                final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfoData = userDetailInfo.getData();
                UserInfoSharePre.setTradeAccount(userDetailInfoData.getTradeAccount());
                UserInfoSharePre.setAccount(userDetailInfoData.getAccount());
                UserInfoSharePre.setFundAccount(userDetailInfoData.getFundAccount());
                UserInfoSharePre.setBrokerNo(userDetailInfoData.getBrokerNo());
                UserInfoSharePre.saveUserType(userDetailInfoData.getUserType());

                baseView.onUserInfo(userDetailInfo,mobile);

            }

        });
    }


    private void updateUserIcon(){

        String priceUrl = parseUrl(AZJ,PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType("16");

        addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

            @Override
            public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {

                ADUtils adUtils = new ADUtils();
                Gson gson = new Gson();
                String result = "";

                List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> iconList =  ads_picture.getData().getAdsPictureList();


                List<BaseCacheBean> loginIconList = new ArrayList<>();

                if (iconList != null && !iconList.isEmpty()){


                    for (ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture picture : iconList){

                        BaseCacheBean baseCacheBean = new BaseCacheBean();
                        baseCacheBean.setJumpRule(picture.getJumpRule());
                        baseCacheBean.setUuid(picture.getUuid());
                        baseCacheBean.setShareUrl(picture.getShareUrl());
                        baseCacheBean.setIsShare(picture.getIsShare());
                        baseCacheBean.setFuncUrl(picture.getFuncUrl());
                        baseCacheBean.setSharePicUrl(picture.getSharePicUrl());
                        baseCacheBean.setFuncIcons(picture.getFuncIcons());
                        baseCacheBean.setShareDesc(picture.getShareStrparam());
                        baseCacheBean.setShareItem(picture.getShareItem());
                        baseCacheBean.setShareIsSure(picture.getShareIsSure());
                        baseCacheBean.setShareTitle(picture.getShareTitle());
                        baseCacheBean.setTitle(picture.getIconsName());
                        baseCacheBean.setKeyword(picture.getKeyword());
                        baseCacheBean.setIntervalSwitch(picture.getIntervalSwitch());
                        baseCacheBean.setLink_keyword_name(picture.getLinkKeywordName());
                        baseCacheBean.setIntervalSecond(picture.getIntervalSecond());
                        baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));

                        loginIconList.add(baseCacheBean);
                    }

                    result = gson.toJson(loginIconList);
                    adUtils.setType(ADSharePre.homeIconsLogin);
                    adUtils.setContent(result);
                    adUtils.setResTime(String.valueOf(System.currentTimeMillis()));
                    ADSharePre.saveConfiguration(ADSharePre.homeIconsLogin, adUtils);

                }
            }
        });
    }



    //理财专区
    public void financeSection() {



        String priceUrl = parseUrl(AZJ,PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType("15");

        addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

            @Override
            public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {

                List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> iconList =  ads_picture.getData().getAdsPictureList();

                List<BaseCacheBean> list = new ArrayList<>();

                String result = "";

                Gson gson = new Gson();

                ADUtils adUtils = new ADUtils();

                if (iconList != null && !iconList.isEmpty()){


                    for (ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture picture : iconList){

                        BaseCacheBean baseCacheBean = new BaseCacheBean();
                        baseCacheBean.setJumpRule(picture.getJumpRule());
                        baseCacheBean.setUuid(picture.getUuid());
                        baseCacheBean.setShareUrl(picture.getShareUrl());
                        baseCacheBean.setIsShare(picture.getIsShare());
                        baseCacheBean.setFuncUrl(picture.getFuncUrl());
                        baseCacheBean.setSharePicUrl(picture.getSharePicUrl());
                        baseCacheBean.setFuncIcons(picture.getFuncIcons());
                        baseCacheBean.setShareDesc(picture.getShareStrparam());
                        baseCacheBean.setShareItem(picture.getShareItem());
                        baseCacheBean.setShareIsSure(picture.getShareIsSure());
                        baseCacheBean.setShareTitle(picture.getShareTitle());
                        baseCacheBean.setKeyword(picture.getKeyword());
                        baseCacheBean.setTitle(picture.getIconsName());
                        baseCacheBean.setIntervalSwitch(picture.getIntervalSwitch());
                        baseCacheBean.setIntervalSecond(picture.getIntervalSecond());
                        baseCacheBean.setLink_keyword_name(picture.getLinkKeywordName());
                        baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));
                        list.add(baseCacheBean);
                    }

                    result = gson.toJson(list);
                    adUtils.setType(ADSharePre.homeSectionLogin);
                    adUtils.setContent(result);
                    adUtils.setResTime(String.valueOf(System.currentTimeMillis()));
                    ADSharePre.saveConfiguration(ADSharePre.homeSectionLogin, adUtils);
                }
            }
        });
    }

}



