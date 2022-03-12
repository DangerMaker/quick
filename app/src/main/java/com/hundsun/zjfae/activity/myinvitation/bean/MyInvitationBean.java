package com.hundsun.zjfae.activity.myinvitation.bean;

/**
 * @Description:我的邀请实体类
 * @Author: zhoujianyu
 * @Time: 2018/9/28 15:19
 */
public class MyInvitationBean {


    /**
     * fh : VREGMZJ000000J00
     * eh : {"p":"and","MFID":"NlVjbWFiY3B4NlRVQnZlOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjY7","appVersion":"1.6.49","ReqTime":"1538123221905","SMID":"WXAybzEwLmQ3N2Y2NjU5N2U1YWFmeThkc2F3OjAwMDAxMDcxMjYvYXBwW","isShare":"1","pbname":"PBIFE_friendsrecommend_userRecommendInfo","realipzjs":"115.236.162.162","userid":"18012340000","platform":"android"}
     * body : {"returnCode":"0000","returnMsg":"操作通过","data":{"registNum":"42","bindCardNum":"32","investNum":"14","info1":"独乐乐不如众乐乐，自己赚钱不如大家一起赚！如果您已经信赖\u201c浙金中心\u201d这个app互联网金融平台，那么就赶紧告诉您的亲朋好友吧！带领大家一同到达安全高效理财的彼岸！体验互联网金融的魅力。11112","info2":"扫一扫，立即分享给好友1112"}}
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
         * MFID : NlVjbWFiY3B4NlRVQnZlOmFwcHVzZXJzbG9naW46LTAwMDAxMDcxMjY7
         * appVersion : 1.6.49
         * ReqTime : 1538123221905
         * SMID : WXAybzEwLmQ3N2Y2NjU5N2U1YWFmeThkc2F3OjAwMDAxMDcxMjYvYXBwW
         * isShare : 1
         * pbname : PBIFE_friendsrecommend_userRecommendInfo
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
         * data : {"registNum":"42","bindCardNum":"32","investNum":"14","info1":"独乐乐不如众乐乐，自己赚钱不如大家一起赚！如果您已经信赖\u201c浙金中心\u201d这个app互联网金融平台，那么就赶紧告诉您的亲朋好友吧！带领大家一同到达安全高效理财的彼岸！体验互联网金融的魅力。11112","info2":"扫一扫，立即分享给好友1112"}
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
             * registNum : 42
             * bindCardNum : 32
             * investNum : 14
             * info1 : 独乐乐不如众乐乐，自己赚钱不如大家一起赚！如果您已经信赖“浙金中心”这个app互联网金融平台，那么就赶紧告诉您的亲朋好友吧！带领大家一同到达安全高效理财的彼岸！体验互联网金融的魅力。11112
             * info2 : 扫一扫，立即分享给好友1112
             */

            private String registNum;
            private String bindCardNum;
            private String investNum;
            private String info1;
            private String info2;

            public String getRegistNum() {
                return registNum;
            }

            public void setRegistNum(String registNum) {
                this.registNum = registNum;
            }

            public String getBindCardNum() {
                return bindCardNum;
            }

            public void setBindCardNum(String bindCardNum) {
                this.bindCardNum = bindCardNum;
            }

            public String getInvestNum() {
                return investNum;
            }

            public void setInvestNum(String investNum) {
                this.investNum = investNum;
            }

            public String getInfo1() {
                return info1;
            }

            public void setInfo1(String info1) {
                this.info1 = info1;
            }

            public String getInfo2() {
                return info2;
            }

            public void setInfo2(String info2) {
                this.info2 = info2;
            }
        }
    }
}
