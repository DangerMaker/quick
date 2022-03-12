package com.hundsun.zjfae.common.http.cookies;

import android.os.Parcel;
import android.os.Parcelable;

public class CookieParcelable implements Parcelable {


    private String name = "";

    private String value = "";

    private String domain = "";

    private String path = "";



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }



    public CookieParcelable() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.value);
        dest.writeString(this.domain);
        dest.writeString(this.path);
    }

    protected CookieParcelable(Parcel in) {
        this.name = in.readString();
        this.value = in.readString();
        this.domain = in.readString();
        this.path = in.readString();
    }

    public static final Creator<CookieParcelable> CREATOR = new Creator<CookieParcelable>() {
        @Override
        public CookieParcelable createFromParcel(Parcel source) {
            return new CookieParcelable(source);
        }

        @Override
        public CookieParcelable[] newArray(int size) {
            return new CookieParcelable[size];
        }
    };
}
