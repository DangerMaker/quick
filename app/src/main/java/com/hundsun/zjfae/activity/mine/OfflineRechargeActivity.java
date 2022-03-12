package com.hundsun.zjfae.activity.mine;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.mine.presenter.OfflineRechargePresenter;
import com.hundsun.zjfae.activity.mine.view.OfflineRechargeView;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.view.WebViewWithProgress;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import onight.zjfae.afront.gensazj.Dictionary;
import onight.zjfae.afront.gensazj.v2.Notices;

public class OfflineRechargeActivity extends CommActivity<OfflineRechargePresenter> implements OfflineRechargeView, View.OnClickListener {


    private WebView mWebView;
    private WebViewWithProgress content_webView;

    private TextView parms_top, parms_center, parms_bottom;

    private String parmsUrl = "";

    private LinearLayout lin_layout, change_bank_layout;


    private static final int RECHARGE_CODE = 0x791;

    @Override
    public void onOfflineRecharge(Notices.Ret_PBAPP_notice notice) {
        mWebView.loadDataWithBaseURL(null, notice.getData().getNotice().getNoticeContent(), "text/html", "UTF-8", null);
    }

    @Override
    public void onDictionary(Dictionary.Ret_PBAPP_dictionary dictionary) {
        parms_top.setText(dictionary.getData().getParms(0).getKeyCode());
        parms_center.setText(dictionary.getData().getParms(1).getKeyCode());
        parms_bottom.setText(dictionary.getData().getParms(2).getKeyCode());
        parmsUrl = dictionary.getData().getParms(3).getKeyCode();
    }

    @Override
    protected OfflineRechargePresenter createPresenter() {
        return  new OfflineRechargePresenter(this);
    }


    @Override
    public void initData() {
        String bankCard = getIntent().getStringExtra("bankCard");
        String bankName = getIntent().getStringExtra("bankName");
        String type = getIntent().getStringExtra("type");
        if (type.equals("301")) {
            lin_layout.setVisibility(View.VISIBLE);
        } else if (type.equals("302")) {
            change_bank_layout.setVisibility(View.VISIBLE);
        }
        presenter.offline(bankCard, bankName, type);
        presenter.offlineRechargeDictionary();
    }

    @Override
    public void initView() {
        setTitle("线下充值");

        content_webView = findViewById(R.id.content_webView);
        mWebView = content_webView.getWebView();

        parms_top = findViewById(R.id.parms_top);
        parms_center = findViewById(R.id.parms_center);
        parms_bottom = findViewById(R.id.parms_bottom);
        findViewById(R.id.parms_layout).setOnClickListener(this);
        findViewById(R.id.copy_top).setOnClickListener(this);
        findViewById(R.id.copy_center).setOnClickListener(this);
        findViewById(R.id.copy_bottom).setOnClickListener(this);
        findViewById(R.id.change_bank).setOnClickListener(this);
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.finish_button).setOnClickListener(this);
        findViewById(R.id.change_bank_button).setOnClickListener(this);
        lin_layout = findViewById(R.id.lin_layout);
        change_bank_layout = findViewById(R.id.change_bank_layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_offline_recharge;
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
            case R.id.parms_layout:
                ShareBean shareBean = new ShareBean();
                shareBean.setFuncUrl(parmsUrl);
                startWebActivity(shareBean);
                break;
            case R.id.finish_button:
                Intent intents = new Intent(this, RechargeActivity.class);
                String payAmount = getIntent().getStringExtra("payAmount");
                String tag = getIntent().getStringExtra("tag");
                boolean isRegister = getIntent().getBooleanExtra("isRegister",false);
                intents.putExtra("payAmount", payAmount);
                intents.putExtra("tag",tag);
                intents.putExtra("isRegister",isRegister);
                startActivityForResult(intents, RECHARGE_CODE);
                break;
            case R.id.change_bank_button:
                baseStartActivity(this, BankCardActivity.class);
                finish();
                break;
            case R.id.copy_top:
                copy(parms_top, "户名复制成功");
                break;
            case R.id.copy_center:
                copy(parms_center, "账号复制成功");
                break;
            case R.id.copy_bottom:
                copy(parms_bottom, "开户行复制成功");
                break;
                default:
                    break;
        }
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.offline_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.destroy();
    }


    private void copy(TextView tv, String msg) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("zjfae_tv",
                tv.getText());

        cm.setPrimaryClip(clip);

        showDialog(msg);
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
