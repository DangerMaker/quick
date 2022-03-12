package com.hundsun.zjfae.activity.home.bean;

import java.util.List;

public class MessageIconBean {

    /**
     * fh : VICOAZJ000000J00
     * eh : {"p":"and","clientOsver":"","MFID":"VjRZdWFiY29OZnp5Y2J2ZTphcHB1c2Vyc2xvZ2luOi0wMDAwMTA0Mzk3d","appVersion":"1.6.48","ReqTime":"1535458854184","SMID":"Y1E5ejEwLmQ3N2Y2NjU5N2U1YWE1djJWeFB6ZTowMDAwMTA0Mzk3L2FwcAj","isShare":"1","realipzjs":"124.160.56.90","ZJS_CUSTOMER_CHECK_COOKIE":"ec0c11c5b5be471590498aae36e939f3","ZJS_AUTH_CODE_ID":"09317a28fae14d12abc40e33307b5263","userid":"13400000001","platform":"win32"}
     * body : {"icons_list":[{"uuid":"35","icons_name":"我的消息","icons_type":"1007","icons_address":"http://10.18.13.141:8181/ife/static/我的消息14999396472181501826544356.png","status":"1","icons_location":"messageCenter","keyword":"我的消息","icons_weights":"3.000000","remark":"0","icons_position":"left","link_keyword_name":"我的消息","link_location":"myMessage","is_share":"0","link_login_flag":"1"},{"uuid":"33","icons_name":"互动交流","icons_type":"1007","icons_address":"http://10.18.13.141:8181/ife/static/互动交流14999396334171513867654734.png","status":"1","icons_location":"messageCenter","keyword":"互动交流","icons_weights":"2.000000","icons_position":"left","icons_link":"http://testwx2.zjfae.com/webkx/im/groupChat.php","link_keyword_name":"互动交流","is_share":"0","link_login_flag":"1"},{"uuid":"34","icons_name":"在线客服","icons_type":"1007","icons_address":"http://10.18.13.141:8181/ife/static/在线客服1499939641163.png","status":"1","icons_location":"messageCenter","keyword":"在线客服","icons_weights":"1.000000","icons_position":"left","icons_link":"http://bingxingwp.zjfae.com/webkx/im/chat.php","link_keyword_name":"在线客服","is_share":"0","link_login_flag":"0"}],"returnCode":"0000","returnMsg":"succ"}
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
         * clientOsver :
         * MFID : VjRZdWFiY29OZnp5Y2J2ZTphcHB1c2Vyc2xvZ2luOi0wMDAwMTA0Mzk3d
         * appVersion : 1.6.48
         * ReqTime : 1535458854184
         * SMID : Y1E5ejEwLmQ3N2Y2NjU5N2U1YWE1djJWeFB6ZTowMDAwMTA0Mzk3L2FwcAj
         * isShare : 1
         * realipzjs : 124.160.56.90
         * ZJS_CUSTOMER_CHECK_COOKIE : ec0c11c5b5be471590498aae36e939f3
         * ZJS_AUTH_CODE_ID : 09317a28fae14d12abc40e33307b5263
         * userid : 13400000001
         * platform : win32
         */

        private String p;
        private String clientOsver;
        private String MFID;
        private String appVersion;
        private String ReqTime;
        private String SMID;
        private String isShare;
        private String realipzjs;
        private String ZJS_CUSTOMER_CHECK_COOKIE;
        private String ZJS_AUTH_CODE_ID;
        private String userid;
        private String platform;

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

        public String getRealipzjs() {
            return realipzjs;
        }

        public void setRealipzjs(String realipzjs) {
            this.realipzjs = realipzjs;
        }

        public String getZJS_CUSTOMER_CHECK_COOKIE() {
            return ZJS_CUSTOMER_CHECK_COOKIE;
        }

        public void setZJS_CUSTOMER_CHECK_COOKIE(String ZJS_CUSTOMER_CHECK_COOKIE) {
            this.ZJS_CUSTOMER_CHECK_COOKIE = ZJS_CUSTOMER_CHECK_COOKIE;
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
    }

