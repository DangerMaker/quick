package com.hundsun.zjfae.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.home.presenter.WebPresenter;
import com.hundsun.zjfae.activity.logingesture.CreateGestureActivity;
import com.hundsun.zjfae.activity.logingesture.GestureLoginActivity;
import com.hundsun.zjfae.activity.mine.AddBankActivity;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.cookies.PersistentCookieStore;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.ushare.UShare;
import com.hundsun.zjfae.common.utils.AndroidBug5497Workaround;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.CrashUtils;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoImpl;
import org.devio.takephoto.model.InvokeParam;
import org.devio.takephoto.model.TContextWrap;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.permission.InvokeListener;
import org.devio.takephoto.permission.PermissionManager;
import org.devio.takephoto.permission.TakePhotoInvocationHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import onight.zjfae.afront.gens.UserHighNetWorthInfo;
import onight.zjfae.afront.gens.v3.UserDetailInfo;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.home
 * @ClassName: WebActivity
 * @Description: web界面
 * @Author: moran
 * @CreateDate: 2019/8/6 16:15
 * @UpdateUser: 更新者：moran
 * @UpdateDate: 2019/10/8 08:53
 * @UpdateRemark: 更新说明：1.增加区块链js模块
 * @Version: 1.0
 */
@RuntimePermissions
public class WebActivity extends CommActivity<WebPresenter> implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener, com.hundsun.zjfae.activity.home.view.WebView {
    private WebView mWebView;

    private String webUrl = "";

    private ShareBean shareBean;
    private String activityTitle = "", htmlTitle = "";

    private String linkUrl = "";//分享链接

    private String shareDesc = "";//分享内容

    private String shareThumb = "";//分享缩略图


    private String htmlUrl = "";//当前webView地址


    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private BottomSheetDialog bottomSheetDialog = null;
    // 表单的数据信息
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;


    //区块链创建手势密码
    private static final int CREATE_GESTURE_REQUEST_CODE = 20199;

    //区块链验证手势密码
    private static final int VERIFY_GESTURE_REQUEST_CODE = 20200;

