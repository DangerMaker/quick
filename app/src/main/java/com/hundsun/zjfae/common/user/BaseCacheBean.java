package com.hundsun.zjfae.common.user;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description:用于存放本地图片或者内容的缓存数据实体类
 * @Author: zhoujianyu
 * @Time: 2019/1/24 13:51
 */
public class BaseCacheBean implements Parcelable {
    /** 以下为图片缓存所需字段 **/
    private String uuid;//图片名字

    private String iconsPosition;//位置

    private String iconsLocation;//位置

    private String iconsAddress = "";//图片地址

    private String is_show = "";//是否展示

    //启动时间
    private String startUpTime = "5";

    private String resTime;//请求时间

    /** 以下为文字缓存所需字段 **/
    private String title;//标题

    private String content;//内容

    private String intervalSecond = "";//角标轮询间隔（秒）


    private String intervalSwitch = "";//角标是否开启

    /** 以下为首页理财专区所需字段 **/
    private String keyword = "";
    private String link_keyword_name = "";
    private String linkLocation = "";

    /** 以下为首页广告弹框时间确认字段 **/
    private String resTime1;


    private String showTime = "";


    private String isUpdate = "";

    private String button_title;//免责声明按钮显示文字

    //webView加载链接
    private String funcUrl = "";

    //是否分享 0不分享，1分享
    private String isShare = "0";


    //分享类型， 1图片分享，2二维码分享，其他分享(链接分享)
    private String shareItem = "";

    //图片分享图片地址
    private String sharePicUrl = "";


    private String shareUrl = "";

    private String shareIsSure = "";

    private String shareDesc= "";

    //跳转Action
    private String jumpRule = "";

    private String shareTitle = "";

    private String funcIcons = "";


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIconsPosition() {
        return iconsPosition;
    }

    public void setIconsPosition(String iconsPosition) {
        this.iconsPosition = iconsPosition;
    }

    public String getIconsLocation() {
        return iconsLocation;
    }

    public void setIconsLocation(String iconsLocation) {
        this.iconsLocation = iconsLocation;
    }

    public String getIconsAddress() {
        return iconsAddress;
    }

    public void setIconsAddress(String iconsAddress) {
        this.iconsAddress = iconsAddress;
    }

    public String getResTime() {
        return resTime;
    }

    public void setResTime(String resTime) {
        this.resTime = resTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntervalSecond() {
        return intervalSecond;
    }

    public void setIntervalSecond(String intervalSecond) {
        this.intervalSecond = intervalSecond;
    }

    public String getIntervalSwitch() {
        return intervalSwitch;
    }

    public void setIntervalSwitch(String intervalSwitch) {
        this.intervalSwitch = intervalSwitch;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
    }



    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLink_keyword_name() {
        return link_keyword_name;
    }

    public void setLink_keyword_name(String link_keyword_name) {
        this.link_keyword_name = link_keyword_name;
    }

    public String getLinkLocation() {
        return linkLocation;
    }

    public void setLinkLocation(String linkLocation) {
        this.linkLocation = linkLocation;
    }

    public String getResTime1() {
        return resTime1;
    }

    public void setResTime1(String resTime1) {
        this.resTime1 = resTime1;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getButton_title() {
        return button_title;
    }

    public void setButton_title(String button_title) {
        this.button_title = button_title;
    }

    public String getStartUpTime() {
        return startUpTime;
    }

    public void setStartUpTime(String startUpTime) {
        this.startUpTime = startUpTime;
    }

    public String getShareIsSure() {
        return shareIsSure;
    }

    public void setShareIsSure(String shareIsSure) {
        this.shareIsSure = shareIsSure;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getFuncUrl() {
        return funcUrl;
    }

    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl;
    }

    public String getShareItem() {
        return shareItem;
    }

    public void setShareItem(String shareItem) {
        this.shareItem = shareItem;
    }

    public String getSharePicUrl() {
        return sharePicUrl;
    }

    public void setSharePicUrl(String sharePicUrl) {
        this.sharePicUrl = sharePicUrl;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getJumpRule() {
        return jumpRule;
    }

    public void setJumpRule(String jumpRule) {
        this.jumpRule = jumpRule;
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getFuncIcons() {
        return funcIcons;
    }

    public void setFuncIcons(String funcIcons) {
        this.funcIcons = funcIcons;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.iconsPosition);
        dest.writeString(this.iconsLocation);
        dest.writeString(this.iconsAddress);
        dest.writeString(this.is_show);
        dest.writeString(this.startUpTime);
        dest.writeString(this.resTime);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.intervalSecond);
        dest.writeString(this.intervalSwitch);
        dest.writeString(this.keyword);
        dest.writeString(this.link_keyword_name);
        dest.writeString(this.linkLocation);
        dest.writeString(this.resTime1);
        dest.writeString(this.showTime);
        dest.writeString(this.isUpdate);
        dest.writeString(this.button_title);
        dest.writeString(this.funcUrl);
        dest.writeString(this.isShare);
        dest.writeString(this.shareItem);
        dest.writeString(this.sharePicUrl);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareIsSure);
        dest.writeString(this.shareDesc);
        dest.writeString(this.jumpRule);
        dest.writeString(this.shareTitle);
        dest.writeString(this.funcIcons);
    }

    public BaseCacheBean() {
    }

    protected BaseCacheBean(Parcel in) {
        this.uuid = in.readString();
        this.iconsPosition = in.readString();
        this.iconsLocation = in.readString();
        this.iconsAddress = in.readString();
        this.is_show = in.readString();
        this.startUpTime = in.readString();
        this.resTime = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.intervalSecond = in.readString();
        this.intervalSwitch = in.readString();
        this.keyword = in.readString();
        this.link_keyword_name = in.readString();
        this.linkLocation = in.readString();
        this.resTime1 = in.readString();
        this.showTime = in.readString();
        this.isUpdate = in.readString();
        this.button_title = in.readString();
        this.funcUrl = in.readString();
        this.isShare = in.readString();
        this.shareItem = in.readString();
        this.sharePicUrl = in.readString();
        this.shareUrl = in.readString();
        this.shareIsSure = in.readString();
        this.shareDesc = in.readString();
        this.jumpRule = in.readString();
        this.shareTitle = in.readString();
        this.funcIcons = in.readString();
    }

    public static final Creator<BaseCacheBean> CREATOR = new Creator<BaseCacheBean>() {
        @Override
        public BaseCacheBean createFromParcel(Parcel source) {
            return new BaseCacheBean(source);
        }

        @Override
        public BaseCacheBean[] newArray(int size) {
            return new BaseCacheBean[size];
        }
    };
}
