package com.hundsun.zjfae.activity.productreserve.bean;

import java.util.List;

/**
 * @Description:我的预约列表实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/27 16:08
 */
public class MyReserveListBean {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"NjhRdmFiY3A2dzhkVWNlOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYq","appVersion":"1.6.49","ReqTime":"1538035397238","SMID":"WXR3MTAuZDc3ZjY2NTk3ZTVhYVlJUHV4eXc6MDAwMDEwNzEyNi9hcHAG","isShare":"1","pbname":"PBIFE_trade_queryMyOrderList","realipzjs":"115.236.162.162","userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"pageInfo":{"pageSize":"10","pageIndex":"1","pageCount":"2","totalCount":"14"},"taProductOrderDetailWrapList":[{"orderNum":"P20180905000017","orderType":"1","orderProductCode":"OD180905010","productName":"LQ201855556","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"2","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"已违约","buyStartDate":"2018-09-05 16:30:00","buyEndDate":"2018-09-19 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 16:28:5 5","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000015","orderType":"1","orderProductCode":"OD180829002","productName":"LQ20180829合格预约测试合格预约测试合格预约测试合格预约测试合格预约测试02","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.10","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:41:12","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"P20180905000013","orderType":"1","orderProductCode":"OD180829007","productName":"LQ20180829非合格预约测试02","orderBuyAmount":"2,000","depositRate":"0","freezeDeposit":"0","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:23:17","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"P20180905000012","orderType":"1","orderProductCode":"OD180829007","productName":"LQ20180829非合格预约测试02","orderBuyAmount":"1,000","depositRate":"0","freezeDeposit":"0","orderStatus":"2","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"取消预约","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:22:01","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000011","orderType":"1","orderProductCode":"OD180905006","productName":"LQ201855550","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"0","orderStatus":"2","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"取消预约","buyStartDate":"2018-09-07 00:00:00","buyEndDate":"2018-09-19 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:20:41","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000010","orderType":"1","orderProductCode":"OD180829005","productName":"LQ20180829高净值100预约测试01","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"10000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:10:11","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"1044","orderType":"0","orderProductCode":"LC18090063","productName":"W合格特约090502","orderBuyAmount":"50,000","depositRate":"0","freezeDeposit":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"--","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-09-11 10:30:12","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 14:29:40","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000009","orderType":"1","orderProductCode":"OD180718001","productName":"LQ20180625","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"10000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"1670197","remainTimeTwo":"518400","status":"预约中","buyStartDate":"2018-10-17 00:00:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 12:04:11","isDisplayBuy":"0","isDisplayCancel":"1"},{"orderNum":"1040","orderType":"0","orderProductCode":"LC18090059","productName":"W合格特约0905","orderBuyAmount":"50,000","depositRate":"0","freezeDeposit":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"1819837","status":"可购买","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-10-18 17:3 4:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 11:53:52","isDisplayBuy":"1","isDisplayCancel":"0"},{"orderNum":"P20180905000008","orderType":"1","orderProductCode":"OD180905004","productName":"W合格预约0905","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"33724597","status":"未购买","buyStartDate":"2018-09-05 11:20:00","buyEndDate":"2019-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 11:18:37","isDisplayBuy":"1","isDisplayCancel":"1"}]}}
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
         * MFID : NjhRdmFiY3A2dzhkVWNlOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjYq
         * appVersion : 1.6.49
         * ReqTime : 1538035397238
         * SMID : WXR3MTAuZDc3ZjY2NTk3ZTVhYVlJUHV4eXc6MDAwMDEwNzEyNi9hcHAG
         * isShare : 1
         * pbname : PBIFE_trade_queryMyOrderList
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
         * data : {"pageInfo":{"pageSize":"10","pageIndex":"1","pageCount":"2","totalCount":"14"},"taProductOrderDetailWrapList":[{"orderNum":"P20180905000017","orderType":"1","orderProductCode":"OD180905010","productName":"LQ201855556","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"2","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"已违约","buyStartDate":"2018-09-05 16:30:00","buyEndDate":"2018-09-19 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 16:28:5 5","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000015","orderType":"1","orderProductCode":"OD180829002","productName":"LQ20180829合格预约测试合格预约测试合格预约测试合格预约测试合格预约测试02","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.10","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:41:12","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"P20180905000013","orderType":"1","orderProductCode":"OD180829007","productName":"LQ20180829非合格预约测试02","orderBuyAmount":"2,000","depositRate":"0","freezeDeposit":"0","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:23:17","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"P20180905000012","orderType":"1","orderProductCode":"OD180829007","productName":"LQ20180829非合格预约测试02","orderBuyAmount":"1,000","depositRate":"0","freezeDeposit":"0","orderStatus":"2","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"取消预约","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:22:01","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000011","orderType":"1","orderProductCode":"OD180905006","productName":"LQ201855550","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"0","orderStatus":"2","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"取消预约","buyStartDate":"2018-09-07 00:00:00","buyEndDate":"2018-09-19 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:20:41","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000010","orderType":"1","orderProductCode":"OD180829005","productName":"LQ20180829高净值100预约测试01","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"10000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:10:11","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"1044","orderType":"0","orderProductCode":"LC18090063","productName":"W合格特约090502","orderBuyAmount":"50,000","depositRate":"0","freezeDeposit":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"--","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-09-11 10:30:12","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 14:29:40","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000009","orderType":"1","orderProductCode":"OD180718001","productName":"LQ20180625","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"10000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"1670197","remainTimeTwo":"518400","status":"预约中","buyStartDate":"2018-10-17 00:00:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 12:04:11","isDisplayBuy":"0","isDisplayCancel":"1"},{"orderNum":"1040","orderType":"0","orderProductCode":"LC18090059","productName":"W合格特约0905","orderBuyAmount":"50,000","depositRate":"0","freezeDeposit":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"1819837","status":"可购买","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-10-18 17:3 4:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 11:53:52","isDisplayBuy":"1","isDisplayCancel":"0"},{"orderNum":"P20180905000008","orderType":"1","orderProductCode":"OD180905004","productName":"W合格预约0905","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"33724597","status":"未购买","buyStartDate":"2018-09-05 11:20:00","buyEndDate":"2019-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 11:18:37","isDisplayBuy":"1","isDisplayCancel":"1"}]}
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
             * pageInfo : {"pageSize":"10","pageIndex":"1","pageCount":"2","totalCount":"14"}
             * taProductOrderDetailWrapList : [{"orderNum":"P20180905000017","orderType":"1","orderProductCode":"OD180905010","productName":"LQ201855556","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"2","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"已违约","buyStartDate":"2018-09-05 16:30:00","buyEndDate":"2018-09-19 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 16:28:5 5","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000015","orderType":"1","orderProductCode":"OD180829002","productName":"LQ20180829合格预约测试合格预约测试合格预约测试合格预约测试合格预约测试02","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.10","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:41:12","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"P20180905000013","orderType":"1","orderProductCode":"OD180829007","productName":"LQ20180829非合格预约测试02","orderBuyAmount":"2,000","depositRate":"0","freezeDeposit":"0","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:23:17","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"P20180905000012","orderType":"1","orderProductCode":"OD180829007","productName":"LQ20180829非合格预约测试02","orderBuyAmount":"1,000","depositRate":"0","freezeDeposit":"0","orderStatus":"2","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"取消预约","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:22:01","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000011","orderType":"1","orderProductCode":"OD180905006","productName":"LQ201855550","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"0","orderStatus":"2","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"取消预约","buyStartDate":"2018-09-07 00:00:00","buyEndDate":"2018-09-19 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:20:41","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000010","orderType":"1","orderProductCode":"OD180829005","productName":"LQ20180829高净值100预约测试01","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"10000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"2188597","status":"未购买","buyStartDate":"2018-09-08 10:21:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 15:10:11","isDisplayBuy":"1","isDisplayCancel":"1"},{"orderNum":"1044","orderType":"0","orderProductCode":"LC18090063","productName":"W合格特约090502","orderBuyAmount":"50,000","depositRate":"0","freezeDeposit":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"0","status":"--","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-09-11 10:30:12","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 14:29:40","isDisplayBuy":"0","isDisplayCancel":"0"},{"orderNum":"P20180905000009","orderType":"1","orderProductCode":"OD180718001","productName":"LQ20180625","orderBuyAmount":"1,000","depositRate":"0.1","freezeDeposit":"10000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"1670197","remainTimeTwo":"518400","status":"预约中","buyStartDate":"2018-10-17 00:00:00","buyEndDate":"2018-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 12:04:11","isDisplayBuy":"0","isDisplayCancel":"1"},{"orderNum":"1040","orderType":"0","orderProductCode":"LC18090059","productName":"W合格特约0905","orderBuyAmount":"50,000","depositRate":"0","freezeDeposit":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"1819837","status":"可购买","buyStartDate":"2018-08-29 15:40:00","buyEndDate":"2018-10-18 17:3 4:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 11:53:52","isDisplayBuy":"1","isDisplayCancel":"0"},{"orderNum":"P20180905000008","orderType":"1","orderProductCode":"OD180905004","productName":"W合格预约0905","orderBuyAmount":"10,000","depositRate":"0.1","freezeDeposit":"100000","orderStatus":"1","subscrebeStatus":"0","appointmentStatus":"0","productRate":"7.20","remainTimeOne":"0","remainTimeTwo":"33724597","status":"未购买","buyStartDate":"2018-09-05 11:20:00","buyEndDate":"2019-10-23 00:00:00","currentDate":"2018-09-27 16:03:23","gmtCreate":"2018-09-05 11:18:37","isDisplayBuy":"1","isDisplayCancel":"1"}]
             */

