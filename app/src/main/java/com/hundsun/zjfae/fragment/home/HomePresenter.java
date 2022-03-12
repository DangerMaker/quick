package com.hundsun.zjfae.fragment.home;

import com.google.gson.Gson;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.cache.app.AppCache;
import com.hundsun.zjfae.common.http.api.ConstantCode;
import com.hundsun.zjfae.common.http.api.ConstantName;
import com.hundsun.zjfae.common.http.observer.BaseObserver;
import com.hundsun.zjfae.common.http.observer.ProtoBufObserver;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserLevelShowTimeSharePre;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.user.UserShowTimeSharedPre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.RxInterval;
import com.hundsun.zjfae.common.utils.SharedPreferenceAccesser;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.fragment.home.bean.ADUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.BecomeInvestor;
import onight.zjfae.afront.gens.PBIFERegAddUsersRegisterPrivacyAgree;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v3.OrderNotSubscribedProduct;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.PrivateNotice;
import onight.zjfae.afront.gensazj.UnReadMes;
import onight.zjfae.afront.gensazj.v2.BannerProto;


/**
 * @author moran
 * 首页Presenter
 */
public class HomePresenter extends BasePresenter<HomeView> {


    public HomePresenter(HomeView baseView) {
        super(baseView);
    }

    /**
     * 请求交易专区是否隐藏展示
     */
    public void getDictionary() {
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ,
                ConstantName.Dictionary,
                getRequestMap());

        Dictionary.REQ_PBAPP_dictionary.Builder diction = Dictionary.REQ_PBAPP_dictionary.newBuilder();
        diction.addKeyNo("home.finance.show");
        diction.addKeyNo("home.finance.title");
//        diction.addKeyNo("prdDetails.valueDate.tips");

        addDisposable(
                apiServer.getDictionary(
                        url,
                        getBody(diction.build().toByteArray())),
                new ProtoBufObserver<Dictionary.Ret_PBAPP_dictionary>(baseView) {
                    @Override
                    public void onSuccess(Dictionary.Ret_PBAPP_dictionary dictionary) {
                        List<Dictionary.PBAPP_dictionary.Parms> parms = dictionary.getData().getParmsList();
                        if (parms.size() > 2) {
                            BaseSharedPreferences.saveManageStartDateTips(parms.get(2).getKeyCode());
                            baseView.onGetTradeAreaIsShow(parms.get(0).getKeyCode(), parms.get(1).getKeyCode());
                        } else if (parms.size() == 2) {
                            baseView.onGetTradeAreaIsShow(parms.get(0).getKeyCode(), parms.get(1).getKeyCode());
                        } else {
                            baseView.onGetTradeAreaIsShow(parms.get(0).getKeyCode(), "交易专区");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        String isShow = SharedPreferenceAccesser.getStringData(AppCache.TRADE_AREA_IS_SHOW);
                        if (isShow == null) {
                            isShow = "0";
                        }
                        baseView.onGetTradeAreaIsShow(
                                isShow,
                                "交易专区");
                    }
                });

    }

