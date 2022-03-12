package com.hundsun.zjfae.activity.productreserve.bean;

/**
 * @Description:长期预约 短期预约详情实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/28 10:43
 */
public class ReserveListDetailBean {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"NjhidmFiY3Badm1lc2NlOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYq","appVersion":"1.6.49","ReqTime":"1538116046547","SMID":"WUdHMTAuZDc3ZjY2NTk3ZTVhYXlkNTJ5b3c6MDAwMDEwNzEyNi9hcHAl","isShare":"1","pbname":"PBIFE_trade_queryProductOrderInfoDetail","realipzjs":"122.225.201.180","userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"taProductOrderInfo":{"orderStartDate":"2018-09-21 19:42:00","orderEndDate":"2018-10-06 19:55:00","productCode":"OD180921003","isTransfer":"notTransfer","riskLevel":"较低风险","buyerSmallestAmount":"1,000","buyTotalAmount":"1000000","buyRemainAmount":"960,000.00","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","deadline":"365","unActualPriceIncreases":"1,000","unActualBuyUserLevel":"0","productSubType":"A","issueCompany":"TY000032","issueCompanyName":"糖糖机构","saleObject":"个人和机构","yearDay":"ACT/365","irstCycle":"2","mostHoldAmount":"10000","mostHolderNum":"1000","incomeType":"2","prodSubType":"组合类收益权","payStyle":"每6个月付息","payFrequency":"6个月/次","financeCompany":"对的","levelCompany":"中诚信证券评估有限公司","payFrequencyUnit":"0000004002","transferObject":"0","generalCustomerUsedNum":"0","productName":"非合格投资者预约","manageCompany":"一样","currency":"人民币","gmtCreate":"2018-09-21 20:30:59","gmtModify":"2018-09-21 20:30:59","productType":"12","subjectType":"投资收益权","productSubtitle":"asd322s2","isSynchronized":"0","depositRate":"10.00","canBuyNum":"1000人","buyEndDate":"2018-10-26 00:00:00","isTransferStr":"不可转让","accreditedBuyIs":"0"}}}
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
         * MFID : NjhidmFiY3Badm1lc2NlOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYq
         * appVersion : 1.6.49
         * ReqTime : 1538116046547
         * SMID : WUdHMTAuZDc3ZjY2NTk3ZTVhYXlkNTJ5b3c6MDAwMDEwNzEyNi9hcHAl
         * isShare : 1
         * pbname : PBIFE_trade_queryProductOrderInfoDetail
         * realipzjs : 122.225.201.180
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
         * data : {"taProductOrderInfo":{"orderStartDate":"2018-09-21 19:42:00","orderEndDate":"2018-10-06 19:55:00","productCode":"OD180921003","isTransfer":"notTransfer","riskLevel":"较低风险","buyerSmallestAmount":"1,000","buyTotalAmount":"1000000","buyRemainAmount":"960,000.00","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","deadline":"365","unActualPriceIncreases":"1,000","unActualBuyUserLevel":"0","productSubType":"A","issueCompany":"TY000032","issueCompanyName":"糖糖机构","saleObject":"个人和机构","yearDay":"ACT/365","irstCycle":"2","mostHoldAmount":"10000","mostHolderNum":"1000","incomeType":"2","prodSubType":"组合类收益权","payStyle":"每6个月付息","payFrequency":"6个月/次","financeCompany":"对的","levelCompany":"中诚信证券评估有限公司","payFrequencyUnit":"0000004002","transferObject":"0","generalCustomerUsedNum":"0","productName":"非合格投资者预约","manageCompany":"一样","currency":"人民币","gmtCreate":"2018-09-21 20:30:59","gmtModify":"2018-09-21 20:30:59","productType":"12","subjectType":"投资收益权","productSubtitle":"asd322s2","isSynchronized":"0","depositRate":"10.00","canBuyNum":"1000人","buyEndDate":"2018-10-26 00:00:00","isTransferStr":"不可转让","accreditedBuyIs":"0"}}
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
             * taProductOrderInfo : {"orderStartDate":"2018-09-21 19:42:00","orderEndDate":"2018-10-06 19:55:00","productCode":"OD180921003","isTransfer":"notTransfer","riskLevel":"较低风险","buyerSmallestAmount":"1,000","buyTotalAmount":"1000000","buyRemainAmount":"960,000.00","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","deadline":"365","unActualPriceIncreases":"1,000","unActualBuyUserLevel":"0","productSubType":"A","issueCompany":"TY000032","issueCompanyName":"糖糖机构","saleObject":"个人和机构","yearDay":"ACT/365","irstCycle":"2","mostHoldAmount":"10000","mostHolderNum":"1000","incomeType":"2","prodSubType":"组合类收益权","payStyle":"每6个月付息","payFrequency":"6个月/次","financeCompany":"对的","levelCompany":"中诚信证券评估有限公司","payFrequencyUnit":"0000004002","transferObject":"0","generalCustomerUsedNum":"0","productName":"非合格投资者预约","manageCompany":"一样","currency":"人民币","gmtCreate":"2018-09-21 20:30:59","gmtModify":"2018-09-21 20:30:59","productType":"12","subjectType":"投资收益权","productSubtitle":"asd322s2","isSynchronized":"0","depositRate":"10.00","canBuyNum":"1000人","buyEndDate":"2018-10-26 00:00:00","isTransferStr":"不可转让","accreditedBuyIs":"0"}
             */

