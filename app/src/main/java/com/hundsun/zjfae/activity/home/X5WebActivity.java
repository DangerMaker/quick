package com.hundsun.zjfae.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
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
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.CrashUtils;
import com.hundsun.zjfae.common.utils.FileUtil;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

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

@RuntimePermissions
public class X5WebActivity extends CommActivity<WebPresenter> implements TakePhoto.TakeResultListener, InvokeListener, View.OnClickListener, com.hundsun.zjfae.activity.home.view.WebView {
    private static final String TAG = "X5WebActivity---";
    private WebView mWebView;

    private String webUrl = "";

    private ShareBean shareBean;
    private String activityTitle = "", htmlTitle = "";
    private String linkUrl = "";//????????????
    private String shareDesc = "";//????????????
    private String shareThumb = "";//???????????????
    private String htmlUrl = "";//??????webView??????
    private String htmlContent = "";

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private BottomSheetDialog bottomSheetDialog = null;  //???????????????
    // ?????????????????????
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    //???????????????????????????
    private static final int CREATE_GESTURE_REQUEST_CODE = 20199;

    //???????????????????????????
    private static final int VERIFY_GESTURE_REQUEST_CODE = 20200;



    /********************************************BaseActivity********************************/

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.share_layout_tencent);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tencent_web;
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initView() {

        getWindow().setFormat(PixelFormat.TRANSLUCENT); //??????????????????????????????????????????????????????
        mWebView = findViewById(R.id.content_webView);
        mTopDefineCancel = true;
        WebSettings webSettings = mWebView.getSettings();
        //5.0??????????????????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //??????js??????
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setDomStorageEnabled(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //????????????
        webSettings.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        //????????????????????????/??????????????????Content Provider????????????????????? true
        webSettings.setAllowContentAccess(true);
        // ??????????????????????????????????????? true
        webSettings.setAllowFileAccess(true);
        //????????????????????????
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        // ??????????????????file url?????????Javascript?????????????????????????????? false
        webSettings.setAllowFileAccessFromFileURLs(false);
        // ??????????????????file url?????????Javascript??????????????????(????????????,http,https)???????????? false
        webSettings.setAllowUniversalAccessFromFileURLs(false);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setTextZoom(100);
        //????????????????????????
        webSettings.setDisplayZoomControls(false);
        mWebView.setWebChromeClient(new WebChrome());
        mWebView.setWebViewClient(new WebClient());
        mWebView.addJavascriptInterface(new AndroidJs(), "AndroidJs");
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

    }

    @Override
    protected void initData() {
        super.initData();
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        shareBean = getIntent().getParcelableExtra("shareBean");
        setCookies();
        if (shareBean != null && StringUtils.isNotBlank(shareBean.getFuncUrl())) {
            webUrl = shareBean.getFuncUrl();
            mWebView.loadUrl(webUrl);
            //??????
            if (shareBean.getIsShare().equals("1")) {
                if (findViewById(R.id.ll_commonn_isShare) != null) {
                    LinearLayout ll_commonn_isShare = findViewById(R.id.ll_commonn_isShare);
                    ll_commonn_isShare.setVisibility(View.VISIBLE);
                    ll_commonn_isShare.setOnClickListener(X5WebActivity.this);
                }
            }
        }

        String x5CrashInfo = WebView.getCrashExtraMessage(this);
        Log.e("X5????????????",x5CrashInfo);
    }

    public void setCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        //cookieManager.setAcceptThirdPartyCookies(mWebView,true);
        List<Cookie> cookies = PersistentCookieStore.getCookieStore().getCookies();

        for (Cookie cookie : cookies) {
            //???????????????????????????????????????cookie.getDomain()???????????????api????????????url,??????baidu.com?????????????????????????????????baidu.com???,
            cookieManager.setCookie(cookie.domain(), cookie.name() + "=" + cookie.value() + "; domain=" + cookie.domain() + "; path=" + cookie.path());
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
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

    @Override
    protected WebPresenter createPresenter() {
        return new WebPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        UShare.onActivityResult(this, requestCode, resultCode, data);

        //???????????????????????????
        if (requestCode == CREATE_GESTURE_REQUEST_CODE) {


            JSONObject object = new JSONObject();
            //????????????
            if (resultCode == RESULT_OK) {

                try {
                    object.put("code", "0000");
                    object.put("gestureCode", UserInfoSharePre.getBlockchainGessturePwdBefore());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            //????????????
            else {
                try {
                    object.put("code", "00");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            String url = "javascript: APPCallJSFunc('result','" + object.toString() + "')";//??????????????????????????????????????????js
            //String url = "javascript:" + "fun" + "(" + object.toString() + ");
            mWebView.loadUrl(url);

        }

        //???????????????????????????
        if (requestCode == VERIFY_GESTURE_REQUEST_CODE) {

            JSONObject object = new JSONObject();
            //?????????????????????????????????
            if (resultCode == RESULT_OK) {


                try {
                    object.put("code", "0000");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //?????????????????????????????????
            else {
                try {
                    object.put("code", "0000");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String url = "javascript: APPCallJSFunc('result','" + object.toString() + "')";//??????????????????????????????????????????js
            //String url = "javascript:" + "fun" + "(" + object.toString() + ");
            mWebView.loadUrl(url);


        }

    }

    /****************************************TakeResultListener******************************/
    @Override
    public void takeSuccess(TResult result, TImage.FromType fromType) {
        TImage image = result.getImage();
        String originalPath = image.getOriginalPath();
        if (fromType != null && fromType == TImage.FromType.OTHER) {  //??????????????????
            //originalPath??????????????????
            Uri uri = Uri.fromFile(new File(originalPath));
            onActivityResultAboveL(uri);
        } else { //????????????
            String photoPath = originalPath.substring(originalPath.lastIndexOf("/") + 1);
            String filePath = FileUtil.getFilePath(this, FileUtil.WEB_KF);
            File file = new File(filePath, photoPath);
            Uri uri = Uri.fromFile(file);
            //file????????????
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

    /*****************************************InvokeListener********************************/
    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /****************************************OnClickListener********************************/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photo:  //????????????
                getTakePhoto().onPickMultiple(1);
                bottomSheetDialog.dismiss();
                break;
            case R.id.camera_image:  //??????
                X5WebActivityPermissionsDispatcher.openCameraWithPermissionCheck(this);
                bottomSheetDialog.dismiss();
                break;
            case R.id.dismiss_image: //????????????
                bottomSheetDialog.dismiss();
                rest();
                break;

            case R.id.image_finish: //????????????
                finish();
                break;
            case R.id.ll_commonn_isShare:  //??????
                if (shareBean != null) {
                    if (shareBean.getShareItem().equals("1")) {  //????????????
                        UShare.openShareImage(this, shareBean.getSharePicUrl());
                        return;
                    }

                    if (shareBean.getShareItem().equals("2")) {  //???????????????
                        UShare.QRCodeShare(this, shareBean.getUuid());
                        return;
                    } else {
                        if (shareBean.getShareIsSure().equals("1")) {

                            htmlTitle = shareBean.getShareTitle();
                            linkUrl = shareBean.getShareUrl();
                            shareThumb = "";
                            shareDesc = shareBean.getShareDesc();

                        } else {

                            //??????title?????????ActivityTitle
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
        mUploadCallbackAboveL.onReceiveValue(null);
        mUploadMessage.onReceiveValue(null);
        mUploadCallbackAboveL = null;
        mUploadMessage = null;
    }

    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    /**
     * ??????????????????
     *
     * @param userbaseinfo_getUserDetailInfo ??????????????????
     * @param isCertification                ??????????????????
     */
    @Override
    public void onUserInfo(UserDetailInfo.Ret_PBIFE_userbaseinfo_getUserDetailInfo userbaseinfo_getUserDetailInfo, boolean isCertification) {
        UserDetailInfo.PBIFE_userbaseinfo_getUserDetailInfo userDetailInfo = userbaseinfo_getUserDetailInfo.getData();

        if (isCertification) {
            certificateStatusType(userDetailInfo.getVerifyName(), userDetailInfo.getCertificateStatusType());
        } else {
            final String highNetWorthStatus = userDetailInfo.getHighNetWorthStatus();
            final String isRealInvestor = userDetailInfo.getIsRealInvestor();
            String userType = userDetailInfo.getUserType();
            if (userDetailInfo.getIsBondedCard().equals("false") && userType.equals("personal")) {

                showDialog("??????????????????????????????????????????????????????", "?????????", "??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //?????????
                        dialog.dismiss();
                        baseStartActivity(X5WebActivity.this, AddBankActivity.class);
                    }
                });

            } else if (!userDetailInfo.getIsAccreditedInvestor().equals("1")) {

                // ????????????????????????
                if (highNetWorthStatus.equals("-1")) {
                    String pmtInfo = "??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????";
                    if (userDetailInfo.getIsAccreditedInvestor().equals("1")) {
                        pmtInfo = "????????????????????????????????????????????????";
                    }
                    showDialog(pmtInfo);
                }
                // ??????????????????????????????
                else if (highNetWorthStatus.equals("0")) {
                    //????????????
                    presenter.requestInvestorStatus(isRealInvestor);
                } else {

                    if (userDetailInfo.getUserType().equals("personal")) {
                        showUserLevelDialog("000", isRealInvestor);
                    } else if (userDetailInfo.getUserType().equals("company")) {
                        showUserLevelDialog("020", isRealInvestor);
                    }

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
        showDialog(buffer.toString() + "".trim(), "????????????", "??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("isRealInvestor", isRealInvestor);
                intent.setClass(X5WebActivity.this, HighNetWorthMaterialsUploadedActivity.class);
                baseStartActivity(intent);
            }
        });
    }

    private class WebClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView webView, String s) {
            webView.loadUrl("javascript:window.local_obj.showSource('<head>'+" + "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            if (s.contains("zjfae.com")) {
                CookieManager cookieManager = CookieManager.getInstance();
                String cookie = cookieManager.getCookie(s);
                if (cookie != null && !cookie.equals("")) {
                    String[] str = cookie.trim().split(";");
                    Map<String, String> map = new HashMap<>();
                    for (int i = 0; i < str.length; i++) {
                        String[] item = str[i].split("=");
                        if (item[0] != null) {
                            if (item[0].trim().equals("SMID")) {
                                map.put(item[0].trim(), item[1]);
                            }
                        }
                    }
                    if (map.isEmpty()) {
                        //????????????cookie
                        PersistentCookieStore.getCookieStore().cleanCookie();
                    }
                }
            }
            if (s.contains("callPhoneClose")) {
                finish();
            }
            super.onPageFinished(webView, s);
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
                    X5WebActivity.this.startActivity(intent);
                    return true;
                }catch (Exception e){ //????????????
                    view.goBack(); // ????????????????????????weixin???????????????????????????????????????
                    Toast.makeText(X5WebActivity.this,"????????????????????????????????????????????????",Toast.LENGTH_LONG).show();
                    return true;
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }



        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            super.onReceivedError(webView, errorCode, description, failingUrl);
            StringBuffer buffer = new StringBuffer("X5WebView?????????????????????");
            buffer.append("description:").append(description).append("\n");
            buffer.append("errorCode:").append(errorCode).append("\n");
            buffer.append("url:").append(failingUrl).append("\n");
            CrashUtils.getInstance().saveCrashFile(buffer.toString());
        }

        @Override
        public void onReceivedError(WebView webView, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(webView, request, error);
            StringBuffer buffer = new StringBuffer("X5WebView?????????????????????");
            buffer.append("request:").append(new Gson().toJson(request)).append("\n");
            buffer.append("error:").append(new Gson().toJson(error)).append("\n");
            buffer.append("errorCode:").append(error.getErrorCode()).append("\n");
            buffer.append("description:").append(error.getDescription()).append('\n');
            buffer.append("url:").append(request.getUrl().toString()).append("\n");
            buffer.append("method:").append(request.getMethod()).append("\n");
            buffer.append("requestHeaders:").append(request.getRequestHeaders()).append("\n");
            CrashUtils.getInstance().saveCrashFile(buffer.toString());
        }


        @Override
        public void onReceivedHttpError(WebView webView, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(webView, request, errorResponse);
            StringBuffer buffer = new StringBuffer("X5WebView?????????????????????");
            buffer.append("errorResponse:").append(new Gson().toJson(errorResponse)).append("\n");
            buffer.append("request:").append(new Gson().toJson(request)).append("\n");
            buffer.append("statusCode:").append(errorResponse.getStatusCode()).append("\n");
            buffer.append("responseHeaders:").append(errorResponse.getResponseHeaders()).append('\n');
            buffer.append("url:").append(request.getUrl().toString()).append("\n");
            buffer.append("method:").append(request.getMethod()).append("\n");
            buffer.append("requestHeaders:").append(request.getRequestHeaders()).append("\n");
            CrashUtils.getInstance().saveCrashFile(buffer.toString());
        }
    }


    private View customView;
    private FrameLayout fullscreenContainer;

    private IX5WebChromeClient.CustomViewCallback customViewCallback;

    /**
     * ??????????????????
     */
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    private class WebChrome extends WebChromeClient {


        @Override
        public View getVideoLoadingProgressView() {
            FrameLayout frameLayout = new FrameLayout(X5WebActivity.this);
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return frameLayout;
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            showCustomView(view, customViewCallback);
            X5WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public void onShowCustomView(View view, int i, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            showCustomView(view, customViewCallback);
            X5WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
            X5WebActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }


        @Override
        public void onReceivedTitle(WebView webView, String s) {
            activityTitle = s;
            htmlUrl = webView.getUrl();
            if (s != null) {
                setTitle(activityTitle);
            }
            super.onReceivedTitle(webView, s);
        }

        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = valueCallback;
            take();
            return true;
        }

        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
            mUploadMessage = valueCallback;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            take();
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            take();
        }


    }


    private void showCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {

        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        this.getWindow().getDecorView();

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new FullscreenHolder(X5WebActivity.this);
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        setStatusBarVisibility(false);
        customViewCallback = callback;
    }

    /**
     * ??????????????????
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
     * ??????????????????
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


    private final class InJavaScriptLocalObj {


        @JavascriptInterface
        public void showSource(String html) {
            getHtmlContent(html);
        }

        /**
         * ????????????
         *
         * @param html
         */
        @SuppressLint("JavascriptInterface")
        @JavascriptInterface
        private void getHtmlContent(String html) {
            Document document = Jsoup.parse(html);

            //??????meta?????????name???????????????
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

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openCamera() {
        File file = FileUtil.createFile(X5WebActivity.this, FileUtil.WEB_KF, FileUtil.getFileName() + ".png");
        Uri uri = Uri.fromFile(file);
        getTakePhoto().onPickFromCapture(uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //?????????????????????Android6.0???7.0??????????????????
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);

        X5WebActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openDeniedCamera() {
        showDialog(R.string.camera_permission_hint, R.string.permission, R.string.clean, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                X5WebActivityPermissionsDispatcher.openCameraWithPermissionCheck(X5WebActivity.this);
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
                startAppInfoActivity(X5WebActivity.this);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
        super.onBackPressed();
    }

    public class AndroidJs {

        private static final String productList = "callPhoneSubscriptionList";//????????????

        private static final String transferList = "callPhoneTransfer";//????????????

        private static final String qualifiedInvestor = "callPhoneQualifiedInvestor";//???????????????

        private static final String callPhoneIDUpload = "callPhoneIDUpload";//????????????

        private static final String callPhoneClose = "callPhoneClose";//????????????

        private static final String callPhoneSetGesturePW = "callPhoneSetGesturePW";//??????????????????

        private static final String callPhoneVerifyGesturePW = "callPhoneVerifyGesturePW";//??????????????????

        @JavascriptInterface
        public void JSCallAPPFunc(String type) {

            try {
                JSONObject object = new JSONObject(type);
                String jumpType = object.optString("jumpType");
                String info = object.optString("info");
                switch (jumpType) {
                    case productList:
                        HomeActivity.show(X5WebActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
                        break;
                    case transferList:
                        HomeActivity.showTransferList(X5WebActivity.this, HomeActivity.HomeFragmentType.PRODUCT_FRAGMENT);
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

                    //??????????????????
                    case callPhoneSetGesturePW:
                        Intent createGesture = new Intent(X5WebActivity.this, CreateGestureActivity.class);
                        createGesture.putExtra("type", "Blockchain");
                        startActivityForResult(createGesture, CREATE_GESTURE_REQUEST_CODE);

                        break;
                    //??????????????????
                    case callPhoneVerifyGesturePW:
                        Intent intent = new Intent(X5WebActivity.this, GestureLoginActivity.class);
                        intent.putExtra("type", "Blockchain");
                        startActivityForResult(intent, VERIFY_GESTURE_REQUEST_CODE);

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
    protected void onDestroy() {
        super.onDestroy();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeAllCookie();
        if (Build.VERSION.SDK_INT < 21) {
            CookieSyncManager.getInstance().sync();
        } else {
            CookieManager.getInstance().flush();
        }
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.destroy();
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
