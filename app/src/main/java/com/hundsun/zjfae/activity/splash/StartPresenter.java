package com.hundsun.zjfae.activity.splash;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.http.observer.UpdateObserver;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.home.bean.ADUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.v2.Notices;
import onight.zjfae.afront.gensazj.v2.LoginIcons;
import onight.zjfae.afront.gensazj.v2.UpdInterface;
import onight.zjfae.afront.gensazj.v3.StatisticsDataProtoBuf;

public class StartPresenter extends BasePresenter<StartView> {
    private List<Integer> requestList = new ArrayList<>();
    //启动页标识
    private String isSplashShow;
    //首页弹框标识
    private String isMainShow;
    //首页弹框标识时间确认字段
    private String resTime1;

    private static final String TAG = "TAG-->";

    public StartPresenter(StartView baseView) {
        super(baseView);
    }

    //登录页图片
    private void loginIcon(String type, final String resTime, final onResultListener onResultListener) {
        final List<BaseCacheBean> iconList = new ArrayList<>();
        Map map = getRequestMap();
        map.put("version",twoVersion);
        String url = parseUrl(AZJ, PBAFT, VICOAZJ,ConstantName.LoginIcon, map);
        LoginIcons.REQ_PBAPP_icons.Builder builder = LoginIcons.REQ_PBAPP_icons.newBuilder();
        builder.setIconsLocation(type);
        addDisposable(apiServer.onLoginIcons(url, getBody(builder.build().toByteArray())), new UpdateObserver<LoginIcons.Ret_PBAPP_icons>() {
            @Override
            public void onSuccess(LoginIcons.Ret_PBAPP_icons retIcons) {

                try {
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    if (! retIcons.getData().getIconsListList().isEmpty()) {
                        for (LoginIcons.PBAPP_icons.Icons icons :  retIcons.getData().getIconsListList()) {
                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            baseCacheBean.setUuid(icons.getUuid());
                            baseCacheBean.setIconsPosition(icons.getIconsPosition());
                            baseCacheBean.setIconsLocation(icons.getIconsLocation());
                            baseCacheBean.setIconsAddress(icons.getIconsAddress());
                            baseCacheBean.setResTime(resTime);
                            iconList.add(baseCacheBean);
                        }
                    }
                    result = gson.toJson(iconList);
                    adUtils.setType(ADSharePre.loginIcon);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.loginIcon, adUtils);
                    //CCLog.e("登录页中间以及底部请求时间",result);
                    onResultListener.onSuccess();
                }
                catch (Exception e){
                    Log.e("TAG","登录图片try异常"+e.getMessage());
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                onResultListener.onError();
            }
        });
    }

