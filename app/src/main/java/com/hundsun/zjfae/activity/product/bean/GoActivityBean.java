package com.hundsun.zjfae.activity.product.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description:
 * @Author: zhoujianyu
 * @Time: 2018/11/27 16:18
 */
public class GoActivityBean implements Parcelable {
    private String productCode;
    private String productName;
    //预约未购买首页弹框专区区分：默认普通预约：02，特约交易专区03
    private String orderType = "";
    private String orderNum;

    //跳转webView
    private String jumpUrl;

    private String areaJumpType;




    //预约首页弹框是否显示立即预约按钮
    private String isDisplayBuy;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getAreaJumpType() {
        return areaJumpType;
    }

    public void setAreaJumpType(String areaJumpType) {
        this.areaJumpType = areaJumpType;
    }

    public String getIsDisplayBuy() {
        return isDisplayBuy;
    }

    public void setIsDisplayBuy(String isDisplayBuy) {
        this.isDisplayBuy = isDisplayBuy;
    }

    public GoActivityBean() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
        dest.writeString(this.orderType);
        dest.writeString(this.orderNum);
        dest.writeString(this.jumpUrl);
        dest.writeString(this.areaJumpType);
        dest.writeString(this.isDisplayBuy);
    }

    protected GoActivityBean(Parcel in) {
        this.productCode = in.readString();
        this.productName = in.readString();
        this.orderType = in.readString();
        this.orderNum = in.readString();
        this.jumpUrl = in.readString();
        this.areaJumpType = in.readString();
        this.isDisplayBuy = in.readString();
    }

    public static final Creator<GoActivityBean> CREATOR = new Creator<GoActivityBean>() {
        @Override
        public GoActivityBean createFromParcel(Parcel source) {
            return new GoActivityBean(source);
        }

        @Override
        public GoActivityBean[] newArray(int size) {
            return new GoActivityBean[size];
        }
    };
}
