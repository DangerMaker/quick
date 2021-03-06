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
 * @Description: ??????fragment
 * @Author: moran
 * @CreateDate: 2019-11-01 14:06
 * @UpdateUser: ????????????moran
 * @UpdateDate: 2019-11-01 14:06
 * @UpdateRemark: ???????????????
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

    // ????????????
    private LinearLayout mTradeLl;
    private ImageView mTradeTitleIv;

    //???????????????
    private LinearLayout lin_indicator;
    private List<ImageView> indicatorImages;

    private ImageView no_banner_data;

    private LoginIconAdapter loginIconAdapter;

    private ProductIconAdapter productIconAdapter;

    /**
     * ??????????????????
     */
    private LimitScrollerView limitScrollerView;
    /**
     * ?????????????????????
     */
    private RecyclerView recyclerViewNoScroll;


    /**
     * ????????????????????????????????????
     */
    private RecyclerViewLimitScrollAdapter recyclerViewLimitScrollAdapter;

    /**
     * ???????????????????????????????????????
     */
    private MyLimitScrollAdapter myLimitScrollAdapter;

    /**
     * ????????????
     */

    int scorllX = 0;

    /**
     * ?????????/????????????Icon
     *
     * @param homeIconList ???????????????Icon
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

                        //????????????
                        itemCount = Math.round((itemCount / 2f));

                        //item?????????,?????????2????????????2???????????????item?????????
                        int itemViewWidth = (int) (recyclerView.getChildAt(0).getMeasuredWidth() * itemCount);

                        //item?????????????????????????????????????????????
                        if (itemViewWidth > recyclerView.getMeasuredWidth()) {
                            indicator_layout.setVisibility(View.VISIBLE);
                            scorllX += dx;
                            //item?????????????????????
                            float scorllWidth = itemViewWidth - recyclerView.getMeasuredWidth();
                            //??????????????????????????????
                            float viewSlideSpace = scorllX * 1.0f / scorllWidth;
                            //??????????????????????????????
                            int scaleWidth = indicator_layout.getScaleWidth();
                            //???????????????????????????
                            float indicatorSlideSpace = viewSlideSpace * scaleWidth;

                            indicator_layout.moveView(indicatorSlideSpace);
                        }
                        //???????????????
                        else {
                            indicator_layout.setVisibility(View.GONE);
                        }
                    }
                });


            }
        }

    }

    /**
     * ??????????????????
     *
     * @param homeIconList ???????????????Icon
     */
    @Override
    public void isUpdateUserIcon(List<BaseCacheBean> homeIconList) {
        homeIconList(homeIconList);
        presenter.getImageListData(homeIconList);
    }

    /**
     * ??????????????????
     *
     * @param unreadMesList ?????????????????????
     **/
    @Override
    public void getUnreadMesList(List<UnReadMes.PBAPP_unreadMes.UnreadMes> unreadMesList) {
        loginIconAdapter.setUnreadMesList(unreadMesList);

    }

    /**
     * ??????????????????
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
                            //????????????????????????
                            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                            if (layoutManager instanceof GridLayoutManager) {
                                GridLayoutManager linearManager = (GridLayoutManager) layoutManager;
                                //??????????????????????????????view?????????
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
     * ????????????????????????????????????
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
     * ??????
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
                        // ?????????WebView
                        HomeFragment.this.startWebActivity(webUrl);
                    }
            );
        } else {
            mTradeTitleIv.setOnClickListener(null);
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param isShow "1" ?????? "0" ??????
     */
    @Override
    public void onGetTradeAreaIsShow(String isShow, String title) {
        if (isShow.equals("1")) {
            mTradeLl.setVisibility(View.VISIBLE);

            // ?????????????????? ??????  ????????????????????????
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
     * ?????????????????????
     */
    @Override
    public void outLoginBanner(BannerProto.Ret_PBAPP_ads retPbappAds) {
        //banner???
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
     * ????????????banner???
     *
     * @param banner_adveritse ????????????banner???
     */
    @Override
    public void loginBanner(ADPictureProtoBuf.Ret_PBAPP_ads_picture banner_adveritse) {
        //banner???
        loginBanner(banner_adveritse.getData().getAdsPictureList());

    }


    /**
     * ??????????????????????????????
     *
     * @param subscribedProduct ????????????????????????
     */
    @Override
    public void subscribedProduct(OrderNotSubscribedProduct.Ret_PBIFE_trade_queryOrderNotSubscribedProduct
                                          subscribedProduct) {
        //???????????????????????? ??????????????????
        goProduct(subscribedProduct);
    }


    /**
     * ????????????????????????
     *
     * @param userDetailInfo ??????????????????
     * @param homeIconList   ??????icon??????
     * @param imageList      ????????????????????????
     */
    @Override
    public void userDetailInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
                                       userDetailInfo, List<BaseCacheBean> homeIconList, List<BaseCacheBean> imageList) {
        final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfoData = userDetailInfo.getData();
        UserInfoSharePre.setTradeAccount(userDetailInfoData.getTradeAccount());
        UserInfoSharePre.setAccount(userDetailInfoData.getAccount());
        UserInfoSharePre.setFundAccount(userDetailInfoData.getFundAccount());
        presenter.upDataHomeLoginIcon(homeIconList, imageList, userDetailInfoData.getIconsUserType());
        //????????????????????????
        presenter.homeAD(userDetailInfoData.getIconsUserType(), userDetailInfoData);
    }

    /**
     * ??????????????????????????????????????????,?????????????????????
     *
     * @param userDetailInfo ??????????????????
     */
    @Override
    public void showUserType(UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {

        isUserState(userDetailInfo);
    }


    private UserPrivateNoticeDialog privateNoticeDialog = null;

    /**
     * ????????????
     *
     * @param privacyNotice ??????????????????
     * @param force         ?????????????????? true-?????????false-????????????""??????
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
        //????????????
        userMessage(retMessageCount.getCount().getUnreadCount());
    }

    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo
                                   userDetailInfo, boolean isAuthentication) {
        UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userbaseinfoGetUserDetailInfo = userDetailInfo.getData();

        //????????????????????????
        if (isAuthentication) {

            certificateStatusType(userbaseinfoGetUserDetailInfo.getVerifyName(), userbaseinfoGetUserDetailInfo.getCertificateStatusType());

        } else {
            final String highNetWorthStatus = userbaseinfoGetUserDetailInfo.getHighNetWorthStatus();
            final String isRealInvestor = userbaseinfoGetUserDetailInfo.getIsRealInvestor();
            String userType = userbaseinfoGetUserDetailInfo.getUserType();
            if ("false".equals(userDetailInfo.getData().getIsBondedCard()) && "personal".equals(userType)) {

                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(mActivity, AddBankActivity.class);
                    }
                });

            } else if (!"1".equals(userDetailInfo.getData().getIsAccreditedInvestor())) {

                // ????????????????????????
                if ("-1".equals(highNetWorthStatus)) {
                    String pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                    if ("1".equals(userDetailInfo.getData().getIsAccreditedInvestor())) {
                        pmtInfo = "????????????????????????????????????????????????";
                    }
                    showDialog(pmtInfo);
                }
                // ??????????????????????????????
                else if ("0".equals(highNetWorthStatus)) {
                    //????????????
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
        showDialog(buffer.toString() + "".trim(), "????????????", "??????", new DialogInterface.OnClickListener() {
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
        // ????????????????????????
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
        //??????
        if ("true".equals(isjump)) {
            image_not_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //url??????
                    if (jumpurl.contains("http")) {
                        ShareBean shareBean = new ShareBean();
                        shareBean.setFuncUrl(jumpurl);
                        shareBean.setIsShare(isShare);
                        startWebActivity(shareBean);
                    }
                    //????????????
                    else if ("authentication".equals(jumpurl)) {
                        presenter.getUserData(true);
                    }
                    //???????????????
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
        //??????????????????
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
     * ??????
     */
    public void userMessage(String unread_count) {
        if (unread_count.equals("0")) {
            message_image.setSelected(false);
        } else {
            message_image.setSelected(true);
        }
    }


    /**
     * banner???
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
     * ????????????
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
            // ????????????????????????
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
            //???????????????????????????
            product_info(detailLists);
            CCLog.i("DisplayNum", data.getDisplayNum() + "?????????");
            int displayNum = Integer.parseInt(data.getDisplayNum());
            int scrollTime = Integer.parseInt(data.getScrollTime());
            //?????????
            image_not_date.setVisibility(View.GONE);
            //?????????
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
     * ??????????????????????????????
     */
    private void product_info
    (List<PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject> detailLists) {

        //????????????
        int status = 0;
        for (PBIFEPrdsubscribequeryPrdQuerySubscribeTradeProductListNewHome.PBIFE_prdsubscribequery_prdQuerySubscribeTradeProductListNewHome.ProductTradeInfoNewObject detail : detailLists) {
            if (!detail.getSellingStatus().equals("1")) {
                status = status + 1;
            }
        }
        if (status == detailLists.size()) {
            //????????????--????????????
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
            showDialog("?????????????????????????????????", "??????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????????????????
                    dialog.dismiss();
                    baseStartActivity(mActivity, FirstPlayPassWordActivity.class);
                }
            });
        } else if ("false".equals(userDetailInfo.getIsBondedCard()) && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //?????????
                    dialog.dismiss();
                    baseStartActivity(mActivity, AddBankActivity.class);
                }
            });

        }
        //????????????????????????????????????
        else if ("1".equals(userDetailInfo.getAccreditedTipsFlag())) {
            //????????????????????????
            if ("personal".equals(userDetailInfo.getUserType())) {

                startEligibleInvestGuideActivity("000", isRealInvestor);
            } else {
                //????????????
                //????????????????????????
                startEligibleInvestGuideActivity("020", isRealInvestor);
            }


        } else if ("2".equals(userDetailInfo.getAccreditedTipsFlag()) && "personal".equals(userDetailInfo.getUserType())) {
            //????????????
            presenter.initBecomeInvestor();
        } else if (!"1".equals(userDetailInfo.getIsRealInvestor()) && !"-1".equals(userDetailInfo.getHighNetWorthStatus()) && !"0".equals(userDetailInfo.getHighNetWorthStatus()) && ("".equals(userDetailInfo.getAccreditedTipsFlag()))) {

            //??????????????????????????????
            if (!"1".equals(userDetailInfo.getIsRealInvestor())) {

                if (!"".equals(userDetailInfo.getAccreditedTipsFlag())) {
                    //????????????
                    presenter.initBecomeInvestor();
                } else {
                    //????????????-????????????????????????
                    if ("personal".equals(userDetailInfo.getUserType())) {

                        startEligibleInvestGuideActivity("000", isRealInvestor);
                    }
                    //????????????-????????????????????????
                    else if ("company".equals(userDetailInfo.getUserType())) {
                        startEligibleInvestGuideActivity("020", isRealInvestor);
                    }

                }

            } else {
                startEligibleInvestGuideActivity("010", isRealInvestor);
            }

        } else if ("false".equals(userDetailInfo.getIsRiskTest()) && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //?????????
                    baseStartActivity(mActivity, RiskAssessmentActivity.class);
                }
            });
        } else if ("true".equals(userDetailInfo.getIsRiskExpired()) && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //?????????
                    baseStartActivity(mActivity, RiskAssessmentActivity.class);
                }
            });
        } else if (Integer.parseInt(!userDetailInfo.getRiskAssessment().equals("") ? userDetailInfo.getRiskAssessment() : "0") < 31 && "personal".equals(userDetailInfo.getUserType())) {
            showDialog("??????????????????????????????????????????????????????~", "?????????", "??????", new DialogInterface.OnClickListener() {
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
        //004????????????
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
            //????????????????????????
            if (UserInfoSharePre.getFingerprintLogin() == false && UserInfoSharePre.getGestureLoginType() == false) {
                showToast("????????????????????????????????????????????????????????????~");
            }
        } else {
            //???????????????????????????
            if (UserInfoSharePre.getGestureLoginType() == false) {
                showToast("????????????????????????????????????????????????????????????~");
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
        //???????????????
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