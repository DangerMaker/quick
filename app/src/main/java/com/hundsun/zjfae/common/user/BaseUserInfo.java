package com.hundsun.zjfae.common.user;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 *  @ProjectName:
 * @Package:        com.hundsun.zjfae.common.user
 * @ClassName:      BaseUserInfo
 * @Description:     用户详细信息
 * @Author:         moran
 * @CreateDate:     2019/8/12 9:24
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/8/12 9:24
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */

@Entity
public class BaseUserInfo implements Parcelable {

    @Id(assignable = true)
    long uid = 1;
    /**
     * 姓名,String,不校验,是
     * */
    String name = "";

    /**
     * 账户,String,不校验,是
     * */
    String account = "";

    /**
     * 最后一次登录时间,String,不校验,是
     * */
    String lastLoginTime = "";

    /**
     * 上次登录终端,String,不校验,是,
     * */
    String lastLoginTerminalName = "";

    /**
     * 资金账号,String,不校验,是
     * */
    String fundAccount = "";

    /**
     * 交易账户,String,不校验,是,
     * */
    String tradeAccount = "";

    /**
     * 安全等级,String,不校验,是,
     * */
    String safeLevel = "";

    /**
     * 是否绑定银行卡,String,不校验,是,true-是，false-否
     * */
    String isBondedCard = "";

    /**
     * 是否设置交易密码,String,不校验,是,true-是，false-否
     * */
    String isFundPasswordSet = "";

    /**
     * 是否做过风险测评,String,不校验,是,true-是，false-否
     * */
    String isRiskTest = "";

    /**
     * 风险测评是否过期,String,不校验,是,true-是，false-否
     * */
    String isRiskExpired = "";

    /**
     * 是否绑定手机号,String,不校验,是,true-是，false-否
     * */
    String isBondedMobile = "";

    /**
     * 手机号,String,不校验,是,
     * */
    String mobile = "";

    /**
     * 是否绑定邮箱,String,不校验,是,true-是，false-否
     * */
    String isBondedEmail = "";

    /**
     * 邮箱,String,不校验,是,
     * */
    String email = "";

    /**
     * 是否设置安保问题,String,不校验,是
     * */
    String isSecuritySet = "";

    /**
     * 证件号码,String,不校验,是,
     * */
    String certificateCode = "";

    /**
     * 证件类型,String,不校验,是,
     * */
    String certificateType = "";

    /**
     * 用户类型,String,不校验,是,
     * */
    String userType = "";

    /**
     * 风险测评等级,String,不校验,是
     * */
    String riskLevel = "";

    /**
     * 风险测评有效期,String,不校验,是,
     * */
    String riskExpiredDate = "";

    /**
     * 是否年过65,String,不校验,是,
     * */
    String isOlderThan65 = "";

    /**
     * 用户经纪商渠道,String,不校验,是,
     * */
    String marketingChannel = "";

    /**
     * 活动码,String,不校验,是,
     * */
    String actCode = "";

    /**
     * 用户经纪商编号,String,不校验,是,
     * */
    String brokerNo = "";

    /**
     * 推荐人账号,String,不校验,是,
     * */
    String recomandAccountName = "";

    /**
     * 首次绑卡时间,String,不校验,是,
     * */
    String firstBindCardTime = "";

    /**
     * 是否有购买记录0-否，1-是,String,不校验,是,
     * */
    String isProductBuy = "";

    /**
     * 是否有二级市场受让0-否，1-是,String,不校验,是,
     * */
    String isTransferBuy = "";

    /**
     * 注册时间,String,不校验,是,
     * */
    String registerTime = "";

    /**
     * 用户等级,String,不校验,是,
     * */
    String userGroup = "";

    /**
     * 是否新客,String,不校验,是,0-否 1-是
     * */
    String isNewCustomer = "";

    /**
     * 是否首次登录,String,不校验,是,true false
     * */
    String isFirstLogin = "";

    /**
     * 离风评到期日的天数,String,不校验,是,
     * */
    String riskAssessment = "";

    /**
     * 用户星级 String 不校验 是 4:5星级客户,5:4星级客户,6:3星级客户,7:2星级客户,8:1星级客户,9:0星级客户
     * */
    String cifsel = "";

    /**
     * 会员换卡审核状态 String 不校验 是 null：未提出过申请；-1：待审核；0审核不通过；1：审核通过
     * */
    String changeCardStatus = "";

