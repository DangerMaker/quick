package com.hundsun.zjfae.activity.productreserve.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @Description:长期预约 短期预约实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/28 10:43
 */
public class ReserveListBean {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"NmM5dWFiY3BWT0ZWNXplOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYK","appVersion":"1.6.49","ReqTime":"1538103946158","SMID":"WTkxZzEwLmQ3N2Y2NjU5N2U1YWE2SUE1S3V3OjAwMDAxMDcxMjYvYXBwK","isShare":"1","pbname":"PBIFE_trade_queryProductOrderInfo","realipzjs":"115.236.162.162","Set-Cookie":["MFID=NmM5dWFiY3BWT0ZWNXplOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYK;Domain=zjfae.com;Path=/;HttpOnly;"],"userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"pageInfo":{"pageSize":"10","pageIndex":"1","pageCount":"1","totalCount":"3"},"taProductOrderInfoWrapList":[{"orderEndDate":"2018-10-06","productCode":"OD180921003","buyerSmallestAmount":"1,000","buyRemainAmount":"960000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","productName":"非合格投资者预约","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"4.00"},{"orderEndDate":"2018-10-06","productCode":"OD180921004","buyerSmallestAmount":"1,000","buyRemainAmount":"960000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","productName":"合格投资者预约","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"4.00"},{"orderEndDate":"2018-10-06","productCode":"OD180926001","buyerSmallestAmount":"1,000","buyRemainAmount":"1000000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 12:09:00","productName":"LQ20180926非合格预约测试01","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"0.00"}]}}
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
         * MFID : NmM5dWFiY3BWT0ZWNXplOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYK
         * appVersion : 1.6.49
         * ReqTime : 1538103946158
         * SMID : WTkxZzEwLmQ3N2Y2NjU5N2U1YWE2SUE1S3V3OjAwMDAxMDcxMjYvYXBwK
         * isShare : 1
         * pbname : PBIFE_trade_queryProductOrderInfo
         * realipzjs : 115.236.162.162
         * Set-Cookie : ["MFID=NmM5dWFiY3BWT0ZWNXplOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYK;Domain=zjfae.com;Path=/;HttpOnly;"]
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
        @SerializedName("Set-Cookie")
        private List<String> SetCookie;

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

        public List<String> getSetCookie() {
            return SetCookie;
        }

        public void setSetCookie(List<String> SetCookie) {
            this.SetCookie = SetCookie;
        }
    }

    public static class BodyBean {
        /**
         * returnCode : 0000
         * returnMsg : 操作通过
         * data : {"pageInfo":{"pageSize":"10","pageIndex":"1","pageCount":"1","totalCount":"3"},"taProductOrderInfoWrapList":[{"orderEndDate":"2018-10-06","productCode":"OD180921003","buyerSmallestAmount":"1,000","buyRemainAmount":"960000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","productName":"非合格投资者预约","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"4.00"},{"orderEndDate":"2018-10-06","productCode":"OD180921004","buyerSmallestAmount":"1,000","buyRemainAmount":"960000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","productName":"合格投资者预约","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"4.00"},{"orderEndDate":"2018-10-06","productCode":"OD180926001","buyerSmallestAmount":"1,000","buyRemainAmount":"1000000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 12:09:00","productName":"LQ20180926非合格预约测试01","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"0.00"}]}
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
             * pageInfo : {"pageSize":"10","pageIndex":"1","pageCount":"1","totalCount":"3"}
             * taProductOrderInfoWrapList : [{"orderEndDate":"2018-10-06","productCode":"OD180921003","buyerSmallestAmount":"1,000","buyRemainAmount":"960000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","productName":"非合格投资者预约","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"4.00"},{"orderEndDate":"2018-10-06","productCode":"OD180921004","buyerSmallestAmount":"1,000","buyRemainAmount":"960000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 19:55:00","productName":"合格投资者预约","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"4.00"},{"orderEndDate":"2018-10-06","productCode":"OD180926001","buyerSmallestAmount":"1,000","buyRemainAmount":"1000000","expectedMaxAnnualRate":"7.20","buyStartDate":"2018-10-06 12:09:00","productName":"LQ20180926非合格预约测试01","depositRate":"0.1","isOrderFlag":"0","ifCanOrder":"true","process":"0.00"}]
             */

            private PageInfoBean pageInfo;
            private List<TaProductOrderInfoWrapListBean> taProductOrderInfoWrapList;

            public PageInfoBean getPageInfo() {
                return pageInfo;
            }

            public void setPageInfo(PageInfoBean pageInfo) {
                this.pageInfo = pageInfo;
            }

            public List<TaProductOrderInfoWrapListBean> getTaProductOrderInfoWrapList() {
                return taProductOrderInfoWrapList;
            }

            public void setTaProductOrderInfoWrapList(List<TaProductOrderInfoWrapListBean> taProductOrderInfoWrapList) {
                this.taProductOrderInfoWrapList = taProductOrderInfoWrapList;
            }

            public static class PageInfoBean {
                /**
                 * pageSize : 10
                 * pageIndex : 1
                 * pageCount : 1
                 * totalCount : 3
                 */

                private String pageSize;
                private String pageIndex;
                private String pageCount;
                private String totalCount;

                public String getPageSize() {
                    return pageSize;
                }

                public void setPageSize(String pageSize) {
                    this.pageSize = pageSize;
                }

                public String getPageIndex() {
                    return pageIndex;
                }

                public void setPageIndex(String pageIndex) {
                    this.pageIndex = pageIndex;
                }

                public String getPageCount() {
                    return pageCount;
                }

                public void setPageCount(String pageCount) {
                    this.pageCount = pageCount;
                }

                public String getTotalCount() {
                    return totalCount;
                }

                public void setTotalCount(String totalCount) {
                    this.totalCount = totalCount;
                }
            }

            public static class TaProductOrderInfoWrapListBean {
                /**
                 * orderEndDate : 2018-10-06
                 * productCode : OD180921003
                 * buyerSmallestAmount : 1,000
                 * buyRemainAmount : 960000
                 * expectedMaxAnnualRate : 7.20
                 * buyStartDate : 2018-10-06 19:55:00
                 * productName : 非合格投资者预约
                 * depositRate : 0.1
                 * isOrderFlag : 0
                 * ifCanOrder : true
                 * process : 4.00
                 */

                private String orderEndDate;
                private String productCode;
                private String buyerSmallestAmount;
                private String buyRemainAmount;
                private String expectedMaxAnnualRate;
                private String buyStartDate;
                private String productName;
                private String depositRate;
                private String isOrderFlag;
                private String ifCanOrder;
                private String process;

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

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getDepositRate() {
                    return depositRate;
                }

                public void setDepositRate(String depositRate) {
                    this.depositRate = depositRate;
                }

                public String getIsOrderFlag() {
                    return isOrderFlag;
                }

                public void setIsOrderFlag(String isOrderFlag) {
                    this.isOrderFlag = isOrderFlag;
                }

                public String getIfCanOrder() {
                    return ifCanOrder;
                }

                public void setIfCanOrder(String ifCanOrder) {
                    this.ifCanOrder = ifCanOrder;
                }

                public String getProcess() {
                    return process;
                }

                public void setProcess(String process) {
                    this.process = process;
                }
            }
        }
    }
}
