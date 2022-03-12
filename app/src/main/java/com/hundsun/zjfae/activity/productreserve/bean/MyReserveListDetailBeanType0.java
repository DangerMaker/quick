package com.hundsun.zjfae.activity.productreserve.bean;

/**
 * @Description:我的预约列表详情实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/27 16:08
 */
public class MyReserveListDetailBeanType0 {


    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"Nm9uZmFiY3BWT0ZWWnplOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYG","appVersion":"1.6.49","ReqTime":"1538099379537","SMID":"WWIzcjEwLmQ3N2Y2NjU5N2U1YWE2SUE1eXV3OjAwMDAxMDcxMjYvYXBw7","isShare":"1","pbname":"PBIFE_prdquery_prdQueryProductDetails","realipzjs":"115.236.162.162","userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"taProductFinanceDetail":{"productCode":"LC18090063","productName":"W合格特约090502","productStatusStr":"购买期","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-09-11 10:30:12","manageStartDate":"2018-05-17","manageEndDate":"2018-12-06","tradeStartDate":"2018-05-23 06:00:00","tradeEndDate":"2018-11-30 21:00:00","currency":"人民币","isTransferStr":"可转让","riskLevel":"较高风险","buyerSmallestAmount":"2,000","buyTotalAmount":"100,000","buySmallestAmount":"0","buyRemainAmount":"1,000","serialNoStr":"PDS1363","subjectTypeName":"定向融资计划","prodSubType":"担保类","deadline":"203","expectedMaxAnnualRate":"7.20","actualAnnualRate":"0.072","productSubType":"A","isTransfer":"transfer","transferIsfloat":"float","transferIsfloatName":"浮动","transferFloat":"0.001","transferFloatBegin":"0","transferFloatEnd":"0.072","delayDays":"0","status":"accepted","unActualPriceIncreases":"1,000","unActualBuyUserLevel":"0","unActualPrice":"100","financeCode":"4564","transferFee":"0","leastHoldAmount":"2,000","issueCompany":"JY000007","issueCompanyName":"杭州卓成","opstBankCode":"广东省大多数的大幅度对法国大使馆2222","opstBankAccount":"1324567876654222","saleObject":"个人和机构","yearDay":"ACT/365","mostHoldAmount":"50000","mostHolderNum":"50","incomeType":"2","tradeLeastHoldDay":"5","leastTranAmount":"1000","delayCashRate":"0","tradeIncrease":"1,000","tradeObject":"双方","isBuyBack":"0","riskTest":"1","cashType":"1","imputType":"1","collectionDestination":"1","cashDestination":"1","specialAmount":"50000","specialCustomerNum":"10","payStyle":"利随本清","issueCreditLevel":"0000015006","directFinanceCreditLevel":"AA","issueBank":"LQ经过提供方法和法国风尚大典222韩国风格","financeCompany":"浙江商业银行","guaranteeWay":"1","delayDayRate":"0","holdersNum":"50","specialBuyAmount":"2000","specialIncreaseAmount":"1000","productStartDate":"2018-05-17 00:00:00","optionTriggerType":"1","optionType":"1","optionDateStart":"2016-11-25 00:00:00","optionDateEnd":"2017-12-28 00:00:00","delayDateStart":"2016-05-18 00:00:00","delayDateEnd":"2016-10-27 00:00:00","sfyzcbxs":"0","isOver":"0","tradeDept":"1006","transferObject":"0","productBrands":"123321","payFrequencyUnit":"0000004002","generalCustomerUsedNum":"1","specialCustomerUsedNum":"1","onlyNew":"0","process":"98.00","orderBuyAmount":"0","canBuyNum":"39人","comparison":"2","incomeTypeName":"固定","unActualTransferUserLevel":"0","countDownSec":"-2570979","accreditedBuyIs":"1","accreditedTransferIs":"1"}}}
     */

    private String fh;
    private EhBean eh;
    private BodyBean body;

    public String getFh() {
        return fh;
    }

    public void setFh(String fh) {
        this.fh = fh;
    }

    public EhBean getEh() {
        return eh;
    }

