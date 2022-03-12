package com.hundsun.zjfae.common.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hundsun.zjfae.R;


/**
 *
 * 备注:
 * 此WebViewWithProgress继承自Relativielayout,
 * 如果要设置webview的属性，要先调用getWebView()来取得
 * webview的实例
 *
 * @author Administrator
 *
 */
public class WebViewWithProgress extends RelativeLayout{


	private Context context;
	private WebView mWebView = null;
	//水平进度条
	private ProgressBar progressBar = null;
	//包含圆形进度条的布局
	private RelativeLayout progressBar_circle = null;
	//进度条样式,Circle表示为圆形，Horizontal表示为水平
	private int mProgressStyle = ProgressStyle.Horizontal.ordinal();
	//默认水平进度条高度
	public static int DEFAULT_BAR_HEIGHT = 8;
	//水平进度条的高
	private int mBarHeight = DEFAULT_BAR_HEIGHT;

	public enum ProgressStyle{
		Horizontal,
		Circle;
	};

	public WebViewWithProgress(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		init();
	}

	public WebViewWithProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.WebViewWithProgress);
		mProgressStyle = attributes.getInt(R.styleable.WebViewWithProgress_progressStyle,ProgressStyle.Horizontal.ordinal());
		mBarHeight = attributes.getDimensionPixelSize(R.styleable.WebViewWithProgress_barHeight, DEFAULT_BAR_HEIGHT);
		attributes.recycle();
		init();
	}

	private void init(){
		mWebView = new WebView(context);
		//setUseWideViewPort
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
		webSettings.setDefaultTextEncodingName("UTF-8");
		// 是否允许通过file url加载的Javascript读取本地文件，默认值 false
		webSettings.setAllowFileAccessFromFileURLs(false);
		// 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
		webSettings.setAllowUniversalAccessFromFileURLs(false);
		webSettings.setSupportZoom(true);
		webSettings.setTextZoom(100);
		this.addView(mWebView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		if(mProgressStyle == ProgressStyle.Horizontal.ordinal()){
			progressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.progress_horizontal, null);
			progressBar.setMax(100);
			progressBar.setProgress(0);
			WebViewWithProgress.this.addView(progressBar, LayoutParams.MATCH_PARENT, mBarHeight);
		}else{
			progressBar_circle = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.progress_circle, null);
			WebViewWithProgress.this.addView(progressBar_circle, LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);
		}
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					view.loadUrl(request.getUrl().toString());
				} else {
					view.loadUrl(request.toString());
				}
				return true;
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				handler.proceed();
				super.onReceivedSslError(view, handler, error);
			}
		});
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if(newProgress == 100){
					if(progressBar!=null){
						progressBar.setVisibility(View.GONE);
					}
					if(progressBar_circle!=null){
						progressBar_circle.setVisibility(View.GONE);
					}
				}else{
					if(mProgressStyle == ProgressStyle.Horizontal.ordinal()){
						progressBar.setVisibility(View.VISIBLE);
						progressBar.setProgress(newProgress);
					}else{
						progressBar_circle.setVisibility(View.VISIBLE);
					}
				}
			}


		});
	}


	public WebView getWebView()
	{
		return mWebView;
	}


}
