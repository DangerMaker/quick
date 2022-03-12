package com.hundsun.zjfae.activity.home.bean;

import android.os.Parcel;
import android.os.Parcelable;



public class UpLoadPicImageBean implements Parcelable {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDynamicKey() {
        return dynamicKey;
    }

    public void setDynamicKey(String dynamicKey) {
        this.dynamicKey = dynamicKey;
    }

    public String getDynamicValue() {
        return dynamicValue;
    }

    public void setDynamicValue(String dynamicValue) {
        this.dynamicValue = dynamicValue;
    }





    private long id;

    private String imagePath;

    private String model;

    private String dynamicKey;

    private String dynamicValue;


    public UpLoadPicImageBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.imagePath);
        dest.writeString(this.model);
        dest.writeString(this.dynamicKey);
        dest.writeString(this.dynamicValue);
    }

    protected UpLoadPicImageBean(Parcel in) {
        this.id = in.readInt();
        this.imagePath = in.readString();
        this.model = in.readString();
        this.dynamicKey = in.readString();
        this.dynamicValue = in.readString();
    }

    public static final Creator<UpLoadPicImageBean> CREATOR = new Creator<UpLoadPicImageBean>() {
        @Override
        public UpLoadPicImageBean createFromParcel(Parcel source) {
            return new UpLoadPicImageBean(source);
        }

        @Override
        public UpLoadPicImageBean[] newArray(int size) {
            return new UpLoadPicImageBean[size];
        }
    };
}
