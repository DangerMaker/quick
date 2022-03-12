package com.hundsun.zjfae.activity.mine;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.presenter.RechargeGuidePresenter;
import com.hundsun.zjfae.activity.mine.view.RechargeGuideView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.WebViewWithProgress;

import onight.zjfae.afront.AllAzjProto;

public class RechargeGuideActivity extends CommActivity implements RechargeGuideView,View.OnClickListener {

    private RechargeGuidePresenter presenter;

    private WebView mWebView;
    private WebViewWithProgress share_webView;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_recharge_guide;
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new RechargeGuidePresenter(this);
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("充值引导");
        share_webView = findViewById(R.id.content_webView);
        mWebView = share_webView.getWebView();
        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        WebSettings webSetting = mWebView.getSettings();
        //支持JS，此项必不可少
        webSetting.setJavaScriptEnabled(true);
//1.网上说是设置此选项提高渲染的优先级，
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
//2.首先阻塞图片，让图片不显示
        webSetting.setBlockNetworkImage(true);
        //3.页面加载好以后，在放开图片：
        webSetting.setBlockNetworkImage(false);
// 设置缓存模式（下面会详细介绍缓存）
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
// 开启 DOM storage API 功能
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowFileAccess(true);
//开启 database storage API 功能
        webSetting.setDatabaseEnabled(true);
//开启 Application Caches 功能
        webSetting.setAppCacheEnabled(true);
//此设置是否保存H5表单数据，发现一个蛋疼的问题，在小米手机上当H5 input框设置为search后，当点击input框的时候//竟然会有历史的搜索记录，而且样式十分难看，设置此属性可以取消历史搜索记录
        webSetting.setSaveFormData(false);
        findViewById(R.id.finish).setOnClickListener(this);
    }

    @Override
    public void initData() {
        presenter.rechargeGuide();
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.recharge_guide_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    public void onRecharge(AllAzjProto.PEARetNotice peaRetNotice) {
        mWebView.loadDataWithBaseURL(null, peaRetNotice.getNotice().getNoticeContent(), "text/html", "UTF-8", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.finish:
                finish();
                break;
        }
    }
}