    /**
     * 会员解卡审核状态	String 不校验 是 null：未提出过申请；-1：待审核；0审核不通过；2：审核通过
     * */
    String unbindCardStatus = "";

    /**
     * 会员换合格投资者审核状态 String 不校验 是 null：未提出过申请；-1：待审核；0审核不通过 3 审核通过
     * */
    String highNetWorthStatus = "";

    /**
     * 会员信息修改审核状态 String 不校验 是 null：未提出过申请；-1：待审核；0审核不通过；4：审核通过
     * */
    String userInfoStatus = "";

    /**
     * 合格投资者认定时间	String	不校验	是	(时分秒)
     * */
    String accreditedTime = "";

    /**
     * 成为合格投资者方式	String	不校验	是	(1-人工;2-自动)
     * */
    String accreditedFlag = "";

    /**
     * 合格投资者授权时间	String	不校验	是	(时分秒)
     * */
    String accreditedSqTime = "";

    /**
     * 是否合格投资者	String	不校验	是	(1-是;0-否)
     * */
    String isAccreditedInvestor = "";

    /**
     * 证件号码、非脱敏,String,不校验,是,
     * */
    String certificateCodeAll = "";

    /**
     * 用户编号,String,不校验,是
     * */
    String userId = "";

    /**
     * 是否允许自动升级：0-否 1-是 String 不校验 是
     * */
    String canAutoUpgrade = "";

    /**
     * 历史最高用户等级 String 不校验 是
     * */
    String maxUserGroup = "";

    /**
     * 是否人工标记的等级：0-否 1-是	String 不校验 是
     * */
    String isArtificialGrade = "";

    /**
     * 升级时间 String 不校验 是
     * */
    String gradeTime = "";

    /**
     * 历史最高值 String 不校验 是
     * */
    String maxValue = "";

    /**
     * 历史最高值时间 String	不校验	是	(时分秒)
     * */
    String maxValueTime = "";

    /**
     * 操作人id String 不校验	是	(1-人工;2-自动)
     * */
    String operId = "";

    /**
     * 操作人姓名	String	不校验	是
     * */
    String operName = "";

    /**
     * 合格投资者授权终端 String	不校验	是	(1-是;0-否)
     * */
    String accreditedChannel = "";

    /**
     * 达标时间,String,不校验,是,
     * */
    String accreditedDbTime = "";

    /**
     * 是否真正合格投资者,String,不校验,是
     * */
    String isRealInvestor= "";

    /**
     * 展示图标用户类型，是否合格投资|高净值用户等类型相拼接
     * */
    String iconsUserType = "";

    /**
     * 是否实名（0-未实名；1-实名
     * */
    String verifyName = "";

    /**
     * 用户会员等级
     * */
    String showLevel = "";

    /**
     * 是否合格投资者认定提醒标志(0-否;1-是),String,不校验,是,
     * */
    String accreditedTipsFlag = "";

    /**
     * 白名单
     * */
    String whiteGroup = "";

    /**
     * 身份证效验状态：1、已绑卡或曾绑卡但未上传身份证2、已上传身份证，但身份证已过期3、已上传身份证未填职业4、已上传身份证已填职业5、身份证已效验
     * */
    String certificateStatusType = "";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginTerminalName() {
        return lastLoginTerminalName;
    }

    public void setLastLoginTerminalName(String lastLoginTerminalName) {
        this.lastLoginTerminalName = lastLoginTerminalName;
    }

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getTradeAccount() {
        return tradeAccount;
    }

    public void setTradeAccount(String tradeAccount) {
        this.tradeAccount = tradeAccount;
    }

    public String getSafeLevel() {
        return safeLevel;
    }

    public void setSafeLevel(String safeLevel) {
        this.safeLevel = safeLevel;
    }

    public String getIsBondedCard() {
        return isBondedCard;
    }

    public void setIsBondedCard(String isBondedCard) {
        this.isBondedCard = isBondedCard;
    }

    public String getIsFundPasswordSet() {
        return isFundPasswordSet;
    }

    public void setIsFundPasswordSet(String isFundPasswordSet) {
        this.isFundPasswordSet = isFundPasswordSet;
    }

    public String getIsRiskTest() {
        return isRiskTest;
    }

    public void setIsRiskTest(String isRiskTest) {
        this.isRiskTest = isRiskTest;
    }