    @Override
    public void initData() {
        shareBean = getIntent().getParcelableExtra("shareBean");
        setCookies();
        if (shareBean != null) {
            CCLog.i("url",webUrl);
            webUrl = shareBean.getFuncUrl();
            mWebView.loadUrl(webUrl);

            //分享
            if (shareBean.getIsShare().equals("1")) {
                if (findViewById(R.id.ll_commonn_isShare) != null) {
                    LinearLayout ll_commonn_isShare = findViewById(R.id.ll_commonn_isShare);
                    ll_commonn_isShare.setVisibility(View.VISIBLE);
                    ll_commonn_isShare.setOnClickListener(WebActivity.this);
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择相片
            case R.id.photo:
                getTakePhoto().onPickMultiple(1);
                bottomSheetDialog.dismiss();
                break;
            //拍照
            case R.id.camera_image:
                WebActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);
                bottomSheetDialog.dismiss();
                break;
            //取消
            case R.id.dismiss_image:
                bottomSheetDialog.dismiss();
                rest();

                break;

            case R.id.image_finish:
                finish();
                break;

            case R.id.ll_commonn_isShare:

                if (shareBean != null) {
                    //图片分享
                    if (shareBean.getShareItem().equals("1")) {


                        UShare.openShareImage(this, shareBean.getSharePicUrl());


                    }

                    //二维码分享
                    else if (shareBean.getShareItem().equals("2")) {

                        UShare.QRCodeShare(this, shareBean.getUuid());


                    }
                    //普通分享
                    else {

                        //内管配置分享
                        if (shareBean.getShareIsSure().equals("1")) {

                            htmlTitle = shareBean.getShareTitle();
                            linkUrl = shareBean.getShareUrl();
                            if (linkUrl.equals("")) {
                                linkUrl = htmlUrl;
                            }
                            shareThumb = "";
                            shareDesc = shareBean.getShareDesc();

                        } else {

                            //网页title为空取ActivityTitle
                            if (htmlTitle == null || htmlTitle.equals("")) {

                                if (activityTitle != null || !activityTitle.equals("")) {

                                    htmlTitle = activityTitle;
                                }

                            }

                            if (linkUrl == null || linkUrl.equals("")) {
                                linkUrl = htmlUrl;

                            }


                            if (shareDesc == null || shareDesc.equals("")) {
                                shareDesc = htmlUrl;
                            }

                        }

                        UShare.openShareUrl(this, htmlTitle, shareDesc, shareThumb, linkUrl, shareBean.getUuid());

                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 用户详细信息
     **/
    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfo_getUserDetailInfo, boolean isCertification) {

        UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userbaseinfo_getUserDetailInfo.getData();
        String userType = userDetailInfo.getUserType();

        if (isCertification) {
            certificateStatusType(userDetailInfo.getVerifyName(), userDetailInfo.getCertificateStatusType());
        } else {

            final String highNetWorthStatus = userDetailInfo.getHighNetWorthStatus();
            final String isRealInvestor = userDetailInfo.getIsRealInvestor();
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {

                showDialog("为了方便您购买产品，请您先绑定银行卡", "去绑卡", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //去绑卡
                        dialog.dismiss();
                        baseStartActivity(WebActivity.this, AddBankActivity.class);
                    }
                });

            } else if (!userDetailInfo.getIsAccreditedInvestor().equals("1")) {

                // 合格投资者审核中
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "您的合格投资者认定材料正在审核中，审核完成并认定为合格投资者后，您可预约、交易产品。";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "您的合格投资者认定材料正在审核中";
                    }
                    showDialog(pmtInfo);
                }
                // 合格投资者审核不通过
                else if (highNetWorthStatus.equals("0")) {
                    //查询原因
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {

                    if (userDetailInfo.getUserType().equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else if (userDetailInfo.getUserType().equals("company")) {
                        showUserLevelDialog("020", isRealInvestor);
                    }

                    com.tencent.smtt.sdk.CookieManager cookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
                }
            } else {
                if (userDetailInfo.getUserType().equals("personal")) {
                    showUserLevelDialog("000", isRealInvestor);
                } else if (userDetailInfo.getUserType().equals("company")) {
                    showUserLevelDialog("020", isRealInvestor);
                }
            }

        }


    }

    @Override
    public void requestInvestorStatus(UserHighNetWorthInfo.Ret_PBIFE_bankcardmanage_getUserHighNetWorthInfo userHighNetWorthInfo, final String isRealInvestor) {
        StringBuffer buffer = new StringBuffer();
        for (UserHighNetWorthInfo.DictDynamic dynamic : userHighNetWorthInfo.getData().getDictDynamicListList()) {
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
                intent.setClass(WebActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });
    }


    private class WebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            CCLog.e("urlonPageFinished", url);

            view.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");

            if (url.contains("zjfae.com")) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(url);


                if (cookie != null && !cookie.equals("")) {
                    CCLog.e("TAG-cookie", cookie);
                    String[] str = cookie.trim().split(";");

                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < str.length; i++) {
                        String[] item = str[i].split("=");
                        if (item[0] != null) {
                            if (item[0].trim().equals("SMID")) {
                                map.put(item[0].trim(), item[1]);
//                                Cookie.Builder baseBuild = new Cookie.Builder();
//                                baseBuild.domain(BasePresenter.DOMAIN);
//                                baseBuild.name("SMID");
//                                baseBuild.value(item[1]);
//                                PersistentCookieStore.getCookieStore().putCooKie("SMID",baseBuild.build());
                            }

//                            if (item[0].trim().equals("MFID")){
//                                Cookie.Builder baseBuild = new Cookie.Builder();
//                                baseBuild.domain(BasePresenter.DOMAIN);
//                                baseBuild.name("MFID");
//                                baseBuild.value(item[1]);
//                                PersistentCookieStore.getCookieStore().putCooKie("MFID",baseBuild.build());
//                            }

                        }
                    }

                    if (map.isEmpty()) {
                        //清空本地cookie
                        PersistentCookieStore.getCookieStore().cleanCookie();
                    }
                }
            }
            if (url.contains("callPhoneClose")) {
                finish();
            }

            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            CCLog.e("error", error.toString());
        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String tag = "tel:";
            if (url.contains(tag)) {
                String mobile = url.substring(url.lastIndexOf("/") + 1);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(mobile));
                startActivity(dialIntent);

