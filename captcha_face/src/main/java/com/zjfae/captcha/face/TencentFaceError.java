package com.zjfae.captcha.face;

import android.os.Parcel;
import android.os.Parcelable;

public class TencentFaceError implements Parcelable {

    /**
     *错误发生的阶段
     * */
    private String domain;

    /**
     * 错误码
     * */
    private String code;

    /**
     * 错误描述,如有需求，可以展示给用户
     * */
    private String desc;

    /**
     * 错误信息内容,错误的详细实际原因，主要用于定位问题
     * */
    private String reason;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.domain);
        dest.writeString(this.code);
        dest.writeString(this.desc);
        dest.writeString(this.reason);
    }

    public TencentFaceError() {
    }

    protected TencentFaceError(Parcel in) {
        this.domain = in.readString();
        this.code = in.readString();
        this.desc = in.readString();
        this.reason = in.readString();
    }

    public static final Creator<TencentFaceError> CREATOR = new Creator<TencentFaceError>() {
        @Override
        public TencentFaceError createFromParcel(Parcel source) {
            return new TencentFaceError(source);
        }

        @Override
        public TencentFaceError[] newArray(int size) {
            return new TencentFaceError[size];
        }
    };
}