    public void setEh(EhBean eh) {
        this.eh = eh;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class EhBean {
        /**
         * p : and
         * MFID : Nm9uZmFiY3BWT0ZWWnplOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYG
         * appVersion : 1.6.49
         * ReqTime : 1538099379537
         * SMID : WWIzcjEwLmQ3N2Y2NjU5N2U1YWE2SUE1eXV3OjAwMDAxMDcxMjYvYXBw7
         * isShare : 1
         * pbname : PBIFE_prdquery_prdQueryProductDetails
         * realipzjs : 115.236.162.162
         * userid : 18012340000
         * platform : android
         */

        private String p;
        private String MFID;
        private String appVersion;
        private String ReqTime;
        private String SMID;
        private String isShare;
        private String pbname;
        private String realipzjs;
        private String userid;
        private String platform;

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getMFID() {
            return MFID;
        }

        public void setMFID(String MFID) {
            this.MFID = MFID;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getReqTime() {
            return ReqTime;
        }

        public void setReqTime(String ReqTime) {
            this.ReqTime = ReqTime;
        }

        public String getSMID() {
            return SMID;
        }

        public void setSMID(String SMID) {
            this.SMID = SMID;
        }

        public String getIsShare() {
            return isShare;
        }

        public void setIsShare(String isShare) {
            this.isShare = isShare;
        }

        public String getPbname() {
            return pbname;
        }

        public void setPbname(String pbname) {
            this.pbname = pbname;
        }

        public String getRealipzjs() {
            return realipzjs;
        }

        public void setRealipzjs(String realipzjs) {
            this.realipzjs = realipzjs;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }
    }

    public static class BodyBean {
        /**
         * returnCode : 0000
         * returnMsg : 操作通过
         * data : {"taProductFinanceDetail":{"productCode":"LC18090063","productName":"W合格特约090502","productStatusStr":"购买期","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-09-11 10:30:12","manageStartDate":"2018-05-17","manageEndDate":"2018-12-06","tradeStartDate":"2018-05-23 06:00:00","tradeEndDate":"2018-11-30 21:00:00","currency":"人民币","isTransferStr":"可转让","riskLevel":"较高风险","buyerSmallestAmount":"2,000","buyTotalAmount":"100,000","buySmallestAmount":"0","buyRemainAmount":"1,000","serialNoStr":"PDS1363","subjectTypeName":"定向融资计划","prodSubType":"担保类","deadline":"203","expectedMaxAnnualRate":"7.20","actualAnnualRate":"0.072","productSubType":"A","isTransfer":"transfer","transferIsfloat":"float","transferIsfloatName":"浮动","transferFloat":"0.001","transferFloatBegin":"0","transferFloatEnd":"0.072","delayDays":"0","status":"accepted","unActualPriceIncreases":"1,000","unActualBuyUserLevel":"0","unActualPrice":"100","financeCode":"4564","transferFee":"0","leastHoldAmount":"2,000","issueCompany":"JY000007","issueCompanyName":"杭州卓成","opstBankCode":"广东省大多数的大幅度对法国大使馆2222","opstBankAccount":"1324567876654222","saleObject":"个人和机构","yearDay":"ACT/365","mostHoldAmount":"50000","mostHolderNum":"50","incomeType":"2","tradeLeastHoldDay":"5","leastTranAmount":"1000","delayCashRate":"0","tradeIncrease":"1,000","tradeObject":"双方","isBuyBack":"0","riskTest":"1","cashType":"1","imputType":"1","collectionDestination":"1","cashDestination":"1","specialAmount":"50000","specialCustomerNum":"10","payStyle":"利随本清","issueCreditLevel":"0000015006","directFinanceCreditLevel":"AA","issueBank":"LQ经过提供方法和法国风尚大典222韩国风格","financeCompany":"浙江商业银行","guaranteeWay":"1","delayDayRate":"0","holdersNum":"50","specialBuyAmount":"2000","specialIncreaseAmount":"1000","productStartDate":"2018-05-17 00:00:00","optionTriggerType":"1","optionType":"1","optionDateStart":"2016-11-25 00:00:00","optionDateEnd":"2017-12-28 00:00:00","delayDateStart":"2016-05-18 00:00:00","delayDateEnd":"2016-10-27 00:00:00","sfyzcbxs":"0","isOver":"0","tradeDept":"1006","transferObject":"0","productBrands":"123321","payFrequencyUnit":"0000004002","generalCustomerUsedNum":"1","specialCustomerUsedNum":"1","onlyNew":"0","process":"98.00","orderBuyAmount":"0","canBuyNum":"39人","comparison":"2","incomeTypeName":"固定","unActualTransferUserLevel":"0","countDownSec":"-2570979","accreditedBuyIs":"1","accreditedTransferIs":"1"}}
         */

        private String returnCode;
        private String returnMsg;
        private DataBean data;

        public String getReturnCode() {
            return returnCode;
        }

        public void setReturnCode(String returnCode) {
            this.returnCode = returnCode;
        }

        public String getReturnMsg() {
            return returnMsg;
        }

        public void setReturnMsg(String returnMsg) {
            this.returnMsg = returnMsg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * taProductFinanceDetail : {"productCode":"LC18090063","productName":"W合格特约090502","productStatusStr":"购买期","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-09-11 10:30:12","manageStartDate":"2018-05-17","manageEndDate":"2018-12-06","tradeStartDate":"2018-05-23 06:00:00","tradeEndDate":"2018-11-30 21:00:00","currency":"人民币","isTransferStr":"可转让","riskLevel":"较高风险","buyerSmallestAmount":"2,000","buyTotalAmount":"100,000","buySmallestAmount":"0","buyRemainAmount":"1,000","serialNoStr":"PDS1363","subjectTypeName":"定向融资计划","prodSubType":"担保类","deadline":"203","expectedMaxAnnualRate":"7.20","actualAnnualRate":"0.072","productSubType":"A","isTransfer":"transfer","transferIsfloat":"float","transferIsfloatName":"浮动","transferFloat":"0.001","transferFloatBegin":"0","transferFloatEnd":"0.072","delayDays":"0","status":"accepted","unActualPriceIncreases":"1,000","unActualBuyUserLevel":"0","unActualPrice":"100","financeCode":"4564","transferFee":"0","leastHoldAmount":"2,000","issueCompany":"JY000007","issueCompanyName":"杭州卓成","opstBankCode":"广东省大多数的大幅度对法国大使馆2222","opstBankAccount":"1324567876654222","saleObject":"个人和机构","yearDay":"ACT/365","mostHoldAmount":"50000","mostHolderNum":"50","incomeType":"2","tradeLeastHoldDay":"5","leastTranAmount":"1000","delayCashRate":"0","tradeIncrease":"1,000","tradeObject":"双方","isBuyBack":"0","riskTest":"1","cashType":"1","imputType":"1","collectionDestination":"1","cashDestination":"1","specialAmount":"50000","specialCustomerNum":"10","payStyle":"利随本清","issueCreditLevel":"0000015006","directFinanceCreditLevel":"AA","issueBank":"LQ经过提供方法和法国风尚大典222韩国风格","financeCompany":"浙江商业银行","guaranteeWay":"1","delayDayRate":"0","holdersNum":"50","specialBuyAmount":"2000","specialIncreaseAmount":"1000","productStartDate":"2018-05-17 00:00:00","optionTriggerType":"1","optionType":"1","optionDateStart":"2016-11-25 00:00:00","optionDateEnd":"2017-12-28 00:00:00","delayDateStart":"2016-05-18 00:00:00","delayDateEnd":"2016-10-27 00:00:00","sfyzcbxs":"0","isOver":"0","tradeDept":"1006","transferObject":"0","productBrands":"123321","payFrequencyUnit":"0000004002","generalCustomerUsedNum":"1","specialCustomerUsedNum":"1","onlyNew":"0","process":"98.00","orderBuyAmount":"0","canBuyNum":"39人","comparison":"2","incomeTypeName":"固定","unActualTransferUserLevel":"0","countDownSec":"-2570979","accreditedBuyIs":"1","accreditedTransferIs":"1"}
             */

            private TaProductFinanceDetailBean taProductFinanceDetail;

            public TaProductFinanceDetailBean getTaProductFinanceDetail() {
                return taProductFinanceDetail;
            }

            public void setTaProductFinanceDetail(TaProductFinanceDetailBean taProductFinanceDetail) {
                this.taProductFinanceDetail = taProductFinanceDetail;
            }

            public static class TaProductFinanceDetailBean {
                /**
                 * productCode : LC18090063
                 * productName : W合格特约090502
                 * productStatusStr : 购买期
                 * buyStartDate : 2018-08-29 15:40:00
                 * buyEndDate : 2018-09-11 10:30:12
                 * manageStartDate : 2018-05-17
                 * manageEndDate : 2018-12-06
                 * tradeStartDate : 2018-05-23 06:00:00
                 * tradeEndDate : 2018-11-30 21:00:00
                 * currency : 人民币
                 * isTransferStr : 可转让
                 * riskLevel : 较高风险
                 * buyerSmallestAmount : 2,000
                 * buyTotalAmount : 100,000
                 * buySmallestAmount : 0
                 * buyRemainAmount : 1,000
                 * serialNoStr : PDS1363
                 * subjectTypeName : 定向融资计划
                 * prodSubType : 担保类
                 * deadline : 203
                 * expectedMaxAnnualRate : 7.20
                 * actualAnnualRate : 0.072
                 * productSubType : A
                 * isTransfer : transfer
                 * transferIsfloat : float
                 * transferIsfloatName : 浮动
                 * transferFloat : 0.001
                 * transferFloatBegin : 0
                 * transferFloatEnd : 0.072
                 * delayDays : 0
                 * status : accepted
                 * unActualPriceIncreases : 1,000
                 * unActualBuyUserLevel : 0
                 * unActualPrice : 100
                 * financeCode : 4564
                 * transferFee : 0
                 * leastHoldAmount : 2,000
                 * issueCompany : JY000007
                 * issueCompanyName : 杭州卓成
                 * opstBankCode : 广东省大多数的大幅度对法国大使馆2222
                 * opstBankAccount : 1324567876654222
                 * saleObject : 个人和机构
                 * yearDay : ACT/365
                 * mostHoldAmount : 50000
                 * mostHolderNum : 50
                 * incomeType : 2
                 * tradeLeastHoldDay : 5
                 * leastTranAmount : 1000
                 * delayCashRate : 0
                 * tradeIncrease : 1,000
                 * tradeObject : 双方
                 * isBuyBack : 0
                 * riskTest : 1
                 * cashType : 1
                 * imputType : 1
                 * collectionDestination : 1
                 * cashDestination : 1
                 * specialAmount : 50000
                 * specialCustomerNum : 10
                 * payStyle : 利随本清
                 * issueCreditLevel : 0000015006
                 * directFinanceCreditLevel : AA
                 * issueBank : LQ经过提供方法和法国风尚大典222韩国风格
                 * financeCompany : 浙江商业银行
                 * guaranteeWay : 1
                 * delayDayRate : 0
                 * holdersNum : 50
                 * specialBuyAmount : 2000
                 * specialIncreaseAmount : 1000
                 * productStartDate : 2018-05-17 00:00:00
                 * optionTriggerType : 1
                 * optionType : 1
                 * optionDateStart : 2016-11-25 00:00:00
                 * optionDateEnd : 2017-12-28 00:00:00
                 * delayDateStart : 2016-05-18 00:00:00
                 * delayDateEnd : 2016-10-27 00:00:00
                 * sfyzcbxs : 0
                 * isOver : 0
                 * tradeDept : 1006
                 * transferObject : 0
                 * productBrands : 123321
                 * payFrequencyUnit : 0000004002
                 * generalCustomerUsedNum : 1
                 * specialCustomerUsedNum : 1
                 * onlyNew : 0
                 * process : 98.00
                 * orderBuyAmount : 0
                 * canBuyNum : 39人
                 * comparison : 2
                 * incomeTypeName : 固定
                 * unActualTransferUserLevel : 0
                 * countDownSec : -2570979
                 * accreditedBuyIs : 1
                 * accreditedTransferIs : 1
                 */

                private String productCode;
                private String productName;
                private String productStatusStr;
                private String buyStartDate;
                private String buyEndDate;
                private String manageStartDate;
                private String manageEndDate;
                private String tradeStartDate;
                private String tradeEndDate;
                private String currency;
                private String isTransferStr;
                private String riskLevel;
                private String buyerSmallestAmount;
                private String buyTotalAmount;
                private String buySmallestAmount;
                private String buyRemainAmount;
                private String serialNoStr;
                private String subjectTypeName;
                private String prodSubType;
                private String deadline;
                private String expectedMaxAnnualRate;
                private String actualAnnualRate;
                private String productSubType;
                private String isTransfer;
                private String transferIsfloat;
                private String transferIsfloatName;
                private String transferFloat;
                private String transferFloatBegin;
                private String transferFloatEnd;
                private String delayDays;
                private String status;
                private String unActualPriceIncreases;
                private String unActualBuyUserLevel;
                private String unActualPrice;
                private String financeCode;
                private String transferFee;
                private String leastHoldAmount;
                private String issueCompany;
                private String issueCompanyName;
                private String opstBankCode;
                private String opstBankAccount;
                private String saleObject;
                private String yearDay;
                private String mostHoldAmount;
                private String mostHolderNum;
                private String incomeType;
                private String tradeLeastHoldDay;
                private String leastTranAmount;
                private String delayCashRate;
                private String tradeIncrease;
                private String tradeObject;
                private String isBuyBack;
                private String riskTest;
                private String cashType;
                private String imputType;
                private String collectionDestination;
                private String cashDestination;
                private String specialAmount;
                private String specialCustomerNum;
                private String payStyle;
                private String payFrequency;
                private String issueCreditLevel;
                private String directFinanceCreditLevel;
                private String issueBank;
                private String financeCompany;
                private String guaranteeWay;
                private String delayDayRate;
                private String holdersNum;
                private String specialBuyAmount;
                private String specialIncreaseAmount;
                private String productStartDate;
                private String optionTriggerType;
                private String optionType;
                private String optionDateStart;
                private String optionDateEnd;
                private String delayDateStart;
                private String delayDateEnd;
                private String sfyzcbxs;
                private String isOver;
                private String tradeDept;
                private String transferObject;
                private String productBrands;
                private String payFrequencyUnit;
                private String generalCustomerUsedNum;
                private String specialCustomerUsedNum;
                private String onlyNew;
                private String process;
                private String orderBuyAmount;
                private String canBuyNum;
                private String comparison;
                private String incomeTypeName;
                private String unActualTransferUserLevel;
                private String countDownSec;
                private String accreditedBuyIs;
                private String accreditedTransferIs;

                public String getPayFrequency() {
                    return payFrequency;
                }

                public void setPayFrequency(String payFrequency) {
                    this.payFrequency = payFrequency;
                }

                public String getProductCode() {
                    return productCode;
                }

                public void setProductCode(String productCode) {
                    this.productCode = productCode;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getProductStatusStr() {
                    return productStatusStr;
                }

                public void setProductStatusStr(String productStatusStr) {
                    this.productStatusStr = productStatusStr;
                }

                public String getBuyStartDate() {
                    return buyStartDate;
                }

                public void setBuyStartDate(String buyStartDate) {
                    this.buyStartDate = buyStartDate;
                }

                public String getBuyEndDate() {
                    return buyEndDate;
                }

                public void setBuyEndDate(String buyEndDate) {
                    this.buyEndDate = buyEndDate;
                }

                public String getManageStartDate() {
                    return manageStartDate;
                }

                public void setManageStartDate(String manageStartDate) {
                    this.manageStartDate = manageStartDate;
                }

                public String getManageEndDate() {
                    return manageEndDate;
                }

                public void setManageEndDate(String manageEndDate) {
                    this.manageEndDate = manageEndDate;
                }

                public String getTradeStartDate() {
                    return tradeStartDate;
                }

                public void setTradeStartDate(String tradeStartDate) {
                    this.tradeStartDate = tradeStartDate;
                }

                public String getTradeEndDate() {
                    return tradeEndDate;
                }

                public void setTradeEndDate(String tradeEndDate) {
                    this.tradeEndDate = tradeEndDate;
                }

                public String getCurrency() {
                    return currency;
                }

                public void setCurrency(String currency) {
                    this.currency = currency;
                }

                public String getIsTransferStr() {
                    return isTransferStr;
                }

                public void setIsTransferStr(String isTransferStr) {
                    this.isTransferStr = isTransferStr;
                }

                public String getRiskLevel() {
                    return riskLevel;
                }

                public void setRiskLevel(String riskLevel) {
                    this.riskLevel = riskLevel;
                }

                public String getBuyerSmallestAmount() {
                    return buyerSmallestAmount;
                }

                public void setBuyerSmallestAmount(String buyerSmallestAmount) {
                    this.buyerSmallestAmount = buyerSmallestAmount;
                }

                public String getBuyTotalAmount() {
                    return buyTotalAmount;
                }

                public void setBuyTotalAmount(String buyTotalAmount) {
                    this.buyTotalAmount = buyTotalAmount;
                }

                public String getBuySmallestAmount() {
                    return buySmallestAmount;
                }

                public void setBuySmallestAmount(String buySmallestAmount) {
                    this.buySmallestAmount = buySmallestAmount;
                }

                public String getBuyRemainAmount() {
                    return buyRemainAmount;
                }

                public void setBuyRemainAmount(String buyRemainAmount) {
                    this.buyRemainAmount = buyRemainAmount;
                }

                public String getSerialNoStr() {
                    return serialNoStr;
                }

                public void setSerialNoStr(String serialNoStr) {
                    this.serialNoStr = serialNoStr;
                }

                public String getSubjectTypeName() {
                    return subjectTypeName;
                }

                public void setSubjectTypeName(String subjectTypeName) {
                    this.subjectTypeName = subjectTypeName;
                }

                public String getProdSubType() {
                    return prodSubType;
                }

                public void setProdSubType(String prodSubType) {
                    this.prodSubType = prodSubType;
                }

                public String getDeadline() {
                    return deadline;
                }

                public void setDeadline(String deadline) {
                    this.deadline = deadline;
                }

                public String getExpectedMaxAnnualRate() {
                    return expectedMaxAnnualRate;
                }

                public void setExpectedMaxAnnualRate(String expectedMaxAnnualRate) {
                    this.expectedMaxAnnualRate = expectedMaxAnnualRate;
                }

                public String getActualAnnualRate() {
                    return actualAnnualRate;
                }

                public void setActualAnnualRate(String actualAnnualRate) {
                    this.actualAnnualRate = actualAnnualRate;
                }

                public String getProductSubType() {
                    return productSubType;
                }

                public void setProductSubType(String productSubType) {
                    this.productSubType = productSubType;
                }

                public String getIsTransfer() {
                    return isTransfer;
                }

                public void setIsTransfer(String isTransfer) {
                    this.isTransfer = isTransfer;
                }

                public String getTransferIsfloat() {
                    return transferIsfloat;
                }

                public void setTransferIsfloat(String transferIsfloat) {
                    this.transferIsfloat = transferIsfloat;
                }

                public String getTransferIsfloatName() {
                    return transferIsfloatName;
                }

                public void setTransferIsfloatName(String transferIsfloatName) {
                    this.transferIsfloatName = transferIsfloatName;
                }

                public String getTransferFloat() {
                    return transferFloat;
                }

                public void setTransferFloat(String transferFloat) {
                    this.transferFloat = transferFloat;
                }

                public String getTransferFloatBegin() {
                    return transferFloatBegin;
                }

                public void setTransferFloatBegin(String transferFloatBegin) {
                    this.transferFloatBegin = transferFloatBegin;
                }

                public String getTransferFloatEnd() {
                    return transferFloatEnd;
                }

                public void setTransferFloatEnd(String transferFloatEnd) {
                    this.transferFloatEnd = transferFloatEnd;
                }

                public String getDelayDays() {
                    return delayDays;
                }

                public void setDelayDays(String delayDays) {
                    this.delayDays = delayDays;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getUnActualPriceIncreases() {
                    return unActualPriceIncreases;
                }

                public void setUnActualPriceIncreases(String unActualPriceIncreases) {
                    this.unActualPriceIncreases = unActualPriceIncreases;
                }

                public String getUnActualBuyUserLevel() {
                    return unActualBuyUserLevel;
                }

                public void setUnActualBuyUserLevel(String unActualBuyUserLevel) {
                    this.unActualBuyUserLevel = unActualBuyUserLevel;
                }

                public String getUnActualPrice() {
                    return unActualPrice;
                }

                public void setUnActualPrice(String unActualPrice) {
                    this.unActualPrice = unActualPrice;
                }

                public String getFinanceCode() {
                    return financeCode;
                }

                public void setFinanceCode(String financeCode) {
                    this.financeCode = financeCode;
                }

                public String getTransferFee() {
                    return transferFee;
                }

                public void setTransferFee(String transferFee) {
                    this.transferFee = transferFee;
                }

                public String getLeastHoldAmount() {
                    return leastHoldAmount;
                }

                public void setLeastHoldAmount(String leastHoldAmount) {
                    this.leastHoldAmount = leastHoldAmount;
                }

                public String getIssueCompany() {
                    return issueCompany;
                }

                public void setIssueCompany(String issueCompany) {
                    this.issueCompany = issueCompany;
                }

                public String getIssueCompanyName() {
                    return issueCompanyName;
                }

                public void setIssueCompanyName(String issueCompanyName) {
                    this.issueCompanyName = issueCompanyName;
                }

                public String getOpstBankCode() {
                    return opstBankCode;
                }

                public void setOpstBankCode(String opstBankCode) {
                    this.opstBankCode = opstBankCode;
                }

                public String getOpstBankAccount() {
                    return opstBankAccount;
                }

                public void setOpstBankAccount(String opstBankAccount) {
                    this.opstBankAccount = opstBankAccount;
                }

                public String getSaleObject() {
                    return saleObject;
                }

                public void setSaleObject(String saleObject) {
                    this.saleObject = saleObject;
                }

                public String getYearDay() {
                    return yearDay;
                }

                public void setYearDay(String yearDay) {
                    this.yearDay = yearDay;
                }

                public String getMostHoldAmount() {
                    return mostHoldAmount;
                }

                public void setMostHoldAmount(String mostHoldAmount) {
                    this.mostHoldAmount = mostHoldAmount;
                }

                public String getMostHolderNum() {
                    return mostHolderNum;
                }

                public void setMostHolderNum(String mostHolderNum) {
                    this.mostHolderNum = mostHolderNum;
                }

                public String getIncomeType() {
                    return incomeType;
                }

                public void setIncomeType(String incomeType) {
                    this.incomeType = incomeType;
                }

                public String getTradeLeastHoldDay() {
                    return tradeLeastHoldDay;
                }

                public void setTradeLeastHoldDay(String tradeLeastHoldDay) {
                    this.tradeLeastHoldDay = tradeLeastHoldDay;
                }

                public String getLeastTranAmount() {
                    return leastTranAmount;
                }

                public void setLeastTranAmount(String leastTranAmount) {
                    this.leastTranAmount = leastTranAmount;
                }

                public String getDelayCashRate() {
                    return delayCashRate;
                }

                public void setDelayCashRate(String delayCashRate) {
                    this.delayCashRate = delayCashRate;
                }

                public String getTradeIncrease() {
                    return tradeIncrease;
                }

                public void setTradeIncrease(String tradeIncrease) {
                    this.tradeIncrease = tradeIncrease;
                }

                public String getTradeObject() {
                    return tradeObject;
                }

                public void setTradeObject(String tradeObject) {
                    this.tradeObject = tradeObject;
                }

                public String getIsBuyBack() {
                    return isBuyBack;
                }

                public void setIsBuyBack(String isBuyBack) {
                    this.isBuyBack = isBuyBack;
                }

                public String getRiskTest() {
                    return riskTest;
                }

                public void setRiskTest(String riskTest) {
                    this.riskTest = riskTest;
                }

                public String getCashType() {
                    return cashType;
                }

                public void setCashType(String cashType) {
                    this.cashType = cashType;
                }

                public String getImputType() {
                    return imputType;
                }

                public void setImputType(String imputType) {
                    this.imputType = imputType;
                }

                public String getCollectionDestination() {
                    return collectionDestination;
                }

                public void setCollectionDestination(String collectionDestination) {
                    this.collectionDestination = collectionDestination;
                }

                public String getCashDestination() {
                    return cashDestination;
                }

                public void setCashDestination(String cashDestination) {
                    this.cashDestination = cashDestination;
                }

                public String getSpecialAmount() {
                    return specialAmount;
                }

                public void setSpecialAmount(String specialAmount) {
                    this.specialAmount = specialAmount;
                }

                public String getSpecialCustomerNum() {
                    return specialCustomerNum;
                }

                public void setSpecialCustomerNum(String specialCustomerNum) {
                    this.specialCustomerNum = specialCustomerNum;
                }

                public String getPayStyle() {
                    return payStyle;
                }

                public void setPayStyle(String payStyle) {
                    this.payStyle = payStyle;
                }

                public String getIssueCreditLevel() {
                    return issueCreditLevel;
                }

                public void setIssueCreditLevel(String issueCreditLevel) {
                    this.issueCreditLevel = issueCreditLevel;
                }

                public String getDirectFinanceCreditLevel() {
                    return directFinanceCreditLevel;
                }

                public void setDirectFinanceCreditLevel(String directFinanceCreditLevel) {
                    this.directFinanceCreditLevel = directFinanceCreditLevel;
                }

                public String getIssueBank() {
                    return issueBank;
                }

                public void setIssueBank(String issueBank) {
                    this.issueBank = issueBank;
                }

                public String getFinanceCompany() {
                    return financeCompany;
                }

                public void setFinanceCompany(String financeCompany) {
                    this.financeCompany = financeCompany;
                }

                public String getGuaranteeWay() {
                    return guaranteeWay;
                }

                public void setGuaranteeWay(String guaranteeWay) {
                    this.guaranteeWay = guaranteeWay;
                }

                public String getDelayDayRate() {
                    return delayDayRate;
                }

                public void setDelayDayRate(String delayDayRate) {
                    this.delayDayRate = delayDayRate;
                }

                public String getHoldersNum() {
                    return holdersNum;
                }

                public void setHoldersNum(String holdersNum) {
                    this.holdersNum = holdersNum;
                }

                public String getSpecialBuyAmount() {
                    return specialBuyAmount;
                }

                public void setSpecialBuyAmount(String specialBuyAmount) {
                    this.specialBuyAmount = specialBuyAmount;
                }

                public String getSpecialIncreaseAmount() {
                    return specialIncreaseAmount;
                }

                public void setSpecialIncreaseAmount(String specialIncreaseAmount) {
                    this.specialIncreaseAmount = specialIncreaseAmount;
                }

                public String getProductStartDate() {
                    return productStartDate;
                }

                public void setProductStartDate(String productStartDate) {
                    this.productStartDate = productStartDate;
                }

                public String getOptionTriggerType() {
                    return optionTriggerType;
                }

                public void setOptionTriggerType(String optionTriggerType) {
                    this.optionTriggerType = optionTriggerType;
                }

                public String getOptionType() {
                    return optionType;
                }

                public void setOptionType(String optionType) {
                    this.optionType = optionType;
                }

                public String getOptionDateStart() {
                    return optionDateStart;
                }

                public void setOptionDateStart(String optionDateStart) {
                    this.optionDateStart = optionDateStart;
                }

                public String getOptionDateEnd() {
                    return optionDateEnd;
                }

                public void setOptionDateEnd(String optionDateEnd) {
                    this.optionDateEnd = optionDateEnd;
                }

                public String getDelayDateStart() {
                    return delayDateStart;
                }

                public void setDelayDateStart(String delayDateStart) {
                    this.delayDateStart = delayDateStart;
                }

                public String getDelayDateEnd() {
                    return delayDateEnd;
                }

                public void setDelayDateEnd(String delayDateEnd) {
                    this.delayDateEnd = delayDateEnd;
                }

                public String getSfyzcbxs() {
                    return sfyzcbxs;
                }

                public void setSfyzcbxs(String sfyzcbxs) {
                    this.sfyzcbxs = sfyzcbxs;
                }

                public String getIsOver() {
                    return isOver;
                }

                public void setIsOver(String isOver) {
                    this.isOver = isOver;
                }

                public String getTradeDept() {
                    return tradeDept;
                }

                public void setTradeDept(String tradeDept) {
                    this.tradeDept = tradeDept;
                }

                public String getTransferObject() {
                    return transferObject;
                }

                public void setTransferObject(String transferObject) {
                    this.transferObject = transferObject;
                }

                public String getProductBrands() {
                    return productBrands;
                }

                public void setProductBrands(String productBrands) {
                    this.productBrands = productBrands;
                }

                public String getPayFrequencyUnit() {
                    return payFrequencyUnit;
                }

                public void setPayFrequencyUnit(String payFrequencyUnit) {
                    this.payFrequencyUnit = payFrequencyUnit;
                }

                public String getGeneralCustomerUsedNum() {
                    return generalCustomerUsedNum;
                }

                public void setGeneralCustomerUsedNum(String generalCustomerUsedNum) {
                    this.generalCustomerUsedNum = generalCustomerUsedNum;
                }

                public String getSpecialCustomerUsedNum() {
                    return specialCustomerUsedNum;
                }

                public void setSpecialCustomerUsedNum(String specialCustomerUsedNum) {
                    this.specialCustomerUsedNum = specialCustomerUsedNum;
                }

                public String getOnlyNew() {
                    return onlyNew;
                }

                public void setOnlyNew(String onlyNew) {
                    this.onlyNew = onlyNew;
                }

                public String getProcess() {
                    return process;
                }

                public void setProcess(String process) {
                    this.process = process;
                }

                public String getOrderBuyAmount() {
                    return orderBuyAmount;
                }

                public void setOrderBuyAmount(String orderBuyAmount) {
                    this.orderBuyAmount = orderBuyAmount;
                }

                public String getCanBuyNum() {
                    return canBuyNum;
                }

                public void setCanBuyNum(String canBuyNum) {
                    this.canBuyNum = canBuyNum;
                }

                public String getComparison() {
                    return comparison;
                }

                public void setComparison(String comparison) {
                    this.comparison = comparison;
                }

                public String getIncomeTypeName() {
                    return incomeTypeName;
                }

                public void setIncomeTypeName(String incomeTypeName) {
                    this.incomeTypeName = incomeTypeName;
                }

                public String getUnActualTransferUserLevel() {
                    return unActualTransferUserLevel;
                }

                public void setUnActualTransferUserLevel(String unActualTransferUserLevel) {
                    this.unActualTransferUserLevel = unActualTransferUserLevel;
                }

                public String getCountDownSec() {
                    return countDownSec;
                }

                public void setCountDownSec(String countDownSec) {
                    this.countDownSec = countDownSec;
                }

                public String getAccreditedBuyIs() {
                    return accreditedBuyIs;
                }

                public void setAccreditedBuyIs(String accreditedBuyIs) {
                    this.accreditedBuyIs = accreditedBuyIs;
                }

                public String getAccreditedTransferIs() {
                    return accreditedTransferIs;
                }

                public void setAccreditedTransferIs(String accreditedTransferIs) {
                    this.accreditedTransferIs = accreditedTransferIs;
                }
            }
        }
    }
}
