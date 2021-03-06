package com.hundsun.zjfae;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.hundsun.zjfae.activity.accountcenter.AccountCenterActivity;
import com.hundsun.zjfae.activity.forget.ForgetPasswordActivity;
import com.hundsun.zjfae.activity.home.ActivityUserInfo;
import com.hundsun.zjfae.activity.home.HighNetWorthMaterialsUploadedActivity;
import com.hundsun.zjfae.activity.login.FingerprintDialogFragment;
import com.hundsun.zjfae.activity.login.LoginDialogFragment;
import com.hundsun.zjfae.activity.login.LoginInfoListener;
import com.hundsun.zjfae.activity.logingesture.GestureLoginActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.activity.moneymanagement.MyHoldingActivity;
import com.hundsun.zjfae.activity.mymessage.MyMessageActivity;
import com.hundsun.zjfae.activity.product.OpenAttachmentActivity;
import com.hundsun.zjfae.activity.product.bean.AttachmentEntity;
import com.hundsun.zjfae.activity.register.RegisterActivity;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.cookies.PersistentCookieStore;
import com.hundsun.zjfae.common.service.UpLoadFileJobIntentService;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.PhoneInfo;
import com.hundsun.zjfae.common.user.UserAgreementSetting;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.user.UserSetting;
import com.hundsun.zjfae.common.user.UserShowTimeSharedPre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.Countdown;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.FingerprintUtil;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.hundsun.zjfae.common.utils.stepcounter.StepServiceClass;
import com.hundsun.zjfae.common.utils.stepcounter.utils.StepCountCheckUtil;
import com.hundsun.zjfae.common.utils.stepcounter.utils.TimeUtil;
import com.hundsun.zjfae.common.view.NavigationButton;
import com.hundsun.zjfae.fragment.account.AccountFragment;
import com.hundsun.zjfae.fragment.account.AssetDetailActivity;
import com.hundsun.zjfae.fragment.finance.FinanceFragment;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.HomeFragment;
import com.hundsun.zjfae.fragment.more.MoreFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zjfae.library.update.UpdateFragment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Cookie;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.PrivateNotice;
import onight.zjfae.afront.gensazj.v2.Login;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae
 * @ClassName: HomeActivity
 * @Description: ?????????????????????fragment
 * @Author: moran
 * @CreateDate: 2019/6/14 13:51
 * @UpdateUser: ????????????
 * @UpdateDate: 2019/6/14 13:51
 * @UpdateRemark: ???????????????
 * @Version: 1.0
 */
//@RuntimePermissions
public class HomeActivity extends CommActivity<HomePresenter> implements View.OnClickListener, HomeView {
    private SlidingMenu slidingMenu;
    private NavigationButton mNavHomes;
    private NavigationButton mNavProduct;
    private NavigationButton mNavMine;
    private NavigationButton mNavMore;
    private FragmentManager mFragmentManager;
    private NavigationButton mCurrentNavButton;
    //????????????
    private ImageView picImageView;
    private String picUrl = "";

    private static HomeFragmentType fragmentType = HomeFragmentType.HOME_FRAGMENT;

    public static boolean isTransferList = false;

    //???????????????
    public static final int GESTURELOGIN_REQUEST_CODE = 0x100;
    private static final int RESULT_ERROR_CODE = 0x9245;
    //??????????????????
    private boolean gestureCount = false;

    //??????
    private static final int REGISTER_REQUEST_CODE = 0X191;
    //????????????
    private static final int CASULA_CODE = 0x759;
    /**
     * ?????????????????????????????????????????? ????????????????????????????????? ?????????????????????????????????
     */
    private boolean isOpen = false;


    private static boolean isJPush = false;

    private StepServiceClass stepServiceClass;


