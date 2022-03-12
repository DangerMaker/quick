package com.hundsun.zjfae.fragment.home;

import com.hundsun.zjfae.common.base.BaseView;
import com.hundsun.zjfae.common.user.BaseCacheBean;

import java.util.List;

import onight.zjfae.afront.AllAzjProto;
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
 * 首页View
 * */

public interface HomeView extends BaseView {

    /**
     * 请求交易专区是否隐藏或者展示
     * @param isShow "1" 展示 "0" 隐藏
     *
     * */
    void onGetTradeAreaIsShow(String isShow, String title);

    /**
     *未登录Icon
     * @param retPbappAds 未登录Icon
     *
     * */
    void outLoginBanner(BannerProto.Ret_PBAPP_ads retPbappAds);

    /**
     * 未登录/登录首页Icon
     * @param homeIconList 缓存的首页Icon
     * */
    void homeIconList(List<BaseCacheBean> homeIconList);

    /**
     * 更新用户角标
     *@param homeIconList 缓存的首页Icon
     * */
    void isUpdateUserIcon(List<BaseCacheBean> homeIconList);

    /**
     * 未登录首页理财图标
     * @param financialIconList 未登录首页理财图标
     * */
    void homeFinancialIcon(List<BaseCacheBean> financialIconList);


    /**
     *置成合格投资者合格投资者
     * */
    void initBecomeInvestor();

    /**
     * 首页广告
     * */
    void homeDialogAd(UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);


    void homeRegisterAd();

    /**
     * 每月一次的等级广告
     * */
    void homeLevel(BaseCacheBean baseCacheBean,UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);
    /**
     * 登录之后banner图
     * @param banner_adveritse 登录之后banner图
     * */
    void loginBanner(ADPictureProtoBuf.Ret_PBAPP_ads_picture banner_adveritse);


    /**
     * 首页用户产品预约提示
     * @param subscribedProduct 首页用户产品预约
     * */
    void subscribedProduct(OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct subscribedProduct);

    /**
     * 用户详细信息接口
     * @param userDetailInfo 用户详细信息
     * @param homeIconList 首页icon集合
     * @param imageList 理财专区图片集合
     * */
    void userDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo,List<BaseCacheBean> homeIconList,List<BaseCacheBean>  imageList);

    /**
     * 首页底部产品
     * @param productList 首页底部产品
     * */
    void homeBottomProduct(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome productList);

    /**
     * 用户消息
     * @param retMessageCount 用户消息
     * */
    void userMassage(AllAzjProto.PEARetMessageCount retMessageCount);

    /**
     *申请合格投资者用户资料检查
     * @param ret_pbife_userbaseinfo_getUserDetailInfo 用户详细信息
     * */
    void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo ret_pbife_userbaseinfo_getUserDetailInfo,boolean isAuthentication);

    /**
     * 合格投资者申请失败原因
     * @param userHighNetWorthInfo 失败原因
     * @param isRealInvestor 是否真正合格投资者
     * */
    void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo userHighNetWorthInfo, String isRealInvestor);



    /**
     * 列表为空时展示的图片
     *
     * @param isjump = 是否跳转
     * @param jumpurl  跳转形式，url，合格投资者，身份认证
     * @param returnMsg 图片链接
     * @param isShare 是否分享
     *
     * */
    void onInvestmentState(String isjump,String jumpurl,String returnMsg,String isShare);

    /**
     *首页轮播消息
     * @param unreadMesList 轮播消息实体类
     * **/
    void getUnreadMesList( List<UnReadMes.PBAPP_unreadMes.UnreadMes> unreadMesList);




     /**
      * 未设置交易密码，风险等级过期,合格投资者弹框
      * @method
      * @date: 2020/8/13 16:56
      * @author: moran
      * @param userDetailInfo 用户详细信息
      */
    void showUserType(UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo);


    /**
     * 用户隐私
     * @param privacyNotice 接口回调数据
     * @param force 是否强制提醒 true-强制，false-非强制，""无效
     *
     * */
    void onUserPrivateNotice(PrivateNotice.PBAPP_privacyNotice.PrivacyNotice privacyNotice,String force);


    /**
     * 交易专区 或者 商城图标点击回调
     * @param imageUrl
     * @param webUrl
     */
    void getIconUrlAndClickUrl(String imageUrl, String webUrl);

}
