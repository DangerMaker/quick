package com.hundsun.zjfae.activity.productreserve.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Description:长短预约详情预约支付发送类
 * @Author: zhoujianyu
 * @Time: 2018/10/29 10:16
 */
public class ReservePlayInfoBean implements Parcelable {
    private String orderType = "";
    private String orderBuyAmount = "";
    private String password = "";
    private String repeatCommitCheckCode = "";//重复提交码
    private String orderProductCode = "";
    private String checkCodeSerialNo = "";
    private String checkCode = "";//（短信验证码）
    private String payType = "";//设置支付方式 1为有混合支付 0为纯银行卡支付

    public ReservePlayInfoBean() {
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderBuyAmount() {
        return orderBuyAmount;
    }

    public void setOrderBuyAmount(String orderBuyAmount) {
        this.orderBuyAmount = orderBuyAmount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatCommitCheckCode() {
        return repeatCommitCheckCode;
    }

    public void setRepeatCommitCheckCode(String repeatCommitCheckCode) {
        this.repeatCommitCheckCode = repeatCommitCheckCode;
    }

    public String getOrderProductCode() {
        return orderProductCode;
    }

    public void setOrderProductCode(String orderProductCode) {
        this.orderProductCode = orderProductCode;
    }

    public String getCheckCodeSerialNo() {
        return checkCodeSerialNo;
    }

    public void setCheckCodeSerialNo(String checkCodeSerialNo) {
        this.checkCodeSerialNo = checkCodeSerialNo;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public static Creator<ReservePlayInfoBean> getCREATOR() {
        return CREATOR;
    }

    protected ReservePlayInfoBean(Parcel in) {
        orderType = in.readString();
        orderBuyAmount = in.readString();
        password = in.readString();
        repeatCommitCheckCode = in.readString();
        orderProductCode = in.readString();
        checkCodeSerialNo = in.readString();
        checkCode = in.readString();
        payType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderType);
        dest.writeString(orderBuyAmount);
        dest.writeString(password);
        dest.writeString(repeatCommitCheckCode);
        dest.writeString(orderProductCode);
        dest.writeString(checkCodeSerialNo);
        dest.writeString(checkCode);
        dest.writeString(payType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReservePlayInfoBean> CREATOR = new Creator<ReservePlayInfoBean>() {
        @Override
        public ReservePlayInfoBean createFromParcel(Parcel in) {
            return new ReservePlayInfoBean(in);
        }

        @Override
        public ReservePlayInfoBean[] newArray(int size) {
            return new ReservePlayInfoBean[size];
        }
    };
}