            private PageInfoBean pageInfo;
            private List<TaProductOrderDetailWrapListBean> taProductOrderDetailWrapList;

            public PageInfoBean getPageInfo() {
                return pageInfo;
            }

            public void setPageInfo(PageInfoBean pageInfo) {
                this.pageInfo = pageInfo;
            }

            public List<TaProductOrderDetailWrapListBean> getTaProductOrderDetailWrapList() {
                return taProductOrderDetailWrapList;
            }

            public void setTaProductOrderDetailWrapList(List<TaProductOrderDetailWrapListBean> taProductOrderDetailWrapList) {
                this.taProductOrderDetailWrapList = taProductOrderDetailWrapList;
            }

            public static class PageInfoBean {
                /**
                 * pageSize : 10
                 * pageIndex : 1
                 * pageCount : 2
                 * totalCount : 14
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

            public static class TaProductOrderDetailWrapListBean {
                /**
                 * orderNum : P20180905000017
                 * orderType : 1
                 * orderProductCode : OD180905010
                 * productName : LQ201855556
                 * orderBuyAmount : 10,000
                 * depositRate : 0.1
                 * freezeDeposit : 100000
                 * orderStatus : 1
                 * subscrebeStatus : 0
                 * appointmentStatus : 2
                 * productRate : 7.20
                 * remainTimeOne : 0
                 * remainTimeTwo : 0
                 * status : 已违约
                 * buyStartDate : 2018-09-05 16:30:00
                 * buyEndDate : 2018-09-19 00:00:00
                 * currentDate : 2018-09-27 16:03:23
                 * gmtCreate : 2018-09-05 16:28:5 5
                 * isDisplayBuy : 0
                 * isDisplayCancel : 0
                 */

