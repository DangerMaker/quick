package com.hundsun.zjfae.activity.mine.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class  AddBankListInfo implements Parcelable {
    private String accountType;
    private String bankCode;
    private String bankFlag;
    private String bankName;
    private String gmtCreate;
    private String gmtModify;
    private String id;
    private String pictName;
    private String sort;
    private String status;
    private String sustainRange;
    private String titleType;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankFlag() {
        return bankFlag;
    }

    public void setBankFlag(String bankFlag) {
        this.bankFlag = bankFlag;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPictName() {
        return pictName;
    }

    public void setPictName(String pictName) {
        this.pictName = pictName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSustainRange() {
        return sustainRange;
    }

    public void setSustainRange(String sustainRange) {
        this.sustainRange = sustainRange;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountType);
        dest.writeString(this.bankCode);
        dest.writeString(this.bankFlag);
        dest.writeString(this.bankName);
        dest.writeString(this.gmtCreate);
        dest.writeString(this.gmtModify);
        dest.writeString(this.id);
        dest.writeString(this.pictName);
        dest.writeString(this.sort);
        dest.writeString(this.status);
        dest.writeString(this.sustainRange);
        dest.writeString(this.titleType);
    }

    public AddBankListInfo() {
    }

    protected AddBankListInfo(Parcel in) {
        this.accountType = in.readString();
        this.bankCode = in.readString();
        this.bankFlag = in.readString();
        this.bankName = in.readString();
        this.gmtCreate = in.readString();
        this.gmtModify = in.readString();
        this.id = in.readString();
        this.pictName = in.readString();
        this.sort = in.readString();
        this.status = in.readString();
        this.sustainRange = in.readString();
        this.titleType = in.readString();
    }

    public static final Creator<AddBankListInfo> CREATOR = new Creator<AddBankListInfo>() {
        @Override
        public AddBankListInfo createFromParcel(Parcel source) {
            return new AddBankListInfo(source);
        }

        @Override
        public AddBankListInfo[] newArray(int size) {
            return new AddBankListInfo[size];
        }
    };
}