    private BaseCacheBean baseCacheBean = null;
    private UserPrivateNoticeDialog privateNoticeDialog = null;

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter(this);
    }


    public void showMenu() {
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        } else {
            refreshUserInfo(true);
        }
    }

    @Override
    public void onClick(View v) {

        ProductDate.rest();
        if (v instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) v;


            if (nav != mCurrentNavButton) {
                doSelect(nav);
                selectNavigationButton(nav);
            }

        }
    }

    private void initFragment() {

        mNavHomes.init(R.drawable.tab_icon_new,
                R.string.main_tab_name_home,
                HomeFragment.class);

        mNavProduct.init(R.drawable.tab_icon_tweet,
                R.string.main_tab_name_product,
                FinanceFragment.class);

        mNavMine.init(R.drawable.tab_icon_me,
                R.string.main_tab_name_my,
                AccountFragment.class);

        mNavMore.init(R.drawable.tab_icon_explore,
                R.string.main_tab_name_more,
                MoreFragment.class);

        // do clear
        clearOldFragment();
        // do select first

        switch (fragmentType) {
            case HOME_FRAGMENT:
                doSelect(mNavHomes);
                break;
            case PRODUCT_FRAGMENT:
                doSelect(mNavProduct);
                break;
            case MINE_FRAGMENT:
                doSelect(mNavMine);
                break;
            case MORE_FRAGMENT:
                doSelect(mNavMore);
                break;
            default:
                break;
        }


    }


    //banner?????????,icon?????????
    public void setProductDate() {
        doSelect(mNavProduct);
        selectNavigationButton(mNavProduct);
    }


    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0) {
            return;
        }

        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != null) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit) {
            transaction.commitNow();
        }

    }

    private void doSelect(NavigationButton newNavButton) {
        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                return;
            }
            oldNavButton.setSelected(false);
        }
        addFragment(newNavButton);
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mCurrentNavButton = newNavButton;
    }

    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(this,
                        newNavButton.getClx().getName(), null);
                ft.add(R.id.main_container, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }


    private void selectNavigationButton(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment instanceof HomeFragment) {
            HomeFragment homeFragment = (HomeFragment) fragment;
            homeFragment.initData();
        } else if (fragment instanceof FinanceFragment) {
            FinanceFragment financeFragment = (FinanceFragment) fragment;
            financeFragment.initData();
        } else if (fragment instanceof AccountFragment) {
            AccountFragment accountFragment = (AccountFragment) fragment;
            accountFragment.initData();
        } else if (fragment instanceof MoreFragment) {

            MoreFragment moreFragment = (MoreFragment) fragment;
            moreFragment.initData();

        }
    }


    //????????????
    private void login(String userName, String passWord, String loginMethod, String accountCode, String needValidateAuthCode) {
        PhoneInfo phoneInfo = PhoneInfo.getPhoneInfo();
        Login.REQ_PBAPP_login.Builder builder = Login.REQ_PBAPP_login.newBuilder();
        builder.setUsername(userName);
        builder.setPassword(passWord);
        builder.setRoutingAddress(phoneInfo.routingAddress);
        builder.setGetStatus(phoneInfo.phoneNumberStatus);
        builder.setPhoneNum(phoneInfo.phoneNumber);
        builder.setLocationInfo(os.toString());
        builder.setAuthCode(accountCode);
        builder.setNeedValidateAuthCode(needValidateAuthCode);
        builder.setGessturePwd(EncDecUtil.AESEncrypt(UserInfoSharePre.getFingerprintPassWordBefore()));
        builder.setLoginType("1");


        //0??????????????????
        builder.setLoginMethod(loginMethod);
        builder.setIsOpenGpwd(loginMethod);

        UserAgreementSetting.setAgreementVersion(userName, loginDialogFragment.getAgreementVersion());
        presenter.login(builder.build().toByteArray(), loginMethod);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //?????????????????????
        if (requestCode == GESTURELOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            login(UserInfoSharePre.getUserName(), UserInfoSharePre.getPassWord(), "1", "", "");
        }
        //?????????????????????
        else if (requestCode == GESTURELOGIN_REQUEST_CODE && resultCode == RESULT_ERROR_CODE) {
            gestureCount = true;
        }
        //????????????--->????????????
        else if (requestCode == REGISTER_REQUEST_CODE && resultCode == CASULA_CODE) {
            loginDialogFragment.dismissDialog();
            doSelect(mNavProduct);
            FinanceFragment financeFragment = (FinanceFragment) mNavProduct.getFragment();
            financeFragment.initData();
        }
        //?????????????????????????????????
        else if (requestCode == REGISTER_REQUEST_CODE && resultCode == RESULT_OK) {
            loginDialogFragment.dismissDialog();
            doSelect(mNavHomes);
            Countdown.getInstance().start(1, new Countdown.ActionInterFace() {
                @Override
                public void action() {
                    HomeFragment fragment = (HomeFragment) mCurrentNavButton.getFragment();
                    fragment.loginDate();
                    Countdown.getInstance().clean();
                }
            });


        }

    }

    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    protected void topDefineCancel() {

        if (mCurrentNavButton != mNavHomes) {
            doSelect(mNavHomes);
        } else {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                showToast("???????????????????????????");
                touchTime = currentTime;
            } else {
                fragmentType = HomeFragmentType.HOME_FRAGMENT;
                PersistentCookieStore.getCookieStore().cleanCookie();
                upDateShow = true;
                noticeShow = true;
                isLogin = false;
                mBaseApplication.finishAll();
            }
        }
    }


    //App??????
    @Override
    public void onAppUpdate(AppUpDate appUpDate) {
        if (appUpDate.getBody().getReturnCode().equals("9999")) {
            String updateVersionid = appUpDate.getBody().getVersionid();
            String updateContents = appUpDate.getBody().getContents();
            String picAddress = appUpDate.getBody().getPicAddress();
            final String updateUrl = appUpDate.getBody().getUpurl();
            boolean isMustUpdate = appUpDate.getBody().getUpType().equals("1") ? true : false;
            UpdateFragment.showFragment(this, isMustUpdate, updateUrl, FileUtil.getFileName(), updateContents, picAddress, updateVersionid, new UpdateFragment.CancelAppUpdate() {
                @Override
                public void onCancelAppUpdate() {
                    upDateShow = false;
                    login();
                }
            });
        } else {
            upDateShow = false;
            login();
        }
    }

    @Override
    public void onAppUpdateError() {
        upDateShow = false;
        login();
    }

    @Override
    public void outLogin() {
        outLogin(HomeActivity.this);
        if (slidingMenu.isMenuShowing()) {
            slidingMenu.toggle();
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showMenu();
//            }
//        }, 1000);
    }

    @Override
    public void onImageCode(byte[] bytes) {
        loginDialogFragment.onImageCode(bytes);
    }

    @Override
    public void onUserPrivateNotice(PrivateNotice.PBAPP_privacyNotice.PrivacyNotice privacyNotice, String force) {
        if (privateNoticeDialog == null) {

            privateNoticeDialog = new UserPrivateNoticeDialog(this);

            privateNoticeDialog.setClickListener(new UserPrivateNoticeDialog.ClickListener() {
                @Override
                public void onAgree(@NotNull DialogInterface dialog, int which) {
                    dialog.dismiss();
                    updateApp();
                    UserSetting.setAgreementShow(true);
//                    presenter.onUpUserNoticeStatus(true, System.currentTimeMillis());
                }

                @Override
                public void onClose(@NotNull DialogInterface dialog, int which, boolean force) {
                    dialog.dismiss();
                    if (force) {
                        fragmentType = HomeFragmentType.HOME_FRAGMENT;
                        PersistentCookieStore.getCookieStore().cleanCookie();
                        upDateShow = true;
                        noticeShow = true;
                        isLogin = false;
                        mBaseApplication.finishAll();
                    } else {
                        updateApp();
                    }
//                    presenter.onUpUserNoticeStatus(false, System.currentTimeMillis());
                }

                @Override
                public void startWebActivity(@NotNull String url) {
                    if (url.endsWith("pdf")) {
                        AttachmentEntity attachmentEntity = new AttachmentEntity();
                        attachmentEntity.setTitle("??????????????????????????????????????????????????????.pdf");
                        attachmentEntity.setOpenUrl(url);
                        Intent intent = new Intent(HomeActivity.this, OpenAttachmentActivity.class);
                        intent.putExtra("attachmentEntity", attachmentEntity);
                        startActivity(intent);
                    } else {
                        HomeActivity.this.startWebActivity(url);
                    }
                }
            });
            privateNoticeDialog.setNoticeTitle(privacyNotice.getNoticeTitle());
            privateNoticeDialog.setLoadUrl(privacyNotice.getNoticeContent());
            privateNoticeDialog.setAgreeText(privacyNotice.getButtonTitle());
            privateNoticeDialog.setForce(force.equals("true"));
            privateNoticeDialog.createDialog().show();
        }
    }

    @Override
    public void refreshImageAuthCode() {
        if (loginDialogFragment.isImageCodeShow()) {
            presenter.onImageCode();
        }
    }

    WebView webView = null;

    @Override
    public void onLoginSuccess(String returnMsg, String mobile, String loginMethod, String fundAccount, String userType, String brokerNo) {
        presenter.onUpUserNoticeStatus(true, loginDialogFragment.getAgreementVersion());

        //?????????????????????????????????
        String lastUserFundAccount = UserInfoSharePre.getFundAccount();
        isLogin = true;
        //????????????
        UserInfoSharePre.saveUserNameType(loginDialogFragment.getRememberUserName());
        UserInfoSharePre.setUserName(loginDialogFragment.getUserName());
        UserInfoSharePre.setBrokerNo(brokerNo);
        UserInfoSharePre.saveUserType(userType);

        //?????????????????????
        UserInfoSharePre.setMobile(mobile);
        if (loginDialogFragment.getRememberUserName()) {

            //?????????????????????????????????????????????????????????????????????????????????
            if (loginMethod.equals("0")) {
                UserInfoSharePre.saveUserInfo(loginDialogFragment.getUserName(), EncDecUtil.AESEncrypt(loginDialogFragment.getPassWord()));
            }
        } else {
            //????????????????????? ?????????????????????????????????????????????
            UserInfoSharePre.saveFingerprintLoginType(false);
            UserInfoSharePre.saveGestureLoginType(false);
        }

        if (gestureCount) {
            UserInfoSharePre.saveGestureLoginType(false);
        }

        //??????????????????
        loginDialogFragment.dismissDialog();


        //??????????????????
        HomeFragment fragment = (HomeFragment) mCurrentNavButton.getFragment();
        fragment.loginDate();

        if (extras != null) {
            openJpushActivity(this);
        }

        if (baseCacheBean != null) {

            onJumpAction(baseCacheBean);
        }


        //???????????? ?????????????????? ??????????????????
        UserInfoSharePre.setFundAccount(fundAccount);
        upLoadStepCount();


        if (lastUserFundAccount.equals(fundAccount)) {

            UserInfoSharePre.setReplaceUser(false);

        }


        webView = new WebView(this);

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //??????js??????
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //????????????
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //????????????????????????/??????????????????Content Provider????????????????????? true
        webSettings.setAllowContentAccess(true);
        // ??????????????????????????????????????? true
        webSettings.setAllowFileAccess(true);
        setCookies();
        webView.loadUrl(BasePresenter.BASE_CHAIN_CACHE_URL);

    }


    public void setCookies() {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = PersistentCookieStore.getCookieStore().getCookies();
        for (Cookie cookie : cookies) {
            //???????????????????????????????????????cookie.getDomain()???????????????api????????????url,??????baidu.com?????????????????????????????????baidu.com???,
            cookieManager.setCookie(cookie.domain(), cookie.name() + "=" + cookie.value() + "; domain=" + cookie.domain() + "; path=" + cookie.path());
        }
        CookieSyncManager.getInstance().sync();


    }


    @Override
    public void setNeedValidateAuthCode(String needValidateAuthCode) {
        if (needValidateAuthCode.equals("1")) {
            loginDialogFragment.setNeedValidateAuthCode(needValidateAuthCode);
            presenter.onImageCode();
        }
    }

    private LoginDialogFragment loginDialogFragment;

    //????????????
    private FingerprintDialogFragment fingerprintDialogFragment;

    private void login() {

        if (loginDialogFragment != null) {
            loginDialogFragment.dismissDialog();
            loginDialogFragment = null;
        }
        loginDialogFragment = new LoginDialogFragment();
        presenter.getAgreementVersion();
        loginDialogFragment.setLoginInfoListener(new LoginInfoListener() {
            @Override
            public void login(String userName, String passWord, String loginMethod, String authCode, String needValidateAuthCode) {
                HomeActivity.this.login(userName, passWord, loginMethod, authCode, needValidateAuthCode);
            }

            //?????????
            @Override
            public void onRegister() {
                startActivityForResult(new Intent(HomeActivity.this, RegisterActivity.class), REGISTER_REQUEST_CODE);
            }

            //????????????
            @Override
            public void forgetPassWord() {
                baseStartActivity(HomeActivity.this, ForgetPasswordActivity.class);
            }

            //???????????????
            @Override
            public void refreshImageCode() {
                presenter.onImageCode();
            }

            @Override
            public void loginErrorInfo(String errMsg) {
                showDialog(errMsg);
            }

            @Override
            public void onLocationPermissions() {
//                HomeActivityPermissionsDispatcher.onLocationWithPermissionCheck(HomeActivity.this);
            }

            @Override
            public void onFinish() {
                fragmentType = HomeFragmentType.HOME_FRAGMENT;
                PersistentCookieStore.getCookieStore().cleanCookie();
                upDateShow = true;
                isLogin = false;
                loginDialogFragment.dismissDialog();
                mBaseApplication.finishAll();
            }

            @Override
            public void startWebActivity(String url) {
                HomeActivity.this.startWebActivity(url);
            }
        });

        loginDialogFragment.showDialog(getSupportFragmentManager());


        //????????????????????????
        if (UserInfoSharePre.getFingerprintLogin()) {
            if (!FingerprintUtil.callFingerPrint(this)) {
                UserInfoSharePre.saveFingerprintLoginType(false);
            } else {
                fingerprintDialogFragment = new FingerprintDialogFragment();
                FingerprintUtil.callFingerPrint(this, new FingerprintUtil.OnCallBackListenr() {

                    @Override
                    public void onSupportFailed() {

                    }

                    @Override
                    public void onInsecurity() {

                    }

                    @Override
                    public void onEnrollFailed() {

                    }

                    @Override
                    public void onAuthenticationStart() {

                    }

                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        fingerprintDialogFragment.dismissDialog();
                        showToast(errString.toString());
                        //?????????????????????
                        if (UserInfoSharePre.getGestureLoginType()) {
                            Intent intent = new Intent(HomeActivity.this, GestureLoginActivity.class);
                            intent.putExtra("type", "login");
                            startActivityForResult(intent, GESTURELOGIN_REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                        showToast("????????????");
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {

                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        fingerprintDialogFragment.dismissDialog();
                        login(UserInfoSharePre.getUserName(), UserInfoSharePre.getPassWord(), "2", "", "");

                    }
                });

                fingerprintDialogFragment.showDialog(getSupportFragmentManager());
            }
        } else if (UserInfoSharePre.getGestureLoginType()) {
            Intent intent = new Intent(this, GestureLoginActivity.class);
            intent.putExtra("type", "login");
            startActivityForResult(intent, GESTURELOGIN_REQUEST_CODE);
        }


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ProductDate.rest();
        doSelect(mNavHomes);
        selectNavigationButton(mNavHomes);
        if (!isLogin) {
            login();
        }
    }

    @Override
    public void initData() {
        //isTransferList = false;

        //??????????????????????????????????????????
        baseCacheBean = getIntent().getParcelableExtra("baseCacheBean");
        //??????????????????
        if (isJPush && isLogin) {
            setProductDate();
        }


        presenter.getProductListTitle();


        //???????????????????????????
        if (!UserSetting.isAgreementHasShow()) {
            presenter.getUserPrivateNotice();
        } else {
            updateApp();
        }

        //??????????????????????????????
        UpLoadFileJobIntentService.enqueueWork(this);
        CCLog.e("????????????", "????????????");


    }


    private void updateApp() {

        if (upDateShow) {
            presenter.queryAppUpdate();
        } else if (!isLogin) {
            login();
        }
    }

    /**
     * ???????????????
     */
    public void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //Menu????????????
        slidingMenu.setMode(SlidingMenu.LEFT);
        //????????????
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //menu????????????
        slidingMenu.setFadeDegree(0.35f);
        //???????????????????????????
        slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_offset);
        //????????????????????????????????????????????????
        slidingMenu.setBehindScrollScale(0.5f);
        //????????????????????????
        slidingMenu.setMenu(R.layout.layout_slidingmenu);
        SupportDisplay.resetAllChildViewParam(slidingMenu);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                //???????????????????????????????????????
                if (UserInfoSharePre.getHeadPic()) {
                    presenter.getDictionary();
                    UserInfoSharePre.saveHeadPic(false);
                }
            }
        });
        View view = slidingMenu.getMenu();
        //??????
        picImageView = view.findViewById(R.id.img_head);
        picImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ActivityUserInfo.class);
                intent.putExtra("picurl", picUrl);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMenu();
                    }
                }, 1000);
            }
        });

        //????????????
        view.findViewById(R.id.lin_my_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(HomeActivity.this, MyMessageActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMenu();
                    }
                }, 1000);
            }
        });

        //????????????
        view.findViewById(R.id.lin_my_have).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(HomeActivity.this, MyHoldingActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMenu();
                    }
                }, 1000);
            }
        });
        //????????????
        view.findViewById(R.id.lin_account_total).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(HomeActivity.this, AssetDetailActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMenu();
                    }
                }, 1000);
            }
        });
        //????????????
        view.findViewById(R.id.lin_account_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baseStartActivity(HomeActivity.this, AccountCenterActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMenu();
                    }
                }, 1000);
            }
        });
        //????????????
        view.findViewById(R.id.lin_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.outLogin();
            }
        });
    }

    @Override
    public void initView() {
        mTopDefineCancel = true;
        mFragmentManager = getSupportFragmentManager();
        mNavHomes = findViewById(R.id.nav_item_home);
        mNavHomes.setOnClickListener(this);
        mNavProduct = findViewById(R.id.nav_item_product);
        mNavProduct.setOnClickListener(this);
        mNavMine = findViewById(R.id.nav_item_mine);
        mNavMine.setOnClickListener(this);
        mNavMore = findViewById(R.id.nav_item_more);
        mNavMore.setOnClickListener(this);

        initFragment();
        //????????????
        initSlidingMenu();
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            //??????????????????????????? ?????????
            setupServiceClass();
        }
    }

    /**
     * ??????HomeActivity
     */
    public static void show(Activity activity, HomeFragmentType type) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        fragmentType = type;