    public static class BodyBean {
        /**
         * icons_list : [{"uuid":"35","icons_name":"我的消息","icons_type":"1007","icons_address":"http://10.18.13.141:8181/ife/static/我的消息14999396472181501826544356.png","status":"1","icons_location":"messageCenter","keyword":"我的消息","icons_weights":"3.000000","remark":"0","icons_position":"left","link_keyword_name":"我的消息","link_location":"myMessage","is_share":"0","link_login_flag":"1"},{"uuid":"33","icons_name":"互动交流","icons_type":"1007","icons_address":"http://10.18.13.141:8181/ife/static/互动交流14999396334171513867654734.png","status":"1","icons_location":"messageCenter","keyword":"互动交流","icons_weights":"2.000000","icons_position":"left","icons_link":"http://testwx2.zjfae.com/webkx/im/groupChat.php","link_keyword_name":"互动交流","is_share":"0","link_login_flag":"1"},{"uuid":"34","icons_name":"在线客服","icons_type":"1007","icons_address":"http://10.18.13.141:8181/ife/static/在线客服1499939641163.png","status":"1","icons_location":"messageCenter","keyword":"在线客服","icons_weights":"1.000000","icons_position":"left","icons_link":"http://bingxingwp.zjfae.com/webkx/im/chat.php","link_keyword_name":"在线客服","is_share":"0","link_login_flag":"0"}]
         * returnCode : 0000
         * returnMsg : succ
         */

        private String returnCode;
        private String returnMsg;
        private List<IconsListBean> icons_list;

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

        public List<IconsListBean> getIcons_list() {
            return icons_list;
        }

        public void setIcons_list(List<IconsListBean> icons_list) {
            this.icons_list = icons_list;
        }

        public static class IconsListBean {
            /**
             * uuid : 35
             * icons_name : 我的消息
             * icons_type : 1007
             * icons_address : http://10.18.13.141:8181/ife/static/我的消息14999396472181501826544356.png
             * status : 1
             * icons_location : messageCenter
             * keyword : 我的消息
             * icons_weights : 3.000000
             * remark : 0
             * icons_position : left
             * link_keyword_name : 我的消息
             * link_location : myMessage
             * is_share : 0
             * link_login_flag : 1
             * icons_link : http://testwx2.zjfae.com/webkx/im/groupChat.php
             */

            private String uuid;
            private String icons_name;
            private String icons_type;
            private String icons_address;
            private String status;
            private String icons_location;
            private String keyword;
            private String icons_weights;
            private String remark;
            private String icons_position;
            private String link_keyword_name;
            private String link_location;
            private String is_share;
            private String link_login_flag;
            private String icons_link;

            public String getUuid() {
                return uuid;
            }

            public void setUuid(String uuid) {
                this.uuid = uuid;
            }

            public String getIcons_name() {
                return icons_name;
            }

            public void setIcons_name(String icons_name) {
                this.icons_name = icons_name;
            }

            public String getIcons_type() {
                return icons_type;
            }

            public void setIcons_type(String icons_type) {
                this.icons_type = icons_type;
            }

            public String getIcons_address() {
                return icons_address;
            }

            public void setIcons_address(String icons_address) {
                this.icons_address = icons_address;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getIcons_location() {
                return icons_location;
            }

            public void setIcons_location(String icons_location) {
                this.icons_location = icons_location;
            }

            public String getKeyword() {
                return keyword;
            }

            public void setKeyword(String keyword) {
                this.keyword = keyword;
            }

            public String getIcons_weights() {
                return icons_weights;
            }

            public void setIcons_weights(String icons_weights) {
                this.icons_weights = icons_weights;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getIcons_position() {
                return icons_position;
            }

            public void setIcons_position(String icons_position) {
                this.icons_position = icons_position;
            }

            public String getLink_keyword_name() {
                return link_keyword_name;
            }

            public void setLink_keyword_name(String link_keyword_name) {
                this.link_keyword_name = link_keyword_name;
            }

            public String getLink_location() {
                return link_location;
            }

            public void setLink_location(String link_location) {
                this.link_location = link_location;
            }

            public String getIs_share() {
                return is_share;
            }

            public void setIs_share(String is_share) {
                this.is_share = is_share;
            }

            public String getLink_login_flag() {
                return link_login_flag;
            }

            public void setLink_login_flag(String link_login_flag) {
                this.link_login_flag = link_login_flag;
            }

            public String getIcons_link() {
                return icons_link;
            }

            public void setIcons_link(String icons_link) {
                this.icons_link = icons_link;
            }
        }
    }
}
