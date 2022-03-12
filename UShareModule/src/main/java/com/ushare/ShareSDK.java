package com.ushare;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.Iterator;
import java.util.Map;

public class ShareSDK {


    private static Application application;





    /**
     * 初始化common库
     * 参数1:上下文，不能为空
     * 参数2:友盟 app_key
     * 参数3:友盟 channel
     * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * 参数5:Push推送业务的secret
     */
    public static void init(Application application,String uMengKey){
        ShareSDK.application = application;
        UMConfigure.init(application,uMengKey,"Umeng",UMConfigure.DEVICE_TYPE_PHONE,"");
    }


    /**
     * 初始化common库
     * 参数1:上下文，不能为空
     * 参数2:友盟 app_key
     * 参数3:友盟 channel
     * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
     * 参数5:Push推送业务的secret
     */
    public static void init(Application application,String uMengKey,String channel){
        ShareSDK.application = application;
        UMConfigure.init(application,uMengKey,channel,UMConfigure.DEVICE_TYPE_PHONE,"");

        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
    }




    public static void addQQPlatform(String appId,String appKey ){
        PlatformConfig.setQQZone(appId, appKey);
    }



    public static void addWexinPlatform(String appId,String appValue){

        PlatformConfig.setWeixin(appId, appValue);
    }




    /**
     * 链接分享
     * @param shareUrl 分享链接
     * @param title 分享title
     * @param desc 分享内容
     * @param resourcesId 分享缩略图--资源文件
     * @param activity 上下文对象
     * @param shareListener 分享回调
     * **/
    public static void openShareUrl(String shareUrl,String title,String desc,int resourcesId,Activity activity,ShareListener shareListener ){

        ShareAction action = new ShareAction(activity);
        UMWeb web = new UMWeb(shareUrl);
        UMImage thumb =  new UMImage(activity, resourcesId);
        web.setThumb(thumb);
        web.setTitle(title);
        web.setDescription(desc);
        action.withMedia(web);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.setCallback(new Share(shareListener));
        action.open();

    }


    /**
     * 链接分享
     * @param shareUrl 分享链接
     * @param map 分享拼接链接
     * @param title 分享title
     * @param desc 分享内容
     * @param resourcesId 分享缩略图--资源文件
     * @param activity 上下文对象
     * @param shareListener 分享回调
     * **/
    public static void openShareUrl(String shareUrl, Map map, String title, String desc, int resourcesId, Activity activity, ShareListener shareListener ){

        ShareAction action = new ShareAction(activity);
        UMWeb web = new UMWeb(shareUrl);
        UMImage thumb =  new UMImage(activity, resourcesId);
        web.setThumb(thumb);
        web.setTitle(title);
        web.setDescription(desc);
        action.withMedia(web);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.setCallback(new Share(shareListener));
        action.open();

    }




    /**
     * 链接分享
     * @param shareUrl 分享链接
     * @param title 分享title
     * @param desc 分享内容
     * @param thumbUrl 分享缩略图--url
     * @param activity 上下文对象
     * @param shareListener 分享回调
     * **/
    public static void openShareUrl(String shareUrl,String title,String desc,String thumbUrl,Activity activity,ShareListener shareListener ){

        ShareAction action = new ShareAction(activity);
        UMWeb web = new UMWeb(shareUrl);
        UMImage thumb =  new UMImage(activity, thumbUrl);
        web.setThumb(thumb);
        web.setTitle(title);
        web.setDescription(desc);
        action.withMedia(web);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.setCallback(new Share(shareListener));
        action.open();
    }



    /**
     * 链接分享
     * @param shareUrl 分享链接
     * @param title 分享title
     * @param desc 分享内容
     * @param resourcesId 分享缩略图--资源文件
     * @param activity 上下文对象
     * @param shareListener 分享回调
     * @param sharePlatform 分享平台
     * **/
    public static void openShareUrl(String shareUrl, String title, String desc, int resourcesId, Activity activity, SharePlatform sharePlatform, ShareListener shareListener){

        ShareAction action = new ShareAction(activity);
        UMWeb web = new UMWeb(shareUrl);
        UMImage thumb =  new UMImage(activity, resourcesId);
        web.setThumb(thumb);
        web.setTitle(title);
        web.setDescription(desc);
        action.withMedia(web);

        if (sharePlatform == SharePlatform.QQ){
            action.setPlatform(SHARE_MEDIA.QZONE);
        }

        else if (sharePlatform == SharePlatform.WEIXIN_CIRCLE){
            action.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
        }
        else if (sharePlatform == SharePlatform.WEIXIN){
            action.setPlatform(SHARE_MEDIA.WEIXIN);
        }
        else if (sharePlatform == SharePlatform.SINA){
            action.setPlatform(SHARE_MEDIA.SINA);
        }

        action.setCallback(new Share(shareListener));
        action.share();


    }


