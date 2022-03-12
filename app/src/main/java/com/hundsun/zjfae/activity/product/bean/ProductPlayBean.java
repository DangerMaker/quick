package com.hundsun.zjfae.activity.product.bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.hundsun.zjfae.common.utils.aes.EncDecUtil;

import java.util.List;

/**
 * 产品购买支付类
 * @author moran
 * **/
public class ProductPlayBean implements Parcelable {

        //卡券信息
        private PlayBaoInfo playBaoInfo;

        //默认为12
        private String channelNo = "12";


        /**
         * 交易密码，加密处理
         * */
        private String playPassWord="";

        /**
         * 支付类型
         * */
        private String payType = "subscribePay";

        //产品编号
        private String productCode="";

        //预期年化收益率
        private String expectedMaxAnnualRate="";

        /**
         * 产品系列编号
         * */
        private String serialNoStr="";

        //产品名称
        private String productName="";

        //理财期限
        private String deadline="";

        //买家最小购买单位
        private String buyerSmallestAmount="";

        //发行剩余量
        private String buyRemainAmount="";

        //购买金额---字段上送
        private String playAmount="";


        //购买总金额
        private String totalPayAmount = "0.00";


        //卡券集合
        private List<CardVoucherBean> cardVoucherList;

        //红包集合
        private List<RadEnvelopeBean> radEnvelopeList;

        //卡券Id
        private String quanDetailsId="";

        //卡券数量
        private String voucherSize;

        //红包数量
        private String cardSize;



        //可用余额
        private String balanceY="0.00";

        /**
         * 委托编号
         * */
        private String delegationCode;


        /**
         * 是否整体转让字段值改了下：1-是（整体）0-否（拆分）
         * */
        private String isWholeTransfer = "";


        /**
         * 付息方式
         * */
        private String payStyle = "";


        /**
         * 金额锁死-是否全额购买
         * */
        private String ifAllBuy = "";

        /**
         * 是否可转让
         * */
        private String isTransfer;

        //计算本金
        private String totalAmount="";

        //余额支付充值金额
        private String inAmountWithBalance="";


        //支付类型 1混合支付，0 纯银行卡支付
        private String playType = "1";

        private String checkCodeSerialNo="";
        private String checkCode="";

        public String getCheckCode() {
            return checkCode;
        }

        public void setCheckCode(String checkCode) {
            this.checkCode = checkCode;
        }

        public void setPayType(String payType) {
            this.payType = payType;
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
            this.playPassWord = EncDecUtil.AESEncrypt(playPassWord);
        }


        public String getPayType() {
            return payType;
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

        public void setPlayAmount(String playAmount) {
            this.playAmount = playAmount;
            setTotalPayAmount(playAmount);
        }

        public String getPlayAmount() {
            return playAmount;
        }

        public void setTotalPayAmount(String totalPayAmount) {
            this.totalPayAmount = totalPayAmount;
        }

        public String getTotalPayAmount() {
            return totalPayAmount;
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


        public String getVoucherSize() {
            return voucherSize;
        }

        public void setVoucherSize(String voucherSize) {
            this.voucherSize = voucherSize;
        }

        public String getCardSize() {
            return cardSize;
        }

        public void setCardSize(String cardSize) {
            this.cardSize = cardSize;
        }


        public String getDelegationCode() {
            return delegationCode;
        }

        public void setDelegationCode(String delegationCode) {
            this.delegationCode = delegationCode;
        }

        public String getIsWholeTransfer() {
            return isWholeTransfer;
        }

        public void setIsWholeTransfer(String isWholeTransfer) {
            this.isWholeTransfer = isWholeTransfer;
        }

        public String getPayStyle() {
            return payStyle;
        }

        public void setPayStyle(String payStyle) {
            this.payStyle = payStyle;
        }

        public String getIfAllBuy() {
            return ifAllBuy;
        }

        public void setIfAllBuy(String ifAllBuy) {
            this.ifAllBuy = ifAllBuy;
        }

        public String getIsTransfer() {
            return isTransfer;
        }

        public void setIsTransfer(String isTransfer) {
            this.isTransfer = isTransfer;
        }

        public ProductPlayBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.playBaoInfo, flags);
            dest.writeString(this.channelNo);
            dest.writeString(this.playPassWord);
            dest.writeString(this.payType);
            dest.writeString(this.productCode);
            dest.writeString(this.expectedMaxAnnualRate);
            dest.writeString(this.serialNoStr);
            dest.writeString(this.productName);
            dest.writeString(this.deadline);
            dest.writeString(this.buyerSmallestAmount);
            dest.writeString(this.buyRemainAmount);
            dest.writeString(this.playAmount);
            dest.writeString(this.totalPayAmount);
            dest.writeTypedList(this.cardVoucherList);
            dest.writeTypedList(this.radEnvelopeList);
            dest.writeString(this.quanDetailsId);
            dest.writeString(this.voucherSize);
            dest.writeString(this.cardSize);
            dest.writeString(this.balanceY);
            dest.writeString(this.delegationCode);
            dest.writeString(this.isWholeTransfer);
            dest.writeString(this.payStyle);
            dest.writeString(this.ifAllBuy);
            dest.writeString(this.isTransfer);
            dest.writeString(this.totalAmount);
            dest.writeString(this.inAmountWithBalance);
            dest.writeString(this.playType);
            dest.writeString(this.checkCodeSerialNo);
            dest.writeString(this.checkCode);
        }

        protected ProductPlayBean(Parcel in) {
            this.playBaoInfo = in.readParcelable(PlayBaoInfo.class.getClassLoader());
            this.channelNo = in.readString();
            this.playPassWord = in.readString();
            this.payType = in.readString();
            this.productCode = in.readString();
            this.expectedMaxAnnualRate = in.readString();
            this.serialNoStr = in.readString();
            this.productName = in.readString();
            this.deadline = in.readString();
            this.buyerSmallestAmount = in.readString();
            this.buyRemainAmount = in.readString();
            this.playAmount = in.readString();
            this.totalPayAmount = in.readString();
            this.cardVoucherList = in.createTypedArrayList(CardVoucherBean.CREATOR);
            this.radEnvelopeList = in.createTypedArrayList(RadEnvelopeBean.CREATOR);
            this.quanDetailsId = in.readString();
            this.voucherSize = in.readString();
            this.cardSize = in.readString();
            this.balanceY = in.readString();
            this.delegationCode = in.readString();
            this.isWholeTransfer = in.readString();
            this.payStyle = in.readString();
            this.ifAllBuy = in.readString();
            this.isTransfer = in.readString();
            this.totalAmount = in.readString();
            this.inAmountWithBalance = in.readString();
            this.playType = in.readString();
            this.checkCodeSerialNo = in.readString();
            this.checkCode = in.readString();
        }

        public static final Creator<ProductPlayBean> CREATOR = new Creator<ProductPlayBean>() {
            @Override
            public ProductPlayBean createFromParcel(Parcel source) {
                return new ProductPlayBean(source);
            }

            @Override
            public ProductPlayBean[] newArray(int size) {
                return new ProductPlayBean[size];
            }
        };
}
