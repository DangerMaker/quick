package com.hundsun.zjfae.activity.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageDataUtils implements Parcelable {

    private String dynamicValue,dynamicKey,model,compressPath;

    public String getDynamicValue() {
        return dynamicValue;
    }

    public void setDynamicValue(String dynamicValue) {
        this.dynamicValue = dynamicValue;
    }

    public String getDynamicKey() {
        return dynamicKey;
    }

    public void setDynamicKey(String dynamicKey) {
        this.dynamicKey = dynamicKey;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dynamicValue);
        dest.writeString(this.dynamicKey);
        dest.writeString(this.model);
        dest.writeString(this.compressPath);
    }

    public ImageDataUtils() {
    }

    protected ImageDataUtils(Parcel in) {
        this.dynamicValue = in.readString();
        this.dynamicKey = in.readString();
        this.model = in.readString();
        this.compressPath = in.readString();
    }

    public static final Creator<ImageDataUtils> CREATOR = new Creator<ImageDataUtils>() {
        @Override
        public ImageDataUtils createFromParcel(Parcel source) {
            return new ImageDataUtils(source);
        }

        @Override
        public ImageDataUtils[] newArray(int size) {
            return new ImageDataUtils[size];
        }
    };
}
