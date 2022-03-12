package com.hundsun.zjfae.activity.productreserve.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.hundsun.zjfae.activity.product.bean.CardVoucherBean;
import com.hundsun.zjfae.activity.product.bean.PlayBaoInfo;
import com.hundsun.zjfae.activity.product.bean.RadEnvelopeBean;

import java.util.List;

/**
 * @Description:预约购买支付bean
 * @Author: zhoujianyu
 * @Time: 2019/3/4 17:03
 */
public class ReserveProductPlay implements Parcelable {


    private PlayBaoInfo playBaoInfo;//卡券信息

    //默认为12
    private String channelNo = "12";


    //交易密码，加密处理
    private String playPassWord="";

    //支付类型
    private String payType = "subscribePay";

    //产品编号
    private String productCode="";

    //预期年化收益率
    private String expectedMaxAnnualRate="";

    private String serialNoStr="";

    //产品名称
    private String productName="";

    //理财期限
    private String deadline="";

    //买家最小购买单位
    private String buyerSmallestAmount="";

    //发行剩余量
    private String buyRemainAmount="";

    //购买金额
    private String playAmount="";


    //卡券集合
    private List<CardVoucherBean> cardVoucherList;

    //红包集合
    private List<RadEnvelopeBean> radEnvelopeList;

    //卡券Id
    private String quanDetailsId="";


    //可用余额
    private String balanceY="";
    //计算本金
    private String totalAmount="";
    //余额支付充值金额
    private String inAmountWithBalance="";

    //短信验证码---为什么不是smscode？
    private String checkCode = "";

    //网易盾
    private String token="";

    //图形验证码
    private String authCode="";
    //支付类型 1混合支付，0 纯银行卡支付
    private String playType = "1";

    private String checkCodeSerialNo="";

    public ReserveProductPlay() {
    }


    protected ReserveProductPlay(Parcel in) {
        playBaoInfo = in.readParcelable(PlayBaoInfo.class.getClassLoader());
        channelNo = in.readString();
        playPassWord = in.readString();
        payType = in.readString();
        productCode = in.readString();
        expectedMaxAnnualRate = in.readString();
        serialNoStr = in.readString();
        productName = in.readString();
        deadline = in.readString();
        buyerSmallestAmount = in.readString();
        buyRemainAmount = in.readString();
        playAmount = in.readString();
        cardVoucherList = in.createTypedArrayList(CardVoucherBean.CREATOR);
        radEnvelopeList = in.createTypedArrayList(RadEnvelopeBean.CREATOR);
        quanDetailsId = in.readString();
        balanceY = in.readString();
        totalAmount = in.readString();
        inAmountWithBalance = in.readString();
        checkCode = in.readString();
        token = in.readString();
        authCode = in.readString();
        playType = in.readString();
        checkCodeSerialNo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(playBaoInfo, flags);
        dest.writeString(channelNo);
        dest.writeString(playPassWord);
        dest.writeString(payType);
        dest.writeString(productCode);
        dest.writeString(expectedMaxAnnualRate);
        dest.writeString(serialNoStr);
        dest.writeString(productName);
        dest.writeString(deadline);
        dest.writeString(buyerSmallestAmount);
        dest.writeString(buyRemainAmount);
        dest.writeString(playAmount);
        dest.writeTypedList(cardVoucherList);
        dest.writeTypedList(radEnvelopeList);
        dest.writeString(quanDetailsId);
        dest.writeString(balanceY);
        dest.writeString(totalAmount);
        dest.writeString(inAmountWithBalance);
        dest.writeString(checkCode);
        dest.writeString(token);
        dest.writeString(authCode);
        dest.writeString(playType);
        dest.writeString(checkCodeSerialNo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReserveProductPlay> CREATOR = new Creator<ReserveProductPlay>() {
        @Override
        public ReserveProductPlay createFromParcel(Parcel in) {
            return new ReserveProductPlay(in);
        }

        @Override
        public ReserveProductPlay[] newArray(int size) {
            return new ReserveProductPlay[size];
        }
    };

    public PlayBaoInfo getPlayBaoInfo() {
        return playBaoInfo;
    }

    public void setPlayBaoInfo(PlayBaoInfo playBaoInfo) {
        this.playBaoInfo = playBaoInfo;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getPlayPassWord() {
        return playPassWord;
    }

    public void setPlayPassWord(String playPassWord) {
        this.playPassWord = playPassWord;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getExpectedMaxAnnualRate() {
        return expectedMaxAnnualRate;
    }

    public void setExpectedMaxAnnualRate(String expectedMaxAnnualRate) {
        this.expectedMaxAnnualRate = expectedMaxAnnualRate;
    }

    public String getSerialNoStr() {
        return serialNoStr;
    }

    public void setSerialNoStr(String serialNoStr) {
        this.serialNoStr = serialNoStr;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getBuyerSmallestAmount() {
        return buyerSmallestAmount;
    }

    public void setBuyerSmallestAmount(String buyerSmallestAmount) {
        this.buyerSmallestAmount = buyerSmallestAmount;
    }

    public String getBuyRemainAmount() {
        return buyRemainAmount;
    }

    public void setBuyRemainAmount(String buyRemainAmount) {
        this.buyRemainAmount = buyRemainAmount;
    }

    public String getPlayAmount() {
        return playAmount;
    }

    public void setPlayAmount(String playAmount) {
        this.playAmount = playAmount;
    }

    public List<CardVoucherBean> getCardVoucherList() {
        return cardVoucherList;
    }

    public void setCardVoucherList(List<CardVoucherBean> cardVoucherList) {
        this.cardVoucherList = cardVoucherList;
    }

    public List<RadEnvelopeBean> getRadEnvelopeList() {
        return radEnvelopeList;
    }

    public void setRadEnvelopeList(List<RadEnvelopeBean> radEnvelopeList) {
        this.radEnvelopeList = radEnvelopeList;
    }

    public String getQuanDetailsId() {
        return quanDetailsId;
    }

    public void setQuanDetailsId(String quanDetailsId) {
        this.quanDetailsId = quanDetailsId;
    }

    public String getBalanceY() {
        return balanceY;
    }

    public void setBalanceY(String balanceY) {
        this.balanceY = balanceY;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInAmountWithBalance() {
        return inAmountWithBalance;
    }

    public void setInAmountWithBalance(String inAmountWithBalance) {
        this.inAmountWithBalance = inAmountWithBalance;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getCheckCodeSerialNo() {
        return checkCodeSerialNo;
    }

    public void setCheckCodeSerialNo(String checkCodeSerialNo) {
        this.checkCodeSerialNo = checkCodeSerialNo;
    }

    public static Creator<ReserveProductPlay> getCREATOR() {
        return CREATOR;
    }
}
