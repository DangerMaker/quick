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
 * @Description: 主界面承载四个fragment
 * @Author: moran
 * @CreateDate: 2019/6/14 13:51
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/14 13:51
 * @UpdateRemark: 更新说明：
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
    //用户头像
    private ImageView picImageView;
    private String picUrl = "";

    private static HomeFragmentType fragmentType = HomeFragmentType.HOME_FRAGMENT;

    public static boolean isTransferList = false;

    //手势密码锁
    public static final int GESTURELOGIN_REQUEST_CODE = 0x100;
    private static final int RESULT_ERROR_CODE = 0x9245;
    //错误次数过多
    private boolean gestureCount = false;

    //注册
    private static final int REGISTER_REQUEST_CODE = 0X191;
    //随便逛逛
    private static final int CASULA_CODE = 0x759;
    /**
     * 随便逛逛回来获取一下用户数据 与按左侧栏走同一条逻辑 用于区分是否打开左侧栏
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


    //banner图点击,icon图点击
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


    //登录方法
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


        //0表示密码登录
        builder.setLoginMethod(loginMethod);
        builder.setIsOpenGpwd(loginMethod);

        UserAgreementSetting.setAgreementVersion(userName, loginDialogFragment.getAgreementVersion());
        presenter.login(builder.build().toByteArray(), loginMethod);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //手势锁解锁成功
        if (requestCode == GESTURELOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            login(UserInfoSharePre.getUserName(), UserInfoSharePre.getPassWord(), "1", "", "");
        }
        //手势锁超出限制
        else if (requestCode == GESTURELOGIN_REQUEST_CODE && resultCode == RESULT_ERROR_CODE) {
            gestureCount = true;
        }
        //注册成功--->随便逛逛
        else if (requestCode == REGISTER_REQUEST_CODE && resultCode == CASULA_CODE) {
            loginDialogFragment.dismissDialog();
            doSelect(mNavProduct);
            FinanceFragment financeFragment = (FinanceFragment) mNavProduct.getFragment();
            financeFragment.initData();
        }
        //注册成功界面返回键退出
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
                showToast("请再按一次退出程序");
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


    //App更新
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
                        attachmentEntity.setTitle("浙江金融资产交易中心个人会员服务协议.pdf");
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

        //最后一个人登录资金账号
        String lastUserFundAccount = UserInfoSharePre.getFundAccount();
        isLogin = true;
        //登录成功
        UserInfoSharePre.saveUserNameType(loginDialogFragment.getRememberUserName());
        UserInfoSharePre.setUserName(loginDialogFragment.getUserName());
        UserInfoSharePre.setBrokerNo(brokerNo);
        UserInfoSharePre.saveUserType(userType);

        //保存用户手机号
        UserInfoSharePre.setMobile(mobile);
        if (loginDialogFragment.getRememberUserName()) {

            //判断登录方式，如果是手势密码登录，则存储用户账号和密码
            if (loginMethod.equals("0")) {
                UserInfoSharePre.saveUserInfo(loginDialogFragment.getUserName(), EncDecUtil.AESEncrypt(loginDialogFragment.getPassWord()));
            }
        } else {
            //不保存用户数据 清空之前设置的手势或者指纹登录
            UserInfoSharePre.saveFingerprintLoginType(false);
            UserInfoSharePre.saveGestureLoginType(false);
        }

        if (gestureCount) {
            UserInfoSharePre.saveGestureLoginType(false);
        }

        //关闭登录弹框
        loginDialogFragment.dismissDialog();


        //刷新首页数据
        HomeFragment fragment = (HomeFragment) mCurrentNavButton.getFragment();
        fragment.loginDate();

        if (extras != null) {
            openJpushActivity(this);
        }

        if (baseCacheBean != null) {

            onJumpAction(baseCacheBean);
        }


        //上送步数 保存资金账号 上传步数需要
        UserInfoSharePre.setFundAccount(fundAccount);
        upLoadStepCount();


        if (lastUserFundAccount.equals(fundAccount)) {

            UserInfoSharePre.setReplaceUser(false);

        }


        webView = new WebView(this);

        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置可以访问文件/，是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowContentAccess(true);
        // 是否可访问本地文件，默认值 true
        webSettings.setAllowFileAccess(true);
        setCookies();
        webView.loadUrl(BasePresenter.BASE_CHAIN_CACHE_URL);

    }


    public void setCookies() {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = PersistentCookieStore.getCookieStore().getCookies();
        for (Cookie cookie : cookies) {
            //注意这里为什么放肆的写了个cookie.getDomain()，而不是像api里边说的url,类似baidu.com如果是域名，直接设置“baidu.com“,
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

    //指纹登录
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

            //去注册
            @Override
            public void onRegister() {
                startActivityForResult(new Intent(HomeActivity.this, RegisterActivity.class), REGISTER_REQUEST_CODE);
            }

            //忘记密码
            @Override
            public void forgetPassWord() {
                baseStartActivity(HomeActivity.this, ForgetPasswordActivity.class);
            }

            //刷新验证码
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


        //是否开启指纹登录
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
                        //是否开启手势锁
                        if (UserInfoSharePre.getGestureLoginType()) {
                            Intent intent = new Intent(HomeActivity.this, GestureLoginActivity.class);
                            intent.putExtra("type", "login");
                            startActivityForResult(intent, GESTURELOGIN_REQUEST_CODE);
                        }
                    }

                    @Override
                    public void onAuthenticationFailed() {

                        showToast("校验失败");
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

        //用于接收广告位点击传递的链接
        baseCacheBean = getIntent().getParcelableExtra("baseCacheBean");
        //极光推送登录
        if (isJPush && isLogin) {
            setProductDate();
        }


        presenter.getProductListTitle();


        //是否弹出过隐私协议
        if (!UserSetting.isAgreementHasShow()) {
            presenter.getUserPrivateNotice();
        } else {
            updateApp();
        }

        //上传用户日志服务开启
        UpLoadFileJobIntentService.enqueueWork(this);
        CCLog.e("奔溃日志", "启动服务");


    }


    private void updateApp() {

        if (upDateShow) {
            presenter.queryAppUpdate();
        } else if (!isLogin) {
            login();
        }
    }

    /**
     * 设置侧边栏
     */
    public void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //Menu所在位置
        slidingMenu.setMode(SlidingMenu.LEFT);
        //全屏滑动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        //menu褪色程度
        slidingMenu.setFadeDegree(0.35f);
        //滑动后菜单显示宽度
        slidingMenu.setBehindWidthRes(R.dimen.slidingmenu_offset);
        //菜单滚动速度比内容滚动速度。。。
        slidingMenu.setBehindScrollScale(0.5f);
        //设置菜单部分布局
        slidingMenu.setMenu(R.layout.layout_slidingmenu);
        SupportDisplay.resetAllChildViewParam(slidingMenu);
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
        slidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                //侧拉栏打开的是请求图片地址
                if (UserInfoSharePre.getHeadPic()) {
                    presenter.getDictionary();
                    UserInfoSharePre.saveHeadPic(false);
                }
            }
        });
        View view = slidingMenu.getMenu();
        //头像
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

        //我的消息
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

        //我的持仓
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
        //账户总额
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
        //账户中心
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
        //安全退出
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
        //侧拉模块
        initSlidingMenu();
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            //如果存在计步器功能 初始化
            setupServiceClass();
        }
    }

    /**
     * 跳转HomeActivity
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
     * 极光推送跳转
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
     * 标记type
     */
    public enum HomeFragmentType {
        /**
         * HOME_FRAGMENT 首页
         * PRODUCT_FRAGMENT 产品列表
         * MINE_FRAGMENT 账户中心
         * MORE_FRAGMENT 更多
         */
        HOME_FRAGMENT,
        PRODUCT_FRAGMENT,
        MINE_FRAGMENT,
        MORE_FRAGMENT
    }


    public void getUserInfoPic(String data, final String usertype, final UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        View view = slidingMenu.getMenu();
        ((TextView) view.findViewById(R.id.tv_account)).setText(data);
        if (usertype.equals("合格投资者")) {
            //显示合格投资者图片
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setBackgroundResource(R.drawable.touzizhe);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.VISIBLE);
        } else if (usertype.equals("申请成为合格投资者")) {
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setBackgroundResource(R.drawable.shenqingtouzizhe);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.VISIBLE);
        } else if (usertype.equals("隐藏按钮")) {
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.GONE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.GONE);
        } else {
            //显示其他布局
            ((TextView) view.findViewById(R.id.tv_usertype)).setText(usertype);
            ((TextView) view.findViewById(R.id.tv_usertype)).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.img_touzizhe)).setVisibility(View.GONE);
        }
        view.findViewById(R.id.lin_usertype).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (userDetailInfo.getIsBondedCard().equals("false") && userDetailInfo.getUserType().equals("personal")) {
                    showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //去绑卡
                            dialog.dismiss();
                            baseStartActivity(HomeActivity.this, AddBankActivity.class);
                        }
                    });
                }
                //合格投资者审核中
                else if (userDetailInfo.getHighNetWorthStatus().equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("") || userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }

                    showDialog(pmtInfo);
                } else {
                    // 合格投资者审核不通过
                    if (userDetailInfo.getHighNetWorthStatus().equals("0")) {
                        presenter.getUserHighNetWorthInfo(userDetailInfo.getIsRealInvestor());
                    }
                    //非合格投资者，合格投资者，高净值统一弹框
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

        showDialog(buffer.toString() + "".trim(), "重新上传", "取消", new DialogInterface.OnClickListener() {
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

    //刷新用户侧边栏用户数据
    public void refreshUserInfo(boolean isOpen) {
        this.isOpen = isOpen;
        presenter.getUserInfoDetail();//防止第一次注册就点击随便逛逛然后点击侧边栏导致用户数据没有加载的问题
    }

    @Override
    public void getUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo) {
        String usertype = "";

        UserInfoSharePre.setTradeAccount(userDetailInfo.getData().getTradeAccount());
        UserInfoSharePre.setAccount(userDetailInfo.getData().getAccount());
        UserInfoSharePre.setFundAccount(userDetailInfo.getData().getFundAccount());


        if (!userDetailInfo.getData().getUserGroup().equals("0")) {

            usertype = "高净值" + userDetailInfo.getData().getUserGroup();
        }

        if (userDetailInfo.getData().getIsAccreditedInvestor().equals("") || userDetailInfo.getData().getIsAccreditedInvestor().equals("0")) {
            if (!userDetailInfo.getData().getIsShow().equals("2")) {
                usertype = "申请成为合格投资者";
            } else {
                usertype = "隐藏按钮";
            }
        } else {
            if (userDetailInfo.getData().getUserGroup().equals("0")) {
                usertype = "合格投资者";
            } else {
                usertype = "高净值" + userDetailInfo.getData().getUserGroup();
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
     * 以下逻辑 获取昨日步数
     **/

    private void upLoadStepCount() {
        /**
         * 这里判断当前设备是否支持计步
         */
        if (StepCountCheckUtil.isSupportStepCountSensor(this)) {
            getLastDayStepCount();
        } else {
            presenter.upLoadSteps("-1", TimeUtil.getLastDate());
        }
    }

    /**
     * 获取计步器数据
     */
    private void setupServiceClass() {
        if (stepServiceClass != null) {
            stepServiceClass.removeRegister();
        }
        stepServiceClass = new StepServiceClass();
        stepServiceClass.init(getApplicationContext());

    }

    /**
     * 获取昨天的历史步数
     */
    private void getLastDayStepCount() {
        int step = stepServiceClass.getStepData();
        if (step > 0) {
            CCLog.e("StepServiceClass昨天的步数----" + TimeUtil.getLastDate() + "----" + step);
            presenter.upLoadSteps(step + "", TimeUtil.getLastDate() + "");
        } else {
            CCLog.e("StepServiceClass昨天的步数----为空");
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