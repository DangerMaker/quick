package com.zjfae.jpush;

import android.os.Parcel;
import android.os.Parcelable;

public class Extras implements Parcelable {


    /**
     * content : LC18080058
     * messageId : f892c9894912488a9803161326b9843c
     * msgPublishType : 01
     * needLogin : 1
     * productCode : LC18080058
     * type : productDetail
     */

    private String content;
    private String messageId;
    private String msgPublishType;
    private String needLogin;
    private String productCode;
    private String type;


    private int notificationId;
    private String title;
    private String JPContent;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMsgPublishType() {
        return msgPublishType;
    }

    public void setMsgPublishType(String msgPublishType) {
        this.msgPublishType = msgPublishType;
    }

    public String getNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(String needLogin) {
        this.needLogin = needLogin;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJPContent() {
        return JPContent;
    }

    public void setJPContent(String JPContent) {
        this.JPContent = JPContent;
    }

    public Extras() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.messageId);
        dest.writeString(this.msgPublishType);
        dest.writeString(this.needLogin);
        dest.writeString(this.productCode);
        dest.writeString(this.type);
        dest.writeInt(this.notificationId);
        dest.writeString(this.title);
        dest.writeString(this.JPContent);
    }

    protected Extras(Parcel in) {
        this.content = in.readString();
        this.messageId = in.readString();
        this.msgPublishType = in.readString();
        this.needLogin = in.readString();
        this.productCode = in.readString();
        this.type = in.readString();
        this.notificationId = in.readInt();
        this.title = in.readString();
        this.JPContent = in.readString();
    }

    public static final Creator<Extras> CREATOR = new Creator<Extras>() {
        @Override
        public Extras createFromParcel(Parcel source) {
            return new Extras(source);
        }

        @Override
        public Extras[] newArray(int size) {
            return new Extras[size];
        }
    };
}
