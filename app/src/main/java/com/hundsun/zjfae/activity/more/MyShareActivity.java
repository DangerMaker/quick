package com.hundsun.zjfae.activity.more;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.activity.myinvitation.QrCodeUtil;
import com.hundsun.zjfae.common.base.BasicsActivity;
import com.hundsun.zjfae.common.base.SupportDisplay;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.ushare.UShare;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.ushare.SharePlatform;

/**
 * @Description:我的分享
 * @Author: zhoujianyu
 * @Time: 2018/9/18 13:46
 */
public class MyShareActivity extends BasicsActivity {
    private ImageView img_download;
    private RelativeLayout rel;
    private String url = "";//分享url
    private static String title = "浙江金融资产交易中心";

    private static String desc = "我现正在使用掌上浙江金融资产交易中心，你也来试试吧。";

    @Override
    public void resetLayout() {
        LinearLayout layout = findViewById(R.id.layout_my_share);
        SupportDisplay.resetAllChildViewParam(layout);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_share;
    }

    @Override
    public void initView() {
        super.initView();
        setTitle("我的分享");
        CCLog.e("用户账号：" + UserInfoSharePre.getMobile());
        CCLog.e("资金账号：" + UserInfoSharePre.getFundAccount());
        img_download = findViewById(R.id.img_download);
        rel = findViewById(R.id.rel);
        //https://activity.zjfae.com/jrzc_app_promotion/index.php/?channel=0013&inviteCode=zjfae&activity=appxiazai_zjfae&inviteFlag=1&recommendMobile= 生产地址
        //https://wxtest-88.zjfae.com/jrzc_app_promotion/index.php/?channel=0054&inviteCode=qing&activity=appt&inviteFlag=1&recommendMobile= 测试地址
        url = "https://activity.zjfae.com/jrzc_app_promotion/index.php/?channel=0013&inviteCode=zjfae&activity=appxiazai_zjfae&inviteFlag=1&recommendMobile=" + EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile());
//        isShare("0", url);
        //微信分享
        findViewById(R.id.ll_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (UShare.isInstall(MyShareActivity.this, SharePlatform.WEIXIN)) {

                    UShare.openShareUrl(url, title, desc, R.drawable.logo, MyShareActivity.this, SharePlatform.WEIXIN);
                } else {
                    showToast("抱歉,您尚未安装微信客户端");
                }

//                if (UMShareAPI.get(MyShareActivity.this).isInstall(MyShareActivity.this, SHARE_MEDIA.WEIXIN)) {
//                    UMWeb web = new UMWeb(url);
//                    web.setTitle(title);
//                    UMImage thumb = new UMImage(MyShareActivity.this, R.drawable.logo);
//                    web.setThumb(thumb);
//                    web.setDescription(desc);
//                    new ShareAction(MyShareActivity.this).withMedia(web)
//                            .setPlatform(SHARE_MEDIA.WEIXIN)
//                            .setCallback(umShareListener)
//                            .share();
//                } else {
//                    showToast("抱歉,您尚未安装微信客户端");
//                }

            }
        });
        //朋友圈分享
        findViewById(R.id.ll_weixin_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (UShare.isInstall(MyShareActivity.this, SharePlatform.WEIXIN)) {

                    UShare.openShareUrl(url, title, desc, R.drawable.logo, MyShareActivity.this, SharePlatform.WEIXIN_CIRCLE);

//                    UMWeb web = new UMWeb(url);
//                    web.setTitle(title);
//                    UMImage thumb = new UMImage(MyShareActivity.this, R.drawable.logo);
//                    web.setThumb(thumb);
//                    web.setDescription(desc);
//                    new ShareAction(MyShareActivity.this).withMedia(web)
//                            .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                            .setCallback(umShareListener)
//                            .share();

                } else {
                    showToast("抱歉,您尚未安装微信客户端");
                }

            }
        });
        //新浪微博分享
        findViewById(R.id.ll_sina_weibo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (UMShareAPI.get(MyShareActivity.this).isInstall(MyShareActivity.this, SHARE_MEDIA.SINA)) {

                if (UShare.isInstall(MyShareActivity.this, SharePlatform.SINA)) {


                    UShare.openShareUrl(url, title, desc, R.drawable.logo, MyShareActivity.this, SharePlatform.SINA);

                } else {
                    showToast("抱歉,您尚未安装新浪微博客户端");
                }

//                UMWeb web = new UMWeb(url);
//                web.setTitle(title);
//                UMImage thumb = new UMImage(MyShareActivity.this, R.drawable.logo);
//                web.setThumb(thumb);
//                web.setDescription(desc);
//                new ShareAction(MyShareActivity.this).withMedia(web)
//                        .setPlatform(SHARE_MEDIA.SINA)
//                        .setCallback(umShareListener)
//                        .share();
//                } else {
//                    showToast("抱歉,您尚未安装新浪微博客户端");
//                }
            }
        });
        //QQ空间分享
//        findViewById(R.id.ll_qzone).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                if (UShare.isInstall(MyShareActivity.this, SharePlatform.QQ)) {
//
//                    UShare.openShareUrl(url, title, desc, R.drawable.logo, MyShareActivity.this, SharePlatform.QQ);
//
//                } else {
//                    showToast("抱歉,您尚未安装QQ客户端");
//                }
//
////                if (UMShareAPI.get(MyShareActivity.this).isInstall(MyShareActivity.this, SHARE_MEDIA.QQ)) {
////                    UMWeb web = new UMWeb(url);
////                    web.setTitle(title);
////                    UMImage thumb = new UMImage(MyShareActivity.this, R.drawable.logo);
////                    web.setThumb(thumb);
////                    web.setDescription(desc);
////                    new ShareAction(MyShareActivity.this).withMedia(web)
////                            .setPlatform(SHARE_MEDIA.QZONE)
////                            .setCallback(umShareListener)
////                            .share();
////                } else {
////                    showToast("抱歉,您尚未安装QQ客户端");
////                }
//            }
//        });
        Bitmap bitmap = QrCodeUtil.createQRCode(url, 500);
        img_download.setImageBitmap(bitmap);
    }


}