    /**
     * 本地缓存的未登录图片
     **/
    public void outLoginInfo() {
        //首页图标
        List<BaseCacheBean> homeIconList = ADSharePre.getHomeIcons(false, BaseCacheBean.class);
        if (!homeIconList.isEmpty()) {
            baseView.homeIconList(homeIconList);
        }
        //理财专区图片
        List<BaseCacheBean> imageList = ADSharePre.getHomeManageFinancesIcon(false, BaseCacheBean.class);
        if (!imageList.isEmpty()) {
            baseView.homeFinancialIcon(imageList);
        }

        //未登录banner图
        Map map = getRequestMap();
        map.put("version", twoVersion);
        String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.BANNER_PBNAME, map);
        BannerProto.REQ_PBAPP_ads.Builder bannerRequest = BannerProto.REQ_PBAPP_ads.newBuilder();
        bannerRequest.setShowType("1");
        addDisposable(apiServer.homeBanner(url, getBody(bannerRequest.build().toByteArray())), new ProtoBufObserver<BannerProto.Ret_PBAPP_ads>(baseView) {
            @Override
            public void onSuccess(BannerProto.Ret_PBAPP_ads ret_pbapp_ads) {
                baseView.outLoginBanner(ret_pbapp_ads);
            }
        });
    }

    /**
     * 登录之后信息
     * 聚合：首页banner图，用户预约购买信息，用户详细信息，底部产品，用户消息接口
     **/
    public void loginInfo() {

        //首页图标
        final List<BaseCacheBean> homeIconList = ADSharePre.getHomeIcons(true, BaseCacheBean.class);

        if (!homeIconList.isEmpty()) {
            baseView.homeIconList(homeIconList);
        } else {
            loginIcons();
        }
        //理财专区图片
        final List<BaseCacheBean> imageList = ADSharePre.getHomeManageFinancesIcon(true, BaseCacheBean.class);

        if (!imageList.isEmpty()) {
            baseView.homeFinancialIcon(imageList);
        } else {
            financeSection();
        }

        Observable observable = Observable.mergeArrayDelayError(loginBanner(), requestQuerySubscribeProductList(), getUserInfo(), homeBottomProduct(), requestMessage());
        addDisposable(observable, new ProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {

                //banner图
                if (mClass instanceof ADPictureProtoBuf.Ret_PBAPP_ads_picture) {

                    baseView.loginBanner((ADPictureProtoBuf.Ret_PBAPP_ads_picture) mClass);
                }
                //用户预约产品信息
                else if (mClass instanceof OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct) {

                    baseView.subscribedProduct((OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct) mClass);
                }
                //用户详细信息
                else if (mClass instanceof UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) {

                    UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = (UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo) mClass;
                    baseView.userDetailInfo(userDetailInfo, homeIconList, imageList);


                }
                //底部产品
                else if (mClass instanceof PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome) {

                    baseView.homeBottomProduct((PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome) mClass);
                }
                //用户信息
                else if (mClass instanceof AllAzjProto.PEARetMessageCount) {

                    baseView.userMassage((AllAzjProto.PEARetMessageCount) mClass);
                }
            }
        });
    }

    /**
     * 是否更新首页图标
     * 是否更新交易专区
     */
    public void upDataHomeLoginIcon(List<BaseCacheBean> homeIconList, List<BaseCacheBean> imageList, String iconsUserType) {

        /**
         * 用户类型发生改变
         * 刷新交易图标以及点击的Url
         */
        if (!UserInfoSharePre.getIconsUserType().equals(iconsUserType)) {
            UserInfoSharePre.saveIconsUserType(iconsUserType);
            updateUserIcon();
            financeSection();
            getTradeImageAndImageUrl(true);
        } else {
            if (homeIconList.isEmpty() || UserSetting.ishomeIconsOutLogin()) {
                UserSetting.setishomeIconsOutLogin(false);
                updateUserIcon();
            } else {
                getImageListData(homeIconList);
            }

            if (imageList.isEmpty() || UserSetting.ishomeSectionOutLogin()) {
                financeSection();
                UserSetting.setishomeSectionOutLogin(false);
            }
        }


    }


    /**
     * 页面切换请求
     */
    public void navigationData() {
        //首页图标
        final List<BaseCacheBean> homeIconList = ADSharePre.getHomeIcons(true, BaseCacheBean.class);

        if (!homeIconList.isEmpty()) {
            baseView.homeIconList(homeIconList);
        } else {
            loginIcons();
        }
        //理财专区图片
        final List<BaseCacheBean> imageList = ADSharePre.getHomeManageFinancesIcon(true, BaseCacheBean.class);

        if (!imageList.isEmpty()) {
            baseView.homeFinancialIcon(imageList);
        } else {
            financeSection();
        }
        getImageListData(homeIconList);
        //聚合3个接口
        Observable observable = Observable.mergeDelayError(homeBottomProduct(), loginBanner(), requestMessage());
        addDisposable(observable, new ProtoBufObserver(baseView) {
            @Override
            public void onSuccess(Object mClass) {
                //底部产品
                if (mClass instanceof PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome) {

                    baseView.homeBottomProduct((PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome) mClass);
                }
                //banner图
                if (mClass instanceof ADPictureProtoBuf.Ret_PBAPP_ads_picture) {

                    baseView.loginBanner((ADPictureProtoBuf.Ret_PBAPP_ads_picture) mClass);
                }
                //用户消息
                else if (mClass instanceof AllAzjProto.PEARetMessageCount) {

                    baseView.userMassage((AllAzjProto.PEARetMessageCount) mClass);
                }
            }
        });
    }


    //首页广告弹框请求
    public void homeAD(String iconUserType, final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        String isUpdate = BaseSharedPreferences.getIsUpdate();

        //首页每日弹框服务器时间
        final String showDayServiceTime = BaseSharedPreferences.getDayShowTime();

        //用户当前存储的弹出广告时间
        final String userShowDayTime = UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).getUserDayShowTime();

        //首页每月一弹等级广告服务器时间
        String levelTime = BaseSharedPreferences.getLevelShowTime();
        //levelTime不为""且长度大于7
        final String levelShowServiceTime = ((!levelTime.equals("")) && (levelTime.length() >= 7)) ? levelTime.substring(0, 7) : "";
        //当前用户存储的每月一弹广告时间
        final String userLevelTime = UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).getUserLevelShowTime();

        /**
         * isUpdate = 1表示每日一弹图片需要更新
         * !UserInfoSharePre.getIconsUserType().equals(iconUserType) 用户类型不一致 更新
         * !levelShowServiceTime.equals(userLevelTime) 首页等级广告 时间不一致，更新
         * !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount()) 当前账号是否登录过 没有登录，更新
         * **/

        //触发接口更新
        if (isUpdate.equals("1") || !UserInfoSharePre.getIconsUserType().equals(iconUserType) || !levelShowServiceTime.equals(userLevelTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount())) {

            String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
            ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
            adsPicture.setShowType("12");

            addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {
                @Override
                public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {


                    List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> adsPictureList = ads_picture.getData().getAdsPictureList();

                    BaseSharedPreferences.saveIsUpdateSate("0");

                    BaseCacheBean levelBaseCacheBean = new BaseCacheBean();

                    if (!adsPictureList.isEmpty()) {

                        for (int i = 0; i < adsPictureList.size(); i++) {

                            if (i == 0) {
                                String imageUrl = adsPictureList.get(0).getFuncIcons();
                                String isShare = adsPictureList.get(0).getIsShare();
                                String openUrl = adsPictureList.get(0).getFuncUrl();
                                levelBaseCacheBean.setJumpRule(adsPictureList.get(0).getJumpRule());
                                levelBaseCacheBean.setUuid(adsPictureList.get(0).getUuid());
                                levelBaseCacheBean.setShareUrl(adsPictureList.get(0).getShareUrl());
                                levelBaseCacheBean.setFuncUrl(openUrl);
                                levelBaseCacheBean.setSharePicUrl(adsPictureList.get(0).getSharePicUrl());
                                levelBaseCacheBean.setFuncIcons(imageUrl);
                                levelBaseCacheBean.setShareDesc(adsPictureList.get(0).getShareStrparam());
                                levelBaseCacheBean.setShareItem(adsPictureList.get(0).getShareItem());
                                levelBaseCacheBean.setShareIsSure(adsPictureList.get(0).getShareIsSure());
                                levelBaseCacheBean.setShareTitle(adsPictureList.get(0).getShareTitle());
                                levelBaseCacheBean.setLink_keyword_name(adsPictureList.get(0).getLinkKeywordName());
                                levelBaseCacheBean.setKeyword(adsPictureList.get(0).getKeyword());
                                levelBaseCacheBean.setIntervalSecond(adsPictureList.get(0).getIntervalSecond());
                                levelBaseCacheBean.setIntervalSwitch(adsPictureList.get(0).getIntervalSwitch());
                                levelBaseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));
                                if (isShare == null || isShare.equals("")) {
                                    levelBaseCacheBean.setIsShare("0");
                                } else {
                                    levelBaseCacheBean.setIsShare(isShare);
                                }
                            } else if (i == 1) {
                                BaseCacheBean baseCacheBean = new BaseCacheBean();
                                baseCacheBean.setJumpRule(adsPictureList.get(1).getJumpRule());
                                baseCacheBean.setUuid(adsPictureList.get(1).getUuid());
                                baseCacheBean.setShareUrl(adsPictureList.get(1).getShareUrl());
                                baseCacheBean.setFuncUrl(adsPictureList.get(1).getFuncUrl());
                                baseCacheBean.setSharePicUrl(adsPictureList.get(1).getSharePicUrl());
                                baseCacheBean.setFuncIcons(adsPictureList.get(1).getFuncIcons());
                                baseCacheBean.setShareDesc(adsPictureList.get(1).getShareStrparam());
                                baseCacheBean.setShareItem(adsPictureList.get(1).getShareItem());
                                baseCacheBean.setShareIsSure(adsPictureList.get(1).getShareIsSure());
                                baseCacheBean.setShareTitle(adsPictureList.get(1).getShareTitle());
                                baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));
                                baseCacheBean.setKeyword(adsPictureList.get(1).getKeyword());
                                baseCacheBean.setIntervalSwitch(adsPictureList.get(1).getIntervalSwitch());
                                baseCacheBean.setIntervalSecond(adsPictureList.get(1).getIntervalSecond());
                                baseCacheBean.setLink_keyword_name(adsPictureList.get(1).getLinkKeywordName());
                                if (adsPictureList.get(1).getIsShare() == null || adsPictureList.get(1).getIsShare().equals("")) {
                                    baseCacheBean.setIsShare("0");
                                } else {
                                    baseCacheBean.setIsShare(adsPictureList.get(1).getIsShare());
                                }
                                Gson gson = new Gson();
                                UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).setHomeAdContent(gson.toJson(baseCacheBean));
                            }
                        }
                        //（弹等级框时间不同 或者 账号未登录过）并上 当月是否升级
                        boolean isShowLevel = (!levelShowServiceTime.equals(userLevelTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount()));
                        //展示等级广告
                        if (isShowLevel && ads_picture.getData().getIsUpgrade().equals("1")) {
                            UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).saveUserLevelShowTime(levelShowServiceTime);
                            baseView.homeLevel(levelBaseCacheBean, userDetailInfo);
                        }
                        //展示活动广告
                        else {
                            //要展示活动广告
                            //用户广告时间不同或者用户没有登录 并且广告展示
                            boolean isShowHomeAd = (!userShowDayTime.equals(showDayServiceTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount())) && BaseSharedPreferences.getIsShowState().equals("1");
                            if (isShowHomeAd) {
                                baseView.homeDialogAd(userDetailInfo);
                            }
                            //不展示活动广告
                            else {
                                //直接回调合格投资者弹框
                                baseView.showUserType(userDetailInfo);
                            }
                        }
                    }
                    //等级和首页广告全部无效
                    else {
                        baseView.showUserType(userDetailInfo);
                    }
                }
            });
        }
        //首页广告不需要更新
        else {
            if (!userShowDayTime.equals(showDayServiceTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount())) {
                UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).saveUserDayShowTime(showDayServiceTime);
                baseView.homeDialogAd(userDetailInfo);
            } else {
                baseView.showUserType(userDetailInfo);
            }
        }
    }


    //首页广告弹框请求---注册流程
    public void homeAD() {

        String isUpdate = BaseSharedPreferences.getIsUpdate();

        //首页每日弹框服务器时间
        final String showDayServiceTime = BaseSharedPreferences.getDayShowTime();

        //用户当前存储的弹出广告时间
        final String userShowDayTime = UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).getUserDayShowTime();
        /**
         * isUpdate = 1表示每日一弹图片需要更新
         * !UserInfoSharePre.getIconsUserType().equals(iconUserType) 用户类型不一致 更新
         * !levelShowServiceTime.equals(userLevelTime) 首页等级广告 时间不一致，更新
         * !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount()) 当前账号是否登录过 没有登录，更新
         * **/

        if (isUpdate.equals("1") || !userShowDayTime.equals(showDayServiceTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount())) {

            String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
            ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
            adsPicture.setShowType("12");


            addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

                @Override
                public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {


                    List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> adsPictureList = ads_picture.getData().getAdsPictureList();

                    if (!adsPictureList.isEmpty()) {

                        BaseSharedPreferences.saveIsUpdateSate("0");

                        for (int i = 0; i < adsPictureList.size(); i++) {

                            if (i == 1) {
                                //每日广告
                                BaseCacheBean baseCacheBean = new BaseCacheBean();
                                baseCacheBean.setJumpRule(adsPictureList.get(1).getJumpRule());
                                baseCacheBean.setUuid(adsPictureList.get(1).getUuid());
                                baseCacheBean.setShareUrl(adsPictureList.get(1).getShareUrl());
                                baseCacheBean.setIsShare(adsPictureList.get(1).getIsShare());
                                baseCacheBean.setFuncUrl(adsPictureList.get(1).getFuncUrl());
                                baseCacheBean.setSharePicUrl(adsPictureList.get(1).getSharePicUrl());
                                baseCacheBean.setFuncIcons(adsPictureList.get(1).getFuncIcons());
                                baseCacheBean.setShareDesc(adsPictureList.get(1).getShareStrparam());
                                baseCacheBean.setShareItem(adsPictureList.get(1).getShareItem());
                                baseCacheBean.setShareIsSure(adsPictureList.get(1).getShareIsSure());
                                baseCacheBean.setShareTitle(adsPictureList.get(1).getShareTitle());
                                baseCacheBean.setKeyword(adsPictureList.get(1).getKeyword());
                                baseCacheBean.setIntervalSwitch(adsPictureList.get(1).getIntervalSwitch());
                                baseCacheBean.setIntervalSecond(adsPictureList.get(1).getIntervalSecond());
                                baseCacheBean.setLink_keyword_name(adsPictureList.get(1).getLinkKeywordName());
                                baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));

                                if (adsPictureList.get(1).getIsShare() == null || adsPictureList.get(1).getIsShare().equals("")) {
                                    baseCacheBean.setIsShare("0");
                                } else {
                                    baseCacheBean.setIsShare(adsPictureList.get(1).getIsShare());
                                }

                                Gson gson = new Gson();
                                UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).setHomeAdContent(gson.toJson(baseCacheBean));

                                if (!userShowDayTime.equals(showDayServiceTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount())) {
                                    UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).saveUserDayShowTime(showDayServiceTime);
                                    baseView.homeRegisterAd();
                                }
                                break;
                            }

                        }


                    }

                }
            });

        } else {
            if (!userShowDayTime.equals(showDayServiceTime) || !UserLevelShowTimeSharePre.getUserAccount(UserInfoSharePre.getAccount())) {
                UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).saveUserDayShowTime(showDayServiceTime);
                baseView.homeRegisterAd();
            }

        }


    }


    //理财专区
    public void financeSection() {

        String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType("15");

        addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

            @Override
            public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {

                List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> iconList = ads_picture.getData().getAdsPictureList();

                List<BaseCacheBean> list = new ArrayList<>();

                String result = "";

                Gson gson = new Gson();

                ADUtils adUtils = new ADUtils();

                if (iconList != null && !iconList.isEmpty()) {

                    for (ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture picture : iconList) {

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
                        baseCacheBean.setTitle(picture.getIconsName());
                        baseCacheBean.setShareIsSure(picture.getShareIsSure());
                        baseCacheBean.setKeyword(picture.getKeyword());
                        baseCacheBean.setLink_keyword_name(picture.getLinkKeywordName());
                        baseCacheBean.setShareTitle(picture.getShareTitle());
                        baseCacheBean.setIntervalSwitch(picture.getIntervalSwitch());
                        baseCacheBean.setIntervalSecond(picture.getIntervalSecond());
                        baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));

                        if (picture.getIsShare() == null || picture.getIsShare().equals("")) {
                            baseCacheBean.setIsShare("0");
                        } else {
                            baseCacheBean.setIsShare(picture.getIsShare());
                        }
                        list.add(baseCacheBean);
                    }

                    result = gson.toJson(list);
                    adUtils.setType(ADSharePre.homeSectionLogin);
                    adUtils.setContent(result);
                    adUtils.setResTime(String.valueOf(System.currentTimeMillis()));
                    ADSharePre.saveConfiguration(ADSharePre.homeSectionLogin, adUtils);
                }

                baseView.homeFinancialIcon(list);
            }
        });

    }


    /**
     * 登录之后的icon图
     */
    public void loginIcons() {

        String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType("16");

        addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

            @Override
            public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {

                ADUtils adUtils = new ADUtils();
                Gson gson = new Gson();
                String result = "";

                List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> iconList = ads_picture.getData().getAdsPictureList();


                List<BaseCacheBean> loginIconList = new ArrayList<>();

                if (iconList != null && !iconList.isEmpty()) {


                    for (ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture picture : iconList) {


                        BaseCacheBean baseCacheBean = new BaseCacheBean();
                        baseCacheBean.setJumpRule(picture.getJumpRule());
                        baseCacheBean.setUuid(picture.getUuid());
                        baseCacheBean.setShareUrl(picture.getShareUrl());
                        baseCacheBean.setIsShare(picture.getIsShare());
                        baseCacheBean.setFuncUrl(picture.getFuncUrl());
                        baseCacheBean.setSharePicUrl(picture.getSharePicUrl());
                        baseCacheBean.setFuncIcons(picture.getFuncIcons());
                        baseCacheBean.setShareDesc(picture.getShareStrparam());
                        baseCacheBean.setTitle(picture.getIconsName());
                        baseCacheBean.setShareItem(picture.getShareItem());
                        baseCacheBean.setShareIsSure(picture.getShareIsSure());
                        baseCacheBean.setShareTitle(picture.getShareTitle());
                        baseCacheBean.setKeyword(picture.getKeyword());
                        baseCacheBean.setIntervalSwitch(picture.getIntervalSwitch());
                        baseCacheBean.setIntervalSecond(picture.getIntervalSecond());
                        baseCacheBean.setLink_keyword_name(picture.getLinkKeywordName());
                        baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));

                        if (picture.getIsShare() == null || picture.getIsShare().equals("")) {
                            baseCacheBean.setIsShare("0");
                        } else {
                            baseCacheBean.setIsShare(picture.getIsShare());
                        }

                        loginIconList.add(baseCacheBean);
                    }

                    result = gson.toJson(loginIconList);
                    adUtils.setType(ADSharePre.homeIconsLogin);
                    adUtils.setContent(result);
                    adUtils.setResTime(String.valueOf(System.currentTimeMillis()));
                    ADSharePre.saveConfiguration(ADSharePre.homeIconsLogin, adUtils);

                }

                baseView.homeIconList(loginIconList);


            }
        });
    }

    private void updateUserIcon() {

        String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType("16");

        addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

            @Override
            public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {

                ADUtils adUtils = new ADUtils();
                Gson gson = new Gson();
                String result = "";

                List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> iconList = ads_picture.getData().getAdsPictureList();


                List<BaseCacheBean> loginIconList = new ArrayList<>();

                if (iconList != null && !iconList.isEmpty()) {


                    for (ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture picture : iconList) {

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
                        baseCacheBean.setIntervalSecond(picture.getIntervalSecond());
                        baseCacheBean.setLink_keyword_name(picture.getLinkKeywordName());
                        baseCacheBean.setResTime(String.valueOf(System.currentTimeMillis()));

                        if (picture.getIsShare() == null || picture.getIsShare().equals("")) {
                            baseCacheBean.setIsShare("0");
                        } else {
                            baseCacheBean.setIsShare(picture.getIsShare());
                        }
                        loginIconList.add(baseCacheBean);
                    }

                    result = gson.toJson(loginIconList);
                    adUtils.setType(ADSharePre.homeIconsLogin);
                    adUtils.setContent(result);
                    adUtils.setResTime(String.valueOf(System.currentTimeMillis()));
                    ADSharePre.saveConfiguration(ADSharePre.homeIconsLogin, adUtils);

                }

                baseView.isUpdateUserIcon(loginIconList);

            }
        });
    }


    /**
     * 获取用户购买信息
     */
    public Observable requestQuerySubscribeProductList() {
        Map map = getRequestMap();
        map.put("version", version);
        OrderNotSubscribedProduct.REQ_PBIFE_trade_queryOrderNotSubscribedProduct.Builder builder = OrderNotSubscribedProduct.REQ_PBIFE_trade_queryOrderNotSubscribedProduct.newBuilder();

        builder.setVersion("1.0.1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.GoProduct, map);
        return apiServer.goProduct(url, getBody(builder.build().toByteArray()));
    }

    /**
     * 置成合格投资者合格投资者
     */
    public void initBecomeInvestor() {
        BecomeInvestor.REQ_PBIFE_userinfomanage_becomeInvestor.Builder builder = BecomeInvestor.REQ_PBIFE_userinfomanage_becomeInvestor.newBuilder();
        builder.setVersion("1.0.1");
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.BecomeInvestor, getRequestMap());


        addDisposable(apiServer.initBecomeInvestor(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<BecomeInvestor.Ret_PBIFE_userinfomanage_becomeInvestor>(baseView) {
            @Override
            public void onSuccess(BecomeInvestor.Ret_PBIFE_userinfomanage_becomeInvestor ret_pbife_userinfomanage_becomeInvestor) {
                baseView.initBecomeInvestor();
            }
        });
    }


    /**
     * @param isAuthentication 是否身份认证调用
     */
    public void getUserData(final boolean isAuthentication) {

        addDisposable(getUserInfo(), new ProtoBufObserver<UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo>(baseView) {
            @Override
            public void onSuccess(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
                baseView.onUserInfo(userDetailInfo, isAuthentication);

            }
        });
    }


    /**
     * 合格投资者申请失败原因
     */
    public void requestInvestorStatus(final String isRealInvestor) {
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UserHighNetWorthInfo, getRequestMap());
        UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.Builder builder =
                UserHighNetWorthInfo.REQ_PBIFE_bankcardmanage_getUserHighNetWorthInfo.newBuilder();
        builder.setDynamicType1("highNetWorthUpload");

        addDisposable(apiServer.investorStatus(url, getBody(builder.build().toByteArray())), new BaseObserver<UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo>(baseView) {
            @Override
            public void onSuccess(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body) {
                baseView.requestInvestorStatus(body, isRealInvestor);
            }
        });
    }


    /**
     * 登录Banner
     */
    private Observable loginBanner() {


        String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
        ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
        adsPicture.setShowType("1");

        return apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray()));
    }

    //首页底部列表
    private Observable homeBottomProduct() {

        PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.REQ_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.Builder homeProduct = PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.REQ_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.newBuilder();

        homeProduct.setPageIndex("1");
        homeProduct.setPageSize("10");
        homeProduct.setTerminalNo("12");
        homeProduct.setSpecialQryType("00");
        Map map = getRequestMap();
        map.put("version", FOUR_VERSION);
        String url = parseUrl(MZJ, PBIFE, VICOAZJ, ConstantName.QuerySubscribeProductListNewHome, map);


        return apiServer.homeBottomProduct(url, getBody(homeProduct.build().toByteArray()));
    }

    /**
     * 消息
     **/
    private Observable requestMessage() {

        AllAzjProto.PEAGetMessageCount.Builder message = AllAzjProto.PEAGetMessageCount.newBuilder();
        message.setIconsLocation("messageCenter");
        String url = parseUrl(AZJ, PBMCO, VMCOAZJ, getRequestMap());
        return apiServer.userMassage(url, getBody(message.build().toByteArray()));
    }


    public void getImageListData(List<BaseCacheBean> imageList) {

        if (baseView != null) {
            long initialDelay = 60;
            final StringBuffer buffer = new StringBuffer();
            for (BaseCacheBean cacheBean : imageList) {
                if (cacheBean.getIntervalSwitch().equals("1")) {
                    buffer.append(cacheBean.getUuid()).append(",");
                    try {
                        initialDelay = Long.parseLong(cacheBean.getIntervalSecond());
                    } catch (Exception e) {
                        initialDelay = 60;
                    }
                }
            }
            if (buffer.length() != 0) {
                final String uuids = buffer.deleteCharAt(buffer.length() - 1).toString();
                //final String uuids = buffer.toString();
                RxInterval.getRxInterval().intervalSeconds(initialDelay, new RxInterval.RxAction() {
                    @Override
                    public void action() {
                        //首页图标
                        String fundAccount = UserInfoSharePre.getFundAccount();
                        final UnReadMes.REQ_PBAPP_unreadMes.Builder builder = UnReadMes.REQ_PBAPP_unreadMes.newBuilder();
                        builder.setIconsUuids(uuids);
                        builder.setFundAccount(fundAccount);
                        Map map = getRequestMap();
                        map.put("version", twoVersion);
                        final String url = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.UNREADMES, map);
                        addDisposable(apiServer.getUnReadMsg(url, getBody(builder.build().toByteArray())), new ProtoBufObserver<UnReadMes.Ret_PBAPP_unreadMes>() {
                            @Override
                            public void onSuccess(UnReadMes.Ret_PBAPP_unreadMes unreadMes) {
                                if (unreadMes.getReturnCode().equals(ConstantCode.RETURN_CODE)) {
                                    List<UnReadMes.PBAPP_unreadMes.UnreadMes> unreadMesList = new ArrayList<>();
                                    unreadMesList.addAll(unreadMes.getData().getMesListList());
                                    baseView.getUnreadMesList(unreadMesList);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                RxInterval.getRxInterval().clean();
                            }
                        });
                    }
                });
                RxInterval.getRxInterval().start();
            } else {
                RxInterval.getRxInterval().setInterval(false);
            }
        }
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

    /**
     * 上送用户确认隐私协议的状态及时间
     *
     * @return
     * @method
     * @date: 2020/8/13 15:22
     * @author: moran
     */
    public void onUpUserNoticeStatus(boolean isAuthorizePrivacy, long agressTime) {

        String status = "0";
        String url = parseUrl(MZJ, PBIFE, VREGMZJ, ConstantName.UP_USER_PRIVATE_NOTICE_STATUS, getRequestMap());
        PBIFERegAddUsersRegisterPrivacyAgree.REQ_PBIFE_reg_addUsersRegisterPrivacyAgree.Builder build = PBIFERegAddUsersRegisterPrivacyAgree.REQ_PBIFE_reg_addUsersRegisterPrivacyAgree.newBuilder();
        //表示同意上送
        if (isAuthorizePrivacy) {
            status = "1";
        }
        build.setIsAuthorizePrivacy(status);

        build.setOperTime(Utils.getStringDate(agressTime));

        addDisposable(apiServer.onUpUserNoticeStatus(url, getBody(build.build().toByteArray())), new ProtoBufObserver<PBIFERegAddUsersRegisterPrivacyAgree.Ret_PBIFE_reg_addUsersRegisterPrivacyAgree>() {

            @Override
            public void onSuccess(PBIFERegAddUsersRegisterPrivacyAgree.Ret_PBIFE_reg_addUsersRegisterPrivacyAgree usersRegisterPrivacyAgree) {
                CCLog.i("协议接口上送成功", "true");
            }
        });

    }

    /**
     * 或者交易专区的图片地址和图片链接
     */
    void getTradeImageAndImageUrl(Boolean userIsChange) {

        String isUpdate = BaseSharedPreferences.getTradeIsUpdate();
        if (isUpdate.equals("1") || userIsChange) {

            BaseSharedPreferences.saveTradeIsUpdateSate("0");

            String priceUrl = parseUrl(AZJ, PBAFT, VAFTAZJ, ConstantName.PICTURE);
            ADPictureProtoBuf.REQ_PBAPP_ads_picture.Builder adsPicture = ADPictureProtoBuf.REQ_PBAPP_ads_picture.newBuilder();
            adsPicture.setShowType("18");

            addDisposable(apiServer.onAdPrice(priceUrl, getBody(adsPicture.build().toByteArray())), new ProtoBufObserver<ADPictureProtoBuf.Ret_PBAPP_ads_picture>() {

                @Override
                public void onSuccess(ADPictureProtoBuf.Ret_PBAPP_ads_picture ads_picture) {

                    ADUtils adUtils = new ADUtils();
                    Gson gson = new Gson();
                    String result = "";

                    List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> iconList = ads_picture.getData().getAdsPictureList();

                    List<BaseCacheBean> loginIconList = new ArrayList<>();

                    if (iconList != null && !iconList.isEmpty()) {
                        ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture picture = iconList.get(0);

                        // 缓存数据
                        SharedPreferenceAccesser.saveStringData(
                                AppCache.TRADE_ICON_URL,
                                picture.getFuncIcons()
                        );
                        SharedPreferenceAccesser.saveStringData(
                                AppCache.TRADE_ICON_CLICK_URL,
                                picture.getFuncUrl()
                        );

                        baseView.getIconUrlAndClickUrl(
                                picture.getFuncIcons(),
                                picture.getFuncUrl()
                        );
                    }
                }
            });
        } else {
            // 取缓存
            baseView.getIconUrlAndClickUrl(
                    SharedPreferenceAccesser.getStringData(AppCache.TRADE_ICON_URL),
                    SharedPreferenceAccesser.getStringData(AppCache.TRADE_ICON_CLICK_URL)
            );
        }
    }
}