                return true;
            }
            else  if (url.startsWith("weixin://wap/pay?")){
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        WebActivity.this.startActivity(intent);
                        return true;
                    }catch (Exception e){ //异常处理
                        view.goBack(); // 因为会出现有一个weixin空白页面；根据需求自己处理
                        Toast.makeText(WebActivity.this,"系统检测未安装微信，请先安装微信",Toast.LENGTH_LONG).show();
                        return true;
                    }
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String tag = "tel:";
            if (request.getUrl().toString().contains(tag)) {
                String mobile = request.getUrl().toString().substring(request.getUrl().toString().lastIndexOf("/") + 1);
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(mobile));
                startActivity(dialIntent);
                return true;
            }
            else if (request.getUrl().toString().startsWith("weixin://wap/pay?")){
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(request.getUrl().toString()));
                        WebActivity.this.startActivity(intent);
                        return true;
                    }catch (Exception e){ //异常处理
                        view.goBack(); // 因为会出现有一个weixin空白页面；根据需求自己处理
                        Toast.makeText(WebActivity.this,"系统检测未安装微信，请先安装微信",Toast.LENGTH_LONG).show();
                        return true;
                    }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            StringBuffer buffer = new StringBuffer("AndroidWebView加载错误信息：");
            buffer.append("errorResponse:").append(new Gson().toJson(errorResponse)).append("\n");
            buffer.append("request:").append(new Gson().toJson(request)).append("\n");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                buffer.append("statusCode:").append(errorResponse.getStatusCode()).append("\n");
                buffer.append("responseHeaders:").append(errorResponse.getResponseHeaders()).append('\n');
                buffer.append("url:").append(request.getUrl().toString()).append("\n");
                buffer.append("method:").append(request.getMethod()).append("\n");
                buffer.append("requestHeaders:").append(request.getRequestHeaders()).append("\n");
            }
            CrashUtils.getInstance().saveCrashFile(buffer.toString());
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            StringBuffer buffer = new StringBuffer("AndroidWebView加载错误信息：");
            buffer.append("request:").append(new Gson().toJson(request)).append("\n");
            buffer.append("error:").append(new Gson().toJson(error)).append("\n");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                buffer.append("errorCode:").append(error.getErrorCode()).append("\n");
                buffer.append("description:").append(error.getDescription()).append('\n');
                buffer.append("url:").append(request.getUrl().toString()).append("\n");
                buffer.append("method:").append(request.getMethod()).append("\n");
                buffer.append("requestHeaders:").append(request.getRequestHeaders()).append("\n");
            }
            CrashUtils.getInstance().saveCrashFile(buffer.toString());

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            StringBuffer buffer = new StringBuffer("AndroidWebView加载错误信息：");
            buffer.append("description:").append(description).append("\n");
            buffer.append("errorCode:").append(errorCode).append("\n");
            buffer.append("url:").append(failingUrl).append("\n");
            CrashUtils.getInstance().saveCrashFile(buffer.toString());
        }
    }


    @Override
    protected void topDefineCancel() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void initView() {
        AndroidBug5497Workaround.assistActivity(this);
        mTopDefineCancel = true;
        mWebView = findViewById(R.id.content_webView);
        findViewById(R.id.image_finish).setOnClickListener(this);
        // 不加上，会显示白边
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = mWebView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
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
        //支持自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setTextZoom(100);
        mWebView.setWebChromeClient(webChromeClient);
        //设定缩放控件隐藏
        webSettings.setDisplayZoomControls(false);
        mWebView.setWebViewClient(new WebClient());
        //AndroidJS.setShapeInfo
        mWebView.addJavascriptInterface(new AndroidJs(), "AndroidJs");
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                        // 表示按返回键
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();
                            return true;
                        } else {
                            finish();
                        }
                    }
                }

                return false;
            }
        });

        CCLog.e(UserInfoSharePre.getTradeAccount());
    }


    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;
    /**
     * 视频全屏参数
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private WebChromeClient webChromeClient = new WebChromeClient() {


        @Nullable
        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(WebActivity.this);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {

            showCustomView(view, callback);

            WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public void onShowCustomView(View view, int requestedOrientation, CustomViewCallback callback) {
            showCustomView(view, callback);
            WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }


        @Override
        public void onHideCustomView() {
            hideCustomView();
            WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            activityTitle = title;
            htmlUrl = view.getUrl();
            if (title != null) {
                setTitle(activityTitle);
            }
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            WebActivity.this.mUploadCallbackAboveL = filePathCallback;
            take();

            return true;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            WebActivity.this.mUploadMessage = uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            WebActivity.this.mUploadMessage = uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            WebActivity.this.mUploadMessage = uploadMsg;
            take();
        }

    };

    /**
     * 视频播放全屏
     **/
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        WebActivity.this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(WebActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }


    /**
     * 隐藏视频全屏
     */
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setStatusBarVisibility(true);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        mWebView.setVisibility(View.VISIBLE);
    }


    /**
     * 全屏容器界面
     */
    static class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(Context ctx) {
            super(ctx);
            setBackgroundColor(ctx.getResources().getColor(android.R.color.black));
        }

        @Override
        public boolean onTouchEvent(MotionEvent evt) {
            return true;
        }
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    protected WebPresenter createPresenter() {
        return new WebPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        UShare.onActivityResult(this, requestCode, resultCode, data);

        //设置区块链手势密码
        if (requestCode == CREATE_GESTURE_REQUEST_CODE) {


            JSONObject object = new JSONObject();
            //创建成功
            if (resultCode == RESULT_OK) {

                try {
                    object.put("code", "0000");
                    object.put("gestureCode", UserInfoSharePre.getBlockchainGessturePwdBefore());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            //创建失败
            else {
                try {
                    object.put("code", "00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            String url = "javascript: APPCallJSFunc('result','" + object.toString() + "')";//拼接参数，就可以把数据传递给js
            //String url = "javascript:" + "fun" + "(" + object.toString() + ");
            mWebView.loadUrl(url);

        }

        //验证区块链手势密码
        if (requestCode == VERIFY_GESTURE_REQUEST_CODE) {

            JSONObject object = new JSONObject();
            //验证区块链手势密码成功
            if (resultCode == RESULT_OK) {


                try {
                    object.put("code", "0000");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //取消验证区块链手势密码
            else {
                try {
                    object.put("code", "00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String url = "javascript: APPCallJSFunc('result','" + object.toString() + "')";//拼接参数，就可以把数据传递给js
            //String url = "javascript:" + "fun" + "(" + object.toString() + ");
            mWebView.loadUrl(url);


        }


    }


    private void onActivityResultAboveL(Uri uri) {
        Uri[] item = new Uri[]{uri};
        if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(item);
            mUploadCallbackAboveL = null;
        } else if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }


    }


    @Override
    public void takeSuccess(TResult result, TImage.FromType fromType) {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        //相册选取图片
        if (fromType != null && fromType == TImage.FromType.OTHER) {
            //originalPath就是完整路径
            Uri uri = Uri.fromFile(new File(originalPath));

            onActivityResultAboveL(uri);
        }
        //相机拍摄
        else {
            String photoPath = originalPath.substring(originalPath.lastIndexOf("/") + 1, originalPath.length());

            String filePath = FileUtil.getFilePath(this, FileUtil.WEB_KF);

            File file = new File(filePath, photoPath);
            Uri uri = Uri.fromFile(file);
            //file完整路径
            onActivityResultAboveL(uri);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

        rest();
    }

    @Override
    public void takeCancel() {
        rest();
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }


    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        //设置压缩规则，最大500kb
        //takePhoto.onEnableCompress(new CompressConfig.Builder().setMaxSize(100 * 1024).create(), true);
        return takePhoto;
    }


    private void take() {
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                rest();
            }
        });
        View rootView = getLayoutInflater().inflate(R.layout.bottom_sheet_sialog_layout, null);
        rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootView.findViewById(R.id.photo).setOnClickListener(this);
        rootView.findViewById(R.id.camera_image).setOnClickListener(this);
        rootView.findViewById(R.id.dismiss_image).setOnClickListener(this);
        SupportDisplay.resetAllChildViewParam((ViewGroup) rootView);
        bottomSheetDialog.setContentView(rootView);

        bottomSheetDialog.show();
    }


    private void rest() {
        if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
        mUploadMessage = null;
    }


    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
        File file = FileUtil.createFile(WebActivity.this, FileUtil.WEB_KF, FileUtil.getFileName() + ".png");
        Uri uri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);

        WebActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openDeniedCamera() {
        showDialog(R.string.camera_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                WebActivityPermissionsDispatcher.openCameraWithPermissionCheck(WebActivity.this);

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAgainCamera() {
        showDialog(R.string.camera_permission_hint, R.string.setting, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startAppInfoActivity(WebActivity.this);


            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.share_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    public void setCookies() {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = PersistentCookieStore.getCookieStore().getCookies();

//        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView,true);
//        }

        for (Cookie cookie : cookies) {
            //注意这里为什么放肆的写了个cookie.getDomain()，而不是像api里边说的url,类似baidu.com如果是域名，直接设置“baidu.com“,
            cookieManager.setCookie(cookie.domain(), cookie.name() + "=" + cookie.value() + "; domain=" + cookie.domain() + "; path=" + cookie.path());
        }


        CookieSyncManager.getInstance().sync();

        //CookieManager.getInstance().flush();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share;
    }


    private String htmlContent = "";

    private final class InJavaScriptLocalObj {


        @JavascriptInterface
        public void showSource(String html) {
            getHtmlContent(html);
        }

        /**
         * 获取内容
         *
         * @param html
         */
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        private void getHtmlContent(String html) {
            CCLog.d("LOGCAT", "网页内容:" + html);
            Document document = Jsoup.parse(html);

            //通过meta标签的name获得其内容
            if (document.select("meta[name=shareInfo]").size() > 0) {
                htmlContent = document.select("meta[name=shareInfo]").get(0).attr("content");
                CCLog.d("LOGCAT", "description:" + htmlContent);

                String data = new String(Base64.decode(htmlContent, Base64.DEFAULT));
                try {
                    JSONObject baseObject = new JSONObject(data);
                    htmlTitle = baseObject.optString("title");

                    linkUrl = baseObject.optString("link");
                    shareDesc = baseObject.optString("desc");

                    shareThumb = baseObject.optString("image");


                    CCLog.e("htmlTitle", htmlTitle);

                    CCLog.e("linkUrl", linkUrl);

                    CCLog.e("shareDesc", shareDesc);

                    CCLog.e("shareThumb", shareThumb);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CookieManager cookieManager = CookieManager.getInstance();


        if (Build.VERSION.SDK_INT < 21) {

            cookieManager.removeAllCookie();
            CookieSyncManager.getInstance().sync();
        } else {
            cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean aBoolean) {
                }
            });
            CookieManager.getInstance().flush();
        }
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.destroy();
    }


    public class AndroidJs {

        private static final String productList = "callPhoneSubscriptionList";//购买列表

        private static final String transferList = "callPhoneTransfer";//转让列表

        private static final String qualifiedInvestor = "callPhoneQualifiedInvestor";//合格投资者

        private static final String callPhoneIDUpload = "callPhoneIDUpload";//身份认证

        private static final String callPhoneClose = "callPhoneClose";//关闭界面

        private static final String callPhoneSetGesturePW = "callPhoneSetGesturePW";//设置手势密码

        private static final String callPhoneVerifyGesturePW = "callPhoneVerifyGesturePW";//验证手势密码

        private static final String callPhoneCloseSync = "callPhoneCloseSync";//关闭区块链

        private static final String callPhoneOpenSync = "callPhoneOpenSync";//开启区块链


        @JavascriptInterface
        public void JSCallAPPFunc(String type) {

            try {
                JSONObject object = new JSONObject(type);
                String jumpType = object.optString("jumpType");
                String info = object.optString("info");
                switch (jumpType) {
                    case productList:
                        HomeActivity.show(WebActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
                        break;
                    case transferList:
                        HomeActivity.showTransferList(WebActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
                        break;
                    case qualifiedInvestor:
                        presenter.onUserInfo(false);
                        break;
                    case callPhoneIDUpload:
                        presenter.onUserInfo(true);
                        break;

                    case callPhoneClose:
                        finish();
                        break;
                    //设置手势密码
                    case callPhoneSetGesturePW:
                        Intent createGesture = new Intent(WebActivity.this, CreateGestureActivity.class);
                        createGesture.putExtra("type", "Blockchain");
                        startActivityForResult(createGesture, CREATE_GESTURE_REQUEST_CODE);

                        break;
                    //验证手势密码
                    case callPhoneVerifyGesturePW:
                        Intent intent = new Intent(WebActivity.this, GestureLoginActivity.class);
                        intent.putExtra("type", "Blockchain");
                        startActivityForResult(intent, VERIFY_GESTURE_REQUEST_CODE);
                        break;

                    case callPhoneCloseSync:

                        UserInfoSharePre.saveBlockchainState(false);

                        break;
                    case callPhoneOpenSync:
                        UserInfoSharePre.saveBlockchainState(true);
                        break;

                    default:
                        break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                if (customView != null) {
                    hideCustomView();
                } else if (mWebView.canGoBack()) {

                    mWebView.goBack();
                } else {
                    finish();
                }
                return true;

            default:
                return super.onKeyUp(keyCode, event);
        }

    }
}
