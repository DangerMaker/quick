package com.hundsun.zjfae.activity.mine;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.WebViewWithProgress;

public class OfflineRechargeWebActivity extends BasicsActivity implements View.OnClickListener {

    private WebView mWebView;
    private WebViewWithProgress content_webView;
    private LinearLayout lin_layout, change_bank_layout;

    private static final int RECHARGE_CODE = 0x791;
    @Override
    protected void initData() {
        String type = getIntent().getStringExtra("type");
        if (type.equals("301")) {
            lin_layout.setVisibility(View.VISIBLE);
        } else if (type.equals("302")) {
            change_bank_layout.setVisibility(View.VISIBLE);
        }
        String url =getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
    }

    @Override
    protected void initView() {
        setTitle("线下充值");
        content_webView = findViewById(R.id.content_webView);
        mWebView = content_webView.getWebView();
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


        lin_layout = findViewById(R.id.lin_layout);
        change_bank_layout = findViewById(R.id.change_bank_layout);
        findViewById(R.id.change_bank).setOnClickListener(this);
        findViewById(R.id.finish_button).setOnClickListener(this);
        findViewById(R.id.change_bank_button).setOnClickListener(this);
        findViewById(R.id.finish).setOnClickListener(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.offlineWeb_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_offline_recharge_web;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_bank:
                baseStartActivity(this, BankCardActivity.class);
                finish();
                break;
            case R.id.finish:
                finish();
                break;


            case R.id.finish_button:
                Intent intents = new Intent(this, RechargeActivity.class);
                String payAmount = getIntent().getStringExtra("payAmount");
                intents.putExtra("payAmount", payAmount);
                startActivityForResult(intents, RECHARGE_CODE);
                break;

            case R.id.change_bank_button:
                baseStartActivity(this, BankCardActivity.class);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RECHARGE_CODE && (resultCode == RESULT_OK || resultCode == RESULT_CANCELED)) {
            setResult(RESULT_OK);
            finish();
        }
    }
}
