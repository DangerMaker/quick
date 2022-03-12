package com.hundsun.zjfae.activity.splash;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.ADSharePre;
import com.hundsun.zjfae.common.user.BaseCacheBean;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SplashActivity extends BasicsActivity implements View.OnClickListener {

    private ImageView splasjh_img;
    private Button start_but;
    private BaseCacheBean baseCacheBean;

    private long millisInFuture = 5;

    private Disposable mdDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
    }


    @Override
    public void initView() {
        splasjh_img = findViewById(R.id.splasjh_img);
        splasjh_img.setOnClickListener(this);
        start_but = findViewById(R.id.start_but);
        start_but.setOnClickListener(this);


    }


    @Override
    public void initData() {
        baseCacheBean = ADSharePre.getConfiguration(ADSharePre.startIcons, BaseCacheBean.class);
        if (baseCacheBean != null){
            if (baseCacheBean.getStartUpTime() != null && !baseCacheBean.getStartUpTime().equals("")){
                long time = Long.valueOf(baseCacheBean.getStartUpTime().trim());
                millisInFuture = time;
            }
        }



        RequestOptions options = new RequestOptions().placeholder(R.drawable.activity_bg).error(R.drawable.activity_bg).signature(new ObjectKey(baseCacheBean.getResTime()));

        Glide.with(this).load(baseCacheBean.getFuncIcons()).apply(options).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                splasjh_img.setBackground(resource);
                splasjh_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (StringUtils.isNotBlank(baseCacheBean.getFuncUrl())) {
                            if (baseCacheBean.getFuncUrl().contains("needLogin=true")) {//点击启动页图片需要登录 跳转链接
                                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                intent.putExtra("baseCacheBean", baseCacheBean);
                                baseStartActivity(intent);
                            }
                            else {
                                //点击启动页图片不需要登录 跳转链接
                                if (!baseCacheBean.getJumpRule().equals("0")){
                                    UserInfoSharePre.saveStatistticsData(baseCacheBean.getUuid());
                                    baseStartActivity(SplashActivity.this, HomeActivity.class);
                                    ShareBean shareBean = new ShareBean();
                                    if (baseCacheBean.getShareIsSure().equals("1")){
                                        shareBean.setJumpRule(baseCacheBean.getJumpRule());
                                        shareBean.setUuid(baseCacheBean.getUuid());
                                        shareBean.setShareUrl(baseCacheBean.getShareUrl());
                                        shareBean.setIsShare(baseCacheBean.getIsShare());
                                        shareBean.setFuncUrl(baseCacheBean.getFuncUrl());
                                        shareBean.setSharePicUrl(baseCacheBean.getSharePicUrl());
                                        shareBean.setFuncIcons(baseCacheBean.getFuncIcons());
                                        shareBean.setShareDesc(baseCacheBean.getShareDesc());
                                        shareBean.setShareItem(baseCacheBean.getShareItem());
                                        shareBean.setShareIsSure(baseCacheBean.getShareIsSure());
                                        shareBean.setShareTitle(baseCacheBean.getShareTitle());
                                        shareBean.setLinkKeywordName(baseCacheBean.getLink_keyword_name());
                                        shareBean.setKeyword(baseCacheBean.getKeyword());
                                    }
                                    else {
                                        shareBean.setIsShare(baseCacheBean.getIsShare());
                                        shareBean.setFuncUrl(baseCacheBean.getFuncUrl());
                                    }
                                    startWebActivity(shareBean);
                                    finish();

                                }

                            }
                        }
                    }
                });
                //当图片加载好时才开始倒计时
                start_but.setVisibility(View.VISIBLE);

                Observable observable = Observable.intervalRange(0,millisInFuture,0,1, TimeUnit.SECONDS);

                mdDisposable =  observable .observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        start_but.setText("跳过" + (millisInFuture - aLong ) + "s");
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        baseStartActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    }
                }).subscribe();
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                baseStartActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.splash_layout);
        SupportDisplay.resetAllChildViewParam(layout);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mdDisposable != null) {
            mdDisposable.dispose();
            mdDisposable = null;
        }

    }



    private class CustomCountDownTimer extends CountDownTimer {


        public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            start_but.setText("跳过" + (millisUntilFinished / 1000) + "s");
        }

        @Override
        public void onFinish() {
            baseStartActivity(new Intent(SplashActivity.this, HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_but:

                if (mdDisposable != null) {
                    mdDisposable.dispose();
                }
                baseStartActivity(this, HomeActivity.class);
                finish();
                break;
            default:
                break;
        }
    }
}
