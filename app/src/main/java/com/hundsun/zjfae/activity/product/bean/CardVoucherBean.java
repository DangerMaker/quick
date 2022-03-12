package com.hundsun.zjfae.activity.product.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 卡券详情
 * */
public class CardVoucherBean implements Parcelable {



    /**
     * quanDetailsId : KDA01j5aL486
     * quanType : A
     * quanTypeName : 加息券
     * quanValue : 0.011
     * quanCanStack : 0
     * quanCatalogRemark_
     * quanValidityStart:2018年10月1号
     * quanValidityEnd : 2018年11月24日
     * quanIncreaseInterestAmount : 5000
     * enableIncreaseInterestAmount : 1000.00
     * percentValue : 1.1%
     * quanName : 有加息金额的加息券
     * quanArrivalPriceLadder
     * quanFullReducedAmount : 1000
     * catalogRemark: 备注
     * mostFineFlag : 1：最优；0：非最优
     */

    private String quanDetailsId="";
    private String quanType="";
    private String quanTypeName="";
    private String quanValue="";


    private String quanCanStack="";
    private String quanValidityEnd="";
    private String quanIncreaseInterestAmount="";
    private String enableIncreaseInterestAmount="";
    private String percentValue="";
    private String quanName="";
    private String quanFullReducedAmount="";
    private String catalogRemark="";//备注
    private String quanArrivalPriceLadder="";

    private String quanCatalogRemark="";//使用说明

    private String mostFineFlag = "";

    public String getQuanArrivalPriceLadder() {
        return quanArrivalPriceLadder;
    }

    public void setQuanArrivalPriceLadder(String quanArrivalPriceLadder) {
        this.quanArrivalPriceLadder = quanArrivalPriceLadder;
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

    public String getQuanIncreaseInterestAmount() {
        return quanIncreaseInterestAmount;
    }

    public void setQuanIncreaseInterestAmount(String quanIncreaseInterestAmount) {
        this.quanIncreaseInterestAmount = quanIncreaseInterestAmount;
    }

    public String getEnableIncreaseInterestAmount() {
        return enableIncreaseInterestAmount;
    }

    public void setEnableIncreaseInterestAmount(String enableIncreaseInterestAmount) {
        this.enableIncreaseInterestAmount = enableIncreaseInterestAmount;
    }

    public String getPercentValue() {
        return percentValue;
    }

    public void setPercentValue(String percentValue) {
        this.percentValue = percentValue;
    }

    public String getQuanName() {
        return quanName;
    }

    public void setQuanName(String quanName) {
        this.quanName = quanName;
    }

    public String getQuanFullReducedAmount() {
        return quanFullReducedAmount;
    }

    public void setQuanFullReducedAmount(String quanFullReducedAmount) {
        this.quanFullReducedAmount = quanFullReducedAmount;
    }
    public String getCatalogRemark() {
        return catalogRemark;
    }

    public void setCatalogRemark(String catalogRemark) {
        this.catalogRemark = catalogRemark;
    }

    public String getQuanCatalogRemark() {
        return quanCatalogRemark;
    }

    public void setQuanCatalogRemark(String quanCatalogRemark) {
        this.quanCatalogRemark = quanCatalogRemark;
    }

    public String getMostFineFlag() {
        return mostFineFlag;
    }

    public void setMostFineFlag(String mostFineFlag) {
        this.mostFineFlag = mostFineFlag;
    }

    public CardVoucherBean() {
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
        dest.writeString(this.quanIncreaseInterestAmount);
        dest.writeString(this.enableIncreaseInterestAmount);
        dest.writeString(this.percentValue);
        dest.writeString(this.quanName);
        dest.writeString(this.quanFullReducedAmount);
        dest.writeString(this.catalogRemark);
        dest.writeString(this.quanArrivalPriceLadder);
        dest.writeString(this.quanCatalogRemark);
        dest.writeString(this.mostFineFlag);
    }

    protected CardVoucherBean(Parcel in) {
        this.quanDetailsId = in.readString();
        this.quanType = in.readString();
        this.quanTypeName = in.readString();
        this.quanValue = in.readString();
        this.quanCanStack = in.readString();
        this.quanValidityEnd = in.readString();
        this.quanIncreaseInterestAmount = in.readString();
        this.enableIncreaseInterestAmount = in.readString();
        this.percentValue = in.readString();
        this.quanName = in.readString();
        this.quanFullReducedAmount = in.readString();
        this.catalogRemark = in.readString();
        this.quanArrivalPriceLadder = in.readString();
        this.quanCatalogRemark = in.readString();
        this.mostFineFlag = in.readString();
    }

    public static final Creator<CardVoucherBean> CREATOR = new Creator<CardVoucherBean>() {
        @Override
        public CardVoucherBean createFromParcel(Parcel source) {
            return new CardVoucherBean(source);
        }

        @Override
        public CardVoucherBean[] newArray(int size) {
            return new CardVoucherBean[size];
        }
    };
}
