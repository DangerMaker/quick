package com.hundsun.zjfae.common.ushare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.hundsun.zjfae.R;
import com.hundsun.zjfae.common.base.BasePresenter;
import com.hundsun.zjfae.common.user.UserInfoSharePre;
import com.hundsun.zjfae.common.utils.CCLog;
import com.hundsun.zjfae.common.utils.aes.EncDecUtil;
import com.ushare.SharePlatform;
import com.ushare.ShareListener;
import com.ushare.ShareSDK;

import java.util.HashMap;
import java.util.Map;

public class UShare {

    private static Application mContext;
    private static Toast toast;



    //二维码分享
    public static String QR_CODE = BasePresenter.BASE_URL + "mzj/pbimg.do?fh=VIMGMZJ000000J00&type=6&p=and&source=app&newtable=true&";


    private static final String APP_KEY = "555e815267e58ea61c003d26";


    public static void ShareInit(Application context) {
        mContext = context;

        ShareSDK.init(context, APP_KEY, "浙金Android");

        //添加微信平台
        ShareSDK.addWexinPlatform("wx81de2adf437dc15e", "bd36ce233ab60d5b662bbaeb1908de84");
        //添加QQ空间平台
        ShareSDK.addQQPlatform("1103423316", "10sJj3x2VTunysaX");

    }



    /**
     * 链接分享
     **/
    @Deprecated
    public static void openShareUrl(Activity activity, String title, String desc, String shareUrl) {

        CCLog.e("title", title);
        CCLog.e("desc", desc);
        CCLog.e("shareUrl", shareUrl);

        try {
            ShareSDK.openShareUrl(shareUrl, title, desc, R.drawable.logo, activity, shareListener);
        } catch (Exception e) {
            CCLog.e("openShareUrl", e.getMessage());
        }


    }


    /**
     * 链接分享
     **/
    public static void openShareUrl(Activity activity, String shareTitle, String shareDesc, String shareThumb, String shareUrl, String uuid) {

        Map map = new HashMap();
        map.put("recommendMobile", UserInfoSharePre.getMobile());
        map.put("inviter", UserInfoSharePre.getFundAccount());
        map.put("uuid",uuid);

        if (shareThumb == null || shareThumb.equals("")) {

            try {
                ShareSDK.openShareUrl(shareUrl, map, shareTitle, shareDesc, R.drawable.logo, activity, shareListener);
            } catch (Exception e) {
                CCLog.e("Exception_openShareUrl", e.getMessage());
            }


        } else {

            try {
                ShareSDK.openShareUrl(shareUrl, map, shareTitle, shareDesc, shareThumb, activity, shareListener);
            } catch (Exception e) {
                CCLog.e("openShareUrl_Exception_openShareUrl", e.getMessage());
            }

        }
    }


    //二维码分享
    public static void QRCodeShare(Activity activity, String uuid) {

        Map map = new HashMap();
        map.put("recommendMobile", EncDecUtil.AESEncrypt(UserInfoSharePre.getMobile()));
        map.put("inviter", EncDecUtil.AESEncrypt(UserInfoSharePre.getFundAccount()));
        map.put("uuid",uuid);


        try {
            ShareSDK.QRCodeShare(QR_CODE, map, activity, shareListener);
        } catch (Exception e) {
            CCLog.e("QRCodeShare", e.getMessage());
        }

    }

    /**
     * 图片分享
     */

    public static void openShareImage(Activity activity, String shareUrl) {


        try {
            ShareSDK.openShareImage(shareUrl, activity, shareListener);
        } catch (Exception e) {
            CCLog.e("openShareImage", e.getMessage());
        }

    }

    /**
     * 图片分享(Bitmap)
     */

    public static void openShareImage(Activity activity, Bitmap bitmap) {


        try {
            ShareSDK.openShareImage(bitmap, activity, shareListener);
        } catch (Exception e) {
            CCLog.e("bitmap_Exception", e.getMessage());

        }

    }


    public static void openShareUrl(String shareUrl, String title, String desc, int resourcesId, Activity activity, SharePlatform sharePlatform) {


        try {
            ShareSDK.openShareUrl(shareUrl, title, desc, resourcesId, activity, sharePlatform, shareListener);
        } catch (Exception e) {
            CCLog.e("Platform_Exception", e.getMessage());
        }


    }


    private static ShareListener shareListener = new ShareListener() {
        @Override
        public void onStart() {

        }

        @Override
        public void onResult() {
        }

        @Override
        public void onError(Throwable throwable) {
            showToast(throwable.getMessage());
        }

        @Override
        public void onCancel() {
        }
    };


    public static boolean isInstall(Activity activity, SharePlatform sharePlatform) {

        return ShareSDK.isInstall(activity, sharePlatform);
    }


    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        ShareSDK.onActivityResult(activity, requestCode, resultCode, data);

    }

    public static void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(mContext,
                    s,
                    Toast.LENGTH_SHORT);
        } else {
            try {
                toast.setText(s);
            } catch (Exception e) {
                toast = Toast.makeText(mContext,
                        s,
                        Toast.LENGTH_SHORT);
            }
        }
        toast.show();
    }
}
