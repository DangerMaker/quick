package com.hundsun.zjfae.activity.product.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Package: com.hundsun.zjfae.activity.product.bean
 * @ClassName: AttachmentBean
 * @Description: 打开附件辅助类
 * @Author: moran
 * @CreateDate: 2020/7/31 16:28
 * @UpdateUser: 更新者：moran
 * @UpdateDate: 2020/7/31 16:28
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class AttachmentEntity implements Parcelable {

    private String title = "";

    private String openUrl = "";

    private boolean iSignature = false;

    //授权码
    private String authorizationCode = "";

    //附件id
    private String id = "";

    //重复提交码
    private String repeatCommitCheckCode = "";

    //短信流水序列号
    private String serialNo = "";

    private TransferDetailPlay playInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    public boolean isiSignature() {
        return iSignature;
    }

    public void setiSignature(boolean iSignature) {
        this.iSignature = iSignature;
    }

    public String getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepeatCommitCheckCode() {
        return repeatCommitCheckCode;
    }

    public void setRepeatCommitCheckCode(String repeatCommitCheckCode) {
        this.repeatCommitCheckCode = repeatCommitCheckCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public TransferDetailPlay getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(TransferDetailPlay playInfo) {
        this.playInfo = playInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.openUrl);
        dest.writeByte(this.iSignature ? (byte) 1 : (byte) 0);
        dest.writeString(this.authorizationCode);
        dest.writeString(this.id);
        dest.writeString(this.repeatCommitCheckCode);
        dest.writeString(this.serialNo);
        dest.writeParcelable(this.playInfo, flags);
    }

    public AttachmentEntity() {
    }

    protected AttachmentEntity(Parcel in) {
        this.title = in.readString();
        this.openUrl = in.readString();
        this.iSignature = in.readByte() != 0;
        this.authorizationCode = in.readString();
        this.id = in.readString();
        this.repeatCommitCheckCode = in.readString();
        this.serialNo = in.readString();
        this.playInfo = in.readParcelable(TransferDetailPlay.class.getClassLoader());
    }

    public static final Creator<AttachmentEntity> CREATOR = new Creator<AttachmentEntity>() {
        @Override
        public AttachmentEntity createFromParcel(Parcel source) {
            return new AttachmentEntity(source);
        }

        @Override
        public AttachmentEntity[] newArray(int size) {
            return new AttachmentEntity[size];
        }
    };
}
