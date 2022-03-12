package com.hundsun.zjfae.activity.product;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banner.Banner;
import com.android.banner.BannerConfig;
import com.android.banner.listener.OnBannerListener;
import com.android.banner.loader.ImageLoader;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.moneymanagement.MyEntrustActivity;
import com.hundsun.zjfae.activity.product.presenter.ProductPlayStatePresenter;
import com.hundsun.zjfae.activity.product.view.ProductPlayStateView;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.base.CommActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.common.utils.Utils;
import com.hundsun.zjfae.common.utils.gilde.ImageLoad;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.ArrayList;
import java.util.List;

import onight.zjfae.afront.gensazj.v2.BannerProto;

public class ProductPlayStateActivity extends CommActivity implements View.OnClickListener, ProductPlayStateView {


    private TextView playState_tv;
    private ProductPlayStatePresenter presenter;
    private Banner banner;
    private List<String> urlList;
    private String playState;
    private LinearLayout banner_layout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_play_state;
    }

    @Override
    protected BasePresenter createPresenter() {
        return presenter = new ProductPlayStatePresenter(this);
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.play_state_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected void initData() {
        playState = getIntent().getStringExtra("playState");
        playState_tv.setText(playState);
        if (playState.equals("交易成功")) {
            presenter.requestBanner("4");
        } else if (playState.equals("转让交易成功")) {
            presenter.requestBanner("5");
        }
    }

    @Override
    public void initView() {
        setNoBack();
        setTitle("成功");

        findViewById(R.id.play_state).setOnClickListener(this);
        banner = findViewById(R.id.banner);
        playState_tv = findViewById(R.id.playState);
        banner_layout = findViewById(R.id.banner_layout);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_state:
                if (playState.equals("挂卖成功")) {
                    baseStartActivity(this, MyEntrustActivity.class);
                    finish();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
                break;
        }
    }

    @Override
    public void banner(final List<BannerProto.PBAPP_ads.Ads> adsList) {

        CCLog.e("adsList", adsList);

        if (adsList.isEmpty()) {
            banner_layout.setVisibility(View.GONE);
        } else {
            banner_layout.setVisibility(View.VISIBLE);
            urlList = new ArrayList<>();
            for (BannerProto.PBAPP_ads.Ads adsBean : adsList) {
                urlList.add(adsBean.getFuncIcons());
            }

            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    ImageLoad.getImageLoad().LoadImage(context, path.toString(), imageView);
                }
            }).setIndicatorGravity(BannerConfig.RIGHT);

            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {

                    CCLog.e("FuncUrl", adsList.get(position).getFuncUrl());
                    if (StringUtils.isNotBlank(adsList.get(position).getFuncUrl())) {

                        ShareBean shareBean = new ShareBean();
                        shareBean.setUuid(adsList.get(position).getUuid());
                        shareBean.setFuncIcons(adsList.get(position).getFuncIcons());
                        shareBean.setAniParams(adsList.get(position).getAniParams());
                        shareBean.setFuncUrl(adsList.get(position).getFuncUrl());
                        shareBean.setShareItem(adsList.get(position).getShareItem());
                        shareBean.setSharePicUrl(adsList.get(position).getSharePicUrl());
                        shareBean.setShareUrl(adsList.get(position).getShareUrl());
                        shareBean.setIsShare(adsList.get(position).getIsShare());
                        startWebActivity(shareBean);
                    }

                }
            });

            banner.setImages(urlList).start();
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        setResult(RESULT_OK);
        finish();
        return true;
    }
}
