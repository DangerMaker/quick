package com.hundsun.zjfae.fragment.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banner.Banner;
import com.android.banner.BannerConfig;
import com.android.banner.listener.OnBannerListener;
import com.android.banner.loader.ImageLoader;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.UserPrivateNoticeDialog;
import com.hundsun.zjfae.activity.accountcenter.RiskAssessmentActivity;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.home.MessageActivity;
import com.hundsun.zjfae.activity.home.WebActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.mine.FirstPlayPassWordActivity;
import com.hundsun.zjfae.activity.product.OpenHomeReservationProductDialog;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.activity.product.SpvProductDetailActivity;
import com.hundsun.zjfae.activity.product.bean.GoActivityBean;
import com.hundsun.zjfae.common.base.BaseActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.cache.app.AppCache;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.BaseSharedPreferences;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserShowTimeSharedPre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.FingerprintUtil;
import com.hundsun.zjfae.common.utils.RxInterval;
import com.hundsun.zjfae.common.utils.SharedPreferenceAccesser;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.common.view.LimitScrollerView;
import com.hundsun.zjfae.fragment.BaseFragment;
import com.hundsun.zjfae.fragment.home.adapter.LoginIconAdapter;
import com.hundsun.zjfae.fragment.home.adapter.MyLimitScrollAdapter;
import com.hundsun.zjfae.fragment.home.adapter.ProductIconAdapter;
import com.hundsun.zjfae.fragment.home.adapter.RecyclerViewLimitScrollAdapter;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.hundsun.zjfae.fragment.home.view.IndicatorLayout;
import com.hundsun.zjfae.fragment.home.view.dialog.HomeAdvertiseDialogFragment;
import com.hundsun.zjfae.fragment.home.view.dialog.HomeLevelDialog;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import onight.zjfae.afront.AllAzjProto;
import onight.zjfae.afront.gens.v4.PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gens.v3.OrderNotSubscribedProduct;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.PrivateNotice;
import onight.zjfae.afront.gensazj.UnReadMes;
import onight.zjfae.afront.gensazj.v2.BannerProto;


