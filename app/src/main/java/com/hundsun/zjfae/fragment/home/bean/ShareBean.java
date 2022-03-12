package com.hundsun.zjfae.fragment.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ShareBean implements Parcelable {



    private String uuid = "";
    private String funcIcons = "";
    private String aniParams = "";
    private String linkLocation = "";
    //webView加载链接
    private String funcUrl = "";

    //是否分享 0不分享，1分享
    private String isShare = "0";


    //分享类型， 1图片分享，2二维码分享，其他分享(链接分享)
    private String shareItem = "";

    //图片分享图片地址
    private String sharePicUrl = "";


    private String shareUrl = "";

    private String shareIsSure = "0";

    private String shareDesc = "";

    //跳转Action
    private String jumpRule = "";

    private String shareTitle = "";


    private String linkKeywordName = "";

    private String Keyword = "";



    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }


    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFuncIcons() {
        return funcIcons;
    }

    public void setFuncIcons(String funcIcons) {
        this.funcIcons = funcIcons;
    }

    public String getAniParams() {
        return aniParams;
    }

    public void setAniParams(String aniParams) {
        this.aniParams = aniParams;
    }

    public String getFuncUrl() {
        return funcUrl;
    }

    public void setFuncUrl(String funcUrl) {
        this.funcUrl = funcUrl;
    }

    public String getIsShare() {
        return isShare;
    }

    public void setIsShare(String isShare) {
        this.isShare = isShare;
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

    public String getLinkLocation() {
        return linkLocation;
    }

    public void setLinkLocation(String linkLocation) {
        this.linkLocation = linkLocation;
    }

    public String getShareIsSure() {
        return shareIsSure;
    }

    public void setShareIsSure(String shareIsSure) {
        this.shareIsSure = shareIsSure;
    }

    public String getJumpRule() {
        return jumpRule;
    }

    public void setJumpRule(String jumpRule) {
        this.jumpRule = jumpRule;
    }

    public String getLinkKeywordName() {
        return linkKeywordName;
    }

    public void setLinkKeywordName(String linkKeywordName) {
        this.linkKeywordName = linkKeywordName;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }
    public ShareBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.funcIcons);
        dest.writeString(this.aniParams);
        dest.writeString(this.linkLocation);
        dest.writeString(this.funcUrl);
        dest.writeString(this.isShare);
        dest.writeString(this.shareItem);
        dest.writeString(this.sharePicUrl);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareIsSure);
        dest.writeString(this.shareDesc);
        dest.writeString(this.jumpRule);
        dest.writeString(this.shareTitle);
        dest.writeString(this.linkKeywordName);
        dest.writeString(this.Keyword);
    }

    protected ShareBean(Parcel in) {
        this.uuid = in.readString();
        this.funcIcons = in.readString();
        this.aniParams = in.readString();
        this.linkLocation = in.readString();
        this.funcUrl = in.readString();
        this.isShare = in.readString();
        this.shareItem = in.readString();
        this.sharePicUrl = in.readString();
        this.shareUrl = in.readString();
        this.shareIsSure = in.readString();
        this.shareDesc = in.readString();
        this.jumpRule = in.readString();
        this.shareTitle = in.readString();
        this.linkKeywordName = in.readString();
        this.Keyword = in.readString();
    }

    public static final Creator<ShareBean> CREATOR = new Creator<ShareBean>() {
        @Override
        public ShareBean createFromParcel(Parcel source) {
            return new ShareBean(source);
        }

        @Override
        public ShareBean[] newArray(int size) {
            return new ShareBean[size];
        }
    };
}
