package com.zjfae.captcha.face;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName:
 * @Package:        com.zjfae.captcha.face
 * @ClassName:      IDCardResult
 * @Description:     身份证识别结果
 * @Author:         moran
 * @CreateDate:     2019/7/19 14:29
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/19 14:29
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
public class IDCardResult implements Parcelable {

    private static IDCardResult idCardResult = null;

    public static IDCardResult getInstance() {
        synchronized (IDCardResult.class){
            if (idCardResult == null){
                idCardResult = new IDCardResult();
            }
        }
        return idCardResult;
    }

    public void recycle(){
        idCardResult = null;
    }






    //拉起SDK的模式所对应的int 值，也就是startActivityForOcr 方法中WBOCRTYPEMODE type的枚举值value
    private int type;
    // 识别人像面返回的信息
    //身份证号码
    private String cardNum = "";
    //姓名
    private String name = "";
    //性别
    private String sex = "";
    //住址
    private String address = "";
    //民族
    private String nation = "";
    //出生年月日
    private String birth = "";
    // 人像面图片存放路径
    private String frontFullImageSrc = "";
    //人像面告警码
    private String frontWarning = "";
    //人像面告警码
    private String frontMultiWarning = "";


    //识别国徽面返回的信息
    //签发机关
    private String office = "";
    //有效期限
    private String validDate = "";
    //国徽面图片存放路径
    private String backFullImageSrc = "";
    //国徽面告警码
    private String backWarning = "";
    //国徽面告警码
    private String backMultiWarning = "";

    //每次网络请求都会返回的信息
    //签名
    private String sign = "";
    //订单号
    private  String orderNo = "";
    //识别的唯一标识
    private String ocrId = "";


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        if (cardNum != null && !cardNum.equals("")){
            this.cardNum = cardNum;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.equals("")){
            this.name = name;
        }
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        if (sex != null && !sex.equals("")){
            this.sex = sex;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null && !address.equals("")){
            this.address = address;
        }
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        if (nation != null && !nation.equals("")){
            this.nation = nation;
        }
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        if (birth != null && !birth.equals("")){
            this.birth = birth;
        }
    }

    public String getFrontFullImageSrc() {
        return frontFullImageSrc;
    }

    public void setFrontFullImageSrc(String frontFullImageSrc) {
        if (frontFullImageSrc != null && !frontFullImageSrc.equals("")){
            this.frontFullImageSrc = frontFullImageSrc;
        }
    }

    public String getFrontWarning() {
        return frontWarning;
    }

    public void setFrontWarning(String frontWarning) {
        if (frontWarning != null && !frontWarning.equals("")){
            this.frontWarning = frontWarning;
        }
    }
    public String getFrontMultiWarning() {
        return frontMultiWarning;
    }

    public void setFrontMultiWarning(String frontMultiWarning) {
        if (frontMultiWarning != null && !frontMultiWarning.equals("")){
            this.frontMultiWarning = frontMultiWarning;
        }
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        if (office != null && !office.equals("")){
            this.office = office;
        }
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        if (validDate != null && !validDate.equals("")){
            this.validDate = validDate;
        }
    }

    public String getBackFullImageSrc() {
        return backFullImageSrc;
    }

    public void setBackFullImageSrc(String backFullImageSrc) {
        if (backFullImageSrc != null && !backFullImageSrc.equals("")){
            this.backFullImageSrc = backFullImageSrc;
        }
    }

    public String getBackWarning() {
        return backWarning;
    }

    public void setBackWarning(String backWarning) {
        if (backWarning != null && !backWarning.equals("")){
            this.backWarning = backWarning;
        }
    }
    public String getBackMultiWarning() {
        return backMultiWarning;
    }

    public void setBackMultiWarning(String backMultiWarning) {
        if (backMultiWarning != null && !backMultiWarning.equals("")){
            this.backMultiWarning = backMultiWarning;
        }
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        if (sign != null && !sign.equals("")){
            this.sign = sign;
        }
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        if (orderNo != null && !orderNo.equals("")){
            this.orderNo = orderNo;
        }
    }

    public String getOcrId() {
        return ocrId;
    }

    public void setOcrId(String ocrId) {
        if (ocrId != null && !ocrId.equals("")){
            this.ocrId = ocrId;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.cardNum);
        dest.writeString(this.name);
        dest.writeString(this.sex);
        dest.writeString(this.address);
        dest.writeString(this.nation);
        dest.writeString(this.birth);
        dest.writeString(this.frontFullImageSrc);
        dest.writeString(this.frontWarning);
        dest.writeString(this.frontMultiWarning);
        dest.writeString(this.office);
        dest.writeString(this.validDate);
        dest.writeString(this.backFullImageSrc);
        dest.writeString(this.backWarning);
        dest.writeString(this.backMultiWarning);
        dest.writeString(this.sign);
        dest.writeString(this.orderNo);
        dest.writeString(this.ocrId);
    }

    private IDCardResult() {
    }

    protected IDCardResult(Parcel in) {
        this.type = in.readInt();
        this.cardNum = in.readString();
        this.name = in.readString();
        this.sex = in.readString();
        this.address = in.readString();
        this.nation = in.readString();
        this.birth = in.readString();
        this.frontFullImageSrc = in.readString();
        this.frontWarning = in.readString();
        this.frontMultiWarning = in.readString();
        this.office = in.readString();
        this.validDate = in.readString();
        this.backFullImageSrc = in.readString();
        this.backWarning = in.readString();
        this.backMultiWarning = in.readString();
        this.sign = in.readString();
        this.orderNo = in.readString();
        this.ocrId = in.readString();
    }

    public static final Creator<IDCardResult> CREATOR = new Creator<IDCardResult>() {
        @Override
        public IDCardResult createFromParcel(Parcel source) {
            return new IDCardResult(source);
        }

        @Override
        public IDCardResult[] newArray(int size) {
            return new IDCardResult[size];
        }
    };
}