    public String getIsRiskExpired() {
        return isRiskExpired;
    }

    public void setIsRiskExpired(String isRiskExpired) {
        this.isRiskExpired = isRiskExpired;
    }

    public String getIsBondedMobile() {
        return isBondedMobile;
    }

    public void setIsBondedMobile(String isBondedMobile) {
        this.isBondedMobile = isBondedMobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsBondedEmail() {
        return isBondedEmail;
    }

    public void setIsBondedEmail(String isBondedEmail) {
        this.isBondedEmail = isBondedEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsSecuritySet() {
        return isSecuritySet;
    }

    public void setIsSecuritySet(String isSecuritySet) {
        this.isSecuritySet = isSecuritySet;
    }

    public String getCertificateCode() {
        return certificateCode;
    }

    public void setCertificateCode(String certificateCode) {
        this.certificateCode = certificateCode;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getRiskExpiredDate() {
        return riskExpiredDate;
    }

    public void setRiskExpiredDate(String riskExpiredDate) {
        this.riskExpiredDate = riskExpiredDate;
    }

    public String getIsOlderThan65() {
        return isOlderThan65;
    }

    public void setIsOlderThan65(String isOlderThan65) {
        this.isOlderThan65 = isOlderThan65;
    }

    public String getMarketingChannel() {
        return marketingChannel;
    }

    public void setMarketingChannel(String marketingChannel) {
        this.marketingChannel = marketingChannel;
    }

    public String getActCode() {
        return actCode;
    }

    public void setActCode(String actCode) {
        this.actCode = actCode;
    }

    public String getBrokerNo() {
        return brokerNo;
    }

    public void setBrokerNo(String brokerNo) {
        this.brokerNo = brokerNo;
    }

    public String getRecomandAccountName() {
        return recomandAccountName;
    }

    public void setRecomandAccountName(String recomandAccountName) {
        this.recomandAccountName = recomandAccountName;
    }

    public String getFirstBindCardTime() {
        return firstBindCardTime;
    }

    public void setFirstBindCardTime(String firstBindCardTime) {
        this.firstBindCardTime = firstBindCardTime;
    }

    public String getIsProductBuy() {
        return isProductBuy;
    }

    public void setIsProductBuy(String isProductBuy) {
        this.isProductBuy = isProductBuy;
    }

    public String getIsTransferBuy() {
        return isTransferBuy;
    }

    public void setIsTransferBuy(String isTransferBuy) {
        this.isTransferBuy = isTransferBuy;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public String getIsNewCustomer() {
        return isNewCustomer;
    }

    public void setIsNewCustomer(String isNewCustomer) {
        this.isNewCustomer = isNewCustomer;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getRiskAssessment() {
        return riskAssessment;
    }

    public void setRiskAssessment(String riskAssessment) {
        this.riskAssessment = riskAssessment;
    }

    public String getCifsel() {
        return cifsel;
    }

    public void setCifsel(String cifsel) {
        this.cifsel = cifsel;
    }

    public String getChangeCardStatus() {
        return changeCardStatus;
    }

    public void setChangeCardStatus(String changeCardStatus) {
        this.changeCardStatus = changeCardStatus;
    }

    public String getUnbindCardStatus() {
        return unbindCardStatus;
    }

    public void setUnbindCardStatus(String unbindCardStatus) {
        this.unbindCardStatus = unbindCardStatus;
    }

    public String getHighNetWorthStatus() {
        return highNetWorthStatus;
    }

    public void setHighNetWorthStatus(String highNetWorthStatus) {
        this.highNetWorthStatus = highNetWorthStatus;
    }

    public String getUserInfoStatus() {
        return userInfoStatus;
    }

    public void setUserInfoStatus(String userInfoStatus) {
        this.userInfoStatus = userInfoStatus;
    }

    public String getAccreditedTime() {
        return accreditedTime;
    }

    public void setAccreditedTime(String accreditedTime) {
        this.accreditedTime = accreditedTime;
    }

    public String getAccreditedFlag() {
        return accreditedFlag;
    }

    public void setAccreditedFlag(String accreditedFlag) {
        this.accreditedFlag = accreditedFlag;
    }

    public String getAccreditedSqTime() {
        return accreditedSqTime;
    }

    public void setAccreditedSqTime(String accreditedSqTime) {
        this.accreditedSqTime = accreditedSqTime;
    }

    public String getIsAccreditedInvestor() {
        return isAccreditedInvestor;
    }

    public void setIsAccreditedInvestor(String isAccreditedInvestor) {
        this.isAccreditedInvestor = isAccreditedInvestor;
    }

    public String getCertificateCodeAll() {
        return certificateCodeAll;
    }

    public void setCertificateCodeAll(String certificateCodeAll) {
        this.certificateCodeAll = certificateCodeAll;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCanAutoUpgrade() {
        return canAutoUpgrade;
    }

    public void setCanAutoUpgrade(String canAutoUpgrade) {
        this.canAutoUpgrade = canAutoUpgrade;
    }

    public String getMaxUserGroup() {
        return maxUserGroup;
    }

    public void setMaxUserGroup(String maxUserGroup) {
        this.maxUserGroup = maxUserGroup;
    }

    public String getIsArtificialGrade() {
        return isArtificialGrade;
    }

    public void setIsArtificialGrade(String isArtificialGrade) {
        this.isArtificialGrade = isArtificialGrade;
    }

    public String getGradeTime() {
        return gradeTime;
    }

    public void setGradeTime(String gradeTime) {
        this.gradeTime = gradeTime;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMaxValueTime() {
        return maxValueTime;
    }

    public void setMaxValueTime(String maxValueTime) {
        this.maxValueTime = maxValueTime;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public String getAccreditedChannel() {
        return accreditedChannel;
    }

    public void setAccreditedChannel(String accreditedChannel) {
        this.accreditedChannel = accreditedChannel;
    }

    public String getAccreditedDbTime() {
        return accreditedDbTime;
    }

    public void setAccreditedDbTime(String accreditedDbTime) {
        this.accreditedDbTime = accreditedDbTime;
    }

    public String getIsRealInvestor() {
        return isRealInvestor;
    }

    public void setIsRealInvestor(String isRealInvestor) {
        this.isRealInvestor = isRealInvestor;
    }

    public String getIconsUserType() {
        return iconsUserType;
    }

    public void setIconsUserType(String iconsUserType) {
        this.iconsUserType = iconsUserType;
    }

    public String getVerifyName() {
        return verifyName;
    }

    public void setVerifyName(String verifyName) {
        this.verifyName = verifyName;
    }

    public String getShowLevel() {
        return showLevel;
    }

    public void setShowLevel(String showLevel) {
        this.showLevel = showLevel;
    }

    public String getAccreditedTipsFlag() {
        return accreditedTipsFlag;
    }

    public void setAccreditedTipsFlag(String accreditedTipsFlag) {
        this.accreditedTipsFlag = accreditedTipsFlag;
    }

    public String getWhiteGroup() {
        return whiteGroup;
    }

    public void setWhiteGroup(String whiteGroup) {
        this.whiteGroup = whiteGroup;
    }

    public String getCertificateStatusType() {
        return certificateStatusType;
    }

    public void setCertificateStatusType(String certificateStatusType) {
        this.certificateStatusType = certificateStatusType;
    }

    public static Creator<BaseUserInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.account);
        dest.writeString(this.lastLoginTime);
        dest.writeString(this.lastLoginTerminalName);
        dest.writeString(this.fundAccount);
        dest.writeString(this.tradeAccount);
        dest.writeString(this.safeLevel);
        dest.writeString(this.isBondedCard);
        dest.writeString(this.isFundPasswordSet);
        dest.writeString(this.isRiskTest);
        dest.writeString(this.isRiskExpired);
        dest.writeString(this.isBondedMobile);
        dest.writeString(this.mobile);
        dest.writeString(this.isBondedEmail);
        dest.writeString(this.email);
        dest.writeString(this.isSecuritySet);
        dest.writeString(this.certificateCode);
        dest.writeString(this.certificateType);
        dest.writeString(this.userType);
        dest.writeString(this.riskLevel);
        dest.writeString(this.riskExpiredDate);
        dest.writeString(this.isOlderThan65);
        dest.writeString(this.marketingChannel);
        dest.writeString(this.actCode);
        dest.writeString(this.brokerNo);
        dest.writeString(this.recomandAccountName);
        dest.writeString(this.firstBindCardTime);
        dest.writeString(this.isProductBuy);
        dest.writeString(this.isTransferBuy);
        dest.writeString(this.registerTime);
        dest.writeString(this.userGroup);
        dest.writeString(this.isNewCustomer);
        dest.writeString(this.isFirstLogin);
        dest.writeString(this.riskAssessment);
        dest.writeString(this.cifsel);
        dest.writeString(this.changeCardStatus);
        dest.writeString(this.unbindCardStatus);
        dest.writeString(this.highNetWorthStatus);
        dest.writeString(this.userInfoStatus);
        dest.writeString(this.accreditedTime);
        dest.writeString(this.accreditedFlag);
        dest.writeString(this.accreditedSqTime);
        dest.writeString(this.isAccreditedInvestor);
        dest.writeString(this.certificateCodeAll);
        dest.writeString(this.userId);
        dest.writeString(this.canAutoUpgrade);
        dest.writeString(this.maxUserGroup);
        dest.writeString(this.isArtificialGrade);
        dest.writeString(this.gradeTime);
        dest.writeString(this.maxValue);
        dest.writeString(this.maxValueTime);
        dest.writeString(this.operId);
        dest.writeString(this.operName);
        dest.writeString(this.accreditedChannel);
        dest.writeString(this.accreditedDbTime);
        dest.writeString(this.isRealInvestor);
        dest.writeString(this.iconsUserType);
        dest.writeString(this.verifyName);
        dest.writeString(this.showLevel);
        dest.writeString(this.accreditedTipsFlag);
        dest.writeString(this.whiteGroup);
        dest.writeString(this.certificateStatusType);
    }

    public BaseUserInfo() {
    }

    protected BaseUserInfo(Parcel in) {
        this.name = in.readString();
        this.account = in.readString();
        this.lastLoginTime = in.readString();
        this.lastLoginTerminalName = in.readString();
        this.fundAccount = in.readString();
        this.tradeAccount = in.readString();
        this.safeLevel = in.readString();
        this.isBondedCard = in.readString();
        this.isFundPasswordSet = in.readString();
        this.isRiskTest = in.readString();
        this.isRiskExpired = in.readString();
        this.isBondedMobile = in.readString();
        this.mobile = in.readString();
        this.isBondedEmail = in.readString();
        this.email = in.readString();
        this.isSecuritySet = in.readString();
        this.certificateCode = in.readString();
        this.certificateType = in.readString();
        this.userType = in.readString();
        this.riskLevel = in.readString();
        this.riskExpiredDate = in.readString();
        this.isOlderThan65 = in.readString();
        this.marketingChannel = in.readString();
        this.actCode = in.readString();
        this.brokerNo = in.readString();
        this.recomandAccountName = in.readString();
        this.firstBindCardTime = in.readString();
        this.isProductBuy = in.readString();
        this.isTransferBuy = in.readString();
        this.registerTime = in.readString();
        this.userGroup = in.readString();
        this.isNewCustomer = in.readString();
        this.isFirstLogin = in.readString();
        this.riskAssessment = in.readString();
        this.cifsel = in.readString();
        this.changeCardStatus = in.readString();
        this.unbindCardStatus = in.readString();
        this.highNetWorthStatus = in.readString();
        this.userInfoStatus = in.readString();
        this.accreditedTime = in.readString();
        this.accreditedFlag = in.readString();
        this.accreditedSqTime = in.readString();
        this.isAccreditedInvestor = in.readString();
        this.certificateCodeAll = in.readString();
        this.userId = in.readString();
        this.canAutoUpgrade = in.readString();
        this.maxUserGroup = in.readString();
        this.isArtificialGrade = in.readString();
        this.gradeTime = in.readString();
        this.maxValue = in.readString();
        this.maxValueTime = in.readString();
        this.operId = in.readString();
        this.operName = in.readString();
        this.accreditedChannel = in.readString();
        this.accreditedDbTime = in.readString();
        this.isRealInvestor = in.readString();
        this.iconsUserType = in.readString();
        this.verifyName = in.readString();
        this.showLevel = in.readString();
        this.accreditedTipsFlag = in.readString();
        this.whiteGroup = in.readString();
        this.certificateStatusType = in.readString();
    }

    public static final Creator<BaseUserInfo> CREATOR = new Creator<BaseUserInfo>() {
        @Override
        public BaseUserInfo createFromParcel(Parcel source) {
            return new BaseUserInfo(source);
        }

        @Override
        public BaseUserInfo[] newArray(int size) {
            return new BaseUserInfo[size];
        }
    };






}
