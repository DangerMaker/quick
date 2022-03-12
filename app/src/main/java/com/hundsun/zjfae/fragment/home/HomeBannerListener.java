package com.hundsun.zjfae.fragment.home;

import android.content.Intent;
import android.widget.Toast;

import com.android.banner.listener.OnBannerListener;
import com.hundsun.zjfae.HomeActivity;
import com.hundsun.zjfae.activity.product.ProductCodeActivity;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.StringUtils;
import com.hundsun.zjfae.fragment.finance.bean.ProductDate;
import com.hundsun.zjfae.fragment.home.bean.ShareBean;

import java.util.List;

import onight.zjfae.afront.gens.v3.UserDetailInfo;
import onight.zjfae.afront.gensazj.ADPictureProtoBuf;
import onight.zjfae.afront.gensazj.Advertise;

public class HomeBannerListener implements OnBannerListener {
    private HomeActivity activity;
    private List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> adsList;

    public HomeBannerListener(HomeActivity activity, List<ADPictureProtoBuf.PBAPP_ads_picture.AdsPicture> adsList) {

        this.activity = activity;
        this.adsList = adsList;

    }

    @Override
    public void OnBannerClick(int position) {
        Intent intent = new Intent();
        String funcUrl = adsList.get(position).getFuncUrl();
        String jumpRule = adsList.get(position).getJumpRule();

        if (jumpRule != null && jumpRule.equals("authentication")){

        }


        if (StringUtils.isNotBlank(funcUrl)) {//链接非空判断
            //外链分享
            if (funcUrl.indexOf("http://") >= 0 || funcUrl.indexOf("https://") >= 0) {

                ShareBean shareBean = new ShareBean();
                shareBean.setUuid(adsList.get(position).getUuid());
                shareBean.setFuncIcons(adsList.get(position).getFuncIcons());
                shareBean.setFuncUrl(adsList.get(position).getFuncUrl());
                shareBean.setShareItem(adsList.get(position).getShareItem());
                shareBean.setSharePicUrl(adsList.get(position).getSharePicUrl());
                shareBean.setShareUrl(adsList.get(position).getShareUrl());
                shareBean.setIsShare(adsList.get(position).getIsShare());
                activity.startWebActivity(shareBean);
            }
            // 站内产品详情跳转
            else if (funcUrl.indexOf("product://") >= 0) {

                funcUrl = funcUrl.replace("product://", "");
                intent.setClass(activity, ProductCodeActivity.class);
                intent.putExtra("productCode", funcUrl);
                intent.putExtra("sellingStatus", "1");

                activity.baseStartActivity(intent);
            }

            // 站内产品列表跳转
            else {
                funcUrl = funcUrl.replaceAll("func://productlist://", "");
                ProductDate.rest();
                ProductDate.productName = funcUrl;
                activity.setProductDate();
            }
        }
    }
}