    /**
     * 链接分享
     * @param shareUrl 分享链接
     * @param map 分享拼接链接
     * @param title 分享title
     * @param desc 分享内容
     * @param thumbUrl 分享缩略图--url
     * @param activity 上下文对象
     * @param shareListener 分享回调
     * **/
    public static void openShareUrl(String shareUrl,Map map,String title,String desc,String thumbUrl,Activity activity,ShareListener shareListener ){

        ShareAction action = new ShareAction(activity);
        UMWeb web = new UMWeb(shareUrl);
        UMImage thumb =  new UMImage(activity, thumbUrl);
        web.setThumb(thumb);
        web.setTitle(title);
        web.setDescription(desc);
        action.withMedia(web);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.setCallback(new Share(shareListener));
        action.open();

    }







    /**
     * 二维码分享
     * @param shareUrl 分享链接
     * @param map 二维码拼接地址
     * @param activity 上下文对象
     * @param shareListener 分享回调
     * **/
    public static void QRCodeShare(String shareUrl,Map map,Activity activity,ShareListener shareListener ){

        ShareAction action = new ShareAction(activity);
        UMImage umImage = new UMImage(activity,appendUrl(shareUrl,map));
        UMImage thumb =  new UMImage(activity,appendUrl(shareUrl,map));
        umImage.setThumb(thumb);
        action.withMedia(umImage);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.setCallback(new Share(shareListener));
        action.open();

    }




    /**
     * 图片分享
     * @param shapeImageUrl 图片链接
     * @param activity 上下文对象
     * @param shareListener 分享回调
     *
     * **/
    public static void openShareImage(String shapeImageUrl,Activity activity,ShareListener shareListener){
        ShareAction action = new ShareAction(activity);
        UMImage umImage = new UMImage(activity,shapeImageUrl);
        UMImage thumb =  new UMImage(activity,shapeImageUrl);
        umImage.setThumb(thumb);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.withMedia(umImage);
        action.setCallback(new Share(shareListener));
        action.open();
    }



    /**
     * 图片分享
     * @param bitmap bitmap图片
     * @param activity 上下文对象
     * @param shareListener 分享回调
     *
     * **/
    public static void openShareImage(Bitmap bitmap, Activity activity, ShareListener shareListener){
        ShareAction action = new ShareAction(activity);
        UMImage umImage = new UMImage(activity,bitmap);
        UMImage thumb =  new UMImage(activity,bitmap);
        umImage.setThumb(thumb);
        action.setDisplayList(SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QZONE);
        action.withMedia(umImage);
        action.setCallback(new Share(shareListener));
        action.open();
    }


















    /**
     * 分享回调
     * */
    private static final class Share implements UMShareListener{

        private ShareListener shareListener;
        public Share(ShareListener listener){
            this.shareListener = listener;
        }


        @Override
        public void onStart(SHARE_MEDIA share_media) {
            shareListener.onStart();
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            shareListener.onResult();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            shareListener.onError(throwable);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            shareListener.onCancel();
        }
    }



    //Activity回调
    public static void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(activity).onActivityResult(requestCode, resultCode, data);
    }




    //链接拼接
    private static String appendUrl(String shareUrl,Map map){
        String url = "";
        StringBuffer buffer = new StringBuffer(shareUrl);
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, String> entry = iter.next();

            if (entry.getValue().equals("")){
                continue;
            }

            buffer.append(entry.getKey()).append("=");
            buffer.append(entry.getValue()).append("&");
        }
        url = buffer.deleteCharAt(buffer.length() - 1).toString();

        return url;
    }


    /**
     * 判断客户端是否安装
     * **/
    public static boolean isInstall(Activity activity, SharePlatform sharePlatform){

        if (sharePlatform == SharePlatform.QQ){
            return UMShareAPI.get(activity).isInstall(activity,SHARE_MEDIA.QQ);
        }
        else if (sharePlatform == SharePlatform.QQZone){
            return UMShareAPI.get(activity).isInstall(activity,SHARE_MEDIA.QZONE);
        }
        else if (sharePlatform == SharePlatform.WEIXIN){
            return UMShareAPI.get(activity).isInstall(activity,SHARE_MEDIA.WEIXIN);
        }
        else if (sharePlatform == SharePlatform.SINA){
            return UMShareAPI.get(activity).isInstall(activity,SHARE_MEDIA.SINA);
        }

        return false;
    }
}
