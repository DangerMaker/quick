package com.hundsun.zjfae.activity.product.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 红包
 * **/
public class RadEnvelopeBean implements Parcelable {

    /**
     * 	"quanDetailsId": "KDR01kLvp908",
     "quanType": "R",
     "quanTypeName": "红包",
     "quanValue": "100",
     "quanCanStack": "1",
     "quanValidityEnd": "2045年12月10日",
     "quanName": "CJW发红包"
     quanCatalogRemark
     mostFineFlag : 1：最优；0：非最优
     * */

    private String quanDetailsId="";
    private String quanType="";
    private String quanTypeName="";
    private String quanValue="";
    private String quanCanStack="";

    private String quanValidityEnd="";
    private String quanName="";
    private String quanCatalogRemark="";
    private String mostFineFlag = "";


    public String getQuanFullReducedAmount() {
        return quanFullReducedAmount;
    }

    public void setQuanFullReducedAmount(String quanFullReducedAmount) {
        this.quanFullReducedAmount = quanFullReducedAmount;
    }

    private String quanFullReducedAmount;//满减金额

    public String getQuanCatalogRemark() {
        return quanCatalogRemark;
    }

    public void setQuanCatalogRemark(String quanCatalogRemark) {
        this.quanCatalogRemark = quanCatalogRemark;
    }

    private String quanValidityStart;

    public String getQuanValidityStart() {
        return quanValidityStart;
    }

    public void setQuanValidityStart(String quanValidityStart) {
        this.quanValidityStart = quanValidityStart;
    }





    public String getQuanDetailsId() {
        return quanDetailsId;
    }

    public void setQuanDetailsId(String quanDetailsId) {
        this.quanDetailsId = quanDetailsId;
    }

    public String getQuanType() {
        return quanType;
    }

    public void setQuanType(String quanType) {
        this.quanType = quanType;
    }

    public String getQuanTypeName() {
        return quanTypeName;
    }

    public void setQuanTypeName(String quanTypeName) {
        this.quanTypeName = quanTypeName;
    }

    public String getQuanValue() {
        return quanValue;
    }

    public void setQuanValue(String quanValue) {
        this.quanValue = quanValue;
    }

    public String getQuanCanStack() {
        return quanCanStack;
    }

    public void setQuanCanStack(String quanCanStack) {
        this.quanCanStack = quanCanStack;
    }

    public String getQuanValidityEnd() {
        return quanValidityEnd;
    }

    public void setQuanValidityEnd(String quanValidityEnd) {
        this.quanValidityEnd = quanValidityEnd;
    }

    public String getQuanName() {
        return quanName;
    }

    public void setQuanName(String quanName) {
        this.quanName = quanName;
    }


    public String getMostFineFlag() {
        return mostFineFlag;
    }

    public void setMostFineFlag(String mostFineFlag) {
        this.mostFineFlag = mostFineFlag;
    }

    public RadEnvelopeBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quanDetailsId);
        dest.writeString(this.quanType);
        dest.writeString(this.quanTypeName);
        dest.writeString(this.quanValue);
        dest.writeString(this.quanCanStack);
        dest.writeString(this.quanValidityEnd);
        dest.writeString(this.quanName);
        dest.writeString(this.quanCatalogRemark);
        dest.writeString(this.mostFineFlag);
        dest.writeString(this.quanFullReducedAmount);
        dest.writeString(this.quanValidityStart);
    }

    protected RadEnvelopeBean(Parcel in) {
        this.quanDetailsId = in.readString();
        this.quanType = in.readString();
        this.quanTypeName = in.readString();
        this.quanValue = in.readString();
        this.quanCanStack = in.readString();
        this.quanValidityEnd = in.readString();
        this.quanName = in.readString();
        this.quanCatalogRemark = in.readString();
        this.mostFineFlag = in.readString();
        this.quanFullReducedAmount = in.readString();
        this.quanValidityStart = in.readString();
    }

    public static final Creator<RadEnvelopeBean> CREATOR = new Creator<RadEnvelopeBean>() {
        @Override
        public RadEnvelopeBean createFromParcel(Parcel source) {
            return new RadEnvelopeBean(source);
        }

        @Override
        public RadEnvelopeBean[] newArray(int size) {
            return new RadEnvelopeBean[size];
        }
    };
}
