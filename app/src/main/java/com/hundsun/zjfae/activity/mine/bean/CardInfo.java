package com.hundsun.zjfae.activity.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class CardInfo implements Parcelable {

    //卡券编号
    private String quanDetailsId = "";
    //核销产品编号
    private String quanUsedProductCode = "";
    //核销系列编号
    private String quanUsedSeriesCode = "";

    public String getQuanDetailsId() {
        return quanDetailsId;
    }

    public void setQuanDetailsId(String quanDetailsId) {
        this.quanDetailsId = quanDetailsId;
    }

    public String getQuanUsedProductCode() {
        return quanUsedProductCode;
    }

    public void setQuanUsedProductCode(String quanUsedProductCode) {
        this.quanUsedProductCode = quanUsedProductCode;
    }

    public String getQuanUsedSeriesCode() {
        return quanUsedSeriesCode;
    }

    public void setQuanUsedSeriesCode(String quanUsedSeriesCode) {
        this.quanUsedSeriesCode = quanUsedSeriesCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quanDetailsId);
        dest.writeString(this.quanUsedProductCode);
        dest.writeString(this.quanUsedSeriesCode);
    }

    public CardInfo() {
    }

    protected CardInfo(Parcel in) {
        this.quanDetailsId = in.readString();
        this.quanUsedProductCode = in.readString();
        this.quanUsedSeriesCode = in.readString();
    }

    public static final Creator<CardInfo> CREATOR = new Creator<CardInfo>() {
        @Override
        public CardInfo createFromParcel(Parcel source) {
            return new CardInfo(source);
        }

        @Override
        public CardInfo[] newArray(int size) {
            return new CardInfo[size];
        }
    };
}