/**
 * @Package: com.hundsun.zjfae.fragment.home
 * @ClassName: HomeFragment
 * @Description: 首页fragment
 * @Author: moran
 * @CreateDate: 2019-11-01 14:06
 * @UpdateUser: 更新者：moran
 * @UpdateDate: 2019-11-01 14:06
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements View.OnClickListener, HomeView {

    private NestedScrollView scrollview;
    private Banner banner;
    private RecyclerView home_icon_recyclerView;
    private RecyclerView home_product_RecyclerView;
    private IndicatorLayout indicator_layout;
    private ImageView message_image;
    private ImageView image_not_date;
    private TextView notification_tv;

    // 交易专区
    private LinearLayout mTradeLl;
    private ImageView mTradeTitleIv;

    //中间小圆标
    private LinearLayout lin_indicator;
    private List<ImageView> indicatorImages;

    private ImageView no_banner_data;

    private LoginIconAdapter loginIconAdapter;

    private ProductIconAdapter productIconAdapter;

    /**
     * 滚动列表布局
     */
    private LimitScrollerView limitScrollerView;
    /**
     * 不滚动列表布局
     */
    private RecyclerView recyclerViewNoScroll;


    /**
     * 首页不滚动产品列表适配器
     */
    private RecyclerViewLimitScrollAdapter recyclerViewLimitScrollAdapter;

    /**
     * 首页底部滚动产品列表适配器
     */
    private MyLimitScrollAdapter myLimitScrollAdapter;

    /**
     * 首页图标
     */

    int scorllX = 0;

    /**
     * 未登录/登录首页Icon
     *
     * @param homeIconList 缓存的首页Icon
     */
    @Override
    public void homeIconList(List<BaseCacheBean> homeIconList) {

        if (!homeIconList.isEmpty()) {

            if (homeIconList.size() <= 8) {
                indicator_layout.setVisibility(View.GONE);
            }

            if (loginIconAdapter != null) {
                loginIconAdapter.refresh(homeIconList);
            } else {
                loginIconAdapter = new LoginIconAdapter(getContext(), homeIconList);

                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, RecyclerView.HORIZONTAL);

                home_icon_recyclerView.setLayoutManager(layoutManager);


                loginIconAdapter.setItemOnclickListener(new LoginIconAdapter.ItemOnclickListener() {
                    @Override
                    public void onItemClickListener(BaseCacheBean baseCacheBean) {

                        onJumpAction(baseCacheBean);


                    }

                });

                home_icon_recyclerView.setAdapter(loginIconAdapter);

                home_icon_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                        int itemCount = recyclerView.getAdapter().getItemCount();

                        //四舍五入
                        itemCount = Math.round((itemCount / 2f));

                        //item总长度,因为是2列，先除2，计算实际item的宽度
                        int itemViewWidth = (int) (recyclerView.getChildAt(0).getMeasuredWidth() * itemCount);

                        //item的长度大于屏幕，否则滑动无意义
                        if (itemViewWidth > recyclerView.getMeasuredWidth()) {
                            indicator_layout.setVisibility(View.VISIBLE);
                            scorllX += dx;
                            //item超过屏幕的宽度
                            float scorllWidth = itemViewWidth - recyclerView.getMeasuredWidth();
                            //计算指示器滑动的距离
                            float viewSlideSpace = scorllX * 1.0f / scorllWidth;
                            //得到指示条滑动总距离
                            int scaleWidth = indicator_layout.getScaleWidth();
                            //得到指示器移动距离
                            float indicatorSlideSpace = viewSlideSpace * scaleWidth;

                            indicator_layout.moveView(indicatorSlideSpace);
                        }
                        //隐藏滑动条
                        else {
                            indicator_layout.setVisibility(View.GONE);
                        }
                    }
                });


            }
        }

    }

    /**
     * 更新用户角标
     *
     * @param homeIconList 缓存的首页Icon
     */
    @Override
    public void isUpdateUserIcon(List<BaseCacheBean> homeIconList) {
        homeIconList(homeIconList);
        presenter.getImageListData(homeIconList);
    }

    /**
     * 首页轮播消息
     *
     * @param unreadMesList 轮播消息实体类
     **/
    @Override
    public void getUnreadMesList(List<UnReadMes.PBAPP_unreadMes.UnreadMes> unreadMesList) {
        loginIconAdapter.setUnreadMesList(unreadMesList);

    }

    /**
     * 首页理财图标
     */
    @Override
    public void homeFinancialIcon(final List<BaseCacheBean> financialIconList) {

        if (!financialIconList.isEmpty()) {

            productIconAdapter = null;
            productIconAdapter = new ProductIconAdapter(mActivity, financialIconList);

            productIconAdapter.setItemClickListener(new ProductIconAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position) {


                    onJumpAction(financialIconList.get(position));
                }
            });
            home_product_RecyclerView.setAdapter(productIconAdapter);


            if (financialIconList.size() > 4) {

                int count = 0;
                if (financialIconList.size() % 4 == 0) {
                    count = financialIconList.size() / 4;
                } else {
                    count = financialIconList.size() / 4 + 1;
                }
                lin_indicator.setVisibility(View.VISIBLE);
                createIndicator(count);
                home_product_RecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            //当停止滑动的时候
                            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                            if (layoutManager instanceof GridLayoutManager) {
                                GridLayoutManager linearManager = (GridLayoutManager) layoutManager;
                                //获取最后一个完全可见view的位置
                                int lastItemPosition = linearManager.findLastCompletelyVisibleItemPosition();

                                for (ImageView imageView : indicatorImages) {
                                    imageView.setImageResource(R.drawable.home_indicator_unselected_radius);
                                }
                                lastItemPosition = lastItemPosition / 4;
                                indicatorImages.get(lastItemPosition).setImageResource(R.drawable.home_indicator_selected_radius);
                            }
                        }
                    }

                });
            } else {

                lin_indicator.setVisibility(View.GONE);
            }

        }
    }

    /**
     * 创建理财产品下面的小点点
     */
    private void createIndicator(int count) {
        indicatorImages = new ArrayList<>();
        lin_indicator.removeAllViews();
        DisplayMetrics dm = mActivity.getResources().getDisplayMetrics();
        int indicatorSize = dm.widthPixels / 40;
        int mIndicatorWidth = indicatorSize;
        int mIndicatorHeight = indicatorSize;
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = 5;
            params.rightMargin = 5;
            if (i == 0) {
                imageView.setImageResource(R.drawable.home_indicator_selected_radius);
            } else {
                imageView.setImageResource(R.drawable.home_indicator_unselected_radius);
            }
            indicatorImages.add(imageView);
            lin_indicator.addView(imageView, params);
        }
    }

    /**
     * 获取
     *
     * @param imageUrl
     * @param webUrl
     */
    @Override
    public void getIconUrlAndClickUrl(String imageUrl, String webUrl) {

        DisplayMetrics dm = Objects.requireNonNull(getContext()).getResources().getDisplayMetrics();

        ViewGroup.LayoutParams layoutParams = mTradeTitleIv.getLayoutParams();
        layoutParams.width = (int) (dm.widthPixels * (130f / 360f));
        layoutParams.height = (int) ((dm.widthPixels * (130f / 360f)) * (49f / 412));
        mTradeTitleIv.requestLayout();
        mTradeTitleIv.setLayoutParams(layoutParams);

        ImageLoad.getImageLoad().LoadImage(
                getContext(),
                imageUrl,
                mTradeTitleIv
        );
        if (StringUtils.isNotBlank(webUrl)) {
            mTradeTitleIv.setOnClickListener(
                    v -> {
                        // 跳转到WebView
                        HomeFragment.this.startWebActivity(webUrl);
                    }
            );
        } else {
            mTradeTitleIv.setOnClickListener(null);
        }
    }

    /**
     * 交易专区模块隐藏或者展示
     *
     * @param isShow "1" 展示 "0" 隐藏
     */
    @Override
    public void onGetTradeAreaIsShow(String isShow, String title) {
        if (isShow.equals("1")) {
            mTradeLl.setVisibility(View.VISIBLE);

            // 获取交易专区 以及  交易专区点击链接
            presenter.getTradeImageAndImageUrl(false);

        } else {
            mTradeLl.setVisibility(View.GONE);
        }
        SharedPreferenceAccesser.saveStringData(
                AppCache.TRADE_AREA_IS_SHOW,
                isShow
        );
    }

    /**
     * 未登录接口调用
     */
    @Override
    public void outLoginBanner(BannerProto.Ret_PBAPP_ads retPbappAds) {
        //banner图
        List<BannerProto.PBAPP_ads.Ads> adsList = retPbappAds.getData().getAdsList();
        if (adsList.isEmpty()) {
            banner.setVisibility(View.GONE);
            no_banner_data.setVisibility(View.VISIBLE);
        } else {
            banner.setVisibility(View.VISIBLE);
            no_banner_data.setVisibility(View.GONE);

            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object mClass, ImageView imageView) {
                    if (mClass instanceof BannerProto.PBAPP_ads.Ads) {
                        BannerProto.PBAPP_ads.Ads adsBean = (BannerProto.PBAPP_ads.Ads) mClass;
                        ImageLoad.getImageLoad().LoadImage(context, adsBean.getFuncIcons(), imageView);
                    }
                }

            }).setIndicatorGravity(BannerConfig.RIGHT);


            banner.setImages(adsList).start();
        }
    }


    /**
     * 登录之后banner图
     *
     * @param banner_adveritse 登录之后banner图
     */
    @Override
    public void loginBanner(ADPictureProtoBuf.Ret_PBAPP_ads_picture banner_adveritse) {
        //banner图
        loginBanner(banner_adveritse.getData().getAdsPictureList());

    }


    /**
     * 首页用户产品预约提示
     *
     * @param subscribedProduct 首页用户产品预约
     */
    @Override
    public void subscribedProduct(OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct
                                          subscribedProduct) {
        //如果有购买的产品 跳转购买页面
        goProduct(subscribedProduct);
    }


    /**
     * 用户详细信息接口
     *
     * @param userDetailInfo 用户详细信息
     * @param homeIconList   首页icon集合
     * @param imageList      理财专区图片集合
     */
    @Override
    public void userDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
                                       userDetailInfo, List<BaseCacheBean> homeIconList, List<BaseCacheBean> imageList) {
        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfoData = userDetailInfo.getData();
        UserInfoSharePre.setTradeAccount(userDetailInfoData.getTradeAccount());
        UserInfoSharePre.setAccount(userDetailInfoData.getAccount());
        UserInfoSharePre.setFundAccount(userDetailInfoData.getFundAccount());
        presenter.upDataHomeLoginIcon(homeIconList, imageList, userDetailInfoData.getIconsUserType());
        //是否提示隐私协议
        presenter.homeAD(userDetailInfoData.getIconsUserType(), userDetailInfoData);
    }

    /**
     * 未设置交易密码，风险等级过期,合格投资者弹框
     *
     * @param userDetailInfo 用户详细信息
     */
    @Override
    public void showUserType(UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        isUserState(userDetailInfo);
    }


    private UserPrivateNoticeDialog privateNoticeDialog = null;

    /**
     * 用户隐私
     *
     * @param privacyNotice 接口回调数据
     * @param force         是否强制提醒 true-强制，false-非强制，""无效
     */
    @Override
    public void onUserPrivateNotice(PrivateNotice.PBAPP_privacyNotice.PrivacyNotice privacyNotice, String force) {

        if (privateNoticeDialog == null) {

            privateNoticeDialog = new UserPrivateNoticeDialog(getActivity());

            privateNoticeDialog.setClickListener(new UserPrivateNoticeDialog.ClickListener() {
                @Override
                public void onAgree(@NotNull DialogInterface dialog, int which) {
                    dialog.dismiss();
                    presenter.onUpUserNoticeStatus(true, System.currentTimeMillis());
                }

                @Override
                public void onClose(@NotNull DialogInterface dialog, int which, boolean force) {
                    dialog.dismiss();
                    presenter.onUpUserNoticeStatus(false, System.currentTimeMillis());
                }

                @Override
                public void startWebActivity(@NotNull String url) {
                    HomeFragment.this.startWebActivity(url);
                }
            });
            privateNoticeDialog.setNoticeTitle(privacyNotice.getNoticeTitle());
            privateNoticeDialog.setLoadUrl(privacyNotice.getNoticeContent());
            privateNoticeDialog.setAgreeText(privacyNotice.getButtonTitle());
            privateNoticeDialog.setForce(false);
            privateNoticeDialog.createDialog().show();
        }
    }


    @Override
    public void homeBottomProduct(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.Ret_PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome data) {
        notification_tv.setVisibility(View.GONE);
        image_not_date.setVisibility(View.GONE);
        initProduct(data.getData());


    }

    @Override
    public void userMassage(AllAzjProto.PEARetMessageCount retMessageCount) {
        //用户信息
        userMessage(retMessageCount.getCount().getUnreadCount());
    }

    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
                                   userDetailInfo, boolean isAuthentication) {
        UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userbaseinfoGetUserDetailInfo = userDetailInfo.getData();

        //首页图标身份认证
        if (isAuthentication) {

            certificateStatusType(userbaseinfoGetUserDetailInfo.getVerifyName(), userbaseinfoGetUserDetailInfo.getCertificateStatusType());

        } else {
            final String highNetWorthStatus = userbaseinfoGetUserDetailInfo.getHighNetWorthStatus();
            final String isRealInvestor = userbaseinfoGetUserDetailInfo.getIsRealInvestor();
            String userType = userbaseinfoGetUserDetailInfo.getUserType();
            if ("false".equals(userDetailInfo.getData().getIsBondedCard()) && "personal".equals(userType)) {

                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(mActivity, AddBankActivity.class);
                    }
                });

            } else if (!"1".equals(userDetailInfo.getData().getIsAccreditedInvestor())) {

                // 合格投资者审核中
                if ("-1".equals(highNetWorthStatus)) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if ("1".equals(userDetailInfo.getData().getIsAccreditedInvestor())) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }
                    showDialog(pmtInfo);
                }
                // 合格投资者审核不通过
                else if ("0".equals(highNetWorthStatus)) {
                    //查询原因
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {
                    if ("personal".equals(userType)) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                }
            }
        }


    }

    @Override
    public void requestInvestorStatus
            (UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo body,
             final String isRealInvestor) {
        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : body.getData().getDictDynamicListList()) {
            if (!dynamic.getAuditComment().equals("0") && !dynamic.getAuditComment().equals("1")) {
                buffer.append(dynamic.getAuditComment()).append("\n");
            }
        }
        showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("isRealInvestor", isRealInvestor);
                intent.setClass(mActivity, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });

    }


    @Override
    public void onInvestmentState(String isjump, final String jumpurl, String returnMsg, final String isShare) {

        image_not_date.setVisibility(View.VISIBLE);
        notification_tv.setVisibility(View.GONE);

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        // 屏幕宽度（像素）
        int width = dm.widthPixels;
        ViewGroup.LayoutParams params = image_not_date.getLayoutParams();
        params.width = width - 50;
        params.height = width - 50;
        image_not_date.setLayoutParams(params);
        image_not_date.setClickable(true);
        image_not_date.setEnabled(true);
        if (!"".equals(returnMsg)) {
            RequestOptions options = new RequestOptions().error(R.drawable.product_no_list).skipMemoryCache(true);
            ImageLoad.getImageLoad().loadImage(this, returnMsg, options, image_not_date);
        } else {
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.product_no_list, image_not_date);
        }

        recyclerViewNoScroll.setVisibility(View.GONE);
        limitScrollerView.setVisibility(View.GONE);
        //跳转
        if ("true".equals(isjump)) {
            image_not_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //url跳转
                    if (jumpurl.contains("http")) {
                        ShareBean shareBean = new ShareBean();
                        shareBean.setFuncUrl(jumpurl);
                        shareBean.setIsShare(isShare);
                        startWebActivity(shareBean);
                    }
                    //身份认证
                    else if ("authentication".equals(jumpurl)) {
                        presenter.getUserData(true);
                    }
                    //合格投资者
                    else if ("callPhoneQualifiedInvestor".equals(jumpurl)) {

                        if (!Utils.isFastDoubleClick()) {
                            presenter.getUserData(false);
                        }
                    } else {
                        image_not_date.setClickable(false);
                        image_not_date.setEnabled(false);
                    }

                }
            });

        } else {
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
        }

    }

    public void goProduct(OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct body) {

        List<OrderNotSubscribedProduct.PBIFE_trade_queryOrderNotSubscribedProduct.TaProductOrderDetailObjectList> detailObjectLists = body.getData().getTaProductOrderDetailObjectListList();

        List<GoActivityBean> list = new ArrayList<>();
        if (!detailObjectLists.isEmpty()) {

            for (OrderNotSubscribedProduct.PBIFE_trade_queryOrderNotSubscribedProduct.TaProductOrderDetailObjectList object : detailObjectLists) {

                GoActivityBean bean = new GoActivityBean();

                bean.setIsDisplayBuy(object.getIsDisplayBuy());
                bean.setAreaJumpType(object.getAreaJumpType());
                bean.setProductCode(object.getOrderProductCode());
                bean.setProductName(object.getProductName());
                bean.setOrderType(object.getOrderType());
                bean.setOrderNum(object.getOrderNum());
                list.add(bean);
            }
        }


        List<OrderNotSubscribedProduct.PBIFE_trade_queryOrderNotSubscribedProduct.TcSpecialOrderDetailObject> specialOrderDetailObjectList = body.getData().getTcSpecialOrderDetailObjectListList();


        if (!specialOrderDetailObjectList.isEmpty()) {

            for (OrderNotSubscribedProduct.PBIFE_trade_queryOrderNotSubscribedProduct.TcSpecialOrderDetailObject object : specialOrderDetailObjectList) {

                GoActivityBean bean = new GoActivityBean();
                bean.setAreaJumpType(object.getAreaJumpType());
                bean.setJumpUrl(object.getJumpUrl());
                bean.setOrderNum(object.getSeriesNo());
                bean.setProductName(object.getSeriesName());
                bean.setIsDisplayBuy(object.getIsDisplayBuy());
                list.add(bean);

            }
        }

        if (!list.isEmpty()) {

            OpenHomeReservationProductDialog productDialog = new OpenHomeReservationProductDialog(getContext());
            productDialog.setData(list);
            productDialog.createDialog().show();

        }

    }

    @Override
    public void homeLevel(final BaseCacheBean baseCacheBean, final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        if (!"".equals(baseCacheBean.getFuncIcons())) {

            final HomeLevelDialog homeLevelDialog = new HomeLevelDialog();
            homeLevelDialog.setImageUrl(baseCacheBean.getFuncIcons());
            homeLevelDialog.setOpenUrl(baseCacheBean.getFuncUrl());
            homeLevelDialog.setJumpRule(baseCacheBean.getJumpRule());

            homeLevelDialog.setLevelOnclick(new HomeLevelDialog.HomeLevelOnclick() {
                @Override
                public void openUrl() {

                    homeLevelDialog.dismissDialog();

                    onJumpAction(baseCacheBean);

                    isUserState(userDetailInfo);
                }

                @Override
                public void close() {
                    homeLevelDialog.dismissDialog();
                    isUserState(userDetailInfo);

                }
            });

            homeLevelDialog.showDialog(getFragmentManager());
        } else {
            isUserState(userDetailInfo);
        }


    }

    @Override
    public void homeDialogAd(final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        //显示首页广告
        if ("1".equals(BaseSharedPreferences.getIsShowState())) {

            String content = UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).getHomeAdContent();
            Gson gson = new Gson();

            final BaseCacheBean baseCacheBean = gson.fromJson(content, BaseCacheBean.class);

            if (baseCacheBean != null) {
                if (!"".equals(baseCacheBean.getFuncIcons())) {

                    final HomeAdvertiseDialogFragment advertiseDialogFragment = new HomeAdvertiseDialogFragment();

                    advertiseDialogFragment.setIconUrl(baseCacheBean.getFuncIcons());
                    UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).saveUserDayShowTime(BaseSharedPreferences.getDayShowTime());

                    advertiseDialogFragment.setOnclick(new HomeAdvertiseDialogFragment.HomeAdOnclick() {
                        @Override
                        public void openUrl() {

                            if (!"0".equals(baseCacheBean.getJumpRule())) {

                                advertiseDialogFragment.dismissDialog();

                                onJumpAction(baseCacheBean);

                                isUserState(userDetailInfo);
                            }

                        }

                        @Override
                        public void close() {
                            advertiseDialogFragment.dismissDialog();
                            isUserState(userDetailInfo);
                        }
                    });


                    advertiseDialogFragment.showDialog(getFragmentManager());
                } else {
                    isUserState(userDetailInfo);
                }
            } else {
                isUserState(userDetailInfo);
            }


        } else {
            isUserState(userDetailInfo);
        }

    }


    @Override
    public void homeRegisterAd() {

        if ("1".equals(BaseSharedPreferences.getIsShowState())) {
            String content = UserShowTimeSharedPre.getInstance(UserInfoSharePre.getAccount()).getHomeAdContent();
            Gson gson = new Gson();


            final BaseCacheBean baseCacheBean = gson.fromJson(content, BaseCacheBean.class);

            if (!"".equals(baseCacheBean.getFuncIcons())) {

                final HomeAdvertiseDialogFragment advertiseDialogFragment = new HomeAdvertiseDialogFragment();

                advertiseDialogFragment.setIconUrl(baseCacheBean.getFuncIcons());

                advertiseDialogFragment.setOnclick(new HomeAdvertiseDialogFragment.HomeAdOnclick() {
                    @Override
                    public void openUrl() {

                        if (!"0".equals(baseCacheBean.getJumpRule())) {
                            advertiseDialogFragment.dismissDialog();
                            onJumpAction(baseCacheBean);
                        }


                    }

                    @Override
                    public void close() {
                        advertiseDialogFragment.dismissDialog();
                    }
                });


                advertiseDialogFragment.showDialog(getFragmentManager());
            }


        }

    }

    /**
     * 消息
     */
    public void userMessage(String unread_count) {
        if (unread_count.equals("0")) {
            message_image.setSelected(false);
        } else {
            message_image.setSelected(true);
        }
    }


    /**
     * banner图
     */
    private void loginBanner(final List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> adsList) {


        if (adsList.isEmpty()) {
            banner.setVisibility(View.GONE);
            no_banner_data.setVisibility(View.VISIBLE);
        } else {
            banner.setVisibility(View.VISIBLE);
            no_banner_data.setVisibility(View.GONE);
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object mClass, ImageView imageView) {
                    if (mClass instanceof ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture) {
                        ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture adsBean = (ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture) mClass;
                        ImageLoad.getImageLoad().LoadImage(context, adsBean.getFuncIcons(), imageView);
                    }

                }

            }).setIndicatorGravity(BannerConfig.RIGHT)
                    //.setOnBannerListener(new HomeBannerListener((HomeActivity) mActivity, adsList));
                    .setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {


                            BaseCacheBean baseCacheBean = new BaseCacheBean();
                            baseCacheBean.setJumpRule(adsList.get(position).getJumpRule());
                            baseCacheBean.setUuid(adsList.get(position).getUuid());
                            baseCacheBean.setShareUrl(adsList.get(position).getShareUrl());
                            baseCacheBean.setIsShare(adsList.get(position).getIsShare());
                            baseCacheBean.setFuncUrl(adsList.get(position).getFuncUrl());
                            baseCacheBean.setSharePicUrl(adsList.get(position).getSharePicUrl());
                            baseCacheBean.setFuncIcons(adsList.get(position).getFuncIcons());
                            baseCacheBean.setShareDesc(adsList.get(position).getShareStrparam());
                            baseCacheBean.setShareItem(adsList.get(position).getShareItem());
                            baseCacheBean.setShareIsSure(adsList.get(position).getShareIsSure());
                            baseCacheBean.setShareTitle(adsList.get(position).getShareTitle());
                            baseCacheBean.setLink_keyword_name(adsList.get(position).getLinkKeywordName());
                            baseCacheBean.setKeyword(adsList.get(position).getKeyword());

                            onJumpAction(baseCacheBean);


                        }
                    });

            banner.setImages(adsList).start();
        }


    }


    /**
     * 底部产品
     */
    private void initProduct(PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome data) {

        List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> detailLists =
                data.getProductTradeInfoListList();

        if (detailLists.isEmpty()) {
            limitScrollerView.setVisibility(View.GONE);
            recyclerViewNoScroll.setVisibility(View.GONE);
            image_not_date.setVisibility(View.VISIBLE);

            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(dm);
            // 屏幕宽度（像素）
            int width = dm.widthPixels;
            ViewGroup.LayoutParams params = image_not_date.getLayoutParams();
            params.width = width - 50;
            params.height = width - 50;
            image_not_date.setLayoutParams(params);
            image_not_date.setClickable(false);
            image_not_date.setEnabled(false);
            ImageLoad.getImageLoad().LoadImage(this, R.drawable.product_no_list, image_not_date);


        } else {
            image_not_date.setVisibility(View.GONE);
            //敬请期待或者已售罄
            product_info(detailLists);
            CCLog.i("DisplayNum", data.getDisplayNum() + "你好呀");
            int displayNum = Integer.parseInt(data.getDisplayNum());
            int scrollTime = Integer.parseInt(data.getScrollTime());
            //不滚动
            image_not_date.setVisibility(View.GONE);
            //不滚动
            if (displayNum >= detailLists.size()) {
                displayNum = detailLists.size();
                recyclerViewNoScroll.setVisibility(View.VISIBLE);
                limitScrollerView.setVisibility(View.GONE);
                recyclerViewLimitScrollAdapter = new RecyclerViewLimitScrollAdapter(mActivity, detailLists, displayNum);
                recyclerViewNoScroll.setLayoutManager(new LinearLayoutManager(mActivity));
                recyclerViewNoScroll.setAdapter(recyclerViewLimitScrollAdapter);
            } else {
                limitScrollerView.setVisibility(View.VISIBLE);
                recyclerViewNoScroll.setVisibility(View.GONE);
                myLimitScrollAdapter = new MyLimitScrollAdapter(mActivity, detailLists);
                limitScrollerView.setDataAdapter(myLimitScrollAdapter);
                limitScrollerView.setPeriodTime(scrollTime);
                limitScrollerView.setLimit(displayNum);
                limitScrollerView.cancel();
                limitScrollerView.startScroll();

            }


        }


    }


    /**
     * 查询底部产品状态信息
     */
    private void product_info
    (List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> detailLists) {

        //产品状态
        int status = 0;
        for (PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject detail : detailLists) {
            if (!detail.getSellingStatus().equals("1")) {
                status = status + 1;
            }
        }
        if (status == detailLists.size()) {
            //所有产品--敬请期待
            BaseCacheBean homeAd = ADSharePre.getConfiguration(ADSharePre.homeProductNoDate, BaseCacheBean.class);

            if (homeAd != null) {
                String content = (homeAd.getContent() != null && !homeAd.getContent().equals("")) ? homeAd.getContent() : "";
                if (!content.equals("")) {
                    notification_tv.setVisibility(View.VISIBLE);
                    content = content.replaceFirst("\\n", "").trim();
                    notification_tv.setText(content);
                }

            }
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        limitScrollerView.cancel();
        RxInterval.getRxInterval().clean();
    }


    @Override
    public void onStart() {
        super.onStart();
        limitScrollerView.startScroll();
        if (BaseActivity.isLogin) {
            RxInterval.getRxInterval().start();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_layout:
                mActivity.startActivity(new Intent(getContext(), MessageActivity.class));
                break;
            default:
                break;
        }
    }

    String isRealInvestor = "";

    protected void isUserState(UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo
                                       userDetailInfo) {

        String isRealInvestor = userDetailInfo.getIsRealInvestor();

        if ("1".equals(userDetailInfo.getIsAuthorizePrivacy())) {

            presenter.onUpUserNoticeStatus(true, System.currentTimeMillis());

        } else if ("false".equals(userDetailInfo.getIsFundPasswordSet())) {
            showDialog("您还尚未设置交易密码哦", "确定", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去设置交易密码
                    dialog.dismiss();
                    baseStartActivity(mActivity, FirstPlayPassWordActivity.class);
                }
            });
        } else if ("false".equals(userDetailInfo.getIsBondedCard()) && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //去绑卡
                    dialog.dismiss();
                    baseStartActivity(mActivity, AddBankActivity.class);
                }
            });

        }
        //个人用户非合格投资者弹框
        else if ("1".equals(userDetailInfo.getAccreditedTipsFlag())) {
            //非合格投资者弹框
            if ("personal".equals(userDetailInfo.getUserType())) {

                startEligibleInvestGuideActivity("000", isRealInvestor);
            } else {
                //机构用户
                //非合格投资者弹框
                startEligibleInvestGuideActivity("020", isRealInvestor);
            }


        } else if ("2".equals(userDetailInfo.getAccreditedTipsFlag()) && "personal".equals(userDetailInfo.getUserType())) {
            //恭喜弹框
            presenter.initBecomeInvestor();
        } else if (!"1".equals(userDetailInfo.getIsRealInvestor()) && !"-1".equals(userDetailInfo.getHighNetWorthStatus()) && !"0".equals(userDetailInfo.getHighNetWorthStatus()) && ("".equals(userDetailInfo.getAccreditedTipsFlag()))) {

            //以前是非合格投资者的
            if (!"1".equals(userDetailInfo.getIsRealInvestor())) {

                if (!"".equals(userDetailInfo.getAccreditedTipsFlag())) {
                    //恭喜弹框
                    presenter.initBecomeInvestor();
                } else {
                    //个人用户-非合格投资者弹框
                    if ("personal".equals(userDetailInfo.getUserType())) {

                        startEligibleInvestGuideActivity("000", isRealInvestor);
                    }
                    //机构用户-非合格投资者弹框
                    else if ("company".equals(userDetailInfo.getUserType())) {
                        startEligibleInvestGuideActivity("020", isRealInvestor);
                    }

                }

            } else {
                startEligibleInvestGuideActivity("010", isRealInvestor);
            }

        } else if ("false".equals(userDetailInfo.getIsRiskTest()) && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("还未进行风险评测，先去评测?", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //去风评
                    baseStartActivity(mActivity, RiskAssessmentActivity.class);
                }
            });
        } else if ("true".equals(userDetailInfo.getIsRiskExpired()) && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("您的风险评测已经过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //去风评
                    baseStartActivity(mActivity, RiskAssessmentActivity.class);
                }
            });
        } else if (Integer.parseInt(!userDetailInfo.getRiskAssessment().equals("") ? userDetailInfo.getRiskAssessment() : "0") < 31 && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("您的风险评测即将过期，请重新进行评测~", "去风评", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    baseStartActivity(mActivity, RiskAssessmentActivity.class);
                }
            });
        }

    }

    @Override
    public void initBecomeInvestor() {
        //004恭喜弹框
        startEligibleInvestGuideActivity("004", isRealInvestor);
    }


    public void startEligibleInvestGuideActivity(String type, String isRealInvestor) {

        showUserLevelDialog(type, isRealInvestor);

    }

    @Override
    public void initData() {
        if (presenter == null) {
            return;
        }
        if (BaseActivity.isLogin) {
            limitScrollerView.setVisibility(View.GONE);
            recyclerViewNoScroll.setVisibility(View.GONE);
            presenter.navigationData();

            if (BaseActivity.isRegister) {
                BaseActivity.isRegister = false;
                presenter.homeAD();
            }
        } else {
            presenter.outLoginInfo();
        }

        presenter.getDictionary();
    }

    public void loginDate() {
        if (FingerprintUtil.callFingerPrint(mActivity)) {
            //设备支持指纹登录
            if (UserInfoSharePre.getFingerprintLogin() == false && UserInfoSharePre.getGestureLoginType() == false) {
                showToast("您可以到账户中心设置手势密码或指纹密码哦~");
            }
        } else {
            //设备不支持指纹登录
            if (UserInfoSharePre.getGestureLoginType() == false) {
                showToast("您可以到账户中心设置手势密码或指纹密码哦~");
            }
        }
        presenter.loginInfo();
    }


    @Override
    public void initWidget() {
        scrollview = (NestedScrollView) findViewById(R.id.scrollview);
        banner = (Banner) findViewById(R.id.banner);
        home_icon_recyclerView = (RecyclerView) findViewById(R.id.home_icon_recyclerView);
        indicator_layout = (IndicatorLayout) findViewById(R.id.indicator_layout);

        home_product_RecyclerView = (RecyclerView) findViewById(R.id.rl_home_product);
        home_product_RecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 1, GridLayoutManager.HORIZONTAL, false));

        message_image = (ImageView) findViewById(R.id.message_image);
        limitScrollerView = (LimitScrollerView) findViewById(R.id.limitScrollerView);

        mTradeLl = (LinearLayout) findViewById(R.id.mTradeAreaLl);
        mTradeTitleIv = (ImageView) findViewById(R.id.mTradeTitleIv);

        limitScrollerView.setOnItemClickListener(new LimitScrollerView.OnItemClickListener() {
            @Override
            public void onItemClick(Object obj) {

                if (obj instanceof PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject) {

                    PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject detail = (PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject) obj;

                    if (Utils.isFastDoubleClick()) {
                        return;
                    }

                    Intent intent = new Intent();
                    intent.putExtra("productCode", detail.getProductCode());
                    intent.putExtra("sellingStatus", detail.getSellingStatus());

                    if (!"05".equals(detail.getProductSpecialArea())) {
                        intent.putExtra("delegationCode", detail.getDelegationCode());
                        intent.putExtra("delegationId", detail.getDelegationId());
                        intent.putExtra("leftDays", detail.getLeftDays());
                        intent.putExtra("ifAllBuy", detail.getIfAllBuy());
                        intent.putExtra("delegateNum", detail.getDelegateNum());
                        intent.setClass(mActivity, SpvProductDetailActivity.class);
                    } else {
                        intent.setClass(mActivity, ProductCodeActivity.class);
                    }

                    baseStartActivity(intent);
                }
            }
        });
        image_not_date = (ImageView) findViewById(R.id.image_not_date);
        notification_tv = (TextView) findViewById(R.id.notification_tv);
        lin_indicator = (LinearLayout) findViewById(R.id.lin_indicator);
        no_banner_data = (ImageView) findViewById(R.id.no_banner_data);
        findViewById(R.id.message_layout).setOnClickListener(this);
        //打开侧边栏
        findViewById(R.id.img_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity) mActivity).showMenu();
            }
        });
        recyclerViewNoScroll = (RecyclerView) findViewById(R.id.recyclerViewNoScroll);

    }

    @Override
    protected void onRefresh() {
        presenter.navigationData();
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    @Override
    protected void resetLayout() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.home_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.home_fragment_layout;
    }

    @Override
    public boolean isShowLoad() {
        return false;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        invokeFragmentManagerNoteStateNotSaved();
    }


    private Method noteStateNotSavedMethod;
    private Object fragmentMgr;
    private String[] activityClassName = {"Activity", "FragmentActivity"};


    private void invokeFragmentManagerNoteStateNotSaved() {
        //java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return;
        }
        try {
            if (noteStateNotSavedMethod != null && fragmentMgr != null) {
                noteStateNotSavedMethod.invoke(fragmentMgr);
                return;
            }
            Class cls = getClass();
            do {
                cls = cls.getSuperclass();
            } while (!(activityClassName[0].equals(cls.getSimpleName())
                    || activityClassName[1].equals(cls.getSimpleName())));

            Field fragmentMgrField = prepareField(cls, "mFragments");
            if (fragmentMgrField != null) {
                fragmentMgr = fragmentMgrField.get(this);
                noteStateNotSavedMethod = getDeclaredMethod(fragmentMgr, "noteStateNotSaved");
                if (noteStateNotSavedMethod != null) {
                    noteStateNotSavedMethod.invoke(fragmentMgr);
                }
            }

        } catch (Exception ex) {
        }
    }

    private Field prepareField(Class<?> c, String fieldName) throws NoSuchFieldException {
        while (c != null) {
            try {
                Field f = c.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } finally {
                c = c.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }

    private Method getDeclaredMethod(Object object, String methodName, Class<?>...
            parameterTypes) {
        Method method = null;
        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {
            }
        }
        return null;
    }


}