                private String orderNum;
                private String orderType;
                private String orderProductCode;
                private String productName;
                private String orderBuyAmount;
                private String depositRate;
                private String freezeDeposit;
                private String orderStatus;
                private String subscrebeStatus;
                private String appointmentStatus;
                private String productRate;
                private String remainTimeOne;
                private String remainTimeTwo;
                private String status;
                private String buyStartDate;
                private String buyEndDate;
                private String currentDate;
                private String gmtCreate;
                private String isDisplayBuy;
                private String isDisplayCancel;

                public String getOrderNum() {
                    return orderNum;
                }

                public void setOrderNum(String orderNum) {
                    this.orderNum = orderNum;
                }

                public String getOrderType() {
                    return orderType;
                }

                public void setOrderType(String orderType) {
                    this.orderType = orderType;
                }

                public String getOrderProductCode() {
                    return orderProductCode;
                }

                public void setOrderProductCode(String orderProductCode) {
                    this.orderProductCode = orderProductCode;
                }

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getOrderBuyAmount() {
                    return orderBuyAmount;
                }

                public void setOrderBuyAmount(String orderBuyAmount) {
                    this.orderBuyAmount = orderBuyAmount;
                }

                public String getDepositRate() {
                    return depositRate;
                }

                public void setDepositRate(String depositRate) {
                    this.depositRate = depositRate;
                }

                public String getFreezeDeposit() {
                    return freezeDeposit;
                }

                public void setFreezeDeposit(String freezeDeposit) {
                    this.freezeDeposit = freezeDeposit;
                }

                public String getOrderStatus() {
                    return orderStatus;
                }

                public void setOrderStatus(String orderStatus) {
                    this.orderStatus = orderStatus;
                }

                public String getSubscrebeStatus() {
                    return subscrebeStatus;
                }

                public void setSubscrebeStatus(String subscrebeStatus) {
                    this.subscrebeStatus = subscrebeStatus;
                }

                public String getAppointmentStatus() {
                    return appointmentStatus;
                }

                public void setAppointmentStatus(String appointmentStatus) {
                    this.appointmentStatus = appointmentStatus;
                }

                public String getProductRate() {
                    return productRate;
                }

                public void setProductRate(String productRate) {
                    this.productRate = productRate;
                }

                public String getRemainTimeOne() {
                    return remainTimeOne;
                }

                public void setRemainTimeOne(String remainTimeOne) {
                    this.remainTimeOne = remainTimeOne;
                }

                public String getRemainTimeTwo() {
                    return remainTimeTwo;
                }

                public void setRemainTimeTwo(String remainTimeTwo) {
                    this.remainTimeTwo = remainTimeTwo;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
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

                public String getCurrentDate() {
                    return currentDate;
                }

                public void setCurrentDate(String currentDate) {
                    this.currentDate = currentDate;
                }

                public String getGmtCreate() {
                    return gmtCreate;
                }

                public void setGmtCreate(String gmtCreate) {
                    this.gmtCreate = gmtCreate;
                }

                public String getIsDisplayBuy() {
                    return isDisplayBuy;
                }

                public void setIsDisplayBuy(String isDisplayBuy) {
                    this.isDisplayBuy = isDisplayBuy;
                }

                public String getIsDisplayCancel() {
                    return isDisplayCancel;
                }

                public void setIsDisplayCancel(String isDisplayCancel) {
                    this.isDisplayCancel = isDisplayCancel;
                }
            }
        }
    }
}
