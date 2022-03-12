package com.hundsun.zjfae.activity.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddBanInfo implements Parcelable {



    private List<AddBankListInfo> bankListInfoList;
    private String bankName = "请选择";
    private String bankCodeNo = "0000";

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCodeNo() {
        return bankCodeNo;
    }

    public void setBankCodeNo(String bankCodeNo) {
        this.bankCodeNo = bankCodeNo;
    }




    public List<AddBankListInfo> getBankListInfoList() {
        return bankListInfoList;
    }

    public void setBankListInfoList(List<AddBankListInfo> bankListInfoList) {
        this.bankListInfoList = bankListInfoList;
    }

    public AddBanInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.bankListInfoList);
        dest.writeString(this.bankName);
        dest.writeString(this.bankCodeNo);
    }

    protected AddBanInfo(Parcel in) {
        this.bankListInfoList = in.createTypedArrayList(AddBankListInfo.CREATOR);
        this.bankName = in.readString();
        this.bankCodeNo = in.readString();
    }

    public static final Creator<AddBanInfo> CREATOR = new Creator<AddBanInfo>() {
        @Override
        public AddBanInfo createFromParcel(Parcel source) {
            return new AddBanInfo(source);
        }

        @Override
        public AddBanInfo[] newArray(int size) {
            return new AddBanInfo[size];
        }
    };
}
