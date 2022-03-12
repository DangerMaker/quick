package com.hundsun.zjfae.activity.home;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.http.cookies.PersistentCookieStore;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.view.WebViewWithProgress;

import java.util.List;

import okhttp3.Cookie;

public class CustomerServerActivity extends BasicsActivity {

    private WebViewWithProgress webViewWithProgress;
    private WebView mWebView;
    private String url;


    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 1;// 表单的结果回调

    @Override
    protected int getLayoutId() {
        return R.layout.activity_customer_server_ativity;
    }


    @Override
    protected void initData() {
        url = getIntent().getStringExtra("url");
        //http://hlmp.zjfae.com/webkx/im/groupChat.php?terminal=2
        setCookies();
        mWebView.loadUrl(url+"?terminal=2");
    }

    @Override
    public void initView(){
        setTitle("客服");
        webViewWithProgress = findViewById(R.id.content_webView);
        mWebView = webViewWithProgress.getWebView();

        mWebView.setWebChromeClient(webChromeClient);
    }



    private WebChromeClient webChromeClient = new WebChromeClient(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {

            String[] acceptTypes = fileChooserParams.getAcceptTypes();

            Toast.makeText(CustomerServerActivity.this,"1",Toast.LENGTH_LONG).show();

            return true;
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Toast.makeText(CustomerServerActivity.this,"2",Toast.LENGTH_LONG).show();

        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType) {
            Toast.makeText(CustomerServerActivity.this,"3",Toast.LENGTH_LONG).show();

        }
        public void openFileChooser(ValueCallback<Uri> uploadMsg,String acceptType, String capture) {
            Toast.makeText(CustomerServerActivity.this,"4",Toast.LENGTH_LONG).show();

        }
    };














    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.message_server_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }



    public void setCookies() {

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        List<Cookie> cookies = PersistentCookieStore.getCookieStore().getCookies();
        for (Cookie  cookie : cookies) {
            //注意这里为什么放肆的写了个cookie.getDomain()，而不是像api里边说的url,类似baidu.com如果是域名，直接设置“baidu.com“,
            cookieManager.setCookie(cookie.domain(),  cookie.name() + "=" + cookie.value() + "; domain=" + cookie.domain() + "; path=" + cookie.path());
        }
        CookieSyncManager.getInstance().sync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }







}
