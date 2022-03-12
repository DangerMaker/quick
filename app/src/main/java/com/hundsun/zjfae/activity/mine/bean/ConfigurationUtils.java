package com.hundsun.zjfae.activity.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ConfigurationUtils implements Parcelable {

    private String idCard;//身份证号

    private String idCardName;//身份证名字

    private String userId;//用户id

    private String verifyscene = "unbindCard";//用户场景  "unbindCard"//解绑,"changeCard"//换卡

    private String bankName;//银行卡名字

    private String payChannelName;//

    private String uploadFileForLiveness;//活体检测校检地址

    private String mobile;//用户手机号

    private String noticeContent;//人脸识别未授权协议内容

    private String pageTitle;//title

    private String noticeTitle;//title


    private boolean isNeedSms;//是否需要短信
    private boolean isZjSendSms;//是否需要浙金短信
    private boolean needIdCard;//签约解绑是否需要身份证
    private String payChannelNo;//支付渠道

    private String businessType = "";//正常换卡和人脸识别为true，资料上传为false


    //unbindBankCardUpload解绑卡
    //changeBankCardUpload换卡申请
    //字段上送用，不参与逻辑判断
    private String dynamicType = "";




    /**
     * highNetWorth = 合格投资者材料上传
     * unbindCard = 解绑
     * changeCard = 换卡
     * account = 上传头像
     * 字段上送用，不参与逻辑判断
     * */
    private String partValue = "";


    //身份证姓名
    private String name;

    //身份证号码
    private String certificateCodeAll;


    //资金账号
    private String account = "";




    //失败次数
    private int failures = 0;

    //腾讯face订单号
    private String tencentfaceOrder = "";

    public boolean isNeedIdCard() {
        return needIdCard;
    }

    public void setNeedIdCard(boolean needIdCard) {
        this.needIdCard = needIdCard;
    }

    public String getTencentfaceOrder() {
        return tencentfaceOrder;
    }

    public void setTencentfaceOrder(String tencentfaceOrder) {
        this.tencentfaceOrder = tencentfaceOrder;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }






    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerifyscene() {

        return verifyscene;
    }


    public void setVerifyscene(String verifyscene) {
        this.verifyscene = verifyscene;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayChannelName() {
        return payChannelName;
    }

    public void setPayChannelName(String payChannelName) {
        this.payChannelName = payChannelName;
    }


    public String getUploadFileForLiveness() {
        return uploadFileForLiveness;
    }

    public void setUploadFileForLiveness(String uploadFileForLiveness) {
        this.uploadFileForLiveness = uploadFileForLiveness;
    }

    public boolean isNeedSms() {
        return isNeedSms;
    }

    public void setNeedSms(boolean needSms) {
        isNeedSms = needSms;
    }

    public boolean isZjSendSms() {
        return isZjSendSms;
    }

    public void setZjSendSms(boolean zjSendSms) {
        isZjSendSms = zjSendSms;
    }

    public String getPayChannelNo() {
        return payChannelNo;
    }

    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }




    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }



    public String getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(String dynamicType) {
        this.dynamicType = dynamicType;
    }

    public void setPartValue(String partValue) {
        this.partValue = partValue;
    }

    public String getPartValue() {
        return partValue;
    }

    //    public String getPartValue() {
//
//        //解绑卡
//        if (getBusinessType().equals("true")){
//
//            return "unbindCard";
//        }
//        else if (getBusinessType().equals("false")){
//            //换卡
//            return "changeCard";
//        }
//
//        return "unbindCard";
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCertificateCodeAll() {
        return certificateCodeAll;
    }

    public void setCertificateCodeAll(String certificateCodeAll) {
        this.certificateCodeAll = certificateCodeAll;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }



    public int getFailures() {
        return failures;
    }

    public void setFailures(int failures) {
        this.failures = failures;
    }

    public ConfigurationUtils() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idCard);
        dest.writeString(this.idCardName);
        dest.writeString(this.userId);
        dest.writeString(this.verifyscene);
        dest.writeString(this.bankName);
        dest.writeString(this.payChannelName);
        dest.writeString(this.uploadFileForLiveness);
        dest.writeString(this.mobile);
        dest.writeString(this.noticeContent);
        dest.writeString(this.pageTitle);
        dest.writeString(this.noticeTitle);
        dest.writeByte(this.isNeedSms ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isZjSendSms ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needIdCard ? (byte) 1 : (byte) 0);
        dest.writeString(this.payChannelNo);
        dest.writeString(this.businessType);
        dest.writeString(this.dynamicType);
        dest.writeString(this.partValue);
        dest.writeString(this.name);
        dest.writeString(this.certificateCodeAll);
        dest.writeString(this.account);
        dest.writeInt(this.failures);
        dest.writeString(this.tencentfaceOrder);
    }

    protected ConfigurationUtils(Parcel in) {
        this.idCard = in.readString();
        this.idCardName = in.readString();
        this.userId = in.readString();
        this.verifyscene = in.readString();
        this.bankName = in.readString();
        this.payChannelName = in.readString();
        this.uploadFileForLiveness = in.readString();
        this.mobile = in.readString();
        this.noticeContent = in.readString();
        this.pageTitle = in.readString();
        this.noticeTitle = in.readString();
        this.isNeedSms = in.readByte() != 0;
        this.isZjSendSms = in.readByte() != 0;
        this.needIdCard = in.readByte() != 0;
        this.payChannelNo = in.readString();
        this.businessType = in.readString();
        this.dynamicType = in.readString();
        this.partValue = in.readString();
        this.name = in.readString();
        this.certificateCodeAll = in.readString();
        this.account = in.readString();
        this.failures = in.readInt();
        this.tencentfaceOrder = in.readString();
    }

    public static final Creator<ConfigurationUtils> CREATOR = new Creator<ConfigurationUtils>() {
        @Override
        public ConfigurationUtils createFromParcel(Parcel source) {
            return new ConfigurationUtils(source);
        }

        @Override
        public ConfigurationUtils[] newArray(int size) {
            return new ConfigurationUtils[size];
        }
    };
}