    //启动图
    private void startIcon(String type, final String resTime, final onResultListener onResultListener) {


        String priceUrl = parseUrl(AZJ,PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType(type);


        addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new UpdateObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

            @Override
            public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture pbapp_ads_picture) {


                ADUtils adUtils = new ADUtils();
                Gson gson = new Gson();
                String result = "";

                List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> adsPictureList = pbapp_ads_picture.getData().getAdsPictureList();

                if (adsPictureList.isEmpty()){
                    BaseCacheBean baseCacheBean = new BaseCacheBean();
                    baseCacheBean.setIs_show("0");//没有数据的时候默认设置为不显示
                    baseCacheBean.setFuncUrl("");
                    baseCacheBean.setUuid("");
                    baseCacheBean.setIconsAddress("");
                    baseCacheBean.setResTime(resTime);
                    baseCacheBean.setIsShare("");
                    result = gson.toJson(baseCacheBean);
                    adUtils.setType(ADSharePre.startIcons);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.startIcons, adUtils);
                    //CCLog.e("启动页图片请求时间",result);
                    onResultListener.onSuccess();
                }
                else {

                    if (!adsPictureList.isEmpty()){

                        for (int i = 0; i <adsPictureList.size() ; i++) {
                            String imageUrl = adsPictureList.get(0).getFuncIcons();
//                            BaseCacheBean baseCacheBean = new BaseCacheBean();
//                            baseCacheBean.setUuid(adsPictureList.get(0).getUuid());
//                            baseCacheBean.setIs_show(isSplashShow);//用来判断是否显示启动广告页
//                            baseCacheBean.setStartUpTime(adsPictureList.get(0).getStartUpTime());
//                            baseCacheBean.setFuncUrl(adsPictureList.get(0).getFuncUrl());
//                            baseCacheBean.setIsShare(adsPictureList.get(0).getIsShare());
//                            baseCacheBean.setIconsAddress(imageUrl);
//                            baseCacheBean.setResTime(resTime);

                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            baseCacheBean.setJumpRule(adsPictureList.get(0).getJumpRule());
                            baseCacheBean.setUuid(adsPictureList.get(0).getUuid());
                            baseCacheBean.setShareUrl(adsPictureList.get(0).getShareUrl());
                            baseCacheBean.setIsShare(adsPictureList.get(0).getIsShare());
                            baseCacheBean.setFuncUrl(adsPictureList.get(0).getFuncUrl());
                            baseCacheBean.setSharePicUrl(adsPictureList.get(0).getSharePicUrl());
                            baseCacheBean.setFuncIcons(adsPictureList.get(0).getFuncIcons());
                            baseCacheBean.setShareDesc(adsPictureList.get(0).getShareStrparam());
                            baseCacheBean.setShareItem(adsPictureList.get(0).getShareItem());
                            baseCacheBean.setTitle(adsPictureList.get(0).getIconsName());
                            baseCacheBean.setShareIsSure(adsPictureList.get(0).getShareIsSure());
                            baseCacheBean.setKeyword(adsPictureList.get(0).getKeyword());
                            baseCacheBean.setLink_keyword_name(adsPictureList.get(0).getLinkKeywordName());
                            baseCacheBean.setShareTitle(adsPictureList.get(0).getShareTitle());
                            baseCacheBean.setIntervalSwitch(adsPictureList.get(0).getIntervalSwitch());
                            baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));
                            baseCacheBean.setStartUpTime(adsPictureList.get(0).getStartUpTime());
                            baseCacheBean.setIs_show(isSplashShow);
                            result = gson.toJson(baseCacheBean);
                            adUtils.setType(ADSharePre.startIcons);
                            adUtils.setContent(result);
                            adUtils.setResTime(resTime);
                            ADSharePre.saveConfiguration(ADSharePre.startIcons, adUtils);
                            //CCLog.e("启动页图片请求时间",result);
                            onResultListener.onSuccess();
                            break;
                        }
                    }
                    else {
                        onResultListener.onError();
                    }

                }



            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                onResultListener.onError();
            }
        });

    }

    //首页产品图标
    private void homeProductIcon(String type, final String resTime, final onResultListener onResultListener) {
        final List<BaseCacheBean> iconList = new ArrayList<>();
        final String url = parseUrl(AZJ, PBICO, VICOAZJ, getRequestMap());
        AllAzjProto.PEAGetIcons.Builder builder = AllAzjProto.PEAGetIcons.newBuilder();
        builder.setIconsLocation(type);
        addDisposable(apiServer.allIconImage(url, getBody(builder.build().toByteArray())), new UpdateObserver<AllAzjProto.PEARetIcons>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetIcons retIcons) {
                Log.e(TAG+"homeProductIcon()","首页产品图标更新");
                try {

                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    if (!retIcons.getIconsListList().isEmpty()) {
                        for (AllAzjProto.PBAPPIcons icons : retIcons.getIconsListList()) {
                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            String imageUrl = icons.getIconsAddress();
                            baseCacheBean.setIconsAddress(imageUrl);
                            String uuid = icons.getUuid();
                            baseCacheBean.setUuid(uuid);
                            baseCacheBean.setResTime(resTime);
                            iconList.add(baseCacheBean);
                        }
                    }
                    result = gson.toJson(iconList);
                    adUtils.setType(ADSharePre.homeProductIcon);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.homeProductIcon, adUtils);
                    //CCLog.e("首页产品图标请求时间",result);
                    onResultListener.onSuccess();
                }catch (Exception e){
                    Log.e(TAG,"首页图标try异常："+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG+"首页图标更新异常->onError","首页产品图标更新");
                onResultListener.onError();
            }
        });
    }

    //购买列表和转让列表详情图标
    private void productIcon(final String type, final String resTime, final onResultListener onResultListener) {
        final List<BaseCacheBean> iconList = new ArrayList<>();
        String url = parseUrl(AZJ, PBICO, VICOAZJ, getRequestMap());
        AllAzjProto.PEAGetIcons.Builder builder = AllAzjProto.PEAGetIcons.newBuilder();
        builder.setIconsLocation(type);
        addDisposable(apiServer.allIconImage(url, getBody(builder.build().toByteArray())), new UpdateObserver<AllAzjProto.PEARetIcons>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetIcons retIcons) {
                Log.e(TAG ,"购买列表和转让列表详情图标更新正常");
                try {
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    if (!retIcons.getIconsListList().isEmpty()) {
                        for (AllAzjProto.PBAPPIcons icons : retIcons.getIconsListList()) {
                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            String imageUrl = icons.getIconsAddress();
                            baseCacheBean.setIconsAddress(imageUrl);
                            String uuid = icons.getUuid();
                            baseCacheBean.setUuid(uuid);
                            baseCacheBean.setResTime(resTime);
                            iconList.add(baseCacheBean);
                        }
                    }
                    result = gson.toJson(iconList);
                    adUtils.setType(type);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(type, adUtils);
                    //CCLog.e("产品详情图标请求时间",result);
                    onResultListener.onSuccess();
                }catch (Exception e){
                    Log.e(TAG ,"购买列表和转让列表详情图标try异常:"+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG+"productIcon()" ,"购买列表和转让列表详情图标更新异常:"+e.getMessage());
                onResultListener.onError();
            }
        });
    }

    //65周岁公告
    private void highAge(String type, final String resTime, final onResultListener onResultListener) {
        String url = parseUrl(AZJ, PBNOT, VNOTAZJ, getRequestMap());
        AllAzjProto.PEAGetNotice.Builder builder = AllAzjProto.PEAGetNotice.newBuilder();
        builder.setType(type);
        addDisposable(apiServer.noticeIcon(url, getBody(builder.build().toByteArray())), new UpdateObserver<AllAzjProto.PEARetNotice>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetNotice peaRetNotice) {
                Log.e(TAG,"65周岁公告更新正常");
                try {
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    BaseCacheBean baseCacheBean = new BaseCacheBean();
                    if (peaRetNotice.getReturnCode().equals("0000")) {
                        String saveTitle = peaRetNotice.getNotice().getNoticeTitle();
                        String saveContent = peaRetNotice.getNotice().getNoticeContent();
                        baseCacheBean.setTitle(saveTitle);
                        baseCacheBean.setContent(saveContent);
                    } else {
                        baseCacheBean.setTitle("");
                        baseCacheBean.setContent("");
                    }
                    baseCacheBean.setResTime(resTime);
                    result = gson.toJson(baseCacheBean);
                    adUtils.setType(ADSharePre.highAge);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.highAge, adUtils);
                    //CCLog.e("65周岁公告",result);
                    onResultListener.onSuccess();
                }catch (Exception e){

                    Log.e(TAG,"65周岁公告try异常"+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG,"65周岁公告更新异常"+e.getMessage());
                onResultListener.onError();
            }
        });
    }

    //高净值公告
    private void high(String type, final String resTime, final onResultListener onResultListener) {
        String url = parseUrl(AZJ, PBNOT, VNOTAZJ, getRequestMap());
        AllAzjProto.PEAGetNotice.Builder builder = AllAzjProto.PEAGetNotice.newBuilder();
        builder.setType(type);
        addDisposable(apiServer.noticeIcon(url, getBody(builder.build().toByteArray())), new UpdateObserver<AllAzjProto.PEARetNotice>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetNotice peaRetNotice) {
                try {
                    Log.e(TAG,"高净值更新图标正常");
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    BaseCacheBean baseCacheBean = new BaseCacheBean();
                    if (peaRetNotice.getReturnCode().equals("0000")) {
                        String saveTitle = peaRetNotice.getNotice().getNoticeTitle();
                        String saveContent = peaRetNotice.getNotice().getNoticeContent();
                        baseCacheBean.setTitle(saveTitle);
                        baseCacheBean.setContent(saveContent);
                    } else {
                        baseCacheBean.setTitle("");
                        baseCacheBean.setContent("");
                    }
                    baseCacheBean.setResTime(resTime);
                    result = gson.toJson(baseCacheBean);
                    adUtils.setType(ADSharePre.high);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.high, adUtils);
                    Log.e("高净值", result);
                    onResultListener.onSuccess();
                }catch (Exception e){
                    Log.e(TAG,"高净值更新图标try异常:"+e.getMessage());
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG,"高净值图标更新异常"+e.getMessage());
                onResultListener.onError();
            }
        });
    }

    //首页无产品
    private void homeProductNoDate(String type, final String resTime, final onResultListener onResultListener) {

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Notice, map);
        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setType(type);

        addDisposable(apiServer.getAllPbaftString(url, getBody(notice.build().toByteArray())), new UpdateObserver<Notices.Ret_PBAPP_notice>() {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice notice) {

                try {

                    Log.e("TAG","首页无产品更新正常");
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    BaseCacheBean baseCacheBean = new BaseCacheBean();
                    if (notice.getReturnCode().equals("0000")) {
                        String saveTitle = notice.getData().getNotice().getNoticeTitle();
                        String saveContent = notice.getData().getNotice().getNoticeContent();
                        baseCacheBean.setTitle(saveTitle);
                        baseCacheBean.setContent(saveContent);
                    } else {
                        baseCacheBean.setTitle("");
                        baseCacheBean.setContent("");
                    }
                    baseCacheBean.setResTime(resTime);
                    result = gson.toJson(baseCacheBean);
                    adUtils.setType(ADSharePre.homeProductNoDate);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.homeProductNoDate, adUtils);
                    //CCLog.e("homeProductNoDate",result);
                    onResultListener.onSuccess();

                }catch (Exception e){

                    Log.e(TAG,"首页无产品try异常："+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG,"首页无产品更新异常"+e.getMessage());
                onResultListener.onError();
            }
        });
    }

    //首页启动广告
    private void homeAd(String type, final String resTime, final onResultListener onResultListener) {
        AllAzjProto.PEAGetAds.Builder builder = AllAzjProto.PEAGetAds.newBuilder();
        builder.setShowType(type);
        String url = parseUrl(AZJ, PBADS, VADSAZJ, getRequestMap());

        addDisposable(apiServer.getAllPbadsIcon(url, getBody(builder.build().toByteArray())), new UpdateObserver<AllAzjProto.PEARetAds>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetAds peaRetAds) {
                Log.e(TAG,"首页启动广告更新正常");
                try {
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    if (peaRetAds.getAdsList().isEmpty()) {
                        BaseCacheBean baseCacheBean = new BaseCacheBean();
                        baseCacheBean.setIs_show("0");
                        baseCacheBean.setResTime(resTime);
                        baseCacheBean.setResTime1(resTime1);
                        result = gson.toJson(baseCacheBean);
                    } else {
                        for (AllAzjProto.PBAPPAds pbappAds : peaRetAds.getAdsList()) {
//                        String imageUrl = pbappAds.getFuncIcons();
                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            baseCacheBean.setIs_show(isMainShow);
//                        baseCacheBean.setFunc_url(pbappAds.getFuncUrl());
//                        baseCacheBean.setIconsAddress(imageUrl);
                            baseCacheBean.setResTime(resTime);
                            baseCacheBean.setResTime1(resTime1);
                            result = gson.toJson(baseCacheBean);
                        }
                    }
                    adUtils.setType(ADSharePre.homeAd);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);

                    //CCLog.e("首页启动广告",json);
                    ADSharePre.saveConfiguration(ADSharePre.homeAd, adUtils);
                    onResultListener.onSuccess();
                }
                catch (Exception e){
                    Log.e(TAG,"首页启动广告try异常："+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG,"首页启动广告更新异常"+e.getMessage());
                onResultListener.onError();
            }
        });
    }

    //首页四个圆圈 未登录
    private void outLoginIcon(final String resTime, final onResultListener onResultListener) {
        AllAzjProto.PEAGetIcons.Builder iconBuilder = AllAzjProto.PEAGetIcons.newBuilder();
        iconBuilder.setIconsLocation("homePage");
        iconBuilder.setIconsPosition("middle");
        String url = parseUrl(AZJ, PBICO, VICOAZJ, getRequestMap());
        addDisposable(apiServer.outLoginIcon(url, getBody(iconBuilder.build().toByteArray())), new ProtoBufObserver<AllAzjProto.PEARetIcons>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetIcons retIcons) {
                Log.e(TAG,"首页四个圆圈 未登录更新正常");
                try {
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    List<BaseCacheBean> iconList = new ArrayList<>();
                    if (!retIcons.getIconsListList().isEmpty()) {
                        for (AllAzjProto.PBAPPIcons pbappIcons : retIcons.getIconsListList()) {
                            String imageUrl = pbappIcons.getIconsAddress();
                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            baseCacheBean.setTitle(pbappIcons.getIconsName());
                            baseCacheBean.setUuid(pbappIcons.getUuid());
                            baseCacheBean.setFuncIcons(imageUrl);
                            if (pbappIcons.getIsShare() == null || pbappIcons.getIsShare().equals("")) {
                                baseCacheBean.setIsShare("0");
                            } else {
                                baseCacheBean.setIsShare(pbappIcons.getIsShare());
                            }
                            baseCacheBean.setFuncUrl(pbappIcons.getIconsLink());
                            baseCacheBean.setResTime(resTime);
                            iconList.add(baseCacheBean);
                        }
                        result = gson.toJson(iconList);
                        adUtils.setType(ADSharePre.homeIconsOutLogin);
                        adUtils.setContent(result);
                        adUtils.setResTime(resTime);
                        ADSharePre.saveConfiguration(ADSharePre.homeIconsOutLogin, adUtils);
                    }
                    onResultListener.onSuccess();
                }
                catch (Exception e){
                    Log.e(TAG,"首页四个圆圈 未登录try异常："+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                onResultListener.onError();
                Log.e(TAG,"首页四个圆圈 未登录更新异常"+e.getMessage());
            }
        });
    }

    //首页理财专区 未登录
    private void highProductIcon(final String resTime, final onResultListener onResultListener) {
        AllAzjProto.PEAGetIcons.Builder iconBuilder = AllAzjProto.PEAGetIcons.newBuilder();
        iconBuilder.setIconsLocation("homeSubscribe");
        iconBuilder.setIconsPosition("right_lower");
        String url = parseUrl(AZJ, PBICO, VICOAZJ, getRequestMap());
        addDisposable(apiServer.highProductStartIcon(url, getBody(iconBuilder.build().toByteArray())), new ProtoBufObserver<AllAzjProto.PEARetIcons>() {
            @Override
            public void onSuccess(AllAzjProto.PEARetIcons section_login) {
                try {

                    Log.e(TAG,"首页理财专区 未登录更新正常");
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    List<BaseCacheBean> iconList = new ArrayList<>();
                    if (!section_login.getIconsListList().isEmpty()) {
                        for (AllAzjProto.PBAPPIcons icons : section_login.getIconsListList()) {
                            String imageUrl = icons.getIconsAddress();
                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            baseCacheBean.setUuid(icons.getUuid());
                            baseCacheBean.setTitle(icons.getIconsName());
                            baseCacheBean.setFuncIcons(imageUrl);
                            baseCacheBean.setJumpRule(icons.getJumpRule());
                            baseCacheBean.setKeyword(icons.getKeyword());
                            baseCacheBean.setLink_keyword_name(icons.getLinkKeywordName());
                            baseCacheBean.setResTime(resTime);
                            iconList.add(baseCacheBean);
                        }
                        result = gson.toJson(iconList);
                        adUtils.setType(ADSharePre.homeSectionOutLogin);
                        adUtils.setContent(result);
                        adUtils.setResTime(resTime);
                        ADSharePre.saveConfiguration(ADSharePre.homeSectionOutLogin, adUtils);
                    }
                    onResultListener.onSuccess();
                }
                catch (Exception e){
                    Log.e(TAG,"首页理财专区 未登录try异常："+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                Log.e(TAG,"首页理财专区 未登录更新异常"+e.getMessage());
                onResultListener.onError();
            }
        });

    }

    //免责声明
    private void disclaimer(String type, final String resTime, final onResultListener onResultListener) {

        Map<String, String> map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.Notice, map);
        Notices.REQ_PBAPP_notice.Builder notice = Notices.REQ_PBAPP_notice.newBuilder();
        notice.setType(type);
        addDisposable(apiServer.getAllPbaftString(url, getBody(notice.build().toByteArray())), new UpdateObserver<Notices.Ret_PBAPP_notice>() {
            @Override
            public void onSuccess(Notices.Ret_PBAPP_notice peaRetNotice) {
                try {
                    Log.e(TAG,"免责声明更新正常");
                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";
                    BaseCacheBean baseCacheBean = new BaseCacheBean();
                    if (peaRetNotice.getReturnCode().equals("0000")) {
                        String saveTitle = peaRetNotice.getData().getNotice().getNoticeTitle();
                        String saveContent = peaRetNotice.getData().getNotice().getNoticeContent();
                        baseCacheBean.setTitle(saveTitle);
                        baseCacheBean.setContent(saveContent);
                        baseCacheBean.setButton_title(peaRetNotice.getData().getNotice().getButtonTitle());
                    } else {
                        baseCacheBean.setTitle("");
                        baseCacheBean.setContent("");
                        baseCacheBean.setButton_title("");
                    }
                    baseCacheBean.setResTime(resTime);
                    result = gson.toJson(baseCacheBean);
                    adUtils.setType(ADSharePre.disclaimer);
                    adUtils.setContent(result);
                    adUtils.setResTime(resTime);
                    ADSharePre.saveConfiguration(ADSharePre.disclaimer, adUtils);
                    onResultListener.onSuccess();

                }
                catch (Exception e){
                    Log.e(TAG,"免责声明try异常："+e.getMessage());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                onResultListener.onError();
            }
        });
    }

    public void isImageUpdate() {

        UpdInterface.REQ_PBAPP_updInterface.Builder imageUpdate = UpdInterface.REQ_PBAPP_updInterface.newBuilder();

        UpdInterface.REQ_PBAPP_updInterface.UpdInterfaceReq.Builder subBuilder = UpdInterface.REQ_PBAPP_updInterface.UpdInterfaceReq.newBuilder();

        //首页小图标
        subBuilder.setPbname("pbico.do");
        subBuilder.setParm1("subscribeListHome");
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.homeProductIcon));
        imageUpdate.addUpdinterface(subBuilder);

        //购买列表icon图
        subBuilder.setPbname("pbico.do");
        subBuilder.setParm1("subscribeList");
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.subscribeIcon));
        imageUpdate.addUpdinterface(subBuilder);

        //转让列表icon图
        subBuilder.setPbname("pbico.do");
        subBuilder.setParm1("transferList");
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.transferIcon));
        imageUpdate.addUpdinterface(subBuilder);

        //高净值
        subBuilder.setPbname("pbnot.do");
        subBuilder.setParm1("0");//高净值(type)
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.high));
        imageUpdate.addUpdinterface(subBuilder);

        //65周岁提示(type)
        subBuilder.setPbname("pbnot.do");
        subBuilder.setParm1("1");//65周岁提示(type)
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.highAge));
        imageUpdate.addUpdinterface(subBuilder);

        //启动广告
        subBuilder.setPbname("pbpic.do");
        subBuilder.setParm1("0");//启动广告(type)
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.startIcons));
        imageUpdate.addUpdinterface(subBuilder);

        //首页弹框（每月一弹）
        subBuilder.setPbname("pbads.do");
        subBuilder.setParm1("12");//首页弹框(type)
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(BaseSharedPreferences.getLevelRetTime());
        imageUpdate.addUpdinterface(subBuilder);

        //登录图片
        subBuilder.setPbname("pbico.do");
        subBuilder.setParm1("bigLoginPage");//登录图片(type)
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.loginIcon));
        imageUpdate.addUpdinterface(subBuilder);

        //产品列表空数据
        subBuilder.setPbname("pbnot.do");
        subBuilder.setParm1("005");
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.homeProductNoDate));
        imageUpdate.addUpdinterface(subBuilder);

        //首页四个圆圈数据 banner底部
        subBuilder.setPbname("pbpic.do");
        subBuilder.setParm1("16");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.homeIconsOutLogin));
        imageUpdate.addUpdinterface(subBuilder);

        //首页理财中心图片
        subBuilder.setPbname("pbpic.do");
        subBuilder.setParm1("15");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.homeSectionOutLogin));
        imageUpdate.addUpdinterface(subBuilder);

        //免责声明(type)
        subBuilder.setPbname("pbnot.do");
        subBuilder.setParm1("006");//免责声明(type)
        subBuilder.setParm2("");
        subBuilder.setLastUpdtime(ADSharePre.getResTime(ADSharePre.disclaimer));
        imageUpdate.addUpdinterface(subBuilder);


        //首页弹框（每日一弹）
        subBuilder.setPbname("pbpic.do");
        subBuilder.setParm1("13");//首页弹框(type)
        subBuilder.setLastUpdtime(BaseSharedPreferences.getDayRetTime());
        imageUpdate.addUpdinterface(subBuilder);

        // 交易专区图标与点击的Url
        subBuilder.setPbname("pbpic.do");
        subBuilder.setParm1("18");
        subBuilder.setLastUpdtime(BaseSharedPreferences.getTradeIconRetTime());
        imageUpdate.addUpdinterface(subBuilder);

        Map map = getRequestMap();
        map.put("version", twoVersion);

        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.ImageUpdate, map);

        addDisposable(apiServer.imageIconUpdate(url, getBody(imageUpdate.build().toByteArray())), new BaseObserver<UpdInterface.Ret_PBAPP_updInterface>(baseView) {
            @Override
            public void onSuccess(UpdInterface.Ret_PBAPP_updInterface ret_pbapp_updInterface) {
                String resTime = ret_pbapp_updInterface.getData().getResTime();
                requestList.clear();
                for (int i = 0; i < ret_pbapp_updInterface.getData().getUpdinterfaceList().size(); i++) {
                    UpdInterface.PBAPP_updInterface.UpdInterfaceRes updInterfaceRes = ret_pbapp_updInterface.getData().getUpdinterfaceList().get(i);
                    switch (i) {
                        case 0:
                            //首页产品列表图标
                            if (ADSharePre.getListConfiguration(ADSharePre.homeProductIcon, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"首页产品列表图标是否需要更新","首页产品列表图标是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(0);
                            }
                            break;
                        case 1:
                            //购买列表icon图
                            if (ADSharePre.getListConfiguration(ADSharePre.subscribeIcon, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"购买列表icon图是否需要更新","购买列表icon图是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(1);
                            }
                            break;
                        case 2:
                            //购买列表icon图
                            if (ADSharePre.getListConfiguration(ADSharePre.transferIcon, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"转让列表icon图是否需要更新","转让列表icon图是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(2);
                            }
                            break;
                        case 3:
                            //高净值
                            if (ADSharePre.getConfiguration(ADSharePre.high, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"高净值是否需要更新","高净值是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(3);
                            }
                            break;
                        case 4:
                            //65周岁提示
                            if (ADSharePre.getConfiguration(ADSharePre.highAge, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"65周岁提示是否需要更新","65周岁提示是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(4);
                            }
                            break;
                        case 5:
                            isSplashShow = updInterfaceRes.getIsShow();

                            BaseCacheBean baseCacheBeans = ADSharePre.getConfiguration(ADSharePre.startIcons, BaseCacheBean.class);

                            if (baseCacheBeans != null){
                                baseCacheBeans.setIs_show(isSplashShow);
                                Gson gsons = new Gson();
                                String results = gsons.toJson(baseCacheBeans);
                                ADUtils adUtils1 = new ADUtils();
                                adUtils1.setType(ADSharePre.startIcons);
                                adUtils1.setContent(results);
                                adUtils1.setResTime(resTime);
                                ADSharePre.saveConfiguration(ADSharePre.startIcons, adUtils1);
                            }


                            //启动广告
                            if (ADSharePre.getConfiguration(ADSharePre.startIcons, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"启动广告是否需要更新","启动广告是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                Log.e(TAG+"启动广告是否显示","启动广告是否显示" + i + "------>" + updInterfaceRes.getIsShow());
                                requestList.add(5);

                            }
                            break;
                        case 6:
                            //首页弹广告(每月一弹)
                            if ( updInterfaceRes.getIsUpdate().equals("1") || !updInterfaceRes.getResTime1().equals(BaseSharedPreferences.getLevelRetTime())) {
//                                requestList.add(5);
                                Log.e(TAG+"首页弹广告是否需要更新","首页弹广告是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                Log.e(TAG+"首页弹广告是否显示","首页弹广告是否显示" + i + "------>" + updInterfaceRes.getIsShow());
                                Log.e(TAG+"首页弹广告更新时间","首页弹广告更新时间" + updInterfaceRes.getResTime1());
                                Log.e(TAG+"首页弹广告本地保存时间","首页弹广告本地保存时间" + UserInfoSharePre.getResTime());

                                BaseSharedPreferences.saveLevelRetTime(resTime);
                                BaseSharedPreferences.saveIsUpdateSate("1");
                                BaseSharedPreferences.saveLevelShowTime(updInterfaceRes.getResTime1());
                                BaseSharedPreferences.saveIsShowState(updInterfaceRes.getIsShow());
                            }
                            break;
                        case 7:
                            //登录图片
                            if (ADSharePre.getListConfiguration(ADSharePre.loginIcon, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"登录页图片是否需要更新","登录页图片是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(7);
                            }
                            break;
                        case 8:
                            //产品列表空数据
                            if (ADSharePre.getConfiguration(ADSharePre.homeProductNoDate, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"产品列表空数据文字是否需要更新","产品列表空数据文字是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(8);
                            }
                            break;
                        case 9:
                            //首页四个圆圈未登录缓存数据
                            if (ADSharePre.getListConfiguration(ADSharePre.homeIconsOutLogin, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"首页四个圆圈未登录是否需要更新","首页四个圆圈未登录是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                if (updInterfaceRes.getIsUpdate().equals("1")) {
                                    UserSetting.setishomeIconsOutLogin(true);
                                }
                                requestList.add(9);
                            }
                            break;
                        case 10:
                            //首页理财中心未登录缓存数据
                            if (ADSharePre.getListConfiguration(ADSharePre.homeSectionOutLogin, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"首页理财中心未登录是否需要更新","首页理财中心未登录是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                if (updInterfaceRes.getIsUpdate().equals("1")) {
                                    UserSetting.setishomeSectionOutLogin(true);
                                }
                                requestList.add(10);
                            }
                            break;
                        case 11:
                            //免责声明
                            if (ADSharePre.getConfiguration(ADSharePre.disclaimer, BaseCacheBean.class) == null || updInterfaceRes.getIsUpdate().equals("1")) {
                                Log.e(TAG+"首页理财中心未登录是否需要更新","免责声明是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                requestList.add(11);
                            }
                            break;


                        case 12:{
                            //首页广告（每日一弹广告）
                            if (updInterfaceRes.getIsUpdate().equals("1") || !updInterfaceRes.getResTime1().equals(BaseSharedPreferences.getDayRetTime())) {
                                Log.e(TAG+"首页弹广告是否需要更新","首页弹广告是否需要更新" + i + "------>" + updInterfaceRes.getIsUpdate());
                                Log.e(TAG+"首页弹广告是否显示","首页弹广告是否显示" + i + "------>" + updInterfaceRes.getIsShow());
                                Log.e(TAG+"首页弹广告更新时间","首页弹广告更新时间" + updInterfaceRes.getResTime1());
                                Log.e(TAG+"首页弹广告本地保存时间","首页弹广告本地保存时间" + UserInfoSharePre.getResTime());

                                BaseSharedPreferences.saveDayRetTime(resTime);

                                BaseSharedPreferences.saveDayShowTime(updInterfaceRes.getResTime1());

                                BaseSharedPreferences.saveIsUpdateSate(updInterfaceRes.getIsUpdate());

                                BaseSharedPreferences.saveIsShowState(updInterfaceRes.getIsShow());
                            }

                        }
                            break;

                        case 13:
                            // 首页交易图标
                            if (updInterfaceRes.getIsUpdate().equals("1") || !updInterfaceRes.getResTime1().equals(BaseSharedPreferences.getTradeIconRetTime())) {

                                BaseSharedPreferences.saveTradeIconRetTime(resTime);
                                BaseSharedPreferences.saveTradeIsUpdateSate(updInterfaceRes.getIsUpdate());
                            }
                            break;

                        default:
                            break;
                    }
                }
                Log.e(TAG+"需要更新的数组数据",requestList.size()+"");
                if (requestList.size() > 0) {
                    Log.e(TAG+"requestlist","requestlist----" + requestList.toString());
                }
                doRequest(resTime);
            }
        });
    }

    private void doRequest(final String resTime) {
        Log.e(TAG+"开始请求更新接口","");
        if (requestList.size() == 0) {//如果数据为空 则直接掉成功方法
            baseView.onSuccess();
            return;
        }
        Log.e(TAG+"requestlist","requestlist----" + requestList.get(requestList.size() - 1));
        switch (requestList.get(requestList.size() - 1)) {
            case 0:
                homeProductIcon(ADSharePre.subscribeListHomeType, resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 1:
                productIcon(ADSharePre.subscribeIcon, resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 2:
                productIcon(ADSharePre.transferIcon, resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 3:
                high("0", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 4:
                highAge("1", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 5:
                startIcon("0", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 6:
                homeAd("2", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 7:
                loginIcon("bigLoginPage", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 8:
                homeProductNoDate("005", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 9:
                outLoginIcon(resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 10:
                highProductIcon(resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;
            case 11:
                disclaimer("006", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });
                break;

            case 12:
                homeAd("2", resTime, new onResultListener() {
                    @Override
                    public void onSuccess() {
                        repeatDoRequest(resTime);
                    }

                    @Override
                    public void onError() {
                        repeatDoRequest(resTime);
                    }
                });

                break;
            default:
                break;
        }

    }

    //循环请求更新图片接口
    private void repeatDoRequest(String resTime) {
        if (requestList.size() == 1) {
            baseView.onSuccess();
        } else {
            requestList.remove(requestList.size() - 1);
            doRequest(resTime);
        }

    }

    //接口回调判断接口是不是请求完毕
    private interface onResultListener {
        void onSuccess();

        void onError();
    }


    public String getResTime(File filePath) {
        String resTime = "0";

        String content = FileUtil.getFileDate(filePath);
        if (content.equals("")) {
            return resTime;
        } else {
            JSONObject os = null;
            try {
                os = new JSONObject(content);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            resTime = os.optString("resTime");
            return resTime;
        }

    }


    //上送点击事件
    public void statistticsDataUpload() {
        if (!StringUtils.isNotBlank(UserInfoSharePre.getStatistticsData())) {
            return;
        }
        Map map = getRequestMap();
        map.put("version",version);
        final String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.StatisticsData, map);

        //StatisticsDataPB.REQ_PBAPP_statisticsData.Builder builder = StatisticsDataPB.REQ_PBAPP_statisticsData.newBuilder();
        StatisticsDataProtoBuf.REQ_PBAPP_statisticsDataNew.Builder builder = StatisticsDataProtoBuf.REQ_PBAPP_statisticsDataNew.newBuilder();
        Type listType = new TypeToken<List<StatisticsDataBean>>() {
        }.getType();
        Gson gson = new Gson();
        List<StatisticsDataBean> list = gson.fromJson(UserInfoSharePre.getStatistticsData(), listType);

        if (list == null){
            return;
        }
        if (list.isEmpty()){
            return;
        }

        CCLog.e("上送点击事件数据----" + list.toString());
        for (StatisticsDataBean dataBean : list) {
            StatisticsDataProtoBuf.REQ_PBAPP_statisticsDataNew.StatisticsData.Builder data = StatisticsDataProtoBuf.REQ_PBAPP_statisticsDataNew.StatisticsData.newBuilder();
            data.setUuid(dataBean.getAdsUuid());
            data.setCount(dataBean.getCount());
            builder.addData(data);
        }

        addDisposable(apiServer.statisticsData(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<StatisticsDataProtoBuf.Ret_PBAPP_statisticsDataNew>() {
            @Override
            public void onSuccess(StatisticsDataProtoBuf.Ret_PBAPP_statisticsDataNew ret_pbapp_statisticsData) {
                if (ret_pbapp_statisticsData.getReturnCode().equals("0000")) {
                    CCLog.e("StatistticsData----数据上送成功");
                    UserSetting.setCrachUpLoadTime();
                    UserInfoSharePre.clearStatistticsData();
                }
            }

        });


    }


}