            private TaProductOrderInfoBean taProductOrderInfo;

            public TaProductOrderInfoBean getTaProductOrderInfo() {
                return taProductOrderInfo;
            }

            public void setTaProductOrderInfo(TaProductOrderInfoBean taProductOrderInfo) {
                this.taProductOrderInfo = taProductOrderInfo;
            }

            public static class TaProductOrderInfoBean {
                /**
                 * orderStartDate : 2018-09-21 19:42:00
                 * orderEndDate : 2018-10-06 19:55:00
                 * productCode : OD180921003
                 * isTransfer : notTransfer
                 * riskLevel : 较低风险
                 * buyerSmallestAmount : 1,000
                 * buyTotalAmount : 1000000
                 * buyRemainAmount : 960,000.00
                 * expectedMaxAnnualRate : 7.20
                 * buyStartDate : 2018-10-06 19:55:00
                 * deadline : 365
                 * unActualPriceIncreases : 1,000
                 * unActualBuyUserLevel : 0
                 * productSubType : A
                 * issueCompany : TY000032
                 * issueCompanyName : 糖糖机构
                 * saleObject : 个人和机构
                 * yearDay : ACT/365
                 * irstCycle : 2
                 * mostHoldAmount : 10000
                 * mostHolderNum : 1000
                 * incomeType : 2
                 * prodSubType : 组合类收益权
                 * payStyle : 每6个月付息
                 * payFrequency : 6个月/次
                 * financeCompany : 对的
                 * levelCompany : 中诚信证券评估有限公司
                 * payFrequencyUnit : 0000004002
                 * transferObject : 0
                 * generalCustomerUsedNum : 0
                 * productName : 非合格投资者预约
                 * manageCompany : 一样
                 * currency : 人民币
                 * gmtCreate : 2018-09-21 20:30:59
                 * gmtModify : 2018-09-21 20:30:59
                 * productType : 12
                 * subjectType : 投资收益权
                 * productSubtitle : asd322s2
                 * isSynchronized : 0
                 * depositRate : 10.00
                 * canBuyNum : 1000人
                 * buyEndDate : 2018-10-26 00:00:00
                 * isTransferStr : 不可转让
                 * accreditedBuyIs : 0
                 */

                private String orderStartDate;
                private String orderEndDate;
                private String productCode;
                private String isTransfer;
                private String riskLevel;
                private String buyerSmallestAmount;
                private String buyTotalAmount;
                private String buyRemainAmount;
                private String expectedMaxAnnualRate;
                private String buyStartDate;
                private String deadline;
                private String unActualPriceIncreases;
                private String unActualBuyUserLevel;
                private String productSubType;
                private String issueCompany;
                private String issueCompanyName;
                private String saleObject;
                private String yearDay;
                private String irstCycle;
                private String mostHoldAmount;
                private String mostHolderNum;
                private String incomeType;
                private String prodSubType;
                private String payStyle;
                private String payFrequency;
                private String financeCompany;
                private String levelCompany;
                private String payFrequencyUnit;
                private String transferObject;
                private String generalCustomerUsedNum;
                private String productName;
                private String manageCompany;
                private String currency;
                private String gmtCreate;
                private String gmtModify;
                private String productType;
                private String subjectType;
                private String productSubtitle;
                private String isSynchronized;
                private String depositRate;
                private String canBuyNum;
                private String buyEndDate;
                private String isTransferStr;
                private String accreditedBuyIs;

                public String getOrderStartDate() {
                    return orderStartDate;
                }

                public void setOrderStartDate(String orderStartDate) {
                    this.orderStartDate = orderStartDate;
                }

                public String getOrderEndDate() {
                    return orderEndDate;
                }

                public void setOrderEndDate(String orderEndDate) {
                    this.orderEndDate = orderEndDate;
                }

                public String getProductCode() {
                    return productCode;
                }

                public void setProductCode(String productCode) {
                    this.productCode = productCode;
                }

                public String getIsTransfer() {
                    return isTransfer;
                }

