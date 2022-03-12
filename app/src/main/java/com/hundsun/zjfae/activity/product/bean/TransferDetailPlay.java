package com.hundsun.zjfae.activity.product.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ProjectName:
 * @Package: com.hundsun.zjfae.activity.product.bean
 * @ClassName: TransferDetailPlay
 * @Description: 转让购买
 * @Author: moran
 * @CreateDate: 2019/6/10 13:46
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/6/10 13:46
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class TransferDetailPlay implements Parcelable {


    //产品代码
    private String productCode = "";

    //下次确权日
    private String nextPayDate = "";

    //可买人数
    private String canBuyNum = "";

    //已存续天数
    private String holdDayNum = "";

    //购买金额
    private String delegateNum = "";

    //参考收益率
    private String targetRate = "";

    //剩余天数
    private String leftDays = "";

    //委托编号
    private String delegationCode = "";

    //最低转让份额
    private String leastTranAmount = "";

    //实际收益率
    private String actualRate = "";

    //是否须一次全额购买
    private String ifAllBuy = "";

    //是否是自己挂单
    private boolean myEntry;

    //支付金额---本金支付，只作用于字段上送，不参与任何金额计算
    private String playAmount = "";


    /**
     * 产品系列编号
     */
    private String serialNoStr = "";

    //产品名字
    private String productName = "";

    //预期年化收益率
    private String expectedMaxAnnualRate = "";

    /**
     * 支付类型 1混合支付，0 纯银行卡支付
     */
    private String playType = "1";

    //卡券信息
    private PlayBaoInfo playBaoInfo;

    //网易盾
    private String token = "";

    //图形验证码
    private String authCode = "";

    //支付密码
    private String playPassWord = "";

    //计算本金
    private String totalAmount = "";

    //理财期限
    private String deadline = "";

    //买家最小购买单位
    private String buyerSmallestAmount = "";

    //发行剩余量
    private String buyRemainAmount = "";


    //可用余额
    private String balanceY = "";

    //余额支付充值金额
    private String inAmountWithBalance = "";

    //短信验证码---为什么不是smscode？
    private String checkCode = "";

    //卡券集合
    private List<CardVoucherBean> cardVoucherList;

    //红包集合
    private List<RadEnvelopeBean> radEnvelopeList;


    //用户手机号
    private String mobile = "";

    //是否整体转让字段值改了下：1-是（整体）0-否（拆分）
    private String isWholeTransfer = "";


    //委托编号
    private String delegationId = "";
    
    //卡券Id
    private String quanDetailsId="";


    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getNextPayDate() {
        return nextPayDate;
    }

    public void setNextPayDate(String nextPayDate) {
        this.nextPayDate = nextPayDate;
    }

    public String getCanBuyNum() {
        return canBuyNum;
    }

    public void setCanBuyNum(String canBuyNum) {
        this.canBuyNum = canBuyNum;
    }

    public String getHoldDayNum() {
        return holdDayNum;
    }

    public void setHoldDayNum(String holdDayNum) {
        this.holdDayNum = holdDayNum;
    }

    public String getDelegateNum() {
        return delegateNum;
    }

    public void setDelegateNum(String delegateNum) {
        this.delegateNum = delegateNum;
    }

    public String getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(String targetRate) {
        this.targetRate = targetRate;
    }

    public String getLeftDays() {
        return leftDays;
    }

    public void setLeftDays(String leftDays) {
        this.leftDays = leftDays;
    }

    public String getDelegationCode() {
        return delegationCode;
    }

    public void setDelegationCode(String delegationCode) {
        this.delegationCode = delegationCode;
    }

    public String getLeastTranAmount() {
        return leastTranAmount;
    }

    public void setLeastTranAmount(String leastTranAmount) {
        this.leastTranAmount = leastTranAmount;
    }

    public String getActualRate() {
        return actualRate;
    }

    public void setActualRate(String actualRate) {
        this.actualRate = actualRate;
    }

    public String getIfAllBuy() {
        return ifAllBuy;
    }

    public void setIfAllBuy(String ifAllBuy) {
        this.ifAllBuy = ifAllBuy;
    }

    public boolean isMyEntry() {
        return myEntry;
    }

    public void setMyEntry(boolean myEntry) {
        this.myEntry = myEntry;
    }

    public String getPlayAmount() {

        return formatMoney(playAmount);
    }


    public void setPlayAmount(String playAmount) {
        this.playAmount = playAmount;
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

    public String getExpectedMaxAnnualRate() {
        return expectedMaxAnnualRate;
    }

    public void setExpectedMaxAnnualRate(String expectedMaxAnnualRate) {
        this.expectedMaxAnnualRate = expectedMaxAnnualRate;
    }

    public String getPlayType() {
        return playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public PlayBaoInfo getPlayBaoInfo() {
        return playBaoInfo;
    }

    public void setPlayBaoInfo(PlayBaoInfo playBaoInfo) {
        this.playBaoInfo = playBaoInfo;
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

    public String getPlayPassWord() {
        return playPassWord;
    }

    public void setPlayPassWord(String playPassWord) {
        this.playPassWord = playPassWord;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getBalanceY() {
        return balanceY;
    }

    public void setBalanceY(String balanceY) {
        this.balanceY = balanceY;
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


    public String getMobile() {
        if (mobile == null) {

            return "";
        }
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsWholeTransfer() {
        return isWholeTransfer;
    }

    public void setIsWholeTransfer(String isWholeTransfer) {
        this.isWholeTransfer = isWholeTransfer;
    }

    public String getDelegationId() {
        return delegationId;
    }

    public void setDelegationId(String delegationId) {
        this.delegationId = delegationId;
    }

    public TransferDetailPlay() {
    }

    //只保留数字
    public static String formatMoney(String value) {

        return value.replaceAll(",", "").trim();
        // return Pattern.compile("[^0-9]").matcher(value).replaceAll("").trim();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCode);
        dest.writeString(this.nextPayDate);
        dest.writeString(this.canBuyNum);
        dest.writeString(this.holdDayNum);
        dest.writeString(this.delegateNum);
        dest.writeString(this.targetRate);
        dest.writeString(this.leftDays);
        dest.writeString(this.delegationCode);
        dest.writeString(this.leastTranAmount);
        dest.writeString(this.actualRate);
        dest.writeString(this.ifAllBuy);
        dest.writeByte(this.myEntry ? (byte) 1 : (byte) 0);
        dest.writeString(this.playAmount);
        dest.writeString(this.serialNoStr);
        dest.writeString(this.productName);
        dest.writeString(this.expectedMaxAnnualRate);
        dest.writeString(this.playType);
        dest.writeParcelable(this.playBaoInfo, flags);
        dest.writeString(this.token);
        dest.writeString(this.authCode);
        dest.writeString(this.playPassWord);
        dest.writeString(this.totalAmount);
        dest.writeString(this.deadline);
        dest.writeString(this.buyerSmallestAmount);
        dest.writeString(this.buyRemainAmount);
        dest.writeString(this.balanceY);
        dest.writeString(this.inAmountWithBalance);
        dest.writeString(this.checkCode);
        dest.writeTypedList(this.cardVoucherList);
        dest.writeTypedList(this.radEnvelopeList);
        dest.writeString(this.mobile);
        dest.writeString(this.isWholeTransfer);
        dest.writeString(this.delegationId);
    }

    protected TransferDetailPlay(Parcel in) {
        this.productCode = in.readString();
        this.nextPayDate = in.readString();
        this.canBuyNum = in.readString();
        this.holdDayNum = in.readString();
        this.delegateNum = in.readString();
        this.targetRate = in.readString();
        this.leftDays = in.readString();
        this.delegationCode = in.readString();
        this.leastTranAmount = in.readString();
        this.actualRate = in.readString();
        this.ifAllBuy = in.readString();
        this.myEntry = in.readByte() != 0;
        this.playAmount = in.readString();
        this.serialNoStr = in.readString();
        this.productName = in.readString();
        this.expectedMaxAnnualRate = in.readString();
        this.playType = in.readString();
        this.playBaoInfo = in.readParcelable(PlayBaoInfo.class.getClassLoader());
        this.token = in.readString();
        this.authCode = in.readString();
        this.playPassWord = in.readString();
        this.totalAmount = in.readString();
        this.deadline = in.readString();
        this.buyerSmallestAmount = in.readString();
        this.buyRemainAmount = in.readString();
        this.balanceY = in.readString();
        this.inAmountWithBalance = in.readString();
        this.checkCode = in.readString();
        this.cardVoucherList = in.createTypedArrayList(CardVoucherBean.CREATOR);
        this.radEnvelopeList = in.createTypedArrayList(RadEnvelopeBean.CREATOR);
        this.mobile = in.readString();
        this.isWholeTransfer = in.readString();
        this.delegationId = in.readString();
    }

    public static final Creator<TransferDetailPlay> CREATOR = new Creator<TransferDetailPlay>() {
        @Override
        public TransferDetailPlay createFromParcel(Parcel source) {
            return new TransferDetailPlay(source);
        }

        @Override
        public TransferDetailPlay[] newArray(int size) {
            return new TransferDetailPlay[size];
        }
    };
}