//        activity.finish();
    }


    public static void showTransferList(Activity activity, HomeFragmentType type) {
        Intent intent = new Intent(activity, HomeActivity.class);
        activity.startActivity(intent);
        fragmentType = type;
        isTransferList = true;
        activity.finish();
    }


    public static void showJPush(Context activity, HomeFragmentType type, boolean isJPush) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        HomeActivity.isJPush = isJPush;
        fragmentType = type;
    }


    /**
     * ??????????????????
     **/


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.drawer_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

//    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
//    void onLocation() {
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        HomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }


    /**
     * ??????type
     */
    public enum HomeFragmentType {
        /**
         * HOME_FRAGMENT ??????
         * PRODUCT_FRAGMENT ????????????
         * MINE_FRAGMENT ????????????
         * MORE_FRAGMENT ??????
         */
        HOME_FRAGMENT,
        PRODUCT_FRAGMENT,
        MINE_FRAGMENT,
        MORE_FRAGMENT
    }


    public void getUserInfoPic(String data, final String usertype, final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        View view = slidingMenu.getMenu();
        ((TextView) view.findViewById(R.id.tv_account)).setText(data);
        if (usertype.equals("???????????????")) {
            //???????????????????????????
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setBackgroundResource(R.drawable.touzizhe);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.VISIBLE);
        } else if (usertype.equals("???????????????????????????")) {
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setBackgroundResource(R.drawable.shenqingtouzizhe);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.VISIBLE);
        } else if (usertype.equals("????????????")) {
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.GONE);
        } else {
            //??????????????????
            ((TextView) view.findViewById(R.id.tv_usertype)).setText(usertype);
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.GONE);
        }
        view.findViewById(R.id.lin_usertype).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (userDetailInfo.getIsBondedCard().equals("false") && userDetailInfo.getUserType().equals("personal")) {
                    showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //?????????
                            dialog.dismiss();
                            baseStartActivity(HomeActivity.this, AddBankActivity.class);
                        }
                    });
                }
                //????????????????????????
                else if (userDetailInfo.getHighNetWorthStatus().equals("-1")) {
                    String pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("") || userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "????????????????????????????????????????????????";
                    }

                    showDialog(pmtInfo);
                } else {
                    // ??????????????????????????????
                    if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                        presenter.getUserHighNetWorthInfo(userDetailInfo.getIsRealInvestor());
                    }
                    //????????????????????????????????????????????????????????????
                    else {
                        String isRealInvestor = userDetailInfo.getIsRealInvestor();

                        if (userDetailInfo.getUserType().equals("personal")) {
                            showUserLevelDialog("000", isRealInvestor);
                        } else if (userDetailInfo.getUserType().equals("company")) {
                            showUserLevelDialog("020", isRealInvestor);
                        }


                    }
                }
                showMenu();
            }
        });
        presenter.getDictionary();
        if (isOpen) {
            slidingMenu.toggle();
        }
    }

    @Override
    public void getPicDictionary(String pic, String fix) {
        String iconUrl = pic + UserInfoSharePre.getAccount() + fix;
        picUrl = iconUrl;
        String updateTime = String.valueOf(System.currentTimeMillis());
        RequestOptions options = new RequestOptions().centerCrop().transform(new CircleCrop()).error(R.drawable.head).signature(new ObjectKey(updateTime));

        Glide.with(HomeActivity.this).load(iconUrl).apply(options).into(picImageView);

    }

    @Override
    public void getAgreementVersion(String version) {
        loginDialogFragment.setAgreementVersion(version);
    }


    @Override
    public void getUserHighNetWorthInfo(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo highNetWorthInfo, final String isRealInvestor) {

        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : highNetWorthInfo.getData().getDictDynamicListList()) {
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
                intent.setClass(HomeActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);

            }
        });
    }

    //?????????????????????????????????
    public void refreshUserInfo(boolean isOpen) {
        this.isOpen = isOpen;
        presenter.getUserInfoDetail();//??????????????????????????????????????????????????????????????????????????????????????????????????????
    }

    @Override
    public void getUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        String usertype = "";

        UserInfoSharePre.setTradeAccount(userDetailInfo.getData().getTradeAccount());
        UserInfoSharePre.setAccount(userDetailInfo.getData().getAccount());
        UserInfoSharePre.setFundAccount(userDetailInfo.getData().getFundAccount());


        if (!userDetailInfo.getData().getUserGroup().equals("0")) {

            usertype = "?????????" + userDetailInfo.getData().getUserGroup();
        }

        if (userDetailInfo.getData().getIsAccreditedInvestor().equals("") || userDetailInfo.getData().getIsAccreditedInvestor().equals("0")) {
            if (!userDetailInfo.getData().getIsShow().equals("2")) {
                usertype = "???????????????????????????";
            } else {
                usertype = "????????????";
            }
        } else {
            if (userDetailInfo.getData().getUserGroup().equals("0")) {
                usertype = "???????????????";
            } else {
                usertype = "?????????" + userDetailInfo.getData().getUserGroup();
            }
        }

        String data = "";

        if (userDetailInfo.getData().getUserType().equals("personal")) {

            data = userDetailInfo.getData().getMobile();
        } else {
            data = userDetailInfo.getData().getFundAccount();

        }
        getUserInfoPic(data, usertype, userDetailInfo.getData());
    }

    /**
     * ???????????? ??????????????????
     **/

    private void upLoadStepCount() {
        /**
         * ??????????????????????????????????????????
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            getLastDayStepCount();
        } else {
            presenter.upLoadSteps("-1", TimeUtil.getLastDate());
        }
    }

    /**
     * ?????????????????????
     */
    private void setupServiceClass() {
        if (stepServiceClass != null) {
            stepServiceClass.removeRegister();
        }
        stepServiceClass = new StepServiceClass();
        stepServiceClass.init(getApplicationContext());

    }

    /**
     * ???????????????????????????
     */
    private void getLastDayStepCount() {
        int step = stepServiceClass.getStepData();
        if (step > 0) {
            CCLog.e("StepServiceClass???????????????----" + TimeUtil.getLastDate() + "----" + step);
            presenter.upLoadSteps(step + "", TimeUtil.getLastDate() + "");
        } else {
            CCLog.e("StepServiceClass???????????????----??????");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stepServiceClass != null) {
            stepServiceClass.removeRegister();
        }

        if (webView != null) {
            webView.destroy();
        }
    }

}