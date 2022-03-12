package com.zjfae.captcha.face;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName:
 * @Package:        com.zjfae.captcha.face
 * @ClassName:      TencentFaceStatus
 * @Description:     腾讯人脸识别回调
 * @Author:         moran
 * @CreateDate:     2019/7/16 17:22
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/16 17:22
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class TencentFaceStatus implements Parcelable {

    /**
     * 人脸核身是否成功
     * */
    private boolean isSuccess;

    /**
     * 签名,供 App 校验人脸核身结果的安全性
     * */
    private String sign;

    /**
     * 活体检测分数,
     * */
    private String liveRate;

    /**
     * 人脸比对分数,“仅活体检测” 类型不提供此分数
     * */
    private String similarity;
    /**
    * 用户人脸核身图片,经过 Base64 编码后的用户人脸核身图片，仅用户成功通过验证时返回
    * */
    private String userImageString;


    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLiveRate() {
        return liveRate;
    }

    public void setLiveRate(String liveRate) {
        this.liveRate = liveRate;
    }

    public String getSimilarity() {
        return similarity;
    }

    public void setSimilarity(String similarity) {
        this.similarity = similarity;
    }

    public String getUserImageString() {
        return userImageString;
    }

    public void setUserImageString(String userImageString) {
        this.userImageString = userImageString;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isSuccess ? (byte) 1 : (byte) 0);
        dest.writeString(this.sign);
        dest.writeString(this.liveRate);
        dest.writeString(this.similarity);
        dest.writeString(this.userImageString);
    }

    public TencentFaceStatus() {
    }

    protected TencentFaceStatus(Parcel in) {
        this.isSuccess = in.readByte() != 0;
        this.sign = in.readString();
        this.liveRate = in.readString();
        this.similarity = in.readString();
        this.userImageString = in.readString();
    }

    public static final Creator<TencentFaceStatus> CREATOR = new Creator<TencentFaceStatus>() {
        @Override
        public TencentFaceStatus createFromParcel(Parcel source) {
            return new TencentFaceStatus(source);
        }

        @Override
        public TencentFaceStatus[] newArray(int size) {
            return new TencentFaceStatus[size];
        }
    };
}
