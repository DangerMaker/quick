package com.hundsun.zjfae.activity.product.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AttachmentBean implements Parcelable {

    private String id;

    private String title;

    /*
    * type=9 购买详情 特约
     *type=12   ;   预约
     * type=10；转让
     * type=11 我要卖 我的持仓商品详情
     * type = 8;
     * */
    private String type;

    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.url);
    }

    public AttachmentBean() {
    }

    protected AttachmentBean(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.url = in.readString();
    }

    public static final Creator<AttachmentBean> CREATOR = new Creator<AttachmentBean>() {
        @Override
        public AttachmentBean createFromParcel(Parcel source) {
            return new AttachmentBean(source);
        }

        @Override
        public AttachmentBean[] newArray(int size) {
            return new AttachmentBean[size];
        }
    };
}