                public void setIsTransfer(String isTransfer) {
                    this.isTransfer = isTransfer;
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

                public String getBuyRemainAmount() {
                    return buyRemainAmount;
                }

                public void setBuyRemainAmount(String buyRemainAmount) {
                    this.buyRemainAmount = buyRemainAmount;
                }

                public String getExpectedMaxAnnualRate() {
                    return expectedMaxAnnualRate;
                }

                public void setExpectedMaxAnnualRate(String expectedMaxAnnualRate) {
                    this.expectedMaxAnnualRate = expectedMaxAnnualRate;
                }

                public String getBuyStartDate() {
                    return buyStartDate;
                }

                public void setBuyStartDate(String buyStartDate) {
                    this.buyStartDate = buyStartDate;
                }

                public String getDeadline() {
                    return deadline;
                }

                public void setDeadline(String deadline) {
                    this.deadline = deadline;
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

                public String getProductSubType() {
                    return productSubType;
                }

                public void setProductSubType(String productSubType) {
                    this.productSubType = productSubType;
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

                public String getIrstCycle() {
                    return irstCycle;
                }

                public void setIrstCycle(String irstCycle) {
                    this.irstCycle = irstCycle;
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

                public String getProdSubType() {
                    return prodSubType;
                }

                public void setProdSubType(String prodSubType) {
                    this.prodSubType = prodSubType;
                }

                public String getPayStyle() {
                    return payStyle;
                }

                public void setPayStyle(String payStyle) {
                    this.payStyle = payStyle;
                }

                public String getPayFrequency() {
                    return payFrequency;
                }

                public void setPayFrequency(String payFrequency) {
                    this.payFrequency = payFrequency;
                }

                public String getFinanceCompany() {
                    return financeCompany;
                }

                public void setFinanceCompany(String financeCompany) {
                    this.financeCompany = financeCompany;
                }

                public String getLevelCompany() {
                    return levelCompany;
                }

                public void setLevelCompany(String levelCompany) {
                    this.levelCompany = levelCompany;
                }

                public String getPayFrequencyUnit() {
                    return payFrequencyUnit;
                }

                public void setPayFrequencyUnit(String payFrequencyUnit) {
                    this.payFrequencyUnit = payFrequencyUnit;
                }

                public String getTransferObject() {
                    return transferObject;
                }

                public void setTransferObject(String transferObject) {
                    this.transferObject = transferObject;
                }

                public String getGeneralCustomerUsedNum() {
                    return generalCustomerUsedNum;
                }

                public void setGeneralCustomerUsedNum(String generalCustomerUsedNum) {
                    this.generalCustomerUsedNum = generalCustomerUsedNum;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getManageCompany() {
                    return manageCompany;
                }

                public void setManageCompany(String manageCompany) {
                    this.manageCompany = manageCompany;
                }

                public String getCurrency() {
                    return currency;
                }

                public void setCurrency(String currency) {
                    this.currency = currency;
                }

                public String getGmtCreate() {
                    return gmtCreate;
                }

                public void setGmtCreate(String gmtCreate) {
                    this.gmtCreate = gmtCreate;
                }

                public String getGmtModify() {
                    return gmtModify;
                }

                public void setGmtModify(String gmtModify) {
                    this.gmtModify = gmtModify;
                }

                public String getProductType() {
                    return productType;
                }

                public void setProductType(String productType) {
                    this.productType = productType;
                }

                public String getSubjectType() {
                    return subjectType;
                }

                public void setSubjectType(String subjectType) {
                    this.subjectType = subjectType;
                }

                public String getProductSubtitle() {
                    return productSubtitle;
                }

                public void setProductSubtitle(String productSubtitle) {
                    this.productSubtitle = productSubtitle;
                }

                public String getIsSynchronized() {
                    return isSynchronized;
                }

                public void setIsSynchronized(String isSynchronized) {
                    this.isSynchronized = isSynchronized;
                }

                public String getDepositRate() {
                    return depositRate;
                }

                public void setDepositRate(String depositRate) {
                    this.depositRate = depositRate;
                }

                public String getCanBuyNum() {
                    return canBuyNum;
                }

                public void setCanBuyNum(String canBuyNum) {
                    this.canBuyNum = canBuyNum;
                }

                public String getBuyEndDate() {
                    return buyEndDate;
                }

                public void setBuyEndDate(String buyEndDate) {
                    this.buyEndDate = buyEndDate;
                }

                public String getIsTransferStr() {
                    return isTransferStr;
                }

                public void setIsTransferStr(String isTransferStr) {
                    this.isTransferStr = isTransferStr;
                }

                public String getAccreditedBuyIs() {
                    return accreditedBuyIs;
                }

                public void setAccreditedBuyIs(String accreditedBuyIs) {
                    this.accreditedBuyIs = accreditedBuyIs;
                }
            }
        }
    }
}
