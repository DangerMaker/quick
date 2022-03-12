package com.hundsun.zjfae.activity.mine.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class FaceConfigurationUtils implements Parcelable {

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
    private String payChannelNo;//支付渠道

    private boolean isDeleteBank = true;//是否是解绑银行卡，默认是，false是申请换卡


    //unbindBankCardUpload解绑卡
    //changeBankCardUpload换卡申请
    private String dynamicType = "";


    /**
     * highNetWorth = 合格投资者材料上传
     * unbindCard = 解绑
     * changeCard = 换卡
     * account = 上传头像
     * */
    private String partValue = "";



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

    public String getPayChannelNo() {
        return payChannelNo;
    }

    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }

    public boolean isDeleteBank() {
        return isDeleteBank;
    }

    public void setDeleteBank(boolean deleteBank) {
        isDeleteBank = deleteBank;
    }

    public String getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(String dynamicType) {
        this.dynamicType = dynamicType;
    }


    public String getPartValue() {

        //解绑卡
        if (isDeleteBank()){

            return "unbindCard";
        }
        else {
            //换卡
            return "changeCard";
        }

    }

    public FaceConfigurationUtils() {
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
        dest.writeString(this.payChannelNo);
        dest.writeByte(this.isDeleteBank ? (byte) 1 : (byte) 0);
        dest.writeString(this.dynamicType);
        dest.writeString(this.partValue);
    }

    protected FaceConfigurationUtils(Parcel in) {
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
        this.payChannelNo = in.readString();
        this.isDeleteBank = in.readByte() != 0;
        this.dynamicType = in.readString();
        this.partValue = in.readString();
    }

    public static final Creator<FaceConfigurationUtils> CREATOR = new Creator<FaceConfigurationUtils>() {
        @Override
        public FaceConfigurationUtils createFromParcel(Parcel source) {
            return new FaceConfigurationUtils(source);
        }

        @Override
        public FaceConfigurationUtils[] newArray(int size) {
            return new FaceConfigurationUtils[size];
        }
    };
}
