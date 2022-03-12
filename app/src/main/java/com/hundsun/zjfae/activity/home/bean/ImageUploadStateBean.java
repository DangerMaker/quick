package com.hundsun.zjfae.activity.home.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageUploadStateBean implements Parcelable {

    private String returnCode;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private String returnMsg;
    private String fileName;
    private String filePath;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    private String model;

    public ImageUploadStateBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.returnCode);
        dest.writeString(this.returnMsg);
        dest.writeString(this.fileName);
        dest.writeString(this.filePath);
        dest.writeString(this.model);
    }

    protected ImageUploadStateBean(Parcel in) {
        this.returnCode = in.readString();
        this.returnMsg = in.readString();
        this.fileName = in.readString();
        this.filePath = in.readString();
        this.model = in.readString();
    }

    public static final Creator<ImageUploadStateBean> CREATOR = new Creator<ImageUploadStateBean>() {
        @Override
        public ImageUploadStateBean createFromParcel(Parcel source) {
            return new ImageUploadStateBean(source);
        }

        @Override
        public ImageUploadStateBean[] newArray(int size) {
            return new ImageUploadStateBean[size];
        }
    };
}
