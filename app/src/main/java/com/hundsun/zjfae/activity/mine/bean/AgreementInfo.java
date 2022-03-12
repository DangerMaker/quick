package com.hundsun.zjfae.activity.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.hundsun.zjfae.common.utils.Utils;

public class AgreementInfo implements Parcelable {


    private String name;//身份证姓名
    private String certificateCode;//身份证号码
    private String account;//资金账号
    private String bankCard;//银行卡号
    private String bankName;//银行名称

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getYear() {
        return Utils.getYear();
    }



    public int getMonth() {
        return Utils.getMonth();
    }


    public int getDay() {
        return Utils.getDay();
    }


    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.certificateCode);
        dest.writeString(this.account);
        dest.writeString(this.bankCard);
        dest.writeString(this.bankName);
    }

    public AgreementInfo() {
    }

    protected AgreementInfo(Parcel in) {
        this.name = in.readString();
        this.certificateCode = in.readString();
        this.account = in.readString();
        this.bankCard = in.readString();
        this.bankName = in.readString();
    }

    public static final Creator<AgreementInfo> CREATOR = new Creator<AgreementInfo>() {
        @Override
        public AgreementInfo createFromParcel(Parcel source) {
            return new AgreementInfo(source);
        }

        @Override
        public AgreementInfo[] newArray(int size) {
            return new AgreementInfo[size];
        }
    };
}
