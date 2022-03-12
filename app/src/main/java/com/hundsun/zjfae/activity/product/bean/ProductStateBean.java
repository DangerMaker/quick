package com.hundsun.zjfae.activity.product.bean;

public class ProductStateBean {

    /**
     * fh : VREGMZJ000000J00
     * eh : {"appVersion":"1.6.48","ZJSRANDOMID":"2eb559f4bff442f8b12940d6e1e54c29","pbname":"PBIFE_trade_subscribeProductPre","realipzjs":"122.225.201.180","ZJS_AUTH_CODE_ID":"96b9a0a5dbd741c9b52dd2c3928f407e","userid":"13400000001","platform":"android","p":"and","clientOsver":"5","MFID":"VmhvY2FiY29GaTN2b2JlOmFwcHVzZXJzbG9naW46LTAwMDAxMDQzOTcX","ReqTime":"1536146968915","SMID":"Y2o0dTEwLmQ3N2Y2NjU5N2U1YWFjOVREQ2w6MDAwMDEwNDM5Ny9hcHA6","isShare":"1","ZJS_CUSTOMER_CHECK_COOKIE":"ec0c11c5b5be471590498aae36e939f3"}
     * body : {"returnCode":"3519","returnMsg":"您当前购买的产品募集已结束。","data":{}}
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
         * appVersion : 1.6.48
         * ZJSRANDOMID : 2eb559f4bff442f8b12940d6e1e54c29
         * pbname : PBIFE_trade_subscribeProductPre
         * realipzjs : 122.225.201.180
         * ZJS_AUTH_CODE_ID : 96b9a0a5dbd741c9b52dd2c3928f407e
         * userid : 13400000001
         * platform : android
         * p : and
         * clientOsver : 5
         * MFID : VmhvY2FiY29GaTN2b2JlOmFwcHVzZXJzbG9naW46LTAwMDAxMDQzOTcX
         * ReqTime : 1536146968915
         * SMID : Y2o0dTEwLmQ3N2Y2NjU5N2U1YWFjOVREQ2w6MDAwMDEwNDM5Ny9hcHA6
         * isShare : 1
         * ZJS_CUSTOMER_CHECK_COOKIE : ec0c11c5b5be471590498aae36e939f3
         */

        private String appVersion;
        private String ZJSRANDOMID;
        private String pbname;
        private String realipzjs;
        private String ZJS_AUTH_CODE_ID;
        private String userid;
        private String platform;
        private String p;
        private String clientOsver;
        private String MFID;
        private String ReqTime;
        private String SMID;
        private String isShare;
        private String ZJS_CUSTOMER_CHECK_COOKIE;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getZJSRANDOMID() {
            return ZJSRANDOMID;
        }

        public void setZJSRANDOMID(String ZJSRANDOMID) {
            this.ZJSRANDOMID = ZJSRANDOMID;
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

        public String getZJS_AUTH_CODE_ID() {
            return ZJS_AUTH_CODE_ID;
        }

        public void setZJS_AUTH_CODE_ID(String ZJS_AUTH_CODE_ID) {
            this.ZJS_AUTH_CODE_ID = ZJS_AUTH_CODE_ID;
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

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }

        public String getClientOsver() {
            return clientOsver;
        }

        public void setClientOsver(String clientOsver) {
            this.clientOsver = clientOsver;
        }

        public String getMFID() {
            return MFID;
        }

        public void setMFID(String MFID) {
            this.MFID = MFID;
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

        public String getZJS_CUSTOMER_CHECK_COOKIE() {
            return ZJS_CUSTOMER_CHECK_COOKIE;
        }

        public void setZJS_CUSTOMER_CHECK_COOKIE(String ZJS_CUSTOMER_CHECK_COOKIE) {
            this.ZJS_CUSTOMER_CHECK_COOKIE = ZJS_CUSTOMER_CHECK_COOKIE;
        }
    }

    public static class BodyBean {
        /**
         * returnCode : 3519
         * returnMsg : 您当前购买的产品募集已结束。
         * data : {}
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
        }
    }
}
